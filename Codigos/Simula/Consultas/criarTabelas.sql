-- Funcao para criar as tabelas relacionadas a um ambiente
CREATE OR REPLACE FUNCTION criarTabelas(ambiente varchar)
RETURNS void AS
$$
BEGIN
	EXECUTE format('SELECT criarTabelaPontos(''%s'')', ambiente);
	EXECUTE format('CREATE TABLE %s_vizinhancasentrepoligonos AS 
  SELECT * FROM getVizinhancasEntrePoligonos(''%s'')', ambiente, ambiente);
	EXECUTE format('CREATE TABLE %s_vizinhancasmoorepontos AS 
  SELECT * FROM getVizinhancasMoorePontos(''%s'')', ambiente, ambiente);
$$
LANGUAGE plpgsql;
