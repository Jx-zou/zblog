package xyz.jxzou.zblog.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import xyz.jxzou.zblog.auth.filter.JwtTokenFilter;
import xyz.jxzou.zblog.auth.handler.AuthenticationAccessDeniedHandler;
import xyz.jxzou.zblog.auth.handler.LoginFailedHandler;
import xyz.jxzou.zblog.auth.handler.LoginSuccessHandler;
import xyz.jxzou.zblog.auth.service.AuthService;
import xyz.jxzou.zblog.common.core.domain.pojo.SafetyManager;

import java.util.Arrays;

@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final SafetyManager safetyManager;
    private final JwtTokenFilter jwtTokenFilter;
    private final AuthService authService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailedHandler loginFailedHandler;
    private final AuthenticationAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                        .addHeaderWriter());

        http
                .httpBasic()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,
                                )
                        .antMatchers("/api/search/**").permitAll()

                        .mvcMatchers(HttpMethod.POST,
                                "/login",
                                "logout",
                                "/registry",
                                "/pkey",
                                "/mail/captcha").servletPath("/api").permitAll()
                        .anyRequest().authenticated());

        http
                .rememberMe(remember -> remember
                        .userDetailsService(authService));

        http
                .formLogin(login -> login
                        .loginPage("/login")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailedHandler));

        http
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .clearAuthentication(true));

        http
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler));

        http
                .cors().and().csrf().disable()
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers().cacheControl();

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    RememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices(safetyManager.getRsaPublicKey().toString(), authService);
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

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }
}
