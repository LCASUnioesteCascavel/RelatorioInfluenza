-- criarTabelaPontos
CREATE OR REPLACE FUNCTION criarTabelaPontos(ambiente varchar)
RETURNS void AS
$$
BEGIN
	EXECUTE format('CREATE SEQUENCE %s_pontos_seq', ambiente);
	EXECUTE format('CREATE TABLE %s_pontos AS
		SELECT nextval(''%s_pontos_seq'') AS id, quadra, lote, 
		(st_dumppoints(geom)).geom, refinamento
		FROM (
			SELECT quadra, lote, interpolarPontos(geom, refinamento) 
			AS geom, refinamento
			FROM %s_poligonos
			ORDER BY quadra, lote
		) AS A
	', ambiente, ambiente, ambiente);
	EXECUTE format('ALTER TABLE %s_pontos ADD PRIMARY KEY (id)', ambiente);
	EXECUTE format('DROP SEQUENCE %s_pontos_seq', ambiente);
	EXECUTE format('CREATE INDEX %s_pontos_geom_idx ON %s_pontos 
		USING gist(geom)', ambiente, ambiente);
END
$$
LANGUAGE plpgsql;
