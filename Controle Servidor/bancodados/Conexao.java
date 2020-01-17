/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDados;

import Classes.Base.Produto;
import Classes.Contrato.Contrato;
import Classes.Contrato.Item.ItemContrato;
import Classes.Contrato.Mapa.MapaContrato;
import Classes.Contrato.Produto.ProdutoContrato;
import Classes.Financeiro.Diario;
import Classes.Financeiro.NotaFinanceiro;
import Classes.Fornecedor.Contato.Contato;
import Classes.Fornecedor.Endereco.Endereco;
import Classes.Fornecedor.Fornecedor;
import Classes.Nota.Nota;
import Classes.Nota.anexo.AnexoNota;
import Classes.Nota.cobranca.Pagamento;
import Classes.Nota.item.ItemNota;
import Configuracao.Referencia;
import GUI.principal.Principal;
import Rotina.Email.Status;
import Seguranca.Login.Conta;
import Util.Calcular;
import Util.CampoBuscar;
import Util.Dado;
import Util.Historico;
import Util.HistoricoPesquisa;
import Util.ItemOrcamento;
import Util.Parametros;
import classes.almoxarife.ProdutoAlmoxarife;
import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 *
 * @author Guilherme
 */
public class Conexao {

    //INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)
    /*
     PreparedStatement prepareStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
     prepareStatement.setDate(1, new Date(pagamento.getTime()));
     System.out.println("Execute: " + prepareStatement.executeUpdate());
     ResultSet result = prepareStatement.getGeneratedKeys();
     if (result.next()) {
     return result.getInt(1);
     }
     */
    Connection connection = null;
    private String nome;
    public boolean executando = false,
            /**
             *
             */
            pmWeb;

    /**
     *
     * @param nome
     * @param caminho
     * @param usuario
     * @param senha
     * @param pmWeb
     */
    public Conexao(String nome, String caminho, String usuario, String senha, boolean pmWeb) {
        this.nome = nome;
        this.pmWeb = pmWeb;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());

            String dburl = "jdbc:derby:" + caminho + ";";

            connection = DriverManager.getConnection(dburl, usuario, senha);

            Principal.setStatusBanco("Conectou no banco de dados: " + nome);
            executando = true;
        } catch (ClassNotFoundException | SQLException ex) {
            Principal.setStatus(ex.getMessage());
            executando = false;
        }

    }

    /**
     *
     * @return
     */
    public String getNomeBanco() {
        return nome;
    }

    /**
     *
     */
    public void fechar() {
        try {
            if (connection != null) {
                connection.close();
                Principal.setStatusBanco("Fechou banco " + nome);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public Conta buscarConta(int id) {
        try {
            String sql = "SELECT NOME,NIVEL,CATEGORIA,BLOCK,COMPRA,ALMOXARIFE,ADMINISTRACAO,ENGENHARIA,FINANCEIRO,GERENCIA,"
                    + "ULTIMOLOGIN,EMAIL,LOGADO,EXPIRA FROM ROOT.CONTA WHERE ID=?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();

            resultSet.next();

            return new Conta(id, resultSet.getInt("NIVEL"), resultSet.getString("NOME"), "", resultSet.getString("CATEGORIA"), "",
                    resultSet.getString("EMAIL"), resultSet.getBoolean("BLOCK"), resultSet.getBoolean("COMPRA"), resultSet.getBoolean("ALMOXARIFE"),
                    resultSet.getBoolean("ADMINISTRACAO"), resultSet.getBoolean("ENGENHARIA"), resultSet.getBoolean("FINANCEIRO"),
                    resultSet.getBoolean("GERENCIA"), resultSet.getBoolean("LOGADO"), resultSet.getLong("ULTIMOLOGIN"), resultSet.getLong("EXPIRA"));

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Conta();
    }

    /**
     *
     * @param id
     */
    public void logou(int id) {
        salvarEvento(id, id, "Logou", "CONTA", "");
        try {
            String sql = "UPDATE ROOT.CONTA SET LOGADO = ?,ULTIMOLOGIN = ? WHERE ID = ?";

            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            prepareStatement.setBoolean(1, true);
            prepareStatement.setLong(2, new java.util.Date().getTime());
            prepareStatement.setInt(3, id);

            prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param id
     */
    public void deslogou(int id) {
        salvarEvento(id, id, "Deslogou", "CONTA", "");
        try {
            String sql = "UPDATE ROOT.CONTA SET LOGADO = ? WHERE ID = ?";

            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            prepareStatement.setBoolean(1, false);
            prepareStatement.setInt(2, id);

            prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param idConta
     * @param tabela
     * @return
     */
    public HistoricoPesquisa buscarCampos(int idConta, String tabela) {
        HistoricoPesquisa itens = new HistoricoPesquisa();
        for (CampoBuscar a : CampoBuscar.values()) {
            if (a.getTabela().equals(tabela)) {
                itens.getItens().add(a.getCampo());
            }
        }
        buscarHistoricoPesquisa(itens, idConta, tabela);
        return itens;
    }

    /**
     *
     * @param idConta
     * @param campo
     * @param valor
     * @return
     */
    public ArrayList<Nota> buscarNotaCampo(int idConta, String campo, String valor) {
        ArrayList<Nota> notas = new ArrayList<>();
        try {
            String sql = "SELECT NOTA.ID,NOTA.NUMERO,NOTA.SERIE,FORNECEDOR.RAZAOSOCIAL,FORNECEDOR.CNPJ,NOTA.DESCRICAO,NOTA.GASTOTOTAL,NOTA.VENCIMENTO,"
                    + "NOTA.VENCIMENTOREAL,NOTA.TIPO,NOTA.PRENOTA,NOTA.CLASSIFICADA,NOTA.LANCADA,NOTA.OBSERVACAO,NOTA.IDFORNECEDOR,NOTA.IDPMWEB "
                    + "FROM NOTA "
                    + "LEFT JOIN FORNECEDOR ON NOTA.IDFORNECEDOR = FORNECEDOR.ID "
                    + "WHERE NOTA.NUMERO LIKE ? "
                    + "AND NOTA.IDFORNECEDOR IN (SELECT FORNECEDOR.ID FROM FORNECEDOR WHERE FORNECEDOR.RAZAOSOCIAL LIKE ?) "
                    + "AND NOTA.IDFORNECEDOR IN (SELECT FORNECEDOR.ID FROM FORNECEDOR WHERE FORNECEDOR.CNPJ LIKE ?) "
                    + "AND NOTA.CANCELADO = FALSE";

            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            prepareStatement.setString(1, CampoBuscar.NOTANUMERO.getCampo().equals(campo) ? valor : "%");
            prepareStatement.setString(2, CampoBuscar.NOTAFORNECEDORNOME.getCampo().equals(campo) ? valor : "%");
            prepareStatement.setString(3, CampoBuscar.NOTAFORNECEDORCNPJ.getCampo().equals(campo) ? valor : "%");

            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {

                do {
                    Nota nota = new Nota();

                    nota.setId(resultSet.getInt("ID"));
                    nota.setNumero(resultSet.getString("NUMERO"));
                    nota.setSerie(resultSet.getString("SERIE"));
                    nota.setTipo(resultSet.getString("TIPO"));
                    nota.setFornecedor(resultSet.getString("RAZAOSOCIAL") + " " + resultSet.getString("CNPJ"));
                    nota.setDescricao(resultSet.getString("DESCRICAO"));
                    nota.setGastoTotal(resultSet.getLong("GASTOTOTAL"));
                    nota.setObservacao(resultSet.getString("OBSERVACAO"));
                    nota.setVencimento(resultSet.getDate("VENCIMENTO"));
                    nota.setVencimentoReal(resultSet.getDate("VENCIMENTOREAL"));
                    nota.setPreNota(resultSet.getBoolean("PRENOTA"));
                    nota.setClassificada(resultSet.getBoolean("CLASSIFICADA"));
                    nota.setLancada(resultSet.getBoolean("LANCADA"));
                    nota.setIdPmWeb(resultSet.getInt("IDPMWEB"));
                    nota.setStatus(buscarPmWeb(nota.getIdPmWeb()).getStatus());

                    notas.add(nota);
                } while (resultSet.next());

            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        atualizarHistoricoPesquisa(idConta, "NOTA", campo, valor);
        return notas;
    }

    /**
     *
     * @param id
     * @return
     */
    public Nota buscarNotaId(int id) {
        Nota nota = new Nota();
        try {

            String sql = "SELECT NOTA.ID,NOTA.INCLUSAO,NOTA.ATUALIZACAO,NOTA.AUTOR,NOTA.CANCELADO,NOTA.ATIVO,NOTA.NUMERO,NOTA.SERIE,"
                    + "NOTA.TIPO,FORNECEDOR.RAZAOSOCIAL,FORNECEDOR.CNPJ,NOTA.VALORTOTAL,NOTA.GASTOTOTAL,NOTA.SALDOTOTAL,NOTA.EMISSAO,NOTA.VENCIMENTO,"
                    + "NOTA.VENCIMENTOREAL,NOTA.PRENOTA,NOTA.CLASSIFICADA,NOTA.LANCADA,NOTA.MAPAS,NOTA.DESCRICAO,"
                    + "NOTA.OBSERVACAO,NOTA.CONTRATO,NOTA.IDFORNECEDOR,NOTA.IDFATURAMENTODIRETO,NOTA.IDCONTRATO,NOTA.IDPMWEB,"
                    + "(SELECT FORNECEDOR.RAZAOSOCIAL FROM CONTRATO LEFT JOIN FORNECEDOR ON CONTRATO.IDFORNECEDOR = FORNECEDOR.ID WHERE CONTRATO.ID = NOTA.IDCONTRATO) "
                    + "FROM NOTA LEFT JOIN FORNECEDOR ON NOTA.IDFORNECEDOR = FORNECEDOR.ID WHERE NOTA.ID = ?";

            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                nota.setId(resultSet.getInt("ID"));
                nota.setInclusao(resultSet.getDate("INCLUSAO"));
                nota.setAtualizacao(resultSet.getDate("ATUALIZACAO"));
                nota.setAutor(resultSet.getString("AUTOR"));
                nota.setCancelado(resultSet.getBoolean("CANCELADO"));
                nota.setDescricao(resultSet.getString("DESCRICAO"));
                nota.setObservacao(resultSet.getString("OBSERVACAO"));
                nota.setContrato(resultSet.getString("CONTRATO"));
                nota.setAtivo(resultSet.getBoolean("ATIVO"));
                nota.setNumero(resultSet.getString("NUMERO"));
                nota.setSerie(resultSet.getString("SERIE"));
                nota.setTipo(resultSet.getString("TIPO"));
                nota.setFornecedor(resultSet.getString("RAZAOSOCIAL") + " " + resultSet.getString("CNPJ"));
                nota.setValorTotal(resultSet.getLong("VALORTOTAL"));
                nota.setGastoTotal(resultSet.getLong("GASTOTOTAL"));
                nota.setSaldoTotal(resultSet.getLong("SALDOTOTAL"));
                nota.setEmissao(resultSet.getDate("EMISSAO"));
                nota.setVencimento(resultSet.getDate("VENCIMENTO"));
                nota.setVencimentoReal(resultSet.getDate("VENCIMENTOREAL"));
                nota.setPreNota(resultSet.getBoolean("PRENOTA"));
                nota.setClassificada(resultSet.getBoolean("CLASSIFICADA"));
                nota.setLancada(resultSet.getBoolean("LANCADA"));
                nota.setFaturamentoDireto(resultSet.getString(29));
                nota.setIdFornecedor(resultSet.getInt("IDFORNECEDOR"));
                nota.setIdFaturamentoDireto(resultSet.getInt("IDFATURAMENTODIRETO"));
                nota.setIdContrato(resultSet.getInt("IDCONTRATO"));
                nota.setIdPmWeb(resultSet.getInt("IDPMWEB"));
                nota.setStatus(buscarPmWeb(nota.getIdPmWeb()).getStatus());

                nota.setItens(buscarItensNota(nota.getId()));
                nota.setPagamentos(buscarPagamentosNota(nota.getId()));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }        
        Calcular.calcularNota(nota);
        return nota;
    }

    private ArrayList<ItemNota> buscarItensNota(int id) {
        ArrayList<ItemNota> itens = new ArrayList<>();
        try {

            String sql = "SELECT a.ID,a.ITEM,b.PLANOCONTA,b.DESCRICAO,a.VALOR,a.SALDO,"
                    + "a.UNIDADE,a.QNTTOTAL,a.PRECOUNITARIO,a.PRECOTOTAL,a.NUMEROMAPA,a.DESCRICAOMAPA "
                    + "FROM ITEMNOTA a LEFT JOIN ITEMORCAMENTO b ON a.ITEM = b.ITEM "
                    + "WHERE a.IDNOTA = ?";

            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {

                do {
                    ItemNota item = new ItemNota();

                    item.setId(resultSet.getInt("ID"));
                    item.setItem(resultSet.getString("ITEM"));
                    item.setPlano(resultSet.getString("PLANOCONTA"));
                    item.setValor(resultSet.getLong("VALOR"));
                    item.setSaldo(resultSet.getLong("SALDO"));
                    item.setDescricao(resultSet.getString("DESCRICAO"));
                    item.setUnidade(resultSet.getString("UNIDADE"));
                    item.setQntTotal(resultSet.getLong("QNTTOTAL"));
                    item.setPrecoUnitario(resultSet.getLong("PRECOUNITARIO"));
                    item.setPrecoTotal(resultSet.getLong("PRECOTOTAL"));
                    item.setNumeroMapa(resultSet.getString("NUMEROMAPA"));
                    item.setDescricaoMapa(resultSet.getString("DESCRICAOMAPA"));

                    itens.add(item);
                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itens;
    }

    private ArrayList<Pagamento> buscarPagamentosNota(int id) {
        ArrayList<Pagamento> pagamentos = new ArrayList<>();

        try {
            String sql = "SELECT ID, VENCIMENTO, VENCIMENTOREAL, VALOR, TIPO, LINHA, CODIGO FROM PAGAMENTONOTA WHERE IDNOTA = ?";
            PreparedStatement prepara = connection.prepareStatement(sql);
            prepara.setInt(1, id);
            ResultSet result = prepara.executeQuery();
            if (result.next()) {
                do {
                    Pagamento pagamento = new Pagamento();
                    pagamento.setId(result.getInt("ID"));
                    pagamento.setVencimento(result.getDate("VENCIMENTO"));
                    pagamento.setVencimentoReal(result.getDate("VENCIMENTOREAL"));
                    pagamento.setValor(result.getLong("VALOR"));
                    pagamento.setTipo(result.getString("TIPO"));
                    pagamento.setLinha(result.getString("LINHA"));
                    pagamento.setCodigo(result.getString("CODIGO"));

                    pagamentos.add(pagamento);
                } while (result.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pagamentos;
    }

    /**
     *
     * @param tabela
     * @param campo
     * @param id
     * @return
     */
    public Object buscarObject(String tabela, String campo, int id) {
        try {

            ResultSet resultSet = buscarID(tabela, id);
            return resultSet.getObject(campo);
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     *
     * @param banco
     * @param id
     * @return
     */
    public ResultSet buscarID(String banco, int id) {
        try {
            String sql = "SELECT * FROM " + banco + " WHERE ID = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();
            resultSet.next();
            return resultSet;
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param tabela
     * @param campo
     * @param id
     * @param arg
     * @param usuario
     * @return
     */
    public int setObject(String tabela, String campo, int id, Object arg, int usuario) {
        try {
            String sql = "UPDATE ROOT." + tabela + " SET " + campo + "=? WHERE ID =?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setObject(1, arg);
            prepareStatement.setInt(2, id);

            salvarEvento(id, usuario, "Atualizou", tabela, campo);

            return prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    /**
     *
     * @param limite
     * @return
     */
    public ArrayList<Nota> buscarNotaUltimos(int limite) {
        ArrayList<Nota> ids = new ArrayList<>();
        try {

            String sql = "SELECT NOTA.ID,NOTA.NUMERO,NOTA.SERIE,FORNECEDOR.RAZAOSOCIAL,FORNECEDOR.CNPJ,NOTA.DESCRICAO,NOTA.GASTOTOTAL,NOTA.VENCIMENTO,"
                    + "NOTA.VENCIMENTOREAL,NOTA.TIPO,NOTA.PRENOTA,NOTA.CLASSIFICADA,NOTA.LANCADA,NOTA.OBSERVACAO,NOTA.IDFORNECEDOR,NOTA.IDPMWEB"
                    + " FROM NOTA LEFT JOIN FORNECEDOR ON NOTA.IDFORNECEDOR = FORNECEDOR.ID WHERE NOTA.CANCELADO = false ORDER BY ID DESC";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setMaxRows(limite);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    Nota nota = new Nota();
                    nota.setId(resultSet.getInt("ID"));
                    nota.setNumero(resultSet.getString("NUMERO"));
                    nota.setSerie(resultSet.getString("SERIE"));
                    nota.setTipo(resultSet.getString("TIPO"));
                    nota.setFornecedor(resultSet.getString("RAZAOSOCIAL") + " " + resultSet.getString("CNPJ"));
                    nota.setDescricao(resultSet.getString("DESCRICAO"));
                    nota.setGastoTotal(resultSet.getLong("GASTOTOTAL"));
                    nota.setObservacao(resultSet.getString("OBSERVACAO"));
                    nota.setVencimento(resultSet.getDate("VENCIMENTO"));
                    nota.setVencimentoReal(resultSet.getDate("VENCIMENTOREAL"));
                    nota.setPreNota(resultSet.getBoolean("PRENOTA"));
                    nota.setClassificada(resultSet.getBoolean("CLASSIFICADA"));
                    nota.setLancada(resultSet.getBoolean("LANCADA"));
                    nota.setIdFornecedor(resultSet.getInt("IDFORNECEDOR"));
                    nota.setIdPmWeb(resultSet.getInt("IDPMWEB"));
                    nota.setStatus(buscarPmWeb(nota.getIdPmWeb()).getStatus());
                    ids.add(nota);
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ids;
    }

    /**
     *
     * @param login
     * @return
     */
    public Conta buscarConta(String login) {
        Conta conta = new Conta();
        try {
            String sql = "SELECT ID,SENHA,BLOCK FROM ROOT.CONTA WHERE LOGIN=?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setString(1, login);
            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {
                conta.setId(resultSet.getInt("ID"));
                conta.setSenha(resultSet.getString("SENHA"));
                conta.setBlock(resultSet.getBoolean("BLOCK"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conta;
    }

    /**
     *
     * @param id
     * @return
     */
    public Contrato buscarContrato(int id) {
        Contrato contrato = new Contrato();
        try {
            String sql = "SELECT CONTRATO.ID,CONTRATO.INCLUSAO,CONTRATO.ATUALIZACAO,CONTRATO.AUTOR,CONTRATO.CANCELADO,CONTRATO.ATIVO,CONTRATO.NUMERO,FORNECEDOR.RAZAOSOCIAL,FORNECEDOR.CNPJ,CONTRATO.DESCRICAO,"
                    + "CONTRATO.VALORTOTAL,CONTRATO.GASTOTOTAL,CONTRATO.SALDOTOTAL,CONTRATO.OBSERVACAO,CONTRATO.EMISSAO,CONTRATO.CONTRATO,CONTRATO.IDFORNECEDOR,CONTRATO.RESPONSAVEL "
                    + "FROM CONTRATO LEFT JOIN FORNECEDOR ON CONTRATO.IDFORNECEDOR = FORNECEDOR.ID WHERE CONTRATO.ID = ?";

            //SELECT CONTRATO.ID,CONTRATO.NUMERO,FORNECEDOR.RAZAOSOCIAL,CONTRATO.DESCRICAO,CONTRATO.IDFORNECEDOR,FORNECEDOR.CNPJ FROM CONTRATO LEFT JOIN FORNECEDOR ON CONTRATO.IDFORNECEDOR = FORNECEDOR.ID WHERE CONTRATO.ID = 6 ;
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {

                contrato.setId(resultSet.getInt("ID"));
                contrato.setInclusao(resultSet.getDate("INCLUSAO"));
                contrato.setAtualizacao(resultSet.getDate("ATUALIZACAO"));
                contrato.setAutor(resultSet.getString("AUTOR"));
                contrato.setCancelado(resultSet.getBoolean("CANCELADO"));
                contrato.setAtivo(resultSet.getBoolean("ATIVO"));
                contrato.setNumero(resultSet.getString("NUMERO"));
                contrato.setFornecedor(resultSet.getString("RAZAOSOCIAL") + " " + resultSet.getString("CNPJ"));
                contrato.setDescricao(resultSet.getString("DESCRICAO"));
                contrato.setValorTotal(resultSet.getLong("VALORTOTAL"));
                contrato.setGastoTotal(resultSet.getLong("GASTOTOTAL"));
                contrato.setSaldoTotal(resultSet.getLong("SALDOTOTAL"));
                contrato.setObservacao(resultSet.getString("OBSERVACAO"));
                contrato.setEmissao(resultSet.getDate("EMISSAO"));
                contrato.setContrato(resultSet.getString("CONTRATO"));
                contrato.setIdFornecedor(resultSet.getInt("IDFORNECEDOR"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calcular.calcularContrato(contrato);
        return contrato;
    }

    /**
     *
     * @param id
     * @return
     */
    public ArrayList<MapaContrato> buscarMapasContrato(int id) {
        ArrayList<MapaContrato> mapas = new ArrayList<>();
        try {
            String sql = "SELECT * FROM MAPACONTRATO WHERE IDCONTRATO = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {
                do {
                    MapaContrato mapa = new MapaContrato();
                    mapa.setId(resultSet.getInt("ID"));
                    mapa.setInclusao(resultSet.getDate("INCLUSAO"));
                    mapa.setAtualizacao(resultSet.getDate("ATUALIZACAO"));
                    mapa.setAutor(resultSet.getString("AUTOR"));
                    mapa.setCancelado(resultSet.getBoolean("CANCELADO"));
                    mapa.setAtivo(resultSet.getBoolean("ATIVO"));
                    mapa.setNumero(resultSet.getString("NUMERO"));
                    mapa.setDescricao(resultSet.getString("DESCRICAO"));
                    mapa.setEmpresa(resultSet.getString("EMPRESA"));
                    mapa.setValor(resultSet.getLong("VALOR"));
                    mapa.setTotal(resultSet.getLong("TOTAL"));
                    mapa.setSaldo(resultSet.getLong("SALDO"));
                    mapa.setContrato(resultSet.getString("CONTRATO"));
                    mapa.setAssinatura(resultSet.getDate("ASSINATURA"));
                    mapa.setCriacao(resultSet.getDate("CRIACAO"));
                    mapa.setObservacaoAssinatura(resultSet.getString("OBSERVACAOASSINATURA"));
                    mapa.setObservacaoSistema(resultSet.getString("OBSERVACAOSISTEMA"));
                    mapa.setFisico(resultSet.getString("FISICO"));
                    mapa.setEncerramento(resultSet.getString("ENCERRAMENTO"));
                    mapa.setObservacao(resultSet.getString("OBSERVACAO"));

                    mapa.setItens(buscarItemContrato(mapa.getId()));

                    mapas.add(mapa);
                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapas;
    }

    /**
     *
     * @param id
     * @return
     */
    public MapaContrato buscarMapaContrato(int id) {
        MapaContrato mapa = new MapaContrato();
        try {

            ResultSet resultSet = buscarID("MAPACONTRATO", id);
            mapa.setId(resultSet.getInt("ID"));
            mapa.setInclusao(resultSet.getDate("INCLUSAO"));
            mapa.setAtualizacao(resultSet.getDate("ATUALIZACAO"));
            mapa.setAutor(resultSet.getString("AUTOR"));
            mapa.setCancelado(resultSet.getBoolean("CANCELADO"));
            mapa.setAtivo(resultSet.getBoolean("ATIVO"));
            mapa.setNumero(resultSet.getString("NUMERO"));
            mapa.setDescricao(resultSet.getString("DESCRICAO"));
            mapa.setEmpresa(resultSet.getString("EMPRESA"));
            mapa.setValor(resultSet.getLong("VALOR"));
            mapa.setTotal(resultSet.getLong("TOTAL"));
            mapa.setSaldo(resultSet.getLong("SALDO"));
            mapa.setContrato(resultSet.getString("CONTRATO"));
            mapa.setAssinatura(resultSet.getDate("ASSINATURA"));
            mapa.setCriacao(resultSet.getDate("CRIACAO"));
            mapa.setObservacaoAssinatura(resultSet.getString("OBSERVACAOASSINATURA"));
            mapa.setObservacaoSistema(resultSet.getString("OBSERVACAOSISTEMA"));
            mapa.setFisico(resultSet.getString("FISICO"));
            mapa.setEncerramento(resultSet.getString("ENCERRAMENTO"));
            mapa.setObservacao(resultSet.getString("OBSERVACAO"));
            mapa.setIdContrato(resultSet.getInt("IDCONTRATO"));

            mapa.setItens(buscarItemContrato(mapa.getId()));

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapa;
    }

    /**
     *
     * @param id
     * @return
     */
    public ArrayList<ItemContrato> buscarItemContrato(int id) {

        ArrayList<ItemContrato> itens = new ArrayList<>();

        try {
            String sql = "SELECT ITEMCONTRATO.ID,ITEMCONTRATO.INCLUSAO,ITEMCONTRATO.ATUALIZACAO,ITEMCONTRATO.AUTOR,ITEMCONTRATO.CANCELADO,ITEMCONTRATO.ATIVO,"
                    + "ITEMCONTRATO.ITEM,ITEMORCAMENTO.PLANOCONTA,ITEMORCAMENTO.DESCRICAO,ITEMCONTRATO.TIPO,ITEMCONTRATO.CUSTO,ITEMCONTRATO.VALOR,ITEMCONTRATO.SALDO,"
                    + "ITEMCONTRATO.UNIDADE,ITEMCONTRATO.QNTTOTAL,ITEMCONTRATO.PRECOUNITARIO,ITEMCONTRATO.PRECOTOTAL,ITEMCONTRATO.NUMEROMAPA,ITEMCONTRATO.DESCRICAOMAPA,ITEMCONTRATO.PRODUTOS"
                    + " FROM ITEMCONTRATO LEFT JOIN ITEMORCAMENTO ON ITEMCONTRATO.ITEM = ITEMORCAMENTO.ITEM WHERE ITEMCONTRATO.IDMAPA = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {
                do {
                    ItemContrato item = new ItemContrato();
                    item.setId(resultSet.getInt("ID"));
                    item.setInclusao(resultSet.getDate("INCLUSAO"));
                    item.setAtualizacao(resultSet.getDate("ATUALIZACAO"));
                    item.setAutor(resultSet.getString("AUTOR"));
                    item.setCancelado(resultSet.getBoolean("CANCELADO"));
                    item.setAtivo(resultSet.getBoolean("ATIVO"));
                    item.setItem(resultSet.getString("ITEM"));
                    item.setPlano(resultSet.getString("PLANOCONTA"));
                    item.setTipo(resultSet.getString("TIPO"));
                    item.setCusto(resultSet.getString("CUSTO"));
                    item.setValor(resultSet.getLong("VALOR"));
                    item.setSaldo(resultSet.getLong("SALDO"));
                    item.setDescricao(resultSet.getString("DESCRICAO"));
                    item.setUnidade(resultSet.getString("UNIDADE"));
                    item.setQntTotal(resultSet.getLong("QNTTOTAL"));
                    item.setPrecoUnitario(resultSet.getLong("PRECOUNITARIO"));
                    item.setPrecoTotal(resultSet.getLong("PRECOTOTAL"));
                    item.setNumeroMapa(resultSet.getString("NUMEROMAPA"));
                    item.setDescricaoMapa(resultSet.getString("DESCRICAOMAPA"));

                    Stream.of(resultSet.getString("PRODUTOS").split(",")).forEach(s -> {
                        if (!s.isEmpty()) {
                            item.adicionarProduto(buscarProdutoContrato(Integer.valueOf(s)));
                        }
                    });
                    itens.add(item);
                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itens;
    }

    /**
     *
     * @param id
     * @return
     */
    public ProdutoContrato buscarProdutoContrato(int id) {
        ProdutoContrato produto = new ProdutoContrato();
        try {

            ResultSet resultSet = buscarID("PRODUTOCONTRATO", id);
            produto.setId(resultSet.getInt("ID"));
            produto.setInclusao(resultSet.getDate("INCLUSAO"));
            produto.setAtualizacao(resultSet.getDate("ATUALIZACAO"));
            produto.setAutor(resultSet.getString("AUTOR"));
            produto.setCancelado(resultSet.getBoolean("CANCELADO"));
            produto.setAtivo(resultSet.getBoolean("ATIVO"));
            produto.setCodigo(resultSet.getString("CODIGO"));
            produto.setDescricao(resultSet.getString("DESCRICAO"));
            produto.setUnidade(resultSet.getString("UNIDADE"));
            produto.setQnt(resultSet.getLong("QNT"));
            produto.setPrecoUnitario(resultSet.getLong("PRECOUNITARIO"));
            produto.setPrecoTotal(resultSet.getLong("PRECOTOTAL"));

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return produto;
    }

    /**
     *
     * @param idConta
     * @param campo
     * @param valor
     * @return
     */
    public ArrayList<Contrato> buscarContratoCampo(int idConta, String campo, String valor) {
        ArrayList<Contrato> contratos = new ArrayList<>();
        try {
            String sql = "SELECT ID,NUMERO,FORNECEDOR,DESCRICAO,VALORTOTAL,GASTOTOTAL,SALDOTOTAL,OBSERVACAO,CONTRATO "
                    + "FROM CONTRATO "
                    + "WHERE NUMERO LIKE ? "
                    + "AND CONTRATO.DESCRICAO LIKE ? "
                    + "AND CONTRATO.IDFORNECEDOR IN (SELECT FORNECEDOR.ID FROM FORNECEDOR WHERE FORNECEDOR.RAZAOSOCIAL LIKE ?) "
                    + "AND CONTRATO.IDFORNECEDOR IN (SELECT FORNECEDOR.ID FROM FORNECEDOR WHERE FORNECEDOR.CNPJ LIKE ?) "
                    + "AND CONTRATO.CANCELADO = FALSE";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setString(1, CampoBuscar.CONTRATONUMERO.getCampo().equals(campo) ? valor : "%");
            prepareStatement.setString(2, CampoBuscar.CONTRATODESCRICAO.getCampo().equals(campo) ? valor : "%");
            prepareStatement.setString(3, CampoBuscar.CONTRATOFORNECEDORNOME.getCampo().equals(campo) ? valor : "%");
            prepareStatement.setString(4, CampoBuscar.CONTRATOFORNECEDORCNPJ.getCampo().equals(campo) ? valor : "%");

            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {

                do {
                    Contrato contrato = new Contrato();
                    contrato.setId(resultSet.getInt("ID"));
                    contrato.setNumero(resultSet.getString("NUMERO"));
                    contrato.setFornecedor(resultSet.getString("FORNECEDOR"));
                    contrato.setDescricao(resultSet.getString("DESCRICAO"));
                    contrato.setValorTotal(resultSet.getLong("VALORTOTAL"));
                    contrato.setGastoTotal(resultSet.getLong("GASTOTOTAL"));
                    contrato.setSaldoTotal(resultSet.getLong("SALDOTOTAL"));
                    contrato.setObservacao(resultSet.getString("OBSERVACAO"));
                    contrato.setContrato(resultSet.getString("CONTRATO"));
                    contratos.add(contrato);
                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        atualizarHistoricoPesquisa(idConta, "CONTRATO", campo, valor);
        return contratos;
    }

    /**
     *
     * @param limite
     * @return
     */
    public ArrayList<Contrato> buscarContratoUltimos(int limite) {
        ArrayList<Contrato> ids = new ArrayList<>();
        try {
            String sql = "SELECT ID,NUMERO,FORNECEDOR,DESCRICAO,VALORTOTAL,GASTOTOTAL,SALDOTOTAL,OBSERVACAO,CONTRATO "
                    + "FROM CONTRATO WHERE CANCELADO = false AND ATIVO = TRUE ORDER BY ID DESC";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setMaxRows(limite);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    Contrato contrato = new Contrato();
                    contrato.setId(resultSet.getInt("ID"));
                    contrato.setNumero(resultSet.getString("NUMERO"));
                    contrato.setFornecedor(resultSet.getString("FORNECEDOR"));
                    contrato.setDescricao(resultSet.getString("DESCRICAO"));
                    contrato.setValorTotal(resultSet.getLong("VALORTOTAL"));
                    contrato.setGastoTotal(resultSet.getLong("GASTOTOTAL"));
                    contrato.setSaldoTotal(resultSet.getLong("SALDOTOTAL"));
                    contrato.setObservacao(resultSet.getString("OBSERVACAO"));
                    contrato.setContrato(resultSet.getString("CONTRATO"));
                    ids.add(contrato);
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ids;
    }

    /**
     *
     * @param autor
     * @param usuario
     * @return
     */
    public int incluirNota(int usuario) {
        try {
            String sql = "INSERT INTO ROOT.NOTA (INCLUSAO, ATUALIZACAO, CANCELADO, ATIVO, NUMERO, EMISSAO, VENCIMENTO, PRENOTA, CLASSIFICADA,"
                    + "LANCADA,VENCIMENTOREAL,TIPO) "
                    + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement prepareStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            prepareStatement.setDate(1, new Date(new java.util.Date().getTime()));
            prepareStatement.setDate(2, new Date(new java.util.Date().getTime()));
            prepareStatement.setBoolean(3, false);
            prepareStatement.setBoolean(4, true);
            prepareStatement.setString(5, "000000000");
            prepareStatement.setDate(6, new Date(new java.util.Date().getTime()));
            prepareStatement.setDate(7, new Date(new java.util.Date().getTime()));
            prepareStatement.setBoolean(8, true);
            prepareStatement.setBoolean(9, false);
            prepareStatement.setBoolean(10, false);
            prepareStatement.setDate(11, new Date(new java.util.Date().getTime()));
            prepareStatement.setString(12, "SE");

            prepareStatement.executeUpdate();
            return idInclusao(prepareStatement, usuario, "NOTA");

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     *
     * @param idFornecedor
     * @param idNota
     * @param numero
     * @return
     */
    public boolean verificarNumeroNota(int idFornecedor, int idNota, String numero) {
        try {
            String sql = "SELECT NOTA.NUMERO FROM NOTA WHERE IDFORNECEDOR = ? AND ID <> ? AND CANCELADO = FALSE";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, idFornecedor);
            prepareStatement.setInt(2, idNota);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    if (numero.equals(resultSet.getString("NUMERO"))) {
                        return true;
                    }

                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /*
     Metodo precisa ser transferido para modulo de Nota
     Verificação necessaria antes de atualizar ou lançar nota
     */
    /**
     *
     * @param nota
     * @return
     */
    public boolean verificarVencimentoNota(Nota nota) {
        return (nota.getVencimento().compareTo(nota.getEmissao()) < 0 || nota.getVencimentoReal().compareTo(nota.getEmissao()) < 0);
    }

    /**
     *
     * @param nota
     * @param usuario
     * @return
     */
    public int atualizarNota(Nota nota, int usuario) {
        if (verificarNumeroNota(nota.getIdFornecedor(), nota.getId(), nota.getNumero())) {
            return -3;
        } else if (verificarVencimentoNota(nota)) {
            return -4;
        }
        Calcular.calcularNota(nota);
        try {
            String sql = "UPDATE ROOT.NOTA SET ATUALIZACAO=?, CANCELADO=?, ATIVO=?, NUMERO=?, FORNECEDOR=?, DESCRICAO=?, "
                    + "VALORTOTAL=?, GASTOTOTAL=?, SALDOTOTAL=?, OBSERVACAO=?, EMISSAO=?, VENCIMENTO=?, PRENOTA=?, CLASSIFICADA=?, PARCELAFINAL=?, "
                    + "LANCADA=?,ORDEM=?, FATURAMENTODIRETO=?, MAPAS=?,SERIE=?,TIPO=?,VENCIMENTOREAL=?,IDFORNECEDOR =?,IDPMWEB = ?,VALORLIQUIDO =? WHERE ID =?";

            nota.getItens().forEach((s) -> {
                atualizarItemNota(s);
            });

            nota.getPagamentos().forEach(s -> {
                atualizarPagamento(s);
            });

            String mapas = "";

            mapas = nota.getItens().stream().collect(Collector.of(
                    () -> new StringJoiner(","),
                    (j, p) -> j.add(String.valueOf(p.getId())),
                    (j1, j2) -> j1.merge(j2),
                    StringJoiner::toString));

            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setDate(1, new Date(new java.util.Date().getTime()));
            prepareStatement.setBoolean(2, nota.isCancelado());
            prepareStatement.setBoolean(3, nota.isAtivo());
            prepareStatement.setString(4, nota.getNumero());
            prepareStatement.setString(5, nota.getFornecedor());
            prepareStatement.setString(6, nota.getDescricao());
            prepareStatement.setLong(7, nota.getValorTotal());
            prepareStatement.setLong(8, nota.getGastoTotal());
            prepareStatement.setLong(9, nota.getSaldoTotal());
            prepareStatement.setString(10, nota.getObservacao());
            prepareStatement.setDate(11, new Date(nota.getEmissao().getTime()));
            prepareStatement.setDate(12, new Date(nota.getVencimento().getTime()));
            prepareStatement.setBoolean(13, nota.isPreNota());
            prepareStatement.setBoolean(14, nota.isClassificada());
            prepareStatement.setBoolean(15, nota.isParcelaFinal());
            prepareStatement.setBoolean(16, nota.isLancada());
            prepareStatement.setInt(17, nota.getOrdem());
            prepareStatement.setString(18, nota.getFaturamentoDireto());
            prepareStatement.setString(19, mapas);
            prepareStatement.setString(20, nota.getSerie());
            prepareStatement.setString(21, nota.getTipo());
            prepareStatement.setDate(22, new Date(nota.getVencimentoReal().getTime()));
            prepareStatement.setInt(23, nota.getIdFornecedor());
            prepareStatement.setInt(24, nota.getIdPmWeb());
            prepareStatement.setLong(25, nota.getValorLiquido());
            prepareStatement.setInt(26, nota.getId());
            salvarEvento(nota.getId(), usuario, "atualizou", "NOTA", "");
            return prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }

    }

    /**
     *
     * @param idNota
     * @param autor
     * @return
     */
    public int incluirItemNota(int autor, int idNota) {
        try {
            String sql = "INSERT INTO ROOT.ITEMNOTA (INCLUSAO, ATUALIZACAO, CANCELADO, ATIVO,IDNOTA) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

            PreparedStatement prepareStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            prepareStatement.setDate(1, new Date(new java.util.Date().getTime()));
            prepareStatement.setDate(2, new Date(new java.util.Date().getTime()));
            prepareStatement.setBoolean(3, false);
            prepareStatement.setBoolean(4, true);
            prepareStatement.setInt(5, idNota);

            prepareStatement.executeUpdate();

            return idInclusao(prepareStatement, autor, "ITEMNOTA");

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     *
     * @param itemContrato
     */
    public void atualizarItemNota(ItemNota itemContrato) {
        try {
            String sql = "UPDATE ROOT.ITEMNOTA SET ATUALIZACAO = ?, AUTOR = ?, CANCELADO = ?, ATIVO = ?, ITEM = ?, PLANO = ?, TIPO = ?, CUSTO = ?, "
                    + "VALOR = ?, SALDO = ?, DESCRICAO = ?, UNIDADE = ?, QNTTOTAL = ?, PRECOUNITARIO = ?, PRECOTOTAL = ?, NUMEROMAPA = ?, "
                    + "DESCRICAOMAPA = ?, RETENCAOEMPREITEIRO = ?, IDCONTRATO = ? WHERE ID = ?";

            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            prepareStatement.setDate(1, new Date(new java.util.Date().getTime()));
            prepareStatement.setString(2, itemContrato.getAutor());
            prepareStatement.setBoolean(3, itemContrato.isCancelado());
            prepareStatement.setBoolean(4, itemContrato.isAtivo());
            prepareStatement.setString(5, itemContrato.getItem());
            prepareStatement.setString(6, itemContrato.getPlano());
            prepareStatement.setString(7, itemContrato.getTipo());
            prepareStatement.setString(8, itemContrato.getCusto());
            prepareStatement.setLong(9, itemContrato.getValor());
            prepareStatement.setLong(10, itemContrato.getSaldo());
            prepareStatement.setString(11, itemContrato.getDescricao());
            prepareStatement.setString(12, itemContrato.getUnidade());
            prepareStatement.setLong(13, itemContrato.getQntTotal());
            prepareStatement.setLong(14, itemContrato.getPrecoUnitario());
            prepareStatement.setLong(15, itemContrato.getPrecoTotal());
            prepareStatement.setString(16, itemContrato.getNumeroMapa());
            prepareStatement.setString(17, itemContrato.getDescricaoMapa());
            prepareStatement.setLong(18, itemContrato.getRetencaoEmpreiteiro());
            prepareStatement.setInt(19, itemContrato.getIdContrato());

            prepareStatement.setInt(20, itemContrato.getId());

            prepareStatement.executeUpdate();
            atualizarItemNotaRetencao(itemContrato);
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param item
     */
    public void atualizarItemNotaRetencao(ItemNota item) {
        try {
            String sql = "UPDATE RETENCAO SET BASEISSQN = ?,ALIQUOTAISSQN = ?,VALORISSQN = ?,BASECSRF = ?,ALIQUOTACSRF = ?,VALORCSRF = ?,BASEIRRF = ?,"
                    + "ALIQUOTAIRRF = ?,VALORIRRF = ?,BASEINSS = ?,ALIQUOTAINSS = ?,VALORINSS = ?,VALORLIQUIDO = ?,CODIGO = ?,CFOP = ? WHERE IDITEM = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setLong(1, item.getBaseISSQN());
            prepareStatement.setFloat(2, item.getAliquotaISSQN());
            prepareStatement.setLong(3, item.getValorISSQN());

            prepareStatement.setLong(4, item.getBaseCSRF());
            prepareStatement.setFloat(5, item.getAliquotaCSRF());
            prepareStatement.setLong(6, item.getValorCSRF());

            prepareStatement.setLong(7, item.getBaseIRRF());
            prepareStatement.setFloat(8, item.getAliquotaIRRF());
            prepareStatement.setLong(9, item.getValorIRRF());

            prepareStatement.setLong(10, item.getBaseINSS());
            prepareStatement.setFloat(11, item.getAliquotaINSS());
            prepareStatement.setLong(12, item.getValorINSS());

            prepareStatement.setLong(13, item.getValorLiquido());
            prepareStatement.setInt(14, item.getCodigo());
            prepareStatement.setInt(15, item.getCfop());

            prepareStatement.setInt(16, item.getId());

            prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param usuario
     * @param descricao
     */
    public void salvarAlteracao(String usuario, String descricao) {
        try {
            String sql = "INSERT INTO ROOT.ALTERACAO (USUARIO, DESCRICAO, DIA) "
                    + "VALUES ( ?, ?, CURRENT_DATE)";

            descricao += " " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setString(1, usuario);
            prepareStatement.setString(2, descricao);
            prepareStatement.executeUpdate();
            Principal.setStatus(usuario + ":" + descricao);
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private ArrayList<Integer> buscarBanco(String banco, String campo, int valor) {
        ArrayList<Integer> temp = new ArrayList<>();
        try {

            String sql = "SELECT ID FROM " + banco + " WHERE " + campo + " = ?";//upper(
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, valor);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    temp.add(resultSet.getInt("ID"));
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return temp;
    }

    /**
     *
     * @return
     */
    public ArrayList<Nota> buscarPreNotas() {
        ArrayList<Nota> ids = new ArrayList<>();
        try {
            String sql = "SELECT NOTA.ID,NOTA.INCLUSAO,NOTA.NUMERO,NOTA.SERIE,FORNECEDOR.RAZAOSOCIAL,FORNECEDOR.CNPJ,NOTA.DESCRICAO,NOTA.GASTOTOTAL,NOTA.VENCIMENTO,"
                    + "NOTA.VENCIMENTOREAL,NOTA.TIPO,NOTA.PRENOTA,NOTA.CLASSIFICADA,NOTA.LANCADA,NOTA.OBSERVACAO,NOTA.IDFORNECEDOR,NOTA.IDPMWEB"
                    + " FROM NOTA LEFT JOIN FORNECEDOR ON NOTA.IDFORNECEDOR = FORNECEDOR.ID WHERE NOTA.CANCELADO = false AND NOTA.PRENOTA = true ORDER BY ID DESC";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    Nota nota = new Nota();
                    nota.setId(resultSet.getInt("ID"));
                    nota.setNumero(resultSet.getString("NUMERO"));
                    nota.setInclusao(resultSet.getDate("INCLUSAO"));
                    nota.setSerie(resultSet.getString("SERIE"));
                    nota.setTipo(resultSet.getString("TIPO"));
                    nota.setFornecedor(resultSet.getString("RAZAOSOCIAL") + " " + resultSet.getString("CNPJ"));
                    nota.setDescricao(resultSet.getString("DESCRICAO"));
                    nota.setGastoTotal(resultSet.getLong("GASTOTOTAL"));
                    nota.setObservacao(resultSet.getString("OBSERVACAO"));
                    nota.setVencimento(resultSet.getDate("VENCIMENTO"));
                    nota.setVencimentoReal(resultSet.getDate("VENCIMENTOREAL"));
                    nota.setPreNota(resultSet.getBoolean("PRENOTA"));
                    nota.setClassificada(resultSet.getBoolean("CLASSIFICADA"));
                    nota.setLancada(resultSet.getBoolean("LANCADA"));
                    nota.setIdPmWeb(resultSet.getInt("IDPMWEB"));
                    nota.setStatus(buscarPmWeb(nota.getIdPmWeb()).getStatus());
                    ids.add(nota);
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ids;
    }

    /**
     *
     * @param idNota
     * @param usuario
     * @return
     */
    public int lancarNota(int idNota, int usuario) {
        Nota nota = buscarNotaId(idNota);
        if (nota.getIdContrato() <= 0) {
            return -1;
        }
        if (nota.getIdPmWeb() == 0) {
            return -2;
        }
        if (!nota.isPreNota()) {
            return -3;
        }
        if (nota.getGastoTotal() == 0) {
            return -4;
        }
        Contrato contrato = buscarContrato(nota.getIdContrato());
        ArrayList<ItemContrato> itemContrato = new ArrayList<>();
        contrato.getMapas().stream().forEach((mapa) -> {
            mapa.getItens().stream().filter((item) -> (contemItem(nota.getItens(), item.getItem()))).forEach((item) -> {
                itemContrato.add(item);
            });
        });
        for (ItemNota item : nota.getItens()) {
            long valor = 0;
            long gasto = item.getPrecoTotal();
            valor = itemContrato.stream().filter(
                    (s) -> s.getItem().equals(item.getItem())).map(
                            (d) -> d.getSaldo()).reduce(
                            valor, (accumulator, _item) -> accumulator + _item);
            item.setValor(valor);
            Iterator ite = itemContrato.stream().filter((s) -> item.getItem().equals(s.getItem())).iterator();
            while (ite.hasNext()) {
                ItemContrato d = (ItemContrato) ite.next();
                if (d.getSaldo() > 0) {
                    if (d.getSaldo() >= gasto) {
                        d.setValor(d.getValor() + gasto);
                        gasto = 0;
                    } else {
                        d.setValor(d.getValor() + d.getSaldo());
                        gasto -= d.getSaldo();
                    }
                }
                if (gasto == 0) {
                    break;
                }

            }
            if (gasto != 0) {
                Iterator iteTemp = itemContrato.stream().filter((s) -> item.getItem().equals(s.getItem())).iterator();
                if (iteTemp.hasNext()) {
                    ItemContrato temp = (ItemContrato) iteTemp.next();
                    temp.setValor(temp.getValor() + gasto);
                }
            }

        }

        nota.setPreNota(false);

        atualizarNota(nota, usuario);
        atualizarContrato(contrato, usuario);
        contrato.getMapas().forEach(s -> {
            atualizarMapaContrato(s);
        });

        Status temp = new Status();
        temp.setId(nota.getIdPmWeb());
        temp.setStatus("Submitted");

        Status temp2 = buscarPmWeb(nota.getIdPmWeb());
        if (temp2.getId() == 0) {
            incluirPmWeb(temp);
        } else if (temp2.getStatus().equals("approved")) {
            aprovarNota(nota, 0);
        }

        salvarEvento(nota.getId(), usuario, "Lançou", "NOTA", "");

        return 1;
    }

    /**
     *
     * @param nota
     * @param usuario
     * @return
     */
    public int estornarNota(Nota nota, int usuario) {
        if (nota.getIdContrato() <= 0) {
            return -1;
        }
        if (nota.isLancada()) {
            return -2;
        }
        if (nota.isPreNota()) {
            return -3;
        }

        Contrato contrato = buscarContrato(nota.getIdContrato());
        ArrayList<ItemContrato> itemContrato = new ArrayList<>();
        contrato.getMapas().stream().forEach((mapa) -> {
            mapa.getItens().stream().filter((item) -> (contemItem(nota.getItens(), item.getItem()))).forEach((item) -> {
                itemContrato.add(item);
            });
        });
        for (ItemNota item : nota.getItens()) {
            long valor = 0;
            long gasto = item.getPrecoTotal();
            valor = itemContrato.stream().filter(
                    (s) -> s.getItem().equals(item.getItem())).map(
                            (d) -> d.getPrecoTotal()).reduce(
                            valor, (accumulator, _item) -> accumulator + _item);
            item.setValor(valor);
            Iterator ite = itemContrato.stream().filter((s) -> item.getItem().equals(s.getItem())).iterator();
            while (ite.hasNext()) {
                ItemContrato d = (ItemContrato) ite.next();
                if (d.getValor() >= 0) {//verifica se saldo do itemContrato é maior ou igual a 0
                    if (d.getValor() >= gasto) {//verifica se gasto do itemContrato é maior que o valor gasto pelo itemNota
                        /*
                         se gasto do itemContrato for maior que o valor gasto pelo itemNota o valor gasto vai ser subtraido do gasto
                         */
                        d.setValor(d.getValor() - gasto);
                        gasto = 0;
                    } else {
                        /*
                         se gasto do itemContrato for menor que o valor gasto pelo itemNota o valor gasto 
                         */
                        gasto -= d.getValor();
                        d.setValor(0);
                    }
                }
                if (gasto == 0) {
                    break;
                }

            }

            if (gasto != 0) {
                Iterator iteTemp = itemContrato.stream().filter((s) -> item.getItem().equals(s.getItem())).iterator();
                if (iteTemp.hasNext()) {
                    ItemContrato temp = (ItemContrato) iteTemp.next();
                    temp.setValor(temp.getValor() - gasto);
                }
            }

        }

        nota.setPreNota(true);

        Calcular.calcularNota(nota);
        Calcular.calcularContrato(contrato);

        atualizarNota(nota, usuario);
        atualizarContrato(contrato, usuario);
        contrato.getMapas().forEach(s -> {
            atualizarMapaContrato(s);
        });

        salvarEvento(nota.getId(), usuario, "Estornou", "NOTA", "");

        return 1;

    }

    private boolean contemItem(ArrayList<ItemNota> itens, String item) {
        return itens.stream().anyMatch((itemNota) -> (itemNota.getItem().equals(item)));
    }

    /**
     *
     * @param contrato
     * @param usuario
     * @return
     */
    public int incluirContrato(int usuario) {
        try {
            String sql = "INSERT INTO ROOT.CONTRATO (INCLUSAO, ATUALIZACAO, CANCELADO, ATIVO) "
                    + "VALUES (?, ?, ?, ?)";

            PreparedStatement prepareStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setDate(1, new Date(new java.util.Date().getTime()));
            prepareStatement.setDate(2, new Date(new java.util.Date().getTime()));
            prepareStatement.setBoolean(3, false);
            prepareStatement.setBoolean(4, true);
            prepareStatement.executeUpdate();
            ResultSet result = prepareStatement.getGeneratedKeys();
            if (result.next()) {
                int id = result.getInt(1);
                salvarEvento(id, usuario, "incluiu", "CONTRATO", "");
                return id;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;

    }

    /**
     *
     * @param contrato
     * @param usuario
     * @return
     */
    public int atualizarContrato(Contrato contrato, int usuario) {
        try {
            String sql = "UPDATE ROOT.CONTRATO SET ATUALIZACAO=?,  CANCELADO=?, ATIVO=?, NUMERO=?, FORNECEDOR=?, DESCRICAO=?, "
                    + "VALORTOTAL=?, GASTOTOTAL=?, SALDOTOTAL=?, OBSERVACAO=?, EMISSAO=?, PMWEB=?, IDPMWEB=?, CONTRATO=?, STATUS=?, RESPONSAVEL=?,IDFORNECEDOR = ? WHERE ID =?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            prepareStatement.setDate(1, new Date(new java.util.Date().getTime()));
            prepareStatement.setBoolean(2, contrato.isCancelado());
            prepareStatement.setBoolean(3, contrato.isAtivo());
            prepareStatement.setString(4, contrato.getNumero());
            prepareStatement.setString(5, contrato.getFornecedor());
            prepareStatement.setString(6, contrato.getDescricao());
            prepareStatement.setLong(7, contrato.getValorTotal());
            prepareStatement.setLong(8, contrato.getGastoTotal());
            prepareStatement.setLong(9, contrato.getSaldoTotal());
            prepareStatement.setString(10, contrato.getObservacao());
            prepareStatement.setDate(11, new Date(contrato.getEmissao().getTime()));
            prepareStatement.setString(12, "");
            prepareStatement.setInt(13, 0);
            prepareStatement.setString(14, contrato.getContrato());
            prepareStatement.setString(15, "");
            prepareStatement.setString(16, contrato.getAutor());
            prepareStatement.setInt(17, contrato.getIdFornecedor());

            prepareStatement.setInt(18, contrato.getId());

            salvarEvento(contrato.getId(), usuario, "atualizou", "CONTRATO", "");

            return prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;

    }

    /**
     *
     * @param mapaContrato
     */
    public int incluirMapaContrato(int autor, int idContrato) {
        try {
            String sql = "INSERT INTO ROOT.MAPACONTRATO (INCLUSAO, ATUALIZACAO, CANCELADO, ATIVO,CRIACAO,IDCONTRATO) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement prepareStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            prepareStatement.setDate(1, new Date(new java.util.Date().getTime()));
            prepareStatement.setDate(2, new Date(new java.util.Date().getTime()));
            prepareStatement.setBoolean(3, false);
            prepareStatement.setBoolean(4, true);
            prepareStatement.setDate(5, new Date(new java.util.Date().getTime()));
            prepareStatement.setInt(6, idContrato);

            prepareStatement.executeUpdate();
            return idInclusao(prepareStatement, autor, "MAPACONTRATO");
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     *
     * @param mapaContrato
     * @return
     */
    public int atualizarMapaContrato(MapaContrato mapaContrato) {
        try {
            String sql = "UPDATE ROOT.MAPACONTRATO SET ATUALIZACAO = ?, AUTOR = ?, CANCELADO = ?, ATIVO = ?, NUMERO = ?, DESCRICAO = ?, EMPRESA = ?, "
                    + "VALOR = ?, TOTAL = ?, SALDO = ?,  ASSINATURA = ?, CRIACAO = ?, OBSERVACAOASSINATURA = ?, OBSERVACAOSISTEMA = ?, FISICO = ?, ENCERRAMENTO = ?, OBSERVACAO = ?, CONTRATO = ? WHERE ID = ?";

            mapaContrato.getItens().forEach((s) -> {
                atualizarItemContrato(s);
            });

            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            prepareStatement.setDate(1, new Date(new java.util.Date().getTime()));
            prepareStatement.setString(2, mapaContrato.getAutor());
            prepareStatement.setBoolean(3, mapaContrato.isCancelado());
            prepareStatement.setBoolean(4, mapaContrato.isAtivo());
            prepareStatement.setString(5, mapaContrato.getNumero());
            prepareStatement.setString(6, mapaContrato.getDescricao());
            prepareStatement.setString(7, mapaContrato.getEmpresa());
            prepareStatement.setLong(8, mapaContrato.getValor());
            prepareStatement.setLong(9, mapaContrato.getTotal());
            prepareStatement.setLong(10, mapaContrato.getSaldo());
            prepareStatement.setDate(11, new Date(mapaContrato.getAssinatura().getTime()));
            prepareStatement.setDate(12, new Date(mapaContrato.getCriacao().getTime()));
            prepareStatement.setString(13, mapaContrato.getObservacaoAssinatura());
            prepareStatement.setString(14, mapaContrato.getObservacaoSistema());
            prepareStatement.setString(15, mapaContrato.getFisico());
            prepareStatement.setString(16, mapaContrato.getEncerramento());
            prepareStatement.setString(17, mapaContrato.getObservacao());
            prepareStatement.setString(18, mapaContrato.getContrato());

            prepareStatement.setInt(19, mapaContrato.getId());

            return prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }

    }

    /**
     *
     * @param itemContrato
     */
    public int incluirItemContrato(int autor, int idContrato, int idMapa) {
        try {
            String sql = "INSERT INTO ROOT.ITEMCONTRATO (INCLUSAO, ATUALIZACAO, CANCELADO, ATIVO,IDCONTRATO,IDMAPA) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

            PreparedStatement prepareStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            prepareStatement.setDate(2, new Date(new java.util.Date().getTime()));
            prepareStatement.setDate(3, new Date(new java.util.Date().getTime()));
            prepareStatement.setBoolean(5, false);
            prepareStatement.setBoolean(6, true);
            prepareStatement.setInt(21, idContrato);
            prepareStatement.setInt(22, idMapa);

            prepareStatement.executeUpdate();
            return idInclusao(prepareStatement, autor, "ITEMCONTRATO");

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     *
     * @param itemContrato
     */
    public void atualizarItemContrato(ItemContrato itemContrato) {
        try {
            String sql = "UPDATE ROOT.ITEMCONTRATO SET ATUALIZACAO = ?, AUTOR = ?, CANCELADO = ?, ATIVO = ?, ITEM = ?, PLANO = ?, TIPO = ?, CUSTO = ?, VALOR = ?, "
                    + "SALDO = ?, DESCRICAO = ?, UNIDADE = ?, QNTTOTAL = ?, PRECOUNITARIO = ?, PRECOTOTAL = ?, NUMEROMAPA = ?, DESCRICAOMAPA = ? WHERE ID = ?";

            itemContrato.getProdutos().forEach((s) -> {
                atualizarProdutoContrato(s);
            });

            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            prepareStatement.setDate(1, new Date(new java.util.Date().getTime()));
            prepareStatement.setString(2, itemContrato.getAutor());
            prepareStatement.setBoolean(3, itemContrato.isCancelado());
            prepareStatement.setBoolean(4, itemContrato.isAtivo());
            prepareStatement.setString(5, itemContrato.getItem());
            prepareStatement.setString(6, itemContrato.getPlano());
            prepareStatement.setString(7, itemContrato.getTipo());
            prepareStatement.setString(8, itemContrato.getCusto());
            prepareStatement.setLong(9, itemContrato.getValor());
            prepareStatement.setLong(10, itemContrato.getSaldo());
            prepareStatement.setString(11, itemContrato.getDescricao());
            prepareStatement.setString(12, itemContrato.getUnidade());
            prepareStatement.setLong(13, itemContrato.getQntTotal());
            prepareStatement.setLong(14, itemContrato.getPrecoUnitario());
            prepareStatement.setLong(15, itemContrato.getPrecoTotal());
            prepareStatement.setString(16, itemContrato.getNumeroMapa());
            prepareStatement.setString(17, itemContrato.getDescricaoMapa());

            prepareStatement.setInt(18, itemContrato.getId());
            prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param produtoContrato
     */
    public int incluirProdutoContrato(int autor) {
        try {
            String sql = "INSERT INTO ROOT.PRODUTOCONTRATO (INCLUSAO, ATUALIZACAO,CANCELADO, ATIVO) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement prepareStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            prepareStatement.setDate(1, new Date(new java.util.Date().getTime()));
            prepareStatement.setDate(2, new Date(new java.util.Date().getTime()));
            prepareStatement.setBoolean(3, false);
            prepareStatement.setBoolean(4, true);

            prepareStatement.executeUpdate();
            return idInclusao(prepareStatement, autor, "PRODUTOCONTRATO");

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     *
     * @param produtoContrato
     */
    public void atualizarProdutoContrato(ProdutoContrato produtoContrato) {
        try {
            String sql = "UPDATE ROOT.PRODUTOCONTRATO SET ATUALIZACAO = ? , AUTOR = ? , CANCELADO = ? , ATIVO = ? ,"
                    + " CODIGO = ? , DESCRICAO = ? , UNIDADE = ? , QNT = ? , PRECOUNITARIO = ? , PRECOTOTAL = ? WHERE ID = ?";

            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            //prepareStatement.setDate(2, new Date(produtoContrato.getInclusao().getTime()));
            prepareStatement.setDate(1, new Date(new java.util.Date().getTime()));
            prepareStatement.setString(2, produtoContrato.getAutor());
            prepareStatement.setBoolean(3, produtoContrato.isCancelado());
            prepareStatement.setBoolean(4, produtoContrato.isAtivo());
            prepareStatement.setString(5, produtoContrato.getCodigo());
            prepareStatement.setString(6, produtoContrato.getDescricao());
            prepareStatement.setString(7, produtoContrato.getUnidade());
            prepareStatement.setLong(8, produtoContrato.getQnt());
            prepareStatement.setLong(9, produtoContrato.getPrecoUnitario());
            prepareStatement.setLong(10, produtoContrato.getPrecoTotal());
            prepareStatement.setInt(11, produtoContrato.getId());
            prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public ArrayList<Nota> buscarNotasContrato(int id) {

        ArrayList<Nota> notas = new ArrayList<>();
        try {

            String sql = "SELECT NOTA.ID,NOTA.NUMERO,NOTA.SERIE,FORNECEDOR.RAZAOSOCIAL,FORNECEDOR.CNPJ,NOTA.DESCRICAO,NOTA.GASTOTOTAL,NOTA.EMISSAO,NOTA.VENCIMENTO,"
                    + "NOTA.VENCIMENTOREAL,NOTA.TIPO,NOTA.PRENOTA,NOTA.CLASSIFICADA,NOTA.LANCADA,NOTA.CANCELADO,NOTA.OBSERVACAO"
                    + " FROM NOTA LEFT JOIN FORNECEDOR ON NOTA.IDFORNECEDOR = FORNECEDOR.ID WHERE NOTA.IDCONTRATO = ?";

            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {

                do {
                    Nota nota = new Nota();

                    nota.setId(resultSet.getInt("ID"));
                    nota.setNumero(resultSet.getString("NUMERO"));
                    nota.setSerie(resultSet.getString("SERIE"));
                    nota.setTipo(resultSet.getString("TIPO"));
                    nota.setFornecedor(resultSet.getString("RAZAOSOCIAL") + " " + resultSet.getString("CNPJ"));
                    nota.setDescricao(resultSet.getString("DESCRICAO"));
                    nota.setGastoTotal(resultSet.getLong("GASTOTOTAL"));
                    nota.setObservacao(resultSet.getString("OBSERVACAO"));
                    nota.setEmissao(resultSet.getDate("EMISSAO"));
                    nota.setVencimento(resultSet.getDate("VENCIMENTO"));
                    nota.setVencimentoReal(resultSet.getDate("VENCIMENTOREAL"));
                    nota.setPreNota(resultSet.getBoolean("PRENOTA"));
                    nota.setClassificada(resultSet.getBoolean("CLASSIFICADA"));
                    nota.setLancada(resultSet.getBoolean("LANCADA"));
                    nota.setCancelado(resultSet.getBoolean("CANCELADO"));

                    notas.add(nota);
                } while (resultSet.next());

            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    /**
     *
     * @param item
     * @return
     */
    public ItemOrcamento buscarItemOrcamento(String item) {
        ItemOrcamento itemOrcamento = new ItemOrcamento();
        try {

            String sql = "SELECT ID, ITEM, PLANOCONTA, DESCRICAO, UNIDADE, QUANTIDADETOTAL, PRECOUNITARIO, PRECOTOTAL FROM ITEMORCAMENTO WHERE ITEM = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setString(1, item);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                itemOrcamento.setId(resultSet.getInt("ID"));
                itemOrcamento.setItem(resultSet.getString("ITEM"));
                itemOrcamento.setPlanoConta(resultSet.getString("PLANOCONTA"));
                itemOrcamento.setDescricao(resultSet.getString("DESCRICAO"));
                itemOrcamento.setUnidade(resultSet.getString("UNIDADE"));
                itemOrcamento.setQuantidadeTotal(resultSet.getLong("QUANTIDADETOTAL"));
                itemOrcamento.setPrecoUnitario(resultSet.getLong("PRECOUNITARIO"));
                itemOrcamento.setPrecoTotal(resultSet.getLong("PRECOTOTAL"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itemOrcamento;
    }

    /**
     *
     * @param parametros
     * @return
     */
    public ArrayList<Nota> buscarNotasRelatorio(Parametros parametros) {
        ArrayList<Nota> notas = new ArrayList<>();
        try {

            String sql = "SELECT NOTA.ID,NOTA.NUMERO,NOTA.SERIE,FORNECEDOR.RAZAOSOCIAL,FORNECEDOR,CNPJ,NOTA.DESCRICAO,NOTA.GASTOTOTAL,NOTA.VENCIMENTO,NOTA.VENCIMENTOREAL,NOTA.TIPO,"
                    + "NOTA.PRENOTA,NOTA.CLASSIFICADA,NOTA.LANCADA,NOTA.OBSERVACAO,NOTA.EMISSAO,NOTA.IDPMWEB "
                    + "FROM NOTA LEFT JOIN FORNECEDOR ON NOTA.IDFORNECEDOR = FORNECEDOR.ID "
                    + "WHERE NOTA.EMISSAO BETWEEN ? AND ? AND "
                    + "NOTA.VENCIMENTOREAL BETWEEN ? AND ? AND upper(NOTA.FORNECEDOR) LIKE ? AND NOTA.GASTOTOTAL BETWEEN ? AND ? AND NOTA.CANCELADO = FALSE";

            if (!parametros.isAll()) {
                sql += " AND NOTA.CLASSIFICADA = ?";
            }

            PreparedStatement prepareStatement = connection.prepareCall(sql);

            prepareStatement.setDate(1, new Date(parametros.getEmissaoDe().getTime()));
            prepareStatement.setDate(2, new Date(parametros.getEmissaoAte().getTime()));
            prepareStatement.setDate(3, new Date(parametros.getVencimentoDe().getTime()));
            prepareStatement.setDate(4, new Date(parametros.getVencimentoAte().getTime()));
            prepareStatement.setString(5, parametros.getFornecedor());
            prepareStatement.setLong(6, parametros.getValorDe());
            prepareStatement.setLong(7, parametros.getValorAte());
            if (!parametros.isAll()) {
                prepareStatement.setBoolean(8, parametros.isClassificada());
            }
            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {
                do {
                    Nota nota = new Nota();
                    nota.setId(resultSet.getInt("ID"));
                    nota.setNumero(resultSet.getString("NUMERO"));
                    nota.setSerie(resultSet.getString("SERIE"));
                    nota.setTipo(resultSet.getString("TIPO"));
                    nota.setFornecedor(resultSet.getString("RAZAOSOCIAL") + " " + resultSet.getString("CNPJ"));
                    nota.setDescricao(resultSet.getString("DESCRICAO"));
                    nota.setGastoTotal(resultSet.getLong("GASTOTOTAL"));
                    nota.setObservacao(resultSet.getString("OBSERVACAO"));
                    nota.setEmissao(resultSet.getDate("EMISSAO"));
                    nota.setVencimento(resultSet.getDate("VENCIMENTO"));
                    nota.setVencimentoReal(resultSet.getDate("VENCIMENTOREAL"));
                    nota.setPreNota(resultSet.getBoolean("PRENOTA"));
                    nota.setClassificada(resultSet.getBoolean("CLASSIFICADA"));
                    nota.setLancada(resultSet.getBoolean("LANCADA"));
                    nota.setIdPmWeb(resultSet.getInt("IDPMWEB"));
                    nota.setStatus(buscarPmWeb(nota.getIdPmWeb()).getStatus());

                    notas.add(nota);
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    /**
     *
     * @param parametros
     * @return
     */
    public ArrayList<Nota> buscarNotasRelatorioOrcamento(Parametros parametros) {
        ArrayList<Nota> notas = new ArrayList<>();
        try {

            String sql = "SELECT NOTA.ID,NOTA.INCLUSAO,NOTA.ATUALIZACAO,NOTA.AUTOR,NOTA.CANCELADO,NOTA.ATIVO,NOTA.NUMERO,NOTA.SERIE,"
                    + "NOTA.TIPO,FORNECEDOR.RAZAOSOCIAL,FORNECEDOR.CNPJ,NOTA.VALORTOTAL,NOTA.GASTOTOTAL,NOTA.SALDOTOTAL,NOTA.EMISSAO,NOTA.VENCIMENTO,"
                    + "NOTA.VENCIMENTOREAL,NOTA.PRENOTA,NOTA.CLASSIFICADA,NOTA.LANCADA,NOTA.MAPAS,NOTA.DESCRICAO,"
                    + "NOTA.OBSERVACAO,NOTA.CONTRATO,NOTA.IDFORNECEDOR,NOTA.IDFATURAMENTODIRETO,NOTA.IDCONTRATO,"
                    + "(SELECT FORNECEDOR.RAZAOSOCIAL FROM CONTRATO LEFT JOIN FORNECEDOR ON CONTRATO.IDFORNECEDOR = FORNECEDOR.ID WHERE CONTRATO.ID = NOTA.IDCONTRATO) "
                    + "FROM NOTA LEFT JOIN FORNECEDOR ON NOTA.IDFORNECEDOR = FORNECEDOR.ID "
                    + "WHERE NOTA.EMISSAO BETWEEN ? AND ? AND "
                    + "NOTA.VENCIMENTOREAL BETWEEN ? AND ? AND upper(NOTA.FORNECEDOR) LIKE ? AND NOTA.GASTOTOTAL BETWEEN ? AND ? AND NOTA.PRENOTA = FALSE AND NOTA.CANCELADO = FALSE";

            if (!parametros.isAll()) {
                sql += " AND NOTA.CLASSIFICADA = ?";
            }

            PreparedStatement prepareStatement = connection.prepareCall(sql);

            prepareStatement.setDate(1, new Date(parametros.getEmissaoDe().getTime()));
            prepareStatement.setDate(2, new Date(parametros.getEmissaoAte().getTime()));
            prepareStatement.setDate(3, new Date(parametros.getVencimentoDe().getTime()));
            prepareStatement.setDate(4, new Date(parametros.getVencimentoAte().getTime()));
            prepareStatement.setString(5, parametros.getFornecedor());
            prepareStatement.setLong(6, parametros.getValorDe());
            prepareStatement.setLong(7, parametros.getValorAte());
            if (!parametros.isAll()) {
                prepareStatement.setBoolean(8, parametros.isClassificada());
            }
            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {
                do {
                    Nota nota = new Nota();
                    nota.setId(resultSet.getInt("ID"));
                    nota.setInclusao(resultSet.getDate("INCLUSAO"));
                    nota.setAtualizacao(resultSet.getDate("ATUALIZACAO"));
                    nota.setAutor(resultSet.getString("AUTOR"));
                    nota.setCancelado(resultSet.getBoolean("CANCELADO"));
                    nota.setDescricao(resultSet.getString("DESCRICAO"));
                    nota.setObservacao(resultSet.getString("OBSERVACAO"));
                    nota.setContrato(resultSet.getString("CONTRATO"));
                    nota.setAtivo(resultSet.getBoolean("ATIVO"));
                    nota.setNumero(resultSet.getString("NUMERO"));
                    nota.setSerie(resultSet.getString("SERIE"));
                    nota.setTipo(resultSet.getString("TIPO"));
                    nota.setFornecedor(resultSet.getString("RAZAOSOCIAL") + " " + resultSet.getString("CNPJ"));
                    nota.setValorTotal(resultSet.getLong("VALORTOTAL"));
                    nota.setGastoTotal(resultSet.getLong("GASTOTOTAL"));
                    nota.setSaldoTotal(resultSet.getLong("SALDOTOTAL"));
                    nota.setEmissao(resultSet.getDate("EMISSAO"));
                    nota.setVencimento(resultSet.getDate("VENCIMENTO"));
                    nota.setVencimentoReal(resultSet.getDate("VENCIMENTOREAL"));
                    nota.setPreNota(resultSet.getBoolean("PRENOTA"));
                    nota.setClassificada(resultSet.getBoolean("CLASSIFICADA"));
                    nota.setLancada(resultSet.getBoolean("LANCADA"));
                    nota.setFaturamentoDireto(resultSet.getString(28));
                    nota.setIdFornecedor(resultSet.getInt("IDFORNECEDOR"));
                    nota.setIdFaturamentoDireto(resultSet.getInt("IDFATURAMENTODIRETO"));
                    nota.setIdContrato(resultSet.getInt("IDCONTRATO"));

                    nota.setItens(buscarItensNota(nota.getId()));
                    notas.add(nota);
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    /**
     *
     * @param limite
     * @return
     */
    public ArrayList<Fornecedor> buscarUltimosFornecedores(int limite) {
        ArrayList<Fornecedor> fornecedores = new ArrayList<>();
        try {
            String sql = "SELECT FORNECEDOR.ID, FORNECEDOR.CNPJ, FORNECEDOR.INCLUSAO, FORNECEDOR.ATUALIZACAO, CONTA.NOME, FORNECEDOR.CANCELADO, FORNECEDOR.ATIVO,"
                    + "FORNECEDOR.RAZAOSOCIAL, FORNECEDOR.NOMEFANTASIA, FORNECEDOR.SIGLA, TIPOEMPRESA.NOME, FORNECEDOR.INSCESTADUAL, FORNECEDOR.INSCMUNICIPAL, NATUREZAFORNECEDOR.NOME "
                    + "FROM FORNECEDOR "
                    + "LEFT JOIN NATUREZAFORNECEDOR ON NATUREZAFORNECEDOR.ID = FORNECEDOR.NATUREZA "
                    + "LEFT JOIN TIPOEMPRESA ON TIPOEMPRESA.ID = FORNECEDOR.TIPOEMPRESA "
                    + "LEFT JOIN CONTA ON CONTA.ID = FORNECEDOR.AUTOR "
                    + "ORDER BY ID DESC";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setMaxRows(limite);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    Fornecedor fornecedor = new Fornecedor();

                    fornecedor.setId(resultSet.getInt(1));
                    fornecedor.setCnpj(resultSet.getString(2));
                    fornecedor.setInclusao(new Date(resultSet.getLong(3)));
                    fornecedor.setAtualizacao(new Date(resultSet.getLong(4)));
                    fornecedor.setAutor(resultSet.getString(5));
                    fornecedor.setCancelado(resultSet.getBoolean(6));
                    fornecedor.setAtivo(resultSet.getBoolean(7));
                    fornecedor.setRazaoSocial(resultSet.getString(8));
                    fornecedor.setNomeFantasia(resultSet.getString(9));
                    fornecedor.setSigla(resultSet.getString(10));
                    fornecedor.setTipoEmpresa(resultSet.getString(11));
                    fornecedor.setInscEstadual(resultSet.getString(12));
                    fornecedor.setInscMunicipal(resultSet.getString(13));
                    fornecedor.setNatureza(resultSet.getString(14));

                    fornecedores.add(fornecedor);
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fornecedores;
    }

    /**
     *
     * @param id
     * @return
     */
    public Fornecedor buscarFornecedor(int id) {
        Fornecedor fornecedor = new Fornecedor();
        try {
            String sql = "SELECT FORNECEDOR.ID, FORNECEDOR.CNPJ, FORNECEDOR.INCLUSAO, FORNECEDOR.ATUALIZACAO, CONTA.NOME, FORNECEDOR.CANCELADO, FORNECEDOR.ATIVO,"
                    + "FORNECEDOR.RAZAOSOCIAL, FORNECEDOR.NOMEFANTASIA, FORNECEDOR.SIGLA, TIPOEMPRESA.NOME, FORNECEDOR.INSCESTADUAL, FORNECEDOR.INSCMUNICIPAL, NATUREZAFORNECEDOR.NOME "
                    + "FROM FORNECEDOR "
                    + "LEFT JOIN NATUREZAFORNECEDOR ON NATUREZAFORNECEDOR.ID = FORNECEDOR.NATUREZA "
                    + "LEFT JOIN TIPOEMPRESA ON TIPOEMPRESA.ID = FORNECEDOR.TIPOEMPRESA "
                    + "LEFT JOIN CONTA ON CONTA.ID = FORNECEDOR.AUTOR "
                    + "WHERE FORNECEDOR.ID = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                fornecedor.setId(resultSet.getInt(1));
                fornecedor.setCnpj(resultSet.getString(2));
                fornecedor.setInclusao(new Date(resultSet.getLong(3)));
                fornecedor.setAtualizacao(new Date(resultSet.getLong(4)));
                fornecedor.setAutor(resultSet.getString(5));
                fornecedor.setCancelado(resultSet.getBoolean(6));
                fornecedor.setAtivo(resultSet.getBoolean(7));
                fornecedor.setRazaoSocial(resultSet.getString(8));
                fornecedor.setNomeFantasia(resultSet.getString(9));
                fornecedor.setSigla(resultSet.getString(10));
                fornecedor.setTipoEmpresa(resultSet.getString(11));
                fornecedor.setInscEstadual(resultSet.getString(12));
                fornecedor.setInscMunicipal(resultSet.getString(13));
                fornecedor.setNatureza(resultSet.getString(14));

            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fornecedor;
    }

    /**
     *
     * @param fornecedor
     * @return
     */
    public int atualizarFornecedor(Fornecedor fornecedor) {
        try {

            String sql = "UPDATE FORNECEDOR SET RAZAOSOCIAL =?,NOMEFANTASIA =?, SIGLA =?,CNPJ =?,INSCESTADUAL =?,INSCMUNICIPAL =?"
                    + " WHERE ID =?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            prepareStatement.setString(1, fornecedor.getRazaoSocial());
            prepareStatement.setString(2, fornecedor.getNomeFantasia());
            prepareStatement.setString(3, fornecedor.getSigla());
            prepareStatement.setString(4, fornecedor.getCnpj());
            prepareStatement.setString(5, fornecedor.getInscEstadual());
            prepareStatement.setString(6, fornecedor.getInscMunicipal());
            prepareStatement.setInt(7, fornecedor.getId());

            return prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     *
     * @param idConta
     * @param campo
     * @param valor
     * @return
     */
    public ArrayList<Fornecedor> buscarFornecedorCampo(int idConta, String campo, String valor) {
        ArrayList<Fornecedor> fornecedores = new ArrayList<>();
        try {
            String sql = "SELECT FORNECEDOR.ID, FORNECEDOR.CNPJ, FORNECEDOR.INCLUSAO, FORNECEDOR.ATUALIZACAO, CONTA.NOME, FORNECEDOR.CANCELADO, FORNECEDOR.ATIVO,"
                    + "FORNECEDOR.RAZAOSOCIAL, FORNECEDOR.NOMEFANTASIA, FORNECEDOR.SIGLA, TIPOEMPRESA.NOME, FORNECEDOR.INSCESTADUAL, FORNECEDOR.INSCMUNICIPAL, NATUREZAFORNECEDOR.NOME "
                    + "FROM FORNECEDOR "
                    + "LEFT JOIN NATUREZAFORNECEDOR ON NATUREZAFORNECEDOR.ID = FORNECEDOR.NATUREZA "
                    + "LEFT JOIN TIPOEMPRESA ON TIPOEMPRESA.ID = FORNECEDOR.TIPOEMPRESA "
                    + "LEFT JOIN CONTA ON CONTA.ID = FORNECEDOR.AUTOR "
                    + "WHERE FORNECEDOR.RAZAOSOCIAL LIKE ? "
                    + "AND FORNECEDOR.NOMEFANTASIA LIKE ? "
                    + "AND FORNECEDOR.CNPJ LIKE ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setString(1, CampoBuscar.FORNECEDORRAZAOSOCIAL.getCampo().equals(campo) ? valor : "%");
            prepareStatement.setString(2, CampoBuscar.FORNECEDORNOMEFANTASIA.getCampo().equals(campo) ? valor : "%");
            prepareStatement.setString(3, CampoBuscar.FORNECEDORCNPJ.getCampo().equals(campo) ? valor : "%");
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    Fornecedor fornecedor = new Fornecedor();

                    fornecedor.setId(resultSet.getInt(1));
                    fornecedor.setCnpj(resultSet.getString(2));
                    fornecedor.setInclusao(new Date(resultSet.getLong(3)));
                    fornecedor.setAtualizacao(new Date(resultSet.getLong(4)));
                    fornecedor.setAutor(resultSet.getString(5));
                    fornecedor.setCancelado(resultSet.getBoolean(6));
                    fornecedor.setAtivo(resultSet.getBoolean(7));
                    fornecedor.setRazaoSocial(resultSet.getString(8));
                    fornecedor.setNomeFantasia(resultSet.getString(9));
                    fornecedor.setSigla(resultSet.getString(10));
                    fornecedor.setTipoEmpresa(resultSet.getString(11));
                    fornecedor.setInscEstadual(resultSet.getString(12));
                    fornecedor.setInscMunicipal(resultSet.getString(13));
                    fornecedor.setNatureza(resultSet.getString(14));

                    fornecedores.add(fornecedor);
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        atualizarHistoricoPesquisa(idConta, "FORNECEDOR", campo, valor);
        return fornecedores;
    }

    /**
     *
     * @param autor
     * @return
     */
    public int incluirFornecedor(int autor) {

        try {
            String sql = "INSERT INTO ROOT.FORNECEDOR (INCLUSAO,ATUALIZACAO, AUTOR, CANCELADO, ATIVO) "
                    + "VALUES ( ?, ?, ?, ?, ?)";
            PreparedStatement prepareStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setLong(1, new Date(new java.util.Date().getTime()).getTime());
            prepareStatement.setLong(2, new Date(new java.util.Date().getTime()).getTime());
            prepareStatement.setInt(3, autor);
            prepareStatement.setBoolean(4, false);
            prepareStatement.setBoolean(5, true);
            prepareStatement.executeUpdate();
            ResultSet result = prepareStatement.getGeneratedKeys();
            if (result.next()) {
                int id = result.getInt(1);
                salvarEvento(id, autor, "incluiu", "FORNECEDOR", "");
                return id;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     *
     * @return
     */
    public ArrayList<Contrato> buscarContratoProblema() {
        ArrayList<Contrato> contratos = new ArrayList<>();
        try {
            String sql = "SELECT ID,NUMERO,FORNECEDOR,DESCRICAO,VALORTOTAL,GASTOTOTAL,SALDOTOTAL,OBSERVACAO,CONTRATO,IDFORNECEDOR FROM CONTRATO WHERE IDFORNECEDOR IS NULL AND CANCELADO = FALSE";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {
                do {
                    Contrato contrato = new Contrato();
                    contrato.setId(resultSet.getInt("ID"));
                    contrato.setNumero(resultSet.getString("NUMERO"));
                    contrato.setFornecedor(resultSet.getString("FORNECEDOR"));
                    contrato.setDescricao(resultSet.getString("DESCRICAO"));
                    contrato.setValorTotal(resultSet.getLong("VALORTOTAL"));
                    contrato.setGastoTotal(resultSet.getLong("GASTOTOTAL"));
                    contrato.setSaldoTotal(resultSet.getLong("SALDOTOTAL"));
                    contrato.setObservacao(resultSet.getString("OBSERVACAO"));
                    contrato.setContrato(resultSet.getString("CONTRATO"));
                    contrato.setIdFornecedor(resultSet.getInt("IDFORNECEDOR"));
                    contratos.add(contrato);
                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contratos;
    }

    /**
     *
     * @return
     */
    public ArrayList<Nota> buscarNotaProblema() {
        ArrayList<Nota> ids = new ArrayList<>();
        try {

            String sql = "SELECT ID,NUMERO,SERIE,FORNECEDOR,DESCRICAO,GASTOTOTAL,VENCIMENTO,VENCIMENTOREAL,TIPO,PRENOTA,CLASSIFICADA,FATURAMENTODIRETO,LANCADA,"
                    + "OBSERVACAO FROM NOTA WHERE FATURAMENTODIRETO <> '' ORDER BY FORNECEDOR ASC";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    Nota nota = new Nota();
                    nota.setId(resultSet.getInt("ID"));
                    nota.setNumero(resultSet.getString("NUMERO"));
                    nota.setSerie(resultSet.getString("SERIE"));
                    nota.setTipo(resultSet.getString("TIPO"));
                    nota.setFornecedor(resultSet.getString("FORNECEDOR"));
                    nota.setDescricao(resultSet.getString("DESCRICAO"));
                    nota.setGastoTotal(resultSet.getLong("GASTOTOTAL"));
                    nota.setObservacao(resultSet.getString("OBSERVACAO"));
                    nota.setVencimento(resultSet.getDate("VENCIMENTO"));
                    nota.setVencimentoReal(resultSet.getDate("VENCIMENTOREAL"));
                    nota.setPreNota(resultSet.getBoolean("PRENOTA"));
                    nota.setClassificada(resultSet.getBoolean("CLASSIFICADA"));
                    nota.setLancada(resultSet.getBoolean("LANCADA"));
                    nota.setFaturamentoDireto(resultSet.getString("FATURAMENTODIRETO"));
                    ids.add(nota);
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ids;
    }

    /**
     *
     * @param id
     * @param nivel
     * @return
     */
    public ArrayList<Conta> listarContas(int id, int nivel) {
        ArrayList<Conta> contas = new ArrayList<>();
        if (nivel < 10 || buscarConta(id).getNivel() < 10) {
            return contas;
        }
        try {
            String sql = "SELECT ID,NOME,NIVEL,CATEGORIA,BLOCK,COMPRA,ALMOXARIFE,ADMINISTRACAO,ENGENHARIA,FINANCEIRO,GERENCIA,"
                    + "ULTIMOLOGIN,EMAIL,LOGADO,EXPIRA,LOGIN FROM ROOT.CONTA";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {
                do {
                    contas.add(new Conta(resultSet.getInt("ID"), resultSet.getInt("NIVEL"), resultSet.getString("NOME"), resultSet.getString("LOGIN"), resultSet.getString("CATEGORIA"), "",
                            resultSet.getString("EMAIL"), resultSet.getBoolean("BLOCK"), resultSet.getBoolean("COMPRA"), resultSet.getBoolean("ALMOXARIFE"),
                            resultSet.getBoolean("ADMINISTRACAO"), resultSet.getBoolean("ENGENHARIA"), resultSet.getBoolean("FINANCEIRO"),
                            resultSet.getBoolean("GERENCIA"), resultSet.getBoolean("LOGADO"), resultSet.getLong("ULTIMOLOGIN"), resultSet.getLong("EXPIRA")));

                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return contas;
    }

    /**
     *
     * @param conta
     * @param usuario
     * @param nivel
     * @return
     */
    public int atualizarConta(Conta conta, int usuario, int nivel) {
        if (nivel < 10 || buscarConta(usuario).getNivel() < 10) {
            return -1;
        }
        try {
            String sql = "UPDATE ROOT.CONTA SET NOME = ?,NIVEL = ?,CATEGORIA = ?,BLOCK = ?,COMPRA = ?,ALMOXARIFE = ?,ADMINISTRACAO = ?,ENGENHARIA = ?,FINANCEIRO = ?,GERENCIA = ?,"
                    + "EMAIL = ?,LOGIN=? WHERE ID = ?";

            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            prepareStatement.setString(1, conta.getNome());
            prepareStatement.setInt(2, conta.getNivel());
            prepareStatement.setString(3, conta.getCategoria());
            prepareStatement.setBoolean(4, conta.isBlock());
            prepareStatement.setBoolean(5, conta.isCompra());
            prepareStatement.setBoolean(6, conta.isAlmoxarife());
            prepareStatement.setBoolean(7, conta.isAdministracao());
            prepareStatement.setBoolean(8, conta.isEngenharia());
            prepareStatement.setBoolean(9, conta.isFinanceiro());
            prepareStatement.setBoolean(10, conta.isGerencia());
            prepareStatement.setString(11, conta.getEmail());
            prepareStatement.setString(12, conta.getLogin());
            prepareStatement.setInt(13, conta.getId());
            salvarEvento(conta.getId(), usuario, "atualizou conta", "CONTA", "");
            return prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     *
     * @param usuario
     * @param nivel
     * @return
     */
    public Conta incluirConta(int usuario, int nivel) {
        Conta conta = new Conta();
        if (nivel < 10 || buscarConta(usuario).getNivel() < 10) {
            return conta;
        }
        try {
            String sql = "INSERT INTO ROOT.CONTA (NOME, LOGIN, NIVEL, CATEGORIA, BLOCK, SENHA, EMAIL, COMPRA, ALMOXARIFE, ADMINISTRACAO, ENGENHARIA, FINANCEIRO, GERENCIA, LOGADO,"
                    + " ULTIMOLOGIN, EXPIRA) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prepareStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            prepareStatement.setInt(1, conta.getId());
            prepareStatement.setString(2, conta.getNome());
            prepareStatement.setString(3, conta.getLogin());
            prepareStatement.setInt(4, conta.getNivel());
            prepareStatement.setString(5, conta.getCategoria());
            prepareStatement.setBoolean(6, conta.isBlock());
            prepareStatement.setString(7, conta.getSenha());
            prepareStatement.setString(8, conta.getEmail());
            prepareStatement.setBoolean(9, conta.isCompra());
            prepareStatement.setBoolean(10, conta.isAlmoxarife());
            prepareStatement.setBoolean(11, conta.isAdministracao());
            prepareStatement.setBoolean(12, conta.isEngenharia());
            prepareStatement.setBoolean(13, conta.isFinanceiro());
            prepareStatement.setBoolean(14, conta.isGerencia());
            prepareStatement.setBoolean(15, conta.isLogado());
            prepareStatement.setLong(16, conta.getUltimoLogin());
            prepareStatement.setLong(17, conta.getExpira());

            prepareStatement.executeUpdate();
            salvarEvento(conta.getId(), usuario, "Incluiu conta", "CONTA", "");
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conta;
    }

    /**
     *
     * @param id
     * @param senha
     * @param usuario
     * @param nivel
     * @return
     */
    public int atualizarSenhaConta(int id, String senha, int usuario, int nivel) {
        if (nivel < 10 || buscarConta(usuario).getNivel() < 10) {
            return -1;
        }
        try {
            String sql = "UPDATE ROOT.CONTA SET SENHA = ? WHERE ID = ?";

            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            prepareStatement.setString(1, senha);
            prepareStatement.setInt(2, id);
            salvarEvento(id, usuario, "Alterou senha", "CONTA", "");
            return prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     *
     * @return
     */
    public ArrayList<ItemOrcamento> listarItensOrcamento() {
        ArrayList<ItemOrcamento> itens = new ArrayList<>();
        try {

            String sql = "SELECT ID, ITEM, PLANOCONTA, DESCRICAO, UNIDADE, QUANTIDADETOTAL, PRECOUNITARIO, PRECOTOTAL FROM ITEMORCAMENTO ORDER BY ITEM ASC";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    ItemOrcamento itemOrcamento = new ItemOrcamento();
                    itemOrcamento.setId(resultSet.getInt("ID"));
                    itemOrcamento.setItem(resultSet.getString("ITEM"));
                    itemOrcamento.setPlanoConta(resultSet.getString("PLANOCONTA"));
                    itemOrcamento.setDescricao(resultSet.getString("DESCRICAO"));
                    itemOrcamento.setUnidade(resultSet.getString("UNIDADE"));
                    itemOrcamento.setQuantidadeTotal(resultSet.getLong("QUANTIDADETOTAL"));
                    itemOrcamento.setPrecoUnitario(resultSet.getLong("PRECOUNITARIO"));
                    itemOrcamento.setPrecoTotal(resultSet.getLong("PRECOTOTAL"));

                    itens.add(itemOrcamento);
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itens;
    }

    /**
     *
     * @param item
     * @return
     */
    public int incluirItemOrcamento(int autor) {
        try {
            String sql = "INSERT INTO ROOT.ITEMORCAMENTO (ITEM, PLANOCONTA, DESCRICAO, UNIDADE, QUANTIDADETOTAL, PRECOUNITARIO, PRECOTOTAL) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prepareStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            prepareStatement.setString(2, "");
            prepareStatement.setString(3, "");
            prepareStatement.setString(4, "");
            prepareStatement.setString(5, "");
            prepareStatement.executeUpdate();
            return idInclusao(prepareStatement, autor, "ITEMORCAMENTO");
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     *
     * @param item
     * @return
     */
    public int atualizarItemOrcamento(ItemOrcamento item) {
        try {
            //UPDATE MAPAS SET CODIGO = ?, EMISSAO = ?, RESPONSAVEL = ?, AUTOR = ?, FORNECEDOR = ?, DESCRICAO = ?, TIPO = ?, CUSTO = ?, ASSINATURA = ?, OBSERVACAO = ?, ITENS = ?, OBSERVACAOASSINATURA = ?, SISTEMA = ?, FISICO = ?, ENCERRAMENTO = ?, REAJUSTE = ? WHERE ID = ?";

            String sql = "UPDATE ITEMORCAMENTO SET ITEM = ?, PLANOCONTA = ?, DESCRICAO = ?, UNIDADE = ?, QUANTIDADETOTAL = ?, PRECOUNITARIO = ?, PRECOTOTAL = ? WHERE ID = ? ";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            prepareStatement.setString(1, item.getItem());
            prepareStatement.setString(2, item.getPlanoConta());
            prepareStatement.setString(3, item.getDescricao());
            prepareStatement.setString(4, item.getUnidade());
            prepareStatement.setLong(5, item.getQuantidadeTotal());
            prepareStatement.setLong(6, item.getPrecoUnitario());
            prepareStatement.setLong(7, item.getPrecoTotal());
            prepareStatement.setInt(8, item.getId());
            return prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    /**
     *
     * @param idEvento
     * @param idUsuario
     * @param evento
     * @param tabela
     * @param campo
     */
    public void salvarEvento(int idEvento, int idUsuario, String evento, String tabela, String campo) {
        try {
            String sql = "INSERT INTO ROOT.EVENTO (IDEVENTO, IDUSUARIO, EVENTO, HORA, TABELA, CAMPO) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            prepareStatement.setInt(1, idEvento);
            prepareStatement.setInt(2, idUsuario);
            prepareStatement.setString(3, evento);
            prepareStatement.setLong(4, new java.util.Date().getTime());
            prepareStatement.setString(5, tabela);
            prepareStatement.setString(6, campo);
            prepareStatement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param status
     */
    public void incluirPmWeb(Status status) {
        if (status.getId() == 0) {
            return;
        }
        try {
            String sql = "INSERT INTO ROOT.PMWEB (IDPMWEB, STATUS) "
                    + "VALUES (?, ?)";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(2, status.getId());
            prepareStatement.setString(3, status.getStatus());
            prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public Status buscarPmWeb(int id) {
        Status status = new Status();
        if (id == 0) {
            return status;
        }
        try {
            String sql = "SELECT ID, IDPMWEB, STATUS FROM PMWEB WHERE IDPMWEB = ?";
            PreparedStatement prepara = connection.prepareStatement(sql);
            prepara.setInt(1, id);
            ResultSet result = prepara.executeQuery();
            if (result.next()) {
                status.setId(result.getInt("IDPMWEB"));
                status.setStatus(result.getString("STATUS"));
            } else {
                status.setStatus("Submitted");
                status.setId(0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    /**
     *
     * @param status
     */
    public void atualizarPmWeb(Status status) {

        try {
            String sql = "UPDATE PMWEB SET STATUS = ? WHERE IDPMWEB = ?";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setString(1, status.getStatus());
            prepare.setInt(2, status.getId());
            prepare.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param tabela
     * @param id
     * @return
     */
    public ArrayList<Historico> buscarHistorico(String tabela, int id) {
        ArrayList<Historico> historicos = new ArrayList<>();
        try {
            String sql = "SELECT a.ID, a.IDEVENTO, a.IDUSUARIO, a.EVENTO, a.HORA,CONTA.NOME,a.CAMPO "
                    + "FROM EVENTO a LEFT JOIN CONTA ON a.IDUSUARIO = CONTA.ID WHERE a.IDEVENTO =? AND a.TABELA = ? ORDER BY a.HORA DESC";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            prepareStatement.setString(2, tabela);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    Historico historico = new Historico();
                    historico.setIdEvento(resultSet.getInt("ID"));
                    historico.setId(resultSet.getInt("IDEVENTO"));
                    historico.setIdConta(resultSet.getInt("IDUSUARIO"));
                    historico.setEvento(resultSet.getString("EVENTO") + " - " + resultSet.getString("CAMPO"));
                    historico.setHora(resultSet.getLong("HORA"));
                    historico.setUsuario(resultSet.getString("NOME"));

                    historicos.add(historico);
                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return historicos;
    }

    /**
     *
     * @param id
     * @return
     */
    public ArrayList<Nota> buscarNotasFornecedor(int id) {

        ArrayList<Nota> notas = new ArrayList<>();
        try {

            String sql = "SELECT NOTA.ID,NOTA.NUMERO,NOTA.SERIE,FORNECEDOR.RAZAOSOCIAL,FORNECEDOR.CNPJ,NOTA.DESCRICAO,NOTA.GASTOTOTAL,NOTA.EMISSAO,NOTA.VENCIMENTO,"
                    + "NOTA.VENCIMENTOREAL,NOTA.TIPO,NOTA.PRENOTA,NOTA.CLASSIFICADA,NOTA.LANCADA,NOTA.CANCELADO,NOTA.OBSERVACAO"
                    + " FROM NOTA LEFT JOIN FORNECEDOR ON NOTA.IDFORNECEDOR = FORNECEDOR.ID WHERE NOTA.IDFORNECEDOR = ? ORDER BY NOTA.EMISSAO DESC";

            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {
                do {
                    Nota nota = new Nota();

                    nota.setId(resultSet.getInt("ID"));
                    nota.setNumero(resultSet.getString("NUMERO"));
                    nota.setSerie(resultSet.getString("SERIE"));
                    nota.setTipo(resultSet.getString("TIPO"));
                    nota.setFornecedor(resultSet.getString("RAZAOSOCIAL") + " " + resultSet.getString("CNPJ"));
                    nota.setDescricao(resultSet.getString("DESCRICAO"));
                    nota.setGastoTotal(resultSet.getLong("GASTOTOTAL"));
                    nota.setObservacao(resultSet.getString("OBSERVACAO"));
                    nota.setEmissao(resultSet.getDate("EMISSAO"));
                    nota.setVencimento(resultSet.getDate("VENCIMENTO"));
                    nota.setVencimentoReal(resultSet.getDate("VENCIMENTOREAL"));
                    nota.setPreNota(resultSet.getBoolean("PRENOTA"));
                    nota.setClassificada(resultSet.getBoolean("CLASSIFICADA"));
                    nota.setLancada(resultSet.getBoolean("LANCADA"));
                    nota.setCancelado(resultSet.getBoolean("CANCELADO"));

                    notas.add(nota);
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    private HashMap<String, ItemContrato> listarItensContrato(int id) {
        HashMap<String, ItemContrato> itens = new HashMap<>();
        Contrato contrato = buscarContrato(id);
        contrato.getMapas().forEach(mapa -> {
            mapa.getItens().forEach(item -> {
                if (itens.containsKey(item.getItem())) {
                    ItemContrato temp = itens.get(item.getItem());
                    temp.setValor(temp.getValor() + item.getValor());
                } else {
                    itens.put(item.getItem(), item);
                }
            });
        });

        return itens;
    }


    /**
     *
     * @param nota
     * @param caminho
     * @param tipo
     * @param nome
     * @param usuario
     * @return
     */
    public int incluirAnexoNota(int nota, String caminho, String tipo, String nome, int usuario) {
        try {
            String sql = "INSERT INTO ROOT.ANEXONOTA (CAMINHO, TIPO, IDNOTA, INCLUIDO,NOME) "
                    + "VALUES (?, ?, ?, ?,?)";
            PreparedStatement prepareStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            prepareStatement.setString(1, caminho);
            prepareStatement.setString(2, tipo);
            prepareStatement.setInt(3, nota);
            prepareStatement.setLong(4, new java.util.Date().getTime());
            prepareStatement.setString(5, nome);
            prepareStatement.executeUpdate();
            return idInclusao(prepareStatement, usuario, "ANEXONOTA");

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     *
     * @param id
     * @return
     */
    public ArrayList<AnexoNota> listarAnexosNota(int id) {
        ArrayList<AnexoNota> anexos = new ArrayList<>();
        try {

            String sql = "SELECT ID,CAMINHO,TIPO,NOME FROM ANEXONOTA WHERE IDNOTA = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    AnexoNota anexo = new AnexoNota();

                    anexo.setId(resultSet.getInt("ID"));
                    anexo.setCaminho(resultSet.getString("CAMINHO"));
                    anexo.setTipo(resultSet.getString("TIPO"));
                    anexo.setNome(resultSet.getString("NOME"));

                    anexos.add(anexo);
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return anexos;
    }

    /**
     *
     * @param id
     * @return
     */
    public File buscarAnexoNota(int id) {
        File temp = null;
        try {
            String sql = "SELECT CAMINHO FROM ANEXONOTA WHERE ID = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                temp = new File(Referencia.getInstancia().getArquivos() + resultSet.getString("CAMINHO"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return temp;
    }

    /**
     *
     * @param id
     * @param liberado
     * @return
     */
    public int incluirPagamento(int usuario, int id, boolean liberado) {
        try {
            String sql = "INSERT INTO ROOT.PAGAMENTONOTA (VENCIMENTO, VENCIMENTOREAL, VALOR, TIPO, LINHA, CODIGO, IDNOTA, LIBERADO, BAIXADA, DESCONTO, VALORLIQUIDO, JUROS, IDDIARIO) "
                    + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prepareStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            prepareStatement.setDate(1, new Date(new java.util.Date().getTime()));
            prepareStatement.setDate(2, new Date(new java.util.Date().getTime()));
            prepareStatement.setLong(3, 0);
            prepareStatement.setString(4, "");
            prepareStatement.setString(5, "");
            prepareStatement.setString(6, "");
            prepareStatement.setInt(7, id);
            prepareStatement.setBoolean(8, liberado);
            prepareStatement.setBoolean(9, false);
            prepareStatement.setLong(10, 0);
            prepareStatement.setLong(11, 0);
            prepareStatement.setLong(12, 0);
            prepareStatement.setInt(13, 0);
            prepareStatement.executeUpdate();
            return idInclusao(prepareStatement, usuario, "PAGAMENTONOTA");
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     *
     * @param pagamento
     */
    public void atualizarPagamento(Pagamento pagamento) {
        try {
            String sql = "UPDATE PAGAMENTONOTA SET VENCIMENTO = ?,VENCIMENTOREAL = ?, VALOR = ?, TIPO = ?, LINHA = ?, CODIGO = ? WHERE ID = ?";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setDate(1, new Date(pagamento.getVencimento().getTime()));
            prepare.setDate(2, new Date(pagamento.getVencimentoReal().getTime()));
            prepare.setLong(3, pagamento.getValor());
            prepare.setString(4, pagamento.getTipo());
            prepare.setString(5, pagamento.getLinha());
            prepare.setString(6, pagamento.getCodigo());
            prepare.setInt(7, pagamento.getId());
            prepare.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public ArrayList<Nota> buscarNotasIdPMWeb(int id) {
        ArrayList<Nota> notas = new ArrayList<>();
        try {
            String sql = "SELECT NOTA.ID,NOTA.INCLUSAO,NOTA.ATUALIZACAO,NOTA.AUTOR,NOTA.CANCELADO,NOTA.ATIVO,NOTA.NUMERO,NOTA.SERIE,"
                    + "NOTA.TIPO,FORNECEDOR.RAZAOSOCIAL,FORNECEDOR.CNPJ,NOTA.VALORTOTAL,NOTA.GASTOTOTAL,NOTA.SALDOTOTAL,NOTA.EMISSAO,NOTA.VENCIMENTO,"
                    + "NOTA.VENCIMENTOREAL,NOTA.PRENOTA,NOTA.CLASSIFICADA,NOTA.LANCADA,NOTA.MAPAS,NOTA.DESCRICAO,"
                    + "NOTA.OBSERVACAO,NOTA.CONTRATO,NOTA.IDFORNECEDOR,NOTA.IDFATURAMENTODIRETO,NOTA.IDCONTRATO,NOTA.IDPMWEB,"
                    + "(SELECT FORNECEDOR.RAZAOSOCIAL FROM CONTRATO LEFT JOIN FORNECEDOR ON CONTRATO.IDFORNECEDOR = FORNECEDOR.ID WHERE CONTRATO.ID = NOTA.IDCONTRATO) "
                    + "FROM NOTA LEFT JOIN FORNECEDOR ON NOTA.IDFORNECEDOR = FORNECEDOR.ID WHERE NOTA.IDPMWEB = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    Nota nota = new Nota();
                    nota.setId(resultSet.getInt("ID"));
                    nota.setInclusao(resultSet.getDate("INCLUSAO"));
                    nota.setAtualizacao(resultSet.getDate("ATUALIZACAO"));
                    nota.setAutor(resultSet.getString("AUTOR"));
                    nota.setCancelado(resultSet.getBoolean("CANCELADO"));
                    nota.setDescricao(resultSet.getString("DESCRICAO"));
                    nota.setObservacao(resultSet.getString("OBSERVACAO"));
                    nota.setContrato(resultSet.getString("CONTRATO"));
                    nota.setAtivo(resultSet.getBoolean("ATIVO"));
                    nota.setNumero(resultSet.getString("NUMERO"));
                    nota.setSerie(resultSet.getString("SERIE"));
                    nota.setTipo(resultSet.getString("TIPO"));
                    nota.setFornecedor(resultSet.getString("RAZAOSOCIAL") + " " + resultSet.getString("CNPJ"));
                    nota.setValorTotal(resultSet.getLong("VALORTOTAL"));
                    nota.setGastoTotal(resultSet.getLong("GASTOTOTAL"));
                    nota.setSaldoTotal(resultSet.getLong("SALDOTOTAL"));
                    nota.setEmissao(resultSet.getDate("EMISSAO"));
                    nota.setVencimento(resultSet.getDate("VENCIMENTO"));
                    nota.setVencimentoReal(resultSet.getDate("VENCIMENTOREAL"));
                    nota.setPreNota(resultSet.getBoolean("PRENOTA"));
                    nota.setClassificada(resultSet.getBoolean("CLASSIFICADA"));
                    nota.setLancada(resultSet.getBoolean("LANCADA"));
                    nota.setFaturamentoDireto(resultSet.getString(29));
                    nota.setIdFornecedor(resultSet.getInt("IDFORNECEDOR"));
                    nota.setIdFaturamentoDireto(resultSet.getInt("IDFATURAMENTODIRETO"));
                    nota.setIdContrato(resultSet.getInt("IDCONTRATO"));
                    nota.setIdPmWeb(resultSet.getInt("IDPMWEB"));
                    nota.setStatus(buscarPmWeb(nota.getIdPmWeb()).getStatus());

                    nota.setItens(buscarItensNota(nota.getId()));
                    nota.setPagamentos(buscarPagamentosNota(nota.getId()));                    
                    notas.add(nota);

                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    /**
     *
     * @param nota
     * @param usuario
     */
    public void aprovarNota(Nota nota, int usuario) {
        if (nota.isCancelado() || nota.isPreNota() || nota.isLancada()) {
            return;
        }
        Calcular.calcularNota(nota);
        incluirNotaFinanceiro(nota, usuario);
        //incluirNotaContabilidade(nota);
        nota.setLancada(true);
        atualizarNota(nota, usuario);
        salvarEvento(nota.getId(), usuario, "Nota aprovada.", "NOTA", "");
    }

    private void incluirNotaFinanceiro(Nota nota, int usuario) {

        nota.getPagamentos().forEach(s -> {
            try {
                String sql = "UPDATE PAGAMENTONOTA SET LIBERADO = ? WHERE ID = ?";
                PreparedStatement prepareStatement = connection.prepareStatement(sql);
                prepareStatement.setBoolean(1, true);
                prepareStatement.setInt(2, s.getId());
                prepareStatement.executeUpdate();
                s.setVencimento(nota.getVencimento());
                s.setVencimentoReal(nota.getVencimentoReal());
                s.setValor(nota.getValorLiquido());
            } catch (SQLException ex) {
                Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        salvarEvento(nota.getId(), usuario, "Nota inclusa em financeiro.", "NOTA", "");
    }

    /**
     *
     * @param pagamento
     */
    public void removerPagamentoNota(Pagamento pagamento) {
        try {
            String sql = "UPDATE PAGAMENTONOTA SET IDNOTA = ?,LIBERADO = ? WHERE ID = ?";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setInt(1, 0);
            prepare.setBoolean(2, false);
            prepare.setInt(3, pagamento.getId());
            prepare.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return
     */
    public ArrayList<NotaFinanceiro> listarAbertasFinanceiro() {
        ArrayList<NotaFinanceiro> notas = new ArrayList<>();
        try {
            String sql = "select PAGAMENTONOTA.ID,PAGAMENTONOTA.IDNOTA,NOTA.NUMERO,FORNECEDOR.RAZAOSOCIAL,FORNECEDOR.CNPJ,PAGAMENTONOTA.VENCIMENTO,PAGAMENTONOTA.VENCIMENTOREAL,"
                    + "PAGAMENTONOTA.VALOR,PAGAMENTONOTA.DESCONTO,PAGAMENTONOTA.JUROS,PAGAMENTONOTA.VALORLIQUIDO,NOTA.VENCIMENTO as VENCIMENTONOTA,NOTA.VENCIMENTOREAL as FLUXO,"
                    + "PAGAMENTONOTA.TIPO,PAGAMENTONOTA.BAIXADA,PAGAMENTONOTA.LINHA,PAGAMENTONOTA.CODIGO,PAGAMENTONOTA.IDDIARIO,NOTA.CLASSIFICADA "
                    + "from ROOT.PAGAMENTONOTA "
                    + "LEFT JOIN NOTA ON NOTA.ID = PAGAMENTONOTA.IDNOTA "
                    + "LEFT JOIN FORNECEDOR ON FORNECEDOR.ID = NOTA.IDFORNECEDOR "
                    + "WHERE PAGAMENTONOTA.BAIXADA = FALSE AND PAGAMENTONOTA.LIBERADO = TRUE AND PAGAMENTONOTA.IDNOTA <> 0 ORDER BY PAGAMENTONOTA.VENCIMENTOREAL ASC";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    NotaFinanceiro nota = new NotaFinanceiro();
                    nota.setId(resultSet.getInt("ID"));
                    nota.setIdNota(resultSet.getInt("IDNOTA"));
                    nota.setIdDiario(resultSet.getInt("IDDIARIO"));
                    nota.setNumero(resultSet.getString("NUMERO"));
                    nota.setFornecedor(resultSet.getString("RAZAOSOCIAL"));
                    nota.setCnpj(resultSet.getString("CNPJ"));
                    nota.setVencimentoNota(resultSet.getDate("VENCIMENTONOTA"));
                    nota.setFluxo(resultSet.getDate("FLUXO"));
                    nota.setVencimento(resultSet.getDate("VENCIMENTO"));
                    nota.setVencimentoReal(resultSet.getDate("VENCIMENTOREAL"));
                    nota.setValor(resultSet.getLong("VALOR"));
                    nota.setDesconto(resultSet.getLong("DESCONTO"));
                    nota.setJuros(resultSet.getLong("JUROS"));
                    nota.setValorLiquido(resultSet.getLong("VALORLIQUIDO"));
                    nota.setTipo(resultSet.getString("TIPO"));
                    nota.setBaixada(resultSet.getBoolean("BAIXADA"));
                    nota.setLinha(resultSet.getString("LINHA"));
                    nota.setCodigo(resultSet.getString("CODIGO"));
                    nota.setClassificada(resultSet.getBoolean("CLASSIFICADA"));
                    notas.add(nota);
                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return notas;
    }

    /**
     *
     * @return
     */
    public ArrayList<NotaFinanceiro> listarBaixadasFinanceiro() {
        ArrayList<NotaFinanceiro> notas = new ArrayList<>();
        try {
            String sql = "select PAGAMENTONOTA.ID,PAGAMENTONOTA.IDNOTA,NOTA.NUMERO,FORNECEDOR.RAZAOSOCIAL,FORNECEDOR.CNPJ,PAGAMENTONOTA.VENCIMENTO,PAGAMENTONOTA.VENCIMENTOREAL,"
                    + "PAGAMENTONOTA.VALOR,PAGAMENTONOTA.DESCONTO,PAGAMENTONOTA.JUROS,PAGAMENTONOTA.VALORLIQUIDO,"
                    + "PAGAMENTONOTA.TIPO,PAGAMENTONOTA.BAIXADA,PAGAMENTONOTA.LINHA,PAGAMENTONOTA.CODIGO,PAGAMENTONOTA.IDDIARIO,NOTA.CLASSIFICADA "
                    + "from ROOT.PAGAMENTONOTA "
                    + "LEFT JOIN NOTA ON NOTA.ID = PAGAMENTONOTA.IDNOTA "
                    + "LEFT JOIN FORNECEDOR ON FORNECEDOR.ID = NOTA.IDFORNECEDOR "
                    + "WHERE PAGAMENTONOTA.BAIXADA = TRUE AND PAGAMENTONOTA.IDDIARIO <> 0 AND PAGAMENTONOTA.IDNOTA <> 0 ORDER BY VENCIMENTO ASC";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    NotaFinanceiro nota = new NotaFinanceiro();
                    nota.setId(resultSet.getInt("ID"));
                    nota.setIdNota(resultSet.getInt("IDNOTA"));
                    nota.setNumero(resultSet.getString("NUMERO"));
                    nota.setFornecedor(resultSet.getString("RAZAOSOCIAL"));
                    nota.setCnpj(resultSet.getString("CNPJ"));
                    nota.setVencimento(resultSet.getDate("VENCIMENTO"));
                    nota.setVencimentoReal(resultSet.getDate("VENCIMENTOREAL"));
                    nota.setValor(resultSet.getLong("VALOR"));
                    nota.setDesconto(resultSet.getLong("DESCONTO"));
                    nota.setJuros(resultSet.getLong("JUROS"));
                    nota.setValorLiquido(resultSet.getLong("VALORLIQUIDO"));
                    nota.setTipo(resultSet.getString("TIPO"));
                    nota.setBaixada(resultSet.getBoolean("BAIXADA"));
                    nota.setLinha(resultSet.getString("LINHA"));
                    nota.setCodigo(resultSet.getString("CODIGO"));
                    nota.setIdDiario(resultSet.getInt("IDDIARIO"));
                    nota.setClassificada(resultSet.getBoolean("CLASSIFICADA"));
                    notas.add(nota);
                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    /**
     *
     * @param status
     * @return
     */
    public ArrayList<Diario> listarDiarios(String status) {
        ArrayList<Diario> diarios = new ArrayList<>();
        try {
            String sql = "SELECT ID, PAGAMENTO, APROVADO,TOTAL FROM DIARIONOTAS "
                    + "WHERE APROVADO = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setBoolean(1, status.equals("Postadas"));
            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {
                do {
                    Diario diario = new Diario();
                    diario.setId(resultSet.getInt("ID"));
                    diario.setPagamento(resultSet.getDate("PAGAMENTO"));
                    diario.setAprovado(resultSet.getBoolean("APROVADO"));
                    diario.setTotal(resultSet.getLong("TOTAL"));
                    diarios.add(diario);
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return diarios;
    }

    /**
     *
     * @param pagamento
     * @return
     */
    public int incluirDiario(Date pagamento) {
        try {
            String sql = "INSERT INTO ROOT.DIARIONOTAS (PAGAMENTO) "
                    + "VALUES (?)";

            PreparedStatement prepareStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setDate(1, new Date(pagamento.getTime()));
            System.out.println("Execute: " + prepareStatement.executeUpdate());
            ResultSet result = prepareStatement.getGeneratedKeys();
            if (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     *
     * @param idNotaFinanceiro
     * @param idDiario
     */
    public void incluirNotaEmDiario(int idNotaFinanceiro, int idDiario) {
        try {
            String sql = "UPDATE PAGAMENTONOTA SET IDDIARIO = ? WHERE ID = ?";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setInt(1, idDiario);
            prepare.setInt(2, idNotaFinanceiro);
            prepare.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public Diario buscarDiario(int id) {
        Diario diario = new Diario();
        try {
            String sql = "SELECT ID, PAGAMENTO, APROVADO,TOTAL FROM DIARIONOTAS "
                    + "WHERE ID = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {
                diario.setId(resultSet.getInt("ID"));
                diario.setPagamento(resultSet.getDate("PAGAMENTO"));
                diario.setAprovado(resultSet.getBoolean("APROVADO"));
                diario.setTotal(resultSet.getLong("TOTAL"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return diario;
    }

    /**
     *
     * @param id
     * @return
     */
    public ArrayList<NotaFinanceiro> buscarNotasFinanceiro(int id) {
        ArrayList<NotaFinanceiro> notas = new ArrayList<>();
        try {
            String sql = "select PAGAMENTONOTA.ID,PAGAMENTONOTA.IDNOTA,NOTA.NUMERO,FORNECEDOR.RAZAOSOCIAL,FORNECEDOR.CNPJ,PAGAMENTONOTA.VENCIMENTO ,PAGAMENTONOTA.VENCIMENTOREAL,"
                    + "PAGAMENTONOTA.VALOR,PAGAMENTONOTA.DESCONTO,PAGAMENTONOTA.JUROS,PAGAMENTONOTA.VALORLIQUIDO,NOTA.VENCIMENTO as VENCIMENTONOTA,NOTA.VENCIMENTOREAL as FLUXO,"
                    + "PAGAMENTONOTA.TIPO,PAGAMENTONOTA.BAIXADA,PAGAMENTONOTA.LINHA,PAGAMENTONOTA.CODIGO,NOTA.CLASSIFICADA "
                    + "from ROOT.PAGAMENTONOTA "
                    + "LEFT JOIN NOTA ON NOTA.ID = PAGAMENTONOTA.IDNOTA "
                    + "LEFT JOIN FORNECEDOR ON FORNECEDOR.ID = NOTA.IDFORNECEDOR "
                    + "WHERE PAGAMENTONOTA.IDDIARIO = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    NotaFinanceiro nota = new NotaFinanceiro();

                    nota.setId(resultSet.getInt("ID"));
                    nota.setIdNota(resultSet.getInt("IDNOTA"));
                    nota.setNumero(resultSet.getString("NUMERO"));
                    nota.setFornecedor(resultSet.getString("RAZAOSOCIAL"));
                    nota.setCnpj(resultSet.getString("CNPJ"));
                    nota.setVencimento(resultSet.getDate("VENCIMENTO"));
                    nota.setVencimentoNota(resultSet.getDate("VENCIMENTONOTA"));
                    nota.setFluxo(resultSet.getDate("FLUXO"));
                    nota.setVencimentoReal(resultSet.getDate("VENCIMENTOREAL"));
                    nota.setValor(resultSet.getLong("VALOR"));
                    nota.setDesconto(resultSet.getLong("DESCONTO"));
                    nota.setJuros(resultSet.getLong("JUROS"));
                    nota.setValorLiquido(resultSet.getLong("VALORLIQUIDO"));
                    nota.setTipo(resultSet.getString("TIPO"));
                    nota.setBaixada(resultSet.getBoolean("BAIXADA"));
                    nota.setLinha(resultSet.getString("LINHA"));
                    nota.setCodigo(resultSet.getString("CODIGO"));
                    nota.setClassificada(resultSet.getBoolean("CLASSIFICADA"));

                    notas.add(nota);
                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return notas;
    }

    /**
     *
     * @param idNotaFinanceiro
     * @param idDiario
     */
    public void excluirNotaEmDiario(int idNotaFinanceiro, int idDiario) {
        try {
            String sql = "UPDATE PAGAMENTONOTA SET IDDIARIO = ?,BAIXADA = ? WHERE ID = ?";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setInt(1, 0);
            prepare.setBoolean(2, false);
            prepare.setInt(3, idNotaFinanceiro);
            prepare.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param id
     * @param valor
     */
    public void atualizarDescontoNotaFinanceiro(int id, long valor) {
        try {
            String sql = "UPDATE PAGAMENTONOTA SET DESCONTO = ? WHERE ID = ?";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setLong(1, valor);
            prepare.setInt(2, id);
            prepare.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param id
     * @param juros
     */
    public void atualizarJurosNotaFinanceiro(int id, long juros) {
        try {
            String sql = "UPDATE PAGAMENTONOTA SET JUROS = ? WHERE ID = ?";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setLong(1, juros);
            prepare.setInt(2, id);
            prepare.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param idNota
     * @param tipo
     */
    public void atualizarTipoNotaFinanceiro(int idNota, String tipo) {
        try {
            String sql = "UPDATE PAGAMENTONOTA SET TIPO = ? WHERE ID = ?";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setString(1, tipo);
            prepare.setInt(2, idNota);
            prepare.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param idNota
     * @param linha
     */
    public void atualizarLinhaNotaFinanceiro(int idNota, String linha) {
        try {
            String sql = "UPDATE PAGAMENTONOTA SET LINHA = ? WHERE ID = ?";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setString(1, linha);
            prepare.setInt(2, idNota);
            prepare.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param idNota
     * @param codigo
     */
    public void atualizarCodigoNotaFinanceiro(int idNota, String codigo) {
        try {
            String sql = "UPDATE PAGAMENTONOTA SET CODIGO = ? WHERE ID = ?";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setString(1, codigo);
            prepare.setInt(2, idNota);
            prepare.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param id
     * @param valor
     */
    public void atualizarValorLiquidoNotaFinanceiro(int id, long valor) {
        try {
            String sql = "UPDATE PAGAMENTONOTA SET VALORLIQUIDO = ? WHERE ID = ?";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setLong(1, valor);
            prepare.setInt(2, id);
            prepare.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int atualizarValorNotaFinanceiro(int id, long valor) {
        try {
            String sql = "UPDATE PAGAMENTONOTA SET VALOR = ?, VALORLIQUIDO = ?,JUROS = ?,DESCONTO = ? WHERE ID = ?";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setLong(1, valor);
            prepare.setLong(2, valor);
            prepare.setLong(3, 0);
            prepare.setLong(4, 0);
            prepare.setInt(5, id);
            return prepare.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -2;
    }

    /**
     *
     * @param diario
     */
    public void salvarDiario(Diario diario) {
        try {
            String sql = "UPDATE DIARIONOTAS SET PAGAMENTO = ?, APROVADO = ?,TOTAL = ? WHERE ID = ?";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setDate(1, new Date(diario.getPagamento().getTime()));
            prepare.setBoolean(2, diario.isAprovado());
            prepare.setLong(3, diario.getTotal());
            prepare.setInt(4, diario.getId());
            prepare.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param idDiario
     * @return
     */
    public int aprovarDiario(int idDiario) {
        try {
            String sql = "UPDATE DIARIONOTAS SET APROVADO = ? WHERE ID = ?";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setBoolean(1, true);
            prepare.setInt(2, idDiario);
            return prepare.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    /**
     *
     * @param idDiario
     * @param pagamento
     * @return
     */
    public int baixarNotasFinanceiro(int idDiario, Date pagamento) {
        try {
            String sql = "UPDATE PAGAMENTONOTA SET VENCIMENTOREAL = ? ,BAIXADA = ? WHERE IDDIARIO = ?";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setDate(1, pagamento);
            prepare.setBoolean(2, true);
            prepare.setInt(3, idDiario);
            return prepare.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    /**
     *
     * @param id
     * @return
     */
    public long buscarValorLiquidoNota(int id) {
        try {
            String sql = "SELECT VALORLIQUIDO FROM NOTA WHERE ID = ?";

            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("VALORLIQUIDO");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private void atualizarValorLiquidoNota(int id, long valor) {
        try {
            String sql = "UPDATE ROOT.NOTA SET VALORLIQUIDO =? WHERE ID =?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setLong(1, valor);
            prepareStatement.setInt(2, id);
            prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    /**
     *
     * @param id
     * @return
     */
    public ArrayList<NotaFinanceiro> listarNotasDividir(int id) {
        ArrayList<NotaFinanceiro> notas = new ArrayList<>();
        try {
            String sql = "select PAGAMENTONOTA.ID,PAGAMENTONOTA.IDNOTA,PAGAMENTONOTA.IDDIARIO,NOTA.NUMERO,FORNECEDOR.RAZAOSOCIAL,FORNECEDOR.CNPJ,PAGAMENTONOTA.VENCIMENTO,PAGAMENTONOTA.VENCIMENTOREAL,"
                    + "PAGAMENTONOTA.VALOR,PAGAMENTONOTA.DESCONTO,PAGAMENTONOTA.JUROS,PAGAMENTONOTA.VALORLIQUIDO,"
                    + "PAGAMENTONOTA.TIPO,PAGAMENTONOTA.BAIXADA,PAGAMENTONOTA.LINHA,PAGAMENTONOTA.CODIGO "
                    + "from ROOT.PAGAMENTONOTA "
                    + "LEFT JOIN NOTA ON NOTA.ID = PAGAMENTONOTA.IDNOTA "
                    + "LEFT JOIN FORNECEDOR ON FORNECEDOR.ID = NOTA.IDFORNECEDOR "
                    + "WHERE PAGAMENTONOTA.IDNOTA = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    NotaFinanceiro nota = new NotaFinanceiro();

                    nota.setId(resultSet.getInt("ID"));
                    nota.setIdNota(resultSet.getInt("IDNOTA"));
                    nota.setIdDiario(resultSet.getInt("IDDIARIO"));
                    nota.setNumero(resultSet.getString("NUMERO"));
                    nota.setFornecedor(resultSet.getString("RAZAOSOCIAL"));
                    nota.setCnpj(resultSet.getString("CNPJ"));
                    nota.setVencimento(resultSet.getDate("VENCIMENTO"));
                    nota.setVencimentoReal(resultSet.getDate("VENCIMENTOREAL"));
                    nota.setValor(resultSet.getLong("VALOR"));
                    nota.setDesconto(resultSet.getLong("DESCONTO"));
                    nota.setJuros(resultSet.getLong("JUROS"));
                    nota.setValorLiquido(resultSet.getLong("VALORLIQUIDO"));
                    nota.setTipo(resultSet.getString("TIPO"));
                    nota.setBaixada(resultSet.getBoolean("BAIXADA"));
                    nota.setLinha(resultSet.getString("LINHA"));
                    nota.setCodigo(resultSet.getString("CODIGO"));
                    notas.add(nota);
                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return notas;
    }

    /**
     *
     * @param nota
     * @param valor
     * @return
     */
    public int atualizarValorFinanceiro(int nota, long valor) {
        try {
            /*
             select PAGAMENTONOTA.ID,PAGAMENTONOTA.IDNOTA,PAGAMENTONOTA.VALOR,PAGAMENTONOTA.BAIXADA, NOTA.VALORLIQUIDO
             from ROOT.PAGAMENTONOTA LEFT JOIN NOTA ON NOTA.ID = PAGAMENTONOTA.IDNOTA WHERE PAGAMENTONOTA.IDNOTA = 
             (SELECT PAGAMENTONOTA.IDNOTA FROM PAGAMENTONOTA WHERE PAGAMENTONOTA.ID = ?);
             */
            long valorLiquido = 0, valorAberto = 0, valorBaixado = 0, valorReservado = 0, valorAntigo = 0;
            boolean baixada = true;
            int idDiario = 0;

            String sql = "select PAGAMENTONOTA.ID,PAGAMENTONOTA.IDNOTA,PAGAMENTONOTA.VALOR,PAGAMENTONOTA.BAIXADA, NOTA.VALORLIQUIDO,PAGAMENTONOTA.IDDIARIO "
                    + "from ROOT.PAGAMENTONOTA LEFT JOIN NOTA ON NOTA.ID = PAGAMENTONOTA.IDNOTA WHERE PAGAMENTONOTA.IDNOTA = "
                    + "(SELECT PAGAMENTONOTA.IDNOTA FROM PAGAMENTONOTA WHERE PAGAMENTONOTA.ID = ?)";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, nota);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    valorLiquido = resultSet.getLong("VALORLIQUIDO");
                    if (resultSet.getBoolean("BAIXADA")) {
                        valorBaixado += resultSet.getLong("VALOR");
                    } else {
                        valorReservado += resultSet.getLong("VALOR");
                    }
                    if (resultSet.getInt("ID") == nota) {
                        valorAntigo = resultSet.getLong("VALOR");
                        if (resultSet.getBoolean("BAIXADA")) {
                            return -3;
                        }
                        if (resultSet.getInt("IDDIARIO") != 0) {
                            return -4;
                        }
                    }
                } while (resultSet.next());
            }
            valorAberto = valorLiquido - valorBaixado - valorReservado;
            if (valorAntigo > valor) {
                return atualizarValorNotaFinanceiro(nota, valor);
            }
            if ((valorAntigo + valorAberto) >= valor) {
                return atualizarValorNotaFinanceiro(nota, valor);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -5;
    }

    /**
     *
     * @param id
     * @return
     */
    public ArrayList<Contrato> listarContratosFornecedor(int id) {
        ArrayList<Contrato> contratos = new ArrayList<>();
        try {
            String sql = "SELECT ID,NUMERO,FORNECEDOR,DESCRICAO,VALORTOTAL,GASTOTOTAL,SALDOTOTAL,OBSERVACAO,CONTRATO FROM CONTRATO WHERE IDFORNECEDOR = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {

                do {
                    Contrato contrato = new Contrato();
                    contrato.setId(resultSet.getInt("ID"));
                    contrato.setNumero(resultSet.getString("NUMERO"));
                    contrato.setFornecedor(resultSet.getString("FORNECEDOR"));
                    contrato.setDescricao(resultSet.getString("DESCRICAO"));
                    contrato.setValorTotal(resultSet.getLong("VALORTOTAL"));
                    contrato.setGastoTotal(resultSet.getLong("GASTOTOTAL"));
                    contrato.setSaldoTotal(resultSet.getLong("SALDOTOTAL"));
                    contrato.setObservacao(resultSet.getString("OBSERVACAO"));
                    contrato.setContrato(resultSet.getString("CONTRATO"));
                    contratos.add(contrato);
                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contratos;
    }

    /**
     *
     * @param idFornecedor
     * @param baixada
     * @return
     */
    public ArrayList<NotaFinanceiro> listarPagamentosFornecedor(int idFornecedor, boolean baixada) {
        ArrayList<NotaFinanceiro> notas = new ArrayList<>();
        try {
            String sql = "select PAGAMENTONOTA.ID,PAGAMENTONOTA.IDNOTA,NOTA.NUMERO,FORNECEDOR.RAZAOSOCIAL,FORNECEDOR.CNPJ,PAGAMENTONOTA.VENCIMENTO,PAGAMENTONOTA.VENCIMENTOREAL,"
                    + "PAGAMENTONOTA.VALOR,PAGAMENTONOTA.DESCONTO,PAGAMENTONOTA.JUROS,PAGAMENTONOTA.VALORLIQUIDO,"
                    + "PAGAMENTONOTA.TIPO,PAGAMENTONOTA.BAIXADA,PAGAMENTONOTA.LINHA,PAGAMENTONOTA.CODIGO,PAGAMENTONOTA.IDDIARIO,NOTA.CLASSIFICADA "
                    + "from ROOT.PAGAMENTONOTA "
                    + "LEFT JOIN NOTA ON NOTA.ID = PAGAMENTONOTA.IDNOTA "
                    + "LEFT JOIN FORNECEDOR ON FORNECEDOR.ID = NOTA.IDFORNECEDOR "
                    + "WHERE PAGAMENTONOTA.BAIXADA = ? "
                    + "AND PAGAMENTONOTA.LIBERADO = TRUE "
                    + "AND IDNOTA IN (select ID from NOTA WHERE IDFORNECEDOR = ?) ORDER BY VENCIMENTOREAL ASC";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setBoolean(1, baixada);
            prepareStatement.setInt(2, idFornecedor);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    NotaFinanceiro nota = new NotaFinanceiro();
                    nota.setId(resultSet.getInt("ID"));
                    nota.setIdNota(resultSet.getInt("IDNOTA"));
                    nota.setIdDiario(resultSet.getInt("IDDIARIO"));
                    nota.setNumero(resultSet.getString("NUMERO"));
                    nota.setFornecedor(resultSet.getString("RAZAOSOCIAL"));
                    nota.setCnpj(resultSet.getString("CNPJ"));
                    nota.setVencimento(resultSet.getDate("VENCIMENTO"));
                    nota.setVencimentoReal(resultSet.getDate("VENCIMENTOREAL"));
                    nota.setValor(resultSet.getLong("VALOR"));
                    nota.setDesconto(resultSet.getLong("DESCONTO"));
                    nota.setJuros(resultSet.getLong("JUROS"));
                    nota.setValorLiquido(resultSet.getLong("VALORLIQUIDO"));
                    nota.setTipo(resultSet.getString("TIPO"));
                    nota.setBaixada(resultSet.getBoolean("BAIXADA"));
                    nota.setLinha(resultSet.getString("LINHA"));
                    nota.setCodigo(resultSet.getString("CODIGO"));
                    nota.setClassificada(resultSet.getBoolean("CLASSIFICADA"));
                    notas.add(nota);
                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return notas;
    }

    /**
     *
     * @param idFornecedor
     * @param usuario
     * @return
     */
    public int incluirContatoFornecedor(int idFornecedor, int usuario) {
        try {
            String sql = "INSERT INTO ROOT.CONTATO ( INCLUSAO, ATUALIZACAO, AUTOR, CANCELADO, ATIVO, IDFORNECEDOR) "
                    + "VALUES ( ?, ?, ?, ?, ?, ?)";

            PreparedStatement prepareStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            prepareStatement.setLong(1, new Date(new java.util.Date().getTime()).getTime());
            prepareStatement.setLong(2, new Date(new java.util.Date().getTime()).getTime());
            prepareStatement.setInt(3, usuario);
            prepareStatement.setBoolean(4, false);
            prepareStatement.setBoolean(5, true);
            prepareStatement.setInt(6, idFornecedor);
            prepareStatement.executeUpdate();
            return idInclusao(prepareStatement, usuario, "CONTATO");
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     *
     * @param endereco
     */
    public void incluirEnderecoFornecedor(Endereco endereco) {
        /*
         INSERT INTO ROOT.ENDERECO (ID, INCLUSAO, ATUALIZACAO, AUTOR, CANCELADO, ATIVO, NOME, ENDERECO, CODIGOPOSTAL, RUA, NUMERO, CIDADE, DISTRITO, ESTADO, PAIS, IDFORNECEDOR) 
         VALUES (1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)
         */
        String sql = "INSERT INTO ROOT.ENDERECO (ID, INCLUSAO, ATUALIZACAO, AUTOR, CANCELADO, ATIVO, NOME, ENDERECO, CODIGOPOSTAL, RUA, NUMERO, CIDADE, DISTRITO, ESTADO, PAIS, IDFORNECEDOR) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    /**
     *
     * @param idFornecedor
     * @return
     */
    public ArrayList<Contato> listarContatoFornecedor(int idFornecedor) {
        ArrayList<Contato> contatos = new ArrayList<>();
        try {
            String sql = "SELECT CONTATO.ID, CONTATO.INCLUSAO, CONTATO.ATUALIZACAO, CONTA.NOME, CONTATO.CANCELADO, CONTATO.ATIVO, CONTATO.DESCRICAO, CONTATO.TIPO, CONTATO.NUMERO "
                    + "FROM ROOT.CONTATO LEFT JOIN CONTA ON CONTA.ID = CONTATO.AUTOR "
                    + "WHERE IDFORNECEDOR = ?";

            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, idFornecedor);
            ResultSet result = prepareStatement.executeQuery();

            if (result.next()) {
                do {
                    Contato contato = new Contato();
                    contato.setId(result.getInt(1));
                    contato.setInclusao(new Date(result.getLong(2)));
                    contato.setAtualizacao(new Date(result.getLong(3)));
                    contato.setAutor(result.getString(4));
                    contato.setCancelado(result.getBoolean(5));
                    contato.setAtivo(result.getBoolean(6));
                    contato.setDescricao(result.getString(7));
                    contato.setTipo(result.getString(8));
                    contato.setNumero(result.getString(9));

                    contatos.add(contato);
                } while (result.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contatos;
    }

    /**
     *
     * @param historico
     * @param idConta
     * @param tabela
     */
    public void buscarHistoricoPesquisa(HistoricoPesquisa historico, int idConta, String tabela) {
        try {
            String sql = "SELECT CAMPO, VALOR FROM HISTORICOPESQUISA WHERE IDCONTA = ? AND TABELA = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, idConta);
            prepareStatement.setString(2, tabela);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                historico.setCampo(resultSet.getString("CAMPO"));
                historico.setValor(resultSet.getString("VALOR"));
            } else {
                historico.setCampo(incluirHistoricoPesquisa(idConta, tabela));
                historico.setValor("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param idConta
     * @param tabela
     * @return
     */
    public String incluirHistoricoPesquisa(int idConta, String tabela) {
        String campo = CampoBuscar.INSTANCIA.campoPadrao(tabela);
        try {
            String sql = "INSERT INTO ROOT.HISTORICOPESQUISA (IDCONTA, TABELA, CAMPO, VALOR) "
                    + "VALUES (?, ?, ?, ?)";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, idConta);
            prepareStatement.setString(2, tabela);
            prepareStatement.setString(3, campo);
            prepareStatement.setString(4, "");
            prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return campo;
    }

    /**
     *
     * @param idConta
     * @param tabela
     * @param campo
     * @param valor
     */
    public void atualizarHistoricoPesquisa(int idConta, String tabela, String campo, String valor) {
        try {
            String sql = "UPDATE ROOT.HISTORICOPESQUISA SET CAMPO = ?,VALOR = ? WHERE IDCONTA = ? AND TABELA = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setString(1, campo);
            prepareStatement.setString(2, valor);
            prepareStatement.setInt(3, idConta);
            prepareStatement.setString(4, tabela);
            prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    /**
     *
     * @param idConta
     * @param idNota
     * @return
     */
    public boolean cancelarNota(int idConta, int idNota) {
        try {
            String sql = "SELECT NOTA.PRENOTA,NOTA.LANCADA,NOTA.CANCELADO FROM NOTA WHERE NOTA.ID = ?";

            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, idNota);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                boolean preNota = resultSet.getBoolean("PRENOTA");
                boolean lancada = resultSet.getBoolean("LANCADA");
                boolean cancelado = resultSet.getBoolean("CANCELADO");

                if (preNota && !lancada) {
                    String sql2 = "UPDATE ROOT.NOTA SET CANCELADO = ? WHERE ID =?";

                    PreparedStatement prepare = connection.prepareStatement(sql2);

                    prepare.setBoolean(1, !cancelado);
                    prepare.setInt(2, idNota);

                    salvarEvento(idNota, idConta, "Cancelou", "NOTA", "");
                    prepare.executeUpdate();
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     *
     * @return
     */
    public ArrayList<Diario> listarDiarios() {
        ArrayList<Diario> diarios = new ArrayList<>();
        try {
            String sql = "SELECT ID, PAGAMENTO, APROVADO,TOTAL FROM DIARIONOTAS ORDER BY PAGAMENTO asc";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {
                do {
                    Diario diario = new Diario();
                    diario.setId(resultSet.getInt("ID"));
                    diario.setPagamento(resultSet.getDate("PAGAMENTO"));
                    diario.setAprovado(resultSet.getBoolean("APROVADO"));
                    diario.setTotal(resultSet.getLong("TOTAL"));
                    diarios.add(diario);
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return diarios;
    }

    /**
     *
     * @param status
     * @return
     */
    public ArrayList<Nota> buscarNotasSubmetidas(String status) {
        ArrayList<Nota> notas = new ArrayList<>();
        try {
            String sql = "SELECT NOTA.ID,NOTA.NUMERO,NOTA.SERIE,FORNECEDOR.RAZAOSOCIAL,FORNECEDOR.CNPJ,NOTA.DESCRICAO,NOTA.GASTOTOTAL,NOTA.VENCIMENTO,"
                    + "NOTA.VENCIMENTOREAL,NOTA.TIPO,NOTA.PRENOTA,NOTA.CLASSIFICADA,NOTA.LANCADA,NOTA.OBSERVACAO,NOTA.IDFORNECEDOR,NOTA.IDPMWEB "
                    + "FROM NOTA "
                    + "LEFT JOIN FORNECEDOR ON NOTA.IDFORNECEDOR = FORNECEDOR.ID "
                    + "WHERE NOTA.IDPMWEB IN (SELECT PMWEB.IDPMWEB FROM ROOT.PMWEB WHERE STATUS = ?)";
            PreparedStatement prepara = connection.prepareStatement(sql);
            prepara.setString(1, status);
            ResultSet resultSet = prepara.executeQuery();
            if (resultSet.next()) {
                do {
                    Nota nota = new Nota();

                    nota.setId(resultSet.getInt("ID"));
                    nota.setNumero(resultSet.getString("NUMERO"));
                    nota.setSerie(resultSet.getString("SERIE"));
                    nota.setTipo(resultSet.getString("TIPO"));
                    nota.setFornecedor(resultSet.getString("RAZAOSOCIAL") + " " + resultSet.getString("CNPJ"));
                    nota.setDescricao(resultSet.getString("DESCRICAO"));
                    nota.setGastoTotal(resultSet.getLong("GASTOTOTAL"));
                    nota.setObservacao(resultSet.getString("OBSERVACAO"));
                    nota.setVencimento(resultSet.getDate("VENCIMENTO"));
                    nota.setVencimentoReal(resultSet.getDate("VENCIMENTOREAL"));
                    nota.setPreNota(resultSet.getBoolean("PRENOTA"));
                    nota.setClassificada(resultSet.getBoolean("CLASSIFICADA"));
                    nota.setLancada(resultSet.getBoolean("LANCADA"));
                    nota.setIdPmWeb(resultSet.getInt("IDPMWEB"));
                    nota.setStatus(buscarPmWeb(nota.getIdPmWeb()).getStatus());

                    notas.add(nota);
                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    /**
     *
     * @param parametros
     * @return
     */
    public ArrayList<Diario> listarDiariosPorPagamento(Parametros parametros) {
        ArrayList<Diario> diarios = new ArrayList<>();
        try {
            String sql = "SELECT ID, PAGAMENTO, APROVADO,TOTAL FROM DIARIONOTAS WHERE PAGAMENTO BETWEEN ? AND ? ORDER BY PAGAMENTO asc";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setDate(1, new Date(parametros.getVencimentoDe().getTime()));
            prepareStatement.setDate(2, new Date(parametros.getVencimentoAte().getTime()));
            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {
                do {
                    Diario diario = new Diario();
                    diario.setId(resultSet.getInt("ID"));
                    diario.setPagamento(resultSet.getDate("PAGAMENTO"));
                    diario.setAprovado(resultSet.getBoolean("APROVADO"));
                    diario.setTotal(resultSet.getLong("TOTAL"));
                    diarios.add(diario);
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return diarios;
    }

    public int incluirProdutoAlmoxarife(int autor) {
        try {
            String sql = "INSERT INTO ROOT.PRODUTOESTOQUE (INCLUSAO, ATUALIZACAO, AUTOR, CANCELADO, ATIVO, COMPLEMENTO, DESTINO, LOCALIZACAO, ESTOQUE, CODIGO, DESCRICAO, UNIDADE) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prepareStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setLong(1, new Date(new java.util.Date().getTime()).getTime());
            prepareStatement.setLong(2, new Date(new java.util.Date().getTime()).getTime());
            prepareStatement.setInt(3, autor);
            prepareStatement.setBoolean(4, false);
            prepareStatement.setBoolean(5, true);
            prepareStatement.setString(6, "");
            prepareStatement.setString(7, "");
            prepareStatement.setString(8, "");
            prepareStatement.setLong(9, 0);
            prepareStatement.setString(10, "");
            prepareStatement.setString(11, "");
            prepareStatement.setString(12, "");
            prepareStatement.executeUpdate();
            
            return idInclusao(prepareStatement,autor,"PRODUTOESTOQUE");
            
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private int idInclusao(PreparedStatement prepareStatement, int usuario, String tabela) {
        try {
            ResultSet result = prepareStatement.getGeneratedKeys();
            if (result.next()) {
                int id = result.getInt(1);
                salvarEvento(id, usuario, "Incluiu", tabela, "");
                return id;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int incluirDado(int autor) {
        try {
            String sql = "INSERT INTO ROOT.DADO (INCLUSAO, ATUALIZACAO, AUTOR, CANCELADO, ATIVO) "
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement prepareStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setLong(1, new Date(new java.util.Date().getTime()).getTime());
            prepareStatement.setLong(2, new Date(new java.util.Date().getTime()).getTime());
            prepareStatement.setInt(3, autor);
            prepareStatement.setBoolean(4, false);
            prepareStatement.setBoolean(5, true);
            System.out.println("Execute: " + prepareStatement.executeUpdate());
            ResultSet result = prepareStatement.getGeneratedKeys();
            if (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int incluirProduto(int autor) {
        try {
            int dado = incluirDado(autor);
            if (dado == 0) {
                return 0;
            }
            String sql = "INSERT INTO ROOT.PRODUTO (IDDADO, CODIGO, DESCRICAO, UNIDADE) "
                    + "VALUES (?, ?, ?, ?)";
            PreparedStatement prepareStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setInt(1, dado);
            prepareStatement.setString(2, "");
            prepareStatement.setString(3, "");
            prepareStatement.setString(4, "");
            System.out.println("Execute: " + prepareStatement.executeUpdate());
            ResultSet result = prepareStatement.getGeneratedKeys();
            if (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public ProdutoAlmoxarife buscarProdutoAlmoxarife(int id) {
        ProdutoAlmoxarife produto = new ProdutoAlmoxarife();
        try {
            String sql = "select e.ID,e.INCLUSAO,e.ATUALIZACAO,e.AUTOR,e.CANCELADO,e.ATIVO,e.CODIGO,e.DESCRICAO,e.UNIDADE,e.COMPLEMENTO,e.DESTINO,"
                    + "e.LOCALIZACAO,e.ESTOQUE,(SELECT CONTA.NOME from ROOT.CONTA WHERE e.AUTOR = CONTA.ID) AS USUARIO "
                    + "from ROOT.PRODUTOESTOQUE AS e "
                    + "WHERE e.ID = ?";

            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                produto.setId(resultSet.getInt("ID"));
                produto.setInclusao(new Date(resultSet.getLong("INCLUSAO")));
                produto.setAtualizacao(new Date(resultSet.getLong("ATUALIZACAO")));
                produto.setAutor(resultSet.getString("USUARIO"));
                produto.setCancelado(resultSet.getBoolean("CANCELADO"));
                produto.setAtivo(resultSet.getBoolean("ATIVO"));

                produto.setCodigo(resultSet.getString("CODIGO"));
                produto.setDescricao(resultSet.getString("DESCRICAO"));
                produto.setUnidade(resultSet.getString("UNIDADE"));
                produto.setComplemento(resultSet.getString("COMPLEMENTO"));
                produto.setDestino(resultSet.getString("DESTINO"));
                produto.setLocalizacao(resultSet.getString("LOCALIZACAO"));
                produto.setEstoque(resultSet.getInt("ESTOQUE"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return produto;
    }

    public void buscarProduto(Produto produto, int id) {
        String sql = "select ID,IDDADO,CODIGO,DESCRICAO,UNIDADE from ROOT.PRODUTO WHERE ID = ?";
    }

    public void buscarDado(Dado dado, int id) {
        try {
            String sql = "select ID,INCLUSAO,ATUALIZACAO,AUTOR,CANCELADO,ATIVO,(SELECT CONTA.NOME from ROOT.CONTA WHERE DADO.AUTOR = CONTA.ID) AS USUARIO from ROOT.DADO WHERE ID = ?";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                dado.setId(resultSet.getInt("ID"));
                dado.setInclusao(new Date(resultSet.getLong("INCLUSAO")));
                dado.setAtualizacao(new Date(resultSet.getLong("ATUALIZACAO")));
                dado.setAutor(resultSet.getString("USUARIO"));
                dado.setCancelado(resultSet.getBoolean("CANCELADO"));
                dado.setAtivo(resultSet.getBoolean("ATIVO"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<ProdutoAlmoxarife> buscarUltimosProdutosAlmoxarife(int limite) {
        ArrayList<ProdutoAlmoxarife> produtos = new ArrayList<>();
        try {
            String sql = "select e.ID,e.INCLUSAO,e.ATUALIZACAO,e.AUTOR,e.CANCELADO,e.ATIVO,e.CODIGO,e.DESCRICAO,e.UNIDADE,e.COMPLEMENTO,e.DESTINO,"
                    + "e.LOCALIZACAO,e.ESTOQUE,(SELECT CONTA.NOME from ROOT.CONTA WHERE e.AUTOR = CONTA.ID) AS USUARIO "
                    + "from ROOT.PRODUTOESTOQUE AS e "
                    + "ORDER BY ID DESC";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setMaxRows(limite);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    ProdutoAlmoxarife produto = new ProdutoAlmoxarife();

                    produto.setId(resultSet.getInt("ID"));
                    produto.setInclusao(new Date(resultSet.getLong("INCLUSAO")));
                    produto.setAtualizacao(new Date(resultSet.getLong("ATUALIZACAO")));
                    produto.setAutor(resultSet.getString("USUARIO"));
                    produto.setCancelado(resultSet.getBoolean("CANCELADO"));
                    produto.setAtivo(resultSet.getBoolean("ATIVO"));

                    produto.setCodigo(resultSet.getString("CODIGO"));
                    produto.setDescricao(resultSet.getString("DESCRICAO"));
                    produto.setUnidade(resultSet.getString("UNIDADE"));
                    produto.setComplemento(resultSet.getString("COMPLEMENTO"));
                    produto.setDestino(resultSet.getString("DESTINO"));
                    produto.setLocalizacao(resultSet.getString("LOCALIZACAO"));
                    produto.setEstoque(resultSet.getInt("ESTOQUE"));

                    produtos.add(produto);
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return produtos;
    }

}
