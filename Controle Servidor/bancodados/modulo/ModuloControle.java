/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDados.modulo;

import BancoDados.Conexao;
import Util.ItemOrcamento;
import java.util.ArrayList;

/**
 *
 * @author guilherme.machado
 */
public class ModuloControle implements Modulo{


    private static ModuloControle instancia;

    /**
     *
     * @return
     */
    public static ModuloControle getInstancia() {
        if (instancia == null) {
            instancia = new ModuloControle();
            instancia.iniciar();
        }
        return instancia;
    }
    
    /**
     *
     */
    @Override
    public void iniciar() {
    }

    /**
     *
     */
    @Override
    public void fechar() {
    }
    
    /**
     *
     * @param banco
     * @param item
     * @return
     */
    public ItemOrcamento buscarItemOrcamento(Conexao banco,String item) {
        return banco.buscarItemOrcamento( item);
    }
    
    /**
     *
     * @param banco
     * @return
     */
    public ArrayList<ItemOrcamento> listarItensOrcamento(Conexao banco) {
        return banco.listarItensOrcamento();
    }
    
    /**
     *
     * @param banco
     * @param item
     * @return
     */
    public int incluirItemOrcamento(Conexao banco,int autor){
        return banco.incluirItemOrcamento(autor);
    }
    
    /**
     *
     * @param banco
     * @param item
     * @return
     */
    public int atualizarItemOrcamento(Conexao banco,ItemOrcamento item) {
        return banco.atualizarItemOrcamento( item);
    }

    @Override
    public int setObject(Conexao conexao,  String campo, int id, Object arg, int usuario) {
        return 0;
    }
}
