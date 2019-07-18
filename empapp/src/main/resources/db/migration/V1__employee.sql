--create table employees (id bigint auto_increment, emp_name varchar(255),
--      constraint pk_employee primary key (id));

create table employees (id serial, emp_name varchar(255),
    primary key(id));