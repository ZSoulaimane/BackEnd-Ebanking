package ma.ensa.banki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankiApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(BankiApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
