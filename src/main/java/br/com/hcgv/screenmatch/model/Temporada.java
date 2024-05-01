package br.com.hcgv.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public record Temporada(@JsonAlias("Season") Integer numero,
                        @JsonAlias("Episodes") List<Episodes> episodios) {
}
