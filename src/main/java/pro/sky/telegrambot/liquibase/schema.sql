 create table notification_task(
    id bigserial primary key,
    text varchar,
    chatId bigint,
    dateTime timestamp
 );

