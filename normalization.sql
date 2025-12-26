use electronicsIMS;

-- 1st norm form
-- address is composite attribute
alter table supplier 
drop column address,
add column street varchar(10), add column city varchar(20), add column country varchar(20) ;

alter table location 
drop column address,
add column street varchar(10), add column city varchar(20), add column country varchar(20) ;

-- 2nd norm form 
-- productsupplier	(productid, supplierid)	unitcost	
-- purchaseorderitem	(purchaseorderid, productid)	quantity, unitcost, receivedquantity
-- salesorderitem	(salesorderid, productid)	quantity, sellingprice	

-- 3rd norm form 
-- totalamount depends on salesorderitem (derived from quantity × sellingprice)
alter table salesorder drop column totalamount;




