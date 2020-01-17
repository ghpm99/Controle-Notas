/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexao.Protocolo.Leitura;

import Classes.Contrato.Contrato;
import Classes.Contrato.Item.ItemContrato;
import Classes.Contrato.Mapa.MapaContrato;
import Classes.Contrato.Produto.ProdutoContrato;
import Classes.Financeiro.Diario;
import Classes.Fornecedor.Fornecedor;
import Classes.Nota.item.ItemNota;
import Classes.Nota.Nota;
import Classes.Nota.cobranca.Pagamento;
import Conexao.Servidor.Cliente.Cliente;
import Seguranca.Login.Conta;
import Util.Dado;
import Util.ItemOrcamento;
import Util.Parametros;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guilherme.machado
 */
public class Leitura {
    
    private DataInputStream input;
    
    /**
     *
     * @param dis
     */
    public Leitura(DataInputStream dis){
        this.input = dis;
    }

    /**
     *
     * @return
     */
    public String lerString() {
        try {
            int tamanho = input.readInt();
            StringBuilder out = new StringBuilder(tamanho);
            for (int i = 0; i < tamanho; i++) {
                out.append(input.readChar());
            }
            return out.toString();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    /**
     *
     * @return
     */
    public Nota lerNotaSimples() {
        Nota nota = new Nota();
        try {
            nota.setId(input.readInt());
            nota.setNumero(lerString());
            nota.setFornecedor(lerString());
            nota.setDescricao(lerString());
            nota.setGastoTotal(input.readLong());
            nota.setObservacao(lerString());
            nota.setVencimento(new java.util.Date(input.readLong()));
            nota.setVencimentoReal(new java.util.Date(input.readLong()));
            nota.setPreNota(input.readBoolean());
            nota.setClassificada(input.readBoolean());
            nota.setLancada(input.readBoolean());
            nota.setFaturamentoDireto(lerString());
            nota.setSerie(lerString());
            nota.setTipo(lerString());
            nota.setIdPmWeb(input.readInt());
            nota.setStatus(lerString());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nota;
    }

    /**
     *
     * @return
     */
    public Nota lerNotaCompleto() {
        Nota nota = new Nota();
        try {
            nota.setId(input.readInt());
            nota.setInclusao(new java.util.Date(input.readLong()));
            nota.setAtualizacao(new java.util.Date(input.readLong()));
            nota.setAutor(lerString());
            nota.setCancelado(input.readBoolean());
            nota.setNumero(lerString());
            nota.setFornecedor(lerString());
            nota.setValorTotal(input.readLong());
            nota.setGastoTotal(input.readLong());
            nota.setSaldoTotal(input.readLong());
            nota.setEmissao(new java.util.Date(input.readLong()));
            nota.setVencimento(new java.util.Date(input.readLong()));
            nota.setVencimentoReal(new java.util.Date(input.readLong()));
            nota.setPreNota(input.readBoolean());
            nota.setClassificada(input.readBoolean());
            nota.setLancada(input.readBoolean());
            nota.setFaturamentoDireto(lerString());
            nota.setSerie(lerString());
            nota.setTipo(lerString());
            nota.setAtivo(input.readBoolean());
            nota.setDescricao(lerString());
            nota.setObservacao(lerString());
            nota.setIdFornecedor(input.readInt());
            nota.setIdFaturamentoDireto(input.readInt());
            nota.setIdContrato(input.readInt());
            nota.setIdPmWeb(input.readInt());
            nota.setStatus(lerString());

            nota.setItens(lerItemNota());

            nota.setPagamentos(lerPagamentosNota());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nota;
    }

    /**
     *
     * @return
     */
    public ArrayList<ItemNota> lerItemNota() {
        ArrayList<ItemNota> itens = new ArrayList<>();
        try {
            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {

                ItemNota item = new ItemNota();
                item.setId(input.readInt());
                item.setItem(lerString());
                item.setPlano(lerString());
                item.setDescricao(lerString());
                item.setPrecoUnitario(input.readLong());
                item.setQntTotal(input.readLong());
                item.setPrecoTotal(input.readLong());
                item.setSaldo(input.readLong());

                item.setNumeroMapa(lerString());
                item.setDescricaoMapa(lerString());

                item.setUnidade(lerString());
                item.setValor(input.readLong());

                itens.add(item);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itens;
    }

    /**
     *
     * @return
     */
    public Nota lerNotaAtualizar() {
        Nota nota = new Nota();
        try {
            nota.setId(input.readInt());
            nota.setInclusao(new java.util.Date(input.readLong()));
            nota.setAtualizacao(new java.util.Date(input.readLong()));
            nota.setAutor(lerString());
            nota.setCancelado(input.readBoolean());
            nota.setNumero(lerString());
            nota.setFornecedor(lerString());
            nota.setValorTotal(input.readLong());
            nota.setGastoTotal(input.readLong());
            nota.setSaldoTotal(input.readLong());
            nota.setEmissao(new java.util.Date(input.readLong()));
            nota.setVencimento(new java.util.Date(input.readLong()));
            nota.setVencimentoReal(new java.util.Date(input.readLong()));
            nota.setPreNota(input.readBoolean());
            nota.setClassificada(input.readBoolean());
            nota.setLancada(input.readBoolean());
            nota.setSerie(lerString());
            nota.setTipo(lerString());
            nota.setDescricao(lerString());
            nota.setObservacao(lerString());
            nota.setIdFornecedor(input.readInt());
            nota.setIdFaturamentoDireto(input.readInt());
            nota.setIdPmWeb(input.readInt());

            nota.setItens(lerItemNotaAtualizar());

            nota.setPagamentos(lerPagamentosNota());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nota;
    }

    /**
     *
     * @return
     */
    public ArrayList<ItemNota> lerItemNotaAtualizar() {
        ArrayList<ItemNota> itens = new ArrayList<>();
        try {
            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {

                ItemNota item = new ItemNota();
                item.setId(input.readInt());
                item.setItem(lerString());
                item.setPlano(lerString());
                item.setDescricao(lerString());
                item.setPrecoUnitario(input.readLong());
                item.setQntTotal(input.readLong());
                item.setPrecoTotal(input.readLong());
                item.setSaldo(input.readLong());
                item.setRetencaoEmpreiteiro(input.readLong());
                item.setNumeroMapa(lerString());
                item.setDescricaoMapa(lerString());
                item.setCusto(lerString());
                item.setTipo(lerString());
                item.setUnidade(lerString());
                item.setValor(input.readLong());

                item.setBaseISSQN(input.readLong());
                item.setAliquotaISSQN(input.readFloat());
                item.setValorISSQN(input.readLong());
                item.setBaseCSRF(input.readLong());
                item.setAliquotaCSRF(input.readFloat());
                item.setValorCSRF(input.readLong());
                item.setBaseIRRF(input.readLong());
                item.setAliquotaIRRF(input.readFloat());
                item.setValorIRRF(input.readLong());
                item.setBaseINSS(input.readLong());
                item.setAliquotaINSS(input.readFloat());
                item.setValorINSS(input.readLong());
                item.setValorLiquido(input.readLong());
                item.setCodigo(input.readInt());
                item.setCfop(input.readInt());

                itens.add(item);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itens;
    }

    /**
     *
     * @return
     */
    public ArrayList<Pagamento> lerPagamentosNota() {
        ArrayList<Pagamento> itens = new ArrayList<>();
        try {
            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                itens.add(lerPagamentoNota());
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itens;
    }

    /**
     *
     * @return
     */
    public Contrato lerContratoAtualizar() {
        Contrato contrato = new Contrato();
        try {
            contrato.setId(input.readInt());
            contrato.setInclusao(new java.util.Date(input.readLong()));
            contrato.setAtualizacao(new java.util.Date(input.readLong()));
            contrato.setAutor(lerString());
            contrato.setCancelado(input.readBoolean());
            contrato.setAtivo(input.readBoolean());
            contrato.setNumero(lerString());
            contrato.setFornecedor(lerString());
            contrato.setDescricao(lerString());
            contrato.setValorTotal(input.readLong());
            contrato.setGastoTotal(input.readLong());
            contrato.setSaldoTotal(input.readLong());
            contrato.setObservacao(lerString());
            contrato.setEmissao(new java.util.Date(input.readLong()));
            contrato.setContrato(lerString());
            contrato.setIdFornecedor(input.readInt());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contrato;
    }

    /**
     *
     * @return
     */
    public MapaContrato lerMapaContrato() {
        MapaContrato mapa = new MapaContrato();
        try {
            mapa.setId(input.readInt());
            mapa.setInclusao(new java.util.Date(input.readLong()));
            mapa.setAtualizacao(new java.util.Date(input.readLong()));
            mapa.setAutor(lerString());
            mapa.setCancelado(input.readBoolean());
            mapa.setAtivo(input.readBoolean());
            mapa.setNumero(lerString());
            mapa.setDescricao(lerString());
            mapa.setEmpresa(lerString());
            mapa.setValor(input.readLong());
            mapa.setTotal(input.readLong());
            mapa.setSaldo(input.readLong());
            mapa.setContrato(lerString());
            mapa.setAssinatura(new java.util.Date(input.readLong()));
            mapa.setCriacao(new java.util.Date(input.readLong()));
            mapa.setObservacaoAssinatura(lerString());
            mapa.setObservacaoSistema(lerString());
            mapa.setFisico(lerString());
            mapa.setEncerramento(lerString());
            mapa.setObservacao(lerString());

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                mapa.adicionarItem(lerItensContrato());
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapa;
    }

    /**
     *
     * @return
     */
    public ItemContrato lerItensContrato() {
        ItemContrato item = new ItemContrato();
        try {
            item.setId(input.readInt());
            item.setInclusao(new java.util.Date(input.readLong()));
            item.setAtualizacao(new java.util.Date(input.readLong()));
            item.setAutor(lerString());
            item.setCancelado(input.readBoolean());
            item.setAtivo(input.readBoolean());
            item.setItem(lerString());
            item.setPlano(lerString());
            item.setTipo(lerString());
            item.setCusto(lerString());
            item.setValor(input.readLong());
            item.setSaldo(input.readLong());
            item.setDescricao(lerString());
            item.setUnidade(lerString());
            item.setQntTotal(input.readLong());
            item.setPrecoUnitario(input.readLong());
            item.setPrecoTotal(input.readLong());
            item.setNumeroMapa(lerString());
            item.setDescricaoMapa(lerString());

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                item.adicionarProduto(lerProdutosContrato());
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return item;
    }

    /**
     *
     * @return
     */
    public ProdutoContrato lerProdutosContrato() {
        ProdutoContrato produto = new ProdutoContrato();
        try {
            produto.setId(input.readInt());
            produto.setInclusao(new java.util.Date(input.readLong()));
            produto.setAtualizacao(new java.util.Date(input.readLong()));
            produto.setAutor(lerString());
            produto.setCancelado(input.readBoolean());
            produto.setAtivo(input.readBoolean());
            produto.setCodigo(lerString());
            produto.setDescricao(lerString());
            produto.setUnidade(lerString());
            produto.setQnt(input.readLong());
            produto.setPrecoUnitario(input.readLong());
            produto.setPrecoTotal(input.readLong());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return produto;
    }

    /**
     *
     * @return
     */
    public ItemOrcamento lerItemOrcamento() {
        ItemOrcamento item = new ItemOrcamento();
        try {
            item.setId(input.readInt());
            item.setItem(lerString());
            item.setPlanoConta(lerString());
            item.setDescricao(lerString());
            item.setUnidade(lerString());
            item.setQuantidadeTotal(input.readLong());
            item.setPrecoUnitario(input.readLong());
            item.setPrecoTotal(input.readLong());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return item;
    }

    /**
     *
     * @return
     */
    public Parametros lerParametrosNota() {
        Parametros parametro = new Parametros();
        try {
            parametro.setFornecedor(lerString());
            parametro.setEmissaoDe(new Date(input.readLong()));
            parametro.setEmissaoAte(new Date(input.readLong()));
            parametro.setVencimentoDe(new Date(input.readLong()));
            parametro.setVencimentoAte(new Date(input.readLong()));
            parametro.setValorDe(input.readLong());
            parametro.setValorAte(input.readLong());
            parametro.setClassificada(input.readBoolean());
            parametro.setAll(input.readBoolean());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return parametro;
    }

    /**
     *
     * @param dado
     */
    public void lerDado(Dado dado) {
        try {
            dado.setId(input.readInt());
            dado.setInclusao(new java.util.Date(input.readLong()));
            dado.setAtualizacao(new java.util.Date(input.readLong()));
            dado.setAutor(lerString());
            dado.setCancelado(input.readBoolean());
            dado.setAtivo(input.readBoolean());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @return
     */
    public Fornecedor lerFornecedorCompleto() {
        Fornecedor fornecedor = new Fornecedor();
        lerDado(fornecedor);
        fornecedor.setRazaoSocial(lerString());
        fornecedor.setNomeFantasia(lerString());
        fornecedor.setSigla(lerString());
        fornecedor.setTipoEmpresa(lerString());
        fornecedor.setCnpj(lerString());
        fornecedor.setInscEstadual(lerString());
        fornecedor.setInscMunicipal(lerString());
        fornecedor.setNatureza(lerString());

        return fornecedor;
    }

    /**
     *
     * @return
     */
    public Conta lerConta() {
        Conta contaTemp = new Conta();
        try {

            contaTemp.setId(input.readInt());
            contaTemp.setNivel(input.readInt());

            contaTemp.setLogin(lerString());
            contaTemp.setNome(lerString());
            contaTemp.setLogin(lerString());
            contaTemp.setCategoria(lerString());
            contaTemp.setSenha(lerString());
            contaTemp.setEmail(lerString());

            contaTemp.setBlock(input.readBoolean());
            contaTemp.setCompra(input.readBoolean());
            contaTemp.setAlmoxarife(input.readBoolean());
            contaTemp.setAdministracao(input.readBoolean());
            contaTemp.setEngenharia(input.readBoolean());
            contaTemp.setFinanceiro(input.readBoolean());
            contaTemp.setGerencia(input.readBoolean());
            contaTemp.setLogado(input.readBoolean());

            contaTemp.setUltimoLogin(input.readLong());
            contaTemp.setExpira(input.readLong());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contaTemp;
    }

    /**
     *
     * @param caminho
     * @return
     */
    public File lerArquivo(File caminho) {
        File arquivo = null;
        FileOutputStream out = null;
        BufferedOutputStream buf = null;
        try {
            if (!caminho.exists()) {
                caminho.mkdirs();
            }
            String caminhoFinal = caminho.getAbsolutePath() + "\\" + lerString();

            long tamanho = input.readLong();

            byte[] bytes = new byte[1024 * 16];
            int read = 0;
            int totalRead = 0;
            int remaining = (int) tamanho;

            arquivo = new File(caminhoFinal);

            out = new FileOutputStream(arquivo);

            buf = new BufferedOutputStream(out);
            //Math.min(bytes.length, remaining)
            while ((read = input.read(bytes, 0, Math.min(bytes.length, remaining))) > 0) {
                totalRead += read;
                remaining -= read;
                //System.out.println("r " + read + " t " + totalRead + " re " + remaining + " ta " + tamanho);
                buf.write(bytes, 0, read);

            }
            bytes = null;
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (buf != null) {
                    buf.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return arquivo;
    }

    /**
     *
     * @return
     */
    public Pagamento lerPagamentoNota() {
        Pagamento pagamento = new Pagamento();
        try {
            pagamento.setId(input.readInt());
            pagamento.setIdNota(input.readInt());
            pagamento.setVencimento(new Date(input.readLong()));
            pagamento.setVencimentoReal(new Date(input.readLong()));
            pagamento.setValor(input.readLong());
            pagamento.setTipo(lerString());
            pagamento.setLinha(lerString());
            pagamento.setCodigo(lerString());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pagamento;
    }

    /**
     *
     * @return
     */
    public Diario lerDiarioSimples() {
        Diario diario = new Diario();
        try {
            diario.setId(input.readInt());
            diario.setPagamento(new java.util.Date(input.readLong()));
            diario.setAprovado(input.readBoolean());
            diario.setTotal(input.readLong());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return diario;
    }
}
