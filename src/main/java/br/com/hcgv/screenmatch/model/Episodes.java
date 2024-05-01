package br.com.hcgv.screenmatch.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public record Episodes(@JsonAlias("Title") String titulo,
                       @JsonAlias("Episode")Integer numeroDoEpisode,
                       @JsonAlias("imdbRating")String avaliacao,
                       @JsonAlias("Released")String dataDeLancamento) {
}

