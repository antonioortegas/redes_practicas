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

public class SWClient {
	// TODO: Complete the application name
	private final String app_name = "aos-swapi-client";
	private final int year = 2024;

	private final String url_api = "https://swapi.dev/api/";

	// Auxiliary methods provided

	// Gets the URL of the resource id of the type resource
	public String buildResourceUrl(String resource, Integer id){
		return url_api + resource + "/" + id + "/";
	}

	// Given a resource URL, gets its ID
	public Integer extractIdFromUrl(String url){
		String[] parts = url.split("/");
		return Integer.parseInt(parts[parts.length-1]);
	}

	// Queries a resource and returns how many elements it has
	public int countResources(String resource){
		int count = 0;
		// Handle possible exceptions appropriately
		try {
			// Create the corresponding URL: https://swapi.dev/api/{resource}/ replacing resource with the parameter
			URL urlname = new URL(url_api + resource + "/");
			// Create the connection from the URL
			HttpsURLConnection connection = (HttpsURLConnection) urlname.openConnection();
			// Add the headers User-Agent and Accept (see the statement)
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("User-Agent", app_name + "-" + year);
			// Indicate that it is a GET request
			connection.setRequestMethod("GET");
			// Check that the response code received is correct
			int responseCode = connection.getResponseCode();
			if(responseCode / 100 != 2) {
				System.err.println("ERROR: El codigo de respuesta " + responseCode + " no es un 2XX");
				return 0;
			}
			// Deserialize the response to ResourceCountResponse
			Gson parser = new Gson();
			InputStream in = connection.getInputStream();
			// Get the InputStream from the connection
			ResourceCountResult c = parser.fromJson(new InputStreamReader(in), ResourceCountResult.class);
			count = c.count;
		} catch (MalformedURLException e) {
			System.err.println("URL mal formada");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error en la conexion");
			e.printStackTrace();
		}
		// Return the number of elements
		return count;
	}

	public Person getPerson(String urlname) {
		Person p = null;
		// Just in case it comes as http, we change it to https
		urlname = urlname.replaceAll("http:", "https:");
		// Handle possible exceptions appropriately
		try {
			// Create the connection from the received URL
			URL url = new URL(urlname);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			// Add the headers User-Agent and Accept (see the statement)
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("User-Agent", app_name + "-" + year);
			// Indicate that it is a GET request
			connection.setRequestMethod("GET");
			// Check that the response code received is correct
			int responseCode = connection.getResponseCode();
			if(responseCode / 100 != 2) {
				System.err.println("ERROR: El codigo de respuesta " + responseCode + " no es un 2XX");
				return p;
			}
			// Deserialize the response to Person
			Gson parser = new Gson();
			InputStream in = connection.getInputStream();
			p = parser.fromJson(new InputStreamReader(in), Person.class);
			// For questions 2 and 3 (do not need to complete this for question 1)
			// From the URL in the homeworld field, get the planet data and store it in the homeplanet attribute
			if (p.homeworld != null) {
				World w = getWorld(p.homeworld);
				p.homeplanet = w;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	public World getWorld(String urlname) {
		World w = null;
		// Just in case it comes as http, we change it to https
		urlname = urlname.replaceAll("http:", "https:");
		// Handle possible exceptions appropriately
		try {
			// Create the connection from the received URL
			URL url = new URL(urlname);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			// Add the headers User-Agent and Accept (see the statement)
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("User-Agent", app_name + "-" + year);
			// Indicate that it is a GET request
			connection.setRequestMethod("GET");
			// Check that the response code received is correct
			int responseCode = connection.getResponseCode();
			if(responseCode / 100 != 2) {
				System.err.println("ERROR: El codigo de respuesta " + responseCode + " no es un 2XX");
				return w;
			}
			// Deserialize the response to Person
			Gson parser = new Gson();
			InputStream in = connection.getInputStream();
			w = parser.fromJson(new InputStreamReader(in), World.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return w;
	}

	public Person searchPersonByName(String name){
		Person p = null;
		// Realizar una petición a la URL https://swapi.dev/api/people/?search=<nombre> (sustituyendo <nombre> por el parámetro)
		// Handle possible exceptions appropriately
		try {
			// Create the connection from the URL (url_api + name processed - see the statement)
			name = URLEncoder.encode(name, "utf-8");
			URL url = new URL(url_api + "people/?search=" + name);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			// Add the headers User-Agent and Accept (see the statement)
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("User-Agent", app_name + "-" + year);
			// Indicate that it is a GET request
			connection.setRequestMethod("GET");
			// Check that the response code received is correct
			int responseCode = connection.getResponseCode();
			if (responseCode / 100 != 2) {
				System.err.println("ERROR: El codigo de respuesta " + responseCode + " no es un 2XX");
				return p;
				}
			// Deserialize the response to SearchResponse -> Use the first position of the array as the result
			Gson parser = new Gson();
			InputStream in = connection.getInputStream();
			QueryResponse qr = parser.fromJson(new InputStreamReader(in), QueryResponse.class);
			if (qr.results.length > 0) {
				p = qr.results[0];
				// For questions 2 and 3 (do not need to complete this for question 1)
				// From the URL in the homeworld field, get the planet data and store it in the homeplanet attribute
				World w = getWorld(p.homeworld);
				p.homeplanet = w;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}
}