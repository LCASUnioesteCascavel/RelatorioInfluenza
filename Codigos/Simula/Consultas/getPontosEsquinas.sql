-- getPontosEsquinas
CREATE OR REPLACE FUNCTION getPontosEsquinas(tabelaPoligonos 
varchar, tabelaPontos varchar) 
RETURNS TABLE (id bigint, quadra varchar, l1 varchar, l2 varchar, 
x float, y float, refinamento integer) AS
$$
BEGIN
	RETURN QUERY
	EXECUTE format(
	'select a.id, a.quadra, b.lote as l1, c.lote as l2, st_x(a.geom) 
	as x, st_y(a.geom) as y, a.refinamento 
	from %s as a, %s as b, %s as c 
	where b.lote != c.lote and b.quadra = ''0000'' and c.quadra = ''0000'' 
	and st_contains(b.geom, a.geom) and st_contains(c.geom, a.geom) 
	order by b.lote, c.lote, st_x(a.geom), st_y(a.geom)'
	, tabelaPontos, tabelaPoligonos, tabelaPoligonos);
END
$$
LANGUAGE plpgsql;
