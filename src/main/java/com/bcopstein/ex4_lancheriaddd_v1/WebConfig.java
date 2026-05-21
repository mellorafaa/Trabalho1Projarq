package com.bcopstein.ex4_lancheriaddd_v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Seguranca.JWTFilter;

/**
 * Configuração do Spring MVC para registrar interceptadores
 * Responsável por aplicar o JWTFilter a todas as requisições
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    private final JWTFilter jwtFilter;
    
    @Autowired
    public WebConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtFilter)
            .addPathPatterns("/**")
            .excludePathPatterns("/autenticacao/login", "/h2/**", "/health");
    }
}
