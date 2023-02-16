create table friend_group (
    id bigserial primary key,
    name varchar(255) not null,
    user_id bigint references user(id) not null
);

create sequence friend_group_id_seq;

alter table friend add column friend_group bigint references friend_group(id) not null;