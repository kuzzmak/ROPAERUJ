package hr.fer.zemris.optjava.rng;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class RNG {

	private static IRNGProvider rngProvider;

	static {

		Properties properties = new Properties();
		FileInputStream inputStream;

		try {
			
			String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			
			String fileName = "rng-config.properties";
			
			inputStream = new FileInputStream(rootPath + fileName);
			properties.load(inputStream);

			String className = properties.getProperty("rng-provider");

			RNG.rngProvider = (IRNGProvider) Class.forName(className).newInstance();

		} catch (IOException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}

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
