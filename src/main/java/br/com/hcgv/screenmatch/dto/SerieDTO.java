package br.com.hcgv.screenmatch.dto;

import br.com.hcgv.screenmatch.model.Categoria;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record SerieDTO(Long id,
                       String titulo,
                       Integer totalTemporadas,
                       Double avaliacao,
                       Categoria genero,
                       String atores,
                       String urlPoster,
                       String sinopse) {

}

