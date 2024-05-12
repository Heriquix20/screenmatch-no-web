package br.com.hcgv.screenmatch.controler;

import br.com.hcgv.screenmatch.dto.EpisodioDTO;
import br.com.hcgv.screenmatch.dto.SerieDTO;
import br.com.hcgv.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// SEMPRE USAR OS {} PARA UMA PATH VARIABLE!!!

@RestController // CLASSE CONTROLADORA REST
@RequestMapping("/series") // QUANDO DIGITAR NA WEB VAI EXECUTAR O METODO (ESTE E O PRINCIPAL)
public class SerieControler {

    @Autowired
    private SerieService servico;  //METODO DO SERVICE QUE CHAMAMOS

    @GetMapping // JA DECLARADO COMO PRINCIPAL NO INICIO
    public List<SerieDTO> obterSeries() {
        return servico.obterTodasAsSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obterTop5() {
        return servico.obterTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamentos() {
        return servico.obterLancamentos();
    }

    @GetMapping("/{id}")  // NUMERO ALEATORIO DEFINIDO PELO USUARIO
    public SerieDTO obterPorId(@PathVariable Long id) {  // PATH VARABLE QUE TA VINDO NO CABEÇALHO
        return servico.obterPorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")  // URL PARA PEGAR TODOS OS EPS DE TODAS AS TEMPORADAS
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id) {
        return servico.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")  // URL PARA PEGAR TODOS OS EPS DE UMA TEMPORADA
    public List<EpisodioDTO> obterTodosEpisodiosPorTemporada(@PathVariable Long id, @PathVariable Long numero) {
        return servico.obterTodosEpsPortTemporada(id, numero);
    }

    @GetMapping("/categoria/{categoria}") // PEGAR SERIES PELA CATEGORIA
    public List<SerieDTO> obterSeriesPorCategoria(@PathVariable String categoria) {
        return servico.obterSeriesPorCategoria(categoria);
    }


}
