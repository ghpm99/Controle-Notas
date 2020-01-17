/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Conexao.Cliente;

/**
 *
 * @author Eduardo
 */
public enum Modulos{

    /**
     *
     */
    CONTROLE("Controle", "Controle.fxml", 0),
    /**
     *
     */
    COMPRA("Compra", "Contrato/Compra.fxml", 0),
    /**
     *
     */
    ALMOXARIFE("Almoxarife", "Almoxarife/Almoxarife.fxml", 0),
    /**
     *
     */
    ADMINISTRACAO("Administracao", "Administracao/Administracao.fxml", 0),
    /**
     *
     */
    ENGENHARIA("Engenharia", "Engenharia/Engenharia.fxml", 0),
    /**
     *
     */
    GERENCIA("Gerencia", "Gerencia/Gerencia.fxml", 9),
    /**
     *
     */
    ORDEMCOMPRA("Ordem de compra", "Contrato/OrdemCompra.fxml", 3),
    /**
     *
     */
    PRODUTO("Produto", "Produto/Produto.fxml", 3),
    /**
     *
     */
    NOTA("Nota", "Nota/Nota.fxml", 3),
    
    NOTASEMCONTRATO("Nota sem contrato", "Nota/SemContrato.fxml", 6),
    /**
     *
     */
    PRENOTA("Pre-Nota", "Nota/PreNota.fxml", 3),
    /**
     *
     */
    LOCACAO("Locacao", "Almoxarife/Locacao.fxml", 3),
    /**
     *
     */
    RELATORIO("Relatorio", "Relatorio/Relatorio.fxml", 6),
    /**
     *
     */
    SAIDA("Saida", "Util/Saida.fxml", 3),
    /**
     *
     */
    CONTA("Conta", "Conta/Conta.fxml", 10),
    
    CONTAALTERAR("Conta Alterar", "ContaAlterar.fxml", 10),
    /**
     *
     */
    CAMINHOS("Caminhos", "Gerencia/Caminhos.fxml", 9),
    /**
     *
     */
    PLANOCONTA("Plano de Conta", "Engenharia/PlanoConta.fxml", 6),
    /**
     *
     */
    FORNECEDOR("Fornecedor", "Fornecedor/Fornecedor.fxml", 3),
    /**
     *
     */
    PRODUTOCADASTRO("ProdutoCadastro", "Produto/ProdutoAlterar.fxml", 3),
    /**
     *
     */
    PRODUTOBUSCAR("ProdutoBuscar", "Produto/Produto.fxml", 3),
    /**
     *
     */
    ORDEMCADASTRO("OrdemCadastro", "Contrato/OrdemCompraCadastro.fxml", 3),
    /**
     *
     */
    SERVIDOR("Servidor", "Util/Servidor.fxml", 10),
    /**
     *
     */
    FORNECEDORALTERAR("FornecedorAlterar", "Fornecedor/FornecedorAlterar.fxml", 3),
    
    ESCOLHERFORNECEDOR("EscolherFornecedor","EscolherFornecedor.fxml",0),
    /**
     *
     */
    NOTAALTERAR("NotaAlterar", "Nota/NotaAlterar.fxml", 3),
    /**
     *
     */
    RELATORIONOTA("Relatorio Nota", "Relatorio/RelatorioNota.fxml", 5),
    /**
     *
     */
    RELATORIOORCAMENTO("Relatorio Orcamento", "Relatorio/RelatorioOrcamento.fxml", 6),
    /**
     *
     */
    RELATORIOPARAMETROS("Relatorio Parametros", "RelatorioParametros.fxml", 6),
    /**
     *
     */
    RELATORIOORCAMENTOPARAMETROS("Relatorio Parametros Orcamento", "Relatorio/RelatorioParametrosOrcamento.fxml", 3),
    /**
     *
     */
    MAPACOTACAO("Mapa de cotacao", "Contrato/MapaCotacao.fxml", 4),
    /**
     *
     */
    MAPACOTACAOALTERAR("Alterar/novo mapa de cotacao", "Contrato/MapaCotacaoAlterar.fxml", 4),
    /**
     *
     */
    ITEMORCAMENTO("Item Orcamento", "Engenharia/TabelaItem.fxml", 6),
    /**
     *
     */
    CONTRATOS("Contratos", "Contrato/Contratos.fxml", 3),
    
    SEMFORNECEDOR("Contratos sem fornecedor", "Contrato/SemFornecedor.fxml", 3),
    /**
     *
     */
    CONTRATO("Contrato", "Contrato/Contrato.fxml", 3),
    /**
     *
     */
    INCC("INCC", "Engenharia/INCC.fxml", 3),
    /**
     *
     */
    SEMACESSO("Sem acesso", "Util/SemAcesso.fxml", 0),
    /**
     *
     */
    RENOMEAR("Renomear", "Util/Renomear.fxml", 5),
    
    SOBRE("Sobre","Util/Sobre.fxml",0),
    
    FINANCEIRO("Financeiro", "Financeiro/Financeiro.fxml", 7),
    
    ABERTASFINANCEIRO("Notas em aberto", "Financeiro/AbertasFinanceiro.fxml", 7),
    
    BAIXADASFINANCEIRO("Notas baixadas", "Financeiro/BaixadasFinanceiro.fxml", 7),
    
    FLUXOFINANCEIRO("Fluxo", "Financeiro/Fluxo.fxml", 7),
    
    RESUMOFINANCEIRO("Detalhado", "Financeiro/ResumoFinanceiro.fxml", 7),
    
    HISTORICO("Historico","Historico.fxml",6),
    
    DIARIO("Diario","Financeiro/Diario.fxml",6),
    
    DIARIOALTERAR("Diario Alterar","Financeiro/DiarioAlterar.fxml",6),
    
    DIARIOBUSCARNOTAS("Diario Buscar Notas","DiarioBuscarNotas.fxml",6),
    
    INCLUIRPAGAMENTO("Incluir Pagamento","IncluirPagamento.fxml",6),
    
    DIVIDIRPAGAMENTO("Dividir Pagamento Nota","DividirPagamento.fxml",8),
    
    MOSTRARCONTRATOS("Mostrar Contrato","Contrato/MostrarContratos.fxml",4),
    
    MOSTRARFINANCEIRO("Mostrar Pagamentos", "Financeiro/MostrarPagamentos.fxml",4),
    
    MOSTRARNOTAS("Mostrar Notas","Nota/MostrarNotas.fxml",4),
    
    NOTASUBMETIDAS("Nota submetidas", "Nota/NotasSubmetidas.fxml", 10),
    
    SUGESTAO("Sugestao","Sugestao.fxml",0),
    
    ERRO("Erro", "Erro.fxml", 0);

    private String nome, caminho;
    private int acesso;

    Modulos(String nome, String caminho, int acesso){
        this.nome = nome;
        this.caminho = caminho;
        this.acesso = acesso;
    }

    /**
     *
     * @param nome
     * @return
     */
    public static Modulos getModulo(String nome){
        int nivel = Cliente.INSTANCIA.getNivelConta().getNivel();
        Modulos[] modulos = Modulos.values();
        for(Modulos a : modulos){
            if(a.getNome().equals(nome)){
                if(a.getAcesso() <= nivel){
                    return a;
                } else{
                    return SEMACESSO;
                }
            }
        }
        return null;
    }

    /**
     *
     * @return
     */
    public String getNome(){
        return nome;
    }

    /**
     *
     * @return
     */
    public String getCaminho(){
        return caminho;
    }

    /**
     *
     * @return
     */
    public int getAcesso(){
        return acesso;
    }

}
