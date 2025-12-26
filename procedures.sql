use electronicsIMS;

delimiter $$
create procedure login_user (
    in p_username varchar(50),
    in p_password varchar(200)
)
begin
    select userid, role, supplierid
    from user
    where username = p_username
      and password = p_password;
end $$
delimiter ;


delimiter $$
create procedure add_supplier(
    in userid_ int,
    in name_ varchar(100),
    in email_ varchar(100),
    in phone_ varchar(20),
    in street_ varchar(100),
    in city_ varchar(50),
    in country_ varchar(50),
    in minordervalue_ float,
    in currency_ varchar(10)
)
begin
    if (select role from user where userid = userid_) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    insert into supplier
    (name, email, phone, street, city, country, minordervalue, currency)
    values
    (name_, email_, phone_, street_, city_, country_, minordervalue_ , currency_);
end$$
delimiter ;


delimiter $$

create procedure update_supplier(
    in p_userid int,
    in p_supplierid int,
    in p_email varchar(100),
    in p_phone varchar(20)
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    update supplier
    set email = p_email, phone = p_phone
    where supplierid = p_supplierid;
end$$
delimiter ;


delimiter $$

create procedure delete_supplier(
    in p_userid int,
    in p_supplierid int
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    delete from supplier where supplierid = p_supplierid;
end$$
delimiter ;


delimiter $$

create procedure add_product(
    in p_userid int,
    in p_name varchar(100),
    in p_sku varchar(50),
    in p_description varchar(1000),
    in p_price float,
    in p_vatrate float,
    in p_category varchar(50)
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    insert into product
    (name, sku, description, price, vatrate, category)
    values
    (p_name, p_sku, p_description, p_price, p_vatrate, p_category);
end$$
delimiter ;

delimiter $$

create procedure update_product_price(
    in p_userid int,
    in p_productid int,
    in p_price float
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    update product set price = p_price
    where productid = p_productid;
end$$
delimiter ;
 
 
delimiter $$

create procedure add_inventory(
    in p_userid int,
    in p_productid int,
    in p_locationid int,
    in p_reorderpoint int
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    insert into inventory
    (productid, locationid, reorderpoint, stocklevel, lastupdated)
    values
    (p_productid, p_locationid, p_reorderpoint, 0, now());
end$$
delimiter ;
 
 
delimiter $$

create procedure create_purchase_order(
    in p_userid int,
    in p_supplierid int,
    in p_expecteddate date
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    insert into purchaseorder (supplierid, orderdate, expecteddate)
    values (p_supplierid, curdate(), p_expecteddate);
end$$
delimiter ;
 
delimiter $$

create procedure add_po_item(
    in p_userid int,
    in p_purchaseorderid int,
    in p_productid int,
    in p_quantity int,
    in p_unitcost float
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    insert into purchaseorderitem
    values (p_purchaseorderid, p_productid, p_quantity, p_unitcost, 0);
end$$
delimiter ;
 
delimiter $$

create procedure receive_purchase(
    in p_userid int,
    in p_inventoryid int,
    in p_purchaseorderid int,
    in p_productid int,
    in p_qty int
)
begin
    declare old_stock int;

    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    select stocklevel into old_stock
    from inventory where inventoryid = p_inventoryid;

    update inventory
    set stocklevel = stocklevel + p_qty,
        lastupdated = now()
    where inventoryid = p_inventoryid;

    update purchaseorderitem
    set receivedquantity = receivedquantity + p_qty
    where purchaseorderid = p_purchaseorderid
      and productid = p_productid;

    insert into inventory_audit
    (inventoryid, old_stocklevel, new_stocklevel, change_type, changed_by)
    values
    (p_inventoryid, old_stock, old_stock + p_qty, 'purchase', p_userid);
end$$
delimiter ;
 
 
delimiter $$

create procedure sales_order(
    in p_userid int,
    in p_customer_email varchar(100)
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    insert into salesorder (orderdate, customer_email)
    values (curdate(), p_customer_email);
end$$
delimiter ;
 
delimiter $$

delimiter $$

create procedure sell_product(
    in p_userid int,
    in p_salesorderid int,
    in p_inventoryid int,
    in p_productid int,
    in p_qty int,
    in p_price float
)
begin
    declare old_stock int;

    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    if p_qty <= 0 then
        signal sqlstate '45000' set message_text = 'invalid quantity';
    end if;

    start transaction;

    select stocklevel
    into old_stock
    from inventory
    where inventoryid = p_inventoryid
    for update;

    if old_stock < p_qty then
        rollback;
        signal sqlstate '45000'
        set message_text = 'insufficient stock';
    end if;

    insert into salesorderitem
    (salesorderid, productid, quantity, sellingprice)
    values
    (p_salesorderid, p_productid, p_qty, p_price);

    update inventory
    set stocklevel = stocklevel - p_qty,
        lastupdated = now()
    where inventoryid = p_inventoryid;

    insert into inventory_audit
    (inventoryid, old_stocklevel, new_stocklevel,
     change_type, changed_by, change_date, reference_order_id)
    values
    (p_inventoryid, old_stock, old_stock - p_qty,
     'sale', p_userid, now(), p_salesorderid);

    commit;
end$$

delimiter ;

 
 
delimiter $$

create procedure ship_order(
    in p_userid int,
    in p_purchaseorderid int
)
begin
    if (select role from user where userid = p_userid) <> 'supplier' then
        signal sqlstate '45000' set message_text = 'supplier only';
    end if;

    update purchaseorder
    set status = 'shipped'
    where purchaseorderid = p_purchaseorderid
      and supplierid = (select supplierid from user where userid = p_userid);
end$$
delimiter ;
