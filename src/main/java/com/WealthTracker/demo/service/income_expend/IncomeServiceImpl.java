package com.WealthTracker.demo.service.income_expend;

import com.WealthTracker.demo.DTO.income_expend.IncomeRequestDTO;
import com.WealthTracker.demo.domain.Category_income;
import com.WealthTracker.demo.domain.Income;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.enums.Asset;
import com.WealthTracker.demo.enums.CategoryExpend;
import com.WealthTracker.demo.enums.CategoryIncome;
import com.WealthTracker.demo.repository.CategoryIncomeRepository;
import com.WealthTracker.demo.repository.ExpendCategoryRepository;
import com.WealthTracker.demo.repository.IncomeCategoryRepository;
import com.WealthTracker.demo.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class IncomeServiceImpl implements IncomeService{
    private final IncomeRepository incomeRepository;
    private final CategoryIncomeRepository categoryIncomeRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;


    @Override
    @Transactional
    public Long writeIncome(IncomeRequestDTO incomeRequestDTO) {
        //카테고리명 변경
        String category=incomeRequestDTO.getCategory();
        CategoryIncome convertIncome=CategoryIncome.fromString(category);

        String asset=incomeRequestDTO.getAsset();
        Asset convertToAsset=Asset.fromString(asset);

        Category_income categoryIncome=Category_income.builder()
                .categoryName(convertIncome)
                .build();
        categoryIncomeRepository.save(categoryIncome);

        //임시 유저
        User user1=new User();


        Income income=Income.builder()
                .incomeDate(incomeRequestDTO.getIncomeDate())
                .incomeName(incomeRequestDTO.getIncomeName())
                .asset(convertToAsset)
                .cost(incomeRequestDTO.getCost())
                .user(user1)
                .build();
        incomeRepository.save(income);

        com.WealthTracker.demo.domain.CategoryIncome categoryIncome1= com.WealthTracker.demo.domain.CategoryIncome
                .builder()
                .categoryIncome(categoryIncome)
                .income(income)
                .build();
        incomeCategoryRepository.save(categoryIncome1);

        return income.getIncomeId();

    }
}
