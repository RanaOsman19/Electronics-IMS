create database if not exists electronicsIMS;
use electronicsIMS;


create table supplier (
    supplierid int auto_increment primary key,
    name varchar(100) not null,
    email varchar(100),
    phone varchar(20),
    address varchar(100),
    minordervalue float,
    currency varchar(10)
);

create table user (
    userid int auto_increment primary key,
    username varchar(50) not null,
    email varchar(100),
    password varchar(200),
    role enum('admin','supplier') not null,
    supplierid int default null,
    foreign key (supplierid) references supplier(supplierid)
        on delete cascade
        on update cascade
);

create table contract (
    contractid int auto_increment primary key,
    supplierid int not null,
    startdate date,
    enddate date,
    leadtimedays int,
    terms varchar(1000),
    foreign key (supplierid) references supplier(supplierid)
        on delete cascade
        on update cascade
);

create table product (
    productid int auto_increment primary key,
    name varchar(100) not null,
    SKU varchar(50) unique not null,
    description varchar(1000),
    price float not null,
    vatrate float
);

create table productsupplier (
    productid int not null,
    supplierid int not null,
    unitcost float,
    primary key (productid, supplierid),
    foreign key (productid) references product(productid)
        on delete cascade
        on update cascade,
    foreign key (supplierid) references supplier(supplierid)
        on delete cascade
        on update cascade
);

create table location (
    locationid int auto_increment primary key,
    name varchar(100),
    address varchar(100)
);

create table inventory (
    inventoryid int auto_increment primary key,
    productid int not null ,
    locationid int not null  ,
    unique(productid, locationid),
    stocklevel int not null default 0,
    reorderpoint int not null,
    lastupdated datetime ,
    foreign key (productid) references product(productid)
        on delete cascade
        on update cascade,
    foreign key (locationid) references location(locationid)
        on delete cascade
        on update cascade
);

create table lowstockalert (
    alertid int auto_increment primary key,
    inventoryid int not null,
    alertdate datetime ,
    status enum('open','resolved') default 'open',
    foreign key (inventoryid) references inventory(inventoryid)
        on delete cascade
        on update cascade
);

create table purchaseorder (
    purchaseorderid int auto_increment primary key,
    supplierid int not null,
    orderdate date not null,
    expecteddate date,
    receiveddate date,
    status enum('pending','shipped','received','cancelled') default 'pending',
    foreign key (supplierid) references supplier(supplierid)
        on delete restrict
        on update cascade
);

create table purchaseorderitem (
    purchaseorderid int not null,
    productid int not null,
    quantity int not null,
    unitcost float,
	receivedquantity int default 0,
    primary key (purchaseorderid, productid),
    foreign key (purchaseorderid) references purchaseorder(purchaseorderid)
        on delete cascade
        on update cascade,
    foreign key (productid) references product(productid)
        on delete restrict
        on update cascade
);

create table salesorder (
    salesorderid int auto_increment primary key,
    orderdate date not null,
    totalamount float
);

create table salesorderitem (
    salesorderid int not null,
    productid int not null,
    quantity int not null,
    sellingprice float,
    primary key (salesorderid, productid),
    foreign key (salesorderid) references salesorder(salesorderid)
        on delete cascade
        on update cascade,
    foreign key (productid) references product(productid)
        on delete restrict
        on update cascade
);

alter table product 
add column category varchar(50) ;

alter table salesorder 
add column customer_email varchar(100);

create table inventory_audit (
    auditid int auto_increment primary key,
    inventoryid int not null,
    old_stocklevel int,
    new_stocklevel int,
    change_type enum('purchase', 'sale', 'adjustment', 'return') not null,
    changed_by int,  
    change_date datetime ,
    reference_order_id int, 
    notes varchar(500),
	foreign key (inventoryid) references inventory(inventoryid)
        on delete cascade 
        on update cascade,
	foreign key (changed_by) references user(userid)
        on delete set null 
        on update cascade
);
