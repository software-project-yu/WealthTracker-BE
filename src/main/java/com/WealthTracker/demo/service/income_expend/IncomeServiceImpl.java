package com.WealthTracker.demo.service.income_expend;

import com.WealthTracker.demo.DTO.income_expend.IncomeRequestDTO;
import com.WealthTracker.demo.DTO.income_expend.IncomeResponseDTO;
import com.WealthTracker.demo.domain.Category_income;
import com.WealthTracker.demo.domain.Income;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.enums.Asset;
import com.WealthTracker.demo.enums.CategoryExpend;
import com.WealthTracker.demo.enums.CategoryIncome;
import com.WealthTracker.demo.repository.*;
import com.WealthTracker.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class IncomeServiceImpl implements IncomeService {
    private final IncomeRepository incomeRepository;
    private final CategoryIncomeRepository categoryIncomeRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public Long writeIncome(IncomeRequestDTO incomeRequestDTO, String token) {
        //카테고리명 변경
        String category = incomeRequestDTO.getCategory();
        CategoryIncome convertIncome = CategoryIncome.fromString(category);

        String asset = incomeRequestDTO.getAsset();
        Asset convertToAsset = Asset.fromString(asset);

        Category_income categoryIncome = Category_income.builder()
                .categoryName(convertIncome)
                .build();
        categoryIncomeRepository.save(categoryIncome);


        Income income = Income.builder()
                .incomeDate(incomeRequestDTO.getIncomeDate())
                .incomeName(incomeRequestDTO.getIncomeName())
                .asset(convertToAsset)
                .cost(incomeRequestDTO.getCost())
                .user(userRepository.findByUserId(jwtUtil.getUserId(token)).get())
                .build();
        log.info("userId",jwtUtil.getUserId(token));
        incomeRepository.save(income);

        com.WealthTracker.demo.domain.CategoryIncome categoryIncome1 = com.WealthTracker.demo.domain.CategoryIncome
                .builder()
                .categoryIncome(categoryIncome)
                .income(income)
                .build();
        incomeCategoryRepository.save(categoryIncome1);

        return income.getIncomeId();

    }

    @Override
    public List<IncomeResponseDTO> list(String token) {
        /*유저정보로 수입 지출 모두 반환*/

        //유저 정보 가져오기
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        List<Income> incomeList = incomeRepository.findAllByUser(user.get());

        //카테고리 불러오기


        return incomeList.stream()
                .map(income -> IncomeResponseDTO
                        .builder()
                        .incomeDate(income.getIncomeDate())
                        .incomeName(income.getIncomeName())
                        .asset(String.valueOf(income.getAsset()))
                        .cost(income.getCost())
                        .category(categoryIncomeRepository.findCategoryNameByIncomeId(income.getIncomeId()).get().toString())
                        .build())
                .toList();
    }

    @Override
    public Long test(String token) {
        return jwtUtil.getUserId(token);
    }

}
