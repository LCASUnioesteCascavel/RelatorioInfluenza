#ifndef __MACROS__
#define __MACROS__

//----------------------------------------------------------------------------//
// Quantidade de colunas das saidas de quantidades dos agentes
// (N_SEXOS * N_IDADES * N_ESTADOS_H
#define N_COLS_H    48
//----------------------------------------------------------------------------//
// Linearizacao de matriz
#define VEC(i, j, nc) (((i) * (nc)) + (j))
//----------------------------------------------------------------------------//
#define RUA 0
//----------------------------------------------------------------------------//
// Distancia euclidiana
#define DIST(x1, y1, x2, y2) \
(double)(sqrt(pow((x1) - (x2), 2.0) + pow((y1) - (y2), 2.0)))
//----------------------------------------------------------------------------//
// Estados da dengue para humanos (SD)
#define N_ESTADOS_H   6

#define VIVO  1
#define MORTO 0

#define SUSCETIVEL 1
#define EXPOSTO    2
#define INFECTANTE 3
#define QUARENTENA 4
#define IMUNIZADO  5
#define RECUPERADO 6
//----------------------------------------------------------------------------//
// Sexos para humanos (S)
#define N_SEXOS 2

#define MASCULINO 0
#define FEMININO  1
//----------------------------------------------------------------------------//
// Faixas etarias dos humanos (FE)
#define N_IDADES 4

#define CRIANCA 0
#define JOVEM   1
#define ADULTO  2
#define IDOSO   3
//----------------------------------------------------------------------------//
#endif
