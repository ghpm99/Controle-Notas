/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.contrato;

import classes.base.Custo;
import classes.contrato.item.ItemContrato;
import classes.contrato.mapa.MapaContrato;
import java.util.ArrayList;

/**
 *
 * @author guilherme.machado
 */
public class Contrato extends Custo{

    private ArrayList<MapaContrato> mapas = new ArrayList<>();

    public ArrayList<MapaContrato> getMapas(){
        return mapas;
    }

    /**
     *
     * @param mapas
     */
    public void setMapas(ArrayList<MapaContrato> mapas){
        this.mapas = mapas;
    }

    /**
     *
     * @param mapa
     */
    public void adicionarMapa(MapaContrato mapa){
        this.mapas.add(mapa);
    }

    /**
     *
     * @param mapa
     */
    public void removerMapa(MapaContrato mapa){
        this.mapas.remove(mapa);
    }

    /**
     *
     * @return
     */
    public ArrayList<ItemContrato> getItens(){
        ArrayList<ItemContrato> itens = new ArrayList<>();
        getMapas().stream().forEach((tempMapa) -> {
            tempMapa.getItens().stream().map((tempItem) -> {
                tempItem.setNumeroMapa(tempMapa.getNumero());
                tempItem.setDescricaoMapa(tempMapa.getDescricao());
                return tempItem;
            }).forEach((tempItem) -> {
                itens.add(tempItem);
            });
        });
        return itens;
    }

    /**
     *
     * @param item
     * @return
     */
    public MapaContrato getMapa(ItemContrato item){

        for(MapaContrato mapa : getMapas()){
            if(mapa.getNumero().equals(item.getNumeroMapa())){
                return mapa;
            }
        }
        return null;
    }


}
