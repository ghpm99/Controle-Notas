/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package classes.nota.mapa;

import classes.nota.item.ItemNota;
import classes.base.Mapa;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Guilherme
 */
public class MapaNota extends Mapa{

    private ArrayList<ItemNota> itens = new ArrayList<>();

    public ArrayList<ItemNota> getItens(){
        return itens;
    }

    /**
     *
     * @param itens
     */
    public void setItens(ArrayList<ItemNota> itens){
        this.itens = itens;
    }
    
    /**
     *
     * @param item
     */
    public void addItem(ItemNota item){
        this.itens.add(item);
    }
    
    /**
     *
     * @param index
     * @return
     */
    public ItemNota getItem(int index){
        return itens.get(index);
    }
}
