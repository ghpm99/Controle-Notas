/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexao;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Guilherme
 */
public abstract class Request{

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(bos);

    public abstract void run() throws IOException;

    public byte[] getMensagem(){
        try{
            run();
            byte[] temp = bos.toByteArray();            
            limpar();            
            return temp;
        } catch(IOException ex){
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new byte[]{5};
    }

    public void limpar(){
        bos.reset();
    }

}
