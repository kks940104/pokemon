package org.koreait.global.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 설정 클래스이기 때문에 넣음
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class) // 파일설정 후 여기서 설정을 이렇게 해야함. Class.class
public class FileConfig implements WebMvcConfigurer { // 스프링 기본 설정 추가 시 WebMvcConfigurer implements를 해줘야함.
    private final FileProperties properties; // 파일설정. 정적 경로.

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(properties.getUrl() + "**") // Upload 경로 설정.
                .addResourceLocations("file:///" + properties.getPath());

    }
}