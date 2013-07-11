DROP TABLE t_biz_menu;
CREATE TABLE t_biz_menu ( id varchar(255) NOT NULL, menu_index int, menu_name varchar(255), menu_url varchar(255), pid varchar(255), PRIMARY KEY (id), CONSTRAINT FK2EAF87636C6444C FOREIGN KEY (pid) REFERENCES t_biz_menu (id), INDEX FK2EAF87636C6444C (pid) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into t_biz_menu (id, menu_index, menu_name, menu_url, pid) values ('1068a7be0163fa8ce95013fa8ce95a50000', 1, '菜单管理', '/views/menu/menuTree.jsp', null);
insert into t_biz_menu (id, menu_index, menu_name, menu_url, pid) values ('1068a7be0163fa8ce95013fa8cf93bc0001', 2, '流程管理', '', null);
insert into t_biz_menu (id, menu_index, menu_name, menu_url, pid) values ('1068a7be0163fa8ce95013fa8d04f9a0002', 1, '流程模版列表', '/views/process/deploymentList.jsp', '1068a7be0163fa8ce95013fa8cf93bc0001');
insert into t_biz_menu (id, menu_index, menu_name, menu_url, pid) values ('1068a7be0d83fc7b09a013fc7b09a800000', 2, '流程定义', '/views/process/processDefList.jsp', '1068a7be0163fa8ce95013fa8cf93bc0001');
insert into t_biz_menu (id, menu_index, menu_name, menu_url, pid) values ('1068a7be0ef3fcb5128013fcb5128a50000', 3, '任务', '', null);
insert into t_biz_menu (id, menu_index, menu_name, menu_url, pid) values ('1068a7be0ef3fcb5128013fcb5203750001', 0, '我的任务', '/views/task/myTaskList.jsp', '1068a7be0ef3fcb5128013fcb5128a50000');
