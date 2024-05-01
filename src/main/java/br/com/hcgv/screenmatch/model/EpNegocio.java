package br.com.hcgv.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EpNegocio {

    private Integer temporada;
    private String titulo;
    private Integer numeroDoEpisode;
    private double avaliacao;
    private LocalDate dataDeLancamento;

    public EpNegocio(Integer numeroTemporada, Episodes episodes) {
        this.temporada = numeroTemporada;
        this.titulo = episodes.titulo();
        this.numeroDoEpisode = episodes.numeroDoEpisode();
        try {
            this.avaliacao = Double.valueOf(episodes.avaliacao());
        } catch (NumberFormatException ex) {
            this.avaliacao = 0.0;
        }
        try {
            this.dataDeLancamento = LocalDate.parse(episodes.dataDeLancamento());
        } catch (DateTimeParseException date) {
            this.dataDeLancamento = null;
        }
    }

    @Override
    public String toString() {
        return "temporada=" + temporada +
                "- titulo='" + titulo  +
                "- numeroDoEpisode=" + numeroDoEpisode +
                "- avaliacao=" + avaliacao +
                "- dataDeLancamento=" + dataDeLancamento;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroDoEpisode() {
        return numeroDoEpisode;
    }

    public void setNumeroDoEpisode(Integer numeroDoEpisode) {
        this.numeroDoEpisode = numeroDoEpisode;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataDeLancamento() {
        return dataDeLancamento;
    }

    public void setDataDeLancamento(LocalDate dataDeLancamento) {
        this.dataDeLancamento = dataDeLancamento;
    }
}
