-- getLotes
CREATE OR REPLACE FUNCTION getLotes(tabelaPoligonos varchar)
RETURNS TABLE (quadra varchar, lote varchar) AS
$$
BEGIN
	RETURN QUERY 
	EXECUTE format( 
	'select quadra, lote 
	from %s 
	where quadra like ''Q%%''
	order by quadra, lote' 
	, tabelaPoligonos);
END
$$
LANGUAGE plpgsql;