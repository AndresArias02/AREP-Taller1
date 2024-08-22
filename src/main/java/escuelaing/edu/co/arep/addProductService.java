
package escuelaing.edu.co.arep;

public class addProductService implements RESTService {

    private shoppingListService cartService;

    public addProductService(shoppingListService cartService) {
        this.cartService = cartService;
    }

    @Override
    public String response(String request) {

        String product = request.split("=")[1];
        product = product.replace("%20", " "); // Reemplazar espacios codificados con espacios normales

        product = java.net.URLDecoder.decode(product, java.nio.charset.StandardCharsets.UTF_8);

        if (cartService.getProducts().contains(product)) {
            return "{\"message\":\"Product already exists in the cart.\"}";
        } else {
            cartService.addProduct(product);
            return "{\"message\":\"Product added successfully.\"}";
        }
    }
}
