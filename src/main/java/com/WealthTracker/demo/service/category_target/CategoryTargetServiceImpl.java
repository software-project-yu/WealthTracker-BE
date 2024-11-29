package com.WealthTracker.demo.service.category_target;

import com.WealthTracker.demo.DTO.category_target.CategoryTargetRequestDTO;
import com.WealthTracker.demo.DTO.category_target.CategoryTargetResponseDTO;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.domain.CategoryTarget;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.enums.Category_Expend;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.repository.CategoryTargetRepository;
import com.WealthTracker.demo.repository.ExpendRepository;
import com.WealthTracker.demo.repository.UserRepository;
import com.WealthTracker.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryTargetServiceImpl implements CategoryTargetService {

    private final CategoryTargetRepository categoryTargetRepository;
    private final UserRepository userRepository;
    private final ExpendRepository expendRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void createTarget(String token, CategoryTargetRequestDTO requestDTO) {
        Long userId = jwtUtil.getUserId(token);
        User user = getUserById(userId);

        categoryTargetRepository.findByUserAndCategory(user, requestDTO.getCategory())
                .ifPresent(target -> {
                    throw new CustomException(ErrorCode.TARGET_ALREADY_EXISTS,ErrorCode.TARGET_ALREADY_EXISTS.getMessage());
                });

        CategoryTarget categoryTarget = CategoryTarget.builder()
                .user(user)
                .category(requestDTO.getCategory())
                .targetAmount(requestDTO.getTargetAmount())
                .build();

        categoryTargetRepository.save(categoryTarget);
    }

    @Override
    @Transactional
    public void updateTarget(String token, CategoryTargetRequestDTO requestDTO) {
        Long userId = jwtUtil.getUserId(token);
        User user = getUserById(userId);

        CategoryTarget categoryTarget = categoryTargetRepository.findByUserAndCategory(user, requestDTO.getCategory())
                .orElseThrow(() -> new CustomException(ErrorCode.TARGET_NOT_FOUND,ErrorCode.TARGET_NOT_FOUND.getMessage()));

        categoryTarget = CategoryTarget.builder()
                .categorytargetId(categoryTarget.getCategorytargetId())
                .user(categoryTarget.getUser())
                .category(requestDTO.getCategory())
                .targetAmount(requestDTO.getTargetAmount())
                .build();

        categoryTargetRepository.save(categoryTarget);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryTargetResponseDTO getTarget(String token, Category_Expend category) {
        Long userId = jwtUtil.getUserId(token);
        User user = getUserById(userId);
        CategoryTarget categoryTarget = categoryTargetRepository.findByUserAndCategory(user, category)
                .orElse(null); // 목표가 없으면 null 반환

        Long currentExpenditure = expendRepository.thisMonthAmountByCategory(user, category); // 현재 카테고리의 총 지출 금액

        if (categoryTarget == null) {
            // 목표가 없을 경우 default를 0원으로 설정해주기
            return CategoryTargetResponseDTO.builder()
                    .category(category)
                    .targetAmount(0L)
                    .currentExpend(currentExpenditure)
                    .message("아직 목표를 설정하지 않았습니다. 현재 지출은 " + currentExpenditure + "원 입니다.")
                    .build();
        }

        // 목표가 있을 경우 기존 로직 사용
        return CategoryTargetResponseDTO.builder()
                .category(categoryTarget.getCategory())
                .targetAmount(categoryTarget.getTargetAmount())
                .currentExpend(currentExpenditure)
                .message(getMessage(categoryTarget.getTargetAmount(), currentExpenditure))
                .build();
    }

    private String getMessage(Long targetAmount, Long currentExpenditure) {
        if (currentExpenditure > targetAmount) {
            return "(UP) 목표보다 " + (currentExpenditure - targetAmount) + "원 더 쓰고 있습니다.";
        } else {
            return "(DOWN) 목표보다 " + (targetAmount - currentExpenditure) + "원 덜 쓰고 있습니다.";
        }
    }

    private User getUserById(Long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND,ErrorCode.USER_NOT_FOUND.getMessage()));

    }
}
