package me.dio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.dio.models.Project;
import me.dio.models.User;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
  List<Project> findByUser(User user);
}
