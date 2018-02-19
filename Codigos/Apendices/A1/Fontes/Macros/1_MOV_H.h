#ifndef __1_MOV_H__
#define __1_MOV_H__

// Macros para acesso aos parametros armazenados no arquivo 
// "Entradas/MonteCarlo_{1}/Humanos/1-MOV.csv": 

// TAXA_MOBILIDADE:    Parametros "MOV001" a "MOV006". 
// PERC_MIGRACAO:      Parametro  "MOV007". 

#include "MacrosParametros.h"

#define TAXA_MOBILIDADE(ie) \
(double)(ENTRE_FAIXA( \
parametros[DESL_1_MOV_H + 0 + ie * 2], \
parametros[DESL_1_MOV_H + 1 + ie * 2], \
(randPerc)))
#define PERC_MIGRACAO \
(double)(ENTRE_FAIXA( \
parametros[DESL_1_MOV_H + 12], \
parametros[DESL_1_MOV_H + 13], \
(randPerc)))

#endif
