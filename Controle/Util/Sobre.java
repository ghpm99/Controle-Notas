/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Guilherme
 */
public class Sobre {

    private static final String VERSAO = "5.010";
    
    private static final String DATA = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(new File(System.getProperty("user.dir") + "\\Controle.jar").lastModified()));
    
    public static String getMensagem(){        
        return "Versão: " + VERSAO + " Compilação: " + DATA;
    }  
  
}
