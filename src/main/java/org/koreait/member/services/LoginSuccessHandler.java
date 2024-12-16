package org.koreait.member.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 로그인 성공시 핸들러. AuthenticationSuccessHandler 구현체 받아야함.
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    /**
     *
     * @param request : request
     * @param response : response
     * @param authentication : 인증.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();

        // requestLogin 세션값 비우기 남아있으면 로그아웃 하고 다시 로그인할 때 남아있으니까.
        session.removeAttribute("requestLogin");

        // UserDetail 구현체

        // MemberInfo memberInfo = (MemberInfo) authentication.getPrincipal();
        // System.out.println(memberInfo);

        /**
         * 로그인 성공 시 페이지 이동.
         * 1) redirectUrl에 지정된 주소로 이동.
         * 2) redirectUrl이 없는 경우는 메인 페이지로 이동
         */

        String redirectUrl = request.getParameter("redirectUrl");
        redirectUrl = StringUtils.hasText(redirectUrl) ? redirectUrl : "/";

        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
