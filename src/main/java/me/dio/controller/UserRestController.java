package me.dio.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import me.dio.models.User;
import me.dio.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/user")
public class UserRestController {

  private final UserService userService;

  public UserRestController(UserService userService){
    this.userService = userService;
  }

  @GetMapping("/find/{id}")
  public ResponseEntity<User> findById(@PathVariable Long id){
    var user = userService.findById(id);
    return ResponseEntity.ok(user);
  }

  @PostMapping("/")
  public ResponseEntity<User> postMethodName(@RequestBody User userToCreate) {
      var userCreated = userService.create(userToCreate);
      URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/find/{id}")
        .buildAndExpand(userCreated.getId_usuario())
        .toUri();
      return ResponseEntity.created(location).body(userCreated);
  }
  
}
