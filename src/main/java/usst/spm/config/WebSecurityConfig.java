package usst.spm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import usst.spm.filter.UserAuthFilter;

/**
 * @author jyzxc
 * @since 2024-11-12
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    private final UserAuthFilter userAuthFilter;
    @Autowired
    public WebSecurityConfig(UserAuthFilter userAuthFilter) {
        this.userAuthFilter = userAuthFilter;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http    // 禁用CSRF保护
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用请求缓存
                .requestCache(RequestCacheConfigurer::disable)
                //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().access((authentication, object) -> new AuthorizationDecision(true))
                );

        http.addFilterBefore(userAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // http.exceptionHandling(exception -> exception.authenticationEntryPoint(myAuthenticationEntryPoint).accessDeniedHandler(myAccessDeniedHandler));

        return http.build();
    }
}
