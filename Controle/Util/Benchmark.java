/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author guilherme.machado
 */
public class Benchmark {

    private static Map<Long, Long> resultados = new HashMap<Long, Long>();

    public void main(BenchmarkInterface metodo) {
        
        
        for (int i = 0; i <= 100; i++) {
            long antes = new Date().getTime();
            metodo.run();
            long depois = new Date().getTime();
            resultados.put(antes, depois);
            System.out.print(i + ",");
        }
        System.out.println("Gerando resultados");
        long media = 0, maximo = 0, minimo = 10000;

        for (long antes1 : resultados.keySet()) {
            long depois1 = resultados.get(antes1);
            long resultado = (depois1 - antes1);
            System.out.println("Tempo de espera: " + resultado);
            media += resultado;
            if (maximo < resultado) {
                maximo = resultado;
            }
            if (minimo > resultado) {
                minimo = resultado;
            }
        }
        System.out.println("Tempo media: " + (media / resultados.size()) + " Maximo: " + maximo + " Minimo: " + minimo);

    }
}
