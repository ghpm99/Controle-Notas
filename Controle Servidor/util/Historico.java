/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Util;

/**
 * @author Guilherme
 */
public class Historico {

    private int idEvento,id,idConta;
    private String evento,usuario;
    private long hora;

    /**
     *
     * @return
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     *
     * @param usuario
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    } 
    
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
    public int getIdEvento() {
        return idEvento;
    }

    /**
     *
     * @param idEvento
     */
    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    /**
     *
     * @return
     */
    public int getIdConta() {
        return idConta;
    }

    /**
     *
     * @param idConta
     */
    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }

    /**
     *
     * @return
     */
    public String getEvento() {
        return evento;
    }

    /**
     *
     * @param evento
     */
    public void setEvento(String evento) {
        this.evento = evento;
    }

    /**
     *
     * @return
     */
    public long getHora() {
        return hora;
    }

    /**
     *
     * @param hora
     */
    public void setHora(long hora) {
        this.hora = hora;
    }
    
    
    
}
