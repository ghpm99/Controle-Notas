/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.nota;

/**
 * @author Guilherme
 */
public class NotasTemp{

    private static String campo = "NUMERO", valor = "";

    public static String getCampo(){
        return campo;
    }

    public static void setCampo(String campo){
        NotasTemp.campo = campo;
    }

    public static String getValor(){
        return valor;
    }

    public static void setValor(String valor){
        NotasTemp.valor = valor;
    }
}
