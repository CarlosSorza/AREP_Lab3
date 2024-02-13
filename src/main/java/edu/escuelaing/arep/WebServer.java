package edu.escuelaing.arep;


import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class WebServer {
    private static final int PORT = 35000;
    private static String staticFilesDirectory = "static";
    private static boolean jsonResponse = false;

    private static Map<String, Function> getHandlers = new HashMap<>();
    private static Map<String, Function> postHandlers = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleRequest(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String requestLine = in.readLine();
            if (requestLine != null) {
                String[] requestParts = requestLine.split(" ");
                String method = requestParts[0];
                String path = requestParts[1];

                // Parse query parameters
                Map<String, String> queryParams = new HashMap<>();
                if (path.contains("?")) {
                    String queryString = path.substring(path.indexOf("?") + 1);
                    String[] params = queryString.split("&");
                    for (String param : params) {
                        String[] keyValue = param.split("=");
                        if (keyValue.length == 2) {
                            queryParams.put(keyValue[0], keyValue[1]);
                        }
                    }
                    path = path.substring(0, path.indexOf("?"));
                }

                if (method.equals("GET")) {
                    Function handler = getHandlers.get(path);
                    if (handler != null) {
                        String response = handler.apply(null, queryParams);
                        sendResponse(out, response);
                    } else {
                        serveStaticFile(out, path);
                    }
                } else if (method.equals("POST")) {
                    Function handler = postHandlers.get(path);
                    if (handler != null) {
                        String inputData = "";
                        String line;
                        while ((line = in.readLine()) != null && !line.isEmpty()) {
                            inputData += line + "\n";
                        }
                        String response = handler.apply(inputData, queryParams);
                        sendResponse(out, response);
                    } else {
                        sendResponse(out, "404 Not Found");
                    }
                }
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void serveStaticFile(PrintWriter out, String path) {
        try {
            File file = new File(staticFilesDirectory + path);
            if (file.exists() && !file.isDirectory()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    content.append(line);
                }
                br.close();

                out.println("HTTP/1.1 200 OK");
                String contentType = getContentType(path);
                if (contentType != null) {
                    out.println("Content-Type: " + contentType);
                }
                out.println();
                out.println(content.toString());
            } else {
                out.println("HTTP/1.1 404 Not Found");
                out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendResponse(PrintWriter out, String response) {
        out.println("HTTP/1.1 200 OK");
        if (jsonResponse) {
            out.println("Content-Type: application/json");
        }
        out.println();
        out.println(response);
    }

    public static void get(String path, Function handler) {
        getHandlers.put(path, handler);
    }

    public static void post(String path, Function handler) {
        postHandlers.put(path, handler);
    }

    public static void setStaticFilesDirectory(String directory) {
        staticFilesDirectory = directory;
    }

    public static void setJsonResponse(boolean enable) {
        jsonResponse = enable;
    }

    private static String getContentType(String path) {
        if (path.endsWith(".html") || path.endsWith(".htm")) {
            return "text/html";
        } else if (path.endsWith(".css")) {
            return "text/css";
        } else if (path.endsWith(".js")) {
            return "application/javascript";
        } else if (path.endsWith(".png")) {
            return "image/png";
        } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (path.endsWith(".gif")) {
            return "image/gif";
        }
        return null;
    }

    
}




