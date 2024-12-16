package org.koreait.global.configs;

import lombok.RequiredArgsConstructor;
import org.koreait.member.libs.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * AuditorAware<String> 데이터가 있는 모든 프레임워크는 이게 있음
 */
@Lazy
@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    private final MemberUtil memberUtils;

    @Override
    public Optional getCurrentAuditor() {
        String email = null;
        if (memberUtils.isLogin()) {
            email = memberUtils.getMember().getEmail();
        }
        return Optional.ofNullable(email);
    }
}
