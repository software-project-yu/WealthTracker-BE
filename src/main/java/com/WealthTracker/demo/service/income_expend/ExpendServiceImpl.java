package com.WealthTracker.demo.service.income_expend;

import com.WealthTracker.demo.DTO.income_expend.ExpendRequestDTO;
import com.WealthTracker.demo.domain.Category_expend;
import com.WealthTracker.demo.domain.Expend;
import com.WealthTracker.demo.enums.Asset;
import com.WealthTracker.demo.enums.CategoryExpend;
import com.WealthTracker.demo.repository.CategoryExpendRepository;
import com.WealthTracker.demo.repository.ExpendCategoryRepository;
import com.WealthTracker.demo.repository.ExpendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class ExpendServiceImpl implements ExpendService {
    private final ExpendRepository expendRepository;
    private final CategoryExpendRepository categoryExpendRepository;
    private final ExpendCategoryRepository expendCategoryRepository;

    @Override
    @Transactional
    public Long writeExpend(ExpendRequestDTO expendRequestDTO) {
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
}
