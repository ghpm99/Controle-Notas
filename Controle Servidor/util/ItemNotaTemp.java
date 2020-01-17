/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Util;



/**
 * @author Guilherme
 */
public class ItemNotaTemp {

    private long retencaoEmpreiteiro;
    private int idContrato;

    /**
     *
     * @return
     */
    public long getRetencaoEmpreiteiro(){
        return retencaoEmpreiteiro;
    }

    /**
     *
     * @param retencaoEmpreiteiro
     */
    public void setRetencaoEmpreiteiro(long retencaoEmpreiteiro){
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
    public void somarTotal(long valor){       
      //  setValor(getValor() + valor);
    }
    
    
}
