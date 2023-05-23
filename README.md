# ARCH4J (Application Archetype for Java) 

## Demo Site

[https://arch4j-web.oopscraft.org](https://arch4j-web.oopscraft.org)

user/password: admin/admin


## Create database

### Mysql
```sql
create database arch4j;
create user 'user'@'%' identified by 'password';
grant all privileges on arch4j.* to 'user'@'%';
```


