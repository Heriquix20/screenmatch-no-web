package br.com.hcgv.screenmatch;


import br.com.hcgv.screenmatch.principal.Principal;
import br.com.hcgv.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	@Autowired  // INJETAR DEPENDENCIAS
	private SerieRepository repositorio;  // INSTANCIEI O REPOSITORIO NA CLASSE QUE O SPRING TEM PODER

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repositorio);
		principal.exibeMenu();



	}
}
