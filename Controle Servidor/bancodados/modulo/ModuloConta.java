/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDados.modulo;

import BancoDados.Conexao;
import Seguranca.Login.Conta;
import java.util.ArrayList;

/**
 *
 * @author guilherme.machado
 */
public class ModuloConta implements Modulo{

    
     private static ModuloConta instancia;

    /**
     *
     * @return
     */
    public static ModuloConta getInstancia() {
        if (instancia == null) {
            instancia = new ModuloConta();
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
     * @param login
     * @param senha
     * @return
     */
    public int logar(Conexao banco, String login, String senha) {
        Conta conta = banco.buscarConta(login);
        if (conta.isBlock()) {
            return -1;
        }
        if (senha.equals(conta.getSenha())) {
            banco.logou(conta.getId());
            return conta.getId();
        } else {
            return -1;
        }
    }
    
    /**
     *
     * @param banco
     * @param id
     * @return
     */
    public Conta buscarConta(Conexao banco,int id){
        return banco.buscarConta(id);
    }
    
    /**
     *
     * @param banco
     * @param id
     * @param nivel
     * @return
     */
    public ArrayList<Conta> listarContas(Conexao banco,int id, int nivel) {
        return banco.listarContas( id, nivel);
    }
    
    /**
     *
     * @param banco
     * @param conta
     * @param usuario
     * @param nivel
     * @return
     */
    public int atualizarConta(Conexao banco,Conta conta, int usuario, int nivel) {
        return banco.atualizarConta( conta, usuario, nivel);
    }
    
    /**
     *
     * @param banco
     * @param usuario
     * @param nivel
     * @return
     */
    public Conta incluirConta(Conexao banco,int usuario, int nivel) {
        return banco.incluirConta( usuario, nivel);
    }
    
    /**
     *
     * @param banco
     * @param id
     * @param senha
     * @param usuario
     * @param nivel
     * @return
     */
    public int atualizarSenhaConta(Conexao banco,int id, String senha, int usuario, int nivel) {
        return banco.atualizarSenhaConta( id, senha, usuario, nivel);
    }

    @Override
    public int setObject(Conexao conexao, String campo, int id, Object arg, int usuario) {
        return 0;
    }
}
