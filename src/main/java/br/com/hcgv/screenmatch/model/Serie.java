package br.com.hcgv.screenmatch.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity     // SINALIZANDO QUE É UMA ENTIDADE DA TABELA
@Table(name = "series")  // MUDADNDO O NOME DA TABELA
public class Serie {
   // @Column(name = "nomeDaSerie")  // MUDAR O NOME DOS ATRIBUTOS

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // GERANDO O ID
    private long id;

    @Column(unique = true)  // INDICANDO QUE NAO PODE REPETIR A MESMA SERIE
    private String titulo;

    private Integer totalTemporadas;
    private Double avaliacao;
    private String ano;
    private String atores;
    private String urlPoster;

    @Enumerated(EnumType.STRING)  // INDICANDO O TIPO DO ENUM NA TABELA
    private Categoria genero;

    private String sinopse;

    @Transient  // TRANSIENT PARA IGNORAR ATRIBUTO
    private List<Episodio> episodios = new ArrayList<>();

    // CONSTRUTOR PADRAO
    public Serie(){}

    // CONTRUTOR PARA TRANSFORMAR OS DADOS VINDOS DA SERIE RECORD
    public Serie(DadosSerie serieRecord) {
        this.titulo = serieRecord.titulo();
        this.totalTemporadas = serieRecord.totalTemporadas();
        // TRANSFORMANDO A STRING EM DOUBLE
        this.avaliacao = OptionalDouble.of(Double.valueOf(serieRecord.avaliacao())).orElse(0);
        // USANDO O SPLIT PARA PEGAR A PRIMEIRA PALAVRA E O .trim QUE PEGA SO OS CARACTERES
        this.genero = Categoria.fromString(serieRecord.genero().split(",")[0].trim());
        this.ano = serieRecord.ano();
        this.urlPoster = serieRecord.urlPoster();
        this.atores = serieRecord.atores();
        this.sinopse = serieRecord.sinopse();
    }

    @Override
    public String toString() {
        return  " - Gênero = " + genero +
                " - Titulo = " + titulo +
                " - Temporadas = " + totalTemporadas +
                " - Avaliacao = " + avaliacao +
                "-  Ano de Lançamento = " + ano +
                " - Atores = " + atores +
                " - Url do Poster = " + urlPoster +
                " - Sinopse = " + sinopse ;
    }


    public List<Episodio> getEpisodios() {
        return episodios;
    }
    public void setEpisodios(List<Episodio> episodios) {
        this.episodios = episodios;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getUrlPoster() {
        return urlPoster;
    }

    public void setUrlPoster(String urlPoster) {
        this.urlPoster = urlPoster;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }
}
