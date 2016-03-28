CREATE TABLE TIMELINE_ITEMS (
    id serial primary key,
    user_id varchar(50) not null,
    service_id varchar(50) not null,
    json text not null,
    created_time timestamp not null default current_timestamp
)