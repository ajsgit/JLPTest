I have created this Dynamic Web project in eclipse using the target runtime Apache tomcat 9.0.

Used JAX-RS Jersey and Jackson libraries to build this simple RESTful service to retreive products from category and return only discounted products.

Url to access the service is http://localhost:8080/JLPTest/rest/categories/600001506?labelType=ShowWasNow

The response for the above request:
[{"productId":"3428696","title":"Hobbs Kiona Dress, Green","colorSwatches":[],"nowPrice":"£74","priceLabel":"Was £149, now £74"},{"productId":"3421340","title":"Phase Eight Beatrix Floral Printed Dress, Cream/Red","colorSwatches":[],"nowPrice":"£99","priceLabel":"Was £140, now £99"},{"productId":"3391561","title":"Phase Eight Katlyn Three Quarter Sleeve Spot Dress, Navy/Ivory","colorSwatches":[],"nowPrice":"£59","priceLabel":"Was £99, now £59"},{"productId":"3459039","title":"Hobbs Bayview Dress, Blue/Multi","colorSwatches":[],"nowPrice":"£55","priceLabel":"Was £89, now £55"},{"productId":"3467432","title":"Boden Rosamund Posy Stripe Dress","colorSwatches":[{"color":"Navy","rgbColor":"0000FF","skuId":"237334043"},{"color":"Mimosa Yellow","rgbColor":"FFFF00","skuId":"237334029"}],"nowPrice":"£63","priceLabel":"Was £90, now £63"}]

Using any other category id to the above url will return empty json response.

labelType query parameter values can be changed to ShowWasThenNow or ShowPercDscount