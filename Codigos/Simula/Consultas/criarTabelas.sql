-- Funcao para criar as tabelas relacionadas a um ambiente
CREATE OR REPLACE FUNCTION criarTabelas(ambiente varchar)
RETURNS void AS
$$
BEGIN
	EXECUTE format('SELECT criarTabelaPontos(''%s'')', ambiente);
	EXECUTE format('CREATE TABLE %s_vizinhancasentrepoligonos AS 
		SELECT * FROM getVizinhancasEntrePoligonos(''%s_poligonos'')', 
		ambiente, ambiente);
	EXECUTE format('CREATE TABLE %s_vizinhancasmoorepontos AS SELECT * 
		FROM getVizinhancasMoorePontos(''%s_pontos'', 
		''%s_vizinhancasentrepoligonos'', ''%s_poligonos'')', 
		ambiente, ambiente, ambiente, ambiente);
END
$$
LANGUAGE plpgsql;

