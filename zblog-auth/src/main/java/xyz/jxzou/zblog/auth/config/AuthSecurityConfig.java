package xyz.jxzou.zblog.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import xyz.jxzou.zblog.auth.filter.JwtTokenFilter;
import xyz.jxzou.zblog.auth.handler.*;
import xyz.jxzou.zblog.auth.service.AuthService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class AuthSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final AuthService authService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailedHandler loginFailedHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final AuthenticationFailedHandler authenticationFailedHandler;
    private final AuthenticationAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                .shouldFilterAllDispatcherTypes(true)
                .mvcMatchers(HttpMethod.POST,"/login/**", "/registry", "/pkey","/mail/send/captcha").servletPath("/api").permitAll()
                .anyRequest().authenticated());

        http
                .rememberMe(remember -> remember
                .rememberMeCookieName("rmc")
                .userDetailsService(authService));

        http
                .formLogin(login -> login
                .loginPage("/login")
                .usernameParameter("account")
                .passwordParameter("password")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailedHandler));

        http
                .logout(logout -> logout
                .clearAuthentication(true)
                .deleteCookies()
                .logoutSuccessHandler(logoutSuccessHandler));

        http
                .exceptionHandling(exception -> exception
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationFailedHandler));

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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }
}
