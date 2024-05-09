package br.com.hcgv.screenmatch.model;


import java.util.OptionalDouble;

public class Serie {

    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    private String ano;
    private String atores;
    private String urlPoster;
    private Categoria genero;
    private String sinopse;

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
