package com.WealthTracker.demo.DTO;

import com.WealthTracker.demo.domain.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SignupRequestDTOTest {
    private Validator validator;
    @BeforeEach
    void setUp(){
        ValidatorFactory validatorFactory= Validation.buildDefaultValidatorFactory();
        validator=validatorFactory.getValidator();
    }

    @Test
    @DisplayName("유효한 회원가입 데이터 테스트")
    void validSignupRequestTest() {
        // given
        SignupRequestDTO dto = SignupRequestDTO.builder()
                .email("tkv00@naver.com")
                .password("Abcd123!@")
                .name("김도연")
                .nickName("tkv000")
                .build();

        // when
        Set<ConstraintViolation<SignupRequestDTO>> violations = validator.validate(dto);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("이메일 검증 단위 테스트")
    void validEmail(){
        //given
        SignupRequestDTO dto = SignupRequestDTO.builder()
                .email("tkv00naver.com")
                .password("Abcd123!@")
                .name("김도연")
                .nickName("tkv000")
                .build();
        //when
        Set<ConstraintViolation<SignupRequestDTO>> violations=validator.validate(dto);
        //then
        assertThat(violations).hasSize(1);
    }


}