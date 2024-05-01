package br.com.hcgv.screenmatch.principal;


import br.com.hcgv.screenmatch.model.EpNegocio;
import br.com.hcgv.screenmatch.model.Episodes;
import br.com.hcgv.screenmatch.model.Serie;
import br.com.hcgv.screenmatch.model.Temporada;
import br.com.hcgv.screenmatch.service.ConsumerApi;
import br.com.hcgv.screenmatch.service.ConvertJson;
import net.bytebuddy.asm.Advice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private ConvertJson convert = new ConvertJson();
    private ConsumerApi consumer = new ConsumerApi();
    private Scanner scanner = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=e3a4fbf1";

    public void exibirMenu() {

        System.out.println("Digite o nome da Série: ");
        var nomeSerie = scanner.nextLine();
        var json = consumer.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY); // pegar o json do endereco

        Serie serie = convert.obterDados(json, Serie.class);  // converter o json em serie class
        System.out.println(serie);
        System.out.println("\n");

        List<Temporada> temporadas = new ArrayList<>();

		for (int i = 1 ; i <= serie.totalTemporadas(); i++) {
			json = consumer.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY); // pegar os dados das temporadas
			Temporada temp = convert.obterDados(json, Temporada.class); // converter em temporada class
			temporadas.add(temp);  // adicionar as temps na lista de temporadas
		}
		temporadas.forEach(System.out::println);  // imprimir as temps
        System.out.println("\n");

//        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo()))); // imprimir titulos de todos os eps da serie
//        System.out.println("\n");

       // for (int i = 0; i < serie.totalTemporadas(); i++) {
       //     List<Episodes> episodesTemporada = temporadas.get(i).episodios();
       //     for (int j = 0; j < episodesTemporada.size(); j++) {
       //        System.out.println(episodesTemporada.get(j).titulo());
       //     }
       // }

        // FAZENDO UMA LISTA DE TODOS OS EPISODIOS
        List<Episodes> episodiosAll = temporadas.stream() //começando o stream
                .flatMap(t -> t.episodios().stream()) // pega todos os episodios de uma temporada
                .collect(Collectors.toList()); // coleta todos os dados para nova lista

        // PEGANDO TOP 10 EPISODIOS DA SERIE PELA AVALIACAO
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


        // FAZENDO UMA LISTA MAIS ROBUSTA DE EPS DESSA VEZ COM A TEMPORADA
        List<EpNegocio> episodesN = temporadas.stream()  // pega a temporada
               .flatMap(t -> t.episodios().stream()  // pega todos os episodios da temporada
                    .map(en -> new EpNegocio(t.numero(), en))  // transformar o dado em outro pegando a temporada
                )     .collect(Collectors.toList());  // adiciona a lista
        episodesN.forEach(System.out::println);

        // PEGANDO O NOME DE UM EPISODIO
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

        // PEGANDO O EPISODIO POR DATAS
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


        // PEGANDO A MEDIA DE AVALIACOES POR TEMPORADA
        Map<Integer, Double> avaliacoesTemporada = episodesN.stream()  // map para mapear cada ep e temp
                .filter(en -> en.getAvaliacao()>0.0)
                .collect(Collectors.groupingBy(EpNegocio::getTemporada,  // usamos collect para pegar a temp dos eps
                        Collectors.averagingDouble(EpNegocio::getAvaliacao))); // collect novamente para pegar media de avaliacoes
        System.out.println(avaliacoesTemporada);

        // PEGANDO MAIS DADOS COM DOUBLE SUMMARY ESTATISTICS
        DoubleSummaryStatistics est = episodesN.stream()
                .filter(en -> en.getAvaliacao()>0.0)
                .collect(Collectors.summarizingDouble(EpNegocio::getAvaliacao));

        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor : " + est.getMax());
        System.out.println("Pior: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());

    }
}
