INSERT INTO `person`(`id`, `e_mail`, `first_name`, `is_blocked`, `last_name`, `password`, `reg_date`) VALUES ('1', 'test1@mail.ru', 'first1', 'N', 'last1', '$2a$10$EqHML.VBGRVxqylVOwETdO3.ueCbsCKF.UPO3AN16l1Fn2pYENPjW', '2020-04-06 06:10:51');
INSERT INTO `person`(`id`, `e_mail`, `first_name`, `is_blocked`, `last_name`, `password`, `reg_date`) VALUES ('2', 'test2@mail.ru', 'first2', 'N', 'last2', '$2a$10$EqHML.VBGRVxqylVOwETdO3.ueCbsCKF.UPO3AN16l1Fn2pYENPjW', '2020-04-06 06:10:51');
INSERT INTO `person`(`id`, `e_mail`, `first_name`, `is_blocked`, `last_name`, `password`, `reg_date`) VALUES ('3', 'test3@mail.ru', 'first3', 'N', 'last3', '$2a$10$EqHML.VBGRVxqylVOwETdO3.ueCbsCKF.UPO3AN16l1Fn2pYENPjW', '2020-04-06 06:10:51');
INSERT INTO `person`(`id`, `e_mail`, `first_name`, `is_blocked`, `last_name`, `password`, `reg_date`) VALUES ('4', 'test4@mail.ru', 'first4', 'N', 'last4', '$2a$10$EqHML.VBGRVxqylVOwETdO3.ueCbsCKF.UPO3AN16l1Fn2pYENPjW', '2020-04-06 06:10:51');


INSERT INTO `dialog`(`id`) VALUES ('1');
INSERT INTO `dialog`(`id`) VALUES ('2');

INSERT INTO `dialog_user` VALUES('1', '1');
INSERT INTO `dialog_user` VALUES('1', '2');
INSERT INTO `dialog_user` VALUES('2', '1');
INSERT INTO `dialog_user` VALUES('2', '3');

INSERT INTO `roles`(`id`,`name`) VALUES(1,"ROLE_USER");

INSERT INTO `user_roles`(`user_id`,`role_id`) VALUES(1,1);
INSERT INTO `user_roles`(`user_id`,`role_id`) VALUES(2,1);
INSERT INTO `user_roles`(`user_id`,`role_id`) VALUES(3,1);
INSERT INTO `user_roles`(`user_id`,`role_id`) VALUES(4,1);

INSERT INTO `message`(`id`,`author_id`,`message_text`, `read_status`, `recipient_id`, `time`, `dialog_id`)
VALUES ('1','1', 'hello world', 'SENT', '2', '2020-04-06 06:07:11', '1');


