package me.dio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.dio.models.Project;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
  List<Project> findById_usuario(long id_usuario);
}
