package com.WealthTracker.demo.service.income_expend;

import com.WealthTracker.demo.DTO.income_expend.ExpendIncomeResponseDTO;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.domain.Expend;
import com.WealthTracker.demo.domain.Income;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.enums.Asset;
import com.WealthTracker.demo.enums.Category_Expend;
import com.WealthTracker.demo.enums.Category_Income;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.repository.*;
import com.WealthTracker.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class ExpendIncomeServiceImpl implements ExpendIncomeService {
    private final ExpendRepository expendRepository;
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional(readOnly = true)
    public List<ExpendIncomeResponseDTO> getExpendIncomeAllList(String token, int month) {
        //유저 정보 가져오기
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        User myUser = user.orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage())
        );

        //이번 달 지출 총 내역
        List<Expend> thisMonthExpend = expendRepository.findAllByExpendDate(myUser, month);

        //이번 달 수입 총 내역
        List<Income> thisMonthIncome = incomeRepository.findAllByIncomeDate(myUser, month);

        //새로운 월별 총지출 생성
        List<Object> transactions = new ArrayList<>();
        transactions.addAll(thisMonthIncome);
        transactions.addAll(thisMonthExpend);
        //날짜별 정렬
        transactions.sort((a, b) -> {
            LocalDateTime dateA = (a instanceof Expend) ? ((Expend) a).getExpendDate() : ((Income) a).getIncomeDate();
            LocalDateTime dateB = (b instanceof Expend) ? ((Expend) b).getExpendDate() : ((Income) b).getIncomeDate();
            return dateB.compareTo(dateA);
        });

        return transactions.stream()
                .map((object) -> {
                    // 지출인 걍우
                    if (object instanceof Expend) {
                        Expend expend = (Expend) object;

                        //카테고리 string 변환
                        Category_Expend category_expend = expend.getCategoryExpend().getCategoryName();
                        String convertCategory = Category_Expend.toString(category_expend);

                        return ExpendIncomeResponseDTO.builder()
                                .asset(Asset.toString(expend.getAsset()))
                                .id(expend.getExpendId())
                                .cost(expend.getCost())
                                .content(expend.getExpendName())
                                .date(expend.getExpendDate().toString().substring(0, 10))
                                .type("지출")
                                .category(convertCategory)
                                .build();
                    }
                    //수입인 경우
                    else {
                        Income income = (Income) object;

                        //카테고리 string 변환
                        Category_Income category_income = income.getCategoryIncome().getCategoryName();
                        String convertCategory = Category_Income.toString(category_income);

                        return ExpendIncomeResponseDTO.builder()
                                .asset(Asset.toString(income.getAsset()))
                                .id(income.getIncomeId())
                                .cost(income.getCost())
                                .content(income.getIncomeName())
                                .date(income.getIncomeDate().toString().substring(0, 10))
                                .type("수입")
                                .category(convertCategory)
                                .build();

                    }
                }).collect(Collectors.toList());
    }
}
