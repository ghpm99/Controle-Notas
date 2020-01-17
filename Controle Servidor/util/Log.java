/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Configuracao.Referencia;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guilherme.machado
 */
public enum Log {

    /**
     *
     */
    INSTANCIA;
    PrintWriter print;
    FileOutputStream fos = null;

    /**
     *
     */
    public void iniciar() {

        try {
            fos = new FileOutputStream(new File(Referencia.getInstancia().getLogs() + new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date()) + ".log")); 
        } catch (FileNotFoundException ex) {
            try {                
                fos = new FileOutputStream(new File("" + new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date()) + ".log"));
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        print = new PrintWriter(fos);
    }

    /**
     *
     * @param texto
     */
    public void escrever(String texto) {
        print.println(texto);
        print.flush();
    }

    /**
     *
     */
    public void fechar() {
        try {
            print.close();
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
