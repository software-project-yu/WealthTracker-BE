package com.WealthTracker.demo.service;

import com.WealthTracker.demo.DTO.PasswordConfirmDTO;
import com.WealthTracker.demo.DTO.SignupRequestDTO;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.constants.SuccessCode;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.domain.VerificationCode;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.repository.UserRepository;
import com.WealthTracker.demo.repository.VerificationCodeRepository;
import com.WealthTracker.demo.util.JwtUtil;
import com.WealthTracker.demo.util.VerificationCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationCodeUtil verificationCodeUtil;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public String createVerificationCodeAndSendEmail(String email) {
        // 이메일 중복 확인
        if (userRepository.findByEmail(email).isPresent()) {

            throw new CustomException(ErrorCode.EMAIL_ALREADY_REGISTERED, ErrorCode.USER_NOT_FOUND.getMessage());

            throw new CustomException(ErrorCode.EMAIL_ALREADY_REGISTERED,ErrorCode.EMAIL_ALREADY_REGISTERED.getMessage());

        }

        // 기존 인증 코드 삭제
        verificationCodeRepository.findByEmail(email).ifPresent(verificationCodeRepository::delete);

        // 인증 코드 생성
        String code = verificationCodeUtil.generateVerificationCode();
        VerificationCode verificationCode = VerificationCode.builder()
                .email(email)
                .code(code)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();

        verificationCodeRepository.save(verificationCode);
        // 이메일 발송
        emailService.sendEmailVerification(email, code);

        return SuccessCode.SUCCESS_EMAIL_SENT.getMessage();
    }

    @Override
    @Transactional
    public void verifyEmail(String email, String code) {
        try {
            VerificationCode verificationCode = verificationCodeRepository.findByEmail(email)

                    .orElseThrow(() -> new CustomException(ErrorCode.VERIFICATION_CODE_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
            if (!verificationCode.getCode().equals(code)) {
                throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE, ErrorCode.USER_NOT_FOUND.getMessage());

                    .orElseThrow(() -> new CustomException(ErrorCode.VERIFICATION_CODE_NOT_FOUND,ErrorCode.VERIFICATION_CODE_NOT_FOUND.getMessage()));
            if (!verificationCode.getCode().equals(code)) {
                throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE,ErrorCode.INVALID_VERIFICATION_CODE.getMessage());

            }

            if (verificationCode.getExpiryDate().isBefore(LocalDateTime.now())) {
                verificationCodeRepository.delete(verificationCode); // 만료된 인증 코드 삭제

                throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE, ErrorCode.USER_NOT_FOUND.getMessage());
            }
        } catch (CustomException e) {
            throw new CustomException(ErrorCode.EMAIL_VERIFY_FAIL, ErrorCode.USER_NOT_FOUND.getMessage());

                throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE,ErrorCode.INVALID_VERIFICATION_CODE.getMessage());
            }
        } catch (CustomException e) {
            throw new CustomException(ErrorCode.EMAIL_VERIFY_FAIL,ErrorCode.EMAIL_VERIFY_FAIL.getMessage());

        }
    }

    @Override
    @Transactional
    public String confirmPassword(String token, PasswordConfirmDTO passwordConfirmDTO) {
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        User myUser = user.orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND,ErrorCode.USER_NOT_FOUND.getMessage())
        ); // 기존 유저의 비밀번호를 확인하기 위해 로그인한 유저의 정보를 가져와야함.

        if (!passwordEncoder.matches(passwordConfirmDTO.getConfirmPassword(), myUser.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH,ErrorCode.PASSWORD_MISMATCH.getMessage()); // 기존 비밀번호와 일치하지 않는 경우에 오류 던지기
        }
        // if 문 통과하면 성공!
        return SuccessCode.SUCCESS_PASSWORD_CONFIRM.getMessage();
    }

    @Override
    @Transactional
    public String signupUser(SignupRequestDTO signupRequestDTO) {
        // 인증된 이메일인지 확인
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(signupRequestDTO.getEmail())

                .orElseThrow(() -> new CustomException(ErrorCode.EMAIL_VERIFY_NEED, ErrorCode.USER_NOT_FOUND.getMessage()));

                .orElseThrow(() -> new CustomException(ErrorCode.EMAIL_VERIFY_NEED,ErrorCode.EMAIL_VERIFY_NEED.getMessage()));


        // 회원가입 로직
        User user = User.builder()
                .email(signupRequestDTO.getEmail())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .name(signupRequestDTO.getName())
                .nickName(signupRequestDTO.getNickName())
                .enabled(true)
                .build();

        userRepository.save(user);
        // 인증 코드 삭제
        verificationCodeRepository.delete(verificationCode);

        return SuccessCode.SUCCESS_SIGNUP.getMessage();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void cleanupExpiredVerificationCodes() {
        verificationCodeRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }

    // 비밀번호 재설정 코드 생성 및 저장
    @Override
    @Transactional
    public void createPasswordResetCode(String email) {
        // User 찾기
        userRepository.findByEmail(email)

                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));

                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND,ErrorCode.USER_NOT_FOUND.getMessage()));


        // 기존 인증 코드 삭제 또는 갱신
        verificationCodeRepository.deleteByEmail(email);

        // 랜덤 코드 생성
        String code = verificationCodeUtil.generateVerificationCode();

        // VerificationCode 생성
        VerificationCode verificationCode = VerificationCode.builder()
                .code(code)
                .email(email)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();
        verificationCodeRepository.save(verificationCode);

        // 이메일 발송
        emailService.sendPasswordReset(email, code);
    }

    @Override
    @Transactional(readOnly = true)
    public String validatePasswordResetCode(String code) {
        VerificationCode verificationCode = verificationCodeRepository.findByCode(code)

                .orElseThrow(() -> new CustomException(ErrorCode.PASSWORD_RESET_INVALID, ErrorCode.USER_NOT_FOUND.getMessage()));

        if (verificationCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.PASSWORD_RESET_INVALID, ErrorCode.USER_NOT_FOUND.getMessage());

                .orElseThrow(() -> new CustomException(ErrorCode.PASSWORD_RESET_INVALID,ErrorCode.PASSWORD_RESET_INVALID.getMessage()));

        if (verificationCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.PASSWORD_RESET_INVALID,ErrorCode.PASSWORD_RESET_INVALID.getMessage());

        }

        return "valid";
    }

    @Override
    @Transactional
    public void resetPassword(String code, String newPassword) {
        VerificationCode verificationCode = verificationCodeRepository.findByCode(code)

                .orElseThrow(() -> new CustomException(ErrorCode.PASSWORD_RESET_INVALID, ErrorCode.USER_NOT_FOUND.getMessage()));

        if (verificationCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            verificationCodeRepository.delete(verificationCode);
            throw new CustomException(ErrorCode.PASSWORD_RESET_INVALID, ErrorCode.USER_NOT_FOUND.getMessage());

                .orElseThrow(() -> new CustomException(ErrorCode.PASSWORD_RESET_INVALID,ErrorCode.PASSWORD_RESET_INVALID.getMessage()));

        if (verificationCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            verificationCodeRepository.delete(verificationCode);
            throw new CustomException(ErrorCode.PASSWORD_RESET_INVALID,ErrorCode.PASSWORD_RESET_INVALID.getMessage());

        }

        // email을 사용해 User 찾기
        User user = userRepository.findByEmail(verificationCode.getEmail())

                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));

                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND,ErrorCode.USER_NOT_FOUND.getMessage()));


        // 기존 비밀번호와 새 비밀번호 비교
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }

        // 비밀번호 업데이트
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 사용된 인증 코드 삭제
        verificationCodeRepository.delete(verificationCode);
    }

}