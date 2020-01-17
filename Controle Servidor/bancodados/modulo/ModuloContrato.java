/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDados.modulo;

import BancoDados.Conexao;
import Classes.Contrato.Contrato;
import Classes.Contrato.Mapa.MapaContrato;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author guilherme.machado
 */
public class ModuloContrato implements Modulo {

    private static ModuloContrato instancia;

    /**
     *
     * @return
     */
    public static ModuloContrato getInstancia() {
        if (instancia == null) {
            instancia = new ModuloContrato();
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
    public ArrayList<Contrato> buscarContratoUltimos(Conexao banco, int limite) {
        return banco.buscarContratoUltimos(limite);
    }

    /**
     *
     * @param banco
     * @param idConta
     * @param campo
     * @param valor
     * @return
     */
    public ArrayList<Contrato> buscarContratoCampo(Conexao banco, int idConta, String campo, String valor) {
        return banco.buscarContratoCampo(idConta, campo, valor);
    }

    /**
     *
     * @param banco
     * @param id
     * @return
     */
    public Contrato buscarContrato(Conexao banco, int id) {
        return banco.buscarContrato(id);
    }

    /**
     *
     * @param banco
     * @param contrato
     * @param usuario
     * @return
     */
    public Contrato incluirContrato(Conexao banco, int usuario) {
        Contrato novo = banco.buscarContrato(banco.incluirContrato(usuario));
        DecimalFormat numero = new DecimalFormat("00000");
        novo.setNumero("CON" + numero.format(novo.getId()));
        alterarNumeroContrato(banco, novo.getId(), novo.getNumero(), usuario);
        return novo;
    }

    /**
     *
     * @param banco
     * @param mapaContrato
     */
    public int incluirMapaContrato(Conexao banco, int autor, int idContrato) {
        return banco.incluirMapaContrato(autor, idContrato);
    }

    /**
     *
     * @param banco
     * @param contrato
     * @param usuario
     * @return
     */
    public int atualizarContrato(Conexao banco, Contrato contrato, int usuario) {
        return banco.atualizarContrato(contrato, usuario);
    }

    /**
     *
     * @param banco
     * @param mapaContrato
     * @return
     */
    public int atualizarMapaContrato(Conexao banco, MapaContrato mapaContrato) {
        return banco.atualizarMapaContrato(mapaContrato);
    }

    /**
     *
     * @param banco
     * @param id
     * @return
     */
    public MapaContrato buscarMapaContrato(Conexao banco, int id) {
        return banco.buscarMapaContrato(id);
    }

    /**
     *
     * @param banco
     * @param itemContrato
     */
    public int incluirItemContrato(Conexao banco, int autor, int idContrato, int idMapa) {
        return banco.incluirItemContrato(autor, idContrato, idMapa);
    }

    /**
     *
     * @param banco
     * @return
     */
    public ArrayList<Contrato> buscarContratoProblema(Conexao banco) {
        return banco.buscarContratoProblema();
    }

    /**
     *
     * @param banco
     * @param id
     * @return
     */
    public ArrayList<Contrato> listarContratosFornecedor(Conexao banco, int id) {
        return banco.listarContratosFornecedor(id);
    }

    @Override
    public int setObject(Conexao conexao, String campo, int id, Object arg, int usuario) {
        switch (campo) {
            case "NUMERO":
                return alterarNumeroContrato(conexao, id, arg, usuario);
            case "EMISSAO":
                return alterarEmissaoContrato(conexao, id, arg, usuario);
            case "RESPONSAVEL":
                return alterarResponsavelContrato(conexao, id, arg, usuario);
            case "DESCRICAO":
                return alterarDescricaoContrato(conexao, id, arg, usuario);
            case "FORNECEDOR":
                return alterarFornecedorContrato(conexao, id, arg, usuario);
            default:
                return 0;

        }
    }

    private int alterarNumeroContrato(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("CONTRATO", "NUMERO", id, arg, usuario);
    }

    private int alterarEmissaoContrato(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("CONTRATO", "EMISSAO", id, arg, usuario);
    }

    private int alterarResponsavelContrato(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("CONTRATO", "AUTOR", id, arg, usuario);
    }

    private int alterarDescricaoContrato(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("CONTRATO", "DESCRICAO", id, arg, usuario);
    }
    
    private int alterarFornecedorContrato(Conexao conexao, int id, Object arg, int usuario){
        return conexao.setObject("CONTRATO", "IDFORNECEDOR", id, arg, usuario);
    }
}
