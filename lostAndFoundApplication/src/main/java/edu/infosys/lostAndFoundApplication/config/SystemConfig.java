package edu.infosys.lostAndFoundApplication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // <-- ADD
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler; // <-- ADD
import org.springframework.security.web.authentication.AuthenticationSuccessHandler; // <-- ADD
import org.springframework.security.web.authentication.HttpStatusEntryPoint; // <-- ADD for unauthorized API calls
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler; // <-- ADD for logout
import org.springframework.http.HttpStatus; // <-- ADD
import jakarta.servlet.http.HttpServletResponse; // <-- ADD
import org.springframework.security.web.context.HttpSessionSecurityContextRepository; // Added back for clarity


import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SystemConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3939");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authenticationProvider(authenticationProvider())
                .securityContext(context -> context
                        .securityContextRepository(new HttpSessionSecurityContextRepository())
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/lost-found/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(successHandler())
                        .failureHandler(failureHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/lost-found/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .permitAll()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .authorizeHttpRequests(authorize -> authorize
                        //Public Endpoints for registration
                        .requestMatchers(HttpMethod.POST, "/lost-found/register").permitAll()

                        // login-logout endpoints
                        .requestMatchers(HttpMethod.POST, "/lost-found/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/lost-found/logout").permitAll()

                        // User Detail Endpoint
                        .requestMatchers(HttpMethod.GET, "/lost-found/user/details").authenticated()

                        //Admin Role based endpoints:
                        .requestMatchers(HttpMethod.GET, "/lost-found/lost-items", "/lost-found/found-items").hasRole("ADMIN")
                        .requestMatchers("/lost-found/admin/students", "/lost-found/admin/student/**").hasRole("ADMIN")

                        //Student Role based endpoints:
                        .requestMatchers(HttpMethod.POST, "/lost-found/lost-items", "/lost-found/found-items").hasRole("STUDENT")
                        .requestMatchers("/lost-found/lost-items/user", "/lost-found/found-items/user").hasRole("STUDENT")
                        .requestMatchers("/lost-found/fuzzy/**").hasRole("STUDENT")
                        .requestMatchers("/lost-found/lost-items/{id}", "/lost-found/found-items/{id}").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.DELETE, "/lost-found/lost-items/{id}").hasRole("STUDENT")

                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                    .orElse("USER");

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write("{\"role\": \"" + role + "\"}");
            response.getWriter().flush();
        };
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid Credentials\"}");
            response.getWriter().flush();
        };
    }
}