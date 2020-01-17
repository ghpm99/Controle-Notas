/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Nota;

import Classes.Base.Custo;
import Classes.Nota.cobranca.Pagamento;
import Classes.Nota.item.ItemNota;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author guilherme.machado
 */
public class Nota extends Custo {
      
    private Date vencimento = new Date();
    private Date vencimentoReal = new Date();
    private boolean preNota;
    private boolean classificada;
    private boolean parcelaFinal;
    private boolean lancada;
    private int ordem,idFaturamentoDireto,idContrato,idPmWeb;
    private long issqn, csrf,inss,irrf,valorImpostos,valorLiquido;
    private String faturamentoDireto = "", serie = "", tipo = "", status = "Submitted";
    private ArrayList<ItemNota> itens = new ArrayList<>();
    private ArrayList<Pagamento> pagamentos = new ArrayList<>();

    @Override
    public void setNumero(String numero) {
        if (numero == null) {
            numero = "";
        }
        if (numero.length() > 32) {
            numero = numero.substring(0, 32);
        }
        super.setNumero(numero.toUpperCase()); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @return
     */
    public int getIdPmWeb(){
        return idPmWeb;
    }

    /**
     *
     * @param idPmWeb
     */
    public void setIdPmWeb(int idPmWeb){
        this.idPmWeb = idPmWeb;
    }

    /**
     *
     * @return
     */
    public String getStatus(){
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status){
        this.status = status;
    } 

    /**
     *
     * @return
     */
    public boolean isLancada() {
        return lancada;
    }

    /**
     *
     * @param lancada
     */
    public void setLancada(boolean lancada) {
        this.lancada = lancada;
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
    public boolean isPreNota() {
        return preNota;
    }

    /**
     *
     * @param preNota
     */
    public void setPreNota(boolean preNota) {
        this.preNota = preNota;
    }

    /**
     *
     * @return
     */
    public boolean isClassificada() {
        return classificada;
    }

    /**
     *
     * @param classificada
     */
    public void setClassificada(boolean classificada) {
        this.classificada = classificada;
    }

    /**
     *
     * @return
     */
    public int getOrdem() {
        return ordem;
    }

    /**
     *
     * @param ordem
     */
    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    /**
     *
     * @return
     */
    public String getFaturamentoDireto() {
        return faturamentoDireto;
    }

    /**
     *
     * @param faturamentoDireto
     */
    public void setFaturamentoDireto(String faturamentoDireto) {
        if (faturamentoDireto == null) {
            faturamentoDireto = "";
        }
        if (faturamentoDireto.length() > 64) {
            faturamentoDireto = faturamentoDireto.substring(0, 64);
        }
        this.faturamentoDireto = faturamentoDireto.toUpperCase();
    }

    /**
     *
     * @return
     */
    public boolean isParcelaFinal() {
        return parcelaFinal;
    }

    /**
     *
     * @param parcelaFinal
     */
    public void setParcelaFinal(boolean parcelaFinal) {
        this.parcelaFinal = parcelaFinal;
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
    public String getSerie() {
        return serie;
    }

    /**
     *
     * @param serie
     */
    public void setSerie(String serie) {
        if (serie == null) {
            serie = "";
        }
        if (serie.length() > 5) {
            serie = serie.substring(0, 5);
        }
        this.serie = serie.toUpperCase();
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
        if (tipo == null) {
            tipo = "";
        }
        if (tipo.length() > 7) {
            tipo = tipo.substring(0, 7);
        }
        this.tipo = tipo.toUpperCase();
    }

    /**
     *
     * @return
     */
    public ArrayList<ItemNota> getItens() {
        return itens;
    }

    /**
     *
     * @param itens
     */
    public void setItens(ArrayList<ItemNota> itens) {
        this.itens = itens;
    }

    /**
     *
     * @param item
     */
    public void addItem(ItemNota item) {
        itens.add(item);
    }

    /**
     *
     * @return
     */
    public int getIdFaturamentoDireto(){
        return idFaturamentoDireto;
    }

    /**
     *
     * @param idFaturamentoDireto
     */
    public void setIdFaturamentoDireto(int idFaturamentoDireto){
        this.idFaturamentoDireto = idFaturamentoDireto;
    }

    @Override
    public void setFornecedor(String fornecedor){
        if(fornecedor.equals("")){
            super.setFornecedor(faturamentoDireto);
        }else{
            super.setFornecedor(fornecedor);
        }
    }

    /**
     *
     * @return
     */
    public int getIdContrato() {
        return idContrato;
    }

    /**
     *
     * @param idContrato
     */
    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    /**
     *
     * @return
     */
    public ArrayList<Pagamento> getPagamentos() {
        return pagamentos;
    }

    /**
     *
     * @param pagamentos
     */
    public void setPagamentos(ArrayList<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }
    
    /**
     *
     * @param pagamento
     */
    public void addPagamento(Pagamento pagamento){
        this.pagamentos.add(pagamento);
    }

    /**
     *
     * @return
     */
    public long getIssqn() {
        return issqn;
    }

    /**
     *
     * @param issqn
     */
    public void setIssqn(long issqn) {
        this.issqn = issqn;
    }

    /**
     *
     * @return
     */
    public long getCsrf() {
        return csrf;
    }

    /**
     *
     * @param csrf
     */
    public void setCsrf(long csrf) {
        this.csrf = csrf;
    }

    /**
     *
     * @return
     */
    public long getInss() {
        return inss;
    }

    /**
     *
     * @param inss
     */
    public void setInss(long inss) {
        this.inss = inss;
    }

    /**
     *
     * @return
     */
    public long getIrrf() {
        return irrf;
    }

    /**
     *
     * @param irrf
     */
    public void setIrrf(long irrf) {
        this.irrf = irrf;
    }

    /**
     *
     * @return
     */
    public long getValorImpostos() {
        return valorImpostos;
    }

    /**
     *
     * @param valorImpostos
     */
    public void setValorImpostos(long valorImpostos) {
        this.valorImpostos = valorImpostos;
    }

    /**
     *
     * @return
     */
    public long getValorLiquido() {
        return valorLiquido;
    }

    /**
     *
     * @param valorLiquido
     */
    public void setValorLiquido(long valorLiquido) {
        this.valorLiquido = valorLiquido;
    }
    
    
}
