-- authority
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN','Y','Admin Access Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_MONITOR','Y','Admin Monitor Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_USER','Y','User Access Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_USER_EDIT','Y','User Edit Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_ROLE','Y','Role Edit Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_ROLE_EDIT','Y','Role Edit Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_MESSAGE','Y','Message Access Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_MESSAGE_EDIT','Y','Message Edit Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_VARIABLE','Y','Variable Access Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_VARIABLE_EDIT','Y','Variable Edit Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_MENU','Y','Menu Access Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_MENU_EDIT','Y','Message Access Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_CODE','Y','Code Access Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_CODE_EDIT','Y','Code Edit Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_BOARD','Y','Board Access Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_BOARD_EDIT','Y','Board Edit Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_PAGE','Y','Page Access Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_PAGE_EDIT','Y','Page Edit Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_GIT','Y','Git Access Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_GIT_EDIT','Y','Git Edit Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_EMAIL','Y','Email Access Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ADMIN_EMAIL_EDIT','Y','Email Edit Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('ACTUATOR','Y','Actuator Access Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('H2-CONSOLE','Y','Actuator Access Authority');
insert into `core_authority` (`authority_id`,`system_required`,`authority_name`) values ('SWAGGER-UI','Y','Swagger UI Access Authority');

-- role
insert into `core_role` (`role_id`,`system_required`,`role_name`) values ('ADMIN','Y','Administrator Role');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_MONITOR');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_USER');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_USER_EDIT');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_ROLE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_ROLE_EDIT');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_MESSAGE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_MESSAGE_EDIT');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_VARIABLE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_VARIABLE_EDIT');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_MENU');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_MENU_EDIT');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_CODE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_CODE_EDIT');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_BOARD');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_BOARD_EDIT');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_PAGE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_PAGE_EDIT');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_GIT');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_GIT_EDIT');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_EMAIL');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_EMAIL_EDIT');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ACTUATOR');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'H2-CONSOLE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'SWAGGER-UI');
insert into `core_role` (`role_id`,`system_required`,`role_name`) values ('DEV','Y','Developer Role');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_MONITOR');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_ROLE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_MENU');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_MESSAGE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_VARIABLE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_CODE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_BOARD');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_PAGE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_GIT');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_EMAIL');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ACTUATOR');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'H2-CONSOLE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'SWAGGER-UI');

-- user
insert into `core_user` (`user_id`,`user_name`,`password`,`type`,`status`) values ('user','User','{noop}user','GENERAL','ACTIVE');
insert into `core_user` (`user_id`,`user_name`,`password`,`type`,`status`) values ('admin','Administrator','{noop}admin','GENERAL','ACTIVE');
insert into `core_user_role` (`user_id`,`role_id`) values ('admin','ADMIN');
insert into `core_user` (`user_id`,`user_name`,`password`,`type`,`status`) values ('dev','Developer Account','{noop}dev','SYSTEM','ACTIVE');
insert into `core_user_role` (`user_id`,`role_id`) values ('dev','DEV');

-- email
insert into `core_email_template` (`template_id`,`template_name`,`subject`,`content`) values ('VERIFICATION','Verification Email', 'Verification Code: [[${answer}]]', 'Verification Code: <b>[[${answer}]]</b>');

-- variable
insert into `core_variable` (`variable_id`,`variable_name`,`value`) values ('test1', 'test property 1','test_value_1');
insert into `core_variable` (`variable_id`,`variable_name`,`value`) values ('test2', 'test property 2','test_value_2');
insert into `core_variable` (`variable_id`,`variable_name`,`value`) values ('test3', 'test property 3','test_value_3');

-- code
insert into `core_code` (`code_id`,`code_name`,`note`) values ('test1','test code 1','test code 1');
insert into `core_code` (`code_id`,`code_name`,`note`) values ('test2','test code 2','test code 2');
insert into `core_code` (`code_id`,`code_name`,`note`) values ('test3','test code 3','test code 3');

-- git
insert into `core_git` (`git_id`,`git_name`,`note`,`url`,`branch`) values ('arch4j', 'arch4j repository', 'Arch4j Github Repository','https://github.com/oopscraft/arch4j.git', null);

-- board
insert into `core_board` (
    `board_id`,
    `board_name`,
    `message_format`,
    `message`,
    `skin`,
    `page_size`,
    `comment_enabled`,
    `file_enabled`,
    `access_policy`,
    `read_policy`,
    `write_policy`,
    `comment_policy`,
    `icon`
) values (
    'notice',
    'Notice Board',
    'TEXT',
    'Notice Board',
    '_default',
    10,
    'Y',
    'Y',
    null,
    'AUTHENTICATED',
    'AUTHORIZED',
    'AUTHENTICATED',
    'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAmBJREFUWEft1kvIzmkYBvDfJxlKSmMWRIoiOTSRU6EsJHIuh4zlsDEk7FiyYzYTK7KymBHlkNNCiaKhJElT0gjNwoJkYyi66vnr/d7e03eoT/nuehfv89yH67me+77+T5cBtq4Brq8vAMZjYTnAHbzszWH6AmAj/ipFN+HMIID+ZGADLuH/FkkXYG/Z/x13W/gOwyqcq/dp1AP7cARXsB4fenOympgUT6+sxUEcrs1XD2A0HmNscbqANFvFxCiswxpMx4Ti96LEnUd+78p6iqc54x/7r8S9qUA0YmAqbmAc3mNRSf4bDmBMG0Ze4xCOYQZuY2QpvhT/tGKg2guIi/gVj/AnltUEfsYTPC9rEzGNbrpyDVswEyexur54YlvpwFAMxy38XAqFkfTHCbyqYyLCtL00Zk4ce4DFpY8+NWKunRCdRSYiFiZyl2+xu/TClEJvbe5JpQ9Cfyw9EKFqaK0ArMDlEvUU8zGrdPRPNdka5fgRfyNgYstxvacM3Mcc5L7nYghuYkRJFCYy+wHayAI434gAvId5PQEwGTl1LNewGQ/LCGXtFPbUjFszhiM80ZJYcj6rd2x2BRm5P4pzdCDNF2GKXcXKwkyzwtV67j4TFNuJ450COFojsxmxXdhfgmeX7m5XPPuJ/bc4JmeV42tsMwZOY2vx+gE7sKT8/wUfO6mOKGEl5cm5rVMGot2hPtZuVNthSRPHGo5js+SDAAYZ+L4ZyPjlrZc3X6zpl6zd/JX96ume70bejt2e743GsJrbDvP32K1bzW8SQKWAPT5ahwFtr6DDPP3j1led7zOKL04qfSGcgtpYAAAAAElFTkSuQmCC'
);

insert into `core_board`(
    `board_id`,
    `board_name`,
    `message_format`,
    `message`,
    `skin`,
    `page_size`,
    `comment_enabled`,
    `file_enabled`,
    `access_policy`,
    `read_policy`,
    `write_policy`,
    `comment_policy`,
    `icon`
) values (
    'anonymous',
    'Anonymous Board',
    'MARKDOWN',
    '**Anonymous Board Demo**
* Accessible for non-logged-in users
* Read/Write enabled

**익명게시판 데모**
* 로그인하지 않은 사용자 접근가능
* 읽기/쓰기 가능
',
    '_default',
    10,
    'Y',
    'Y',
    null,
    null,
    null,
    null,
    'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAuRJREFUWEftlttL1FEQx+ec32V31dVdt5WtvFFpIoG6iq0LCZH4FIT01F9QtHYh6CUIliIIIggz6ql8KILwpctbl4colbQto4u3yFTM9bLuthfX8/udM/GLCtYsLwhG7Hk7nDMzn/nOwAyBdT5kneNDGuDfUcDaOuZQCdzWUNSrEg4zTrqEID0ceWCOmXvhpCu+on65MJFpUZMVEpHchGKNLKFH56RYIvQpRzgQbc6fMfz9UiDryujlIqt08JzXpnyK6BCYnOfdQaaNRXUTAKAq02HGsQsRegiSgArJV6GjJV8NJ7ktg9kMzFVI0E0I1CiUeDQuig3/Tos0vz1XVkptqrQxU4Lr72LsS0JcizUXHEsBcF793HGkKqfu9M6clERjDKF3msHrKQaBIBPdQcaGI5oJAUCl9ImGQBDFbsPIlSHPl+XKaqldodtsMmzNlsGipFb55oc4tA/GO0O+Iu8CgJHO4+5sz6naVIDFZE/oCG+mGDTdn9a5QDhblyNvsclglpZuqVt9cbgzkOia9RXWrRrgJ9Se9kmIMgEX623Lbo81BTj0OATBOIcTbmsaIK1AWoG0AmkF/gMF7K0jd/eVWPbeaNxAl5vOambB+e6IeD6h3YscLmhKmYbWlpFdhMIjh5kKd54qVbtMSqVTgUqnCsYisdjZ/2AKQkkBZzyLj/CZpIChsAZDEQ6DIU3rD+s8ygRBgg1RX9GzFADjYr40Xkip3gBEVGcoxMt0Us4RVLuJMneeQmpcJqXCqYIB5jBLUN42znVE0tbooBEmvgf6GNagP6RpA2EdYxqqlABTKL5PcuwApC9Rkh7O+TaP/kzo7xuEH+Us+2iZsWoBUrdFBa8mxA5dEItEECnQWW54Qm4XQAmlOCcReKtptAOICBirW2y2oA/8RP9TWZdeYRZa+pFabeMlQtbzLfGMF8bzXGailuryWDS8aRD8RCy3h34rwUoM1+rvyhVYq8g//KQB1l2Bb6xnBD8tREAmAAAAAElFTkSuQmCC'
);
insert into `core_board` (
    `board_id`,
    `board_name`,
    `message_format`,
    `message`,
    `skin`,
    `page_size`,
    `comment_enabled`,
    `file_enabled`,
    `access_policy`,
    `read_policy`,
    `write_policy`,
    `comment_policy`,
    `icon`
) values (
    'member',
    'Member Board',
    'TEXT',
    'Member Board',
    '_default',
    10,
    'Y',
    'Y',
    null,
    'AUTHENTICATED',
    'AUTHENTICATED',
    'AUTHENTICATED',
    'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAABW1JREFUWEe9l2lMVFcUx//nPsCFpglSl0TFXSsVq1XTGqsozow6g1RmQdMPSIxLXVps2qDWjRhs1VaIRuPauFcDzGCdBZkZlaq1torRUms0bY1pVVxQKIICwz3NG0ODsg7avk8v793zP79z7j3n3ksI8NFMMg0lwfGAeIvBXQGuZClukZAnJSPnuMt2IxBJaulgjd4SRcTrAdbW2gSHhDBLCZ/P59dh5hoS2CWFb9mxI0futES7RQAavSlBEPYw0DYyKgpjxo/DwDcGoX1oqN/HwwfFKLx4Cd7cXNy+eQsg3BTguDyH7UJzEM0CaPTm9wBpa9O2Hc2aP5eGDB/eqKaUEkftduRkZqnpKJEs3z7myrnWFESTADF6Yw+F6JeQkJDQlJUrqEevXs0F5P///XcnsXvbNvX11wdF4UMKCrZXN2bYJIBOb9zDRIlJs2dj1NjoFjmvHbR35w6cOp4PBuZ5ndYtAQOMj48Pp0pxp2v3bkrq2jUgana2nvFRWlKKJcnJXFVVfdXrsg4MGEATa04k5j3GaVMxKS4uoOhrB29Oz8DF8+chWQ5obC00GpZWb14L4pRFK1ei74D+rQLwuFzI3H8ADDJ5ndm2hkQaBaid/9UZ6ejUuXOrAH46cwY7Nm0GGAs8LuvmgAC0etMWED5YuWYNukV0bxXAyRMnsG/HThCQ5HZa9wQEoDGYlhKQNu/jhRg6YkSrAKwHD/n7Aog0Hkf2sYAAxsdaRgqWZ0ZFj0HSnDmtAljxaQqKbt180kZUhdvt9oqAAFJTU8WZc4XXleCg7p9npFNYh/CAIC4WFGDz+nR1h8jyOG0JAZehaqDVG5NAtKt3v75Qq0EI0SKI0tISrFqylEsfPmRFocF5duvl1gCQzmCZJSE3EtDmzWHDMDc5GUqQ0iREWdkjfLFiOe7duevfDyAww+Ow5QQEYLFYlJIK3zeASGDmciGojBld+vTri6nTE9Grd596esyMC+fOIXPffn5QXEwM/E5ADwBBYFrncWUvavEa0BlMaQwsleCCGlZiEYpHwRW+r1UgVSSiZ08MjBqEsLAOkLIGd4vuoPDSRS6+d1/t2D4pOc3rsq2aMNkUKSWcUEEIcz0O69bnIeo1orF6S5cg1NwAUbEvuCYy//DhklqjCQbjOClpEQRiAAQ/I0b4G8C3Usq0um1X3VEFQV0DlaI6NMLt3lde164egFZvngPircRIcbusXzaUNt1k0+sscaXOP1+QbBuem3tAhaj3aA3mrwD+hATi3Xbr4SYBdJPMGSx4IRON8zqy8xsBmMISOf2Hj0bl43LcuHwBxHjH7bL+2AiABeDMhoKqlwGdwbSdgVkMGel15tSN8l9trcG4GqDPRpuS8KS8DOeOWsGED70O66aGADSxlneJ5SmA13qctsVNZ6AFABp9fB6R0MXNW4YnFWVw794AYt7rdtmm/x8ApDGY7r/yaliYfnYKgRm2jansq6pq9ODxUjMQM2lKH0Uov3UbEIWRk9/3B3zi0Hbc/+s6VwfXdKhbNbXZeKkAOoN5GoMPDo6eiAEjnp4TL+W7cO38KQhwTJ7TduL5aXipALUlNTZhJjpGPO2If179GWftBwHQYo8ze+1/C6A35pMQ0XELliOkTTu/r/LSh3DtWAciWN0Oq/mFANRbEAgflbcX2h+ysh7XEzMY7ypKcMfohJl4rava6oGiP67hdM5u9Wp2xeO0Rj5vo56wRaXIZcJyr9Oa12QZNlRGdb9p9ab5DN6gKIoYEhNL1ZWVKDztZjBXE9EMtyP7QHMaLwSgGuv0xjEQIouZOz0Vo9sspdGbazsbiHO/ZaAGteMnTpzS06co+wlUUc2UmO/KKmqN1j+PxZs/bL7TnQAAAABJRU5ErkJggg=='
);

-- page
insert into `core_page` (
    `page_id`,
    `page_name`,
    `content_format`,
    `content`
) values (
    'board',
    'Board Demo',
    'MARKDOWN',
    '# 데모 게시판 최근 게시물 페이지

데모게시판 최근게시물 위젯을 포함한 페이지

'
);
insert into `core_page_widget` (
    `page_id`,
    `index`,
    `type`,
    `properties`
) values (
    'board',
    0,
    'org.oopscraft.arch4j.web.board.widget.LatestArticlesWidgetController',
    'boardId=notice
pageSize=10'
);
insert into `core_page_widget` (
    `page_id`,
    `index`,
    `type`,
    `properties`
) values (
    'board',
    1,
    'org.oopscraft.arch4j.web.board.widget.LatestArticlesWidgetController',
    'boardId=anonymous
pageSize=10'
);
insert into `core_page_widget` (
    `page_id`,
    `index`,
    `type`,
    `properties`
) values (
    'board',
    2,
    'org.oopscraft.arch4j.web.board.widget.LatestArticlesWidgetController',
    'boardId=member
pageSize=10'
);

-- menu
insert into `core_menu` (`menu_id`,`parent_menu_id`,`menu_name`,`link`,`target`,`icon`) values ('board',null,'Board Demo','/page/board',null,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAM5JREFUWEftlzEOgkAQRR8ewspWa+y04RAYPYgH8SAavYSFNjR09lYewpgxYCYISLK7WYuhI1nmv31b8Dch8pNEzscAtIExcAZmgY/lBmTAQ3I0QA4cA4fX41fAqQmwBvbVigtw9wwzAZbVzA1w6AP4LPAIoTdoAGbADJgBM2AGzMBPAzvg6vFXLKMWwHZoH/Cc/TWu1UAKFMAocPoTmANlsxHJu0BMAwNIKX2HtwHobN3hXJk6O2bfxUR3OFeAzo751wDRj8BV+6Dvo19OX6AXWCE+Gh4hAAAAAElFTkSuQmCC');
insert into `core_menu` (`menu_id`,`parent_menu_id`,`menu_name`,`link`,`target`,`icon`) values ('board-notice','board','Notice','/board/notice',null,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAmBJREFUWEft1kvIzmkYBvDfJxlKSmMWRIoiOTSRU6EsJHIuh4zlsDEk7FiyYzYTK7KymBHlkNNCiaKhJElT0gjNwoJkYyi66vnr/d7e03eoT/nuehfv89yH67me+77+T5cBtq4Brq8vAMZjYTnAHbzszWH6AmAj/ipFN+HMIID+ZGADLuH/FkkXYG/Z/x13W/gOwyqcq/dp1AP7cARXsB4fenOympgUT6+sxUEcrs1XD2A0HmNscbqANFvFxCiswxpMx4Ti96LEnUd+78p6iqc54x/7r8S9qUA0YmAqbmAc3mNRSf4bDmBMG0Ze4xCOYQZuY2QpvhT/tGKg2guIi/gVj/AnltUEfsYTPC9rEzGNbrpyDVswEyexur54YlvpwFAMxy38XAqFkfTHCbyqYyLCtL00Zk4ce4DFpY8+NWKunRCdRSYiFiZyl2+xu/TClEJvbe5JpQ9Cfyw9EKFqaK0ArMDlEvUU8zGrdPRPNdka5fgRfyNgYstxvacM3Mcc5L7nYghuYkRJFCYy+wHayAI434gAvId5PQEwGTl1LNewGQ/LCGXtFPbUjFszhiM80ZJYcj6rd2x2BRm5P4pzdCDNF2GKXcXKwkyzwtV67j4TFNuJ450COFojsxmxXdhfgmeX7m5XPPuJ/bc4JmeV42tsMwZOY2vx+gE7sKT8/wUfO6mOKGEl5cm5rVMGot2hPtZuVNthSRPHGo5js+SDAAYZ+L4ZyPjlrZc3X6zpl6zd/JX96ume70bejt2e743GsJrbDvP32K1bzW8SQKWAPT5ahwFtr6DDPP3j1led7zOKL04qfSGcgtpYAAAAAElFTkSuQmCC');
insert into `core_menu` (`menu_id`,`parent_menu_id`,`menu_name`,`link`,`target`,`icon`) values ('board-anonymous','board','Anonymous','/board/anonymous',null,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAuRJREFUWEftlttL1FEQx+ec32V31dVdt5WtvFFpIoG6iq0LCZH4FIT01F9QtHYh6CUIliIIIggz6ql8KILwpctbl4colbQto4u3yFTM9bLuthfX8/udM/GLCtYsLwhG7Hk7nDMzn/nOwAyBdT5kneNDGuDfUcDaOuZQCdzWUNSrEg4zTrqEID0ceWCOmXvhpCu+on65MJFpUZMVEpHchGKNLKFH56RYIvQpRzgQbc6fMfz9UiDryujlIqt08JzXpnyK6BCYnOfdQaaNRXUTAKAq02HGsQsRegiSgArJV6GjJV8NJ7ktg9kMzFVI0E0I1CiUeDQuig3/Tos0vz1XVkptqrQxU4Lr72LsS0JcizUXHEsBcF793HGkKqfu9M6clERjDKF3msHrKQaBIBPdQcaGI5oJAUCl9ImGQBDFbsPIlSHPl+XKaqldodtsMmzNlsGipFb55oc4tA/GO0O+Iu8CgJHO4+5sz6naVIDFZE/oCG+mGDTdn9a5QDhblyNvsclglpZuqVt9cbgzkOia9RXWrRrgJ9Se9kmIMgEX623Lbo81BTj0OATBOIcTbmsaIK1AWoG0AmkF/gMF7K0jd/eVWPbeaNxAl5vOambB+e6IeD6h3YscLmhKmYbWlpFdhMIjh5kKd54qVbtMSqVTgUqnCsYisdjZ/2AKQkkBZzyLj/CZpIChsAZDEQ6DIU3rD+s8ygRBgg1RX9GzFADjYr40Xkip3gBEVGcoxMt0Us4RVLuJMneeQmpcJqXCqYIB5jBLUN42znVE0tbooBEmvgf6GNagP6RpA2EdYxqqlABTKL5PcuwApC9Rkh7O+TaP/kzo7xuEH+Us+2iZsWoBUrdFBa8mxA5dEItEECnQWW54Qm4XQAmlOCcReKtptAOICBirW2y2oA/8RP9TWZdeYRZa+pFabeMlQtbzLfGMF8bzXGailuryWDS8aRD8RCy3h34rwUoM1+rvyhVYq8g//KQB1l2Bb6xnBD8tREAmAAAAAElFTkSuQmCC');
insert into `core_menu` (`menu_id`,`parent_menu_id`,`menu_name`,`link`,`target`,`icon`) values ('board-member','board','Member','/board/member',null,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAABW1JREFUWEe9l2lMVFcUx//nPsCFpglSl0TFXSsVq1XTGqsozow6g1RmQdMPSIxLXVps2qDWjRhs1VaIRuPauFcDzGCdBZkZlaq1torRUms0bY1pVVxQKIICwz3NG0ODsg7avk8v793zP79z7j3n3ksI8NFMMg0lwfGAeIvBXQGuZClukZAnJSPnuMt2IxBJaulgjd4SRcTrAdbW2gSHhDBLCZ/P59dh5hoS2CWFb9mxI0futES7RQAavSlBEPYw0DYyKgpjxo/DwDcGoX1oqN/HwwfFKLx4Cd7cXNy+eQsg3BTguDyH7UJzEM0CaPTm9wBpa9O2Hc2aP5eGDB/eqKaUEkftduRkZqnpKJEs3z7myrnWFESTADF6Yw+F6JeQkJDQlJUrqEevXs0F5P///XcnsXvbNvX11wdF4UMKCrZXN2bYJIBOb9zDRIlJs2dj1NjoFjmvHbR35w6cOp4PBuZ5ndYtAQOMj48Pp0pxp2v3bkrq2jUgana2nvFRWlKKJcnJXFVVfdXrsg4MGEATa04k5j3GaVMxKS4uoOhrB29Oz8DF8+chWQ5obC00GpZWb14L4pRFK1ei74D+rQLwuFzI3H8ADDJ5ndm2hkQaBaid/9UZ6ejUuXOrAH46cwY7Nm0GGAs8LuvmgAC0etMWED5YuWYNukV0bxXAyRMnsG/HThCQ5HZa9wQEoDGYlhKQNu/jhRg6YkSrAKwHD/n7Aog0Hkf2sYAAxsdaRgqWZ0ZFj0HSnDmtAljxaQqKbt180kZUhdvt9oqAAFJTU8WZc4XXleCg7p9npFNYh/CAIC4WFGDz+nR1h8jyOG0JAZehaqDVG5NAtKt3v75Qq0EI0SKI0tISrFqylEsfPmRFocF5duvl1gCQzmCZJSE3EtDmzWHDMDc5GUqQ0iREWdkjfLFiOe7duevfDyAww+Ow5QQEYLFYlJIK3zeASGDmciGojBld+vTri6nTE9Grd596esyMC+fOIXPffn5QXEwM/E5ADwBBYFrncWUvavEa0BlMaQwsleCCGlZiEYpHwRW+r1UgVSSiZ08MjBqEsLAOkLIGd4vuoPDSRS6+d1/t2D4pOc3rsq2aMNkUKSWcUEEIcz0O69bnIeo1orF6S5cg1NwAUbEvuCYy//DhklqjCQbjOClpEQRiAAQ/I0b4G8C3Usq0um1X3VEFQV0DlaI6NMLt3lde164egFZvngPircRIcbusXzaUNt1k0+sscaXOP1+QbBuem3tAhaj3aA3mrwD+hATi3Xbr4SYBdJPMGSx4IRON8zqy8xsBmMISOf2Hj0bl43LcuHwBxHjH7bL+2AiABeDMhoKqlwGdwbSdgVkMGel15tSN8l9trcG4GqDPRpuS8KS8DOeOWsGED70O66aGADSxlneJ5SmA13qctsVNZ6AFABp9fB6R0MXNW4YnFWVw794AYt7rdtmm/x8ApDGY7r/yaliYfnYKgRm2jansq6pq9ODxUjMQM2lKH0Uov3UbEIWRk9/3B3zi0Hbc/+s6VwfXdKhbNbXZeKkAOoN5GoMPDo6eiAEjnp4TL+W7cO38KQhwTJ7TduL5aXipALUlNTZhJjpGPO2If179GWftBwHQYo8ze+1/C6A35pMQ0XELliOkTTu/r/LSh3DtWAciWN0Oq/mFANRbEAgflbcX2h+ysh7XEzMY7ypKcMfohJl4rava6oGiP67hdM5u9Wp2xeO0Rj5vo56wRaXIZcJyr9Oa12QZNlRGdb9p9ab5DN6gKIoYEhNL1ZWVKDztZjBXE9EMtyP7QHMaLwSgGuv0xjEQIouZOz0Vo9sspdGbazsbiHO/ZaAGteMnTpzS06co+wlUUc2UmO/KKmqN1j+PxZs/bL7TnQAAAABJRU5ErkJggg==');
insert into `core_menu` (`menu_id`,`parent_menu_id`,`menu_name`,`link`,`target`,`icon`) values ('admin',null,'Admin Console','/admin','_blank','data:image/png;base64,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');
insert into `core_menu` (`menu_id`,`parent_menu_id`,`menu_name`,`link`,`target`,`icon`) values ('git',null,'Git Repository','https://github.com/oopscraft/arch4j','_blank','data:image/png;base64,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');



-- demo data
INSERT INTO core_article
(article_id, system_required, system_updated_by, system_updated_at, board_id, comment_count, vote_positive_count, vote_negative_count, content, content_format, created_at, password, title, user_id, user_name)
VALUES('7b70bab4b58d4265b18b7d5859efbb62', 'N', 'admin', '2023-06-18 17:14:36.236', 'anonymous', 0, 0, 0, '# Java 프로세스를 통해 PDF 문서의 페이지를 삭제하는 방법
PDF 파일은 신뢰성이 높기 때문에 계약서나 저작권 성명 등 중요한 내용을 보관하는데 자주 사용됩니다. 그러나 때로는 중복 페이지를 삭제하거나 파일 크기를 줄이기 위해 중요하지 않은 내용을 삭제해야 할 수도 있습니다. 이를 위해 PDF 문서를 Word 문서로 변환할 수 있지만, Free Spire.PDF for Java프로그래밍 라이브러리를 사용하여 프로그래밍 방식으로 PDF 파일에서 지정된 페이지를 빠르게 제거할 수도 있습니다. 아래는 구체적인 튜토리얼입니다.

## 프로그램 환경
IntelliJ IDEA 2018 (jdk 1.8.0)

먼저Free Spire.PDF for Java를 설치하십시오.

이 링크에서  Free Spire.PDF for Java를 다운로드하고 패키지의 압축을 풀 수 있습니다.그런 다음 IDEA에서 새 프로젝트를 만들고 차례로 "파일" - "프로젝트 구조" - "모듈" - "의존성"을 클릭합니다. 오른쪽 초록색 플러스 아래에있는 "JAR 또는 디렉토리"를 선택하고, 압축 해제 된 패키지의 lib 폴더에서 "Spire.PDF.jar"를 찾아 프로젝트로 가져옵니다.

```java
import com.spire.pdf.*;

public class DeletePage {
    public static void main(String[] args) {

        //PdfDocument 인스턴스 만들기
        PdfDocument pdf = new PdfDocument();

        //PDF 문서 로드
        pdf.loadFromFile("sample.pdf");

        //두 번째 페이지 삭제
        pdf.getPages().removeAt(1);

        //문서를 다른 파일에 저장
        pdf.saveToFile("output/result.pdf");
        pdf.close();
    }
}
```

먼저 PdfDocument 객체를 인스턴스화합니다. 그런 다음 pdf.loadFromFile() 메서드를 사용하여 PDF 문서를 로드합니다. pdf.getPages().removeAt() 메서드를 통해 PDF 문서의 페이지 컬렉션을 가져와 지정된 페이지를 삭제합니다. 마지막으로 수정된 PDF 문서가 다른 파일 "result.pdf"에 저장됩니다.

이 코드는 PDF 파일에서 지정된 페이지를 삭제하는 방법을 보여줍니다. 이 외에도 Free Spire.PDF for Java는 "PDF에 페이지 추가", "PDF 파일의 페이지 크기 변경"도 지원합니다.','MARKDOWN', '2023-06-18 17:14:36.231', NULL, 'Java 프로세스를 통해 PDF 문서의 페이지를 삭제하는 방법', 'admin', NULL);







-- sample
insert into `core_sample` (`sample_id`, `name`, `type`) values ('sample001', 'sample 001', 'A');
insert into `core_sample_item` (`sample_id`, `item_id`, `name`) values ('sample001','item01','Item 01');
insert into `core_sample_item` (`sample_id`, `item_id`, `name`) values ('sample002','item02','Item 02');



