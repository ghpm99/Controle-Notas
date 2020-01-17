/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracao;

import GUI.principal.Principal;
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
 *
 * @author Guilherme
 */
public class Referencia {

    private static Referencia instancia;

    private Properties pro = new Properties();

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
        return new FileInputStream("servidor.config");
    }

    /**
     *
     * @return
     */
    public String getConfiguracao() {
        return pro.getProperty("config");
    }

    /**
     *
     * @return
     */
    public String getLogs() {
        return pro.getProperty("log");
    }

    /**
     *
     * @return
     */
    public String getPorta() {
        return pro.getProperty("porta");
    }

    /**
     *
     * @return
     */
    public String getArquivos() {
        return pro.getProperty("arquivos");
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return pro.getProperty("email");
    }

    /**
     *
     * @return
     */
    public String getSenhaemail() {
        return pro.getProperty("senhaemail");
    }

    /**
     *
     * @return
     */
    public boolean getAutoiniciar() {
        return pro.getProperty("autoiniciar").equals("1");
    }

    /**
     *
     * @param arg
     */
    public void setConfiguracao(String arg) {
        pro.setProperty("config", arg);
        salvar();
    }

    /**
     *
     * @param arg
     */
    public void setLogs(String arg) {
        pro.setProperty("log", arg);
        salvar();
    }

    /**
     *
     * @param arg
     */
    public void setPorta(String arg) {
        pro.setProperty("porta", arg);
        salvar();
    }

    /**
     *
     * @param arg
     */
    public void setArquivos(String arg) {
        pro.setProperty("arquivos", arg);
        salvar();
    }

    /**
     *
     * @param arg
     */
    public void setEmail(String arg) {
        pro.setProperty("email", arg);
        salvar();
    }

    /**
     *
     * @param arg
     */
    public void setSenhaemail(String arg) {
        pro.setProperty("senhaemail", arg);
        salvar();
    }

    /**
     *
     * @param arg
     */
    public void setAutoiniciar(boolean arg) {
        pro.setProperty("autoiniciar", arg ? "1" : "0");
        salvar();
    }

    private void salvar() {
        try {            
            pro.store(new FileOutputStream("servidor.config"), new Date().toString());
            Principal.setStatus("Configuração atualizada com sucesso");
        } catch (IOException ex) {
            Principal.setStatus("Falha ao atualizar a configuração");
           ex.printStackTrace();
            
        }

    }
    
    /**
     *
     * @return
     */
    public boolean getTarefa(){
        return pro.getProperty("tarefa").equals("1");
    }

}
