create or replace view product_details as
select
    p.productid,
    p.name as product_name,
    p.sku,
    p.category,
    p.price,
    p.vatrate
from product p;


create or replace view supplier_profile as
select
    s.supplierid,
    s.name,
    s.email,
    s.phone,
    s.street,
    s.city,
    s.country,
    s.minordervalue,
    s.currency
from supplier s;


create or replace view product_supplier_cost as
select
    p.productid,
    p.name as product_name,
    s.supplierid,
    s.name as supplier_name,
    ps.unitcost
from productsupplier ps
join product p  on ps.productid = p.productid
join supplier s on ps.supplierid = s.supplierid;


create or replace view inventory_status as
select
    i.inventoryid,
    p.productid,
    p.name as product_name,
    l.locationid,
    l.name as location_name,
    i.stocklevel,
    i.reorderpoint,
    case
        when i.stocklevel <= i.reorderpoint then 'low'
        else 'ok'
    end as stock_status,
    i.lastupdated
from inventory i
join product p  on i.productid = p.productid
join location l on i.locationid = l.locationid;


create or replace view low_stock_alerts as
select 
    i.inventoryid,
    p.name as product,
    l.name as location,
    i.stocklevel,
    i.reorderpoint
from inventory i
join product p on i.productid = p.productid
join location l on i.locationid = l.locationid
where i.stocklevel <= i.reorderpoint;



create or replace view purchase_order_summary as
select
    po.purchaseorderid,
    s.name as supplier_name,
    po.orderdate,
    po.expecteddate,
    po.receiveddate,
    po.status
from purchaseorder po
join supplier s on po.supplierid = s.supplierid;


create or replace view purchase_order_items as
select
    poi.purchaseorderid,
    p.name as product_name,
    poi.quantity,
    poi.unitcost,
    (poi.quantity * poi.unitcost) as line_total
from purchaseorderitem poi
join product p on poi.productid = p.productid;


create or replace view sales_order_summary as
select
    so.salesorderid,
    so.orderdate,
    so.customer_email,
    sum(soi.quantity * soi.sellingprice) as order_total
from salesorder so
join salesorderitem soi on so.salesorderid = soi.salesorderid
group by
    so.salesorderid,
    so.orderdate,
    so.customer_email;


create or replace view sales_order_items as
select
    soi.salesorderid,
    p.name as product_name,
    soi.quantity,
    soi.sellingprice,
    (soi.quantity * soi.sellingprice) as total
from salesorderitem soi
join product p on soi.productid = p.productid;


create or replace view inventory_history as
select
    ia.auditid,
    p.name as product_name,
    l.name as location_name,
    ia.old_stocklevel,
    ia.new_stocklevel,
    ia.change_type,
    ia.change_date,
    ia.notes
from inventory_audit ia
join inventory i on ia.inventoryid = i.inventoryid
join product p   on i.productid = p.productid
join location l  on i.locationid = l.locationid;


create or replace view product_stock as
select
    p.productid,
    p.name as product_name,
    sum(i.stocklevel) as total_stock
from product p
left join inventory i on p.productid = i.productid
group by
    p.productid,
    p.name;

create or replace view supplier_spend as
select
    s.supplierid,
    s.name as supplier_name,
    sum(poi.quantity * poi.unitcost) as total_spend
from supplier s
join purchaseorder po     on s.supplierid = po.supplierid
join purchaseorderitem poi on po.purchaseorderid = poi.purchaseorderid
group by
    s.supplierid,
    s.name;
