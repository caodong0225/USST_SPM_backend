package usst.spm.config;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import usst.spm.exception.MyAccessDeniedHandler;
import usst.spm.exception.MyAuthenticationEntryPoint;
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
    private final SecurityPathsConfig securityPathsConfig;

    @Autowired
    public WebSecurityConfig(UserAuthFilter userAuthFilter, SecurityPathsConfig securityPathsConfig) {
        this.userAuthFilter = userAuthFilter;
        this.securityPathsConfig = securityPathsConfig;
    }

    @Resource
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;
    @Resource
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .requestCache(RequestCacheConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    // 配置公共路径的权限
                    securityPathsConfig.getPublicRoutes().forEach(path -> auth.requestMatchers(path).permitAll());
                    // 配置需要认证的路径权限
                    securityPathsConfig.getAuthenticated().forEach(path -> auth.requestMatchers(path).authenticated());
                    // 其他路径默认需要认证
                    auth.anyRequest().access((authentication, object) -> {
                        if ("anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
                            return new AuthorizationDecision(false);
                        }
                        return new AuthorizationDecision(true);
                    });
                });

        http.addFilterBefore(userAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                .accessDeniedHandler(myAccessDeniedHandler));

        return http.build();
    }
}