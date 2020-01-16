package hr.fer.zemris.optjava.rng;

import java.util.Properties;

public class RNG {

	private static IRNGProvider rngProvider;

	static {
		
		Properties prop = new Properties();
		// Stvorite primjerak razreda Properties;
		// Nad Classloaderom razreda RNG tražite InputStream prema resursu
		// rng-config.properties
		// recite stvorenom objektu razreda Properties da se učita podatcima iz tog
		// streama.
		// Dohvatite ime razreda pridruženo ključu "rng-provider"; zatražite Classloader
		// razreda
		// RNG da učita razred takvog imena i nad dobivenim razredom pozovite metodu
		// newInstance()
		// kako biste dobili jedan primjerak tog razreda; castajte ga u IRNGProvider i
		// zapamtite.
	}

	public static IRNG getRNG() {
		return rngProvider.getRNG();
	}
}
