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
    in p_phone varchar(20),
    in p_minordervalue float
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    update supplier
    set email = p_email, phone = p_phone, minordervalue=p_minordervalue
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
 
 
-- delimiter $$

-- create procedure create_purchase_order(
--     in p_userid int,
--     in p_supplierid int,
--     in p_expecteddate date
-- )
-- begin
--     if (select role from user where userid = p_userid) <> 'admin' then
--         signal sqlstate '45000' set message_text = 'admin only';
--     end if;

--     insert into purchaseorder (supplierid, orderdate, expecteddate)
--     values (p_supplierid, curdate(), p_expecteddate);
-- end$$
-- delimiter ;
 
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


delimiter $$
create procedure delete_inventory(
    in p_userid int,
    in p_inventoryid int
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    delete from inventory
    where inventoryid = p_inventoryid;
end$$
delimiter ;

delimiter $$
create procedure update_inventory_stock(
    in p_userid int,
    in p_inventoryid int,
    in p_newstock int
)
begin
    declare old_stock int;

    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    select stocklevel into old_stock
    from inventory
    where inventoryid = p_inventoryid;

    update inventory
    set stocklevel = p_newstock,
        lastupdated = now()
    where inventoryid = p_inventoryid;

    insert into inventory_audit
    (inventoryid, old_stocklevel, new_stocklevel, change_type, changed_by)
    values
    (p_inventoryid, old_stock, p_newstock, 'adjustment', p_userid);
end$$
delimiter ;


delimiter $$
create procedure delete_product(
    in p_userid int,
    in p_productid int
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    delete from product where productid = p_productid;
end$$
delimiter ;

delimiter $$

create procedure create_purchase_order (
    in p_user_id int,
    in p_supplier_id int,
    in p_order_date date
)
begin
    if p_supplier_id is null
       or not exists (
            select 1 from supplier where supplierid = p_supplier_id
       ) then
        signal sqlstate '45000'
        set message_text = 'invalid supplier selected';
    end if;

    if p_order_date is null
       or p_order_date < curdate() then
        signal sqlstate '45000'
        set message_text = 'order date must be today or later';
    end if;

    insert into purchase_order (
        supplierid,
        createdby,
        orderdate,
        status
    )
    values (
        p_supplier_id,
        p_user_id,
        p_order_date,
        'created'
    );
end$$

delimiter ;

delimiter $$
create procedure update_inventory_reorderpoint(
    in p_userid int,
    in p_inventoryid int,
    in p_newreorderpoint int
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    update inventory
    set reorderpoint = p_newreorderpoint
    where inventoryid = p_inventoryid;
end$$
delimiter ;

delimiter $$
create procedure update_purchase_order_status(
    in p_userid int,
    in p_purchaseorderid int,
    in p_newstatus enum('pending','shipped','received','cancelled')
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    update purchaseorder
    set status = p_newstatus
    where purchaseorderid = p_purchaseorderid;
end$$
delimiter ;
 
 delimiter $$
create procedure delete_purchase_order(
    in p_userid int,
    in p_purchaseorderid int
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    delete from purchaseorder where purchaseorderid = p_purchaseorderid;
end$$
delimiter ;
 delimiter $$
create procedure resolve_low_stock_alert(
    in p_userid int,
    in p_alertid int
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    update lowstockalert
    set status = 'resolved'
    where alertid = p_alertid;
end$$
delimiter ;

delimiter $$
create procedure update_product_details(
    in p_userid int,
    in p_productid int,
    in p_name varchar(100),
    in p_sku varchar(50),
    in p_description varchar(1000),
    in p_vatrate float,
    in p_category varchar(50)
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    update product
    set name = p_name,
        sku = p_sku,
        description = p_description,
        vatrate = p_vatrate,
        category = p_category
    where productid = p_productid;
end$$
delimiter ;
 delimiter $$
create procedure delete_sales_order(
    in p_userid int,
    in p_salesorderid int
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    delete from salesorder where salesorderid = p_salesorderid;
end$$
delimiter ;
 delimiter $$
create procedure add_product_supplier(
    in p_userid int,
    in p_productid int,
    in p_supplierid int,
    in p_unitcost float
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    insert into productsupplier(productid, supplierid, unitcost)
    values (p_productid, p_supplierid, p_unitcost);
end$$
delimiter ;

delimiter $$
create procedure update_product_supplier_cost(
    in p_userid int,
    in p_productid int,
    in p_supplierid int,
    in p_unitcost float
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    update productsupplier
    set unitcost = p_unitcost
    where productid = p_productid and supplierid = p_supplierid;
end$$
delimiter ;

delimiter $$
create procedure delete_product_supplier(
    in p_userid int,
    in p_productid int,
    in p_supplierid int
)
begin
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000' set message_text = 'admin only';
    end if;

    delete from productsupplier
    where productid = p_productid and supplierid = p_supplierid;
end$$
delimiter ;


delimiter $$

create procedure create_sales_order(
    in p_userid int,
    in p_customer_email varchar(100),
    in p_items json
)
begin
    declare v_salesorderid int;
    declare i int default 0;
    declare v_count int;

    declare v_inventoryid int;
    declare v_productid int;
    declare v_qty int;
    declare v_price float;
    declare old_stock int;

    -- admin check
    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000'
        set message_text = 'admin only';
    end if;

    set v_count = json_length(p_items);

    if v_count = 0 then
        signal sqlstate '45000'
        set message_text = 'no items provided';
    end if;

    start transaction;

    insert into salesorder (orderdate, customer_email)
    values (curdate(), p_customer_email);

    set v_salesorderid = last_insert_id();

    while i < v_count do

        set v_inventoryid = json_extract(p_items, concat('$[', i, '].inventoryid'));
        set v_productid   = json_extract(p_items, concat('$[', i, '].productid'));
        set v_qty         = json_extract(p_items, concat('$[', i, '].qty'));
        set v_price       = json_extract(p_items, concat('$[', i, '].price'));

        if v_qty <= 0 then
            rollback;
            signal sqlstate '45000'
            set message_text = 'invalid quantity';
        end if;

        select stocklevel
        into old_stock
        from inventory
        where inventoryid = v_inventoryid
        for update;

        if old_stock < v_qty then
            rollback;
            signal sqlstate '45000'
            set message_text = 'insufficient stock';
        end if;

        insert into salesorderitem
        (salesorderid, productid, quantity, sellingprice)
        values
        (v_salesorderid, v_productid, v_qty, v_price);

        update inventory
        set stocklevel = stocklevel - v_qty,
            lastupdated = now()
        where inventoryid = v_inventoryid;

        insert into inventory_audit
        (inventoryid, old_stocklevel, new_stocklevel,
         change_type, changed_by, change_date, reference_order_id)
        values
        (v_inventoryid, old_stock, old_stock - v_qty,
         'sale', p_userid, now(), v_salesorderid);

        set i = i + 1;
    end while;

    commit;
end$$
delimiter ;

 delimiter $$

create procedure create_purchase_order(
    in p_userid int,
    in p_supplierid int,
    in p_items json
)
begin
    declare v_poid int;
    declare i int default 0;
    declare v_count int;

    declare v_inventoryid int;
    declare v_productid int;
    declare v_qty int;
    declare v_cost float;
    declare old_stock int;

    if (select role from user where userid = p_userid) <> 'admin' then
        signal sqlstate '45000'
        set message_text = 'admin only';
    end if;

    set v_count = json_length(p_items);

    if v_count = 0 then
        signal sqlstate '45000'
        set message_text = 'no items provided';
    end if;

    start transaction;

    insert into purchaseorder (supplierid, orderdate)
    values (p_supplierid, curdate());

    set v_poid = last_insert_id();

    while i < v_count do

        set v_inventoryid = json_extract(p_items, concat('$[', i, '].inventoryid'));
        set v_productid   = json_extract(p_items, concat('$[', i, '].productid'));
        set v_qty         = json_extract(p_items, concat('$[', i, '].qty'));
        set v_cost        = json_extract(p_items, concat('$[', i, '].cost'));

        if v_qty <= 0 then
            rollback;
            signal sqlstate '45000'
            set message_text = 'invalid quantity';
        end if;

        select stocklevel into old_stock
        from inventory
        where inventoryid = v_inventoryid
        for update;

        insert into purchaseorderitem
        (purchaseorderid, productid, quantity, unitcost)
        values
        (v_poid, v_productid, v_qty, v_cost);

        update inventory
        set stocklevel = stocklevel + v_qty,
            lastupdated = now()
        where inventoryid = v_inventoryid;

        insert into inventory_audit
        (inventoryid, old_stocklevel, new_stocklevel,
         change_type, changed_by, change_date, reference_order_id)
        values
        (v_inventoryid, old_stock, old_stock + v_qty,
         'purchase', p_userid, now(), v_poid);

        set i = i + 1;
    end while;

    commit;
end$$
delimiter ;

