select * from (select se.cust, se.quant as MAX_Q, se.prod as MAX_PRODUCT, se.month as MAN_MONTH, se.day as MAX_DAY, se.year as MAX_YEAR, se.state as MAX_ST
from sales se
where (se.cust, se.quant) in (select s.cust, max(s.quant)
                    from sales s
                  	group by s.cust)) as a
natural join
(select se.cust, se.quant as MIN_Q, se.prod as PRODUCT, se.month as MIN_MONTH, se.day as MIN_DAY, se.year as MIN_YEAR, se.state as MIN_ST, ss.avg
from sales se, (select s.cust as C, min(s.quant) as MIN_Q, avg(s.quant)
                    	from sales s
                     	group by s.cust) as ss
where se.cust = ss.C and se.quant = ss.MIN_Q) as b 
 
 
 



 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 