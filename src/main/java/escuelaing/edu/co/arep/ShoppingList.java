/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package escuelaing.edu.co.arep;

/**
 * Shopping class to start the aplication
 * @author Andres Felipe Arias
 * @author Luis Daniel Benavides Navarro
 */
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
 
public class ShoppingList {
    private static final int PORT = 8080;
    public static final String WEB_ROOT = "target/classes/webroot";
    public static final Map<String, RESTService> services = new HashMap();
    private static final shoppingListService cartService = new shoppingListService();
    
    public static void main(String[] args) throws IOException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        ServerSocket serverSocket = new ServerSocket(PORT);
        addServices();
 
        while (true) {
            Socket clientSocket = serverSocket.accept();
            threadPool.submit(new ClientHandler(clientSocket));
        }
    }
    private static void addServices(){
        services.put("addProduct", new addProductService(cartService));
        services.put("getProducts", new getProductsService(cartService));
        services.put("deleteProduct", new deleteProductService(cartService));
    }
}
 
