select
	c.name,
	c.country_id,
	r.name as region,
	c2.name as continent
from
	countries c
join regions r
on
	r.region_id = c.region_id
join continents c2
on
	c2.continent_id = r.continent_id
group by
	c.name
order by
	c.name;

select c.name
from countries c 
where c.country_id = 1;

select l.`language` 
from country_languages cl 
join languages l 
on l.language_id = cl.language_id
where country_id = 107;

select max(cs.`year`), cs.population, cs.gdp 
from country_stats cs 
where country_id = 107;

