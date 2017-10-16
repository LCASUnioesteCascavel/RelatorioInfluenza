-- Funcao para remover as tabelas relacionadas a um ambiente
CREATE OR REPLACE FUNCTION removerTabelas(ambiente varchar)
RETURNS void AS
$$
BEGIN
	EXECUTE format('DROP TABLE %s_pontos', ambiente);
	EXECUTE format('DROP TABLE %s_vizinhancasentrepoligonos', ambiente);
	EXECUTE format('DROP TABLE %s_vizinhancasmoorepontos', ambiente);
END
$$
LANGUAGE plpgsql;
