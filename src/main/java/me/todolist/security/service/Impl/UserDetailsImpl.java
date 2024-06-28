/*
 * Esse código é responsável por definir a classe UserDetailsImpl, que implementa
 * a classe userDetails do spring security. A classe UserDetailsImpl encapsula 
 * as informações do usuário e suas autoridades (roles) para serem usadas pelo 
 * Spring Security durante o processo de autenticação e autorização. 
 */

package me.todolist.security.service.Impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import me.todolist.models.User;

public class UserDetailsImpl implements UserDetails {
  // identificador de versão único para uma classe serializável.
  private static final long serialVersionUID = 1L;

  private Long id;
  private String login;
  private String email;
  private Collection<? extends GrantedAuthority> authorities;

  @JsonIgnore
  private String senha;

  public UserDetailsImpl(Long id, String login, String email,
      Collection<? extends GrantedAuthority> authorities, String senha) {
    this.id = id;
    this.login = login;
    this.email = email;
    this.authorities = authorities;
    this.senha = senha;
  }

  public static UserDetailsImpl build(User user) {
    List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
        .collect(Collectors.toList());

    return new UserDetailsImpl(user.getId_usuario(), user.getLogin(),
        user.getEmail(), authorities, user.getSenha());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return senha;
  }

  @Override
  public String getUsername() {
    return login;
  }

  public Long getId(){
    return id;
  }

  public String getEmail(){
    return email;
  }
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o){
    if(this == o){
      return true;
    }
    if(o == null || getClass() != o.getClass()){
      return false;
    }
    UserDetailsImpl user = (UserDetailsImpl) o;
    return id.equals(user.id);
  }
}
