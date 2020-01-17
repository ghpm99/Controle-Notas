/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Conexao.Cliente;
import javafx.concurrent.Task;

/**
 *
 * @author guilherme.machado
 */
public class Salvar extends Task<Dado>{

    private Dado dado;
    
    /**
     *
     * @param dado
     */
    public Salvar(Dado dado){
        this.dado = dado;
    }
    
    @Override
    protected Dado call() throws Exception {
        return null;
       
    }
    
}
