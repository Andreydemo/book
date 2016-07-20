insert into user(name, username, password) values ('Roy','user','user');
insert into user(name, username, password) values ('Craig','admin','admin');

insert into role(id, name) values (1,'ROLE_USER');
insert into role(id, name) values (2,'ROLE_ADMIN');

insert into user_role(user_id, role_id) values (1,1);
insert into user_role(user_id, role_id) values (2,1);
insert into user_role(user_id, role_id) values (2,2);