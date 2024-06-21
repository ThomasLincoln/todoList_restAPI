package me.dio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.dio.models.Task;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findById_usuario(long id_usuario);
}
