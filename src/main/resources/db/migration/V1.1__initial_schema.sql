create table users (
  id uuid not null,
  name varchar not null,

  primary key (id)
);

create table items (
  id uuid not null,
  user_id uuid not null,
  name varchar not null,
  description varchar,

  primary key (id),
  foreign key (user_id) references users(id)
);