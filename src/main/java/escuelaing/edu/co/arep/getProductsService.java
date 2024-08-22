package escuelaing.edu.co.arep;

import java.util.List;
import java.util.stream.Collectors;

public class getProductsService implements RESTService{

    private shoppingListService cartService;

    public getProductsService(shoppingListService cartService) {
        this.cartService = cartService;
    }

    @Override
    public String response(String request) {
        List<String> products = cartService.getProducts();

        // Convertir la lista de productos en una cadena JSON válida
        String jsonProducts = products.stream()
                .map(product -> "\"" + product + "\"")
                .collect(Collectors.joining(","));
        return "{\"products\":[" + jsonProducts + "]}";
    }
}
