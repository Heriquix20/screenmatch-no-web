package br.com.hcgv.screenmatch;

import br.com.hcgv.screenmatch.model.Serie;
import br.com.hcgv.screenmatch.service.ConsumerApi;
import br.com.hcgv.screenmatch.service.ConvertJson;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumer = new ConsumerApi();
		var json = consumer.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=e3a4fbf1");
		System.out.println(json);

		ConvertJson convert = new ConvertJson();
		Serie serie = convert.obterDados(json, Serie.class);  //convertendo o json em Serie class
		System.out.println(serie);

	}
}
