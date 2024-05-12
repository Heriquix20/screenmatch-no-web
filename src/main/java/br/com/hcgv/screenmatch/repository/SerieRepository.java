package br.com.hcgv.screenmatch.repository;

import br.com.hcgv.screenmatch.model.Categoria;
import br.com.hcgv.screenmatch.model.Episodio;
import br.com.hcgv.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


// PEGANDO O TIPO DA ENTIDADE QUE E SERIE, E QUAL E O TIPO DO ID QUAL A CHAVE PRIMARIA DA SERIE
public interface SerieRepository extends JpaRepository<Serie, Long> {

    // METODO PARA BUSCAR UMA SERIE PELO TITULO
    Optional<Serie> findByTituloContainingIgnoreCase(String nome);

    // METODO PARA BUSCAR UMA SERIE QUE CONTENHA UM CERTO ATOR
    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

    // PEGAR AS TOP 5 SERIES PELA AVALIACAO
    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    // PEGAR SERIE PELA CATEGORIA
    List<Serie> findByGenero(Categoria categoria);

    // PEGAR SERIE PELO MAXIMO DE TEMPORADAS E AVALIACAO
    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(int totalTemporadas, Double avaliacao);

    // USANDO A QUERY DO JPQL PARA BUSCAR SERIE PELO MAXIMO DE TEMPORADAS E AVALIACAO
    @Query("select s from Serie s where s.totalTemporadas <= :totalTemporadas and s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAvaliacao(int totalTemporadas, Double avaliacao);

    // USANDO A QUERY DO JPQL PARA BUSCAR EPISODIO PELO NOME
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nomeEpisodio%")
    List<Episodio> episodiosPorTrecho(String nomeEpisodio);

    // USANDO A QUERY DO JPQL PARA PEGAR TOP 5 EPISODIOS
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    // USANDO A QUERY DO JPQL PARA PEGAR UMA SÉRIE PELO ANO
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);

    // PEGAR LANCAMENTOS MAIS RECENTES
    @Query("SELECT s FROM Serie s " +
            "JOIN s.episodios e " +
            "GROUP BY s " +
            "ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")
    List<Serie> lancamentosMaisRecentes();

    // OBTER EPS POR TEMPORADA
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numero")
    List<Episodio> obterEpisodiosPorTemporada(Long id, Long numero);
}
