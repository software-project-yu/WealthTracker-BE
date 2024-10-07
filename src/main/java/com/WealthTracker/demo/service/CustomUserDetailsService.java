package com.WealthTracker.demo.service;

import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // email을 기반으로 사용자 정보 로드
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 가진 유저를 찾을 수 없습니다.: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(), // 계정 활성화 여부
                true, // 계정 만료 여부
                true, // 자격 증명 만료 여부
                true, // 계정 잠금 여부
                Collections.emptyList() // 권한 목록 (필요 시 추가)
        );
    }
}