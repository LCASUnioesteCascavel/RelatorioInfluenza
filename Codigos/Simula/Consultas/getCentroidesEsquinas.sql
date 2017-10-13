-- getCentroidesEsquinas
CREATE OR REPLACE FUNCTION getCentroidesEsquinas(tabelaPoligonos varchar, 
tabelaPontos varchar)
RETURNS TABLE (l1 varchar, l2 varchar, x float, y float, refinamento integer) AS
$$
BEGIN
	RETURN QUERY
	EXECUTE format(
	'select l1, l2, st_x(cen) as x, st_y(cen) as y, refinamento from ( 
		select b.lote as l1, c.lote as l2, st_centroid(st_collect(a.geom)) 
		as cen, a.refinamento 
		from %s as a, %s as b, %s as c 
		where b.lote != c.lote and b.quadra = ''0000'' and c.quadra = ''0000'' 
		and st_contains(b.geom, a.geom) and st_contains(c.geom, a.geom) 
		group by a.quadra, b.lote, c.lote, a.refinamento 
		order by a.quadra, b.lote, c.lote 
	) as a '
	, tabelaPontos, tabelaPoligonos, tabelaPoligonos);
END
$$
LANGUAGE plpgsql;
