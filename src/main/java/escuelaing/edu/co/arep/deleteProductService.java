package escuelaing.edu.co.arep;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class deleteProductService implements RESTService{

    private shoppingListService listService;

    public deleteProductService(shoppingListService listService){
        this.listService = listService;
    }

    @Override
    public String response(String request) {
        // Extraer el nombre del producto desde la URL
        String[] parts = request.split("\\?");
        if (parts.length > 1) {
            String query = parts[1];
            String[] queryParams = query.split("&");
            for (String param : queryParams) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && keyValue[0].equals("name")) {
                    String productName = keyValue[1];
                    // Decodificar el valor del nombre del producto
                    productName = URLDecoder.decode(productName, StandardCharsets.UTF_8);
                    // Eliminar el producto
                    listService.deleteProduct(productName);
                    return "{\"message\":\"Product deleted successfully.\"}";
                }
            }
        }
        return "{\"message\":\"Product not found.\"}";
    }
}
