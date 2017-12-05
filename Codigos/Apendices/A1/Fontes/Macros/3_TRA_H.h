#ifndef __3_TRA_H__
#define __3_TRA_H__

#include "MacrosParametros.h"

#define PERIODO_EXPOSTO_HUMANO(ie) \
(int) (ENTRE_FAIXA( \
parametros[DESL_3_TRA_H + 0 + ie * 2], \
parametros[DESL_3_TRA_H + 1 + ie * 2], \
(randPerc)))
#define PERIODO_INFECTADO_HUMANO(ie) \
(int) (ENTRE_FAIXA( \
parametros[DESL_3_TRA_H + 8 + ie * 2], \
parametros[DESL_3_TRA_H + 9 + ie * 2], \
(randPerc)))
#define PERIODO_IMUNIZADO_HUMANO(ie) \
(int) (ENTRE_FAIXA( \
parametros[DESL_3_TRA_H + 16 + ie * 2], \
parametros[DESL_3_TRA_H + 17 + ie * 2], \
(randPerc)))
#define PERIODO_QUARENTENA_HUMANO(ie) \
(int) (ENTRE_FAIXA( \
parametros[DESL_3_TRA_H + 24 + ie * 2], \
parametros[DESL_3_TRA_H + 25 + ie * 2], \
(randPerc)))
#define PERIODO_RECUPERADO_HUMANO(ie) \
(int) (ENTRE_FAIXA( \
parametros[DESL_3_TRA_H + 32 + ie * 2], \
parametros[DESL_3_TRA_H + 33 + ie * 2], \
(randPerc)))
#define TAXA_EFICACIA_VACINA \
(double) (ENTRE_FAIXA( \
parametros[DESL_3_TRA_H + 40], \
parametros[DESL_3_TRA_H + 41], \
(randPerc)))

#endif
