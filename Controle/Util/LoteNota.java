/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.Date;

/**
 *
 * @author guilherme.machado
 */
public class LoteNota {

    private boolean dataSelect, classificadaSelect, classificada;
    private Date data;

    /**
     *
     * @return
     */
    public boolean isDataSelect() {
        return dataSelect;
    }

    /**
     *
     * @param dataSelect
     */
    public void setDataSelect(boolean dataSelect) {
        this.dataSelect = dataSelect;
    }

    /**
     *
     * @return
     */
    public boolean isClassificadaSelect() {
        return classificadaSelect;
    }

    /**
     *
     * @param classificadaSelect
     */
    public void setClassificadaSelect(boolean classificadaSelect) {
        this.classificadaSelect = classificadaSelect;
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
    public Date getData() {
        return data;
    }

    /**
     *
     * @param data
     */
    public void setData(Date data) {
        this.data = data;
    }
    
}
