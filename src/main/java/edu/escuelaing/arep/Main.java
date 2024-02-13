package edu.escuelaing.arep;



/**
 * Hello world!
 *
 */
public class Main {
        public static void main(String[] args) {
            // Configurar el directorio de archivos estáticos
            WebServer.setStaticFilesDirectory("static");
    
            // Cambiar el tipo de respuesta a JSON si es necesario
            WebServer.setJsonResponse(true);
    
            // Registrar servicios GET
            WebServer.get("/hello", (inputData, queryParams) -> {
                return "Hello, World!";
            });
    
            // Registrar servicios POST
            WebServer.post("/echo", (inputData, queryParams) -> {
                return "You said: " + inputData;
            });
    
            // Iniciar el servidor
            WebServer.main(args);
       
    }
}
