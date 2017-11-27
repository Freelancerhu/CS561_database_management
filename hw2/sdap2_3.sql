create view vq1 as
select sa.cust as CUSTOMER, sa.prod as PRODUCT, Avg(sa.quant) as QAvg, Min(sa.quant) as QMin
from sales as sa
where sa.month >= 1 and sa.month <= 3
group by CUSTOMER, PRODUCT;

create view vq2 as
select sa.cust as CUSTOMER, sa.prod as PRODUCT, Avg(sa.quant) as QAvg, Min(sa.quant) as QMin
from sales as sa
where sa.month >= 4 and sa.month <= 6
group by CUSTOMER, PRODUCT;

create view vq3 as
select sa.cust as CUSTOMER, sa.prod as PRODUCT, Avg(sa.quant) as QAvg, Min(sa.quant) as QMin
from sales as sa
where sa.month >= 7 and sa.month <= 9
group by CUSTOMER, PRODUCT;

create view vq4 as
select sa.cust as CUSTOMER, sa.prod as PRODUCT, Avg(sa.quant) as QAvg, Min(sa.quant) as QMin
from sales as sa
where sa.month >= 10 and sa.month <= 12
group by CUSTOMER, PRODUCT;



create view vu1 as
  select cust CUSTOMER, prod PRODUCT, quant
  from sales
  where month between 1 and 3;

create view vu2 as
  select cust CUSTOMER, prod PRODUCT, quant
  from sales
  where month between 4 and 6;

create view vu3 as
  select cust CUSTOMER, prod PRODUCT, quant
  from sales
  where month between 7 and 9;

create view vu4 as
  select cust CUSTOMER, prod PRODUCT, quant
  from sales
  where month between 10 and 12;
  
  
  

create view vq1_a as 
  select vq1.CUSTOMER, vq1.PRODUCT, count(vu2.quant) as AFTER_TOT
  from vq1 natural join vu2
  where vu2.quant between vq1.QMin and vq1.QAvg
  group by vq1.CUSTOMER, vq1.PRODUCT;

create view vq2_b as 
  select vq2.CUSTOMER, vq2.PRODUCT, count(vu1.quant) as BEFORE_TOT
  from vq2 natural join vu1  
  where vu1.quant between vq2.QMin and vq2.QAvg 
  group by vq2.CUSTOMER, vq2.PRODUCT;

create view vq2_a as
  select vq2.CUSTOMER, vq2.PRODUCT, count(vu3.quant) as AFTER_TOT
  from vq2 natural join vu3
  where vu3.quant between vq2.QMin and vq2.QAvg
  group by vq2.CUSTOMER, vq2.PRODUCT;

create view vq3_b as 
  select vq3.CUSTOMER, vq3.PRODUCT, count(vu2.quant) as BEFORE_TOT
  from vq3 natural join vu2 
  where vu2.quant between vq3.QMin and vq3.QAvg 
  group by vq3.CUSTOMER, vq3.PRODUCT;

create view vq3_a as 
  select vq3.CUSTOMER, vq3.PRODUCT, count(vu4.quant) as AFTER_TOT
  from vq3 natural join vu4
  where vu4.quant between vq3.QMin and vq3.QAvg
  group by vq3.CUSTOMER, vq3.PRODUCT;

create view vq4_a as 
  select vq4.CUSTOMER, vq4.PRODUCT, count(vu3.quant) as BEFORE_TOT
  from vq4 natural join vu3 
  where vu3.quant between vq4.QMin and vq4.QAvg
  group by vq4.CUSTOMER, vq4.PRODUCT;



create or replace view vr1 as
  select CUSTOMER as CUST, PRODUCT as PROD, 'Q1' as QUARTER, NULL as BEFORE_TOT, AFTER_TOT
  from vq1 natural full outer join vq1_a;

create or replace view vr2 as
  select vq2.CUSTOMER as CUST, PRODUCT as PROD, 'Q2' as QUARTER, BEFORE_TOT, AFTER_TOT
  from vq2 natural full outer join vq2_b natural full outer join vq2_a;

create or replace view vr3 as
  select CUSTOMER as CUST, PRODUCT as PROD, 'Q3' as QUARTER, BEFORE_TOT, AFTER_TOT
  from vq3 natural full outer join vq3_b natural full outer join vq3_a;

create or replace view vr4 as
  select CUSTOMER as CUST, PRODUCT as PROD, 'Q4' as QUARTER, BEFORE_TOT, NULL as AFTER_TOT
  from vq4 natural full outer join vq4_a;




select * from vr1 union
select * from vr2 union
select * from vr3 union
select * from vr4
order by cust, prod