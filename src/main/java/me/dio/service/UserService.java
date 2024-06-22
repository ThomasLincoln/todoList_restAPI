package me.dio.service;

import me.dio.models.User;

public interface UserService {
  User findById(Long id);
  User create(User userToCreate);
}
