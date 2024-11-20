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
                    throw new CustomException(ErrorCode.TARGET_ALREADY_EXISTS);
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
                .orElseThrow(() -> new CustomException(ErrorCode.TARGET_NOT_FOUND));

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
                .orElseThrow(() -> new CustomException(ErrorCode.TARGET_NOT_FOUND));

        Long currentExpenditure = expendRepository.thisMonthAmountByCategory(user, category);

        return new CategoryTargetResponseDTO(
                categoryTarget.getCategory(),
                categoryTarget.getTargetAmount(),
                currentExpenditure,
                getMessage(categoryTarget.getTargetAmount(), currentExpenditure)
        );
    }

    private String getMessage(Long targetAmount, Long currentExpenditure) {
        if (currentExpenditure > targetAmount) {
            return "목표보다 " + (currentExpenditure - targetAmount) + "원 더 쓰고 있습니다.";
        } else {
            return "목표보다 " + (targetAmount - currentExpenditure) + "원 덜 쓰고 있습니다.";
        }
    }

    private User getUserById(Long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    }
}
