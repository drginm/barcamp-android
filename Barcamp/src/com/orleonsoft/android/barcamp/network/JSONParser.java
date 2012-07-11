package com.orleonsoft.android.barcamp.network;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.net.http.AndroidHttpClient;

public class JSONParser {

	public JSONArray getJSONFromURL(String url) throws IOException {
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
			throw new IOException("Error IO");
		} catch (JSONException e) {
			throw new IOException("Error IO");
		}

	}

}
