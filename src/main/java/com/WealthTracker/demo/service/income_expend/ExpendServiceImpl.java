package com.WealthTracker.demo.service.income_expend;

import com.WealthTracker.demo.DTO.income_expend.*;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.domain.Expend;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.enums.Asset;
import com.WealthTracker.demo.enums.CategoryExpend;
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
import java.util.List;
import java.util.Optional;
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
        CategoryExpend convertToCategory = CategoryExpend.fromString(category);

        //변환결과 예외처리
        if(convertToCategory==null){
            throw new CustomException(ErrorCode.INVALID_CATEGORY);
        }

        String asset = expendRequestDTO.getAsset();
        Asset convertToAsset = Asset.fromString(asset);

        //카테고리 저장-기존 카테고리 존재 여부 확인
        com.WealthTracker.demo.domain.CategoryExpend categoryExpend =
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
        /*유저정보로 수입 지출 모두 반환*/

        //유저 정보 가져오기
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        List<Expend> expendList = expendRepository.findAllByUser(user.orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        ));

        //카테고리 불러오기

        return expendList.stream()
                .map(expend -> {
                    return ExpendResponseDTO
                            .builder()
                            .expendId(expend.getExpendId())
                            .expendDate(String.valueOf(expend.getExpendDate()))
                            .expendName(expend.getExpendName())
                            .asset(expend.getAsset().toString())
                            .cost(expend.getCost())
                            .category(expend.getCategoryExpend().toString())
                            .build();
                }).collect(Collectors.toList());
    }

    //지출 상세 내역 리턴
    @Override
    public ExpendResponseDTO expendResponseDetail(String token, Long expendId) {
        //토큰 검증
        //지출아이디로 지출 내역 찾기
        Optional<Expend> findExpend = expendRepository.findByExpendId(expendId);


        return ExpendResponseDTO
                .builder()
                .expendId(findExpend.get().getExpendId())
                .expendDate(findExpend.get().getExpendDate().toString())
                .asset(findExpend.get().getAsset().toString())
                .expendName(findExpend.get().getExpendName())
                .cost(findExpend.get().getCost())
                .category(findExpend.get().getCategoryExpend().toString())
                .build();
    }


    @Override
    public List<ExpendCategoryAmountDTO> expendCategoryResponse(String token, String category) {
        return null;
    }

    @Override
    public Long updateExpend(String token, Long expendId) {
        return null;
    }

    @Override
    public Long deleteExpend(String token, Long expendId) {
        return null;
    }

    //최근 지출내역 5개 가져오기
    @Override
    public List<ExpendResponseDTO> getRecentExpend(String token) {
        //jwt토큰 검증 실시
        Optional<User> findUser = userRepository.findByUserId(jwtUtil.getUserId(token));
        User user = findUser.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //유저의 최신 지출 내역 5개 불러오기
        List<Expend> recentExpendList = expendRepository.findRecentExpend(
                user, PageRequest.of(0, 5)
        );

        //Expend리스트를 ExpendResponseDTO리스트로 반환
        return recentExpendList.stream()
                .map(expend -> ExpendResponseDTO.builder()
                        .expendId(expend.getExpendId())
                        .expendName(expend.getExpendName())
                        .expendDate(expend.getExpendDate().toString())
                        .asset(expend.getAsset().toString())
                        .category(expend.getCategoryExpend().toString())
                        .cost(expend.getCost())
                        .build()
                ).collect(Collectors.toList());
    }

    @Override
    public List<ExpendDateResponseDTO> getAmountByWeek(String token) {
        return null;
    }

//    @Override
//    public ExpendResponseDTO expendDetail(String token, Long expendId) {
//        expendRepository.findByExpendId(expendId).orElseThrow(
//                () -> new CustomException(ErrorCode.EXPEND_NOT_FOUND)
//        );
//    }
}
