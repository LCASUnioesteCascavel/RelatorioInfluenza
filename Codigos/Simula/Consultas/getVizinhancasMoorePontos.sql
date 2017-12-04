-- getVizinhancasMoorePontos
CREATE OR REPLACE FUNCTION getVizinhancasMoorePontos(ambiente varchar)
RETURNS TABLE (id1 bigint, q1 varchar, l1 varchar, x1 float, y1 float, 
ref1 integer, id2 bigint, q2 varchar, l2 varchar, x2 float, y2 float, 
ref2 integer) AS
$$
BEGIN
	RETURN QUERY
	EXECUTE format(
	'select a.id id1, a.quadra q1, a.lote l1, st_x(a.geom) x1, st_y(a.geom) y1, 
    a.ref ref1, b.id id2, b.quadra q2, b.lote l2, st_x(b.geom) x2, 
    st_y(b.geom) y2, b.ref ref2 
	from %s_pontos a, %s_pontos b, 
		(select * from %s_vizinhancasentrepoligonos union 
		 select lote l1, quadra q1, lote l2, quadra q2 from %s_poligonos) c
	where a.quadra = c.q1 and a.lote = c.l1 and b.quadra = c.q2 and 
        b.lote = c.l2 and st_dwithin(a.geom, b.geom, greatest(a.ref, b.ref) * 
        sqrt(2) + 0.1) and b.id != a.id'
	, ambiente, ambiente, ambiente, ambiente);
END
$$
LANGUAGE plpgsql;
