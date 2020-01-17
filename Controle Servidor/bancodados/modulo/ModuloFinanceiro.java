/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDados.modulo;

import BancoDados.Conexao;
import Classes.Financeiro.Diario;
import Classes.Financeiro.Fluxo;
import Classes.Financeiro.NotaFinanceiro;
import Classes.Nota.Nota;
import Classes.Nota.cobranca.Pagamento;
import Util.Parametros;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author guilherme.machado
 */
public class ModuloFinanceiro implements Modulo {

    private static ModuloFinanceiro instancia;

    /**
     *
     * @return
     */
    public static ModuloFinanceiro getInstancia() {
        if (instancia == null) {
            instancia = new ModuloFinanceiro();
            instancia.iniciar();
        }
        return instancia;
    }

    /**
     *
     */
    @Override
    public void iniciar() {
    }

    /**
     *
     */
    @Override
    public void fechar() {

        instancia = null;
    }

    /**
     *
     * @param banco
     * @return
     */
    public Diario incluirDiario(Conexao banco) {
        Diario diario = new Diario();
        diario.setId(banco.incluirDiario(new Date(new java.util.Date().getTime())));
        return diario;
    }

    /**
     *
     * @param banco
     * @return
     */
    public ArrayList<Fluxo> listarFluxo(Conexao banco) {

        ArrayList<Fluxo> fluxo = new ArrayList<>();
        diarioParaFluxo(banco.listarDiarios(), fluxo);
        return fluxo;
    }

    /**
     *
     * @param banco
     * @return
     */
    public ArrayList<NotaFinanceiro> listarPagamentosAbertos(Conexao banco) {
        return banco.listarAbertasFinanceiro();
    }

    /**
     *
     * @param banco
     * @param idFornecedor
     * @param baixada
     * @return
     */
    public ArrayList<NotaFinanceiro> listarPagamentosFornecedor(Conexao banco, int idFornecedor, boolean baixada) {
        ArrayList<NotaFinanceiro> notas = banco.listarPagamentosFornecedor(idFornecedor, baixada);

        return notas;

    }

    /**
     *
     * @param banco
     * @param id
     * @return
     */
    public int postarDiario(Conexao banco, int id) {
        Diario diario = banco.buscarDiario(id);
        banco.baixarNotasFinanceiro(id, new Date(diario.getPagamento().getTime()));
        banco.aprovarDiario(id);
        return 1;
    }

    /**
     *
     * @param banco
     * @param parametro
     * @return
     */
    public ArrayList<Fluxo> buscarFluxoDetalhado(Conexao banco, Parametros parametro) {
        ArrayList<Fluxo> fluxo = new ArrayList<>();
        diarioParaFluxo(banco.listarDiariosPorPagamento(parametro), fluxo);
        return fluxo;
    }

    private void diarioParaFluxo(ArrayList<Diario> diarios, ArrayList<Fluxo> fluxo) {
        long total = 0;
        for (Diario diario : diarios) {
            Fluxo temp = new Fluxo();

            total += diario.getTotal();
            temp.setInclusao(diario.getPagamento());
            temp.setValor(diario.getTotal());
            temp.setNota(diario.getId());
            temp.setSaldo(total);

            fluxo.add(temp);
        }

        Collections.sort(fluxo, Collections.reverseOrder((Fluxo o1, Fluxo o2) -> Long.compare(o1.getInclusao().getTime(), o2.getInclusao().getTime())));
    }

    /**
     *
     * @param banco
     * @param idDiario
     * @return
     */
    public ArrayList<Nota> listarNotasDiario(Conexao banco, int idDiario) {
        ArrayList<Nota> notas = new ArrayList<>();

        buscarNotasDiario(banco, idDiario).forEach(s -> {
            Nota nota = banco.buscarNotaId(s.getIdNota());
            if (nota.getValorLiquido() != s.getValorLiquido()) {
                ratearValorNota(nota, s.getValorLiquido());
            }
            notas.add(nota);
        });

        return notas;
    }

    private void ratearValorNota(Nota nota, long valor) {
        nota.getItens().forEach(s -> {
            s.setPrecoTotal((long) (((double) s.getPrecoTotal() / (double) nota.getGastoTotal()) * (double) valor));
        });
    }

    /**
     *
     * @param banco
     * @param id
     * @return
     */
    public Diario buscarDiario(Conexao banco, int id) {
        return banco.buscarDiario(id);
    }

    /**
     *
     * @param banco
     * @param id
     * @return
     */
    public ArrayList<NotaFinanceiro> buscarNotasDiario(Conexao banco, int id) {
        return banco.buscarNotasFinanceiro(id);
    }

    /**
     *
     * @param banco
     * @param id
     * @param liberado
     * @return
     */
    public int incluirPagamento(Conexao banco,int usuario, int id, boolean liberado) {
        return banco.incluirPagamento(usuario,id, liberado);
    }

    /**
     *
     * @param banco
     * @param pagamento
     */
    public void removerPagamentoNota(Conexao banco, Pagamento pagamento) {
        banco.removerPagamentoNota(pagamento);
    }

    /**
     *
     * @param banco
     * @return
     */
    public ArrayList<NotaFinanceiro> listarBaixadasFinanceiro(Conexao banco) {
        return banco.listarBaixadasFinanceiro();
    }

    /**
     *
     * @param banco
     * @param status
     * @return
     */
    public ArrayList<Diario> listarDiarios(Conexao banco, String status) {
        return banco.listarDiarios(status);
    }

    /**
     *
     * @param banco
     * @param idNotaFinanceiro
     * @param idDiario
     */
    public void incluirNotaEmDiario(Conexao banco, int idNotaFinanceiro, int idDiario) {
        banco.incluirNotaEmDiario(idNotaFinanceiro, idDiario);
    }

    /**
     *
     * @param banco
     * @param idNotaFinanceiro
     * @param idDiario
     */
    public void excluirNotaEmDiario(Conexao banco, int idNotaFinanceiro, int idDiario) {
        banco.excluirNotaEmDiario(idNotaFinanceiro, idDiario);
    }

    /**
     *
     * @param banco
     * @param id
     * @param valor
     */
    public void atualizarDescontoNotaFinanceiro(Conexao banco, int id, long valor) {
        banco.atualizarDescontoNotaFinanceiro(id, valor);
    }

    /**
     *
     * @param banco
     * @param id
     * @param juros
     */
    public void atualizarJurosNotaFinanceiro(Conexao banco, int id, long juros) {
        banco.atualizarJurosNotaFinanceiro(id, juros);
    }

    /**
     *
     * @param banco
     * @param idNota
     * @param tipo
     */
    public void atualizarTipoNotaFinanceiro(Conexao banco, int idNota, String tipo) {
        banco.atualizarTipoNotaFinanceiro(idNota, tipo);
    }

    /**
     *
     * @param banco
     * @param idNota
     * @param linha
     */
    public void atualizarLinhaNotaFinanceiro(Conexao banco, int idNota, String linha) {
        banco.atualizarLinhaNotaFinanceiro(idNota, linha);
    }

    /**
     *
     * @param banco
     * @param idNota
     * @param codigo
     */
    public void atualizarCodigoNotaFinanceiro(Conexao banco, int idNota, String codigo) {
        banco.atualizarCodigoNotaFinanceiro(idNota, codigo);
    }
    
    /**
     *
     * @param banco
     * @param id
     * @param valor
     */
    public void atualizarValorLiquidoNotaFinanceiro(Conexao banco,int id, long valor) {
        banco.atualizarValorLiquidoNotaFinanceiro(id, valor);
    }
    
    /**
     *
     * @param banco
     * @param diario
     */
    public void salvarDiario(Conexao banco,Diario diario) {
        banco.salvarDiario(diario);
    }
    
    /**
     *
     * @param banco
     * @param id
     * @return
     */
    public ArrayList<NotaFinanceiro> listarNotasDividir(Conexao banco,int id) {
        return banco.listarNotasDividir(id);
    }
    
    /**
     *
     * @param banco
     * @param id
     * @return
     */
    public long buscarValorLiquidoNota(Conexao banco,int id) {
        return banco.buscarValorLiquidoNota(id);
    }
    
    /**
     *
     * @param banco
     * @param nota
     * @param valor
     * @return
     */
    public int atualizarValorFinanceiro(Conexao banco,int nota, long valor) {
        return banco.atualizarValorFinanceiro(nota, valor);
    }

    @Override
    public int setObject(Conexao conexao, String campo, int id, Object arg, int usuario) {
        return 0;
    }
}
