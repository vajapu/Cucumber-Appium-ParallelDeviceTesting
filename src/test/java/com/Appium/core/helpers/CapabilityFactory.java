package com.Appium.core.helpers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CapabilityFactory {
	public static JSONArray getDeviceCapabilities(String deviceJsonPath) {
		try (FileInputStream file = new FileInputStream(deviceJsonPath)) {
			return (JSONArray) new JSONParser().parse(deviceJsonPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JSONArray getDeviceCapabilities(URI deviceJsonURI) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(deviceJsonURI);
		try {
			HttpResponse response = client.execute(request);
			String responseCode = response.getStatusLine().toString();
			if(!responseCode.contains("200"))
				throw new RuntimeException("Unable to fetch device details from uri, status:"+ responseCode);
			else {
				InputStream in = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line,inline=null;
				while((line = reader.readLine())!=null) {
					inline = line;
				}
				reader.close();
				return (JSONArray) new JSONParser().parse(inline);
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
