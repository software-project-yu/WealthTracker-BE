package com.WealthTracker.demo.service.income_expend;

import com.WealthTracker.demo.DTO.income_expend.*;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.domain.CategoryExpend;
import com.WealthTracker.demo.domain.Expend;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.enums.Asset;
import com.WealthTracker.demo.enums.Category_Expend;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.repository.ExpendCategoryRepository;
import com.WealthTracker.demo.repository.ExpendRepository;
import com.WealthTracker.demo.repository.UserRepository;
import com.WealthTracker.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class ExpendServiceImpl implements ExpendService {
    private final ExpendRepository expendRepository;
    private final ExpendCategoryRepository expendCategoryRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public Long writeExpend(ExpendRequestDTO expendRequestDTO, String token) {
        if (!jwtUtil.validationToken(token)) {
            return -1L;
        }
        //카테고리명 변경
        String category = expendRequestDTO.getCategory();
        Category_Expend convertToCategory = Category_Expend.fromString(category);

        //변환결과 예외처리
        if(convertToCategory==null){
            throw new CustomException(ErrorCode.INVALID_CATEGORY);
        }

        String asset = expendRequestDTO.getAsset();
        Asset convertToAsset = Asset.fromString(asset);

        //카테고리 저장-기존 카테고리 존재 여부 확인
       CategoryExpend categoryExpend =
                expendCategoryRepository.findByCategoryName(convertToCategory)
                        .orElseGet(() -> {
                            com.WealthTracker.demo.domain.CategoryExpend newCategory = com.WealthTracker.demo.domain.CategoryExpend
                                    .builder()
                                    .categoryName(convertToCategory)
                                    .build();
                            return expendCategoryRepository.save(newCategory);
                        });
        //지출 객체 저장
        Expend expend = Expend.builder()
                .expendDate(LocalDate.parse(expendRequestDTO.getExpendDate()).atStartOfDay())
                .expendName(expendRequestDTO.getExpendName())
                .categoryExpend(categoryExpend)
                .asset(convertToAsset)
                .cost(expendRequestDTO.getCost())
                .user(userRepository.findByUserId(jwtUtil.getUserId(token)).orElseThrow(
                        () -> new CustomException(ErrorCode.USER_NOT_FOUND)
                ))
                .build();
        expendRepository.save(expend);

        return expend.getExpendId();
    }


    @Override
    public List<ExpendResponseDTO> expendList(String token) {
        /*유저정보로 지출 모두 반환*/

        //유저 정보 가져오기
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        List<Expend> expendList = expendRepository.findAllByUserWithCategory(user.orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        ));

        //지출 카테고리 정보 가져오기
        Map<Long, CategoryExpend> categoryExpendMap = expendCategoryRepository.findAllById(
                        expendList.stream()
                                .map(Expend::getCategoryExpend)
                                .map(CategoryExpend::getCategoryId)
                                .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(
                        CategoryExpend::getCategoryId,
                        Function.identity()));


        return expendList.stream()
                .map(expend -> mapToExpendResponseDTO(expend,categoryExpendMap))
                .collect(Collectors.toList());
    }
    //DTO로 매핑하는 함수
    private ExpendResponseDTO mapToExpendResponseDTO(Expend expend, Map<Long, com.WealthTracker.demo.domain.CategoryExpend> categoryExpendMap) {
        CategoryExpend categoryExpend = categoryExpendMap.get(expend.getCategoryExpend().getCategoryId());

        // 카테고리명 한글로 변환
        String convertedCategory = (categoryExpend != null) ? Category_Expend.toString(categoryExpend.getCategoryName()): null;

        return ExpendResponseDTO.builder()
                .expendId(expend.getExpendId())
                .expendDate(expend.getExpendDate().toString().substring(0,10))
                .expendName(expend.getExpendName())
                .asset(Asset.toString(expend.getAsset()))
                .cost(expend.getCost())
                .category(convertedCategory)
                .build();
    }
    //지출 상세 내역 리턴
    @Override
    public ExpendResponseDTO expendResponseDetail(String token, Long expendId) {
        //토큰 검증
        //유저 정보 가져오기
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        User myUser=user.orElseThrow(
                ()->new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        //지출아이디로 지출 내역 찾기
        Expend findExpend=expendRepository.findByExpendId(expendId).orElseThrow(
                ()->new CustomException(ErrorCode.EXPEND_NOT_FOUND)
        );

        //유저의 지출 내역인지 확인
        if(!Objects.equals(findExpend.getUser().getUserId(), myUser.getUserId())){
            throw  new CustomException(ErrorCode.USER_NOT_CORRECT);
        }


        //카테고리 변환
        Category_Expend categoryExpend=findExpend.getCategoryExpend().getCategoryName();
        String convertCategory=Category_Expend.toString(categoryExpend);


        return ExpendResponseDTO
                .builder()
                .expendId(findExpend.getExpendId())
                .expendDate(findExpend.getExpendDate().toString().substring(0,10))
                .asset(Asset.toString(findExpend.getAsset()))
                .expendName(findExpend.getExpendName())
                .cost(findExpend.getCost())
                .category(convertCategory)
                .build();
    }


    @Override
    public List<ExpendCategoryAmountDTO> expendCategoryResponse(String token, String category) {
        return null;
    }

    @Override
    @Transactional
    public Long updateExpend(String token, Long expendId,ExpendRequestDTO expendRequestDTO) {
        // 유저 정보 가져오기
        User myUser = userRepository.findByUserId(jwtUtil.getUserId(token))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 지출아이디로 지출 내역 찾기
        Expend findExpend = expendRepository.findByExpendId(expendId)
                .orElse(null);
        if(findExpend==null){
            throw new CustomException(ErrorCode.EXPEND_NOT_FOUND);
        }

        // 유저의 지출 내역인지 확인
        if (!Objects.equals(findExpend.getUser().getUserId(), myUser.getUserId())) {
            throw new CustomException(ErrorCode.USER_NOT_CORRECT);
        }

        // 카테고리 ENUM으로 변환
        Category_Expend newCategoryExpend = Category_Expend.fromString(expendRequestDTO.getCategory());
        if (newCategoryExpend == null) {
            throw new CustomException(ErrorCode.INVALID_CATEGORY);
        }

        // 새로운 카테고리 객체 찾기 또는 생성
        CategoryExpend categoryExpendToUpdate = expendCategoryRepository.findByCategoryName(newCategoryExpend)
                .orElseGet(() -> new CategoryExpend(null, new ArrayList<>(), newCategoryExpend));



        //지출 내역수정
        Expend updateExpend=findExpend.toBuilder()
                .expendName(expendRequestDTO.getExpendName())
                .expendDate(LocalDateTime.parse(expendRequestDTO.getExpendDate()+"T00:00"))
                .asset(Asset.fromString(expendRequestDTO.getAsset()))
                .cost(expendRequestDTO.getCost())
                .categoryExpend(categoryExpendToUpdate)
                .build();

        //레포지토리에 수정
        expendRepository.save(updateExpend);
        return updateExpend.getExpendId();
    }

    @Override
    @Transactional
    public Long deleteExpend(String token, Long expendId) {
        //유저 정보 가져오기
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        User myUser=user.orElseThrow(
                ()->new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        //지출아이디로 지출 내역 찾기
        Expend findExpend=expendRepository.findByExpendId(expendId).orElseThrow(
                ()->new CustomException(ErrorCode.EXPEND_NOT_FOUND)
        );

        //유저의 지출 내역인지 확인
        if(!Objects.equals(findExpend.getUser().getUserId(), myUser.getUserId())){
            throw  new CustomException(ErrorCode.USER_NOT_CORRECT);
        }

        expendRepository.deleteById(findExpend.getExpendId());
        return 1L;
    }

    //최근 지출내역 5개 가져오기
    @Override
    public List<ExpendResponseDTO> getRecentExpend(String token) {
        //jwt토큰 검증 실시
        Optional<User> findUser = userRepository.findByUserId(jwtUtil.getUserId(token));
        User user = findUser.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //유저의 최신 지출 내역 5개 불러오기
        List<Expend> recentExpendList = expendRepository.findRecentExpend(
                 PageRequest.of(0, 5)).orElseThrow(
                ()->new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
        );

        //지출 카테고리 정보 가져오기
        Map<Long, CategoryExpend> categoryExpendMap = expendCategoryRepository.findAllById(
                        recentExpendList.stream()
                                .map(Expend::getCategoryExpend)
                                .map(CategoryExpend::getCategoryId)
                                .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(
                        CategoryExpend::getCategoryId,
                        Function.identity()));


        return recentExpendList.stream()
                .map(expend -> mapToExpendResponseDTO(expend,categoryExpendMap))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpendDateResponseDTO> getAmountByWeek(String token) {
        //jwt토큰 검증 실시
        Optional<User> findUser = userRepository.findByUserId(jwtUtil.getUserId(token));
        User user = findUser.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //이번달 불러오기
        int nowMonth=LocalDate.now().getMonthValue();

        //저번달
        int prevMonth=nowMonth-1;
        if(prevMonth==0){
            prevMonth=12;
        }

        //주차별 총 지출금액 가져오기
        List<Object[]> nowMonthData=expendRepository.getTotalExpendThisMonth(user);
        List<Object[]> prevMonthData=expendRepository.getTotalExpendLastMonth(user);

        List<ExpendDateResponseDTO> graphReport=new ArrayList<>();
        Map<Integer,Integer> currentMonthMap=nowMonthData.stream()
                .collect(Collectors.toMap(
                        o->((Number) o[0]).intValue(),
                        o->((Number) o[1]).intValue()
                ));

        Map<Integer,Integer> prevMonthMap=prevMonthData.stream()
                .collect(Collectors.toMap(
                        o->((Number) o[0]).intValue(),
                        o->((Number) o[1]).intValue()
                ));
        for(int week=1;week<=5;week++){
            ExpendDateResponseDTO dto=ExpendDateResponseDTO.builder()
                    .month(nowMonth)
                    .weekNum(week)
                    .thisWeekTotalCost(currentMonthMap.getOrDefault(week,0))
                    .lastWeekTotalCost(prevMonthMap.getOrDefault(week,0))
                    .build();
            graphReport.add(dto);
        }
        return graphReport;
    }
}