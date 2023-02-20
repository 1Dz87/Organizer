create index on friend_group(user_id);

alter table archive add CONSTRAINT fk_account_id
             FOREIGN KEY(account_id)
       	  REFERENCES account(id);

create index on archive(account_id);