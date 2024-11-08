package com.WealthTracker.demo.service.income_expend;

import com.WealthTracker.demo.DTO.income_expend.IncomeRequestDTO;
import com.WealthTracker.demo.DTO.income_expend.IncomeResponseDTO;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.domain.*;
import com.WealthTracker.demo.enums.Asset;
import com.WealthTracker.demo.enums.Category_Income;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.repository.*;
import com.WealthTracker.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

public class IncomeServiceImpl implements IncomeService {
    private final IncomeRepository incomeRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long writeIncome(IncomeRequestDTO incomeRequestDTO, String token) {
        if (!jwtUtil.validationToken(token)) {
            return -1L;
        }
        //카테고리명 변환
        String category = incomeRequestDTO.getCategory();
        Category_Income convertToCategory = Category_Income.fromString(category);

        //변환결과 예외처리
        if (convertToCategory == null) {
            throw new CustomException(ErrorCode.INVALID_CATEGORY);
        }

        String asset = incomeRequestDTO.getAsset();
        Asset convertToAsset = Asset.fromString(asset);

        //카테고리 저장-기존 카테고리 존재 여부
        CategoryIncome categoryIncome =
                incomeCategoryRepository.findByCategoryName(convertToCategory)
                        .orElseGet(() -> {
                            CategoryIncome newCategory = CategoryIncome.builder()
                                    .categoryName(convertToCategory)
                                    .build();
                            return incomeCategoryRepository.save(newCategory);
                        });
        //수입 객체 저장
        Income income = Income.builder()
                .incomeDate(LocalDate.parse(incomeRequestDTO.getIncomeDate()).atStartOfDay())
                .incomeName(incomeRequestDTO.getIncomeName())
                .categoryIncome(categoryIncome)
                .asset(convertToAsset)
                .cost(incomeRequestDTO.getCost())
                .user(userRepository.findByUserId(jwtUtil.getUserId(token)).orElseThrow(
                        () -> new CustomException(ErrorCode.USER_NOT_FOUND)
                )).build();
        incomeRepository.save(income);
        return income.getIncomeId();
    }


    @Override
    public List<IncomeResponseDTO> incomeList(String token) {
        //유저 정보 가져오기
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        List<Income> incomeList = incomeRepository.findAllByUserWithCategory(user.orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        ));

        //수입 카테고리 정보 가져오기
        Map<Long, CategoryIncome> categoryIncomeMap = incomeCategoryRepository.findAllById(
                        incomeList.stream()
                                .map(Income::getCategoryIncome)
                                .map(CategoryIncome::getCategoryId)
                                .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(
                        CategoryIncome::getCategoryId,
                        Function.identity()));

        return incomeList.stream()
                .map(income -> mapToIncomeResponseDTO(income, categoryIncomeMap))
                .collect(Collectors.toList());

    }

    @Override
    public IncomeResponseDTO incomeResponseDetail(String token, Long incomeId) {
        //유저 정보 가져오기
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        User myUser=user.orElseThrow(
                ()->new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        //수입아이디로 수입 내역 찾기
        Income findIncome=incomeRepository.findByIncomeId(incomeId).orElseThrow(
                ()->new CustomException(ErrorCode.INCOME_NOT_FOUND)
        );

        //유저의 수입내역 확인
        if(!Objects.equals(findIncome.getUser().getUserId(),myUser.getUserId())){
            throw new CustomException(ErrorCode.USER_NOT_CORRECT);
        }

        //카테고리 변환
        Category_Income categoryIncome=findIncome.getCategoryIncome().getCategoryName();
        String convertCategory=Category_Income.toString(categoryIncome);

        return IncomeResponseDTO
                .builder()
                .incomeId(findIncome.getIncomeId())
                .incomeDate(findIncome.getIncomeDate().toString().substring(0,10))
                .asset(Asset.toString(findIncome.getAsset()))
                .incomeName(findIncome.getIncomeName())
                .cost(findIncome.getCost())
                .category(convertCategory)
                .build();
    }

    @Override
    @Transactional
    public Long updateIncome(String token, Long incomeId, IncomeRequestDTO incomeRequestDTO) {
        // 유저 정보 가져오기
        User myUser = userRepository.findByUserId(jwtUtil.getUserId(token))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 지출아이디로 지출 내역 찾기
        Income findIncome = incomeRepository.findByIncomeId(incomeId)
                .orElse(null);
        if(findIncome==null){
            throw new CustomException(ErrorCode.EXPEND_NOT_FOUND);
        }

        // 유저의 지출 내역인지 확인
        if (!Objects.equals(findIncome.getUser().getUserId(), myUser.getUserId())) {
            throw new CustomException(ErrorCode.USER_NOT_CORRECT);
        }

        // 카테고리 ENUM으로 변환
        Category_Income newCategoryIncome = Category_Income.fromString(incomeRequestDTO.getCategory());
        if (newCategoryIncome == null) {
            throw new CustomException(ErrorCode.INVALID_CATEGORY);
        }

        // 새로운 카테고리 객체 찾기 또는 생성
        CategoryIncome categoryIncomeToUpdate = incomeCategoryRepository.findByCategoryName(newCategoryIncome)
                .orElseGet(() -> CategoryIncome
                        .builder()
                        .categoryName(newCategoryIncome)
                        .build());
        incomeCategoryRepository.save(categoryIncomeToUpdate);


        //수입 내역 수정
        Income updateIncome=findIncome.toBuilder()
                .incomeName(incomeRequestDTO.getIncomeName())
                .incomeDate(LocalDateTime.parse(incomeRequestDTO.getIncomeDate()+"T00:00"))
                .asset(Asset.fromString(incomeRequestDTO.getAsset()))
                .cost(incomeRequestDTO.getCost())
                .categoryIncome(categoryIncomeToUpdate)
                .build();

        //레포지토리에 수정
        incomeRepository.save(updateIncome);
        return updateIncome.getIncomeId();
    }

    @Override
    @Transactional
    public Long deleteIncome(String token, Long incomeId) {
        //유저 정보 가져오기
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        User myUser=user.orElseThrow(
                ()->new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        //수입아이디로 수입 내역 찾기
        Income findIncome=incomeRepository.findByIncomeId(incomeId).orElseThrow(
                ()->new CustomException(ErrorCode.EXPEND_NOT_FOUND)
        );

        //유저의 지출 내역인지 확인
        if(!Objects.equals(findIncome.getUser().getUserId(), myUser.getUserId())){
            throw  new CustomException(ErrorCode.USER_NOT_CORRECT);
        }

        incomeRepository.deleteById(findIncome.getIncomeId());
        return 1L;
    }

    @Override
    public List<IncomeResponseDTO> getRecentIncome(String token) {
        //jwt토큰 검증 실시
        Optional<User> findUser = userRepository.findByUserId(jwtUtil.getUserId(token));
        User user = findUser.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //유저의 최신 지출 내역 5개 불러오기
        List<Income> recentIncomeList = incomeRepository.findRecentIncome(
                PageRequest.of(0, 5)).orElseThrow(
                ()->new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)
        );

        //지출 카테고리 정보 가져오기
        Map<Long, CategoryIncome> categoryIncomeMap = incomeCategoryRepository.findAllById(
                        recentIncomeList.stream()
                                .map(Income::getCategoryIncome)
                                .map(CategoryIncome::getCategoryId)
                                .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(
                        CategoryIncome::getCategoryId,
                        Function.identity()));


        return recentIncomeList.stream()
                .map(income -> mapToIncomeResponseDTO(income,categoryIncomeMap))
                .collect(Collectors.toList());
    }

    //DTO로 매핑하는 함수
    private IncomeResponseDTO mapToIncomeResponseDTO(Income income, Map<Long, CategoryIncome> categoryIncomeMap) {
        CategoryIncome categoryIncome = categoryIncomeMap.get(income.getCategoryIncome().getCategoryId());

        //카테고리명 한글로 변환
        String convertedCategory = (categoryIncome != null) ? Category_Income.toString(categoryIncome.getCategoryName()) : null;

        return IncomeResponseDTO.builder()
                .incomeId(income.getIncomeId())
                .incomeDate(income.getIncomeDate().toString().substring(0, 10))
                .incomeName(income.getIncomeName())
                .asset(Asset.toString(income.getAsset()))
                .cost(income.getCost())
                .category(convertedCategory)
                .build();
    }
}