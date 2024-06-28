/**
 * Esse arquivo é a configuração de segurança da aplicação usando Spring Security.
 */

package me.todolist.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import me.todolist.security.jwt.AuthEntryPointJwt;
import me.todolist.security.jwt.AuthTokenFilter;
import me.todolist.security.service.Impl.UserDetailsServiceImpl;

// Define que a classe é uma fonte de configuração de beans
// Adiciona a segurança a nível de método
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

  // Injeta o caminho do console h2 nas propriedades
  // @Value("${spring.h2.console.path}")
  // private String h2ConsolePath;

  // Injeta o serviço de detalhes do usuário personalizado.
  @Autowired
  UserDetailsServiceImpl userDetailsServiceImpl;

  // Injeta o ponto de entrada para lidar com acessos não autorizados.
  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwAuthTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsServiceImpl);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(new AntPathRequestMatcher("/user/auth/**")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/swagger-ui.html")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
            // .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
            // .requestMatchers(new AntPathRequestMatcher("/h2-ui/**")).permitAll()
            .anyRequest().authenticated());
    http.headers(headers -> headers.frameOptions(frameOption -> frameOption.sameOrigin()));
    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
