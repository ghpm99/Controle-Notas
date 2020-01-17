/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDados.modulo;

import BancoDados.Conexao;

/**
 *
 * @author guilherme.machado
 * @param <V>
 */
public interface Modulo<V> {
    
    /**
     *
     */
    public void iniciar();
    
    /**
     *
     */
    public void fechar();
    
    public int setObject(Conexao conexao, String campo, int id, Object arg, int usuario);
    
}
