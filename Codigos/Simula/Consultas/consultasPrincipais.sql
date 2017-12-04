SELECT removerTabelas('cascavel');

SELECT criarTabelas('cascavel');

UPDATE cascavel_poligonos SET ref = 15 WHERE quadra = '0000';
UPDATE cascavel_poligonos SET ref = 25 WHERE quadra != '0000';
