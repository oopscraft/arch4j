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
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_USER');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_ROLE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_MENU');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_BOARD');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_PAGE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_GIT');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_EMAIL');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_MESSAGE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_MESSAGE_EDIT');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_VARIABLE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_VARIABLE_EDIT');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_CODE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ADMIN_CODE_EDIT');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'ACTUATOR');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'H2-CONSOLE');
insert into `core_role_authority` (`role_id`,`authority_id`) values ('DEV', 'SWAGGER-UI');

-- user
insert into `core_user` (`user_id`,`user_name`,`password`,`type`,`status`) values ('user','User','{noop}user','GENERAL','ACTIVE');
insert into `core_user` (`user_id`,`user_name`,`email`,`password`,`type`,`status`) values ('admin','Administrator',null,'{noop}admin','GENERAL','ACTIVE');
insert into `core_user_role` (`user_id`,`role_id`) values ('admin','ADMIN');
insert into `core_user` (`user_id`,`user_name`,`password`,`type`,`status`) values ('dev','Developer Account','{noop}dev','SYSTEM','ACTIVE');
insert into `core_user_role` (`user_id`,`role_id`) values ('dev','DEV');

-- email
insert into `core_email_template` (`template_id`,`template_name`,`subject`,`content`) values ('VERIFICATION','Verification Email', 'Verification Answer: [[${answer}]]', 'Verification Answer: [[${answer}]]');

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
insert into `core_board_role` (`board_id`,`role_id`,`type`)
     values ('notice','ADMIN', 'WRITE');

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
insert into `core_menu` (`menu_id`,`parent_menu_id`,`menu_name`,`link`,`sort`,`icon`) values
    ('fce30ba305ba4742a84cdc41996810fd',null,'Board Demo','/page/board',1,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAAXNSR0IArs4c6QAABGJJREFUeF7tWl1oHUUUPmeuZSkaDdg+WIPcO7PJNQ0VQnxQsFANSAriiz/VUBRbKdVaX3yQCMWrICiCCLYGxIBgS6XxTUlVUiulIIIRqXg1zczubWgNFiWhtGCT3T0yyQaC3tzdzZ2Ylcy+hNw9880535yzM/NxENb5g+s8frAE2AwwzMDAmanNziwMElIfAN5oBp6uRYAnC4yeq9y/5Q8zmAsoxkugMjr1KSE8YtLJRSwEGK703va4SezUBBSLxdZCoXAPIrY0cqB/8JuPkRUcIrjIEL414WxEcC8itEVh+Nfx53c8lYB5JQiC72q12kyauVMRwDl/BgDeRcSbk0B3f3B23uTPC+PVw3t2dCXZp3n/wtCp6q3FrZ3a9ui++xKHENE1xtjLUsojScaJBAgh+gBgJG255IGAOGhCxJ1Syi8bkZBIQKlU+oEx1g0AIQC8iojn81wCRFQGgAoAFABgTCl1d1MEcM5nEXFDFEVf+L6/MymlDp2aGmYAjybZrex9dOK13tt3JY0tlUonGWN9RDTreZ7TFAFCCNIARDTseV7iF7hy+rdNYYSDSNSHiDclOZvmPRFdRaQRZHggzTbIOT+BiI9pbKVUwyxPLIGsBKQJaLVtLAE2A2wJ2G+A/QjaXcBug/YcYA9C9iRoj8L2LmAvQ/Y2aK/DVg/4TxWh1RY70uBbQcQKIlYQsYKIFUSsIGIFESuIWEHECiJWELGCSLOCSEEIEcQXkAki+jzNZWStbRDxIQBojz+CN8TNHXXdWvauzDnfhohHAeCutQ6oyfnPM8Z2TUxM/FgPpy4BnPNbEPEcANzR5OS5GE5El4Ig2DY5OTn9T4fqEuC67itE9EZsPEJE47mIJKMTiHgnAMy39RDRgOd5b6Yl4BgR9Wtjx3FaqtXq1Yxz58K8XC63BEFwRTuDiMeklLtTEZBFUclFpA2cSGrxWa4Ehohoj8YNw7CzVqv9mvdA6/nX3t7eGUVRNS6BIc/znk2bAf06ZWLjywBwloh0n2CWBxFxvlOUiH7Wf7IMXsha7CIiPU4HkWk8IhaIaDsibo7nfVIp9UkqAnSToRDiKwB4IKPTeTUfVUrpjtd/LeKy54Cenp4N09PTA4i493+8HV4AgA9bW1vfGhsbm0t9Dmh2GTnn+xFxUONEUfSe7/svrgRTCHEIAF6Py+h9z/MOrASn0ZjERsmsE+rMmZmZ0f3ERSK6zhhzpZQXs+Jo+7a2to2O4+j6L+r0JaJuz/N+WgnWcmOME2Bq9RcdFkI8AQDH4/+/Vkr15pYAk6u/NEghxBkA2K5/Q8SHpZSfmSLBaAaYXv3FIF3X7Sai7wGAEZFijHVJKa+bIMEYAau1+ktK4SMAeDrOgpeklO/kigDOeS8ijsZOHVZKHTTh4CJGuVzeMjc3Nx634P+ilNpqAt9YBnR0dGwKw/D0ws4XPej7/u8mHFyK4bruXiJ6GwCOKKX0Ftn0Y4yApj1ZIwBLwBoRn5tp130G/A33IBFuxy9ymgAAAABJRU5ErkJggg==');
insert into `core_menu` (`menu_id`,`parent_menu_id`,`menu_name`,`link`,`sort`,`icon`) values
    ('01f9240a225f4b5c821e00a5fe1b9353','fce30ba305ba4742a84cdc41996810fd','Anonymous Board','/board/anonymous',1,'data:image/png;base64,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');
insert into `core_menu` (`menu_id`,`parent_menu_id`,`menu_name`,`link`,`sort`,`icon`) values
    ('188de70ba71e4d23bafa4a232379efff','fce30ba305ba4742a84cdc41996810fd','Member Board','/board/member',2,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAABW1JREFUWEe9l2lMVFcUx//nPsCFpglSl0TFXSsVq1XTGqsozow6g1RmQdMPSIxLXVps2qDWjRhs1VaIRuPauFcDzGCdBZkZlaq1torRUms0bY1pVVxQKIICwz3NG0ODsg7avk8v793zP79z7j3n3ksI8NFMMg0lwfGAeIvBXQGuZClukZAnJSPnuMt2IxBJaulgjd4SRcTrAdbW2gSHhDBLCZ/P59dh5hoS2CWFb9mxI0futES7RQAavSlBEPYw0DYyKgpjxo/DwDcGoX1oqN/HwwfFKLx4Cd7cXNy+eQsg3BTguDyH7UJzEM0CaPTm9wBpa9O2Hc2aP5eGDB/eqKaUEkftduRkZqnpKJEs3z7myrnWFESTADF6Yw+F6JeQkJDQlJUrqEevXs0F5P///XcnsXvbNvX11wdF4UMKCrZXN2bYJIBOb9zDRIlJs2dj1NjoFjmvHbR35w6cOp4PBuZ5ndYtAQOMj48Pp0pxp2v3bkrq2jUgana2nvFRWlKKJcnJXFVVfdXrsg4MGEATa04k5j3GaVMxKS4uoOhrB29Oz8DF8+chWQ5obC00GpZWb14L4pRFK1ei74D+rQLwuFzI3H8ADDJ5ndm2hkQaBaid/9UZ6ejUuXOrAH46cwY7Nm0GGAs8LuvmgAC0etMWED5YuWYNukV0bxXAyRMnsG/HThCQ5HZa9wQEoDGYlhKQNu/jhRg6YkSrAKwHD/n7Aog0Hkf2sYAAxsdaRgqWZ0ZFj0HSnDmtAljxaQqKbt180kZUhdvt9oqAAFJTU8WZc4XXleCg7p9npFNYh/CAIC4WFGDz+nR1h8jyOG0JAZehaqDVG5NAtKt3v75Qq0EI0SKI0tISrFqylEsfPmRFocF5duvl1gCQzmCZJSE3EtDmzWHDMDc5GUqQ0iREWdkjfLFiOe7duevfDyAww+Ow5QQEYLFYlJIK3zeASGDmciGojBld+vTri6nTE9Grd596esyMC+fOIXPffn5QXEwM/E5ADwBBYFrncWUvavEa0BlMaQwsleCCGlZiEYpHwRW+r1UgVSSiZ08MjBqEsLAOkLIGd4vuoPDSRS6+d1/t2D4pOc3rsq2aMNkUKSWcUEEIcz0O69bnIeo1orF6S5cg1NwAUbEvuCYy//DhklqjCQbjOClpEQRiAAQ/I0b4G8C3Usq0um1X3VEFQV0DlaI6NMLt3lde164egFZvngPircRIcbusXzaUNt1k0+sscaXOP1+QbBuem3tAhaj3aA3mrwD+hATi3Xbr4SYBdJPMGSx4IRON8zqy8xsBmMISOf2Hj0bl43LcuHwBxHjH7bL+2AiABeDMhoKqlwGdwbSdgVkMGel15tSN8l9trcG4GqDPRpuS8KS8DOeOWsGED70O66aGADSxlneJ5SmA13qctsVNZ6AFABp9fB6R0MXNW4YnFWVw794AYt7rdtmm/x8ApDGY7r/yaliYfnYKgRm2jansq6pq9ODxUjMQM2lKH0Uov3UbEIWRk9/3B3zi0Hbc/+s6VwfXdKhbNbXZeKkAOoN5GoMPDo6eiAEjnp4TL+W7cO38KQhwTJ7TduL5aXipALUlNTZhJjpGPO2If179GWftBwHQYo8ze+1/C6A35pMQ0XELliOkTTu/r/LSh3DtWAciWN0Oq/mFANRbEAgflbcX2h+ysh7XEzMY7ypKcMfohJl4rava6oGiP67hdM5u9Wp2xeO0Rj5vo56wRaXIZcJyr9Oa12QZNlRGdb9p9ab5DN6gKIoYEhNL1ZWVKDztZjBXE9EMtyP7QHMaLwSgGuv0xjEQIouZOz0Vo9sspdGbazsbiHO/ZaAGteMnTpzS06co+wlUUc2UmO/KKmqN1j+PxZs/bL7TnQAAAABJRU5ErkJggg==');
insert into `core_menu` (`menu_id`,`parent_menu_id`,`menu_name`,`link`,`sort`,`icon`) values
    ('408f6d1824e143d18d3e4ef24ffedabc','fce30ba305ba4742a84cdc41996810fd','Notice Board','/board/notice',3,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAmBJREFUWEft1kvIzmkYBvDfJxlKSmMWRIoiOTSRU6EsJHIuh4zlsDEk7FiyYzYTK7KymBHlkNNCiaKhJElT0gjNwoJkYyi66vnr/d7e03eoT/nuehfv89yH67me+77+T5cBtq4Brq8vAMZjYTnAHbzszWH6AmAj/ipFN+HMIID+ZGADLuH/FkkXYG/Z/x13W/gOwyqcq/dp1AP7cARXsB4fenOympgUT6+sxUEcrs1XD2A0HmNscbqANFvFxCiswxpMx4Ti96LEnUd+78p6iqc54x/7r8S9qUA0YmAqbmAc3mNRSf4bDmBMG0Ze4xCOYQZuY2QpvhT/tGKg2guIi/gVj/AnltUEfsYTPC9rEzGNbrpyDVswEyexur54YlvpwFAMxy38XAqFkfTHCbyqYyLCtL00Zk4ce4DFpY8+NWKunRCdRSYiFiZyl2+xu/TClEJvbe5JpQ9Cfyw9EKFqaK0ArMDlEvUU8zGrdPRPNdka5fgRfyNgYstxvacM3Mcc5L7nYghuYkRJFCYy+wHayAI434gAvId5PQEwGTl1LNewGQ/LCGXtFPbUjFszhiM80ZJYcj6rd2x2BRm5P4pzdCDNF2GKXcXKwkyzwtV67j4TFNuJ450COFojsxmxXdhfgmeX7m5XPPuJ/bc4JmeV42tsMwZOY2vx+gE7sKT8/wUfO6mOKGEl5cm5rVMGot2hPtZuVNthSRPHGo5js+SDAAYZ+L4ZyPjlrZc3X6zpl6zd/JX96ume70bejt2e743GsJrbDvP32K1bzW8SQKWAPT5ahwFtr6DDPP3j1led7zOKL04qfSGcgtpYAAAAAElFTkSuQmCC');
insert into `core_menu` (`menu_id`,`parent_menu_id`,`menu_name`,`link`,`sort`,`icon`) values
     ('bb9cdea4a96b46958468f3aca15832bc',null,'Documentation',null,2,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADQAAAA0CAYAAADFeBvrAAAAAXNSR0IArs4c6QAAAaxJREFUaEPtmjtOw0AQhr80UAAnAG6ABBWPG4DECaCHKtwFREUPJ+BxhdAhwQl4nCBQQAMaa41sY4mdZBnW1qSM5vX9f7yyMjugZ59Bz3hwoNwdrTo0CxwCe8AKMBc5/BvwAJwDZ8B7ZN6fhJVAi8AVsDpllztgF3iZss7E6QIkztwmgCmHEKjN/3JKgI6A44klaU8cAqeJa0aVEyBxZz0qOj5oBGzFh6eLFKAxMJ+uZFHpFVhIXDOqnAB9RkXmF9R6unYZqCrx9+naFyCBK07XPgEJ1LAN6Dq8MTzn99jUJloKbyY7lW9HbUDLQO4wJYPM+lgBGrcBde0NvHZKO1CGz5U7lKEptZHcIXfIWAH/yRkLrm7nDqklM05wh4wFV7dzh9SSGSe4Q8aCq9u5Q2rJjBPcIWPB1e3cIbVkxgnukLHg6nbukFoy44QfDjVXkp3/s765NL4J65QnY6W17UR4ueix3VynyAr+RFst0/hi4SUXL2QNv5bpkLFjyUpyo3o15rLDULWlcanADHAA7IfLS6nvLsQqHRsndyHugYvwLH1IYte2db/C9g7oCxuTcXAHtcXHAAAAAElFTkSuQmCC');
insert into `core_menu` (`menu_id`,`parent_menu_id`,`menu_name`,`link`,`sort`,`icon`) values
     ('57c2454133be4fe9b7bf352053186187','bb9cdea4a96b46958468f3aca15832bc','1.Installation','/git/arch4j/doc/01.installation/index.md',1,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAAXNSR0IArs4c6QAAAplJREFUeF7t2svrDlEcx/HXD7kkt0QiEomEiLAgdpSibGysSPZSbotfWViw4A9Qdna2LGVjgYVLIpHcErknkmunnqlf0/ye58zz/Hp+M86c3TTfM+d83vM533POnBmQeBlIXL8GQOOAxAk0Q6CkAf6WjO8l/C2u4xyu9fKgdnXLOqCfALJ+hzZP4xhGvP06AMhADOLkSDuhTgDC29+LiyMJoVcAZevH9n0O5rcEH8SEVsXv2IobsQ/qFFdWQH4Mlq3fqT9F9/fj/JAbr7EBL7p5WL5OWQGjAWAaPuU6fhub8bVXCHUAsA43C4Rexk787gVCHQCcweFhRIbp8cj/DCAkw0eY0kbkPlzoFkLVHXAJuzuI+4ltuNoNhKoD2NSy+A7abt3fYyMel4VQdQCZnpAIT2BXGxAPsL7szFAXABmIFTiOPRhT8LbDeuFAGRfUDUCmbQ3OYktO7B8ESMENUaWuAIK40PejOJVTGsAcilLfIbEUPWM0VoKdtATbh+VyVu5gdadK2f06OyDTsB1Xhgj+jOkpAViLWznB0S82OrDVQBWHwCoE2w8t0bqiAysMYCXupgwgTHv3UgawHPdTBrCsYOETPbSjAyucA5biYcoOWNL6ZpDsLLC4YBsc7ezowAoPgUV4kvIQWIinKQNYgGcpAwgnSM9TBjAPL1MGMBevUgYQzg7CeWGy64DZeJMygFkIv9Mk64CZeJcygBn4kDKA8AH0Y8oApiJ8CU42B4Sj8y8pA5hccCAavcuNDqzwdngSvqXsgIkIv88lmwPG40fKAMYh/CaTrAPG4lfKAEIiDz9GjIoDcu1W5jJ6dosOHGYarIzixgHNEOhPDqiq5bvuV9kc0HVDVa3YAKjqm+lXvxoH9It0Vdv5BwSbf0GhLrNkAAAAAElFTkSuQmCC');
insert into `core_menu` (`menu_id`,`parent_menu_id`,`menu_name`,`link`,`sort`,`icon`) values
     ('92bdfc7c38d6438680ff41420eb4ebb6','bb9cdea4a96b46958468f3aca15832bc','2.Configuration','/git/arch4j/doc/02.configuration/index.md',2,'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAAXNSR0IArs4c6QAAAplJREFUeF7t2svrDlEcx/HXD7kkt0QiEomEiLAgdpSibGysSPZSbotfWViw4A9Qdna2LGVjgYVLIpHcErknkmunnqlf0/ye58zz/Hp+M86c3TTfM+d83vM533POnBmQeBlIXL8GQOOAxAk0Q6CkAf6WjO8l/C2u4xyu9fKgdnXLOqCfALJ+hzZP4xhGvP06AMhADOLkSDuhTgDC29+LiyMJoVcAZevH9n0O5rcEH8SEVsXv2IobsQ/qFFdWQH4Mlq3fqT9F9/fj/JAbr7EBL7p5WL5OWQGjAWAaPuU6fhub8bVXCHUAsA43C4Rexk787gVCHQCcweFhRIbp8cj/DCAkw0eY0kbkPlzoFkLVHXAJuzuI+4ltuNoNhKoD2NSy+A7abt3fYyMel4VQdQCZnpAIT2BXGxAPsL7szFAXABmIFTiOPRhT8LbDeuFAGRfUDUCmbQ3OYktO7B8ESMENUaWuAIK40PejOJVTGsAcilLfIbEUPWM0VoKdtATbh+VyVu5gdadK2f06OyDTsB1Xhgj+jOkpAViLWznB0S82OrDVQBWHwCoE2w8t0bqiAysMYCXupgwgTHv3UgawHPdTBrCsYOETPbSjAyucA5biYcoOWNL6ZpDsLLC4YBsc7ezowAoPgUV4kvIQWIinKQNYgGcpAwgnSM9TBjAPL1MGMBevUgYQzg7CeWGy64DZeJMygFkIv9Mk64CZeJcygBn4kDKA8AH0Y8oApiJ8CU42B4Sj8y8pA5hccCAavcuNDqzwdngSvqXsgIkIv88lmwPG40fKAMYh/CaTrAPG4lfKAEIiDz9GjIoDcu1W5jJ6dosOHGYarIzixgHNEOhPDqiq5bvuV9kc0HVDVa3YAKjqm+lXvxoH9It0Vdv5BwSbf0GhLrNkAAAAAElFTkSuQmCC');
insert into `core_menu` (`menu_id`,`parent_menu_id`,`menu_name`,`link`,`target`,`sort`,`icon`) values
    ('1e9fd6f84bfb4504ae0e067c45244907',null,'Admin Console','/admin','_blank',3,'data:image/png;base64,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');
insert into `core_menu` (`menu_id`,`parent_menu_id`,`menu_name`,`link`,`target`,`sort`,`icon`) values
    ('767c59aa3b2344a7bbfdabead7b6c4a3',null,'Git Repository','https://github.com/oopscraft/arch4j','_blank',4,'data:image/png;base64,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');




-- demo data
INSERT INTO core_article
(article_id, board_id, title, content, content_format, user_id, created_at)
VALUES('8fb7622000744407af0d762c71fe02f1', 'anonymous', 'Markdown-it demo page contents (copy)', 'You will like those projects!
---
__Advertisement :)__

- __[pica](https://nodeca.github.io/pica/demo/)__ - high quality and fast image
  resize in browser.
- __[babelfish](https://github.com/nodeca/babelfish/)__ - developer friendly
  i18n with plurals support and easy syntax.

---

# h1 Heading 8-)
## h2 Heading
### h3 Heading
#### h4 Heading
##### h5 Heading
###### h6 Heading


## Horizontal Rules

___

---

***


## Typographic replacements

Enable typographer option to see result.

(c) (C) (r) (R) (tm) (TM) (p) (P) +-

test.. test... test..... test?..... test!....

!!!!!! ???? ,,  -- ---

"Smartypants, double quotes" and ''single quotes''


## Emphasis

**This is bold text**

__This is bold text__

*This is italic text*

_This is italic text_

~~Strikethrough~~


## Blockquotes


> Blockquotes can also be nested...
>> ...by using additional greater-than signs right next to each other...
> > > ...or with spaces between arrows.


## Lists

Unordered

+ Create a list by starting a line with `+`, `-`, or `*`
+ Sub-lists are made by indenting 2 spaces:
  - Marker character change forces new list start:
    * Ac tristique libero volutpat at
    + Facilisis in pretium nisl aliquet
    - Nulla volutpat aliquam velit
+ Very easy!

Ordered

1. Lorem ipsum dolor sit amet
2. Consectetur adipiscing elit
3. Integer molestie lorem at massa


1. You can use sequential numbers...
1. ...or keep all the numbers as `1.`

Start numbering with offset:

57. foo
1. bar


## Code

Inline `code`

Indented code

    // Some comments
    line 1 of code
    line 2 of code
    line 3 of code


Block code "fences"

```
Sample text here...
```

Syntax highlighting

``` js
var foo = function (bar) {
  return bar++;
};

console.log(foo(5));
```

## Tables

| Option | Description |
| ------ | ----------- |
| data   | path to data files to supply the data that will be passed into templates. |
| engine | engine to be used for processing templates. Handlebars is the default. |
| ext    | extension to be used for dest files. |

Right aligned columns

| Option | Description |
| ------:| -----------:|
| data   | path to data files to supply the data that will be passed into templates. |
| engine | engine to be used for processing templates. Handlebars is the default. |
| ext    | extension to be used for dest files. |


## Links

[link text](http://dev.nodeca.com)

[link with title](http://nodeca.github.io/pica/demo/ "title text!")

Autoconverted link https://github.com/nodeca/pica (enable linkify to see)


## Images

![Minion](https://octodex.github.com/images/minion.png)
![Stormtroopocat](https://octodex.github.com/images/stormtroopocat.jpg "The Stormtroopocat")

Like links, Images also have a footnote style syntax

![Alt text][id]

With a reference later in the document defining the URL location:

[id]: https://octodex.github.com/images/dojocat.jpg  "The Dojocat"


## Plugins

The killer feature of `markdown-it` is very effective support of
[syntax plugins](https://www.npmjs.org/browse/keyword/markdown-it-plugin).


### [Emojies](https://github.com/markdown-it/markdown-it-emoji)

> Classic markup: :wink: :crush: :cry: :tear: :laughing: :yum:
>
> Shortcuts (emoticons): :-) :-( 8-) ;)

see [how to change output](https://github.com/markdown-it/markdown-it-emoji#change-output) with twemoji.


### [Subscript](https://github.com/markdown-it/markdown-it-sub) / [Superscript](https://github.com/markdown-it/markdown-it-sup)

- 19^th^
- H~2~O


### [\\<ins>](https://github.com/markdown-it/markdown-it-ins)

++Inserted text++


### [\\<mark>](https://github.com/markdown-it/markdown-it-mark)

==Marked text==


### [Footnotes](https://github.com/markdown-it/markdown-it-footnote)

Footnote 1 link[^first].

Footnote 2 link[^second].

Inline footnote^[Text of inline footnote] definition.

Duplicated footnote reference[^second].

[^first]: Footnote **can have markup**

    and multiple paragraphs.

[^second]: Footnote text.


### [Definition lists](https://github.com/markdown-it/markdown-it-deflist)

Term 1

:   Definition 1
with lazy continuation.

Term 2 with *inline markup*

:   Definition 2

        { some code, part of Definition 2 }

    Third paragraph of definition 2.

_Compact style:_

Term 1
  ~ Definition 1

Term 2
  ~ Definition 2a
  ~ Definition 2b


### [Abbreviations](https://github.com/markdown-it/markdown-it-abbr)

This is HTML abbreviation example.

It converts "HTML", but keep intact partial entries like "xxxHTMLyyy" and so on.

*[HTML]: Hyper Text Markup Language

### [Custom containers](https://github.com/markdown-it/markdown-it-container)

::: warning
*here be dragons*
:::
', 'MARKDOWN', 'admin', '2023-07-15 14:01:17.777');



INSERT INTO core_article
(article_id, system_required, system_updated_by, system_updated_at, board_id, comment_count, vote_positive_count, vote_negative_count, content, content_format, created_at, password, title, user_id, user_name)
VALUES('7b70bab4b58d4265b18b7d5859efbb62', 'N', 'admin', '2023-06-18 17:14:36.236', 'anonymous', 0, 0, 0, 'We are uncovering better ways of developing software by doing it and helping others do it.
Through this work we have come to value:

Individuals and interactions over processes and tools
Working software over comprehensive documentation
Customer collaboration over contract negotiation
Responding to change over following a plan

That is, while there is value in the items on the right, we value the items on the left more.
© 2001, the Agile Manifesto authors
This declaration may be freely copied in any form, but only in its entirety through this notice.

1. Our highest priority is to satisfy the customer through early and continuous delivery of valuable software.

2. Welcome changing requirements, even late in development. Agile processes harness change for the customer’s competitive advantage.

3. Deliver working software frequently, from a couple of weeks to a couple of months, with a preference to the shorter timescale.

4. Business people and developers must work together daily throughout the project.

5. Build projects around motivated individuals. Give them the environment and support they need, and trust them to get the job done.

6. The most efficient and effective method of conveying information to and within a development team is face-to-face conversation.

7. Working software is the primary measure of progress.

8. Agile processes promote sustainable development. The sponsors, developers, and users should be able to maintain a constant pace indefinitely.

9. Continuous attention to technical excellence and good design enhances agility.

10. Simplicity–the art of maximizing the amount of work not done–is essential.

11. The best architectures, requirements, and designs emerge from self-organizing teams.

12. At regular intervals, the team reflects on how to become more effective, then tunes and adjusts its behavior accordingly.
','MARKDOWN', '2023-06-18 17:14:36.231', NULL, 'The 12 Principles behind the Agile Manifesto(from agilealliance.org)', 'admin', NULL);







-- sample
insert into `core_sample` (`sample_id`, `name`, `type`) values ('sample001', 'sample 001', 'A');
insert into `core_sample_item` (`sample_id`, `item_id`, `name`) values ('sample001','item01','Item 01');
insert into `core_sample_item` (`sample_id`, `item_id`, `name`) values ('sample002','item02','Item 02');





-- test
insert into `core_board_role` (`board_id`,`role_id`,`type`)
     values ('anonymous', 'DEV', 'READ_ARTICLE');

