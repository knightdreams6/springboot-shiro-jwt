drop schema if exists project;

create schema project collate utf8mb4_0900_ai_ci;


create table project.sys_user
(
  id          varchar(20) not null comment '用户id'
    primary key,
  su_phone    char(18)    not null comment '手机号',
  su_password char(200)   not null comment '密码',
  su_avatar   varchar(255) null comment '头像',
  su_name     char(20) null comment '用户名',
  su_sex      tinyint(1) default 0 null comment '性别0男1女',
  su_birth    date null comment '出生日期',
  su_mail     varchar(20) default '' null comment '邮箱',
  create_date datetime null comment '创建时间',
  update_date datetime null comment '更新时间',
  delete_date datetime null comment '删除时间',
  deleted     tinyint(1) default 0 null comment '0未删除1已删除',
  constraint phone
    unique (su_phone) comment '手机号唯一索引'
) comment '用户' row_format = DYNAMIC;


create table project.sys_role
(
  id          varchar(20) not null comment '角色id'
    primary key,
  sr_name     varchar(30) not null comment '角色名称',
  sr_key      varchar(20) not null comment '角色权限字符串',
  sr_remark   varchar(500) null comment '备注',
  create_date datetime null comment '创建时间',
  update_date datetime null comment '更新时间',
  delete_date datetime null comment '删除时间',
  deleted     tinyint(1) default 0 not null,
  constraint ux_role_key
    unique (sr_key) comment '角色权限key唯一索引'
) comment '角色' row_format = DYNAMIC;


create table project.sys_user_role
(
  id          varchar(20) not null comment '主键id'
    primary key,
  user_id     varchar(20) not null comment '用户ID',
  role_id     varchar(20) not null comment '角色ID',
  create_date datetime null comment '创建时间',
  constraint uk_role_id_user_id
    unique (user_id, role_id) comment '唯一索引'
) comment '用户角色关联表' row_format = DYNAMIC;


create table project.sys_perm
(
  id          varchar(20) not null comment '权限id'
    primary key,
  sp_name     varchar(50) not null comment '权限名称',
  sp_key      varchar(20) null comment '权限标识',
  sp_remark   varchar(500) default '' null comment '备注',
  create_date datetime null comment '创建时间',
  update_date datetime null comment '更新时间',
  delete_date datetime null comment '删除时间',
  deleted     tinyint(1) default 0 not null,
  constraint ux_perms_key
    unique (sp_key) comment '权限key唯一索引'
) comment '权限' row_format = DYNAMIC;


create table project.sys_role_perm
(
  id          varchar(20) not null comment '主键id'
    primary key,
  role_id     varchar(20) not null comment '角色ID',
  perm_id     varchar(20) not null comment '权限ID',
  create_date datetime null comment '创建时间',
  constraint role_id
    unique (role_id, perm_id)
) comment '角色权限关联表' row_format = DYNAMIC;

create table project.multipart_files
(
  id             varchar(20)       not null comment 'id'
    primary key,
  mf_bucket_name varchar(50)       not null comment '桶名称',
  mf_object_name varchar(200)      not null comment '对象名称',
  mf_upload_id   varchar(100)      not null comment '上传唯一id',
  mf_hash        varchar(100)      not null comment '文件的hash值',
  mf_size        int               not null comment '文件大小',
  mf_chunks      int               not null comment '分片数量',
  mf_state       tinyint default 0 not null comment '0待合并1成功2失败',
  create_date    datetime null comment '创建时间',
  update_date    datetime null comment '更新时间',
  delete_date    datetime null comment '删除时间',
  deleted        tinyint(1) default 0 not null
) comment '分片文件记录' row_format = DYNAMIC;


create table project.multipart_chunk_files
(
  id              varchar(20)  not null comment 'id'
    primary key,
  mcf_bucket_name varchar(50)  not null comment '桶名称',
  mcf_object_name varchar(200) not null comment '对象名称',
  mcf_upload_id   varchar(100) not null comment '上传唯一id',
  mcf_part_number int          not null comment '分片位置',
  mcf_part_size   int          not null comment '分片大小',
  mcf_eTag        varchar(100) not null comment '分片eTag',
  create_date     datetime null comment '创建时间',
  update_date     datetime null comment '更新时间',
  delete_date     datetime null comment '删除时间',
  deleted         tinyint(1) default 0 not null
) comment '分片块文件记录' row_format = DYNAMIC;


-- 默认密码 123456
INSERT INTO project.sys_user (id, su_phone, su_password, su_avatar, su_name, su_sex, su_birth, su_mail,
                              create_date, update_date, delete_date, deleted)
VALUES ('1', '18735182285',
        '$shiro2$argon2id$v=19$t=1,m=65536,p=4$MPA60MRQhEogL3M/NNerFg$nePPF9Q0nTOKSjP8ncAyK7TjrGBSEylHV8Y9pPR2tZg',
        'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg', 'sweetBaby', 0, '1997-05-12', '',
        '2020-05-06 13:00:31', '2020-05-06 13:01:25', null, 0);

INSERT INTO project.sys_role (id, sr_name, sr_key, sr_remark, create_date, update_date, delete_date, deleted)
VALUES ('1', '管理员', 'admin', '管理员', '2020-06-22 08:58:26', '2020-06-22 08:58:26', null, 0);
INSERT INTO project.sys_role (id, sr_name, sr_key, sr_remark, create_date, update_date, delete_date, deleted)
VALUES ('2', '普通角色', 'common', '普通角色', '2020-06-22 08:58:26', '2020-06-22 08:58:26', null, 0);

INSERT INTO project.sys_user_role (id, user_id, role_id, create_date)
VALUES ('1', '1', '1', '2021-11-23 17:29:53');

INSERT INTO project.sys_perm (id, sp_name, sp_key, sp_remark, create_date, update_date, delete_date, deleted)
VALUES ('1', '查看用户权限', 'system:user:list', '', '2020-11-02 09:16:31', null, null, 0);
INSERT INTO project.sys_perm (id, sp_name, sp_key, sp_remark, create_date, update_date, delete_date, deleted)
VALUES ('2', '更新用户权限', 'system:user:update', '', '2020-11-02 09:16:31', null, null, 0);
INSERT INTO project.sys_perm (id, sp_name, sp_key, sp_remark, create_date, update_date, delete_date, deleted)
VALUES ('3', '删除用户权限', 'system:user:remove', '', '2020-11-02 09:16:31', null, null, 0);
INSERT INTO project.sys_perm (id, sp_name, sp_key, sp_remark, create_date, update_date, delete_date, deleted)
VALUES ('4', '添加用户权限', 'system:user:insert', '', '2020-11-02 09:16:31', null, null, 0);
INSERT INTO project.sys_perm (id, sp_name, sp_key, sp_remark, create_date, update_date, delete_date, deleted)
VALUES ('5', '用户所有权限', 'system:user:*', '', '2020-11-02 09:16:31', null, null, 0);
INSERT INTO project.sys_perm (id, sp_name, sp_key, sp_remark, create_date, update_date, delete_date, deleted)
VALUES ('6', '上传附件', 'attachment:insert', '', '2020-11-02 09:16:31', null, null, 0);
INSERT INTO project.sys_perm (id, sp_name, sp_key, sp_remark, create_date, update_date, delete_date, deleted)
VALUES ('7', '下载附件', 'attachment:get', '', '2020-11-02 09:16:31', null, null, 0);
INSERT INTO project.sys_perm (id, sp_name, sp_key, sp_remark, create_date, update_date, delete_date, deleted)
VALUES ('8', '删除附件', 'attachment:remove', '', '2020-11-02 09:16:31', null, null, 0);
INSERT INTO project.sys_perm (id, sp_name, sp_key, sp_remark, create_date, update_date, delete_date, deleted)
VALUES ('9', '附件所有权限', 'attachment:*', '', '2020-11-02 09:16:31', null, null, 0);

INSERT INTO project.sys_role_perm (id, role_id, perm_id, create_date)
VALUES ('1', '1', '5', '2021-11-23 17:49:28');
INSERT INTO project.sys_role_perm (id, role_id, perm_id, create_date)
VALUES ('2', '1', '9', '2023-01-20 19:41:07');
