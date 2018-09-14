insert into user(id,email,password,first_name,last_name) values (1,'josh@example.com','password','Josh','Cummings');
insert into user(id,email,password,first_name,last_name) values (2,'joe@example.com','password','Joe','Grandja');
insert into user(id,email,password,first_name,last_name) values (3,'rob@example.com','password','Rob','Winch');

insert into user_authority(id,user_id,authority) values (1,1,'ROLE_USER');
insert into user_authority(id,user_id,authority) values (2,2,'ROLE_USER');
insert into user_authority(id,user_id,authority) values (3,3,'ROLE_USER');
