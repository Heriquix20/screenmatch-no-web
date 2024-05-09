package br.com.hcgv.screenmatch.repository;

import br.com.hcgv.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;


// PEGANDO O TIPO DA ENTIDADE QUE E SERIE, E QUAL E O TIPO DO ID QUAL A CHAVE PRIMARIA DA SERIE
public interface SerieRepository extends JpaRepository<Serie, Long> {
}
