# todoList_restAPI

```mermaid
classDiagram
    class User {
        +String name
        +int id_usuario
        +String login
        +String senha
        +String email
        +Task[] tasks
        +Project[] projetos
    }

    class Task {
        +int id_task
        +int id_usuario
        +String titulo
        +String descricao
        +String data_final
        +boolean concluido
        +int id_projeto
    }

    class Project {
        +int id_projeto
        +int id_usuario
        +String titulo
        +String descricao
    }

    User "1" --> "0..*" Task : has
    User "1" --> "0..*" Project : has
```
---

Run on the database console:

```sql

INSERT INTO tb_roles(name) VALUES('ROLE_USER');
INSERT INTO tb_roles(name) VALUES('ROLE_ADMIN');

```