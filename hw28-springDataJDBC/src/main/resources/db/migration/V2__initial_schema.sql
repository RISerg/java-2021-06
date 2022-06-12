drop table if exists address cascade;
drop table if exists client cascade;
drop table if exists phone cascade;

create table address
(
    id        bigserial not null primary key,
    street    varchar(255),
    client_id int8 not null
);

create table phone
(
    id     bigserial not null primary key,
    number varchar(255)
);

create table client
(
    id       bigserial not null primary key,
    name     varchar(255),
    phone_id int8
);

alter table if exists address
    add constraint address_client_fk
        foreign key (client_id)
            references client;

alter table if exists client
    add constraint client_phone_fk
        foreign key (phone_id)
            references phone;