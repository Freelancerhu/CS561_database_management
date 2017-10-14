create view v1 as
    select s.cust, s.prod, s.quant as JAN_MAX, s.month as JAN_MONTH, s.day as JAN_DAY, s.year as JAN_YEAR
    from sales s, (select s.cust, s.prod, max(s.quant)
                        from sales s
                        where s.month = 1 and s.year >= 2000 and s.year <= 2005
                        group by s.cust, s.prod) as se
    where s.cust = se.cust and s.prod = se.prod and s.quant = se.max;

create view v2 as
    select s.cust, s.prod, s.quant as FEB_MIN, s.month as FEB_MONTH, s.day as FEB_DAY, s.year as FEB_YEAR
    from sales s, (select s.cust, s.prod, min(s.quant)
                        from sales s
                        where s.month = 2
                        group by s.cust, s.prod) as se
    where s.cust = se.cust and s.prod = se.prod and s.quant = se.min;


create view v3 as
    select s.cust, s.prod, s.quant as MAR_MIN, s.month as MAR_MONTH, s.day as MAR_DAY, s.year as MAR_YEAR
    from sales s, (select s.cust, s.prod, min(s.quant)
                        from sales s
                        where s.month = 3
                        group by s.cust, s.prod) as se
    where s.cust = se.cust and s.prod = se.prod and s.quant = se.min;



select *
from v1 natural full join v2  natural full join v3
order by cust













