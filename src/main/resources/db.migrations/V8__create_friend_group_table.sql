create table friend_group (
    id bigserial primary key,
    name varchar(255) not null,
    user_id bigint references org_user(id) not null
);

-- create sequence friend_group_id_seq;

truncate friend restart identity cascade;

alter table friend add column friend_group bigint references friend_group(id) not null;