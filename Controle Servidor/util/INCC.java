/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author Guilherme
 */
public class INCC implements Serializable{

    private int id = 0;
    private Date mes = new Date(new java.util.Date().getTime());
    private double indice;

    /**
     *
     * @return
     */
    public Date getMes(){
        return mes;
    }

    /**
     *
     * @param mes
     */
    public void setMes(Date mes){
        this.mes = mes;
    }

    /**
     *
     * @return
     */
    public double getIndice(){
        return indice;
    }

    /**
     *
     * @param indice
     */
    public void setIndice(double indice){
        this.indice = indice;
    }

    /**
     *
     * @return
     */
    public int getId(){
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id){
        this.id = id;
    }

}
