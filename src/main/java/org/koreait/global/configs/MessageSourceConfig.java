package org.koreait.global.configs;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageSourceConfig {

    /**
     * 메시지 코드 설정.
     * 사이트 설정할 때 초반에 무조건 작업 진행.
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.addBasenames("messages.commons", "messages.validations", "messages.errors", "messages.pokemon");
        ms.setDefaultEncoding("UTF-8");
        ms.setUseCodeAsDefaultMessage(true); // 키와 값으로 저장. 만약 value값이 없을 경우 키값인 코드 형태로 출력. 만약 false로 하면 코드형태로 출력되지 않음.

        return ms;
    }
}
