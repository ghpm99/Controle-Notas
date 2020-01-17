/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.financeiro;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author guilherme.machado
 */
public class Diario {

    private int id;

    private Date pagamento = new Date();

    private boolean aprovado;
    
    private long total;

    private ArrayList<NotaFinanceiro> notas = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getPagamento() {
        return pagamento;
    }

    public void setPagamento(Date pagamento) {
        this.pagamento = pagamento;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }

    public ArrayList<NotaFinanceiro> getNotas() {
        return notas;
    }

    public void setNotas(ArrayList<NotaFinanceiro> notas) {
        this.notas = notas;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
    
    
}
