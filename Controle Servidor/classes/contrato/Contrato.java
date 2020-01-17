/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Contrato;

import Classes.Base.Custo;
import Classes.Contrato.Item.ItemContrato;
import Classes.Contrato.Mapa.MapaContrato;
import java.util.ArrayList;

/**
 *
 * @author guilherme.machado
 */
public class Contrato extends Custo{

    
    private ArrayList<MapaContrato> mapas = new ArrayList<>();

    /**
     *
     * @return
     */
    public ArrayList<MapaContrato> getMapas(){
        return mapas;
    }

    /**
     *
     * @param mapas
     */
    public void setMapas(ArrayList<MapaContrato> mapas){
        if(mapas == null)return;
        this.mapas = mapas;
    }

    /**
     *
     * @param mapa
     */
    public void adicionarMapa(MapaContrato mapa){
        if(mapa == null)return;
        this.mapas.add(mapa);
    }

    /**
     *
     * @param mapa
     */
    public void removerMapa(MapaContrato mapa){
        if(mapa == null)return;
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
