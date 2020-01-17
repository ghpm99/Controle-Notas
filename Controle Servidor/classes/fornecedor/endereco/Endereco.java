/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Fornecedor.Endereco;

import Util.Dado;

/**
 *
 * @author guilherme.machado
 */
public class Endereco extends Dado {

    private String nome = "", endereço = "", codigoPostal = "", rua = "", numero = "", cidade = "", distrito = "", estado = "", pais = "";

    /**
     *
     * @return
     */
    public String getNome() {
        return nome;
    }

    /**
     *
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     *
     * @return
     */
    public String getEndereço() {
        return endereço;
    }

    /**
     *
     * @param endereço
     */
    public void setEndereço(String endereço) {
        this.endereço = endereço;
    }

    /**
     *
     * @return
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }

    /**
     *
     * @param codigoPostal
     */
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     *
     * @return
     */
    public String getRua() {
        return rua;
    }

    /**
     *
     * @param rua
     */
    public void setRua(String rua) {
        this.rua = rua;
    }

    /**
     *
     * @return
     */
    public String getNumero() {
        return numero;
    }

    /**
     *
     * @param numero
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     *
     * @return
     */
    public String getCidade() {
        return cidade;
    }

    /**
     *
     * @param cidade
     */
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    /**
     *
     * @return
     */
    public String getDistrito() {
        return distrito;
    }

    /**
     *
     * @param distrito
     */
    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    /**
     *
     * @return
     */
    public String getEstado() {
        return estado;
    }

    /**
     *
     * @param estado
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     *
     * @return
     */
    public String getPais() {
        return pais;
    }

    /**
     *
     * @param pais
     */
    public void setPais(String pais) {
        this.pais = pais;
    }

}
