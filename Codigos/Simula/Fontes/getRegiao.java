private int getRegiao(char c) {
  int regiao = -1;
  switch (c) {
  case '0':
    regiao = 0;
    break;
  case 'G':
    regiao = 1;
    break;
  case 'Q':
    regiao = 2;
    break;
  }
  return regiao;
}
