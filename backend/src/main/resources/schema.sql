create table if not exists customer
(
    id            bigint not null,
    customer_type varchar(255),
    first_name    varchar(255),
    last_name     varchar(255),
    primary key (id)
);

create table if not exists parking_record
(
    id            bigint not null,
    customer_id   bigint,
    customer_type varchar(255),
    status        varchar(255),
    begin_at      timestamp,
    end_by        timestamp,
    primary key (id)
);

create table if not exists invoice
(
    id            bigint not null,
    customer_id   bigint,
    amount          bigint,
    currency        varchar(255),
    created_at      timestamp,
    primary key (id)
);

create sequence customer_seq;
create sequence parking_record_seq;
create sequence invoice_seq;