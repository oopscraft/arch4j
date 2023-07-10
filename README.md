# ARCH4J (Application Archetype for Java) 

## Git and Website

* Git Repository: [https://github.com/oopscraft/arch4j](https://github.com/oopscraft/arch4j)
* Website: [https://arch4j.oopscraft.org](https://arch4j.oopscraft.org)

## Demo Site

* Service Page: [https://arch4j-web.oopscraft.org](https://arch4j-web.oopscraft.org)

* Admin Console: [https://arch4j-web.oopscraft.org/admin](https://arch4j-web.oopscraft.org/admin)
  
Test Account: dev/dev

## Main Features

| Functionality                                                                      | Description                                                                                                  |
|------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| **Admin Web Console**                                                              | Provide Web-based administration console(/admin)                                                             |
| **Degisn Theme**                                                                   | Customizable design theme (with Thymeleaf)                                                                   |
| **User/Role/Authority with Spring Security**                                       | Database-based User, Role, Authority (with Spring Security)                                                  |
| **Data Encryption**                                                                | Database Encyption/Decryption (spring-data-jpa, mybatis with spring-security)                                |
| **Multiple Bulletin Boards**                                                       | Multiple bulletin board with skin, file attachments and comments.                                            |
| **Dynamic Page Composition**                                                       | Database-based dynamic content page publishing.                                                              |
| **Markdown Content with Git Integration**                                          | Git integrated Markdown page publishing.                                                                     |
| **Menu Management**                                                                | Dynamic menu management.                                                                                     |
| **Email Templates**                                                                | Email template(with thymeleaf).                                                                              |
| **Message, Variable, and Common Code Management for Additional Development Needs** | Common functionality for managing messages, variables, and common codes for additional business development. |



## Create database

### Mysql

```sql
-- create admin account
create database [database];
create user [usernmae]@'%' identified by [password];
grant all privileges on [database].* to [username]@'%';

```


