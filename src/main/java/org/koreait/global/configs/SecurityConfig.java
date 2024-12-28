package org.koreait.global.configs;


import jakarta.servlet.http.HttpSession;
import org.koreait.member.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 스프링 시큐리티 설정.
 */
@Configuration
@EnableMethodSecurity // 요청 메서드로 권한 통제 가능.
public class SecurityConfig {

    @Autowired
    private MemberInfoService memberInfoService;

    @Autowired
    private HttpSession session;

    // 이 수동빈 정말 중요.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // region 인증 설정 - 로그인, 로그아웃

        /**
         * 로그인 처리를 대신 해줌.
         */
        http.formLogin(c -> {
            c.loginPage("/member/login") // 로그인 방식을 처리할 주소
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .failureHandler(new LoginFailureHandler())
                    .successHandler(new LoginSuccessHandler());
        });

        /**
         * 로그아웃 설정. 로그아웃 주소는 결정된게 아니기 때문에 따로 추가.
         */
/*        http.logout(c -> {
            c.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                    .logoutSuccessHandler((req,  res, auth) -> {
                        session.setAttribute("member", null);
                        res.sendRedirect(req.getContextPath() + "/member/login");
            });
        });*/

        http.logout(c -> {
            c.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                    .logoutSuccessUrl("/member/login");
        });

        // endregion

        // region 인증 설정 - 페이지 접근 통제

        /**
         * 접근 제한 통제 설정.
         * authenticated() : 인증받은 사용자만 접근.
         * anonymous() : 인증받지 않은 사용자만 접근.
         * permitAll() : 모든 사용자가 접근 가능.
         * hasAuthority("권한 명칭") : 하나의 권한을 체크
         * hasAnyAuthority("권한1", "권한2", ... ) : 나열된 권한 중 하나라도 충족하면 접근 가능
         * ROLE
         * ROLE_명칭
         * hasRole("명칭")
         * hasAnyRole(...)
         */

        http.authorizeHttpRequests(c -> {
            c.requestMatchers("/mypage/**").authenticated() // 인증한 회원
                    .requestMatchers("/game/**").authenticated() // 게임도 인증한 회원만 가능.
                    .requestMatchers("/member/login", "/member/join", "/member/agree").anonymous() // 미인증 회원
                    .requestMatchers("/admin/**").hasAnyAuthority("MANAGER", "ADMIN") // 관리자 페이지는 MANAGER, ADMIN 권한
                    .anyRequest().permitAll(); // 나머지 페이지는 모두 접근 가능
        });

        /**
         *
         */
        http.exceptionHandling(c -> {
            c.authenticationEntryPoint(new MemberAuthenticationExceptionHandler()) // 미로그인시 인가 실패
                    .accessDeniedHandler(new MemberAccessDeniedHandler()); // 로그인 이후 인가 실패
        });

        // endregion

        // region 자동 로그인 설정

        // rememberMe 쿠키가 나옴.
        http.rememberMe(c -> {
           c.rememberMeParameter("autoLogin") // rememberMe 파라미터.
                   .tokenValiditySeconds(60 * 60 * 24 * 30)// 자동 로그인을 유지할 시간. 초단위. default 14일
                   .userDetailsService(memberInfoService) // 조회할 Service
                   .authenticationSuccessHandler(new LoginSuccessHandler()); // 성공시 Callback...
        });

        // endregion

        http.headers(c -> c.frameOptions(o -> o.sameOrigin()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
