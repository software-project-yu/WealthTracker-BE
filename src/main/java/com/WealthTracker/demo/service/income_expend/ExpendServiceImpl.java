package com.WealthTracker.demo.service.income_expend;

import com.WealthTracker.demo.DTO.income_expend.ExpendRequestDTO;
import com.WealthTracker.demo.DTO.income_expend.ExpendResponseDTO;
import com.WealthTracker.demo.DTO.income_expend.IncomeResponseDTO;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.domain.Category_expend;
import com.WealthTracker.demo.domain.Expend;
import com.WealthTracker.demo.domain.Income;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.enums.Asset;
import com.WealthTracker.demo.enums.CategoryExpend;
import com.WealthTracker.demo.enums.CategoryIncome;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.repository.CategoryExpendRepository;
import com.WealthTracker.demo.repository.ExpendCategoryRepository;
import com.WealthTracker.demo.repository.ExpendRepository;
import com.WealthTracker.demo.repository.UserRepository;
import com.WealthTracker.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class ExpendServiceImpl implements ExpendService {
    private final ExpendRepository expendRepository;
    private final CategoryExpendRepository categoryExpendRepository;
    private final ExpendCategoryRepository expendCategoryRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public Long writeExpend(ExpendRequestDTO expendRequestDTO,String token) {
        if(!jwtUtil.validationToken(token)){
            return -1L;
        }
        //카테고리명 변경
        String category=expendRequestDTO.getCategory();
        CategoryExpend convertToCategory=CategoryExpend.fromString(category);

        String asset=expendRequestDTO.getAsset();
        Asset convertToAsset=Asset.fromString(asset);

        //카테고리 저장
        Category_expend categoryExpend=Category_expend.builder()
                .categoryName(convertToCategory)
                .build();
        categoryExpendRepository.save(categoryExpend);

        //지출 객체 저장
        Expend expend=Expend.builder()
                .expendDate(expendRequestDTO.getExpendDate())
                .expendName(expendRequestDTO.getExpendName())
                .asset(convertToAsset)
                .cost(expendRequestDTO.getCost())
                .user(userRepository.findByUserId(jwtUtil.getUserId(token)).orElseThrow(
                        ()->new CustomException(ErrorCode.USER_NOT_FOUND)
                ))
                .build();
        expendRepository.save(expend);

        //중간테이블 값 저장
        com.WealthTracker.demo.domain.CategoryExpend categoryExpend1= com.WealthTracker.demo.domain.CategoryExpend
                .builder()
                .categoryExpend(categoryExpend)
                .expend(expend)
                .build();
        expendCategoryRepository.save(categoryExpend1);

        return expend.getExpendId();
    }



    @Override
    public List<ExpendResponseDTO> list(String token) {
        /*유저정보로 수입 지출 모두 반환*/

        //유저 정보 가져오기
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        List<Expend> expendList = expendRepository.findAllByUser(user.orElseThrow(
                ()->new CustomException(ErrorCode.USER_NOT_FOUND)
        ));

        //카테고리 불러오기

        return expendList.stream()
                .map(expend -> {
                    CategoryExpend categoryExpend = CategoryExpend.valueOf(
                            categoryExpendRepository.findCategoryNameByExpendId(expend.getExpendId()).get().toString()
                    );
                    return ExpendResponseDTO
                            .builder()
                            .expendId(expend.getExpendId())
                            .expendDate(expend.getExpendDate())
                            .expendName(expend.getExpendName())
                            .asset(Asset.toString(expend.getAsset()))
                            .cost(expend.getCost())
                            .category(CategoryExpend.toString(categoryExpend))
                            .build();
                })
                .toList();
    }

    @Override
    public ExpendResponseDTO expendDetail(String token, Long expendId) {
        return null;
    }

//    @Override
//    public ExpendResponseDTO expendDetail(String token, Long expendId) {
//        expendRepository.findByExpendId(expendId).orElseThrow(
//                () -> new CustomException(ErrorCode.EXPEND_NOT_FOUND)
//        );
//    }
}
