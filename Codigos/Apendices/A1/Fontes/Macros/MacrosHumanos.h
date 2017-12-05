#ifndef __MACROS_HUMANOS__
#define __MACROS_HUMANOS__

//----------------------------------------------------------------------------//
/*
 S  = Sexo                                (1 bit,  2 valores)
 FE = Faixa etaria                        (3 bits, 8 valores)
 C  = Contador                            (9 bits, 512 valores)
 SD = Saude                               (3 bits, 8 valores)

 X = Latitude                             (19 bits, 524.288 valores)
 L = Lote                                 (13 bits, 8.192 valores)

 Y = Longitude                            (23 bits, 8.388.608 valores)
 Q = Quadra                               (9 bits, 512 valores)
*/
//----------------------------------------------------------------------------//
// Tamanho dos atributos

#define TH_S  1
#define TH_FE 3
#define TH_C  9
#define TH_SD 3

#define TH_X 19
#define TH_L 13

#define TH_Y 23
#define TH_Q 9
//----------------------------------------------------------------------------//
// Bits anteriores

#define AH_S  15
#define AH_FE 12
#define AH_C  3
#define AH_SD 0

#define AH_X 13
#define AH_L 0

#define AH_Y 9
#define AH_Q 0
//----------------------------------------------------------------------------//
// Mascaras positivas

#define MAH_S  (uint16_t) 32768U
#define MAH_FE (uint16_t) 28672U
#define MAH_C  (uint16_t) 4088U
#define MAH_SD (uint16_t) 7U

#define MAH_X  (uint32_t) 4294959104U
#define MAH_L  (uint32_t) 8191U

#define MAH_Y  (uint32_t) 4294966784U
#define MAH_Q  (uint32_t) 511U

//----------------------------------------------------------------------------//
// Mascaras negativas

#define NMH_S  (uint16_t) 32767U
#define NMH_FE (uint16_t) 36863U
#define NMH_C  (uint16_t) 61447U
#define NMH_SD (uint16_t) 65528U

#define NMH_X  (uint32_t) 8191U
#define NMH_L  (uint32_t) 4294959104U

#define NMH_Y  (uint32_t) 511U
#define NMH_Q  (uint32_t) 4294966784U

//----------------------------------------------------------------------------//
// Gets

#define GET_H(i, t, ma, a) ((humanos[(i)].t & ma) >> a)

#define GET_S_H(i)  (int)(GET_H(i, t1, MAH_S, AH_S))
#define GET_FE_H(i) (int)(GET_H(i, t1, MAH_FE, AH_FE))
#define GET_C_H(i)  (int)(GET_H(i, t1, MAH_C, AH_C))
#define GET_SD_H(i) (int)(GET_H(i, t1, MAH_SD, AH_SD))

#define GET_X_H(i)  (int)(GET_H(i, t2, MAH_X, AH_X))
#define GET_L_H(i)  (int)(GET_H(i, t2, MAH_L, AH_L))

#define GET_Y_H(i)  (int)(GET_H(i, t3, MAH_Y, AH_Y))
#define GET_Q_H(i)  (int)(GET_H(i, t3, MAH_Q, AH_Q))

//----------------------------------------------------------------------------//
// Sets

#define SET_H(i, t, novo, nm, a) (humanos[(i)].t = \
((humanos[(i)].t & nm) | (((unsigned)(novo)) << a)))

#define SET_S_H(i, novo)  (SET_H(i, t1, novo, NMH_S, AH_S))
#define SET_FE_H(i, novo) (SET_H(i, t1, novo, NMH_FE, AH_FE))
#define SET_C_H(i, novo)  (SET_H(i, t1, novo, NMH_C, AH_C))
#define SET_SD_H(i, novo) (SET_H(i, t1, novo, NMH_SD, AH_SD))

#define SET_X_H(i, novo)  (SET_H(i, t2, novo, NMH_X, AH_X))
#define SET_L_H(i, novo)  (SET_H(i, t2, novo, NMH_L, AH_L))

#define SET_Y_H(i, novo)  (SET_H(i, t3, novo, NMH_Y, AH_Y))
#define SET_Q_H(i, novo)  (SET_H(i, t3, novo, NMH_Q, AH_Q))

//----------------------------------------------------------------------------//

#endif
