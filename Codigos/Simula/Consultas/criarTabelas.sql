-- Funcao para criar as tabelas relacionadas a um ambiente
CREATE OR REPLACE FUNCTION criarTabelas(ambiente varchar)
RETURNS void AS
$$
BEGIN
	EXECUTE format('CREATE TABLE %s_lotes AS SELECT * 
		FROM getLotes(''%s_poligonos'')', ambiente, ambiente);
	EXECUTE format('CREATE TABLE %s_vertices AS SELECT * 
		FROM getVertices(''%s_poligonos'')', ambiente, ambiente);
	EXECUTE format('CREATE TABLE %s_arestas AS SELECT * 
		FROM getArestas(''%s_poligonos'')', ambiente, ambiente);
	EXECUTE format('SELECT criarTabelaPontos(''%s'')', ambiente);
	EXECUTE format('CREATE TABLE %s_pontosloteseruas AS 
		SELECT * FROM getPontosLotesERuas(''%s_pontos'')', ambiente, ambiente);
	EXECUTE format('CREATE TABLE %s_pontosesquinas AS SELECT * 
		FROM getPontosEsquinas(''%s_poligonos'', ''%s_pontos'')', 
		ambiente, ambiente, ambiente);
	EXECUTE format('CREATE TABLE %s_vizinhancasentrepoligonos AS 
		SELECT * FROM getVizinhancasEntrePoligonos(''%s_poligonos'')', 
		ambiente, ambiente);
	EXECUTE format('CREATE TABLE %s_vizinhancasmoorepontos AS SELECT * 
		FROM getVizinhancasMoorePontos(''%s_pontos'', 
		''%s_vizinhancasentrepoligonos'', ''%s_poligonos'')', 
		ambiente, ambiente, ambiente, ambiente);
	EXECUTE format('CREATE TABLE %s_centroidesesquinas AS SELECT * 
		FROM getCentroidesEsquinas(''%s_poligonos'', ''%s_pontos'')', 
		ambiente, ambiente, ambiente);
	EXECUTE format('CREATE TABLE %s_centroideslotes AS SELECT * 
		FROM getCentroidesLotes(''%s_poligonos'')', ambiente, ambiente);
END
$$
LANGUAGE plpgsql;

