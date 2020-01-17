/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

/**
 *
 * @author Guilherme
 */
public class Comparador {
    
    private String[] arg0;
    private boolean[] arg1;
    private Vencimentos arg2;

    /**
     *
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public Comparador(String[] arg0, boolean[] arg1, Vencimentos arg2) {
        this.arg0 = arg0;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    /**
     *
     * @return
     */
    public String[] getArg0() {
        return arg0;
    }

    /**
     *
     * @return
     */
    public boolean[] getArg1() {
        return arg1;
    }

    /**
     *
     * @return
     */
    public Vencimentos getArg2() {
        return arg2;
    }
    
    
    
}
