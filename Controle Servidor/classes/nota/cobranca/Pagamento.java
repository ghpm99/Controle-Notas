/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Nota.cobranca;

import java.util.Date;

/**
 *
 * @author guilherme.machado
 */
public class Pagamento {
    private int id,idNota;
    private Date vencimento = new Date(),vencimentoReal = new Date();
    private long valor;
    private String tipo = "",linha = "",codigo = "";

    /**
     *
     * @return
     */
    public int getIdNota(){
        return idNota;
    }

    /**
     *
     * @param idNota
     */
    public void setIdNota(int idNota){
        this.idNota = idNota;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public Date getVencimento() {
        return vencimento;
    }

    /**
     *
     * @param vencimento
     */
    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    /**
     *
     * @return
     */
    public Date getVencimentoReal() {
        return vencimentoReal;
    }

    /**
     *
     * @param vencimentoReal
     */
    public void setVencimentoReal(Date vencimentoReal) {
        this.vencimentoReal = vencimentoReal;
    }

    /**
     *
     * @return
     */
    public long getValor() {
        return valor;
    }

    /**
     *
     * @param valor
     */
    public void setValor(long valor) {
        this.valor = valor;
    }

    /**
     *
     * @return
     */
    public String getTipo() {
        return tipo;
    }

    /**
     *
     * @param tipo
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     *
     * @return
     */
    public String getLinha() {
        return linha;
    }

    /**
     *
     * @param linha
     */
    public void setLinha(String linha) {
        this.linha = linha;
    }

    /**
     *
     * @return
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     *
     * @param codigo
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    
}
