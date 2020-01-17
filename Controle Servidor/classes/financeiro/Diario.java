/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Financeiro;

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

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public Date getPagamento() {
        return pagamento;
    }

    /**
     *
     * @param pagamento
     */
    public void setPagamento(Date pagamento) {
        this.pagamento = pagamento;
    }

    /**
     *
     * @return
     */
    public boolean isAprovado() {
        return aprovado;
    }

    /**
     *
     * @param aprovado
     */
    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }

    /**
     *
     * @return
     */
    public ArrayList<NotaFinanceiro> getNotas() {
        return notas;
    }

    /**
     *
     * @param notas
     */
    public void setNotas(ArrayList<NotaFinanceiro> notas) {
        this.notas = notas;
    }

    /**
     *
     * @return
     */
    public long getTotal() {
        return total;
    }

    /**
     *
     * @param total
     */
    public void setTotal(long total) {
        this.total = total;
    }
    
    
}
