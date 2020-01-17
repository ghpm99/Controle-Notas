/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDados.modulo;

import BancoDados.Conexao;
import Util.Historico;
import java.util.ArrayList;

/**
 *
 * @author guilherme.machado
 */
public class ModuloUtil implements Modulo{

    
     private static ModuloUtil instancia;

    /**
     *
     * @return
     */
    public static ModuloUtil getInstancia() {
        if (instancia == null) {
            instancia = new ModuloUtil();
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
     * @param tabela
     * @param id
     * @return
     */
    public ArrayList<Historico> buscarHistorico(Conexao banco,String tabela, int id) {
        return banco.buscarHistorico( tabela, id);
    }

    @Override
    public int setObject(Conexao conexao, String campo, int id, Object arg, int usuario) {
        return 0;
    }
}
