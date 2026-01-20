package com.ty.study_with_be.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.study_with_be.global.security.filter.JwtAuthenticationFilter;
import com.ty.study_with_be.global.security.filter.JwtLoginFilter;
import com.ty.study_with_be.global.security.handler.ApiAuthenticationEntryPoint;
import com.ty.study_with_be.global.security.handler.LoginFailureHandler;
import com.ty.study_with_be.global.security.handler.LoginSuccessHandler;
import com.ty.study_with_be.global.security.handler.OAuth2LoginSuccessHandler;
import com.ty.study_with_be.global.security.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2LoginSuccessHandler oAuthSuccessHandler;

    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    private final ApiAuthenticationEntryPoint apiAuthenticationEntryPoint;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

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
                        .requestMatchers(HttpMethod.GET, "/api/study_group/**").permitAll()
                        .anyRequest().authenticated()
                )
                // JWT만 사용하므로 서버 세션은 생성하지 않음.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 세션에서 SecurityContext를 저장/복원하지 않음.
                .securityContext(security -> security.securityContextRepository(new NullSecurityContextRepository()))
                .exceptionHandling(exception -> exception
                        .defaultAuthenticationEntryPointFor(
                                apiAuthenticationEntryPoint,
                                new AntPathRequestMatcher("/api/**")
                        )
                )
                // OAuth2 로그인 성공 시 커스텀 핸들러 적용.
                .oauth2Login(oauth ->
                        oauth.successHandler(oAuthSuccessHandler)
                );

        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(authenticationManager, objectMapper);
        jwtLoginFilter.setFilterProcessesUrl("/api/auth/login");
        jwtLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        jwtLoginFilter.setAuthenticationFailureHandler(loginFailureHandler);

        // JWT 인증 필터를 기본 인증 필터 앞에 배치.
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        //  Local 로그인 필터 (JWT 발급)
        http.addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
