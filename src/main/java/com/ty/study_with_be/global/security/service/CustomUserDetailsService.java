package com.ty.study_with_be.global.security.service;

import com.ty.study_with_be.global.security.CustomUserDetails;
import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다."));
        return new CustomUserDetails(member);
    }
}