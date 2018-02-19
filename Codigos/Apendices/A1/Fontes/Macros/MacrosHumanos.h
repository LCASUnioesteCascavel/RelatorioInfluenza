#ifndef __MACROS_HUMANOS__
#define __MACROS_HUMANOS__

//----------------------------------------------------------------------------//
/*
 Definicies a representacao bitstring dos agentes humanos: 

 Tira 1:
 S  = Sexo                                (1 bit,  2 valores)
 FE = Faixa etaria                        (3 bits, 8 valores)
 C  = Contador                            (9 bits, 512 valores)
 SD = Saude                               (3 bits, 8 valores)
 
 Tira 2:
 X = Latitude                             (19 bits, 524.288 valores)
 L = Lote                                 (13 bits, 8.192 valores)

 Tira 3:
 Y = Longitude                            (23 bits, 8.388.608 valores)
 Q = Quadra                               (9 bits, 512 valores)
*/
//----------------------------------------------------------------------------//
// Tamanho dos atributos

#define TH_S  1 // Tamanho em bits do atributo S
#define TH_FE 3 // Tamanho em bits do atributo FE
#define TH_C  9 // Tamanho em bits do atributo C
#define TH_SD 3 // Tamanho em bits do atributo SD

#define TH_X 19 // Tamanho em bits do atributo X
#define TH_L 13 // Tamanho em bits do atributo L

#define TH_Y 23 // Tamanho em bits do atributo Y
#define TH_Q 9  // Tamanho em bits do atributo Q
//----------------------------------------------------------------------------//
// Bits anteriores

#define AH_S  15 // Quantidade de bits anteriores ao atributo S
#define AH_FE 12 // Quantidade de bits anteriores ao atributo FE
#define AH_C  3  // Quantidade de bits anteriores ao atributo C
#define AH_SD 0  // Quantidade de bits anteriores ao atributo SD

#define AH_X 13  // Quantidade de bits anteriores ao atributo X
#define AH_L 0   // Quantidade de bits anteriores ao atributo L

#define AH_Y 9   // Quantidade de bits anteriores ao atributo Y
#define AH_Q 0   // Quantidade de bits anteriores ao atributo Q
//----------------------------------------------------------------------------//
// Mascaras positivas

#define MAH_S  (uint16_t) 32768U      // Mascara positiva ao atributo S
#define MAH_FE (uint16_t) 28672U      // Mascara positiva ao atributo FE
#define MAH_C  (uint16_t) 4088U       // Mascara positiva ao atributo C
#define MAH_SD (uint16_t) 7U          // Mascara positiva ao atributo SD

#define MAH_X  (uint32_t) 4294959104U // Mascara positiva ao atributo X
#define MAH_L  (uint32_t) 8191U       // Mascara positiva ao atributo L

#define MAH_Y  (uint32_t) 4294966784U // Mascara positiva ao atributo Y
#define MAH_Q  (uint32_t) 511U        // Mascara positiva ao atributo Q

//----------------------------------------------------------------------------//
// Mascaras negativas

#define NMH_S  (uint16_t) 32767U       // Mascara negativa ao atributo S
#define NMH_FE (uint16_t) 36863U       // Mascara negativa ao atributo FE
#define NMH_C  (uint16_t) 61447U       // Mascara negativa ao atributo C
#define NMH_SD (uint16_t) 65528U       // Mascara negativa ao atributo SD

#define NMH_X  (uint32_t) 8191U        // Mascara negativa ao atributo X
#define NMH_L  (uint32_t) 4294959104U  // Mascara negativa ao atributo L

#define NMH_Y  (uint32_t) 511U         // Mascara negativa ao atributo Y
#define NMH_Q  (uint32_t) 4294966784U  // Mascara negativa ao atributo Q

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
