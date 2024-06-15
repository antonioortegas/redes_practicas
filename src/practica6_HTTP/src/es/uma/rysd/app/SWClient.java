package es.uma.rysd.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;

import es.uma.rysd.entities.*;

/**
 * CHANGES:<p>
 * Completed declared functions::
 * <ul>
 *	<li>{@link #countResources(String)}</li>
 *	<li>{@link #getPerson(String)}</li>
 *	<li>{@link #getWorld(String)}</li>
 *	<li>{@link #searchPersonByName(String)}</li>
 * </ul>
 * </ul>
 * Helper functions:
 * <ul>
 * 	<li>{@link #createConnection(URL)}</li>
 * 	<li>{@link #isResponseCodeSuccess(HttpsURLConnection)}</li>
 * 	<li>{@link #deserializeResponse(HttpsURLConnection, Class)}</li>
 * </ul>
 * </ul>
 * New functions:
 * <ul>
 *  <li>{@link #getSpaceShip(String)}</li>
 * </ul>
 */
public class SWClient {
	// TODO: Complete the application name
	private final String app_name = "aos_swapi_client";
	private final int year = 2024;

	private final String url_api = "https://swapi.dev/api/";

	// Auxiliary methods provided

	// Gets the URL of the resource id of the type resource
	public String buildResourceUrl(String resource, Integer id){
		return url_api + resource + "/" + id + "/";
	}

	// Given a resource URL, gets its ID
	public Integer extractIdFromUrl(String url) {
		String[] parts = url.split("/");
		return Integer.parseInt(parts[parts.length - 1]);
	}
	
	// Queries a resource and return how many elements it has
	public int countResources(String resource){
		int count = 0;
		URL urlname = null;

		// Create the corresponding URL: https://swapi.dev/api/{resource}/ replacing resource with the parameter
		try {
			urlname = new URL(url_api + resource + "/");
		} catch (MalformedURLException e) {
			System.err.println("URL mal formada");
			e.printStackTrace();
		}

		HttpsURLConnection connection = createConnection(urlname);
		if (isResponseCodeSuccess(connection)) {
			ResourceCountResult countResult = deserializeResponse(connection, ResourceCountResult.class);
			count = countResult.count;
		}
		
		// Return the number of elements
		return count;
	}

	public Person getPerson(String urlname) {
		Person person = null;
		URL url = null;
		// Just in case it comes as http, we change it to https
		urlname = urlname.replaceAll("http:", "https:");

		// Create the connection from the received URL
		try {
			url = new URL(urlname);
		} catch (IOException e) {
			e.printStackTrace();
		}

		HttpsURLConnection connection = createConnection(url);
		if (isResponseCodeSuccess(connection)) {
			person = deserializeResponse(connection, Person.class);
		}

		// For questions 2 and 3 (do not need to complete this for question 1)
		// From the URL in the homeworld field, get the planet data and store it in the homeplanet attribute
		if (person.homeworld != null) {
			World w = getWorld(person.homeworld);
			person.homeplanet = w;
		}

		return person;
	}

	public World getWorld(String urlname) {
		World world = null;
		URL url = null;
		// Just in case it comes as http, we change it to https
		urlname = urlname.replaceAll("http:", "https:");

		// Create the connection from the received URL
		try {
			url = new URL(urlname);
		} catch (IOException e) {
			e.printStackTrace();
		}

		HttpsURLConnection connection = createConnection(url);
		if (isResponseCodeSuccess(connection)) {
			world = deserializeResponse(connection, World.class);
		}

		return world;
	}
	
	/**
	 * Get the SpaceShip from the specified URL
	 * @param urlname The URL to get the SpaceShip from
	 * @return The SpaceShip object
	 */
	public SpaceShip getSpaceShip(String urlname) {
		SpaceShip spaceShip = null;
		URL url = null;
		// Just in case it comes as http, we change it to https
		urlname = urlname.replaceAll("http:", "https:");

		// Create the connection from the received URL
		try {
			url = new URL(urlname);
		} catch (IOException e) {
			e.printStackTrace();
		}

		HttpsURLConnection connection = createConnection(url);
		if (isResponseCodeSuccess(connection)) {
			spaceShip = deserializeResponse(connection, SpaceShip.class);
		}

		return spaceShip;
	}

	public Person searchPersonByName(String name) {
		Person person = null;
		URL url = null;

		// Realizar una petición a la URL https://swapi.dev/api/people/?search=<nombre> (sustituyendo <nombre> por el parámetro)
		// Create the connection from the URL (url_api + name processed - see the statement)
		try {
			name = URLEncoder.encode(name, "utf-8");
			url = new URL(url_api + "people/?search=" + name);
		} catch (IOException e) {
			e.printStackTrace();
		}

		HttpsURLConnection connection = createConnection(url);
		if (isResponseCodeSuccess(connection)) {
			QueryResponse queryResponse = deserializeResponse(connection, QueryResponse.class);
			if (queryResponse.results.length > 0) {
				person = queryResponse.results[0];
				// For questions 2 and 3 (do not need to complete this for question 1)
				// From the URL in the homeworld field, get the planet data and store it in the homeplanet attribute
				World w = getWorld(person.homeworld);
				person.homeplanet = w;
			}
		}

		return person;
	}
	
	/**
	 * Create a connection to the specified URL with the headers User-Agent and Accept, and the GET method
	 * @param url The URL to create the connection
	 * @return The connection created
	 */
	private HttpsURLConnection createConnection(URL url) {
		HttpsURLConnection connection = null;
		try {
			// Create the connection from the URL
			connection = (HttpsURLConnection) url.openConnection();
			// Add the headers User-Agent and Accept (see the statement)
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("User-Agent", app_name + "-" + year);
			// Indicate that it is a GET request
			connection.setRequestMethod("GET");
			// System.out.println(connection.getHeaderFields().toString());
		} catch (IOException e) {
			System.err.println("ERROR: No se ha podido crear la conexion");
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * Make a request to the specified URL and check if the response code is 2XX
	 * @param connection The connection to make the request
	 * @return <b>true</b> if the response code is 2XX, <b>false</b> otherwise
	 */
	private boolean isResponseCodeSuccess(HttpsURLConnection connection) {
		boolean success = false;
		try {
			// Check that the response code received is correct
			int responseCode = connection.getResponseCode();
			if (responseCode / 100 == 2) {
				success = true;
			} else {
				System.err.println("ERROR: El codigo de respuesta " + responseCode + " no es un 2XX");
			}
		} catch (IOException e) {
			System.err.println("ERROR: No se ha podido obtener el codigo de respuesta");
			e.printStackTrace();
		}
		return success;
	}
	
	/**
	 * Deserialize the response to the specified class using Gson
	 * @param connection The connection to get the InputStream from
	 * @param classType The class to deserialize the response to
	 * @return The deserialized object, an instance of the specified class
	 */
	private <T> T deserializeResponse(HttpsURLConnection connection, Class<T> classType) {
		T result = null;
		try {
			// Instantiate the Gson parser
			Gson parser = new Gson();
			// Get the InputStream from the connection
			InputStream in = connection.getInputStream();
			// Deserialize the response to the specified class
			result = parser.fromJson(new InputStreamReader(in), classType);
		} catch (IOException e) {
			System.err.println("ERROR: No se ha podido deserializar la respuesta");
			e.printStackTrace();
		}
		return result;
	}
}
