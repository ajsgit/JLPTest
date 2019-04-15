package com.jlp.rest.util;

import java.io.StringReader;
import java.net.URI;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

public class CategoryResourceFactory {
	
	public static JsonObject getProductsForCategory(String categoryId) {
		String webServiceURI = "https://jl-nonprod-syst.apigee.net/v1/categories/" + categoryId + "/products?key=2ALHCAAs6ikGRBoy6eTHA58RaG097Fma";

		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		URI serviceURI = UriBuilder.fromUri(webServiceURI).build();
		WebTarget webTarget = client.target(serviceURI);

		Response response = webTarget.request().accept(MediaType.APPLICATION_JSON).get(Response.class);
		String output = response.readEntity(String.class);
		JsonReader jsonReader = Json.createReader(new StringReader(output));
		JsonObject jsonObject = jsonReader.readObject();
		return jsonObject;
	}

}
