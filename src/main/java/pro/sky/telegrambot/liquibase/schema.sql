 create table notification_task(
    id bigserial primary key,
    chatId bigint,
    message varchar(200),
    time timestamp
 );


