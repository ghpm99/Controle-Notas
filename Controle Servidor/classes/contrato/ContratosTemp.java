/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Classes.Contrato;

/**
 * @author Guilherme
 */
public class ContratosTemp {

    private static String campo ="NUMERO", valor = "";

    /**
     *
     * @return
     */
    public static String getCampo(){
        return campo;
    }

    /**
     *
     * @param campo
     */
    public static void setCampo(String campo){
        ContratosTemp.campo = campo;
    }

    /**
     *
     * @return
     */
    public static String getValor(){
        return valor;
    }

    /**
     *
     * @param valor
     */
    public static void setValor(String valor){
        ContratosTemp.valor = valor;
    }   
    
}
