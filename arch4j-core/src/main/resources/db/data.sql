-- role
insert into `role` (`id`,`system_required`,`name`) values ('ADMIN','Y','Administrator Role');
insert into `role` (`id`,`system_required`,`name`) values ('API','Y','Restful Api Role');
insert into `role` (`id`,`name`) values ('TEST','Test Role');

-- authority
insert into `authority` (`id`,`system_required`,`name`) values ('USER','Y','User Management Authority');

-- user
insert into `user` (`id`,`password`,`name`) values ('admin','{noop}admin','Administrator');
insert into `user_role` (`user_id`,`role_id`) values ('admin','ADMIN');
insert into `user` (`id`,`password`,`name`) values ('test','{noop}test','Tester');
insert into `user_role` (`user_id`,`role_id`) values ('test','TEST');

-- property
insert into `property` (`id`,`name`,`value`) values ('test1', 'test property 1','test_value_1');
insert into `property` (`id`,`name`,`value`) values ('test2', 'test property 2','test_value_2');
insert into `property` (`id`,`name`,`value`) values ('test3', 'test property 3','test_value_3');

-- code
insert into `code` (`id`,`name`,`note`) values ('test1','test code 1','test code 1');
insert into `code` (`id`,`name`,`note`) values ('test2','test code 2','test code 2');
insert into `code` (`id`,`name`,`note`) values ('test3','test code 3','test code 3');


