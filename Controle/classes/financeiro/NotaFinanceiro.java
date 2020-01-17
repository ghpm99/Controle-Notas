/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.financeiro;

import classes.nota.cobranca.Pagamento;
import java.util.Date;

/**
 * @author Guilherme
 */
public class NotaFinanceiro extends Pagamento {

    private int idDiario;
    private boolean baixada, classificada;
    private long desconto, juros, valorLiquido;
    private String numero = "", fornecedor = "", cnpj = "";
    private Date fluxo = new Date(), vencimentoNota = new Date();

    public int getIdDiario() {
        return idDiario;
    }

    public void setIdDiario(int idDiario) {
        this.idDiario = idDiario;
    }

    public boolean isBaixada() {
        return baixada;
    }

    public void setBaixada(boolean baixada) {
        this.baixada = baixada;
    }

    public long getDesconto() {
        return desconto;
    }

    public void setDesconto(long desconto) {
        this.desconto = desconto;
    }

    public long getJuros() {
        return juros;
    }

    public void setJuros(long juros) {
        this.juros = juros;
    }

    public long getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(long valorLiquido) {
        this.valorLiquido = valorLiquido;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public boolean isClassificada() {
        return classificada;
    }

    public void setClassificada(boolean classificada) {
        this.classificada = classificada;
    }

    public Date getFluxo() {
        return fluxo;
    }

    public void setFluxo(Date fluxo) {
        this.fluxo = fluxo;
    }

    public Date getVencimentoNota() {
        return vencimentoNota;
    }

    public void setVencimentoNota(Date vencimentoNota) {
        this.vencimentoNota = vencimentoNota;
    }
    
    
}
