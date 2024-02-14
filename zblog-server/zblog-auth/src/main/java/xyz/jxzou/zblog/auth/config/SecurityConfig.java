package xyz.jxzou.zblog.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import xyz.jxzou.zblog.auth.filter.JwtTokenFilter;
import xyz.jxzou.zblog.auth.handler.*;
import xyz.jxzou.zblog.auth.service.AuthService;
import xyz.jxzou.zblog.core.pojo.SafetyManager;

import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final SafetyManager safetyManager;
    private final JwtTokenFilter jwtTokenFilter;
    private final AuthService authService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailedHandler loginFailedHandler;
    private final AuthenticationAccessDeniedHandler accessDeniedHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final CsrfDeniedHandler csrfDeniedHandler;

    @Autowired
    public SecurityConfig(SafetyManager safetyManager, JwtTokenFilter jwtTokenFilter, AuthService authService, LoginSuccessHandler loginSuccessHandler, LoginFailedHandler loginFailedHandler, AuthenticationAccessDeniedHandler accessDeniedHandler, LogoutSuccessHandler logoutSuccessHandler, CsrfDeniedHandler csrfDeniedHandler) {
        this.safetyManager = safetyManager;
        this.jwtTokenFilter = jwtTokenFilter;
        this.authService = authService;
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailedHandler = loginFailedHandler;
        this.accessDeniedHandler = accessDeniedHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.csrfDeniedHandler = csrfDeniedHandler;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                .pathMatchers(HttpMethod.POST, "/login", "logout", "/registry", "/pkey", "/mail/captcha").permitAll()
                .pathMatchers("/api/search/**").permitAll()
                .anyExchange().authenticated()
        );

        http.formLogin(loginSpec -> loginSpec
                .loginPage("/login")
                .authenticationFailureHandler(loginFailedHandler)
                .authenticationSuccessHandler(loginSuccessHandler)
        );

        http.logout(logoutSpec -> logoutSpec
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
        );

        http.exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                .accessDeniedHandler(accessDeniedHandler)
        );

        http.csrf(csrfSpec -> csrfSpec
                .accessDeniedHandler(csrfDeniedHandler)
        );

        http.addFilterBefore(jwtTokenFilter, SecurityWebFiltersOrder.FORM_LOGIN);
        return http.build();
    }

    ReactiveUserDetailsService

    @Bean
    RememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices(safetyManager.getRsaPublicKey().toString(), authService);
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("cid",HttpHeaders.AUTHORIZATION));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","OPTIONS"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("https://www.jxzou.xyz","http://127.0.0.1","http://localhost"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
