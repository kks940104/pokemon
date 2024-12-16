package org.koreait.global.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaAuditing // 리스너 설정 변화감지.
@EnableScheduling // 스케줄 변화감지.
@EnableRedisHttpSession // 이걸 설정해서 세션이 Redis에 저장될 수 있게 만들어주는 에너테이션.
public class MvcConfig implements WebMvcConfigurer {
    /**
     * 정적 경로 설정, CSS, JS, 이미지
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    /**
     * Patch, Delete, Put 메서드로 요청을 보내는 경우 사용해야하는 설정.
     * <form method='post'......>
     *      <input type='hidden' name='_method' value='PATCH'>
     * </form>
     */
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
