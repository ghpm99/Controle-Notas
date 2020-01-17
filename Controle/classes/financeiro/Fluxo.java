/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.financeiro;

import Util.Dado;
import java.util.Date;

/**
 * @author Guilherme
 */
public class Fluxo extends Dado{

    private long valor, saldo, antes;
    private int operacao, nota;

    public long getValor(){
        return valor;
    }

    public void setValor(long valor){
        this.valor = valor;
    }

    public long getSaldo(){
        return saldo;
    }

    public void setSaldo(long saldo){
        this.saldo = saldo;
    }

    public long getAntes(){
        return antes;
    }

    public void setAntes(long antes){
        this.antes = antes;
    }

    public int getOperacao(){
        return operacao;
    }

    public void setOperacao(int operacao){
        this.operacao = operacao;
    }

    public int getNota(){
        return nota;
    }

    public void setNota(int nota){
        this.nota = nota;
    }

}
