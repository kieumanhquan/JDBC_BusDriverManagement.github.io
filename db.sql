create table driver(
	id number primary key,
	name nvarchar2(30) not null,
	address nvarchar2(50) not null,
	phone_number nvarchar2(10) not null,
	driver_level nvarchar2(2) not null
);


create table bus_line(
	id number primary key,
	distance number not null,
	bus_stop_number number not null
);


create table driving_assignment(
	driver_id number not null,
	bus_line_id number not null,
	round_trip_number number not null,
	constraint driving_assignment_PK primary key (driver_id, bus_line_id)
);