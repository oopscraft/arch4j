# ARCH4J (Application Archetype for Java) 

spring-boot-based archetype for wab application, batch application, CLI(Command Line Interface) application.

This archetype has Basic CMS(Content Management System) functionality.

## Git and Website

* Git Repository: [https://github.com/oopscraft/arch4j](https://github.com/oopscraft/arch4j)
* Website: [https://arch4j.oopscraft.org](https://arch4j.oopscraft.org)

## Demo Site

* Service Page: [https://arch4j-web.oopscraft.org](https://arch4j-web.oopscraft.org)

* Admin Console: [https://arch4j-web.oopscraft.org/admin](https://arch4j-web.oopscraft.org/admin)
  
* Test Account: dev/dev

## Main Features

| Functionality                                                                      | Description                                                                                                  |
|------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| **Admin Web Console**                                                              | Provide Web-based administration console(/admin)                                                             |
| **Design Theme**                                                                   | Customizable design theme (with Thymeleaf)                                                                   |
| **User/Role/Authority with Spring Security**                                       | Database-based User, Role, Authority (with Spring Security)                                                  |
| **Multiple Bulletin Boards**                                                       | Multiple bulletin board with skin, file attachments and comments.                                            |
| **Dynamic Page Composition**                                                       | Database-based dynamic content page publishing.                                                              |
| **Markdown Content with Git Integration**                                          | Git integrated Markdown page publishing.                                                                     |
| **Menu Management**                                                                | Dynamic menu management.                                                                                     |
| **Email Templates**                                                                | Email template(with thymeleaf).                                                                              |
| **Message, Variable, and Common Code Management for Additional Development Needs** | Common functionality for managing messages, variables, and common codes for additional business development. |


## Local Test

```shell
# download source
git clone https://github.com/oopscraft/arch4j.git

# run application
./gradlew :arch4j-web:bootRun

```
connect to http://localhost:8080


## Documentation

[1.Installation](doc/01.installation/index.md)

[2.Configuration](doc/02.configuration/index.md)


## License

Licence: LGPL(GNU Lesser General Public License version 3)
Copyright (C) 2016 oopscraft.org

- Anyone can use it freely.
- Modify the source or allow re-creation. However, you must state that you have the original creator.
- However, we can not grant patents or licenses for reproductives. (Modifications or reproductions must be shared with the public.)
