/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import GuiPrincipal.Main;
import java.util.Date;
import javafx.concurrent.Task;

public abstract class CustomTask<V> extends Task<V> {
   
    
    @Override
    protected V call() throws Exception {
        long antes = new Date().getTime();
        V retorno = executar();
        long depois = new Date().getTime();
        long diferença = depois - antes;
        Main.getInstancia().ping(diferença);
        return retorno;
    }

    protected abstract V executar() throws Exception;
    
    

}
