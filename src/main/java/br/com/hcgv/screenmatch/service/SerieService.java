package br.com.hcgv.screenmatch.service;

import br.com.hcgv.screenmatch.dto.EpisodioDTO;
import br.com.hcgv.screenmatch.dto.SerieDTO;
import br.com.hcgv.screenmatch.model.Categoria;
import br.com.hcgv.screenmatch.model.Serie;
import br.com.hcgv.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service  // PARA IDENTIFICAR QUE E DE SERVIÇO

public class SerieService {

    @Autowired
    private SerieRepository repositorio;


    public List<SerieDTO> obterTodasAsSeries() {
        return converteDados(repositorio.findAll());  // CONVERTER USANDO METODO
    }

    public List<SerieDTO> obterTop5Series() {
        return converteDados(repositorio.findTop5ByOrderByAvaliacaoDesc());  // CONVERTER USANDO METODO
    }

    // TRANFORMAS SERIES EM SERIES DTO PARA IR PRO CONTROLLER
    private List<SerieDTO> converteDados(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(s.getId(),s.getTitulo(),s.getTotalTemporadas(),
                        s.getAvaliacao(),s.getGenero(),s.getAtores(),s.getUrlPoster(),s.getSinopse()))
                .collect(Collectors.toList());


    }

    // PEGAR SERIES COM BASE NA DATA PEGAR OS LANCAMENTOS
    public List<SerieDTO> obterLancamentos() {
        return converteDados(repositorio.lancamentosMaisRecentes());
    }


    // PEGAR A SERIE POR ID PARA APARECER AS INFORMAÇOES
    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serie = repositorio.findById(id); // COMO RETORNA UM OPTIONAL NOS CRIAMOS UM
        if (serie.isPresent()) {  // ANALISAMOS SE ESTA PRESENTE OU NAO
            Serie s = serie.get();  // INTANCIAMOS A SERIE BUSCADA
            return new SerieDTO(s.getId(),s.getTitulo(),s.getTotalTemporadas(),
                    s.getAvaliacao(),s.getGenero(),s.getAtores(),s.getUrlPoster(),s.getSinopse());
        } else {
            return null;
        }
    }

    // PEGAR TODOS EPS E TEMPORADAS
    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = repositorio.findById(id); // COMO RETORNA UM OPTIONAL NOS CRIAMOS UM

        if (serie.isPresent()) {  // ANALISAMOS SE ESTA PRESENTE OU NAO
            Serie s = serie.get();  // INTANCIAMOS A SERIE BUSCADA
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    // PEGAR TODOS EPS DE UMA CERTA TEMPORADA
    public List<EpisodioDTO> obterTodosEpsPortTemporada(Long id, Long numero) {
        return repositorio.obterEpisodiosPorTemporada(id, numero)
                .stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterSeriesPorCategoria(String categoria) {
        Categoria categorias = Categoria.fromPortugues(categoria); // PASSAR A CATEGORIA E COMPARAR COM OS QUE EXISTEM NO ENUM
        return converteDados(repositorio.findByGenero(categorias)); // CONVERTE A LISTA DE SERIE EM LISTA DE SERIE DTO
    }


//        METODO USANDO STREAMS
//        Optional<Serie> serie = repositorio.findById(id); // COMO RETORNA UM OPTIONAL NOS CRIAMOS UM
//
//        if (serie.isPresent()) {  // ANALISAMOS SE ESTA PRESENTE OU NAO
//            Serie s = serie.get();  // INTANCIAMOS A SERIE BUSCADA
//            return s.getEpisodios().stream()
//                    .filter(e -> e.getTemporada() == temporada)
//                    .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
//                    .collect(Collectors.toList());
//        }
//        return null;

}

