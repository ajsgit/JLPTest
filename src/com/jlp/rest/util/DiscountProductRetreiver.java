package com.jlp.rest.util;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.swing.text.html.StyleSheet;

import org.apache.commons.lang3.StringUtils;

import com.jlp.rest.model.ColorSwatch;
import com.jlp.rest.model.Product;

public class DiscountProductRetreiver {
	
	private static final String RGB_HEX_VALUE_FORMATTER = "%02X%02X%02X";
	private static final String SKU_ID = "skuId";
	private static final String BASIC_COLOR = "basicColor";
	private static final String COLOR = "color";
	private static final String COLOR_SWATCHES = "colorSwatches";
	private static final String LABEL_SHOW_PERC_DSCOUNT = "ShowPercDscount";
	private static final String LABEL_SHOW_WAS_THEN_NOW = "ShowWasThenNow";
	private static final String THEN2 = "then2";
	private static final String THEN1 = "then1";
	private static final String TITLE = "title";
	private static final String PRODUCT_ID = "productId";
	private static final String NOW = "now";
	private static final String WAS = "was";
	private static final String PRICE = "price";
	private static final String PRODUCTS = "products";

	public static List<Product> getDiscountedProducts(JsonObject categoryObject, String labelType) {
		JsonArray productsArray = categoryObject.getJsonArray(PRODUCTS);
		List<Product> producsListWithWasPrice = new ArrayList<Product>();

		productsArray.forEach(item -> {
			JsonObject productObject = (JsonObject) item;
			JsonObject priceObject = (JsonObject) productObject.get(PRICE);
			String wasPrice = priceObject.getString(WAS);
			JsonValue nowPriceValue = priceObject.get(NOW);
			if(nowPriceValue.getValueType() == ValueType.STRING) {
				String nowPrice = priceObject.getString(NOW);
				if (!StringUtils.isEmpty(wasPrice) && !StringUtils.isEmpty(nowPrice)) {
					Product product = new Product();
					product.setProductId(productObject.getString(PRODUCT_ID));
					product.setTitle(productObject.getString(TITLE));
					product.setColorSwatches(getColorSwatch(productObject));
					product.setNowPrice(getFormattedPrice(nowPrice));
					product.setPriceLabel(getPriceLabel(wasPrice, nowPrice, priceObject.getString(THEN1),
							priceObject.getString(THEN2), labelType));
					product.setPriceReduction(getPriceReduction(wasPrice, nowPrice));
					producsListWithWasPrice.add(product);
				}
			}
		});
		
		return producsListWithWasPrice;
	}
	

	static Double getPriceReduction(String wasPrice, String nowPrice) {
		return Double.valueOf(wasPrice) - Double.valueOf(nowPrice);
	}

	static String getPriceLabel(String wasPrice, String nowPrice, String then1, String then2, String labelType) {
		String wasPriceFormatted = getFormattedPrice(wasPrice);
		String nowPriceFormatted = getFormattedPrice(nowPrice);
		
		if (!StringUtils.isEmpty(labelType)) {
			if (labelType.equalsIgnoreCase(LABEL_SHOW_WAS_THEN_NOW)) {
				if (!StringUtils.isEmpty(then2)) {
					String then2PriceFormatted = getFormattedPrice(then2);
					return "Was " + wasPriceFormatted + ", then " + then2PriceFormatted + ", now " + nowPriceFormatted;
				} else if (!StringUtils.isEmpty(then1)) {
					String then1PriceFormatted = getFormattedPrice(then1);
					return "Was " + wasPriceFormatted + ", then " + then1PriceFormatted + ", now " + nowPriceFormatted;
				}
			}

			if (labelType.equalsIgnoreCase(LABEL_SHOW_PERC_DSCOUNT)) {
				double wasPriceDouble = Double.valueOf(wasPrice);
				double nowPriceDouble = Double.valueOf(nowPrice);

				double difference = wasPriceDouble - nowPriceDouble;
				if (difference > 0) {
					double percentageDiscount = difference / wasPriceDouble * 100;
					return Math.round(percentageDiscount) + "% off - now " + nowPriceFormatted;
				}

			}
		}
		return "Was " + wasPriceFormatted + ", now " + nowPriceFormatted;
	}

	static String getFormattedPrice(String nowPriceInRequestString) {
		String nowPriceString = "£0.00";
		if (!StringUtils.isEmpty(nowPriceInRequestString)) {
			double nowPrice = Double.valueOf(nowPriceInRequestString);
			NumberFormat formatter = NumberFormat.getCurrencyInstance();
			nowPriceString = formatter.format(nowPrice);
			if (nowPrice == Math.floor(nowPrice) && Math.floor(nowPrice) >= 10) {
				int centsIndex = nowPriceString.lastIndexOf(".00");
				nowPriceString = nowPriceString.substring(0, centsIndex);
			}
		}
		return nowPriceString;
	}

	static List<ColorSwatch> getColorSwatch(JsonObject productObject) {
		JsonArray colorSwatchesArray = productObject.getJsonArray(COLOR_SWATCHES);
		List<ColorSwatch> colorSwatchesList = new ArrayList<ColorSwatch>();
		colorSwatchesArray.forEach(colorSwatchItem -> {
			JsonObject colorSwatchObject = (JsonObject) colorSwatchItem;
			ColorSwatch colorSwatch = new ColorSwatch();
			colorSwatch.setColor(colorSwatchObject.getString(COLOR));
			colorSwatch.setRgbColor(getHexValue(colorSwatchObject.getString(BASIC_COLOR)));
			colorSwatch.setSkuId(colorSwatchObject.getString(SKU_ID));
			colorSwatchesList.add(colorSwatch);
		});
		return colorSwatchesList;
	}

	static String getHexValue(String basicColor) {
		if (!StringUtils.isEmpty(basicColor)) {
			StyleSheet styleSheet = new StyleSheet();
			Color color = styleSheet.stringToColor(basicColor.toUpperCase());
			int r = color.getRed();
			int g = color.getGreen();
			int b = color.getBlue();

			return String.format(RGB_HEX_VALUE_FORMATTER, r, g, b);
		}
		return null;
	}

}
