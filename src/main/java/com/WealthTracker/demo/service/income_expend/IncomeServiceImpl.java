package com.WealthTracker.demo.service.income_expend;

import com.WealthTracker.demo.DTO.income_expend.IncomeRequestDTO;
import com.WealthTracker.demo.DTO.income_expend.IncomeResponseDTO;
import com.WealthTracker.demo.repository.*;
import com.WealthTracker.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class IncomeServiceImpl implements IncomeService {
    private final IncomeRepository incomeRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public Long writeIncome(IncomeRequestDTO incomeRequestDTO, String token) {
        return null;
    }

    @Override
    public List<IncomeResponseDTO> list(String token) {
        return null;
    }


//    @Override
//    @Transactional
//    public Long writeIncome(IncomeRequestDTO incomeRequestDTO, String token) {
//        //jwt확인
//        if (!jwtUtil.validationToken(token)) {
//            return -1L;
//        }
//        //카테고리명 변경
//        String category = incomeRequestDTO.getCategory();
//        CategoryIncome convertIncome = CategoryIncome.fromString(category);
//
//        String asset = incomeRequestDTO.getAsset();
//        Asset convertToAsset = Asset.fromString(asset);
//
//        Category_income categoryIncome = Category_income.builder()
//                .categoryName(convertIncome)
//                .build();
//        categoryIncomeRepository.save(categoryIncome);
//
//
//        Income income = Income.builder()
//                .incomeDate(incomeRequestDTO.getIncomeDate())
//                .incomeName(incomeRequestDTO.getIncomeName())
//                .asset(convertToAsset)
//                .cost(incomeRequestDTO.getCost())
//                .user(userRepository.findByUserId(jwtUtil.getUserId(token)).orElseThrow(
//                        () -> new CustomException(ErrorCode.USER_NOT_FOUND)
//                ))
//                .build();
//        log.info("userId", jwtUtil.getUserId(token));
//        incomeRepository.save(income);
//
//        com.WealthTracker.demo.domain.CategoryIncome categoryIncome1 = com.WealthTracker.demo.domain.CategoryIncome
//                .builder()
//                .categoryIncome(categoryIncome)
//                .income(income)
//                .build();
//        incomeCategoryRepository.save(categoryIncome1);
//
//        return income.getIncomeId();
//
//    }
//
//    @Override
//    public List<IncomeResponseDTO> list(String token) {
//        /*유저정보로 수입 지출 모두 반환*/
//
//        //유저 정보 가져오기
//        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
//        List<Income> incomeList = incomeRepository.findAllByUser(user.orElseThrow(
//                ()->new CustomException(ErrorCode.USER_NOT_FOUND)
//        ));
//
//        //카테고리 불러오기
//
//        return incomeList.stream()
//                .map(income -> {
//                    CategoryIncome categoryIncome = CategoryIncome.valueOf(
//                            categoryIncomeRepository.findCategoryNameByIncomeId(income.getIncomeId()).get().toString()
//                    );
//                    return IncomeResponseDTO
//                            .builder()
//                            .incomeId(income.getIncomeId())
//                            .incomeDate(income.getIncomeDate())
//                            .incomeName(income.getIncomeName())
//                            .asset(Asset.toString(income.getAsset()))
//                            .cost(income.getCost())
//                            .category(CategoryIncome.toString(categoryIncome))
//                            .build();
//                })
//                .toList();
//    }
//
//
}