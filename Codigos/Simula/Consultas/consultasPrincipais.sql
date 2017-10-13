SELECT removerTabelas('cascavel');

SELECT criarTabelas('cascavel');

UPDATE cascavel_poligonos SET refinamento = 20 WHERE quadra = '0000';
UPDATE cascavel_poligonos SET refinamento = 30 WHERE quadra != '0000';