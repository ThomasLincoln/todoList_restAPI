package me.todolist.security.service;

import me.todolist.models.User;

public interface UserService {
  User findById(Long id);
  User create(User userToCreate);
}
