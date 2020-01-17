/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Nota.item;

import Classes.Base.Item;

/**
 *
 * @author guilherme.machado
 */
public class ItemNota extends Item {

    private long retencaoEmpreiteiro, baseISSQN, valorISSQN, baseCSRF, valorCSRF, baseIRRF, valorIRRF, baseINSS, valorINSS, valorLiquido;
    private int idContrato, idNota, cfop, codigo,idRetencao;
    private float aliquotaISSQN, aliquotaCSRF, aliquotaIRRF, aliquotaINSS;

    /**
     *
     * @return
     */
    public long getRetencaoEmpreiteiro() {
        return retencaoEmpreiteiro;
    }

    /**
     *
     * @param retencaoEmpreiteiro
     */
    public void setRetencaoEmpreiteiro(long retencaoEmpreiteiro) {
        this.retencaoEmpreiteiro = retencaoEmpreiteiro;
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
     * @param valor
     */
    public void somarTotal(long valor) {
        setValor(getValor() + valor);
    }

    /**
     *
     * @return
     */
    public int getIdNota() {
        return idNota;
    }

    /**
     *
     * @param idNota
     */
    public void setIdNota(int idNota) {
        this.idNota = idNota;
    }

    /**
     *
     * @return
     */
    public long getValorISSQN() {
        return valorISSQN;
    }

    /**
     *
     * @param valorISSQN
     */
    public void setValorISSQN(long valorISSQN) {
        this.valorISSQN = valorISSQN;
    }

    /**
     *
     * @return
     */
    public long getBaseCSRF() {
        return baseCSRF;
    }

    /**
     *
     * @param baseCSRF
     */
    public void setBaseCSRF(long baseCSRF) {
        this.baseCSRF = baseCSRF;
    }

    /**
     *
     * @return
     */
    public long getValorCSRF() {
        return valorCSRF;
    }

    /**
     *
     * @param valorCSRF
     */
    public void setValorCSRF(long valorCSRF) {
        this.valorCSRF = valorCSRF;
    }

    /**
     *
     * @return
     */
    public long getBaseIRRF() {
        return baseIRRF;
    }

    /**
     *
     * @param baseIRRF
     */
    public void setBaseIRRF(long baseIRRF) {
        this.baseIRRF = baseIRRF;
    }

    /**
     *
     * @return
     */
    public long getValorIRRF() {
        return valorIRRF;
    }

    /**
     *
     * @param valorIRRF
     */
    public void setValorIRRF(long valorIRRF) {
        this.valorIRRF = valorIRRF;
    }

    /**
     *
     * @return
     */
    public long getBaseINSS() {
        return baseINSS;
    }

    /**
     *
     * @param baseINSS
     */
    public void setBaseINSS(long baseINSS) {
        this.baseINSS = baseINSS;
    }

    /**
     *
     * @return
     */
    public long getValorINSS() {
        return valorINSS;
    }

    /**
     *
     * @param valorINSS
     */
    public void setValorINSS(long valorINSS) {
        this.valorINSS = valorINSS;
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

    /**
     *
     * @return
     */
    public int getCfop() {
        return cfop;
    }

    /**
     *
     * @param cfop
     */
    public void setCfop(int cfop) {
        this.cfop = cfop;
    }

    /**
     *
     * @return
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     *
     * @param codigo
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     *
     * @return
     */
    public float getAliquotaISSQN() {
        return aliquotaISSQN;
    }

    /**
     *
     * @param aliquotaISSQN
     */
    public void setAliquotaISSQN(float aliquotaISSQN) {
        this.aliquotaISSQN = aliquotaISSQN;
    }

    /**
     *
     * @return
     */
    public float getAliquotaCSRF() {
        return aliquotaCSRF;
    }

    /**
     *
     * @param aliquotaCSRF
     */
    public void setAliquotaCSRF(float aliquotaCSRF) {
        this.aliquotaCSRF = aliquotaCSRF;
    }

    /**
     *
     * @return
     */
    public float getAliquotaIRRF() {
        return aliquotaIRRF;
    }

    /**
     *
     * @param aliquotaIRRF
     */
    public void setAliquotaIRRF(float aliquotaIRRF) {
        this.aliquotaIRRF = aliquotaIRRF;
    }

    /**
     *
     * @return
     */
    public float getAliquotaINSS() {
        return aliquotaINSS;
    }

    /**
     *
     * @param aliquotaINSS
     */
    public void setAliquotaINSS(float aliquotaINSS) {
        this.aliquotaINSS = aliquotaINSS;
    }

    /**
     *
     * @return
     */
    public long getBaseISSQN() {
        return baseISSQN;
    }

    /**
     *
     * @param baseISSQN
     */
    public void setBaseISSQN(long baseISSQN) {
        this.baseISSQN = baseISSQN;
    }

    @Override
    public void setPrecoTotal(long precoTotal) {
        super.setPrecoTotal(precoTotal);
        this.baseCSRF = precoTotal;
        this.baseINSS = precoTotal;
        this.baseIRRF = precoTotal;
        this.baseISSQN = precoTotal;
    }

    /**
     *
     * @return
     */
    public int getIdRetencao() {
        return idRetencao;
    }

    /**
     *
     * @param idRetencao
     */
    public void setIdRetencao(int idRetencao) {
        this.idRetencao = idRetencao;
    }

    
}
