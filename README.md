# ARCH4J (Application Archetype for Java) 

## Demo Site

* Service Page: [https://arch4j-web.oopscraft.org](https://arch4j-web.oopscraft.org)

* Admin Console: [https://arch4j-web.oopscraft.org/admin](https://arch4j-web.oopscraft.org/admin)
  
Test Account: dev/dev

## Main Features

The main features provided are as follows.

### Admin Web Console

Access the system administration console by visiting /admin.

### Design Theme Functionality

Theming functionality based on Thymeleaf templates.

### User/Role/Authority with Spring Security

Database-based User, Role, Authority functionality with Spring Security.

### Multiple Bulletin Boards

Multiple bulletin board functionality with skin, file attachments and comments.

### Dynamic Page Composition

Database-based dynamic content page composition functionality.

### Markdown Content with Git Integration

Markdown page service with Git integration for content management.

### Menu Management

Dynamic menu functionality based on database.

### Email Templates

Email template functionality.

### Message, Variable, and Common Code Management for Additional Development Needs

Common functionality for managing messages, variables, and common codes for additional business development.


## Create database

### Mysql
```sql
create database arch4j;
create user 'user'@'%' identified by 'password';
grant all privileges on arch4j.* to 'user'@'%';
```


