/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.almoxarife;

import classes.base.Produto;

/**
 *
 * @author guilherme.machado
 */
public class ProdutoAlmoxarife extends Produto {

    private String complemento, destino, localizacao;
    private long estoque;

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public long getEstoque() {
        return estoque;
    }

    public void setEstoque(long estoque) {
        this.estoque = estoque;
    }

}
