#ifndef __2_CON_H__
#define __2_CON_H__

// Macros para acesso aos parametros armazenados no arquivo 
// "Entradas/MonteCarlo_{1}/Humanos/2-CON.csv": 

// TAXA_INFECCAO_HUMANO_SUSCETIVEL:    Parametros "CON001" a "CON006". 
// K_COMP:                             Parametro  "CON007". 

#include "MacrosParametros.h"

#define TAXA_INFECCAO_HUMANO_SUSCETIVEL(fe) \
(double)(ENTRE_FAIXA( \
parametros[DESL_2_CON_H + 0 + fe * 2], \
parametros[DESL_2_CON_H + 1 + fe * 2], \
(randPerc)))
#define K_COMP \
(double)(ENTRE_FAIXA( \
parametros[DESL_2_CON_H + 12], \
parametros[DESL_2_CON_H + 13], \
(randPerc)))

#endif
