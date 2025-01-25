# ARCH4J (Application Archetype for Java) 

spring-boot-based archetype for web application, batch application, CLI(Command Line Interface) application.

This archetype has Basic CMS(Content Management System) functionality.

## Git and Website

* Git Repository: [https://github.com/chomookun/arch4j](https://github.com/oopscraft/arch4j)
* Website: [https://arch4j.chomookun.org](https://arch4j.chomookun.org)

## Demo Site

* Service Page: [https://arch4j-web.chomookun.org](https://arch4j-web.chomookun.org)
* Admin Console: [https://arch4j-web.chomookun.org/admin](https://arch4j-web.chomookun.org/admin)
* Test Account: **developer/developer**

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


## Quick Local Test

```shell
# download source
git clone https://github.com/chomookun/arch4j.git

# run application
./gradlew :arch4j-web:bootRun

```
connect to http://localhost:8080


## Documentation

[1.Installation](doc/01.installation/index.md)

[2.Configuration](doc/02.configuration/index.md)


## License

[LICENSE](LICENSE)


## Contact
* email: [chomookun@gmailcom](mailto:chomookun@gmail.com)
* linkedin: [https://www.linkedin.com/in/chomookun](https://www.linkedin.com/in/chomookun)
