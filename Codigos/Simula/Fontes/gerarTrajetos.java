private void gerarTrajetos() {
  trajetosCrianca = new ArrayList<>();
  trajetosJovem = new ArrayList<>();
  trajetosAdulto = new ArrayList<>();
  trajetosIdoso = new ArrayList<>();
  ArrayList<Local> trajeto = null;

  for (Lote casa : locaisCasa) {
    int duracaoDia = 100;

    // TRAJETOS PARA CRIANCA
    switch ((int) (Math.random() * 4)) {
    case 0: {
      // Casa -> Lazer -> Casa (0.4, 0.2, 0.4)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.4 * duracaoDia)));
      trajeto.add(new Local(
          locaisLazer.get((int) (Math.random() * locaisLazer.size())),
          (int) (0.2 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.4 * duracaoDia)));
      trajetosCrianca.add(trajeto);
    }
      break;

    case 1: {
      // Casa -> Estudo -> Casa (0.3, 0.4, 0.3)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.3 * duracaoDia)));
      trajeto.add(new Local(
          locaisEstudo.get((int) (Math.random() * locaisEstudo.size())),
          (int) (0.4 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.3 * duracaoDia)));
      trajetosCrianca.add(trajeto);
    }
      break;

    case 2: {
      // Casa -> Estudo -> Lazer -> Casa (0.1, 0.35, 0.2, 0.35)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.1 * duracaoDia)));
      trajeto.add(new Local(
          locaisEstudo.get((int) (Math.random() * locaisEstudo.size())),
          (int) (0.35 * duracaoDia)));
      trajeto.add(new Local(
          locaisLazer.get((int) (Math.random() * locaisLazer.size())),
          (int) (0.2 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.35 * duracaoDia)));
      trajetosCrianca.add(trajeto);
    }
      break;

    case 3: {
      // Casa -> Lazer -> Estudo -> Casa (0.1, 0.2, 0.35, 0.35)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.1 * duracaoDia)));
      trajeto.add(new Local(
          locaisLazer.get((int) (Math.random() * locaisLazer.size())),
          (int) (0.2 * duracaoDia)));
      trajeto.add(new Local(
          locaisEstudo.get((int) (Math.random() * locaisEstudo.size())),
          (int) (0.35 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.35 * duracaoDia)));
      trajetosCrianca.add(trajeto);
    }
      break;
    }

    // TRAJETOS PARA JOVEM
    switch ((int) (Math.random() * 6)) {
    case 0: {
      // Casa -> Estudo -> Casa (0.1, 0.55, 0.35)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.1 * duracaoDia)));
      trajeto.add(new Local(
          locaisEstudo.get((int) (Math.random() * locaisEstudo.size())),
          (int) (0.55 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.35 * duracaoDia)));
      trajetosJovem.add(trajeto);
    }
      break;

    case 1: {
      // Casa -> Lazer -> Casa (0.2, 0.45, 0.35)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.2 * duracaoDia)));
      trajeto.add(new Local(
          locaisLazer.get((int) (Math.random() * locaisLazer.size())),
          (int) (0.45 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.35 * duracaoDia)));
      trajetosJovem.add(trajeto);
    }
      break;

    case 2: {
      // Casa -> Trabalho -> Casa (0.1, 0.55, 0.35)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.1 * duracaoDia)));
      trajeto.add(new Local(
          locaisTrabalho.get((int) (Math.random() * locaisTrabalho.size())),
          (int) (0.55 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.35 * duracaoDia)));
      trajetosJovem.add(trajeto);
    }
      break;

    case 3: {
      // Casa -> Estudo -> Lazer -> Casa (0.1, 0.3, 0.25, 0.35)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.1 * duracaoDia)));
      trajeto.add(new Local(
          locaisEstudo.get((int) (Math.random() * locaisEstudo.size())),
          (int) (0.3 * duracaoDia)));
      trajeto.add(new Local(
          locaisLazer.get((int) (Math.random() * locaisLazer.size())),
          (int) (0.25 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.35 * duracaoDia)));
      trajetosJovem.add(trajeto);
    }
      break;

    case 4: {
      // Casa -> Estudo -> Trabalho -> Casa (0.1, 0.25, 0.3, 0.35)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.1 * duracaoDia)));
      trajeto.add(new Local(
          locaisEstudo.get((int) (Math.random() * locaisEstudo.size())),
          (int) (0.25 * duracaoDia)));
      trajeto.add(new Local(
          locaisTrabalho.get((int) (Math.random() * locaisTrabalho.size())),
          (int) (0.3 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.35 * duracaoDia)));
      trajetosJovem.add(trajeto);
    }
      break;

    case 5: {
      // Casa -> Estudo -> Trabalho -> Lazer -> Casa (0.1, 0.2, 0.25, 0.1,
      // 0.35)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.1 * duracaoDia)));
      trajeto.add(new Local(
          locaisEstudo.get((int) (Math.random() * locaisEstudo.size())),
          (int) (0.2 * duracaoDia)));
      trajeto.add(new Local(
          locaisTrabalho.get((int) (Math.random() * locaisTrabalho.size())),
          (int) (0.25 * duracaoDia)));
      trajeto.add(new Local(
          locaisLazer.get((int) (Math.random() * locaisLazer.size())),
          (int) (0.1 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.35 * duracaoDia)));
      trajetosJovem.add(trajeto);
    }
      break;
    }

    // TRAJETOS PARA ADULTO
    switch ((int) (Math.random() * 5)) {
    case 0: {
      // Casa -> Trabalho -> Casa (0.1, 0.6, 0.3)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.1 * duracaoDia)));
      trajeto.add(new Local(
          locaisTrabalho.get((int) (Math.random() * locaisTrabalho.size())),
          (int) (0.6 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.3 * duracaoDia)));
      trajetosAdulto.add(trajeto);
    }
      break;

    case 1: {
      // Casa -> Trabalho -> Estudo -> Casa (0.1, 0.5, 0.1, 0.3)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.1 * duracaoDia)));
      trajeto.add(new Local(
          locaisTrabalho.get((int) (Math.random() * locaisTrabalho.size())),
          (int) (0.5 * duracaoDia)));
      trajeto.add(new Local(
          locaisEstudo.get((int) (Math.random() * locaisEstudo.size())),
          (int) (0.1 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.3 * duracaoDia)));
      trajetosAdulto.add(trajeto);
    }
      break;

    case 2: {
      // Casa -> Trabalho -> Lazer -> Casa (0.1, 0.45, 0.15, 0.3)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.1 * duracaoDia)));
      trajeto.add(new Local(
          locaisTrabalho.get((int) (Math.random() * locaisTrabalho.size())),
          (int) (0.45 * duracaoDia)));
      trajeto.add(new Local(
          locaisLazer.get((int) (Math.random() * locaisLazer.size())),
          (int) (0.15 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.3 * duracaoDia)));
      trajetosAdulto.add(trajeto);
    }
      break;

    case 3: {
      // Casa -> Trabalho -> Estudo -> Lazer -> Casa (0.1, 0.35, 0.2, 0.1,
      // 0.25)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.1 * duracaoDia)));
      trajeto.add(new Local(
          locaisTrabalho.get((int) (Math.random() * locaisTrabalho.size())),
          (int) (0.35 * duracaoDia)));
      trajeto.add(new Local(
          locaisEstudo.get((int) (Math.random() * locaisEstudo.size())),
          (int) (0.2 * duracaoDia)));
      trajeto.add(new Local(
          locaisLazer.get((int) (Math.random() * locaisLazer.size())),
          (int) (0.1 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.25 * duracaoDia)));
      trajetosAdulto.add(trajeto);
    }
      break;

    case 4: {
      // Casa -> Trabalho -> Lazer -> Estudo -> Casa (0.1, 0.35, 0.1, 0.2,
      // 0.25)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.1 * duracaoDia)));
      trajeto.add(new Local(
          locaisTrabalho.get((int) (Math.random() * locaisTrabalho.size())),
          (int) (0.35 * duracaoDia)));
      trajeto.add(new Local(
          locaisLazer.get((int) (Math.random() * locaisLazer.size())),
          (int) (0.1 * duracaoDia)));
      trajeto.add(new Local(
          locaisEstudo.get((int) (Math.random() * locaisEstudo.size())),
          (int) (0.2 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.25 * duracaoDia)));
      trajetosAdulto.add(trajeto);
    }
      break;
    }

    // TRAJETOS PARA IDOSO
    switch ((int) (Math.random() * 3)) {
    case 0: {
      // Casa -> Trabalho -> Casa (0.3, 0.35, 0.35)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.3 * duracaoDia)));
      trajeto.add(new Local(
          locaisTrabalho.get((int) (Math.random() * locaisTrabalho.size())),
          (int) (0.35 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.35 * duracaoDia)));
      trajetosIdoso.add(trajeto);
    }
      break;

    case 1: {
      // Casa -> Lazer -> Casa (0.3, 0.4, 0.3)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.3 * duracaoDia)));
      trajeto.add(new Local(
          locaisLazer.get((int) (Math.random() * locaisLazer.size())),
          (int) (0.4 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.3 * duracaoDia)));
      trajetosIdoso.add(trajeto);
    }
      break;

    case 2: {
      // Casa -> Trabalho -> Lazer -> Casa (0.2, 0.35, 0.1, 0.35)
      trajeto = new ArrayList<>();
      trajeto.add(new Local(casa, (int) (0.2 * duracaoDia)));
      trajeto.add(new Local(
          locaisTrabalho.get((int) (Math.random() * locaisTrabalho.size())),
          (int) (0.35 * duracaoDia)));
      trajeto.add(new Local(
          locaisLazer.get((int) (Math.random() * locaisLazer.size())),
          (int) (0.1 * duracaoDia)));
      trajeto.add(new Local(casa, (int) (0.35 * duracaoDia)));
      trajetosIdoso.add(trajeto);
    }
      break;
    }
  }
}
