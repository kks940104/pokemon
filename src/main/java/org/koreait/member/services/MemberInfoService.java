package org.koreait.member.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.admin.member.controllers.MemberSearch;
import org.koreait.file.entites.FileInfo;
import org.koreait.file.services.FileInfoService;
import org.koreait.global.paging.ListData;
import org.koreait.global.paging.Pagination;
import org.koreait.member.MemberInfo;
import org.koreait.member.constants.Authority;
import org.koreait.member.entities.Authorities;
import org.koreait.member.entities.Member;
import org.koreait.member.entities.QMember;
import org.koreait.member.reporitories.MemberRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Spring Security는 UserDetailsService 구현해야함.
 */
@Lazy
@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final FileInfoService fileInfoService;
    private final JPAQueryFactory queryFactory;
    private final HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));

        List<Authorities> items = member.getAuthorities();
        if (items == null) {
            Authorities auth = new Authorities();
            auth.setMember(member);
            auth.setAuthority(Authority.USER);
            items = List.of(auth);
        }

        List<SimpleGrantedAuthority> authorities = items.stream().map(a -> new SimpleGrantedAuthority(a.getAuthority().name())).toList();

        // 추가 정보 처리.

        addInfo(member);

        return MemberInfo.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .member(member)
                .authorities(authorities)
                .build();
    }

    /**
     * 회원 목록
     * @param search
     * @return
     */
    public ListData<Member> getList(MemberSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;
        int offset = (page - 1) * limit;
        QMember member = QMember.member;

        // region 검색 처리

        BooleanBuilder andBuilder = new BooleanBuilder();

        // region 키워드 검색

        String sopt = search.getSopt(); // 검색 옵션
        String skey = search.getSkey(); // 검색 키워드
        sopt = StringUtils.hasText(sopt) ? sopt : "ALL";

        /**
         * sopt - ALL : 통합 검색 - 이메일 + 회원명 + 닉네임
         *       NAME : 회원명 + 닉네임
         *      EMAIL : 이메일
         */
        if (StringUtils.hasText(skey)) {
            skey = skey.trim();
            StringExpression condition;
            if (sopt.equals("EMAIL")) {
                condition = member.email;
            } else if (sopt.equals("NAME")) {
                condition = member.email.concat(member.nickName);
            } else {
                condition = member.email.concat(member.name).concat(member.nickName);
            }

            andBuilder.and(condition.contains(skey));
        }

        // endregion

        // region 이메일 검색

        List<String> emails = search.getEmail();

        if (emails != null && !emails.isEmpty()) {
            andBuilder.and(member.email.in(emails));
        }

        // endregion

        // region 권한 검색

        List<Authority> authorities = search.getAuthority();
        if (authorities != null && !authorities.isEmpty()) {
/*            List<Authorities> _authorities = authorities.stream()
                            .map(a -> )
            andBuilder.and(member.authorities)*/
        }

        // endregion

        // region 날짜 검색

        String dateType = search.getDateType();
        dateType = StringUtils.hasText(dateType) ? dateType : "createdAt"; // 가입일 기준
        LocalDate sDate = search.getSDate();
        LocalDate eDate = search.getEDate();

        DateTimePath<LocalDateTime> condition;
        if (dateType.equals("deletedAt")) condition = member.deletedAt; // 탈퇴일 기준
        else if (dateType.equals("credentialChangedAt")) condition = member.credentialChangedAt; // 비밀번호 변경 기준
        else condition = member.createdAt; // 가입일 기준

        if (sDate != null) {
            andBuilder.and(condition.after(sDate.atStartOfDay()));
        }

        if (eDate != null) {
            andBuilder.and(condition.before(eDate.atTime(LocalTime.of(23,59,59))));
        }

        // endregion

        // endregion

        List<Member> items = queryFactory.selectFrom(member)
                .leftJoin(member.authorities)
                .fetchJoin()
                .where(andBuilder)
                .orderBy(member.createdAt.desc())
                .offset(offset)
                .limit(limit)
                .fetch();

        long total = memberRepository.count(andBuilder); // 총 회원 수
        Pagination pagination = new Pagination(page, (int)total, 10, limit, request);

        return new ListData<>(items, pagination);
    }

    /**
     * 추가 정보 처리
     * @param member
     */
    public void addInfo(Member member) {
        List<FileInfo> files = fileInfoService.getList(member.getEmail(), "profile");
        if (files != null && !files.isEmpty()) {
            member.setProfileImage(files.get(0));
        }
    }
}
