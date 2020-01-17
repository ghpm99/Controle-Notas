/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.nota.cobranca;

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

    public int getIdNota(){
        return idNota;
    }

    public void setIdNota(int idNota){
        this.idNota = idNota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public Date getVencimentoReal() {
        return vencimentoReal;
    }

    public void setVencimentoReal(Date vencimentoReal) {
        this.vencimentoReal = vencimentoReal;
    }

    public long getValor() {
        return valor;
    }

    public void setValor(long valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {        
        this.tipo = tipo;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    
}
