-- getVizinhancasEntrePoligonos
CREATE OR REPLACE FUNCTION getVizinhancasEntrePoligonos(ambiente varchar)
RETURNS TABLE (l1 varchar, q1 varchar, l2 varchar, q2 varchar) AS
$$
BEGIN
	RETURN QUERY
	EXECUTE format(
	'select * from ( 
		select a.lote l1, a.quadra q1, b.lote l2, b.quadra q2 
		from %s_poligonos a, %s_poligonos b 
		where a.lote < b.lote and a.quadra = b.quadra and a.quadra != ''0000'' and 
    st_dwithin(a.geom, b.geom, 2) 
		union 
		select a.lote l1, a.quadra q1, b.lote l2, b.quadra q2 
		from %s_poligonos a, %s_poligonos b 
		where a.quadra = ''0000'' and b.quadra != ''0000'' and 
    st_dwithin(a.geom, b.geom, 2) 
		union 
		select a.lote l1, a.quadra q1, b.lote l2, b.quadra q2 
		from %s_poligonos a, %s_poligonos b 
		where a.lote < b.lote and a.quadra = ''0000'' and b.quadra = ''0000'' and 
    st_dwithin(a.geom, b.geom, 2)
		union
		select a.lote l1, a.quadra q1, b.lote l2, b.quadra q2 
		from %s_poligonos a, %s_poligonos b 
		where a.lote < b.lote and a.quadra like ''G%%'' and b.quadra like ''G%%'' and 
		      a.quadra != b.quadra and st_dwithin(a.geom, b.geom, 2)
	) a'
	, ambiente, ambiente, ambiente, ambiente, ambiente, ambiente, ambiente, ambiente);
END
$$
LANGUAGE plpgsql;
