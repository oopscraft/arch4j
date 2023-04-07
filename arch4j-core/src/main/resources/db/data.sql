-- authority
insert into `authority` (`id`,`system_required`,`name`,`icon`) values ('ADMIN_USER','Y','User Access Authority','/static/image/icon-user.png');
insert into `authority` (`id`,`system_required`,`name`,`icon`) values ('ADMIN_USER_EDIT','Y','User Edit Authority','/static/image/icon-user.png');
insert into `authority` (`id`,`system_required`,`name`,`icon`) values ('ADMIN_PROPERTY','Y','Property Access Authority','/static/image/icon-property.png');
insert into `authority` (`id`,`system_required`,`name`,`icon`) values ('ADMIN_PROPERTY_EDIT','Y','Property Edit Authority','/static/image/icon-property.png');
insert into `authority` (`id`,`system_required`,`name`,`icon`) values ('ADMIN_MESSAGE','Y','Message Access Authority','/static/image/icon-message.png');
insert into `authority` (`id`,`system_required`,`name`,`icon`) values ('ADMIN_MESSAGE_EDIT','Y','Message Edit Authority','/static/image/icon-message.png');
insert into `authority` (`id`,`system_required`,`name`,`icon`) values ('ADMIN_MENU','Y','Menu Access Authority','/static/image/icon-menu.png');
insert into `authority` (`id`,`system_required`,`name`,`icon`) values ('ADMIN_MENU_EDIT','Y','Message Access Authority','/static/image/icon-message.png');
insert into `authority` (`id`,`system_required`,`name`,`icon`) values ('ADMIN_CODE','Y','Code Access Authority','/static/image/icon-code.png');
insert into `authority` (`id`,`system_required`,`name`,`icon`) values ('ADMIN_CODE_EDIT','Y','Code Edit Authority','/static/image/icon-code.png');

-- role
insert into `role` (`id`,`system_required`,`name`,`icon`) values ('ADMIN','Y','Administrator Access RoleEntity','/static/image/icon-admin.png');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_USER');
insert into `role` (`id`,`system_required`,`name`,`icon`) values ('API','Y','Restful Api Access RoleEntity','/static/image/icon-api.png');
insert into `role` (`id`,`system_required`,`name`,`icon`) values ('H2-CONSOLE','Y','H2 Console Access RoleEntity','/static/image/icon-database.png');
insert into `role` (`id`,`name`,`icon`) values ('TEST','Test RoleEntity','/static/image/icon-test.png');

-- user
insert into `user` (`id`,`name`,`email`,`mobile`,`password`,`type`,`status`) values ('admin','Administrator','admin@oopscraft.org','010-1111-2222','{noop}admin','GENERAL','ACTIVE');
insert into `user_role` (`user_id`,`role_id`) values ('admin','ADMIN');
insert into `user_role` (`user_id`,`role_id`) values ('admin','API');
insert into `user_role` (`user_id`,`role_id`) values ('admin','H2-CONSOLE');
insert into `user` (`id`,`name`,`email`,`mobile`,`password`,`type`,`status`) values ('api','API Account','api@oopscraft.org','010-2222-3333','{noop}api','SYSTEM','ACTIVE');
insert into `user_role` (`user_id`,`role_id`) values ('api','API');
insert into `user` (`id`,`name`,`email`,`mobile`,`password`,`type`,`status`) values ('test','Test Account','test@oopscraft.org','010-3333-4444','{noop}api','GENERAL','ACTIVE');
insert into `user_role` (`user_id`,`role_id`) values ('test','TEST');

-- property
insert into `property` (`id`,`name`,`value`) values ('test1', 'test property 1','test_value_1');
insert into `property` (`id`,`name`,`value`) values ('test2', 'test property 2','test_value_2');
insert into `property` (`id`,`name`,`value`) values ('test3', 'test property 3','test_value_3');

-- code
insert into `code` (`id`,`name`,`note`) values ('test1','test code 1','test code 1');
insert into `code` (`id`,`name`,`note`) values ('test2','test code 2','test code 2');
insert into `code` (`id`,`name`,`note`) values ('test3','test code 3','test code 3');

-- sample
insert into `sample` (`id`, `name`, `type`) values ('sample001', 'sample 001', 'A');
insert into `sample_item` (`sample_id`, `id`, `name`) values ('sample001','item01','Item 01');
insert into `sample_item` (`sample_id`, `id`, `name`) values ('sample002','item02','Item 02');


