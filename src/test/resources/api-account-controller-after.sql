delete from user_roles where user_id in (select id from person where e_mail = "newuser@gmail.com");
delete from person_notification_settings where person_id in (select id from person where e_mail = "newuser@gmail.com");
delete from person where e_mail = "newuser@gmail.com";