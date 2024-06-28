package me.todolist.security.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.transaction.Transactional;
import me.todolist.models.User;
import me.todolist.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
    User user = userRepository.findByLogin(login)
      .orElseThrow(() -> new UsernameNotFoundException
        ("User not found with login: " + login));
    return UserDetailsImpl.build(user);
  }

}
