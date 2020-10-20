/* Database Initialization Script: */
USE MODERN_WAITER_DB;

create table options
(
    id   int          not null,
    name varchar(250) not null,
    cost double       not null,
    constraint options_id_uindex
        unique (id)
);

alter table options
    add primary key (id);

create table restaurant
(
    id                     int          not null,
    name                   varchar(250) not null,
    location               varchar(250) not null,
    tax_percentage         double       not null,
    service_fee_percentage double       not null,
    constraint restaurant_id_uindex
        unique (id)
);

alter table restaurant
    add primary key (id);

create table items
(
    id               int           not null,
    restaurant_id    int           not null,
    name             varchar(250)  not null,
    type             varchar(250)  not null,
    cost             double        not null,
    description      varchar(1000) not null,
    calories         double        not null,
    popularity_count int           not null,
    image            varchar(1000) not null,
    constraint items_id_uindex
        unique (id),
    constraint items_restaurant_id_fk
        foreign key (restaurant_id) references restaurant (id)
);

alter table items
    add primary key (id);

create table items_options
(
    id         int not null;
    items_id   int not null,
    options_id int not null,
    constraint items_options_items_id_fk
        foreign key (items_id) references items (id),
    constraint items_options_options_id_fk
        foreign key (options_id) references options (id)
);

create table tables
(
    id           int not null,
    table_number int not null,
    constraint tables_id_uindex
        unique (id)
);

alter table tables
    add primary key (id);

create table users
(
    id         int          not null,
    username   varchar(250) not null,
    email      varchar(250) not null,
    created_at datetime     not null,
    constraint users_email_uindex
        unique (email),
    constraint users_id_uindex
        unique (id),
    constraint users_username_uindex
        unique (username)
);

alter table users
    add primary key (id);

create table orders
(
    id                int                  not null,
    tables_id         int                  not null,
    users_id          int                  not null,
    restaurant_id     int                  not null,
    amount            double               not null,
    ordered_at        datetime             not null,
    has_paid          tinyint(1) default 0 not null,
    is_active_session tinyint(1) default 0 not null,
    constraint orders_id_uindex
        unique (id),
    constraint orders_tables_id_fk
        foreign key (tables_id) references tables (id),
    constraint orders_users_id_fk
        foreign key (users_id) references users (id)
);

alter table orders
    add primary key (id);

create table ordered_items
(
    id        int not null,
    orders_id int not null,
    items_id  int not null,
    has_paid          tinyint(1) default 0 not null,
    is_selected          tinyint(1) default 0 not null,
    constraint ordered_items_id_uindex
        unique (id),
    constraint ordered_items_orders_id_fk
        foreign key (orders_id) references orders (id)
);

alter table ordered_items
    add primary key (id);

