package org.koreait.member.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.global.paging.ListData;
import org.koreait.global.paging.Pagination;
import org.koreait.member.constants.Authority;
import org.koreait.member.controllers.RequestJoin;
import org.koreait.member.entities.Authorities;
import org.koreait.member.entities.Member;
import org.koreait.member.entities.QAuthorities;
import org.koreait.member.libs.MemberUtil;
import org.koreait.member.reporitories.AuthoritiesRepository;
import org.koreait.member.reporitories.MemberRepository;
import org.koreait.mypage.controllers.RequestProfile;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Lazy // 지연로딩 - 최초의 빈을 사용할 때 생성
@RequiredArgsConstructor
@Transactional
public class MemberUpdateService {

    private final MemberRepository memberRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final MemberUtil memberUtil;
    private final MemberInfoService infoService;


    /**
     * 커멘드 객체의 타입에 따라서 RequestJoin이면 회원가입 처리
     *                          RequestProfile이면 회원정보 수정 처리
     * @param form
     */
    public void process(RequestJoin form) {
        // 커멘드 객체 -> 엔티티 객체 데이터 옮기기
        Member member = modelMapper.map(form, Member.class);

        // 선택 약관 -> 약관 항목1 || 약관 항목2 || ...
        List<String> optionalTerms = form.getOptionalTerms();
        if (optionalTerms != null) {
            member.setOptionalTerms(String.join("||", optionalTerms));
        }


        // 비밀번호 해시화 - BCrypt
        String hash = passwordEncoder.encode(form.getPassword());
        member.setPassword(hash);
        member.setCredentialChangedAt(LocalDateTime.now());

        // 회원 권한
        Authorities auth = new Authorities();
        auth.setMember(member);
        auth.setAuthority(Authority.USER); // 회원 권한이 없는 경우 - 회원 가입시, 기본 권한 USER
        System.out.println(auth);

        save(member, List.of(auth)); // 회원 저장 처리
    }

    /**
     * 회원정보 수정.
     * @param form
     */
    public void process(RequestProfile form) {
        process(form, null);
    }

    public void process(RequestProfile form, List<Authority> authorities) {
        Member member = memberUtil.getMember(); // 로그인한 사용자의 정보를 가지고온다.
        member.setName(form.getName());
        member.setNickName(form.getNickName());
        member.setBirthDt(form.getBirthDt());
        member.setGender(form.getGender());
        member.setZipCode(form.getZipCode());
        member.setAddress(form.getAddress());
        member.setAddressSub(form.getAddressSub());

        List<String> optionalTerms = form.getOptionalTerms();
        if (optionalTerms != null) {
            member.setOptionalTerms(String.join("||", optionalTerms));
        }

        // 회원정보 수정일땐 비밀번호가 입력된 경우만 저장.
        String password = form.getPassword();

        if (StringUtils.hasText(password)) {
            String hash = passwordEncoder.encode(password);
            member.setPassword(hash);
            member.setCredentialChangedAt(LocalDateTime.now());
        }

        /**
         * 회원 권한은 관리자만 수정 가능하게 통제.
         */

        List<Authorities> _authorities = null;

        if (authorities != null && memberUtil.isAdmin()) {
            _authorities = authorities.stream().map(a -> {
               Authorities auth = new Authorities();
               auth.setAuthority(a);
               auth.setMember(member);
               return auth;
            }).toList();
        }

        save(member, _authorities);

        // region 로그인 회원 정보 업데이트

        Member _member = memberRepository.findByEmail(member.getEmail()).orElse(null);
        if (_member != null) {
            infoService.addInfo(_member);
            memberUtil.setMember(_member);
        }
        // endregion
    }

    /**
     * 회원정보 추가 또는 수정 처리
     *
     */
    private void save(Member member, List<Authorities> authorities) {
        memberRepository.saveAndFlush(member);

        System.out.println(authorities);

        // region 회원 권한 업데이트 처리

        if (authorities != null) {
            /**
             * 기존 권한을 삭제하고 다시 등록
             */
            QAuthorities qAuthorities = QAuthorities.authorities;
            List<Authorities> items = (List<Authorities>) authoritiesRepository.findAll(qAuthorities.member.eq(member));

            System.out.println(qAuthorities);

            if (items != null) {
                authoritiesRepository.deleteAll(items);
                authoritiesRepository.flush();
            }

            authoritiesRepository.saveAllAndFlush(authorities);
            //authoritiesRepository.flush();
        }

        // endregion

    }
}
