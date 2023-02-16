package xyz.jxzou.zblog.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import xyz.jxzou.zblog.auth.filter.JwtTokenFilter;
import xyz.jxzou.zblog.auth.handler.*;
import xyz.jxzou.zblog.auth.service.AuthService;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class AuthorizationSecurityConfiguration {

    private final JwtTokenFilter jwtTokenFilter;
    private final AuthService authService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailedHandler loginFailedHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final AuthenticationFailedHandler authenticationFailedHandler;
    private final AuthenticationAccessDeniedHandler accessDeniedHandler;

    @Autowired
    public AuthorizationSecurityConfiguration(JwtTokenFilter jwtTokenFilter, AuthService authService,
                                              LoginSuccessHandler loginSuccessHandler,
                                              LoginFailedHandler loginFailedHandler,
                                              LogoutSuccessHandler logoutSuccessHandler,
                                              AuthenticationFailedHandler authenticationFailedHandler,
                                              AuthenticationAccessDeniedHandler accessDeniedHandler) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.authService = authService;
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailedHandler = loginFailedHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.authenticationFailedHandler = authenticationFailedHandler;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable();

        http
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationFailedHandler));

        http
                .authorizeHttpRequests(authorize -> authorize
                        .shouldFilterAllDispatcherTypes(true)
                        .mvcMatchers("/login","/registry","/pkey").servletPath("/api").permitAll()
                        .anyRequest().authenticated());

        http
                .rememberMe(remember -> remember
                        .rememberMeCookieName("rmc")
                        .userDetailsService(authService))
                .formLogin(login -> login
                        .loginPage("/login")
                        .usernameParameter("account")
                        .passwordParameter("password")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailedHandler))
                .oauth2Login(oath2 -> oath2
                        .loginPage("/oath")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailedHandler))
                .oauth2Client(oath2 -> oath2
                        .clientRegistrationRepository())
                .logout(logout -> logout
                        .clearAuthentication(true)
                        .deleteCookies()
                        .logoutSuccessHandler(logoutSuccessHandler))
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
}
