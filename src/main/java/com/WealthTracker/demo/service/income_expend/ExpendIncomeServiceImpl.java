package com.WealthTracker.demo.service.income_expend;

import com.WealthTracker.demo.DTO.income_expend.ExpendIncomeResponseDTO;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.repository.ExpendRepository;
import com.WealthTracker.demo.repository.IncomeRepository;
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

public class ExpendIncomeServiceImpl implements ExpendIncomeService{
    private final ExpendRepository expendRepository;
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public List<ExpendIncomeResponseDTO> getExpendIncomeAllList(String token, String month) {
        //유저 정보 가져오기
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        User myUser=user.orElseThrow(
                ()->new CustomException(ErrorCode.USER_NOT_FOUND)
        );


    }
}
