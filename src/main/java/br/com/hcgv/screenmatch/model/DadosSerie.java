package br.com.hcgv.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)  // ignorar tudo que nao for atributos

public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer totalTemporadas,
                         @JsonAlias("imdbRating") String avaliacao,
                         @JsonAlias("Year") String ano,
                         @JsonAlias("Actors") String atores,
                         @JsonAlias("Poster") String urlPoster,
                         @JsonAlias("Genre") String genero,
                         @JsonAlias("Plot") String sinopse) {
    @Override
    public String toString() {
        return " - Gênero = " + genero +
                " - Titulo = " + titulo +
                " - Temporadas = " + totalTemporadas +
                " - Avaliacao = " + avaliacao +
                "-  Ano de Lançamento = " + ano +
                " - Atores = " + atores +
                " - Url do Poster = " + urlPoster +
                " - Sinopse = " + sinopse ;
    }
}

//@JsonAlias e @JsonProperty são anotações em Jackson,
// uma biblioteca Java para processar JSON,
// que ajudam a mapear propriedades de classe para campos JSON.
