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
 */
public class Modulos {

    private static Modulos instancia;

    /**
     *
     * @return
     */
    public static Modulos getInstancia() {
        if (instancia == null) {
            instancia = new Modulos();            
        }
        return instancia;
    }
    

    /**
     *
     * @param conexao
     * @param tabela
     * @param campo
     * @param id
     * @return
     */
    public Object buscarObject(Conexao conexao, String tabela, String campo, int id) {
        return conexao.buscarObject(tabela, campo, id);
    }

    /**
     *
     * @param conexao
     * @param tabela
     * @param campo
     * @param id
     * @param arg
     * @param usuario
     * @return
     */   
    public int setObject(Conexao conexao, String tabela,String campo, int id, Object arg, int usuario) {
        return buscarModulo(tabela).setObject(conexao, campo, id, arg, usuario);
    }

    private Modulo buscarModulo(String nome) {
        switch (nome) {
            case "NOTA":
                return ModuloNota.getInstancia();
                
            case "CONFIGURACAO":
                return ModuloConfiguracao.getInstancia();

            case "CONTA":
                return ModuloConta.getInstancia();

            case "CONTRATO":
                return ModuloContrato.getInstancia();

            case "CONTROLE":
                return ModuloControle.getInstancia();

            case "FINANCEIRO":
                return ModuloFinanceiro.getInstancia();

            case "FORNECEDOR":
                return ModuloFornecedor.getInstancia();

            case "PRODUTO":
                return bancodados.modulo.ModuloProduto.getInstancia();

            case "UTIL":
                return ModuloUtil.getInstancia();

            default:
                return new Modulo() {

                    @Override
                    public void iniciar() {
                    }

                    @Override
                    public void fechar() {
                    }

                    @Override
                    public int setObject(Conexao conexao, String campo, int id, Object arg, int usuario) {
                        return 0;
                    }
                };
        }
    }


}
