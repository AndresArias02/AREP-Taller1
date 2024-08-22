import escuelaing.edu.co.arep.ShoppingList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class shoppingListTest {

    private static final String BASE_URL = "http://localhost:8080/app";
    private static HttpURLConnection connection = null;



    @BeforeEach
    public void setup() throws Exception {
        // Iniciar el servidor
        new Thread(() -> {
            try {
                // Inicia el servidor en un hilo separado
                ShoppingList.main(new String[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        // Esperar un poco para asegurarse de que el servidor esté en funcionamiento
        try {
            Thread.sleep(2000); // Espera de 2 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddProduct() throws Exception {
        String product = "Apple";
        URL url = new URL(BASE_URL + "/addProduct?name=" + product);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        assertEquals(HttpURLConnection.HTTP_OK, responseCode);

        String response = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertTrue(response.contains("Product added successfully."));
    }

    @Test
    public void testGetProducts() throws Exception {
        URL url = new URL(BASE_URL + "/getProducts");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        assertEquals(HttpURLConnection.HTTP_OK, responseCode);

        String response = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertTrue(response.contains("products"));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        String product = "Apple";
        URL url = new URL(BASE_URL + "/deleteProduct?name=" + product);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        assertEquals(HttpURLConnection.HTTP_OK, responseCode);

        String response = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertTrue(response.contains("Product deleted successfully."));
    }
}
