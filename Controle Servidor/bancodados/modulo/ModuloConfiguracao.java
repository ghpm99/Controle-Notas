/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDados.modulo;

import BancoDados.Conexao;
import Util.HistoricoPesquisa;

/**
 *
 * @author guilherme.machado
 */
public class ModuloConfiguracao implements Modulo{

    
     private static ModuloConfiguracao instancia;

    /**
     *
     * @return
     */
    public static ModuloConfiguracao getInstancia() {
        if (instancia == null) {
            instancia = new ModuloConfiguracao();
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
     * @param conexao
     * @param idConta
     * @param tabela
     * @return
     */
    public HistoricoPesquisa buscarCampos(Conexao conexao,int idConta, String tabela) {
         return conexao.buscarCampos(idConta, tabela);
     }

    @Override
    public int setObject(Conexao conexao, String campo, int id, Object arg, int usuario) {
    return 0;
    }
    
}
