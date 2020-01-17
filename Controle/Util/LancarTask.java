/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

/**
 *
 * @author guilherme.machado
 * @param <Nota>
 */
public class LancarTask<Nota> extends CustomTask<Nota> {

    private Nota dado;

    public LancarTask(Nota dado) {
        this.dado = dado;
    }    

    @Override
    protected Nota executar() throws Exception {
        return null;
    }

}
