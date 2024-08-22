package escuelaing.edu.co.arep;

import java.io.*;
import java.net.Socket;

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedOutputStream dataOut = new BufferedOutputStream(clientSocket.getOutputStream())) {

            String requestLine = in.readLine();
            if (requestLine == null) return;
            String[] tokens = requestLine.split(" ");
            String method = tokens[0];
            String fileRequested = tokens[1];
            printRequestHeader(requestLine, in);

            handleRequest(method,fileRequested,out,dataOut);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(String method, String fileRequested, PrintWriter out, BufferedOutputStream dataOut) throws IOException {
        if (method.equals("GET") && !fileRequested.startsWith("/app")) {
            handleGetRequest(fileRequested, out, dataOut);
        }

        if (method.equals("GET") && fileRequested.startsWith("/app/getProducts")) {
            out.println("HTTP/1.1 200 OK");
            out.println("Content-type: " + "application/json");
            out.println();
            out.println(ShoppingList.services.get("getProducts").response(fileRequested));
        }

        if(method.equals("POST") && fileRequested.startsWith("/app/addProduct")){
            out.println("HTTP/1.1 200 OK");
            out.println("Content-type: " + "application/json");
            out.println();
            out.println(ShoppingList.services.get("addProduct").response(fileRequested));
        }

        if(method.equals("DELETE") && fileRequested.startsWith("/app/deleteProduct")){
            out.println("HTTP/1.1 200 OK");
            out.println("Content-type: " + "application/json");
            out.println();
            out.println(ShoppingList.services.get("deleteProduct").response(fileRequested));
        }
    }

    private void printRequestHeader(String requestLine, BufferedReader in) throws IOException {
        System.out.println("Request line: " + requestLine);
        String inputLine = "";
        while ((inputLine = in.readLine()) != null) {
            System.out.println("hearder: " + inputLine);
            if (!in.ready()) {
                break;
            }
        }
    }

    private void handleGetRequest(String fileRequested, PrintWriter out, BufferedOutputStream dataOut) throws IOException {
        File file = new File(ShoppingList.WEB_ROOT, fileRequested);
        int fileLength = (int) file.length();
        String content = getContentType(fileRequested);

        if (file.exists()) {
            byte[] fileData = readFileData(file, fileLength);
            out.println("HTTP/1.1 200 OK");
            out.println("Content-type: " + content);
            out.println("Content-length: " + fileLength);
            out.println();
            out.flush();
            dataOut.write(fileData, 0, fileLength);
            dataOut.flush();
        } else {
            out.println("HTTP/1.1 404 Not Found");
            out.println("Content-type: text/html");
            out.println();
            out.flush();
            out.println("<html><body><h1>File Not Found</h1></body></html>");
            out.flush();
        }
    }

    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".html")) return "text/html";
        else if (fileRequested.endsWith(".css")) return "text/css";
        else if (fileRequested.endsWith(".js")) return "application/javascript";
        else if (fileRequested.endsWith(".png")) return "image/png";
        else if (fileRequested.endsWith(".jpg")) return "image/jpeg";
        return "text/plain";
    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];
        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null) fileIn.close();
        }
        return fileData;
    }
}
