-- getPontosLotesERuas
CREATE OR REPLACE FUNCTION getPontosLotesERuas(tabelaPontos varchar) 
RETURNS TABLE (id bigint, quadra varchar, lote varchar, x float, 
y float, refinamento integer) AS
$$
BEGIN
	RETURN QUERY
	EXECUTE format(
	'SELECT id, quadra, lote, st_x(geom) AS x, st_y(geom) AS y, refinamento 
	FROM %s 
	ORDER BY id'
	, tabelaPontos);
END
$$ 
LANGUAGE plpgsql;
