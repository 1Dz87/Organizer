CREATE TABLE organizer.contacts (
    id bigserial primary key,
    address character varying(255)
);

CREATE TABLE organizer.contacts_email (
    contacts_id bigserial references contacts(id),
    email character varying(255)
);

CREATE TABLE organizer.contacts_messengers (
    contacts_id bigserial references contacts(id),
    messengers character varying(255)
);

CREATE TABLE organizer.contacts_phones (
    contacts_id bigserial references contacts(id),
    phones character varying(255)
);

CREATE TABLE organizer.archive (
    id bigserial primary key,
    date_from date,
    income real,
    spend real,
    til date,
    account_id bigint
);

CREATE TABLE organizer.org_user (
    id bigserial primary key,
    birth_day date,
    login character varying(255),
    name character varying(255),
    password character varying(255),
    contacts_id bigint references contacts(id),
    uuid uuid
);

CREATE TABLE organizer.account (
    id bigserial primary key,
    ammount real,
    currency character varying(255),
    name character varying(255),
    user_id bigint references org_user(id)
);

CREATE TABLE organizer.authority (
    id bigserial primary key,
    authority character varying(255),
    org_user bigint references org_user(id)
);

CREATE TABLE organizer.friend (
    id bigserial primary key,
    birthday timestamp without time zone,
    name character varying(255),
    contacts_id bigint references contacts(id),
    user_id bigint references org_user(id),
    uuid uuid
);

CREATE TABLE organizer.transaction (
    id bigserial primary key,
    amount real,
    date_time timestamp without time zone,
    friend_id bigint,
    source_account bigint references account(id),
    target_account bigint references account(id)
);