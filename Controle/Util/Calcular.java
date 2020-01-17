/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Conexao.Cliente;
import classes.contrato.Contrato;
import classes.contrato.item.ItemContrato;
import classes.contrato.mapa.MapaContrato;
import classes.financeiro.Diario;
import classes.financeiro.NotaFinanceiro;
import classes.nota.Nota;
import classes.nota.item.ItemNota;

/**
 * @author Guilherme
 */
public class Calcular {

    /**
     *
     * @param con
     */
    public static void calcularContrato(Contrato con) {
        con.getMapas().stream().forEach(s -> {
            calcularMapaContrato(s);
        });
        long total = 0;
        long gasto = 0;
        total = con.getMapas().stream().map((d) -> d.getTotal()).reduce(total, (accumulator, _item) -> accumulator + _item);
        gasto = con.getMapas().stream().map((d) -> d.getValor()).reduce(gasto, (accumulator, _item) -> accumulator + _item);
        long saldo = total - gasto;
        con.setValorTotal(total);
        con.setGastoTotal(gasto);
        con.setSaldoTotal(saldo);
    }

    public static void calcularMapaContrato(MapaContrato mapa) {
        mapa.getItens().forEach(s -> calcularItemContrato(s));
        long total = 0;
        long gasto = 0;
        total = mapa.getItens().stream().map((d) -> d.getPrecoTotal()).reduce(total, (accumulator, _item) -> accumulator + _item);
        gasto = mapa.getItens().stream().map((d) -> d.getValor()).reduce(gasto, (accumulator, _item) -> accumulator + _item);
        long saldo = total - gasto;
        mapa.setTotal(total);
        mapa.setSaldo(saldo);
        mapa.setValor(gasto);
    }

    private static void calcularItemContrato(ItemContrato item) {
        item.setSaldo(item.getPrecoTotal() - item.getValor());
    }

    /**
     *
     * @param nota
     */
    public static void calcularNota(Nota nota) {
        nota.getItens().stream().forEach(s -> calcularItemNota(s, nota.isPreNota()));
        long gasto = 0;
        long total = 0;
        long issqn = 0;
        long irrf = 0;
        long inss = 0;
        long csrf = 0;
        long valorLiquido = 0;

        gasto = nota.getItens().stream().map(s -> s.getPrecoTotal()).reduce(gasto, (acumulador, item) -> acumulador + item);
        total = nota.getItens().stream().map(s -> s.getValor()).reduce(total, (acumulador, item) -> acumulador + item);
        
        nota.setValorTotal(total);
        nota.setGastoTotal(gasto);
        nota.setIssqn(issqn);
        nota.setIrrf(irrf);
        nota.setInss(inss);
        nota.setCsrf(csrf);
        nota.setValorLiquido(valorLiquido);
        nota.setValorImpostos(gasto - valorLiquido);
        nota.setSaldoTotal(nota.getValorTotal() - nota.getGastoTotal());
    }

    private static void calcularItemNota(ItemNota item, boolean preNota) {
        if (item.getPrecoTotal() == 0 && item.getPrecoUnitario() > 0 && item.getQntTotal() > 0) {
            item.setPrecoTotal((item.getPrecoUnitario() * item.getQntTotal()) / 100);
        }
        long saldo = item.getValor() - item.getPrecoTotal();

        if (item.getPrecoTotal() > 0 && item.getPrecoUnitario() > 0 && item.getQntTotal() == 0) {
            item.setQntTotal((item.getPrecoTotal() / item.getPrecoUnitario()) * 100);
        } else if (item.getPrecoTotal() > 0 && item.getPrecoUnitario() == 0 && item.getQntTotal() > 0) {
            item.setPrecoUnitario((item.getPrecoTotal() / item.getQntTotal()) * 100);
        }
        if (preNota) {
            item.setSaldo(saldo);

        } else {
            item.setSaldo(item.getValor());
        } 
       
    }

    private static long calcularValor(long base, float aliquota) {
        return (long) (base * (aliquota / 100));
    }
    
    public static void calcularDiario(Diario diario){
        long gasto = 0;
        diario.getNotas().forEach(s -> calcularNotasFinanceiro(s));
        gasto = diario.getNotas().stream().map((d) -> d.getValorLiquido()).reduce(gasto, (accumulator, _item) -> accumulator + _item);
        diario.setTotal(gasto);
    }
    
    public static void calcularNotasFinanceiro(NotaFinanceiro nota){
        long total = nota.getValor() + nota.getJuros();
        total -= nota.getDesconto();  
        Cliente.INSTANCIA.atualizarValorLiquidoNotaFinanceiro(nota.getId(), total);
        nota.setValorLiquido(total);
    }

}
