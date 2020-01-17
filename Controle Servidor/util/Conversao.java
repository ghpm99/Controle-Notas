/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;


import Classes.Contrato.Contrato;
import Classes.Contrato.Item.ItemContrato;
import Classes.Contrato.Mapa.MapaContrato;
import Classes.Nota.Nota;
import Classes.Nota.item.ItemNota;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author guilherme.machado
 */
public class Conversao{

    /**
     *
     */
    public static NumberFormat dinheiro = NumberFormat.getCurrencyInstance();

    /**
     *
     */
    public static DecimalFormat quantidade = new DecimalFormat("0.00");

    /**
     *
     * @param format
     * @param valor
     * @return
     */
    public static String longToString(NumberFormat format, long valor){
        return format.format(valor / 100.0);
    }

    /**
     *
     * @param format
     * @param valor
     * @return
     * @throws ParseException
     */
    public static long stringToLong(NumberFormat format, String valor) throws ParseException{
        return (long) (format.parse(valor).doubleValue() * 100);
    }

    /**
     *
     * @param contrato
     * @return
     */
    public static Nota contratoToNota(Contrato contrato) {
        Nota nota = new Nota();
        nota.setIdFornecedor(contrato.getIdFornecedor()); 
        nota.setFornecedor(contrato.getFornecedor());
        nota.setDescricao(contrato.getDescricao());
        nota.setFaturamentoDireto(contrato.getNumero() + " " + contrato.getFornecedor());
        nota.setValorTotal(contrato.getSaldoTotal());
        nota.setIdContrato(contrato.getId());
        nota.setPreNota(true);
        HashMap<String, ItemNota> itens = new HashMap<>();
        for (MapaContrato mapa : contrato.getMapas()) {
            for (ItemContrato itemCon : mapa.getItens()) {
                ItemNota item = gerarItemNota(itemCon);
                if (itens.containsKey(item.getItem())) {
                    ItemNota temp = itens.get(item.getItem());
                    temp.setValor(temp.getValor() + item.getValor());

                    temp.setNumeroMapa(temp.getNumeroMapa().contains(item.getNumeroMapa()) ? temp.getNumeroMapa() : temp.getNumeroMapa() + ";" + item.getNumeroMapa());
                    if (temp.getNumeroMapa().length() > 64) {
                        temp.setNumeroMapa(temp.getNumeroMapa().substring(0, 64));
                    }
                } else {
                    itens.put(item.getItem(), item);
                }
            }
        }
        HashMap<String, ItemNota> itensOrdenado = itens.entrySet().stream().sorted((e1, e2) -> e1.getValue().getItem().compareTo(e2.getValue().getItem()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        nota.setItens(new ArrayList<>(itensOrdenado.values()));
        return nota;
    }

    /**
     *
     * @param itemCon
     * @return
     */
    public static ItemNota gerarItemNota(ItemContrato itemCon) {
        ItemNota item = new ItemNota();               
        item.setItem(itemCon.getItem());
        item.setPlano(itemCon.getPlano());
        item.setTipo(itemCon.getTipo());
        item.setCusto(itemCon.getCusto());
        item.setValor(itemCon.getSaldo());
        item.setDescricao(itemCon.getDescricao());
        item.setNumeroMapa(itemCon.getNumeroMapa());
        item.setDescricaoMapa(itemCon.getDescricaoMapa());
        item.setUnidade(itemCon.getUnidade());
        item.setIdContrato(itemCon.getId());
        return item;
    }
}
