-- interpolarPontos
CREATE OR REPLACE FUNCTION interpolarPontos(geometry, integer)
RETURNS geometry AS
	'select st_collect(st_setsrid(st_point(x::float, y::float), st_srid($1))) from 
	generate_series((round(floor(st_xmin($1))::int / $2) * $2)::int, 
	(round(ceiling(st_xmax($1))::int / $2) * $2)::int, $2) as x,
	generate_series((round(floor(st_ymin($1))::int / $2) * $2)::int, 
	(round(ceiling(st_ymax($1))::int / $2) * $2)::int, $2) as y 
	where st_intersects($1, st_setsrid(st_point(x::float, y::float), st_srid($1)))'
LANGUAGE SQL