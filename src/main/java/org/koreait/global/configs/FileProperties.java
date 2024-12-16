package org.koreait.global.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "file.upload")
public class FileProperties {

    // application.yml 파일 안에 있는 파일 설정의 url과 path에 접근
    // @ConfigurationProperties(prefix = "file.upload") 를 사용함으로써 path와 url을 설정해준다.
    // 범주화 설정.
    private String path;
    private String url;
}