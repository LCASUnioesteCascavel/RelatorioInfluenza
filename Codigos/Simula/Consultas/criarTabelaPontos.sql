-- criarTabelaPontos
-- Alternativas a interpolarPontos: 
-- 	. st_centroid(geom)
-- 	. st_pointonsurface(geom)
CREATE OR REPLACE FUNCTION criarTabelaPontos(ambiente varchar)
RETURNS void AS
$$
BEGIN
	EXECUTE format('CREATE SEQUENCE %s_pontos_seq', ambiente);
	EXECUTE format('CREATE TABLE %s_pontos AS
		SELECT nextval(''%s_pontos_seq'') id, quadra, lote, (st_dumppoints(geom)).geom, 
		      st_x((st_dumppoints(geom)).geom) x, st_y((st_dumppoints(geom)).geom) y, ref
		FROM (
			SELECT quadra, lote, interpolarPontos(geom, ref) geom, ref
			FROM %s_poligonos
		) A
	', ambiente, ambiente, ambiente);
	EXECUTE format('ALTER TABLE %s_pontos ADD PRIMARY KEY (id)', ambiente);
	EXECUTE format('DROP SEQUENCE %s_pontos_seq', ambiente);
	EXECUTE format('CREATE INDEX %s_pontos_geom_idx ON %s_pontos USING gist(geom)', 
  ambiente, ambiente);
END
$$
LANGUAGE plpgsql;
