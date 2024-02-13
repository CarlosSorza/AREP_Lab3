package edu.escuelaing.arep;

import java.lang.reflect.Method;
@Component
public class HelloController {
    

    @GetMapping ("/hello")
    public static String index (){
        return "Grettings from Spring Boot";
    }

    @GetMapping ("helloname")
    public static String hellName (String name){
        return "Hello " + name;
    }

    @GetMapping ("square")
    public static String Square (String val){
        return " " + (Double.valueOf(val) * Double.valueOf(val));
    }
    
}
