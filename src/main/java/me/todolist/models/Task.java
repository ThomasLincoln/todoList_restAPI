package me.todolist.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "tb_tasks")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id_task;

  @ManyToOne
  @JoinColumn(name = "id_usuario", nullable = false)
  private User user;

  private String titulo;
  private String descricao;
  private String dataFinal;
  private boolean concluido;
  private int id_projeto;

  public Task(){
    super();
  }

  public long getId_task() {
    return id_task;
  }

  public void setId_task(long id_task) {
    this.id_task = id_task;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getDataFinal() {
    return dataFinal;
  }

  public void setDataFinal(String dataFinal) {
    this.dataFinal = dataFinal;
  }

  public boolean isConcluido() {
    return concluido;
  }

  public void setConcluido(boolean concluido) {
    this.concluido = concluido;
  }

  public int getId_projeto() {
    return id_projeto;
  }

  public void setId_projeto(int id_projeto) {
    this.id_projeto = id_projeto;
  }

  
}
