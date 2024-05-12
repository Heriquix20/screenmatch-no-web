package br.com.hcgv.screenmatch.principal;

import br.com.hcgv.screenmatch.model.*;
import br.com.hcgv.screenmatch.repository.SerieRepository;
import br.com.hcgv.screenmatch.service.ConsumoApi;
import br.com.hcgv.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private SerieRepository repositorio;
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBusca;
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=" + System.getenv("API_KEY_OMDB");  // USAR A VARIAVEL DE AMBIENTE
    private List<DadosSerie> dadosSeries = new ArrayList<>();


    public Principal(SerieRepository repositorio) {  // CONSTRUTOR PARA PASSA O REPOSITORY NA CLASSE PRINCIPAL E PODER USAR
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    \n1 - Buscar séries
                    2 - Buscar episódios de uma série
                    3 - Listar séries buscadas
                    4 - Buscar séries por nome
                    5 - Buscar séries por ator
                    6 - Top 5 séries
                    7 - Buscar séries por categoria
                    8 - Filtrar séries por número de temporadas
                    9 - Buscar episódios pelo título
                    10 - Top 5 episódios por série
                    11 - Buscar episódios a partir de uma data
                                    
                    0 - Sair                                 
                    """;

            System.out.println("\n\n********** MENU SCREEN MATCH **************");
            System.out.println(menu);
            System.out.println("*******************************************");
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTopSeries();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    buscarSeriePorNumeroTemporada();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosPorData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }


// METODOS DOS CASES

    // BUSCAR UMA SÉRIE E GUARDAR NO BANCO
    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie);  // SALVAR A SERIE NO BANCO
        System.out.println(dados);
    }

    // PEGAR OS DADOS DE UMA SÉRIE E RETORNAR
    private DadosSerie getDadosSerie() {
        System.out.println("\n\nDigite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        DadosSerie dados = null;
        try {
            var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
            dados = conversor.obterDados(json, DadosSerie.class);
        } catch (Exception e) {
            System.out.println("Série não encontrada! Verifique o nome da série digitada novamente");
        }
        return dados;
    }

    // BUSCAR OS EPISODIOS DE UMA SERIE
    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();  // USAR O METODO PARA LISTAR AS SERIES JA GUARDADAS NO BANCO
        System.out.println("\n\nEscolha uma série pelo nome");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie); // OPTIONAL PARA VER SE EXISTE A SERIE NO BANCO

        if (serie.isPresent()) {   // SE ESTIVER PRESENTE
            var serieEncontrada = serie.get();  // ARMAZENA A SERIE ENCONTRADA

            List<DadosTemporada> temporadas = new ArrayList<>();  // CRIA UMA LISTA DE TEMPORADAS
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {  // LAÇO PARA IR ADICIONANDO OS EPS EM SUAS DEVIDAS TEMPORADAS
                // PEGAMOS O JSON
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class); // CONVERTEMOS EM TEMPORADA
                temporadas.add(dadosTemporada);  // ADICIONANDO NA LISTA DE TEMPORADA
            }
            temporadas.forEach(System.out::println); // IMPRIMIMOS AS TEMPORADAS E SEUS EPS (JA QUE SAO ATRIBUTOS DA TEMPORADA)

            // LISTA DE EPISODIOS PARA SER ADICIONADA NO BANCO
            List<Episodio> episodios = temporadas.stream() // FAZEMOS UMA LISTA DE EPISODIOS
                    .flatMap(d -> d.episodios().stream() // ACESSANDO OS EPISODIOS DA TEMPORADA
                            .map(e -> new Episodio(d.numero(), e)))  // MAPEANDO PARA CADA EP SER CRIADO UM EPISODIO
                    .collect(Collectors.toList());  // ADICIONANDO A LISTA
            serieEncontrada.setEpisodios(episodios);  // PEGAMOS A SERIE ENCONTRADA E SETAMOS OS EPISODIOS
            repositorio.save(serieEncontrada);  // ADICIONAMOS NO BANCO DE DADOS
        } else {
            System.out.println("\n\nSérie não encontrada!!!");  // SE NAO ENCONTRA A SERIE
        }
    }

    // LISTAR TODAS AS SERIES BUSCADAS QUE ESTAO NO BANCO
    private void listarSeriesBuscadas() {
        series = repositorio.findAll();  // VAI PEGAR AS SERIES GUARDADAS NO BANCO E DEVOLVER UMA LIST
        series.stream()
                .sorted(Comparator.comparing(Serie::getTitulo))
                .forEach(System.out::println);
    }

    // BUSCAR SERIE PELO TITULO
    private void buscarSeriePorTitulo() {
        System.out.println("\n\nEscolha uma série pelo nome");
        var nomeSerie = leitura.nextLine();
        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);  // ENCONTRAR POR UM TRECHO DO TITULO

        if (serieBusca.isPresent()) {  // SE ENCONTRAR
            System.out.println("\nDados da série: " + serieBusca.get());
        } else {  // SE NAO ENCONTRAR
            System.out.println("\nSérie não encontrada!");
        }
    }

    // BUSCAR SERIE PELO ATOR
    private void buscarSeriePorAtor() {
        System.out.println("Digite o nome do ator");
        var nomeAtor = leitura.nextLine();
        System.out.println("\nAvaliações a partir de que valor?");
        var avaliacao = leitura.nextDouble();
        // METODO PARA PEGAR TODAS SERIES DE UM ATOR E UMA AVALIACAO FORNECIDA PELO USUARIO
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("\n\nSéries em que " + nomeAtor + " trabalhou: ");
        seriesEncontradas.forEach(s -> System.out.println
                ("\nNome: " + s.getTitulo() + "\n Avaliação: " + s.getAvaliacao()));
    }

    // PEGAR TOP 5 SERIES
    private void buscarTopSeries() {
        List<Serie> seriesTop = repositorio.findTop5ByOrderByAvaliacaoDesc();  // METODO PARA PEGAR AS 5 MELHORES
        seriesTop.forEach(s -> System.out.println
                ("\nNome: " + s.getTitulo() + "\n Avaliação: " + s.getAvaliacao()));
    }

    // BUSCAR UMA SERIE PELA CATEGORIA
    private void buscarSeriesPorCategoria() {
        System.out.println("\nInforme a categoria da série: ");
        var nomeCategoria = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeCategoria);  // PEGANDO O NOME E COLOCANDO NO METODO DA CATEGORIA PARA SER COMPARADO
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);  // ENCONTRAR PELA CATEGORIA
        System.out.println("\nSéries encontradas no gênero " + nomeCategoria + ":");
        seriesPorCategoria.forEach(System.out::println);
    }

    // BUSCAR SERIE ATE UM LIMITE DE TEMPORADAS
    private void buscarSeriePorNumeroTemporada() {
        System.out.println("\nInforme o número máximo de temporadas: ");
        var totalTemporadas = leitura.nextInt();
        System.out.println("\nA partir de qual avaliação?");
        var avaliacao = leitura.nextDouble();
        // METODO PARA FILTRAR PELA TEMPORADA E AVALIACAO
        List<Serie> seriesPorTemporada = repositorio.seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        System.out.println("\nSéries encontradas com no máximo " + totalTemporadas + " temporadas ou menor:");
        seriesPorTemporada.forEach(s ->
                System.out.println("\nNome: " + s.getTitulo() +
                        "\nAvaliação: " + s.getAvaliacao() + "\nTemporadas: " + s.getTotalTemporadas()));
    }

    // BUSCAR UM EPISODIO POR UM TRECHO DO NOME
    private void buscarEpisodioPorTrecho(){
        System.out.println("\nQual o nome do episódio?");
        var nomeEpisodio = leitura.nextLine();
        List<Episodio> episodiosPorTrecho = repositorio.episodiosPorTrecho(nomeEpisodio); // METODO PARA PEGAR EPS POR TRECHO
        System.out.println("\nEpisódios encontrados: ");
        episodiosPorTrecho.forEach(e ->
                System.out.printf("\nSérie: %s Temporada %s - Episódio %s - %s\n",  // FORMATACAO PARA IMPRIMIR VARIAVEIS
                        e.getSerie().getTitulo(), e.getTemporada(),
                        e.getNumeroEpisodio(), e.getTitulo()));

    }

    // PEGAR TOP 5 EPISODIOS POR SERIE
    private void topEpisodiosPorSerie() {
        buscarSeriePorTitulo();  // BUSCAMOS DETERMINADA SERIE UTILIZANDO O METODO
        if (serieBusca.isPresent()) { // SE ENCONTRAR
            Serie serie = serieBusca.get();  // ARMAZENAMOS EM 'SERIE' A SERIE ENCONTRADA
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie); // METODO PARA PEGAR TOP EPISODIOS
            topEpisodios.forEach(e ->
                    System.out.printf("\nSérie: %s Temporada %s - Episódio %s - %s\n",  // IMPRIMINDO PELA FORMMATACAO
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));
        }
    }

    // BUSCAR EPISODIOS POR TAL ANO
    private void buscarEpisodiosPorData() {
        buscarSeriePorTitulo();   // BUSCAMOS DETERMINADA SERIE UTILIZANDO O METODO
        if (serieBusca.isPresent()) {  // SE ENCONTRAR
            Serie serie = serieBusca.get();   // ARMAZENAMOS EM 'SERIE' A SERIE ENCONTRADA
            System.out.println("\nDigite o ano limite de lançamento:");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();  // QUEBRAR SCANNER
            List<Episodio> episodiosAno = repositorio.episodiosPorSerieEAno(serie, anoLancamento);  // METODO PARA FILTRAR AS SERIES PELO ANO
            episodiosAno.forEach(System.out::println);
        }
    }
}

























//        System.out.println("Digite o nome da Série: ");
//        var nomeSerie = scanner.nextLine();
//        var json = consumer.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY); // pegar o json do endereco
//
//        Serie serie = convert.obterDados(json, Serie.class);  // converter o json em serie class
//        System.out.println(serie);
//        System.out.println("\n");
//
//        List<Temporada> temporadas = new ArrayList<>();
//
//		for (int i = 1 ; i <= serie.totalTemporadas(); i++) {
//			json = consumer.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY); // pegar os dados das temporadas
//			Temporada temp = convert.obterDados(json, Temporada.class); // converter em temporada class
//			temporadas.add(temp);  // adicionar as temps na lista de temporadas
//		}
//		temporadas.forEach(System.out::println);  // imprimir as temps
//        System.out.println("\n");
//
//        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo()))); // imprimir titulos de todos os eps da serie
//        System.out.println("\n");
//
//
//
//        // FAZENDO UMA LISTA DE TODOS OS EPISODIOS
//        List<Episodes> episodiosAll = temporadas.stream() //começando o stream
//                .flatMap(t -> t.episodios().stream()) // pega todos os episodios de uma temporada
//                .collect(Collectors.toList()); // coleta todos os dados para nova lista
//
//        // PEGANDO TOP 10 EPISODIOS DA SERIE PELA AVALIACAO
//        System.out.println("Top 10 episódios:");
//        episodiosAll.stream()
//                .filter(e ->!e.avaliacao().equalsIgnoreCase("N/A")) // tirou todos as avaliacoes n/a
//                .peek(e -> System.out.println("Primeiro filtro -- " + e))  // debug do stream
//                .sorted(Comparator.comparing(Episodes::avaliacao).reversed())  // ordenou pela avaliacao e dps deu um revert
//                .peek(e -> System.out.println("Ordenando -- " + e)) // debug do stream
//                .limit(10)  // no maximo 5 eps
//                .peek(e -> System.out.println("Limite -- " + e)) // debug do stream
//                .map(e -> e.titulo().toUpperCase()) // colocar em letra maiuscula
//                .peek(e -> System.out.println("Mapeando -- " + e)) // debug do stream
//                .forEach(System.out::println);  // imprimi tudo
//        System.out.println("\n");
//
//
//        List<EpNegocio> episodesN = temporadas.stream()  // pega a temporada
//               .flatMap(t -> t.episodios().stream()  // pega todos os episodios da temporada
//                    .map(en -> new EpNegocio(t.numero(), en))  // transformar o dado em outro pegando a temporada
//                )     .collect(Collectors.toList());  // adiciona a lista
//        episodesN.forEach(System.out::println);
//
//        // PEGANDO O NOME DE UM EPISODIO
//        System.out.println("Digite o nome do episódio:");
//        var trechoTitulo = scanner.nextLine();
//        Optional<EpNegocio> episodioBuscado  = episodesN.stream()  // variavel para armazenar um episodio encontrado
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))  // contains para ver se contem um trecho do titulo
//                .findFirst(); // encontrar o titulo
//
//        if (episodioBuscado.isPresent()) {
//            System.out.println("Episódio encontrado!!");
//            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
//       } else {
//            System.out.println("Episódio não encontrado...");
//        }
//
//        // PEGANDO O EPISODIO POR DATAS
//        System.out.println("A partir de que ano você deseja ver o episódio?");
//        var ano =  scanner.nextInt();
//        scanner.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);  // pegar o ano e fazer uma local date
//        DateTimeFormatter formatadorDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");  // instaciar o formatador e colocar de acordo com a data brasileira
//        episodesN.stream()
//               .filter(en -> en.getDataDeLancamento() != null && en.getDataDeLancamento().isAfter(dataBusca)) // filtrar para tirar os sem datas e datas depois da informada
//                .forEach(en -> System.out.println(
//                       "Temporada: " + en.getTemporada() +
//                                     " - Episódio: " + en.getTitulo()+
//                                " - Data de lançamento: " + en.getDataDeLancamento().format(formatadorDate)  // passamos o formatador
//               ));
//
//
//        // PEGANDO A MEDIA DE AVALIACOES POR TEMPORADA
//        Map<Integer, Double> avaliacoesTemporada = episodesN.stream()  // map para mapear cada ep e temp
//                .filter(en -> en.getAvaliacao()>0.0)
//                .collect(Collectors.groupingBy(EpNegocio::getTemporada,  // usamos collect para pegar a temp dos eps
//                        Collectors.averagingDouble(EpNegocio::getAvaliacao))); // collect novamente para pegar media de avaliacoes
//        System.out.println(avaliacoesTemporada);
//
//        // PEGANDO MAIS DADOS COM DOUBLE SUMMARY ESTATISTICS
//        DoubleSummaryStatistics est = episodesN.stream()
//                .filter(en -> en.getAvaliacao()>0.0)
//                .collect(Collectors.summarizingDouble(EpNegocio::getAvaliacao));
//
//        System.out.println("Média: " + est.getAverage());
//        System.out.println("Melhor : " + est.getMax());
//        System.out.println("Pior: " + est.getMin());
//        System.out.println("Quantidade: " + est.getCount());
//



