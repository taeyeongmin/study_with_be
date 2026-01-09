package com.ty.study_with_be.security;

import com.ty.study_with_be.security.filter.JwtAuthenticationFilter;
import com.ty.study_with_be.security.handler.OAuth2LoginSuccessHandler;
import com.ty.study_with_be.security.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.NullSecurityContextRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2LoginSuccessHandler successHandler;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS는 전역 설정값을 그대로 사용.
                .cors(Customizer.withDefaults())
                // CSRF는 비활성화.( TODO: 쿠키를 사용해서 잡긴 해야할 듯함)
                .csrf(AbstractHttpConfigurer::disable)
                // 세션 기반 폼 로그인 기능 비활성화.
                .formLogin(AbstractHttpConfigurer::disable)
                // 브라우저 기본 인증 팝업 방지를 위해 HTTP Basic 비활성화.
                .httpBasic(AbstractHttpConfigurer::disable)
                // 서버 세션을 쓰지 않으므로 로그아웃 엔드포인트 비활성화.
                .logout(AbstractHttpConfigurer::disable)
                // 영구 로그인 쿠키 사용 방지를 위해 remember-me 비활성화.
                .rememberMe(AbstractHttpConfigurer::disable)
                // 요청 캐시로 인한 세션 생성 방지.
                .requestCache(AbstractHttpConfigurer::disable)
                // 인증 관련 엔드포인트만 비로그인 허용.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/oauth2/**", "/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                // JWT만 사용하므로 서버 세션은 생성하지 않음.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 세션에서 SecurityContext를 저장/복원하지 않음.
                .securityContext(security -> security.securityContextRepository(new NullSecurityContextRepository()))
                // OAuth2 로그인 성공 시 커스텀 핸들러 적용.
                .oauth2Login(oauth ->
                        oauth.successHandler(successHandler)
                );
        // JWT 인증 필터를 기본 인증 필터 앞에 배치.
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
