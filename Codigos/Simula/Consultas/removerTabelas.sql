-- Funcao para remover as tabelas relacionadas a um ambiente
CREATE OR REPLACE FUNCTION removerTabelas(ambiente varchar)
RETURNS void AS
$$
BEGIN
	EXECUTE format('DROP TABLE %s_pontos', ambiente);
	EXECUTE format('DROP TABLE %s_pontosloteseruas', ambiente);
	EXECUTE format('DROP TABLE %s_pontosesquinas', ambiente);
	EXECUTE format('DROP TABLE %s_vizinhancasentrepoligonos', ambiente);
	EXECUTE format('DROP TABLE %s_vizinhancasmoorepontos', ambiente);
	EXECUTE format('DROP TABLE %s_lotes', ambiente);
	EXECUTE format('DROP TABLE %s_vertices', ambiente);
	EXECUTE format('DROP TABLE %s_arestas', ambiente);
	EXECUTE format('DROP TABLE %s_centroidesesquinas', ambiente);
	EXECUTE format('DROP TABLE %s_centroideslotes', ambiente);
END
$$
LANGUAGE plpgsql;
