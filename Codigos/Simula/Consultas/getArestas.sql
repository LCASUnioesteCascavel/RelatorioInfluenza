-- getArestas
CREATE OR REPLACE FUNCTION getArestas(tabelaPoligonos varchar)
RETURNS TABLE (l1 varchar, q1 varchar, l2 varchar, q2 varchar) AS
$$
BEGIN
	RETURN QUERY
	EXECUTE format(
	'select * from ( 
		select a.lote as l1, a.quadra as q1, b.lote as l2, b.quadra as q2 
		from %s as a, %s as b 
		where a.lote <> b.lote and a.quadra = b.quadra and a.quadra like ''Q%%'' 
		and st_dwithin(a.geom, b.geom, 2) 
		union 
		select a.lote as l1, a.quadra as q1, b.lote as l2, b.quadra as q2 
		from %s as a, %s as b 
		where ((a.quadra like ''Q%%'' and b.quadra = ''0000'') or 
		(a.quadra = ''0000'' and b.quadra like ''Q%%'')) and 
		st_dwithin(a.geom, b.geom, 2) 
		union 
		select a.lote as l1, a.quadra as q1, b.lote as l2, b.quadra as q2 
		from %s as a, %s as b 
		where a.lote <> b.lote and a.quadra = ''0000'' and b.quadra = ''0000'' 
		and st_dwithin(a.geom, b.geom, 2) 
	) AS a order by a.q1, a.l1, a.q2, a.l2'
	, tabelaPoligonos, tabelaPoligonos, tabelaPoligonos, tabelaPoligonos, 
		tabelaPoligonos, tabelaPoligonos);
END
$$
LANGUAGE plpgsql;