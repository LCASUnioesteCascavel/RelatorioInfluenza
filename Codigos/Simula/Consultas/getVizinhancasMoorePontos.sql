-- getVizinhancasMoorePontos
CREATE OR REPLACE FUNCTION getVizinhancasMoorePontos(tabelaPontos varchar, 
tabelaVizinhancasEntrePoligonos varchar, tabelaPoligonos varchar)
RETURNS TABLE (id1 bigint, q1 varchar, l1 varchar, x1 float, y1 float, 
refinamento1 integer, id2 bigint, q2 varchar, l2 varchar, x2 float, 
y2 float, refinamento2 integer) AS
$$
BEGIN
	RETURN QUERY
	EXECUTE format(
	'select a.id as id1, a.quadra as q1, a.lote as l1, st_x(a.geom) as x1, 
	st_y(a.geom) as y1, a.refinamento as refinamento1, 
		b.id as id2, b.quadra as q2, b.lote as l2, st_x(b.geom) as x2, st_y(b.geom) 
		as y2, b.refinamento as refinamento2 
	from %s as a, %s as b, 
		(select * from %s union 
		 select lote as l1, quadra as q1, lote as l2, quadra as q2 from %s) as c
	where a.quadra = c.q1 and a.lote = c.l1 and b.quadra = c.q2 and b.lote 
	= c.l2 and st_dwithin(a.geom, b.geom, greatest(a.refinamento, b.refinamento) 
	* sqrt(2) + 0.1) and b.id != a.id'
	, tabelaPontos, tabelaPontos, tabelaVizinhancasEntrePoligonos, tabelaPoligonos);
END
$$
LANGUAGE plpgsql;
