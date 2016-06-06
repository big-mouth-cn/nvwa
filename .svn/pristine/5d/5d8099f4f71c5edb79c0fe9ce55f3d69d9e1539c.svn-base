CREATE SEQUENCE seq_common_user
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1000
  CACHE 1;
CREATE SEQUENCE seq_common_role
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1000
  CACHE 1;
CREATE SEQUENCE seq_common_resource
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1000
  CACHE 1;

-- Table: tbl_common_user

-- DROP TABLE tbl_common_user;

CREATE TABLE tbl_common_user
(
  id numeric(19,0) NOT NULL DEFAULT NEXTVAL('seq_common_user'),
  login_name character varying(50),
  username character varying(50),
  password character varying(50),
  useable integer DEFAULT 1, -- 0：不可用、1：可用
  email character varying(255),
  telephone character varying(20),
  create_time timestamp(0) without time zone not null default now(),
  modify_time timestamp(0) without time zone,
  CONSTRAINT pk_common_user PRIMARY KEY (id )
);
COMMENT ON TABLE tbl_common_user IS '系统用户信息表';
COMMENT ON COLUMN tbl_common_user.useable IS '0：不可用、1：可用';

-- Table: tbl_common_role

-- DROP TABLE tbl_common_role;

CREATE TABLE tbl_common_role
(
  id numeric(19,0) NOT NULL DEFAULT NEXTVAL('seq_common_role'),
  type integer default 0,
  role_name character varying(100),
  role_identifying character varying(255),
  description character varying(500),
  is_default integer DEFAULT 0,
  create_time timestamp(0) without time zone not null default now(),
  modify_time timestamp(0) without time zone,
  CONSTRAINT pk_common_role PRIMARY KEY (id )
);
COMMENT ON TABLE tbl_common_role IS '系统角色信息表';
COMMENT ON COLUMN tbl_common_role.type IS '角色类型：0- 普通、99- 管理员';

  -- Table: tbl_common_resource

-- DROP TABLE tbl_common_resource;

CREATE TABLE tbl_common_resource
(
  id numeric(19,0) NOT NULL DEFAULT NEXTVAL('seq_common_resource'),
  parent_id numeric(19,0),
  name character varying(255),
  resource_identifying character varying(255),
  type integer, -- 1 公共资源; 2 私有资源
  url character varying(255),
  order_by numeric,
  description character varying(500),
  create_time timestamp(0) without time zone not null default now(),
  modify_time timestamp(0) without time zone,
  CONSTRAINT pk_common_resource PRIMARY KEY (id )
) ;
COMMENT ON TABLE tbl_common_resource IS '系统资源定义表';
COMMENT ON COLUMN tbl_common_resource.type IS '1 公共资源; 2 私有资源';


-- Table: tbl_common_role_resource_ref

-- DROP TABLE tbl_common_role_resource_ref;

CREATE TABLE tbl_common_role_resource_ref
(
  role_id numeric(19,0),
  resource_id numeric(19,0),
  CONSTRAINT pk_common_role_resource_ref PRIMARY KEY (role_id, resource_id )
);
COMMENT ON TABLE tbl_common_role_resource_ref IS '角色资源对应表';

  -- Table: tbl_common_role_user_ref

-- DROP TABLE tbl_common_role_user_ref;

CREATE TABLE tbl_common_role_user_ref
(
  role_id numeric(19,0) NOT NULL,
  user_id numeric(19,0) NOT NULL,
  CONSTRAINT pk_common_role_user_ref PRIMARY KEY (role_id , user_id )
);
COMMENT ON TABLE tbl_common_role_user_ref IS '系统角色用户对应表';

INSERT INTO tbl_common_resource(id, parent_id, name, resource_identifying, type, url, order_by, description, create_time, modify_time)
VALUES (1, 0, '权限', 'authority', 2, '/authority', 99, '管理系统角色、资源、权限', NOW(), NULL);

INSERT INTO tbl_common_resource(id, parent_id, name, resource_identifying, type, url, order_by, description, create_time, modify_time)
VALUES (2, 1, '用户', 'authority:user', 2, '/authority/user', 0, '用户管理', NOW(), NULL);

INSERT INTO tbl_common_resource(id, parent_id, name, resource_identifying, type, url, order_by, description, create_time, modify_time)
VALUES (3, 1, '角色', 'authority:role', 2, '/authority/role', 0, '角色管理', NOW(), NULL);

INSERT INTO tbl_common_resource(id, parent_id, name, resource_identifying, type, url, order_by, description, create_time, modify_time)
VALUES (4, 1, '资源', 'authority:resource', 2, '/authority/resource', 0, '资源管理', NOW(), NULL);

INSERT INTO tbl_common_role(id, type, role_name, role_identifying, description, is_default, create_time, modify_time)
VALUES (1, 99, '系统管理员', 'admin', '系统管理员', 0, NOW(), NULL);

INSERT INTO tbl_common_user(id, login_name, username, password, useable, email, telephone, create_time, modify_time)
VALUES (1, 'root', '系统管理员', 'abc123456!.', 1, '', '', NOW(), NULL);

INSERT INTO tbl_common_role_user_ref(role_id, user_id) VALUES (1, 1);
