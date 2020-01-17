/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDados.modulo;

import BancoDados.Conexao;
import Classes.Fornecedor.Contato.Contato;
import Classes.Fornecedor.Fornecedor;
import java.util.ArrayList;

/**
 *
 * @author guilherme.machado
 */
public class ModuloFornecedor implements Modulo {

    private static ModuloFornecedor instancia;

    /**
     *
     * @return
     */
    public static ModuloFornecedor getInstancia() {
        if (instancia == null) {
            instancia = new ModuloFornecedor();
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
     * @param limite
     * @return
     */
    public ArrayList<Fornecedor> buscarUltimosFornecedores(Conexao banco, int limite) {
        return banco.buscarUltimosFornecedores(limite);
    }

    /**
     *
     * @param banco
     * @param id
     * @return
     */
    public Fornecedor buscarFornecedor(Conexao banco, int id) {
        return banco.buscarFornecedor(id);
    }

    /**
     *
     * @param banco
     * @param idConta
     * @param campo
     * @param valor
     * @return
     */
    public ArrayList<Fornecedor> buscarFornecedorCampo(Conexao banco, int idConta, String campo, String valor) {
        return banco.buscarFornecedorCampo(idConta, campo, valor);
    }

    /**
     *
     * @param banco
     * @param autor
     * @return
     */
    public Fornecedor incluirFornecedor(Conexao banco, int autor) {
        
        return banco.buscarFornecedor(banco.incluirFornecedor(autor));
    }

    /**
     *
     * @param banco
     * @param idFornecedor
     * @param usuario
     * @return
     */
    public int incluirContatoFornecedor(Conexao banco, int idFornecedor, int usuario) {
        return banco.incluirContatoFornecedor(idFornecedor, usuario);
    }

    /**
     *
     * @param banco
     * @param idFornecedor
     * @return
     */
    public ArrayList<Contato> listarContatoFornecedor(Conexao banco, int idFornecedor) {
        return banco.listarContatoFornecedor(idFornecedor);
    }

    @Override
    public int setObject(Conexao conexao, String campo, int id, Object arg, int usuario) {
        switch (campo) {
            case "RAZAOSOCIAL":
                return atualizarRazaoSocial(conexao, id, arg, usuario);
            case "NOMEFANTASIA":
                return atualizarNomeFantasia(conexao, id, arg, usuario);
            case "SIGLA":
                return atualizarSigla(conexao, id, arg, usuario);
            case "TIPOEMPRESA":
                return atualizarTipoEmpresa(conexao, id, arg, usuario);
            case "CNPJ":
                return atualizarCnpj(conexao, id, arg, usuario);
            case "INSCESTADUAL":
                return atualizarInscEstadual(conexao, id, arg, usuario);
            case "INSCMUNICIPAL":
                return atualizarInscMunicipal(conexao, id, arg, usuario);
            case "NATUREZA":
                return atualizarNatureza(conexao, id, arg, usuario);
            default:
                return 0;
        }
    }

    private int atualizarRazaoSocial(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("FORNECEDOR", "RAZAOSOCIAL", id, arg, usuario);
    }

    private int atualizarNomeFantasia(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("FORNECEDOR", "NOMEFANTASIA", id, arg, usuario);
    }

    private int atualizarSigla(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("FORNECEDOR", "SIGLA", id, arg, usuario);
    }

    private int atualizarTipoEmpresa(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("FORNECEDOR", "TIPOEMPRESA", id, arg, usuario);
    }

    private int atualizarCnpj(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("FORNECEDOR", "CNPJ", id, arg, usuario);
    }

    private int atualizarInscEstadual(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("FORNECEDOR", "INSCESTADUAL", id, arg, usuario);
    }

    private int atualizarInscMunicipal(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("FORNECEDOR", "INSCMUNICIPAL", id, arg, usuario);
    }

    private int atualizarNatureza(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("FORNECEDOR", "NATUREZA", id, arg, usuario);
    }

}
