create table if not exists users (
                                         id bigserial not null,
                                         user_name varchar not null,
                                         primary key (id)
    );

insert into users (user_name) values ('Aliaksei');
insert into users (user_name) values ('Masha');
insert into users (user_name) values ('Petya');
