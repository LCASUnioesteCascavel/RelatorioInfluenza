#ifndef __0_SIM__
#define __0_SIM__

#include "MacrosParametros.h"

// Macros para acesso aos parametros armazenados no arquivo 
// "Entradas/MonteCarlo_{1}/Simulacao/0-SIM.csv": 

// QUANTIDADE_SIMULACOES:    Parametro "SIM001". 
// QUANTIDADE_CICLOS:        Parametro "SIM002". 

#define QUANTIDADE_SIMULACOES (int)(parametros[DESL_0_SIM + 0])
#define QUANTIDADE_CICLOS (int)(parametros[DESL_0_SIM + 2])

#endif
