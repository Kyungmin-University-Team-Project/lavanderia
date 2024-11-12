package com.kyungmin.lavanderia.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    /*@Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
         corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 허용할 출처
                .allowedMethods("*") // 모든 HTTP 메서드 허용
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true) // 자격 증명 허용
                .exposedHeaders("Set-Cookie", "Authorization"); // 'Set-Cookie' 및 'Authorization' 헤더 노출
    }*/

}
