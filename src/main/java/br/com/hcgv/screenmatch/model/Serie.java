package br.com.hcgv.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)  // ignorar tudo que nao for atributos

public record Serie(@JsonAlias("Title") String titulo,
                    @JsonAlias("totalSeasons") Integer totalTemporadas,
                    @JsonAlias("imdbRating") String avaliacao) {
}

//@JsonAlias e @JsonProperty são anotações em Jackson,
// uma biblioteca Java para processar JSON,
// que ajudam a mapear propriedades de classe para campos JSON.
