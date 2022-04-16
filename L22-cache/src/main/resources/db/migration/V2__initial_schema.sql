drop table if exists address cascade;
drop table if exists client cascade;
drop table if exists phone cascade;

create table address
(
    id        int8 not null,
    street    varchar(255),
    client_id int8 not null,
    primary key (id)
);

create table phone
(
    id     int8 not null,
    number varchar(255),
    primary key (id)
);

create table client
(
    id       int8 not null,
    name     varchar(255),
    phone_id int8,
    primary key (id)
);

alter table if exists address
    add constraint address_client_fk
        foreign key (client_id)
            references client;

alter table if exists client
    add constraint client_phone_fk
        foreign key (phone_id)
            references phone;