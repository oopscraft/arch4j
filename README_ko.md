# ARCH4J (Application Archetype for Java) 

웹 애플리케이션, 배치 애플리케이션, CLI(Command Line Interface) 애플리케이션을 위한 spring-boot 기반 아키타입 입니다.

이 아키타입은 기본적인 CMS(Content Management System) 기능을 가지고 있습니다.


## Git 저장소 및 웹사이트

* Git Repository: [https://github.com/oopscraft/arch4j](https://github.com/oopscraft/arch4j)
* Website: [https://arch4j.oopscraft.org](https://arch4j.oopscraft.org)

## 데모 사이트

* Service Page: [https://arch4j-web.oopscraft.org](https://arch4j-web.oopscraft.org)
* Admin Console: [https://arch4j-web.oopscraft.org/admin](https://arch4j-web.oopscraft.org/admin)
* Test Account: **dev/dev**

## 주요 기능

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


## 빠른 로컬 테스트

```shell
# download source
git clone https://github.com/oopscraft/arch4j.git

# run application
./gradlew :arch4j-web:bootRun

```
connect to http://localhost:8080


## 문서

[1.Installation](doc/01.installation/index.md)

[2.Configuration](doc/02.configuration/index.md)


## 라이센스

ARCH4J (Application Archetype for Java)
Copyright (C) 2016 oopscraft.org

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


## 연락처
* email: [chomookun@gmailcom](mailto:chomookun@gmail.com)
* linkedin: [https://www.linkedin.com/in/chomookun](https://www.linkedin.com/in/chomookun)
