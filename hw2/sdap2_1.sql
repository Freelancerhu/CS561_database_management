create view v1 as
select sa.cust as CUSTOMER, sa.prod as PRODUCT, Avg(quant) as THE_AVG
from sales as sa
group by CUSTOMER, PRODUCT;

create view v2 as
select v1.CUSTOMER, v1.PRODUCT, Avg(sa.quant) as OTHER_PROD_AVG
from sales as sa, v1
where sa.cust = v1.CUSTOMER and sa.prod != v1.PRODUCT
group by v1.CUSTOMER, v1.PRODUCT;

create view v3 as
select v1.CUSTOMER, v1.PRODUCT, Avg(sa.quant) as OTHER_CUST_AVG
from sales as sa, v1
where sa.cust != v1.CUSTOMER and sa.prod = v1.PRODUCT
group by v1.CUSTOMER, v1.PRODUCT;

select *
from (v1 natural full outer join v2 natural full outer join v3)
order by customer, product