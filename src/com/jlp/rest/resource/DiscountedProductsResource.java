package com.jlp.rest.resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jlp.rest.model.Product;
import com.jlp.rest.util.CategoryResourceFactory;
import com.jlp.rest.util.DiscountProductRetreiver;

@Path("/categories/{categoryId}") // categories/600001506
public class DiscountedProductsResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getProducts(@PathParam("categoryId") String categoryId, @QueryParam("labelType") String labelType) {

		JsonObject jsonObject = CategoryResourceFactory.getProductsForCategory(categoryId);

		List<Product> producsListWithWasPrice = DiscountProductRetreiver.getDiscountedProducts(jsonObject, labelType);
				
		Collections.sort(producsListWithWasPrice, Collections.reverseOrder());
		
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final ObjectMapper mapper = new ObjectMapper();

		try {
			mapper.writeValue(out, producsListWithWasPrice);
		} catch (IOException e) {
			e.printStackTrace();
		}

		final byte[] data = out.toByteArray();
		String responseJSON = new String(data);
		System.out.println("response JSON : " + responseJSON);
		return responseJSON;
	}

}
