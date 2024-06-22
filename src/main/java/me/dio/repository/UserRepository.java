package me.dio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.dio.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
