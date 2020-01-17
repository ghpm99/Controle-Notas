/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import javafx.scene.layout.BorderPane;

/**
 *
 * @author Guilherme
 */
public interface PaneController {
    
    /**
     *
     * @param controller
     */
    public void setPaneController(BorderPane controller);
    
    /**
     *
     * @param modulo
     */
    public void setModuloVoltar(Modulos modulo);
    
    public void succeeded();
}
