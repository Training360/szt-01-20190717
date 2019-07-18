--create table log_entries (id bigint auto_increment, message varchar(255),
--      constraint pk_employee primary key (id));

create table log_entries (id serial, message varchar(255),
    primary key(id));