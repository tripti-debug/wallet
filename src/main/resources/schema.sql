CREATE TABLE IF NOT EXISTS bank_account(
    Id SERIAL primary key,
    bank_id varchar(50),
    ba_number varchar(50));

CREATE TABLE IF NOT EXISTS elec_address(
    identifier varchar(100) primary key,
    verified varchar(50),
    otp int,
    typeOf varchar(50),
    ssn varchar(50));


CREATE TABLE IF NOT EXISTS user_account(
    ssn varchar(50) primary key,
    firstname varchar(50),
    lastname varchar(50),
    phoneno varchar(50),
    email varchar(50),
    balance numeric,
    bankId varchar(50),
    baNumber varchar(50),
    bank_balance numeric ,
    verification_amount numeric,
    pbaVerified varchar(10),
    password varchar(50),
    confirmed varchar(50),
    foreign key(phoneno) references elec_address(identifier));

CREATE TABLE IF NOT EXISTS has_additional(
    Id SERIAL primary key,
    ssn varchar(50),
    bankId varchar(50),
    baNumber varchar(50),
    bank_balance numeric,
    amount_deducted numeric,
    verified varchar(50),
    foreign key(Id) references bank_account(Id));

CREATE TABLE IF NOT EXISTS send_transaction(
    stid SERIAL primary key,
    dateortime timestamptz,
    memo varchar(50),
    cancel_reason varchar(50),
    identifier varchar(100),
    ssn varchar(50),
    amount numeric,
    total_amount numeric,
    status varchar(10),
    foreign key(ssn) references user_account(ssn),
    foreign key(identifier) references elec_address(identifier));

CREATE TABLE IF NOT EXISTS request_transaction(
    rtid SERIAL primary key,
    dateortime timestamptz,
    memo varchar(50),
    identifier varchar(100),
    ssn varchar (50),
    amount numeric,
    total_amount numeric,
    status varchar(10),
    foreign key(ssn) references user_account(ssn));

CREATE TABLE IF NOT EXISTS email(
    emailAdd varchar(50) primary key,
    ssn varchar(50),
    foreign key(ssn) references user_account(ssn),
    foreign key(emailAdd) references elec_address(identifier));

CREATE TABLE IF NOT EXISTS fromm(
    rtid SERIAL,
    identifier varchar(100),
    percentage numeric,
    primary key(rtid,identifier),
    foreign key(rtid) references request_transaction(rtid),
    foreign key(identifier) references elec_address(identifier));

CREATE TABLE IF NOT EXISTS receive(
    tid SERIAL primary key ,
    ttid int,
    send_from_ssn varchar(50),
    receive_to_identifier varchar(100),
    request_from_identifier varchar(50),
    receive_to_ssn varchar(100),
    dateortime varchar(50),
    memo varchar(50),
    amount numeric,
    total_amount numeric);