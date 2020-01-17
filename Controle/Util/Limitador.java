/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Guilherme
 */
public class Limitador extends PlainDocument {

    private int maximo;

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str != null && (getLength() + str.length() <= maximo)) {
            super.insertString(offs, str, a);
        }
    }

    /**
     *
     * @param max
     */
    public void setMaximo(int max) {
        this.maximo = max;
    }

}
