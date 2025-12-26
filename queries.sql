use electronicsIMS;

set @name = 'Techsource ';
set @email = 'contact@techsource.com';
set @phone = '01011111111';
set @street = '10 tech st';
set @city = 'Cairo';
set @country = 'Egypt';
set @minordervalue = 5000;
set @currency = 'EGP';

insert into supplier
(name, email, phone, street, city, country, minordervalue, currency)
values
(@name, @email, @phone, @street, @city, @country, @minordervalue, @currency);

set @name = 'Globalelectro';
set @email = 'sales@globalelectro.com';
set @phone = '01022222222';
set @street = '22 rd';
set @city = 'Alexandria';
set @country = 'Egypt';
set @minordervalue = 3000;
set @currency = 'EGP';

insert into supplier
(name, email, phone, street, city, country, minordervalue, currency)
values
(@name, @email, @phone, @street, @city, @country, @minordervalue, @currency);


set @supplierid = 1;
select * from supplier where supplierid = @supplierid;

select * from supplier;

set @supplierid = 1;
set @newphone = '01099999999';

update supplier
set phone = @newphone
where supplierid = @supplierid;





-- product 1
set @name = 'laptop pro';
set @sku = 'ltp100';
set @desc = 'high performance laptop';
set @price = 25000;
set @vat = 14;
set @category = 'computers';


insert into product
(name, sku, description, price, vatrate, category)
values
(@name, @sku, @desc, @price, @vat, @category);

-- product 2
set @name = 'wireless mouse';
set @sku = 'wm200';
set @desc = 'wireless mouse';
set @price = 500;
set @vat = 14;
set @category = 'accessories';

insert into product
(name, sku, description, price, vatrate, category)
values
(@name, @sku, @desc, @price, @vat, @category);


set @productid = 1;
select * from product where productid = @productid;

set @category = 'accessories';
select * from product where category = @category;


set @productid = 1;
set @newprice = 27000;

update product
set price = @newprice
where productid = @productid;




-- admin user
set @username = 'admin';
set @email = 'admin@ims.com';
set @pass = 'admin';
set @role = 'admin';
set @supplierid = null;

insert into user (username, email, password, role, supplierid)
values (@username, @email, @pass, @role, @supplierid);

-- supplier user
set @username = 'rana';
set @email = 'contact@techsource.com';
set @pass = '123';
set @role = 'supplier';
set @supplierid = 1;

insert into user (username, email, password, role, supplierid)
values (@username, @email, @pass, @role, @supplierid);




set @supplierid = 1;
set @startdate = '2025-01-01';
set @enddate = '2026-01-01';
set @leadtimedays = 7;
set @terms = 'net 30 payment';

insert into contract
(supplierid, startdate, enddate, leadtimedays, terms)
values
(@supplierid, @startdate, @enddate, @leadtimedays, @terms);



set @productid = 1;
set @supplierid = 1;
set @unitcost = 20000;

insert into productsupplier
(productid, supplierid, unitcost)
values
(@productid, @supplierid, @unitcost);


set @productid = 1;

select s.name, ps.unitcost
from productsupplier ps
join supplier s on ps.supplierid = s.supplierid
where ps.productid = @productid;
 
 
 
 
set @name = 'main warehouse';
set @street = 'main rd';
set @city = 'cairo';
set @country = 'egypt';

insert into location
(name, street, city, country)
values
(@name, @street, @city, @country);


set @locationid = 1;
set @newcity = 'Giza';

update location
set city = @newcity
where locationid = @locationid;


set @productid = 1;
set @locationid = 1;
set @stock = 10;
set @reorder = 5;

insert into inventory
(productid, locationid, stocklevel, reorderpoint, lastupdated)
values
(@productid, @locationid, @stock, @reorder, now());


set @inventoryid = 1;
set @status = 'open';

insert into lowstockalert (inventoryid, status)
values (@inventoryid, @status);



set @supplierid = 1;
set @orderdate = '2025-12-01';
set @expecteddate = '2025-12-08';

insert into purchaseorder
(supplierid, orderdate, expecteddate)
values
(@supplierid, @orderdate, @expecteddate);


set @purchaseorderid = 1;
set @productid = 1;
set @quantity = 5;
set @unitcost = 20000;

insert into purchaseorderitem
(purchaseorderid, productid, quantity, unitcost)
values
(@purchaseorderid, @productid, @quantity, @unitcost);


set @orderdate = '2025-12-10';
set @customeremail = 'customer1@mail.com';

insert into salesorder
(orderdate, customer_email)
values
(@orderdate, @customeremail);


set @salesorderid = 1;
set @productid = 1;
set @quantity = 1;
set @price = 27000;

insert into salesorderitem
(salesorderid, productid, quantity, sellingprice)
values
(@salesorderid, @productid, @quantity, @price);




set @inventoryid = 1;
set @oldstock = 15;
set @newstock = 10;
set @changetype = 'sale';
set @changedby = 1;
set @reforder = 1;
set @notes = 'laptop sold';

insert into inventory_audit
(inventoryid, old_stocklevel, new_stocklevel, change_type, changed_by, reference_order_id, notes)
values
(@inventoryid, @oldstock, @newstock, @changetype, @changedby, @reforder, @notes);

