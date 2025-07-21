package com.WealthTracker.demo.domain.expend.service;

import com.WealthTracker.demo.global.constants.ErrorCode;
import com.WealthTracker.demo.domain.expend.dto.*;
import com.WealthTracker.demo.domain.expend.entity.CategoryExpend;
import com.WealthTracker.demo.domain.expend.entity.Expend;
import com.WealthTracker.demo.domain.user.entity.User;
import com.WealthTracker.demo.domain.category.enums.Asset;
import com.WealthTracker.demo.domain.category.enums.Category_Expend;
import com.WealthTracker.demo.global.error.CustomException;
import com.WealthTracker.demo.domain.category.repository.ExpendCategoryRepository;
import com.WealthTracker.demo.domain.expend.repository.ExpendRepository;
import com.WealthTracker.demo.domain.user.repository.UserRepository;
import com.WealthTracker.demo.global.lock.ExpendCategoryNamedLockFacade;
import com.WealthTracker.demo.global.util.JwtUtil;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ExpendServiceImpl implements ExpendService {
    private final ExpendRepository expendRepository;
    private final ExpendCategoryRepository expendCategoryRepository;
    private final UserRepository userRepository;
    private final ExpendCategoryNamedLockFacade lockFacade;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long writeExpend(ExpendRequestDTO expendRequestDTO, String token) {
        if (!jwtUtil.validationToken(token)) {
            return -1L;
        }
        //카테고리명 변경
        String category = expendRequestDTO.getCategory();

        //변환결과 예외처리
        if (category == null || category.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_CATEGORY,ErrorCode.INVALID_CATEGORY.getMessage());
        }

        String asset = expendRequestDTO.getAsset();
        Asset convertToAsset = Asset.fromString(asset);
        if(convertToAsset==null){
            throw new CustomException(ErrorCode.INVALID_CATEGORY,ErrorCode.INVALID_CATEGORY.getMessage());
        }

        //카테고리 문자열이 기본 카테고리 확인
        Category_Expend isBaseCategory=Category_Expend.fromString(category);
        //기본 카테고리가 아닌 경우
        CategoryExpend categoryExpend;
        if(isBaseCategory==null){
            //사용자 정의 카테고리
            categoryExpend=lockFacade.getOrCreateCustomCategoryExpend(category);
        }else {
            //기존 카테고리
            categoryExpend=expendCategoryRepository.findByCategoryName(isBaseCategory)
                    .orElseThrow(()->new CustomException(ErrorCode.INVALID_CATEGORY,ErrorCode.INVALID_CATEGORY.getMessage()));
        }

        //지출 객체 저장
        Expend expend = Expend.builder()
                .expendDate(LocalDate.parse(expendRequestDTO.getExpendDate()).atStartOfDay())
                .expendName(expendRequestDTO.getExpendName())
                .categoryExpend(categoryExpend)
                .asset(convertToAsset)
                .cost(expendRequestDTO.getCost())
                .user(userRepository.findByUserId(jwtUtil.getUserId(token)).orElseThrow(
                        () -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage())
                ))
                .build();
        expendRepository.save(expend);

        return expend.getExpendId();
    }

    @Timed(value = "service.expendList", description = "Time spent on ExpendService#getExpendList")
    @Override
    public List<ExpendResponseDTO> expendList(String token, int month) {
        long start = System.currentTimeMillis();
        log.info("expendList() 시작 - month: {}, token 일부: {}", month, token.substring(0, 10));

        /*유저정보로 지출 모두 반환*/

        //유저 정보 가져오기
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));

        User findUser = user.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));

        List<Expend> expendList = expendRepository.findAllByExpendDate(findUser, month);

        //지출 카테고리 정보 가져오기
        Map<Long, CategoryExpend> categoryExpendMap = getCategoryExpendMap(expendList);


        List<ExpendResponseDTO> result = expendList.stream()
                .map(expend -> mapToExpendResponseDTO(expend, categoryExpendMap))
                .collect(Collectors.toList());

        log.info("expendList() 완료 - 총 {}건 반환, 처리 시간: {}ms", result.size(), System.currentTimeMillis() - start);
        return result;
    }

    @Timed(value = "service.getCategoryExpendMap", description = "Time spent on ExpendService#getCategoryExpendMap")
    private Map<Long, CategoryExpend> getCategoryExpendMap(List<Expend> expendList) {
        return expendCategoryRepository.findAllById(
                        expendList.stream()
                                .map(Expend::getCategoryExpend)
                                .map(CategoryExpend::getCategoryId)
                                .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(
                        CategoryExpend::getCategoryId,
                        Function.identity()));
    }

    //지출 상세 내역 리턴

    @Override
    public ExpendResponseDTO expendResponseDetail(String token, Long expendId) {
        //토큰 검증
        //유저 정보 가져오기
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        User myUser = user.orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage())
        );

        //지출아이디로 지출 내역 찾기
        Expend findExpend = expendRepository.findByExpendId(expendId).orElseThrow(
                () -> new CustomException(ErrorCode.EXPEND_NOT_FOUND,ErrorCode.EXPEND_NOT_FOUND.getMessage())
        );

        //유저의 지출 내역인지 확인
        if (!Objects.equals(findExpend.getUser().getUserId(), myUser.getUserId())) {
            throw new CustomException(ErrorCode.USER_NOT_CORRECT, ErrorCode.USER_NOT_FOUND.getMessage());
        }


        //카테고리 변환
        Category_Expend categoryExpend = findExpend.getCategoryExpend().getCategoryName();
        String convertCategory = Category_Expend.toString(categoryExpend);


        return ExpendResponseDTO
                .builder()
                .expendId(findExpend.getExpendId())
                .expendDate(findExpend.getExpendDate().toString().substring(0, 10))
                .asset(Asset.toString(findExpend.getAsset()))
                .expendName(findExpend.getExpendName())
                .cost(findExpend.getCost())
                .category(convertCategory)
                .build();
    }


    @Override
    @Transactional
    public Long updateExpend(String token, Long expendId, ExpendRequestDTO expendRequestDTO) {
        // 유저 정보 가져오기
        User myUser = userRepository.findByUserId(jwtUtil.getUserId(token))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
        // 지출아이디로 지출 내역 찾기
        Expend findExpend = expendRepository.findByExpendId(expendId)
                .orElse(null);
        if (findExpend == null) {
            throw new CustomException(ErrorCode.EXPEND_NOT_FOUND,ErrorCode.EXPEND_NOT_FOUND.getMessage());
        }

        // 유저의 지출 내역인지 확인
        if (!Objects.equals(findExpend.getUser().getUserId(), myUser.getUserId())) {
            throw new CustomException(ErrorCode.USER_NOT_CORRECT, ErrorCode.USER_NOT_FOUND.getMessage());
        }

        // 카테고리 ENUM으로 변환
        Category_Expend newCategoryExpend = Category_Expend.fromString(expendRequestDTO.getCategory());
        if (newCategoryExpend == null) {
            throw new CustomException(ErrorCode.INVALID_CATEGORY,ErrorCode.INVALID_CATEGORY.getMessage());
        }

        // 새로운 카테고리 객체 찾기 또는 생성
        CategoryExpend categoryExpendToUpdate = expendCategoryRepository.findByCategoryName(newCategoryExpend)
                .orElseGet(()->CategoryExpend.createBaseCategory(newCategoryExpend));
        expendCategoryRepository.save(categoryExpendToUpdate);
        Asset convertToAsset = Asset.fromString(expendRequestDTO.getAsset());
        if(convertToAsset ==null){
            throw new CustomException(ErrorCode.INVALID_CATEGORY,ErrorCode.INVALID_CATEGORY.getMessage());
        }

        //지출 내역수정
        Expend updateExpend = findExpend.toBuilder()
                .expendName(expendRequestDTO.getExpendName())
                .expendDate(LocalDateTime.parse(expendRequestDTO.getExpendDate() + "T00:00"))
                .asset(convertToAsset)
                .cost(expendRequestDTO.getCost())
                .categoryExpend(categoryExpendToUpdate)
                .updateDate(LocalDateTime.now())
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
        User myUser = user.orElseThrow(

                () -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage())
        );
        //지출아이디로 지출 내역 찾기
        Expend findExpend = expendRepository.findByExpendId(expendId).orElseThrow(
                () -> new CustomException(ErrorCode.EXPEND_NOT_FOUND,ErrorCode.EXPEND_NOT_FOUND.getMessage())

        );

        //유저의 지출 내역인지 확인
        if (!Objects.equals(findExpend.getUser().getUserId(), myUser.getUserId())) {
            throw new CustomException(ErrorCode.USER_NOT_CORRECT, ErrorCode.USER_NOT_FOUND.getMessage());
        }

        expendRepository.deleteById(findExpend.getExpendId());
        return 1L;
    }

    //최근 지출내역 5개 가져오기

    @Override
    public List<ExpendResponseDTO> getRecentExpend(String token) {
        //jwt토큰 검증 실시
        Optional<User> findUser = userRepository.findByUserId(jwtUtil.getUserId(token));

        User user = findUser.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));

        //유저의 최신 지출 내역 5개 불러오기
        List<Expend> recentExpendList = expendRepository.findRecentExpend(
                PageRequest.of(0, 5),user).orElseThrow(
                () -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR,ErrorCode.INTERNAL_SERVER_ERROR.getMessage())

        );

        //지출 카테고리 정보 가져오기
        Map<Long, CategoryExpend> categoryExpendMap = getCategoryExpendMap(recentExpendList);


        return recentExpendList.stream()
                .map(expend -> mapToExpendResponseDTO(expend, categoryExpendMap))
                .collect(Collectors.toList());
    }
    @Override
    public List<ExpendDateResponseDTO> getAmountByWeek(String token) {
        //jwt토큰 검증 실시
        Optional<User> findUser = userRepository.findByUserId(jwtUtil.getUserId(token));
        User user = findUser.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));

        //이번달 불러오기
        int nowMonth = LocalDate.now().getMonthValue();

        //저번달
        int prevMonth = nowMonth - 1;
        if (prevMonth == 0) {
            prevMonth = 12;
        }

        //주차별 총 지출금액 가져오기
        List<Object[]> nowMonthData = expendRepository.getTotalExpendThisMonth(user);
        List<Object[]> prevMonthData = expendRepository.getTotalExpendLastMonth(user);

        List<ExpendDateResponseDTO> graphReport = new ArrayList<>();
        Map<Integer, Integer> currentMonthMap = nowMonthData.stream()
                .collect(Collectors.toMap(
                        o -> ((Number) o[0]).intValue(),
                        o -> ((Number) o[1]).intValue()
                ));

        Map<Integer, Integer> prevMonthMap = prevMonthData.stream()
                .collect(Collectors.toMap(
                        o -> ((Number) o[0]).intValue(),
                        o -> ((Number) o[1]).intValue()
                ));
        for (int week = 1; week <= 5; week++) {
            ExpendDateResponseDTO dto = ExpendDateResponseDTO.builder()
                    .month(nowMonth)
                    .weekNum(week)
                    .thisWeekTotalCost(currentMonthMap.getOrDefault(week, 0))
                    .lastWeekTotalCost(prevMonthMap.getOrDefault(week, 0))
                    .build();
            graphReport.add(dto);
        }
        return graphReport;
    }

    @Override
    public List<ExpendCategoryAmountDTO> getAmountByMonth(String token) {
        //jwt토큰 검증 실시
        Optional<User> findUser = userRepository.findByUserId(jwtUtil.getUserId(token));
        User user = findUser.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));

        //ENUM 리스트
        Category_Expend[] categoryList = Category_Expend.values();

        List<ExpendCategoryAmountDTO> nowMonthList = new ArrayList<>();
        for (Category_Expend category : categoryList) {
            //카테고리별 이번달 지출액 불러오기
            Long thisMonthAmount = expendRepository.thisMonthAmountByCategory(user, category);
            //카테고리별 저번달 지출액 불러오기
            Long prevMonthAmount = expendRepository.prevMonthAmountByCategory(user, category);
            //카테고리 변환
            String convertCategory = Category_Expend.toString(category);

            //퍼센트 계산
            String upOrDown;
            int percentChange;
            if (prevMonthAmount == 0) {
                // 저번 달 지출액이 0일 때
                if (thisMonthAmount > 0) {
                    percentChange = 100;
                    upOrDown = "UP";
                } else {
                    percentChange = 0;
                    upOrDown = "SAME";
                }
            } else {
                // 저번 달 지출액이 0이 아닐 때
                double change = (double) (thisMonthAmount - prevMonthAmount) / prevMonthAmount * 100;
                percentChange = Math.abs((int) change);
                upOrDown = change >= 0 ? "UP" : "DOWN";
            }

            //카테고리 상세 내역 2개씩 리스트
            List<Expend> expendList = expendRepository.findRecentExpend(user, category, PageRequest.of(0, 2));
            List<ExpendResponseDTO> expendResponseDTOList = expendList.stream()
                    .map(expend -> ExpendResponseDTO.builder()
                            .expendId(expend.getExpendId())
                            .expendName(expend.getExpendName())
                            .expendDate(expend.getExpendDate().toString())
                            .cost(expend.getCost())
                            .category(convertCategory)
                            .asset(Asset.toString(expend.getAsset()))
                            .build()).toList();

            ExpendCategoryAmountDTO expendCategoryAmountDTO = ExpendCategoryAmountDTO.builder()
                    .categoryName(convertCategory)
                    .amount(thisMonthAmount)
                    .percent(percentChange)
                    .upOrDown(upOrDown)
                    .expendList(expendResponseDTOList)
                    .build();

            nowMonthList.add(expendCategoryAmountDTO);
        }
        return nowMonthList;
    }

    @Override
    public List<ExpendDayResponseDTO> getAmountByDate(String token) {
        //jwt토큰 검증 실시
        Optional<User> findUser = userRepository.findByUserId(jwtUtil.getUserId(token));
        User user = findUser.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));

        //2주 계산
        LocalDateTime startDate = LocalDateTime.now().minusWeeks(2);
        LocalDateTime endDate = LocalDateTime.now();

        //2주 단위 가져오기
        List<Object[]> totalExpendList = expendRepository.findExpendTotalByDateRange(user, startDate, endDate);
        List<ExpendDayResponseDTO> expendDayResponseDTOList = new ArrayList<>();

        //데이터 반환
        for (int i = 0; i < 14; i++) {
            LocalDate targetDate = LocalDate.now().minusDays(13 - i);

            Long dailyExpendTotal = totalExpendList.stream()
                    .filter(dayTotal -> targetDate.equals(((LocalDateTime) dayTotal[1]).toLocalDate()))
                    .map(dayTotal -> (Long) dayTotal[0])
                    .findFirst()
                    .orElse(0L);

            ExpendDayResponseDTO expendDayResponseDTO = ExpendDayResponseDTO.builder()
                    .dayNum(i + 1)
                    .dayNum(targetDate.getDayOfMonth())
                    .costSum(dailyExpendTotal)
                    .build();

            expendDayResponseDTOList.add(expendDayResponseDTO);

        }
        return expendDayResponseDTOList;
    }

    // DTO로 매핑하는 함수
    private ExpendResponseDTO mapToExpendResponseDTO(Expend expend, Map<Long, CategoryExpend> categoryExpendMap) {
        CategoryExpend categoryExpend = categoryExpendMap.get(expend.getCategoryExpend().getCategoryId());

        // 카테고리명 한글로 변환
        String convertedCategory = (categoryExpend != null) ? Category_Expend.toString(categoryExpend.getCategoryName()) : null;

        return ExpendResponseDTO.builder()
                .expendId(expend.getExpendId())
                .expendDate(expend.getExpendDate().toString().substring(0, 10))
                .expendName(expend.getExpendName())
                .asset(Asset.toString(expend.getAsset()))
                .cost(expend.getCost())
                .category(convertedCategory)
                .build();
    }
}
