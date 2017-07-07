package com.pyesmeadow.george.currencyconverter.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPUtil {

	private static final String USER_AGENT = "Mozilla/5.0";

	/**
	 * Sends a HTTP GET request to a specified url.
	 * 
	 * @param url
	 *            the URL to send a request to
	 * @return
	 * @throws Exception
	 */
	public static StringBuffer sendGetRequest(String url) throws Exception {

		URL connectionURL = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) connectionURL.openConnection();

		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", USER_AGENT);

		System.out.println("\nSending 'GET' request to " + connectionURL);

		/* Read the data from the HTTP Request */
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		/* Add lines to response */
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response;
	}

	/**
	 * Sends a HTTP POST request to a specified url with parameters.
	 * 
	 * @param url
	 *            the URL to send a request to
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public static StringBuffer sendPostRequest(String url, String... parameters) throws Exception {

		/* Create connection */
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		/* Create querystring */
		String querystring = "?";
		for (String p : parameters) {
			querystring = querystring + p + "&";
		}

		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(querystring);
		wr.flush();
		wr.close();
		
		System.out.println("\nSending 'POST' request to " + url);

		/* Read the data from the HTTP Request */
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		/* Add lines to response */
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response;
	}
}
