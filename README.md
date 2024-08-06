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

# Urls:

http://localhost:8080/h2-console/login.jsp?jsessionid=aa5ceff966884716a10b34c56c361013 -> Acessar banco local
http://localhost:8080/swagger-ui/index.html#/user-rest-controller -> API doc