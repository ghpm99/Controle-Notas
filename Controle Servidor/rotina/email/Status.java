/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rotina.Email;

/**
 * @author Guilherme
 */
public class Status{

    String status = "Draft";
    int id = 0;

    /**
     *
     * @return
     */
    public String getStatus(){
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status){
        this.status = status;
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
