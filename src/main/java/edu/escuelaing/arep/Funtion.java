package edu.escuelaing.arep;

import java.util.Map;


interface Function {
    String apply(String inputData, Map<String, String> queryParams);
}
