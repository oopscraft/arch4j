# ARCH4J (Application Archetype for Java) 

## 데모 사이트

[https://arch4j-web.oopscraft.org/admin](https://arch4j-web.oopscraft.org/admin)

user/password: admin/admin, dev/dev


## 데이터베이스 생성 

### Mysql
```sql
create database arch4j;
create user 'user'@'%' identified by 'password';
grant all privileges on arch4j.* to 'user'@'%';
```


