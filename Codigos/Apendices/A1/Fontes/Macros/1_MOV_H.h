#ifndef __1_MOV_H__
#define __1_MOV_H__

#include "MacrosParametros.h"

#define TAXA_MOBILIDADE(ie) \
(double)(ENTRE_FAIXA( \
parametros[DESL_1_MOV_H + 0 + ie * 2], \
parametros[DESL_1_MOV_H + 1 + ie * 2], \
(randPerc)))
#define PERC_MIGRACAO \
(double)(ENTRE_FAIXA( \
parametros[DESL_1_MOV_H + 8], \
parametros[DESL_1_MOV_H + 9], \
(randPerc)))

#endif
