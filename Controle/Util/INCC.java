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

    public Date getMes(){
        return mes;
    }

    public void setMes(Date mes){
        this.mes = mes;
    }

    public double getIndice(){
        return indice;
    }

    public void setIndice(double indice){
        this.indice = indice;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

}
