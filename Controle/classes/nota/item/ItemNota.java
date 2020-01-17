/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.nota.item;

import classes.base.Item;

public class ItemNota extends Item {

    private int idContrato, idNota, cfop, codigo;

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public void somarTotal(long valor) {
        setValor(getValor() + valor);
    }

    public int getIdNota() {
        return idNota;
    }

    public void setIdNota(int idNota) {
        this.idNota = idNota;
    }

    public int getCfop() {
        return cfop;
    }

    public void setCfop(int cfop) {
        this.cfop = cfop;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

}
