package me.dio.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "tb_projects")
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id_projeto;

  @ManyToOne
  @JoinColumn(name = "id_usuario", nullable = false)
  private long id_usuario;
  
  private String titulo;
  private String descricao;

  public Project() {
    super();
  }

  public long getId_projeto() {
    return id_projeto;
  }

  public void setId_projeto(long id_projeto) {
    this.id_projeto = id_projeto;
  }

  public long getId_usuario() {
    return id_usuario;
  }

  public void setId_usuario(long id_usuario) {
    this.id_usuario = id_usuario;
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

}
