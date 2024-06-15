package es.uma.rysd.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import es.uma.rysd.entities.Person;
import es.uma.rysd.entities.SpaceShip;
import es.uma.rysd.entities.World;

/**
 * CHANGES:<p>
 * Modified:
 * <ul>
 * 	<li>Added another condition to a loop in {@link #tallest(SWClient)} to account for the fact that the value of height might be "unknown"</li>
 * </ul>
 * Helper functions:
 * <ul>
 * 	<li>{@link #parseGravity(String)} Parses the gravity of a planet to a double, where the gravity comes in the format "0.8 standard", standard being 9.8m/s^2</li>
 * </ul>
 * New Trivia Questions:
 * <ul>
 *  <li>{@link #mostExpensiveStarship(SWClient)} Which of this two starships is the most expensive? </li>
 *  <li>{@link #howManyMovies(SWClient)} How many movies does this character appear in? </li>
 *  <li>{@link #whoWeighsMore(SWClient)} Which of this two characters weights the most (Based on mass x gravity of a random planet)? 
 * [This question is absurdly difficult, but I though it was a cool idea!] </li>
 * </ul>
 */
public class Main {
	private static Random rand; // for random numbers
	private static Scanner sc; // for reading from keyboard
	// private final static String proxy = "proxy.lcc.uma.es";
	// private final static String proxy_port = "3128";
	

	public static void main(String[] args) {
		// Uncomment the following lines if you are testing in the lab and accessing the Internet using the proxy
		// System.setProperty("https.proxyHost",proxy);
		// System.setProperty("https.proxyPort",proxy_port);

		SWClient sw = new SWClient();
		String response = null;
		rand = new Random();
		sc = new Scanner(System.in);

		do {
			// tallest(sw);
			// whoBornIn1(sw);
			// whoBornIn2(sw);
			// mostExpensiveStarship(sw);
			// howManyMovies(sw);
			whoWeighsMore(sw);
			System.out.println("Desea otra ronda (s/n)?");
			response = sc.nextLine();
		} while (response.equals("s"));
		sc.close();

	}
	
	public static void mostExpensiveStarship(SWClient sw) {
		/*
		 * 1. Get two starships
		 * 2. Ask which one is the most expensive
		 * 3. After the user answers, show the cost of both starships
		 * 4. Show if the user was right or wrong
		 */
		// Get the number of spaceships
		int max_starships = sw.countResources("starships");
		if (max_starships == 0) {
			System.out.println("No starships found.");
			return;
		}
		System.out.println("Generating new question...");
		// Picking two random starships without repeating
		List<Integer> used = new ArrayList<Integer>();
		List<SpaceShip> starships = new ArrayList<SpaceShip>();
		int counter = 0;
		while (counter < 2) {
			Integer ss = getRandomResource(max_starships, used);
			SpaceShip spaceShip = sw.getSpaceShip(sw.buildResourceUrl("starships", ss));
			if (spaceShip == null || spaceShip.cost_in_credits.equals("unknown")) {
				System.out.println("Error finding resource " + ss);
			} else {
				starships.add(spaceShip);
				counter++;
			}
			used.add(ss);
		}

		// Writing the question and reading the option
		Integer n = null;
		do {
			System.out.println("Which one is the most expensive? [0] " + starships.get(0).name + " or [1] " + starships.get(1).name);
			try {
				n = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException ex) {
				n = -1;
			}
		} while (n != 0 && n != 1);

		// Showing information about the chosen starships
		for (SpaceShip ss : starships) {
			System.out.println(ss.name + " costs " + ss.cost_in_credits + " credits");
		}

		// Result
		if (Double.parseDouble(starships.get(n).cost_in_credits) >= Double.parseDouble(starships.get((n + 1) % 2).cost_in_credits)) {
			System.out.println("Congratulations!!! " + success[rand.nextInt(success.length)]);
		} else {
			System.out.println("You failed :( " + error[rand.nextInt(error.length)]);
		}
	}

	public static void howManyMovies(SWClient sw) {
		/*
		 * 1. Get a character
		 * 2. Ask how many movies does this character appear in
		 * 3. After the user answers, show the number of movies
		 * 4. Show if the user was right or wrong
		 */
		// Getting the number of stored people
		int max_people = sw.countResources("people");
		if (max_people == 0) {
			System.out.println("No people found.");
			return;
		}

		System.out.println("Generating new question...");
		// Picking a random person
		Person person = null;
		do{
			Integer p = rand.nextInt(max_people);
			person = sw.getPerson(sw.buildResourceUrl("people", p));
			if(person == null){
				System.out.println("Error finding resource " + p);
			}
		} while (person == null || person.films == null || person.films.length == 0);
		
		// Writing the question and reading the option
		Integer n = null;
		do {
			System.out.println("How many movies does " + person.name + " appear in? [0-6]");
			try {
				n = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException ex) {
				n = -1;
			}
		} while (n < 0);

		// Showing information about the chosen character
		System.out.println(person.name + " appears in " + person.films.length + " movies");
		
		// Result
		if(person.films.length == n){
			System.out.println("Congratulations!!! "+ success[rand.nextInt(success.length)]);
		} else {
			System.out.println("You failed :( " + error[rand.nextInt(error.length)]);
		}
	}

	public static void whoWeighsMore(SWClient sw) {
		/*
		 * 1. Get two characters and two planets
		 * 2. Ask who weighs more
		 * 3. After the user answers, show the weight of the character and the gravity of the planet
		 * 4. Show if the user was right or wrong
		 */
		// Getting the number of stored people and planets
		int max_people = sw.countResources("people");
		int max_planet = sw.countResources("planets");
		if (max_people == 0 || max_planet == 0) {
			System.out.println("No people or no planets found.");
			return;
		}

		System.out.println("Generating new question...");
		// Picking two random people without repeating
		List<Integer> used = new ArrayList<Integer>();
		List<Person> people = new ArrayList<Person>();
		int counter = 0;
		while (counter < 2) {
			Integer p = getRandomResource(max_people, used);
			Person person = sw.getPerson(sw.buildResourceUrl("people", p));
			if (person == null || person.mass.equals("unknown")) {
				System.out.println("Error finding resource " + p);
			} else {
				people.add(person);
				counter++;
			}
			used.add(p);
		}
		used.clear();
		// Picking two random planets without repeating
		List<World> planets = new ArrayList<World>();
		counter = 0;
		while (counter < 2) {
			Integer p = getRandomResource(max_planet, used);
			World world = sw.getWorld(sw.buildResourceUrl("planets", p));
			if (world == null || world.gravity.equals("unknown")) {
				System.out.println("Error finding resource " + p);
			} else {
				planets.add(world);
				counter++;
			}
			used.add(p);
		}

		// Writing the question and reading the option
		Integer n = null;
		do {
			System.out.println("Who weighs more? [0] " + people.get(0).name + " in " + planets.get(0).name
				+ " or [1] " + people.get(1).name + " in " + planets.get(1).name);
			try {
				n = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException ex) {
				n = -1;
			}
		} while (n != 0 && n != 1);

		// Showing information about the chosen characters and planets
		double[] weights = new double[2];
		for (int i = 0; i < 2; i++) {
			// Calculate the weight of the person in the planet
			double mass = Double.parseDouble(people.get(i).mass);
			double gravity = parseGravity(planets.get(i).gravity);
			double weight = mass * gravity;
			System.out.println(people.get(i).name + " weighs " + weight + "N in " + planets.get(i).name);
			weights[i] = weight;
		}

		// Result
		if (weights[n] >= weights[(n + 1) % 2]) {
			System.out.println("Congratulations!!! " + success[rand.nextInt(success.length)]);
		} else {
			System.out.println("You failed :( " + error[rand.nextInt(error.length)]);
		}
	}

	/**
	 * Parses the gravity of a planet to a double
	 * @param gravity The gravity of the planet
	 * @return the gravity as a double, or -1 if it could not be parsed
	 */
	private static double parseGravity(String gravity) {
		double number = -1;
		// Gravity comes as a string in the form "1 standard" or "1.5 standard"
		// Invalid values are "unknown" or "N/A"
		String[] parts = gravity.split(" ");
		String firstPart = parts[0];
		// try to parse to double, return -1 if it fails
		try {
			number = Double.parseDouble(firstPart) * 9.8;
		} catch (NumberFormatException e) {
			System.err.println("Error parsing gravity: " + gravity);
		}
		return number;
	}

	// Generates a number between 0 and max-1 that has not been used previously (the used numbers are in l)
    public static Integer getRandomResource(int max, List<Integer> l){
    	if(max == l.size()) return null;

    	Integer p = rand.nextInt(max);
    	while(l.contains(p)){
    		p = (p+1)%max;
    	}
    	return p;
    }

	// Question that obtains two identical resources (characters in this case) and compares them
	public static void tallest(SWClient sw) {
		// Getting the number of stored people
		int max_people = sw.countResources("people");
		if (max_people == 0) {
			System.out.println("No people found.");
			return;
		}

		System.out.println("Generating new question...");
		// Picking two random people without repeating
		List<Integer> used = new ArrayList<Integer>();
		List<Person> people = new ArrayList<Person>();
		int counter = 0;
		while (counter < 2) {
			Integer p = getRandomResource(max_people, used);
			Person person = sw.getPerson(sw.buildResourceUrl("people", p));
			//! This has been modified to account for the fact that the value of height might be "unknown"
			if (person == null || person.height.equals("unknown")) {
				System.out.println("Hubo un error al encontrar el recurso " + p);
			} else {
				people.add(person);
				counter++;
			}
			used.add(p);
		}

		// Writing the question and reading the option
		Integer n = null;
		do {
			System.out.println("�Qui�n es m�s alto? [0] "+ people.get(0).name + " o [1] " + people.get(1).name);
			try {
				n = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException ex) {
				n = -1;
			}
		} while (n != 0 && n != 1);

		// Showing information about the chosen characters
		for (Person p : people) {
			System.out.println(p.name + " mide " + p.height);
		}

		// Result
		if(Double.parseDouble(people.get(n).height) >= Double.parseDouble(people.get((n+1)%2).height)){
			System.out.println("Enhorabuena!!! "+ success[rand.nextInt(success.length)]);
		} else {
			System.out.println("Fallaste :( " + error[rand.nextInt(error.length)]);
		}
	}

	// Question that relates multiple resources:
	// - Chooses a resource (planet in this case)
	// - Asks about a related resource (person who was born or created there)
	// - Searches for that resource and checks if it is related to the first one (if the searched person has the original planet)
	public static void whoBornIn1(SWClient sw) {
		// Getting the number of planets
		int max_planet = sw.countResources("planets");
		if (max_planet == 0) {
			System.out.println("No se encontraron planetas.");
			return;
		}

		System.out.println("Generando nueva pregunta...");
		// Getting the planet (that has people)
		List<Integer> used = new ArrayList<Integer>();
		World world = null;
		do {
			Integer p = getRandomResource(max_planet, used);
			world = sw.getWorld(sw.buildResourceUrl("planets", p));
			if (world == null) {
				System.out.println("Hubo un error al encontrar el recurso " + p);
			}
			used.add(p);
		} while (world == null || world.residents == null || world.residents.length < 1 || world.name.equals("unknown"));

		// Posing the question
		String s = null;
		System.out.println("�Qui�n naci� o fue creado en " + world.name + "?");
		s = sc.nextLine();
		// Searching for the indicated person
		Person p = sw.searchPersonByName(s);

		// Validating the answer and displaying their information
		if (p == null) {
			System.out.println("No hay nadie con ese nombre");
		} else {
			System.out.println(p.name + " naci� en " + p.homeplanet.name);
		}

		// Results
		if (p != null && p.homeplanet.name.equals(world.name)) {
			System.out.println("Enhorabuena!!! " + success[rand.nextInt(success.length)]);
		} else {
			System.out.println("Fallaste :( " + error[rand.nextInt(error.length)]);
		}
	}

	// Similar to the previous one but instead of asking to write, alternatives are offered:
	// - A correct person from the planet is chosen randomly from the available ones
	// - Three others that are not related to the resource are chosen randomly (the incorrect ones)
	// - The correct one is inserted randomly
	public static void whoBornIn2(SWClient sw) {

		// Getting the number of planets and people
		int max_people = sw.countResources("people");
		int max_planet = sw.countResources("planets");
		if (max_people == 0 || max_planet == 0) {
			System.out.println("No se encontraron personas o planetas.");
			return;
		}

		System.out.println("Generando nueva pregunta...");
		// Getting the planet (with people)
		List<Integer> used = new ArrayList<Integer>();
		World world = null;
		do {
			Integer p = getRandomResource(max_planet, used);
			world = sw.getWorld(sw.buildResourceUrl("planets", p));
			if (world == null) {
				System.out.println("Hubo un error al encontrar el recurso " + p);
			}
			used.add(p);
		} while (world == null || world.residents == null || world.residents.length < 1 || world.name.equals("unknown"));
		used.clear();
		// Picking one randomly as the correct answer
		String[] residents = world.residents;
		Person correct = sw.getPerson(residents[rand.nextInt(residents.length)]);
		// Marking all people from the planet as used so they won't be selected again
		for (String s : residents) {
			used.add(sw.extractIdFromUrl(s));
		}

		// Finding 3 more
		List<Person> people = new ArrayList<Person>();
		int contador = 0;
		while (contador < 3) {
			Integer p = getRandomResource(max_people, used);
			Person person = sw.getPerson(sw.buildResourceUrl("people", p));
			if (person == null) {
				System.out.println("Hubo un error al encontrar el recurso " + p);
			} else {
				people.add(person);
				contador++;
			}
			used.add(p);
		}
		// Inserting the correct one randomly
		int pos_correct = rand.nextInt(4);
		people.add(pos_correct, correct);

		// Reading the option
		Integer n = null;
		do {
			System.out.print("�Qui�n naci� o fue fabricado en " + world.name + "?");
			for (int i = 0; i < 4; i++) {
				System.out.print(" [" + i + "] " + people.get(i).name);
			}
			System.out.println();
			try {
				n = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException ex) {
				n = -1;
			}
		} while (n < 0 || n > 3);

		// Displaying the results
		for (Person p : people) {
			System.out.println(p.name + " naci� en " + p.homeplanet.name);
		}

		// Results
		if (n == pos_correct) {
			System.out.println("Enhorabuena!!! " + success[rand.nextInt(success.length)]);
		} else {
			System.out.println("Fallaste :( " + error[rand.nextInt(error.length)]);
		}
	}

  
	private static String [] success = {"This is the way",
			"Eres uno con la Fuerza. La Fuerza est� contigo",
			"Que la fuerza te acompa�e",
			"Nada ocurre por accidente",
			"Sin duda, maravillosa tu mente es",
			"Cuando te fuiste, no era m�s que el aprendiz. Ahora eres el maestro",
			"La Fuerza te est� llamando, d�jala entrar",
			"Tu cantidad de midiclorianos debe ser muy alta",
			"Una lecci�n aprendida es una lecci�n ganada",
			"Debes creer en ti mismo o nadie lo har�",
			"El camino a la sabiduria es sencillo para aquellos que no se dejan cegar por el ego" };
	private static String [] error = {"El mejor profesor, el fracaso es.",
			"El miedo es el camino hacia el Lado Oscuro. El miedo lleva a la ira, la ira lleva al odio, el odio lleva al sufrimiento. Percibo mucho miedo en ti",
			"Tu carencia de fe resulta molesta",
			"La capacidad de hablar no te hace inteligente",
			"Conc�ntrate en el momento. Siente, no pienses, usa tu instinto",
			"No lo intentes. Hazlo, o no lo hagas, pero no lo intentes",
			"Paciencia, utiliza la Fuerza. Piensa",
			"Siento una perturbaci�n en la fuerza",
			"El lado oscurso se intensifica en ti",
			"El primer paso para corregir un error es la paciencia",
			"El exceso de confianza es el mas peligroso de los descuidos",
			"El camino de la ignorancia es guiado por el miedo" };

}
