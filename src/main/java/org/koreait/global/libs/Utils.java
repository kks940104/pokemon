package org.koreait.global.libs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.koreait.file.entites.FileInfo;
import org.koreait.file.services.FileInfoService;
import org.koreait.member.libs.MemberUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Utils {

    private final FileInfoService fileInfoService;
    private final MessageSource messageSource;
    private final HttpServletRequest request;
    private final MemberUtil memberUtil;

    public boolean isMobile() {

        // 요청 헤더 - User-Agent / 브라우저 정보
        String ua = request.getHeader("User-Agent");
        String pattern = ".*(iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson).*";


        return StringUtils.hasText(ua) && ua.matches(pattern);
    }

    /**
     * mobile, front 템플릿 분리 함수
     *
     * @param path
     * @return
     */
    public String tpl(String path) {
        String prefix = isMobile() ? "mobile" : "front";

        return String.format("%s/%s", prefix, path);
    }

    /**
     * 메세지 코드로 조회된 문구
     * messageSource에서 조회.
     * @param code
     * @return
     */
    public String getMessage(String code) {
        Locale lo = request.getLocale(); // 사용자 요청 헤더(Accept-Language)

        return messageSource.getMessage(code, null, lo);
    }

    /**
     * 빈 문자열은 필요 없으니 버림.
     */
    public List<String> getMessages(String[] codes) {
        return Arrays.stream(codes).map(c -> {
            try {
                return getMessage(c);
            } catch (Exception e) {
                return "";
            }
        }).filter(s -> !s.isBlank()).toList();
    }

    /**
     * REST 커멘드 객체 검증 실패시에 검증 에러 코드를 가지고 메세지를 추출하는 기능
     */
    public Map<String, List<String>> getErrorMessages(Errors errors) {
        ResourceBundleMessageSource ms = (ResourceBundleMessageSource) messageSource;
        ms.setUseCodeAsDefaultMessage(false);

        try {
            // 필드별 에러코드 - getFieldErrors()
            // Collectors.toMap
            Map<String, List<String>> messages =
                    errors.getFieldErrors()
                            .stream()
                            .collect(Collectors.toMap
                                    (FieldError::getField,
                                            f -> getMessages(f.getCodes()),
                                            (v1, v2) -> v2));
            // V1, V2 -> 현재 toMap은 <,> 안에 key값이 있으면 오류 발생.
            // 그때 나오는게 매개변수 3개짜리인 마지막 BinaryOperator
            // v1(이전거), v2(현재꺼) -> value값을 바꿔줌.

            // 글로벌 에러코드 - getGlobalErrors()

            List<String> gMessages = errors.getGlobalErrors()
                    .stream()
                    .flatMap(o -> getMessages(o.getCodes())
                            .stream()).toList();
            // 글로벌 에러코드 필드 - global

            if (!gMessages.isEmpty()) {
                messages.put("global", gMessages);
            }

            return messages; // 임시.
        } finally {
            ms.setUseCodeAsDefaultMessage(true);
        }
    }

    /**
     * 이미지 출력
     * @param seq
     * @param width : 너비값
     * @param height : 높이값
     * @param mode - image : 이미지 태그로 출력, background : 배경 이미지 형태로 출력.
     * @return
     */

    public String showImage(Long seq, int width, int height, String mode, String className) {
        return showImage(seq, null, width, height, mode, className);
    }

    public String showImage(String url, int width, int height, String mode, String className) {
        return showImage(null, url, width, height, mode, className);
    }

    public String showImage(Long seq, int width, int height, String className) {
        return showImage(seq, null, width, height, "image", className);
    }

    public String showBackground(Long seq, int width, int height, String className) {
        return showImage(seq, null, width, height, "background", className);
    }

    public String showImage(String url, int width, int height, String className) {
        return showImage(null, url, width, height, "image", className);
    }

    public String showBackground(String url, int width, int height, String className) {
        return showImage(null, url, width, height, "Background", className);
    }

    public String showImage(Long seq, String url, int width, int height, String mode, String className) {
        try {
            String imageUrl = null;
            if (seq != null && seq > 0L) {
                FileInfo item = fileInfoService.get(seq);

                if (!item.isImage()) return "";

                imageUrl = String.format("%s&width=%d&height=%d", item.getThumbUrl(), width, height);

            } else if (StringUtils.hasText(url)) {
                imageUrl = String.format("%s/api/file/thumb?url=%s&width=%d&height=%d",request.getContextPath(), url, width, height);
            }

            if (!StringUtils.hasText(imageUrl)) return "";

            mode = Objects.requireNonNullElse(mode, "image");
            className = Objects.requireNonNullElse(className, "image");

            if (mode.equals("background")) return String.format("<div style='width: %dpx; height: %dpx; background: url(\"%s\") no-repeat center center; background-size: cover;' class='%s' %s></div>", width, height, imageUrl, className, seq != null && seq > 0L ? "data-seq=" + seq + "'" : ""); // 배경 이미지

            else return String.format("<img src='%s' class='%s'>", imageUrl, className); // 이미지 태그

        } catch (Exception e) {}
        return "";
    }

    /**
     * 메세지를 세션쪽에 저장해서 임시 팝업으로 띄운다.
     * @param message
     */
    public void showSessionMessage(String message) {
        HttpSession session = request.getSession();
        session.setAttribute("showMessage", message);
    }

    public void removeSessionMessage() {
        HttpSession session = request.getSession();
        session.removeAttribute("showMessage");
    }

    public String getParam(String name) {
        return request.getParameter(name);
    }

    public String[] getParams(String name) {
        return request.getParameterValues(name);
    }

    /**
     * 줄개행 문자 (\n 또는 \r\n 문자를 <br>태그로 변환
     * @param text
     * @return
     */
    public String nl2br(String text) {
        text = text == null ? "" : text.replaceAll("\\r", "")
                                       .replaceAll("\\n", "<br>");
        return text;
    }

    public String popup(String url, int width, int height) {
        return String.format("commonLib.popup('%s', %d, %d)", url, width, height);
    }

    // 회원, 비회원 구분 해시

    /**
     * 회원, 비회원 구분 해시
     * 회원 - 회원번호, 비회원 - IP + User-Agent
     * @return
     */
    public int getMemberHash() {
        if (memberUtil.isLogin()) return Objects.hash(memberUtil.getMember().getSeq()); // 회원
        else { // 비회원
            String ip = request.getRemoteAddr();
            String ua = request.getHeader("User-Agent");

            return Objects.hash(ip, ua);
        }
    }

    /**
     * 전체 주소
     * @param url
     * @return
     */
    public String getUrl(String url) {
        return String.format("%s://%s:%d%s%s", request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath(), url);
    }
}

















