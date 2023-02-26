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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import xyz.jxzou.zblog.auth.filter.ClientFilter;
import xyz.jxzou.zblog.auth.filter.JwtTokenFilter;
import xyz.jxzou.zblog.auth.handler.AuthenticationAccessDeniedHandler;
import xyz.jxzou.zblog.auth.handler.LoginFailedHandler;
import xyz.jxzou.zblog.auth.handler.LoginSuccessHandler;
import xyz.jxzou.zblog.auth.handler.LogoutSuccessHandler;
import xyz.jxzou.zblog.auth.service.AuthService;

import java.util.Arrays;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final ClientFilter clientFilter;
    private final JwtTokenFilter jwtTokenFilter;
    private final AuthService authService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailedHandler loginFailedHandler;
    private final LogoutHandler logoutHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final AuthenticationAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.POST,"/api/search/**").permitAll()
                        .mvcMatchers(HttpMethod.POST,
                                "/login",
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
                        .addLogoutHandler(logoutHandler)
                        .clearAuthentication(true)
                        .deleteCookies()
                        .logoutSuccessHandler(logoutSuccessHandler));

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
        http.addFilterBefore(clientFilter, JwtTokenFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:9000"));
        corsConfiguration.addExposedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }
}
