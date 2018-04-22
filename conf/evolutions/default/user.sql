CREATE TABLE IF NOT EXISTS user(
  id bigint not null auto_increment,
  name varchar(255) not null,
  email varchar(255) not null,
  primary key(id),
  unique key(email)
) ENGINE=InnoDB charset=UTF8;
