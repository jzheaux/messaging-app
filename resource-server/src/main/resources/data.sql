insert into user_profile(id,user_id,first_name,last_name,email) values (100,'jcummings','Josh','Cummings','jcummings@springsecurity.io');
insert into user_profile(id,user_id,first_name,last_name,email) values (101,'jgrandja','Joe','Grandja','jgrandja@springsecurity.io');
insert into user_profile(id,user_id,first_name,last_name,email) values (102,'rwinch','Rob','Winch','rwinch@springsecurity.io');

insert into user_contact(id,owner_user_id,contact_user_id) values (100,'jcummings','jgrandja');
insert into user_contact(id,owner_user_id,contact_user_id) values (101,'jcummings','rwinch');

insert into user_contact(id,owner_user_id,contact_user_id) values (102,'jgrandja','jcummings');
insert into user_contact(id,owner_user_id,contact_user_id) values (103,'jgrandja','rwinch');

insert into message(id,created,to_id,from_id,summary,text) values (100,'2018-09-20 08:00:00','jcummings','jgrandja','Hello Josh','This message is for Josh');
insert into message(id,created,to_id,from_id,summary,text) values (101,'2018-09-20 10:00:00','jcummings','rwinch','How are you Josh?','This message is for Josh');
insert into message(id,created,to_id,from_id,summary,text) values (102,'2018-09-21 14:00:00','jcummings','jgrandja','Is this secure?','This message is for Josh');

insert into message(id,created,to_id,from_id,summary,text) values (200,'2018-09-18 10:00:00','jgrandja','jcummings','Hello Joe','This message is for Joe');
insert into message(id,created,to_id,from_id,summary,text) values (201,'2018-09-20 12:00:00','jgrandja','rwinch','How are you Joe?','This message is for Joe');
insert into message(id,created,to_id,from_id,summary,text) values (202,'2018-09-21 18:00:00','jgrandja','jcummings','Is this secure?','This message is for Joe');

insert into message(id,created,to_id,from_id,summary,text) values (300,'2018-09-19 11:00:00','rwinch','jgrandja','Hello Rob','This message is for Rob');
insert into message(id,created,to_id,from_id,summary,text) values (301,'2018-09-21 13:00:00','rwinch','jcummings','How are you Rob?','This message is for Rob');
insert into message(id,created,to_id,from_id,summary,text) values (302,'2018-09-22 16:00:00','rwinch','jcummings','Is this secure?','This message is for Rob');




