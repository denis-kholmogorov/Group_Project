INSERT INTO `person`(`id`, `e_mail`, `first_name`, `is_blocked`, `last_name`, `password`, `reg_date`) VALUES ('10', 'test1@mail.ru', 'first1', 'N', 'last1', '$2a$10$EqHML.VBGRVxqylVOwETdO3.ueCbsCKF.UPO3AN16l1Fn2pYENPjW', '2020-04-06 06:10:51');
INSERT INTO `person`(`id`, `e_mail`, `first_name`, `is_blocked`, `last_name`, `password`, `reg_date`) VALUES ('2', 'test2@mail.ru', 'first2', 'N', 'last2', '$2a$10$EqHML.VBGRVxqylVOwETdO3.ueCbsCKF.UPO3AN16l1Fn2pYENPjW', '2020-04-06 06:10:51');
INSERT INTO `person`(`id`, `e_mail`, `first_name`, `is_blocked`, `last_name`, `password`, `reg_date`) VALUES ('3', 'test3@mail.ru', 'first3', 'N', 'last3', '$2a$10$EqHML.VBGRVxqylVOwETdO3.ueCbsCKF.UPO3AN16l1Fn2pYENPjW', '2020-04-06 06:10:51');
INSERT INTO `person`(`id`, `e_mail`, `first_name`, `is_blocked`, `last_name`, `password`, `reg_date`) VALUES ('4', 'test3@mail.ru', 'first4', 'N', 'last4', '$2a$10$EqHML.VBGRVxqylVOwETdO3.ueCbsCKF.UPO3AN16l1Fn2pYENPjW', '2020-04-06 06:10:51');

INSERT INTO `roles` (`id`, `name`) VALUES ('5', 'ROLE_USER');

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES ('10', '5');
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES ('2', '5');
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES ('3', '5');
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES ('4', '5');

INSERT INTO `dialog`(`id`) VALUES ('6');
--INSERT INTO `dialog`(`id`) VALUES ('7');

INSERT INTO `dialog_user` VALUES('6', '10');
INSERT INTO `dialog_user` VALUES('6', '2');
--INSERT INTO `dialog_user` VALUES('7', '1');
--INSERT INTO `dialog_user` VALUES('7', '3');

INSERT INTO `message`(`id`, `author_id`, `message_text`, `read_status`, `recipient_id`, `time`, `dialog_id`) VALUES ('9', '10', 'hello world', 'SENT', '2', '2020-04-06 06:07:11', '6');





