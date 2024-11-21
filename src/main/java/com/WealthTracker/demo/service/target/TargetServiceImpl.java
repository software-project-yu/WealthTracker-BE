package com.WealthTracker.demo.service.target;

import com.WealthTracker.demo.DTO.dailysaving.DailySavingRequestDTO;
import com.WealthTracker.demo.DTO.target.TargetRequestDTO;
import com.WealthTracker.demo.DTO.target.TargetResponseDTO;
import com.WealthTracker.demo.domain.DailySaving;
import com.WealthTracker.demo.domain.Target;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.repository.DailySavingRepository;
import com.WealthTracker.demo.repository.TargetRepository;
import com.WealthTracker.demo.repository.UserRepository;
import com.WealthTracker.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TargetServiceImpl implements TargetService {

    private final TargetRepository targetRepository;
    private final DailySavingRepository dailySavingRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private Long getUserIdFromToken(String token) { //* token을 이용해 userId를 추출하는 공통 메서드 만듦
        return jwtUtil.getUserId(token);
    }

    @Override
    @Transactional
    public TargetResponseDTO createTarget(TargetRequestDTO requestDTO, String token) { //* 새로운 목표 생성하는 서비스 로직
        User user = userRepository.findByUserId(getUserIdFromToken(token))
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        // 정상적으로 진행되는 서비스 로직 구현부
        Target target = Target.builder()
                .user(user)
                .targetAmount(requestDTO.getTargetAmount())
                .savedAmount(0)
                .startDate(requestDTO.getStartDate())
                .endDate(requestDTO.getEndDate())
                .build();
        // 목표에 대한 내용들을 builder로 저장한 후
        Target savedTarget = targetRepository.save(target); // 목표를 저장

        return new TargetResponseDTO(
                savedTarget.getTargetId(),
                savedTarget.getTargetAmount(),
                savedTarget.getSavedAmount(),
                savedTarget.getAchievementRate()
        ); // DTO형태로 반환
    }

    @Override
    @Transactional
    public TargetResponseDTO updateTarget(Long targetId, TargetRequestDTO requestDTO, String token) { //* 목표 수정 로직
        Long userId = getUserIdFromToken(token);

        Target target = targetRepository.findByTargetIdAndUserUserId(targetId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저에게는 존재하지 않는 목표입니다."));

        target.updateTarget(requestDTO.getTargetAmount(), requestDTO.getStartDate(), requestDTO.getEndDate());

        Target updatedTarget = targetRepository.save(target);

        return new TargetResponseDTO(
                updatedTarget.getTargetId(),
                updatedTarget.getTargetAmount(),
                updatedTarget.getSavedAmount(),
                updatedTarget.getAchievementRate()
        );
    }

    @Override
    @Transactional
    public void deleteTarget(Long targetId, String token) { //* 목표 삭제 로직
        Long userId = getUserIdFromToken(token);

        Target target = targetRepository.findByTargetIdAndUserUserId(targetId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저에게는 존재하지 않는 목표입니다."));

        targetRepository.delete(target);
    }

    @Override
    @Transactional
    public void addDailySaving(Long targetId, DailySavingRequestDTO requestDTO, String token) { //* 목표 저축하는 서비스 로직
        Long userId = getUserIdFromToken(token);

        Target target = targetRepository.findByTargetIdAndUserUserId(targetId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저에게는 존재하지 않는 목표입니다."));
        // 날짜별 저축에 날짜와 목표에 대한 내용 저장
        DailySaving dailySaving = DailySaving.builder()
                .target(target)
                .date(requestDTO.getDate())
                .amount(requestDTO.getAmount())
                .build();

        // 목표(Target)에 저축 금액 반영
        target.addSaving(dailySaving);

        // 변경된 Target과 DailySaving 저장
        dailySavingRepository.save(dailySaving); // dailySaving 저장
        targetRepository.save(target); // savedAmount 업데이트를 반영하기 위해 저장

    }

    @Override
    @Transactional(readOnly = true)
    public double getAchievementRate(Long targetId, String token) { //* 저축 목표 달성률 반환하는 서비스 로직
        Long userId = getUserIdFromToken(token);

        Target target = targetRepository.findByTargetIdAndUserUserId(targetId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저에게는 존재하지 않는 목표입니다."));

        return target.getAchievementRate();
    }
}