package com.project.shopapp.configurations;

import com.project.shopapp.filters.JwtTokenFilter;
import com.project.shopapp.models.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests ->{
                    requests
                            .requestMatchers(
                                String.format("%s/users/login", apiPrefix),
                                String.format("%s/users/register", apiPrefix)
                            )
                            .permitAll()

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/roles**", apiPrefix)).permitAll()

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/categories**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/categories/**", apiPrefix)).hasRole(RoleEntity.ADMIN)
                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/categories/**", apiPrefix)).hasRole(RoleEntity.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/categories/**", apiPrefix)).hasRole(RoleEntity.ADMIN)

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/products**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/products/images**", apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/products/uploads**", apiPrefix)).hasRole(RoleEntity.ADMIN)
                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/products/**", apiPrefix)).hasRole(RoleEntity.ADMIN)
                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/products/**", apiPrefix)).hasRole(RoleEntity.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/products/**", apiPrefix)).hasRole(RoleEntity.ADMIN)

                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/orders/**", apiPrefix)).hasRole(RoleEntity.USER)
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/orders/**", apiPrefix)).hasAnyRole(RoleEntity.USER,RoleEntity.ADMIN)
                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/orders/**", apiPrefix)).hasRole(RoleEntity.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/orders/**", apiPrefix)).hasRole(RoleEntity.ADMIN)

                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/order_details/**", apiPrefix)).hasRole(RoleEntity.USER)
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/order_details/**", apiPrefix)).hasAnyRole(RoleEntity.USER,RoleEntity.ADMIN)
                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/order_details/**", apiPrefix)).hasRole(RoleEntity.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/order_details/**", apiPrefix)).hasRole(RoleEntity.ADMIN)
                            .anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable);
        http.cors(httpSecurityCorsConfigurer -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(List.of("*"));
            corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
            corsConfiguration.setAllowedHeaders(Arrays.asList("authorization","content-type","x-auth-token"));
            corsConfiguration.setExposedHeaders(List.of("x-auth-token"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**",corsConfiguration);
            httpSecurityCorsConfigurer.configurationSource(source);
        });
        return http.build();
    }
}
