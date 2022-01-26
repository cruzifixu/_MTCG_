create table users
(
    id       serial
        constraint users_pk
            primary key,
    username varchar(250) not null,
    password varchar(50)  not null,
    coins    integer      not null,
    nickname varchar(250),
    bio      varchar(300),
    image    varchar(10),
    won      integer,
    lost     integer,
    elo      integer     default 100,
    status   varchar(50) default 'idle'::character varying
);

alter table users
    owner to swe1user;

create unique index users_username_uindex
    on users (username);

create table package
(
    package_num integer default nextval('packages_package_num_seq'::regclass) not null
        constraint packages_pk
            primary key,
    ownedby     varchar(255)
);

alter table package
    owner to swe1user;

create table card
(
    id           varchar(50) not null,
    name         varchar(50) not null,
    element_type varchar(50) not null,
    damage       integer     not null,
    ownedby      varchar(255),
    package_num  integer     not null,
    in_deck      boolean default false,
    for_trading  boolean default false
);

alter table card
    owner to swe1user;

create unique index cards_id_uindex
    on card (id);

create table trade
(
    id            varchar(100) not null
        constraint trading_pk
            primary key,
    card_to_trade varchar(50)  not null,
    type          varchar(20)  not null,
    min_damage    integer      not null
);

alter table trade
    owner to swe1user;

create unique index trading_id_uindex
    on trade (id);

create table transaction
(
    id          serial,
    username    varchar(250) not null,
    item_bought varchar(100) not null,
    time_stamp  timestamp    not null
);

alter table transaction
    owner to swe1user;

create unique index transaction_id_uindex
    on transaction (id);


