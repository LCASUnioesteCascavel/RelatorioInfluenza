// Tamanho em bits dos campos:
#define T_Q 7
#define T_S 1

#define T_I 2
#define T_L 11
#define T_X 19

#define T_Y 24
#define T_C 6
#define T_E 2
//----------------------------------------------------------------------------//
// Numero de bits anteriores a cada campo:
#define A_Q 1
#define A_S 0

#define A_I 30
#define A_L 19
#define A_X 0

#define A_Y 8
#define A_C 2
#define A_E 0
//----------------------------------------------------------------------------//
// Mascaras Positivas

#define MA_Q (UINT8)254U
#define MA_S (UINT8)1U

#define MA_I (UINT32)3221225472U
#define MA_L (UINT32)1073217536U
#define MA_X (UINT32)524287U

#define MA_Y (UINT32)4294967040U
#define MA_C (UINT32)252U
#define MA_E (UINT32)3U
//----------------------------------------------------------------------------//
// Mascaras Negativas

#define NM_Q (UINT8)4294967041U
#define NM_S (UINT8)4294967294U

#define NM_I (UINT32)1073741823U
#define NM_L (UINT32)3221749759U
#define NM_X (UINT32)4294443008U

#define NM_Y (UINT32)255U
#define NM_C (UINT32)4294967043U
#define NM_E (UINT32)4294967292U
//----------------------------------------------------------------------------//
// Gets

#define GET_Q(i) (int)((agentes[i].t1 & MA_Q) >> A_Q)
#define GET_S(i) (int)((agentes[i].t1 & MA_S) >> A_S)

#define GET_I(i) (int)((agentes[i].t2 & MA_I) >> A_I)
#define GET_L(i) (int)((agentes[i].t2 & MA_L) >> A_L)
#define GET_X(i) (int)((agentes[i].t2 & MA_X) >> A_X)

#define GET_Y(i) (int)((agentes[i].t3 & MA_Y) >> A_Y)
#define GET_C(i) (int)((agentes[i].t3 & MA_C) >> A_C)
#define GET_E(i) (int)((agentes[i].t3 & MA_E) >> A_E)
//----------------------------------------------------------------------------//
// Sets

#define SET_Q(i, novo)                                                         \
  (agentes[i].t1 = ((agentes[i].t1 & NM_Q) | (((UINT8)novo) << A_Q)))
#define SET_S(i, novo)                                                         \
  (agentes[i].t1 = ((agentes[i].t1 & NM_S) | (((UINT8)novo) << A_S)))

#define SET_I(i, novo)                                                         \
  (agentes[i].t2 = ((agentes[i].t2 & NM_I) | (((UINT32)novo) << A_I)))
#define SET_L(i, novo)                                                         \
  (agentes[i].t2 = ((agentes[i].t2 & NM_L) | (((UINT32)novo) << A_L)))
#define SET_X(i, novo)                                                         \
  (agentes[i].t2 = ((agentes[i].t2 & NM_X) | (((UINT32)novo) << A_X)))

#define SET_Y(i, novo)                                                         \
  (agentes[i].t3 = ((agentes[i].t3 & NM_Y) | (((UINT32)novo) << A_Y)))
#define SET_C(i, novo)                                                         \
  (agentes[i].t3 = ((agentes[i].t3 & NM_C) | (((UINT32)novo) << A_C)))
#define SET_E(i, novo)                                                         \
  (agentes[i].t3 = ((agentes[i].t3 & NM_E) | (((UINT32)novo) << A_E)))
