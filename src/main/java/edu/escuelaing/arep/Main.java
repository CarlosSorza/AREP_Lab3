package edu.escuelaing.arep;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class Main {

    static Map<String, Method> componentes = new HashMap<String,Method>();
  
        public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            

            Class<?> c = Class.forName(args[0]);
            if (c.isAnnotationPresent(Component.class)){
                for(Method m: c.getDeclaredMethods()){
                    if(m.isAnnotationPresent(GetMapping.class)){
                        componentes.put(m.getAnnotation(GetMapping.class).value(),m);
                    }

                }
            }
            String pathLlamado = "/component/hello";
            String queryValue = "Daniel";

            if(pathLlamado.startsWith("/component")){
                Method mLlamado = componentes.get(pathLlamado.substring(10));
                if(mLlamado != null){
                    if(mLlamado.getParameterCount()==1){
                        
                        //String[] margs = new String[]{queryValue};
                        System.out.println("Salida " + mLlamado.invoke(null, (Object)queryValue));  
                    }else{
                        System.out.println(mLlamado.invoke(null));  
                
                    }
                 }
                    
            }
            
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
