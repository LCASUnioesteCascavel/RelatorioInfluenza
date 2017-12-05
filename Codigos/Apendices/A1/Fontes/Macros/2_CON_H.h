#ifndef __2_CON_H__
#define __2_CON_H__

#include "MacrosParametros.h"

#define TAXA_INFECCAO_HUMANO_SUSCETIVEL(fe) \
(double)(ENTRE_FAIXA( \
parametros[DESL_2_CON_H + 0 + fe * 2], \
parametros[DESL_2_CON_H + 1 + fe * 2], \
(randPerc)))
#define K_SAZO \
(double)(ENTRE_FAIXA( \
parametros[DESL_2_CON_H + 8], \
parametros[DESL_2_CON_H + 9], \
(randPerc)))

#endif
