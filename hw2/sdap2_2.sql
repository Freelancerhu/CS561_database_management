create view vq1 as
select sa.cust as CUSTOMER, sa.prod as PRODUCT, Avg(sa.quant) as Q1
from sales as sa
where sa.month >= 1 and sa.month <= 3
group by CUSTOMER, PRODUCT;

create view vq2 as
select sa.cust as CUSTOMER, sa.prod as PRODUCT, Avg(sa.quant) as Q2
from sales as sa
where sa.month >= 4 and sa.month <= 6
group by CUSTOMER, PRODUCT;

create view vq3 as
select sa.cust as CUSTOMER, sa.prod as PRODUCT, Avg(sa.quant) as Q3
from sales as sa
where sa.month >= 7 and sa.month <= 9
group by CUSTOMER, PRODUCT;

create view vq4 as
select sa.cust as CUSTOMER, sa.prod as PRODUCT, Avg(sa.quant) as Q4
from sales as sa
where sa.month >= 10 and sa.month <= 12
group by CUSTOMER, PRODUCT;




create view vr1 as
select CUSTOMER, PRODUCT, 'Q1' as QUARTER, NULL as BEFORE_AVG, vq2.Q2 as AFTER_AVG
from vq1 natural full outer join vq2;

create view vr2 as
select CUSTOMER, PRODUCT, 'Q2' as QUARTER, vq1.Q1 as BEFORE_AVG, vq3.Q3 as AFTER_AVG
from vq2 natural full outer join vq1 natural full outer join vq3;

create view vr3 as
select CUSTOMER, PRODUCT, 'Q3' as QUARTER, vq2.Q2 as BEFORE_AVG, vq4.Q4 as AFTER_AVG
from vq3 natural full outer join vq2 natural full outer join vq4;

create view vr4 as
select CUSTOMER, PRODUCT, 'Q4' as QUARTER, vq3.Q3 as BEFORE_AVG, NULL as AFTER_AVG
from vq4 natural full outer join vq3;


select *
from vr1 
union
select *
from vr2 
union
select * 
from vr3 
union
select *
from vr4
order by CUSTOMER, PRODUCT

