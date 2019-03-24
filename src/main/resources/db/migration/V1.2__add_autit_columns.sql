alter table users add column created TIMESTAMP;
alter table users add column created_by varchar;
alter table users add column last_modified TIMESTAMP;
alter table users add column last_modified_by varchar;

alter table items add column created TIMESTAMP;
alter table items add column created_by varchar;
alter table items add column last_modified TIMESTAMP;
alter table items add column last_modified_by varchar;
