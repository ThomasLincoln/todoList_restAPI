/*
 * Esse código define o filtro AuthTokenFilter, que é responsável por verificar
 * a presença e validade de um token JWT (JSON Web Token) em cada requisição 
 * HTTP recebida pela aplicação. 
 */

package me.todolist.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.todolist.security.service.Impl.UserDetailsServiceImpl;

public class AuthTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, 
    HttpServletResponse response, FilterChain filterChain) 
    throws ServletException, IOException {
      try {
        String jwt = parseJwt(request);
        if(jwt != null && jwtUtils.validateJwtToken(jwt)) {
          String username = jwtUtils.getUserNameFromJwtToken(jwt);

          UserDetails userDetails = userDetailsService.loadUserByUsername(username);

          UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(userDetails, 
            null, userDetails.getAuthorities());

          authentication.setDetails(new WebAuthenticationDetailsSource()
            .buildDetails(request));
          
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } catch (Exception e){
        logger.error("Cannor set user authentication {}", e);
      }
      filterChain.doFilter(request, response);
    }
    private String parseJwt(HttpServletRequest request){
      String jwt = jwtUtils.getJwtFromCookies(request);
      return jwt;
    }

}
