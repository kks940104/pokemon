package org.koreait.member.libs;

import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.koreait.member.MemberInfo;
import org.koreait.member.constants.Authority;
import org.koreait.member.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// @Lazy // Lazy는 순환참조 방지를 해 주는 역할도 있는거같음. - 추측. 근데 오류남ㅋ
@Component
public class MemberUtil {

    @Autowired
    private HttpSession session;

    public boolean isLogin() {
        return getMember() != null;
    }

    /**
     * 관리자 여부
     * 권한 - MANAGER, ADMIN
     * @return
     */

   public boolean isAdmin() {
       return isLogin() && getMember()
               .getAuthorities()
               .stream()
               .anyMatch(a -> a.getAuthority() == Authority.ADMIN
                       || a.getAuthority() == Authority.MANAGER);
   }

    /**
     * 로그인 한 회원의 정보
     * @return
     */

    public Member getMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // 인증을 해야 객체가 만들어짐.

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof MemberInfo memberInfo) {
            Member member = (Member) session.getAttribute("member");
            if (member == null) {
                member = memberInfo.getMember();
                session.setAttribute("member", member);
            }
            return member;
        }

        return null;
    }
}
