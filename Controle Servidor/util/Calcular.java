
package Util;

import Classes.Contrato.Contrato;
import Classes.Contrato.Item.ItemContrato;
import Classes.Contrato.Mapa.MapaContrato;
import Classes.Financeiro.Diario;
import Classes.Financeiro.NotaFinanceiro;
import Classes.Nota.Nota;
import Classes.Nota.item.ItemNota;

/**
 * @author Guilherme
 */
public class Calcular{

    /**
     *
     * @param con
     */
    public static void calcularContrato(Contrato con){
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

    private static void calcularMapaContrato(MapaContrato mapa){
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

    private static void calcularItemContrato(ItemContrato item){
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
        issqn = nota.getItens().stream().map(s -> s.getValorISSQN()).reduce(issqn, (acumulador, item) -> acumulador + item);
        irrf = nota.getItens().stream().map(s -> s.getValorIRRF()).reduce(irrf, (acumulador, item) -> acumulador + item);
        inss = nota.getItens().stream().map(s -> s.getValorINSS()).reduce(inss, (acumulador, item) -> acumulador + item);
        csrf = nota.getItens().stream().map(s -> s.getValorCSRF()).reduce(csrf, (acumulador, item) -> acumulador + item);
        valorLiquido = nota.getItens().stream().map(s -> s.getValorLiquido()).reduce(valorLiquido, (acumulador, item) -> acumulador + item);

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

        item.setValorISSQN(calcularValor(item.getBaseISSQN(), item.getAliquotaISSQN()));
        item.setValorCSRF(calcularValor(item.getBaseCSRF(), item.getAliquotaCSRF()));
        item.setValorINSS(calcularValor(item.getBaseINSS(), item.getAliquotaINSS()));
        item.setValorIRRF(calcularValor(item.getBaseIRRF(), item.getAliquotaIRRF()));

        item.setValorLiquido(item.getPrecoTotal() - item.getValorISSQN() - item.getValorIRRF() - item.getValorINSS() - item.getValorCSRF());
    }

    private static long calcularValor(long base, float aliquota) {
        return (long) (base * (aliquota / 100));
    }
    
    /**
     *
     * @param diario
     */
    public static void calcularDiario(Diario diario){
        long gasto = 0;
        diario.getNotas().forEach(s -> calcularNotasFinanceiro(s));
        gasto = diario.getNotas().stream().map((d) -> d.getValorLiquido()).reduce(gasto, (accumulator, _item) -> accumulator + _item);
        diario.setTotal(gasto);
    }
    
    /**
     *
     * @param nota
     */
    public static void calcularNotasFinanceiro(NotaFinanceiro nota){
        long total = nota.getValor() + nota.getJuros();
        total -= nota.getDesconto();          
        nota.setValorLiquido(total);
    }

    
}
