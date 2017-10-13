-- getVertices
CREATE OR REPLACE FUNCTION getVertices(tabelaPoligonos varchar)
RETURNS TABLE (quadra varchar, lote varchar) AS
$$
BEGIN
	RETURN QUERY
	EXECUTE format(
	'select quadra, lote 
	from %s 
	where quadra not like ''G%%'' 
	order by quadra, lote'
	, tabelaPoligonos);
END
$$
LANGUAGE plpgsql;
