package me.todolist.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import me.todolist.models.ERole;
import me.todolist.models.Role;
import me.todolist.models.User;
import me.todolist.payload.request.LoginRequest;
import me.todolist.payload.request.SignupRequest;
import me.todolist.payload.response.MessageResponse;
import me.todolist.payload.response.UserInfoResponse;
import me.todolist.repository.RoleRepository;
import me.todolist.repository.UserRepository;
import me.todolist.security.jwt.JwtUtils;
import me.todolist.security.service.UserService;
import me.todolist.security.service.Impl.UserDetailsImpl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
public class UserRestController {

  private final UserService userService;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  public UserRestController(UserService userService) {
    this.userService = userService;
  }

  @CrossOrigin
  @GetMapping("/find/{id}")
  public ResponseEntity<User> findById(@PathVariable Long id) {
    var user = userService.findById(id);
    return ResponseEntity.ok(user);
  }

  @CrossOrigin
  // Cadastro
  @PostMapping("/auth/signup")
  public ResponseEntity<?> userSignup(@RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByLogin(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(
        signUpRequest.getName(),
        signUpRequest.getUsername(),
        signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      System.out.println("criaremos um usuário");
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
          case "admin":
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);
            break;
          default:
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  // Login
  @CrossOrigin
  @PostMapping("/auth/signin")
  public ResponseEntity<?> userSignin(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication
        .getPrincipal();
    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetailsImpl);

    List<String> roles = userDetailsImpl.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(new UserInfoResponse(userDetailsImpl.getId(),
            userDetailsImpl.getUsername(),
            userDetailsImpl.getEmail(),
            roles));
  }

  // Logout
  @CrossOrigin
  @PostMapping("/auth/signout")
  @PreAuthorize("has")
  public ResponseEntity<?> userSignout() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }

  // admin deletar um usuário
  @CrossOrigin
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> userDelete(@PathVariable Long id) {
    // Obter o usuário autenticado
    Authentication authentication = SecurityContextHolder.getContext()
      .getAuthentication();
    UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication
      .getPrincipal();

    boolean hasAdminRole = userDetailsImpl.getAuthorities().stream()
        .anyMatch(grantedAuthority -> grantedAuthority
            .getAuthority().equals("ROLE_ADMIN"));

    if (hasAdminRole) {
      Optional<User> userOptional = userRepository.findById(id);
      if (userOptional.isPresent()) {
        userRepository.delete(userOptional.get());
        return ResponseEntity
            .ok(new MessageResponse("User deleted successfully!"));
      } else {
        return ResponseEntity
            .ok(new MessageResponse("Error: the user don't exist"));
      }
    } else {
      return ResponseEntity.status(403)
          .body(new MessageResponse("Error: You do not have permission to delete this user!"));
    }
  }

  // usuário excluir a conta
  @CrossOrigin
  @DeleteMapping("auth/delete")
  public ResponseEntity<?> deleteUserLogged() {
    Authentication authentication = SecurityContextHolder
      .getContext().getAuthentication();
    if(authentication != null && authentication.isAuthenticated()){
      UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication
      .getPrincipal();
      Long userId = userDetailsImpl.getId();
      userRepository.deleteById(userId);
      return ResponseEntity
        .ok(new MessageResponse("User deleted successfully!"));
    }
    return ResponseEntity.ok(new MessageResponse("Error: No authenticated user found!")); 
  }
}
