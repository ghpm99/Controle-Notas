/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.nota;

import classes.nota.cobranca.Pagamento;
import classes.nota.item.ItemNota;
import classes.base.Custo;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author guilherme.machado
 */
public class Nota extends Custo{

    private Date vencimento = new Date();
    private Date vencimentoReal = new Date();
    private boolean preNota;
    private boolean classificada;
    private boolean parcelaFinal;
    private boolean lancada;
    private int ordem,idFaturamentoDireto,idContrato,idPmWeb;
    private long issqn, csrf,inss,irrf,valorImpostos,valorLiquido;
    private String faturamentoDireto = "", serie = "", tipo = "",status = "Submitted";
    private ArrayList<ItemNota> itens = new ArrayList<>();
    private ArrayList<Pagamento> pagamentos = new ArrayList<>();
 
    @Override
    public void setNumero(String numero){
        if(numero.length() > 32){
            numero = numero.substring(0, 32);
        }
        super.setNumero(numero.toUpperCase()); //To change body of generated methods, choose Tools | Templates.
    }

    public int getIdPmWeb(){
        return idPmWeb;
    }

    public void setIdPmWeb(int idPmWeb){
        this.idPmWeb = idPmWeb;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    /**
     *
     * @return
     */
    public boolean isLancada(){
        return lancada;
    }

    /**
     *
     * @param lancada
     */
    public void setLancada(boolean lancada){
        this.lancada = lancada;
    }

    /**
     *
     * @return
     */
    public Date getVencimento(){
        return vencimento;
    }

    /**
     *
     * @param vencimento
     */
    public void setVencimento(Date vencimento){
        this.vencimento = vencimento;
    }

    /**
     *
     * @return
     */
    public boolean isPreNota(){
        return preNota;
    }

    /**
     *
     * @param preNota
     */
    public void setPreNota(boolean preNota){
        this.preNota = preNota;
    }

    /**
     *
     * @return
     */
    public boolean isClassificada(){
        return classificada;
    }

    /**
     *
     * @param classificada
     */
    public void setClassificada(boolean classificada){
        this.classificada = classificada;
    }

    /**
     *
     * @return
     */
    public int getOrdem(){
        return ordem;
    }

    /**
     *
     * @param ordem
     */
    public void setOrdem(int ordem){
        this.ordem = ordem;
    }

    /**
     *
     * @return
     */
    public String getFaturamentoDireto(){
        return faturamentoDireto;
    }

    /**
     *
     * @param faturamentoDireto
     */
    public void setFaturamentoDireto(String faturamentoDireto){
        if(faturamentoDireto == null){
            faturamentoDireto = "";
        }
        if(faturamentoDireto.length() > 64){
            faturamentoDireto = faturamentoDireto.substring(0, 64);
        }
        this.faturamentoDireto = faturamentoDireto.toUpperCase();
    }

    /**
     *
     * @return
     */
    public boolean isParcelaFinal(){
        return parcelaFinal;
    }

    /**
     *
     * @param parcelaFinal
     */
    public void setParcelaFinal(boolean parcelaFinal){
        this.parcelaFinal = parcelaFinal;
    }

    public Date getVencimentoReal(){
        return vencimentoReal;
    }

    public void setVencimentoReal(Date vencimentoReal){
        this.vencimentoReal = vencimentoReal;
    }

    public String getSerie(){
        return serie;
    }

    public void setSerie(String serie){
        if(serie.length() > 5){
            serie = serie.substring(0, 5);
        }
        this.serie = serie.toUpperCase();
    }

    public String getTipo(){
        return tipo;
    }

    public void setTipo(String tipo){
        if(tipo.length() > 7){
            tipo = tipo.substring(0, 7);
        }
        this.tipo = tipo.toUpperCase();
    }

    public ArrayList<ItemNota> getItens(){
        return itens;
    }

    public void setItens(ArrayList<ItemNota> itens){
        this.itens = itens;
    }

    public void addItem(ItemNota item){
        itens.add(item);
    }

    
    public int getIdFaturamentoDireto(){
        return idFaturamentoDireto;
    }

    public void setIdFaturamentoDireto(int idFaturamentoDireto){
        this.idFaturamentoDireto = idFaturamentoDireto;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public ArrayList<Pagamento> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(ArrayList<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }

    public long getIssqn() {
        return issqn;
    }

    public void setIssqn(long issqn) {
        this.issqn = issqn;
    }

    public long getCsrf() {
        return csrf;
    }

    public void setCsrf(long csrf) {
        this.csrf = csrf;
    }

    public long getInss() {
        return inss;
    }

    public void setInss(long inss) {
        this.inss = inss;
    }

    public long getIrrf() {
        return irrf;
    }

    public void setIrrf(long irrf) {
        this.irrf = irrf;
    }

    public long getValorImpostos() {
        return valorImpostos;
    }

    public void setValorImpostos(long valorImpostos) {
        this.valorImpostos = valorImpostos;
    }

    public long getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(long valorLiquido) {
        this.valorLiquido = valorLiquido;
    }
    
    
}
