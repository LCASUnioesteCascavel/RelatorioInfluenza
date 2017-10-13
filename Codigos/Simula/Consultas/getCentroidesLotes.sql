-- getCentroidesLotes
CREATE OR REPLACE FUNCTION getCentroidesLotes(tabelaPoligonos varchar)
RETURNS TABLE (q varchar, l varchar, x float, y float, refinamento integer) AS
$$
BEGIN
	RETURN QUERY
	EXECUTE format(
	'select q, l, st_x(cen) as x, st_y(cen) as y, refinamento from ( 
		select a.quadra as q, a.lote as l, st_centroid(a.geom) as cen, a.refinamento
		from %s as a 
		where a.quadra like ''Q%%'' 
		order by a.quadra, a.lote
	) as a'
	, tabelaPoligonos);
END
$$
LANGUAGE plpgsql;
