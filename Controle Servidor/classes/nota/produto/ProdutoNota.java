/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Nota.produto;

import Classes.Base.Produto;

/**
 * @author Guilherme
 */
public class ProdutoNota extends Produto {

    private int NCM, CFOP;
    private double qCom, vUnCom, vProd;

    /**
     *
     * @return
     */
    public int getNCM() {
        return NCM;
    }

    /**
     *
     * @param NCM
     */
    public void setNCM(int NCM) {
        this.NCM = NCM;
    }

    /**
     *
     * @return
     */
    public int getCFOP() {
        return CFOP;
    }

    /**
     *
     * @param CFOP
     */
    public void setCFOP(int CFOP) {
        this.CFOP = CFOP;
    }

    /**
     *
     * @return
     */
    public double getqCom() {
        return qCom;
    }

    /**
     *
     * @param qCom
     */
    public void setqCom(double qCom) {
        this.qCom = qCom;
    }

    /**
     *
     * @return
     */
    public double getvUnCom() {
        return vUnCom;
    }

    /**
     *
     * @param vUnCom
     */
    public void setvUnCom(double vUnCom) {
        this.vUnCom = vUnCom;
    }

    /**
     *
     * @return
     */
    public double getvProd() {
        return vProd;
    }

    /**
     *
     * @param vProd
     */
    public void setvProd(double vProd) {
        this.vProd = vProd;
    }
    
    

}
