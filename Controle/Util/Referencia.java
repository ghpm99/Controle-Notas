/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Guilherme
 */
public class Referencia {

    private static Referencia instancia;
    private Properties pro = new Properties();
    XStream stream = new XStream(new DomDriver());
    
    private Referencia() {
        try {
            pro.load(getLocal());
        } catch (IOException ex) {
            Logger.getLogger(Referencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return
     */
    public static Referencia getInstancia() {
        if (instancia == null) {
            instancia = new Referencia();
        }
        return instancia;
    }

    private InputStream getLocal() throws FileNotFoundException {
        return new FileInputStream("caminhos.txt");
    }

    /**
     *
     * @return
     */
    public String getLocalRede() {
        return pro.getProperty("rede");
    }

    /**
     *
     * @return
     */
    public String getLocalBackup() {
        return pro.getProperty("local");
    }

    /**
     *
     * @return
     */
    public String getId() {
        return pro.getProperty("id");
    }
    
    /**
     *
     * @return
     */
    public String getBD() {
        return pro.getProperty("bd");
    }
    //config

    /**
     *
     * @return
     */
    public String getConfigServidor() {
        return pro.getProperty("config");
    }
    
    public String getIp() {
        return pro.getProperty("ip");
    }
    /**
     *
     * @return
     */
    public String getCategoria() {
        return pro.getProperty("categoria") + "categoria.xml";
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        try {
            //System.err.println(id);
            pro.setProperty("id", String.valueOf(id));
            pro.store(new FileOutputStream("caminhos.txt"), new Date().toString());
            pro.load(getLocal());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
}
