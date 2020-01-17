/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDados.modulo;

import BancoDados.Conexao;
import Classes.Nota.Nota;
import Classes.Nota.anexo.AnexoNota;
import Rotina.Email.Status;
import Util.Parametros;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author guilherme.machado
 */
public class ModuloNota implements Modulo {


    private static ModuloNota instancia;

    /**
     *
     * @return
     */
    public static ModuloNota getInstancia() {
        if (instancia == null) {
            instancia = new ModuloNota();
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
     * @return
     */
    public ArrayList<Nota> listarNotasSubmetidas(Conexao banco) {
        return banco.buscarNotasSubmetidas( "Submitted");
    }

    /**
     *
     * @param banco
     * @param status
     * @param usuario
     * @return
     */
    public int workflow(Conexao banco, Status status, int usuario) {

        Status temp2 = banco.buscarPmWeb( status.getId());

        if (temp2.getStatus().equals("approved")) {
            return -1;
        }

        if (temp2.getId() == 0) {
            banco.incluirPmWeb( status);
        } else {
            banco.atualizarPmWeb( status);
        }

        if (status.getStatus().equals("approved")) {
            ArrayList<Nota> notas = banco.buscarNotasIdPMWeb( status.getId());
            notas.forEach(s -> {
                if (!s.isCancelado() && !s.isLancada() && !s.isPreNota()) {
                    banco.aprovarNota( s, usuario);
                }
            });
        } else if (status.getStatus().equals("Withdrawal")) {

        } else if (status.getStatus().equals("Return")) {

        } else if (status.getStatus().equals("Rejection")) {

        }

        return 0;

    }

    /**
     *
     * @param banco
     * @param idConta
     * @param campo
     * @param valor
     * @return
     */
    public ArrayList<Nota> buscarNotaCampo(Conexao banco, int idConta, String campo, String valor) {
        return banco.buscarNotaCampo( idConta, campo, valor);
    }

    /**
     *
     * @param banco
     * @param id
     * @return
     */
    public Nota buscarNotaId(Conexao banco, int id) {
        return banco.buscarNotaId( id);
    }

    /**
     *
     * @param banco
     * @param limite
     * @return
     */
    public ArrayList<Nota> buscarNotaUltimos(Conexao banco, int limite) {
        return banco.buscarNotaUltimos( limite);
    }

    /*
    Mudança no modo como é inserido nota no sistema
    Antes era necessario um contrato e apos isso selecionar um fornecedor
    agora nota é uma entidade independente com vinculo com contrato e fornecedor.
    Caso a nota seja inclusa dentro do fornecedor, ela ja vem com vinculo com aquele fornecedor,
    Caso a nota seja inclusa dentro de um contrato, ela ja vem com o vinculo com aquele contrato,
    Caso a nota seja inclusa com base em outra nota, ela ja vem com o vinculo com o mesmo fornecedor e contrato daquela nota.
    é necessario a instancia do banco de dados, id do fornecedor e id do contrato,
    caso o id do fornecedor nao seja 0 a nota é inclusa com vinculo com aquele fornecedor,
    caso o id do contrato nao seja 0 a nota é inclusa com vinculo com aquele fornecedor.
    */

    /**
     *
     * @param banco
     * @param idFornecedor
     * @param idContrato
     * @return
     */
    
    public Nota incluirNota(Conexao banco,int idFornecedor, int idContrato){
       
       return null; 
    }
    
    /**
     *
     * @param banco
     * @return
     */
    public ArrayList<Nota> buscarPreNotas(Conexao banco) {
        return banco.buscarPreNotas();
    }
    
    /**
     *
     * @param banco
     * @param idNota
     * @param usuario
     * @return
     */
    public int lancarNota(Conexao banco,int idNota, int usuario) {
        return banco.lancarNota( idNota, usuario);
    }
    
    /**
     *
     * @param banco
     * @param id
     * @return
     */
    public ArrayList<Nota> buscarNotasContrato(Conexao banco,int id){
        return banco.buscarNotasContrato( id);
    }
    
    /**
     *
     * @param banco
     * @param parametros
     * @return
     */
    public ArrayList<Nota> buscarNotasRelatorio(Conexao banco,Parametros parametros) {
        return banco.buscarNotasRelatorio( parametros);
    }
    
    /**
     *
     * @param banco
     * @return
     */
    public ArrayList<Nota> buscarNotaProblema(Conexao banco) {
        return banco.buscarNotaProblema();
    }
    
    /**
     *
     * @param banco
     * @param parametros
     * @return
     */
    public ArrayList<Nota> buscarNotasRelatorioOrcamento(Conexao banco,Parametros parametros) {
        return banco.buscarNotasRelatorioOrcamento( parametros);
    }
    
    /**
     *
     * @param banco
     * @param id
     * @return
     */
    public ArrayList<Nota> buscarNotasFornecedor(Conexao banco,int id) {
        return banco.buscarNotasFornecedor( id);
    }
    
    /**
     *
     * @param banco
     * @param nota
     * @param caminho
     * @param tipo
     * @param nome
     * @param usuario
     * @return
     */
    public int incluirAnexoNota(Conexao banco,int nota, String caminho, String tipo, String nome, int usuario) {
        return banco.incluirAnexoNota( nota, caminho, tipo, nome, usuario);
    }
    
    /**
     *
     * @param banco
     * @param id
     * @return
     */
    public ArrayList<AnexoNota> listarAnexosNota(Conexao banco,int id) {
        return banco.listarAnexosNota( id);
    }
    
    /**
     *
     * @param banco
     * @param id
     * @return
     */
    public File buscarAnexoNota(Conexao banco,int id) {
        return banco.buscarAnexoNota( id);
    }
    
    /**
     *
     * @param banco
     * @param nota
     * @param usuario
     * @return
     */
    public int estornarNota(Conexao banco,Nota nota, int usuario) {
        return banco.estornarNota( nota, usuario);
    }
    
    /**
     *
     * @param banco
     * @param idFornecedor
     * @param idNota
     * @param numero
     * @return
     */
    public boolean verificarNumeroNota(Conexao banco,int idFornecedor, int idNota, String numero) {
        return banco.verificarNumeroNota(idFornecedor, idNota, numero);
    }
    
    /**
     *
     * @param banco
     * @param idConta
     * @param idNota
     * @return
     */
    public boolean cancelarNota(Conexao banco,int idConta, int idNota) {
        return banco.cancelarNota(idConta, idNota);
    }

    @Override
    public int setObject(Conexao conexao, String campo, int id, Object arg, int usuario) {
        return 0;
    }
}
