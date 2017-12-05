#ifndef __MACROS_PARAMETROS__
#define __MACROS_PARAMETROS__

//----------------------------------------------------------------------------//
#define N_0_SIM   2
#define N_0_INI_H 32
#define N_1_MOV_H 5
#define N_2_CON_H 5
#define N_3_TRA_H 21

#define DESL_0_SIM   0   // (0 << 1)
#define DESL_0_INI_H 4   // (2 << 1)
#define DESL_1_MOV_H 68 // (34 << 1)
#define DESL_2_CON_H 78 // (39 << 1)
#define DESL_3_TRA_H 88 // (44 << 1)
#define N_PAR       130 // (65 << 1)
//----------------------------------------------------------------------------//
#define ENTRE_FAIXA(min, max, per) ((min) + ((max) - (min)) * (per))
#define randPerc dist(seed)
//----------------------------------------------------------------------------//

#endif
