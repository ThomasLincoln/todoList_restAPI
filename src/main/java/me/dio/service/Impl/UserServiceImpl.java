package me.dio.service.Impl;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import me.dio.models.User;
import me.dio.repository.UserRepository;
import me.dio.service.UserService;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  @Override
  public User findById(Long id) {
    return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public User create(User userToCreate) {
    if(userToCreate.getLogin() != null && userRepository.existsByLogin(userToCreate.getLogin())){
      throw new IllegalArgumentException("Esse Login já está em uso");
    }
    return userRepository.save(userToCreate);
  }
}
