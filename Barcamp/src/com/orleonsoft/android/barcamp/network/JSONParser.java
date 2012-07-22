package com.orleonsoft.android.barcamp.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;
import android.util.Log;

public class JSONParser {

	public static JSONArray getJSONArrayFromURL(String url) throws IOException {
		try {
			// Building the request
			AndroidHttpClient httpClient = AndroidHttpClient
					.newInstance("Android");
			URI uri = new URI(url);
			HttpGet getRequest = new HttpGet();
			getRequest.setURI(uri);
			

			// getting the response
			HttpResponse httpResponse = httpClient.execute(getRequest);
			

			final int statusCode = httpResponse.getStatusLine().getStatusCode();

			// check the response if it's ok
			if (statusCode != HttpStatus.SC_OK) {
				httpClient.close();
				return new JSONArray();
			}

			final HttpEntity entity = httpResponse.getEntity();

			String data = EntityUtils.toString(entity, HTTP.UTF_8);
			entity.consumeContent();			
			httpClient.close();
			
			return new JSONArray(data);
		} catch (URISyntaxException e) {
			throw new IOException("Error internet connection");
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException("Protocol error");
		} catch (IOException e) {
			throw new IOException("Error IO"+e.getMessage());
		} catch (JSONException e) {
			throw new IOException("JSON Error");
		}

	}
	
	public static JSONObject getJSONFromURL(String url) throws IOException {
		try {
			// Building the request
			AndroidHttpClient httpClient = AndroidHttpClient
					.newInstance("Android");
			URI uri = new URI(url);
			HttpGet getRequest = new HttpGet();
			getRequest.setURI(uri);
			

			// getting the response
			HttpResponse httpResponse = httpClient.execute(getRequest);
			

			final int statusCode = httpResponse.getStatusLine().getStatusCode();

			// check the response if it's ok
			if (statusCode != HttpStatus.SC_OK) {
				httpClient.close();
				return new JSONObject();
			}

			final HttpEntity entity = httpResponse.getEntity();

			String data = EntityUtils.toString(entity, HTTP.UTF_8);
			entity.consumeContent();			
			httpClient.close();
			
			return new JSONObject(data);
		} catch (URISyntaxException e) {
			throw new IOException("Error internet connection");
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException("Protocol error");
		} catch (IOException e) {
			throw new IOException("Error IO"+e.getMessage());
		} catch (JSONException e) {
			throw new IOException("JSON Error");
		}

	}
	
	/**
	 * hace una peticion http y lee la respuesta del servidor la convierte en un JSON
	 * 
	 * @param array
	 * @param nombreFormulario
	 * 
	 * @return JSONObject
	 * 
	 */
	public static JSONObject getJSONObjectFromURL(String url) throws JSONException {
		StringBuffer stringResponse = null;// para guardar la respuesta

		// para leer la respuesta
		BufferedReader in = null;

		try {

			HttpClient httpclient = new DefaultHttpClient();

			HttpGet request = new HttpGet();
			request.setURI(new URI(url));

			HttpResponse response = httpclient.execute(request);

			// esto de aca es para procesar la respuesta del script despues de
			// hacer el htpp reques es decir tomar el mensaje que devuelve por
			// ejemplo
			// registro exitoso o error (el script actual no retorna nada . aun
			// hayq eu opnerle el mensaje y ya si debeleve bien se muestra en un
			// toast o dialogo que lo hizo si no lo contrario .)
			if (response.getStatusLine() != null) {
				in = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent(), "UTF-8"));
				stringResponse = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					stringResponse.append(line + NL);
				}

				in.close();

				
			}

		} catch (ClientProtocolException e) {
			Log.e("CLIENT_PROTOCOL_ERROR",
					"error con el protocolo ::  " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IO_ERROR", "error de escritura/lectura :: " + e.getMessage());
			e.printStackTrace();
		} catch (URISyntaxException e) {
			Log.e("URL_ERROR", "error en la url" + e.getMessage());
			e.printStackTrace();
		}
		return new JSONObject(stringResponse.toString());

	}
	
	

}
