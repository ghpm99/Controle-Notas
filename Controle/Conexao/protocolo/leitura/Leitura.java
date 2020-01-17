/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexao.protocolo.leitura;

import Conexao.Cliente;
import Util.Dado;
import Util.Historico;
import Util.HistoricoPesquisa;
import Util.ItemOrcamento;
import Util.Parametros;
import classes.almoxarife.ProdutoAlmoxarife;
import classes.base.Custo;
import classes.base.Item;
import classes.base.Mapa;
import classes.base.Produto;
import classes.conta.Conta;
import classes.contrato.Contrato;
import classes.contrato.item.ItemContrato;
import classes.contrato.mapa.MapaContrato;
import classes.contrato.produto.ProdutoContrato;
import classes.financeiro.Diario;
import classes.financeiro.Fluxo;
import classes.financeiro.NotaFinanceiro;
import classes.fornecedor.Fornecedor;
import classes.fornecedor.contato.Contato;
import classes.nota.Nota;
import classes.nota.anexo.AnexoNota;
import classes.nota.cobranca.Pagamento;
import classes.nota.item.ItemNota;
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

    public Leitura(DataInputStream dis) {
        this.input = dis;
    }

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

    public void lerCusto(Custo custo) {
        try {
            lerDado(custo);
            custo.setNumero(lerString());
            custo.setFornecedor(lerString());
            custo.setDescricao(lerString());
            custo.setValorTotal(input.readLong());
            custo.setGastoTotal(input.readLong());
            custo.setSaldoTotal(input.readLong());
            custo.setObservacao(lerString());
            custo.setContrato(lerString());
            custo.setEmissao(new Date(input.readLong()));
            custo.setIdFornecedor(input.readInt());
        } catch (IOException ex) {
            Logger.getLogger(Leitura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void lerProduto(Produto produto) {
        lerDado(produto);
        produto.setCodigo(lerString());
        produto.setDescricao(lerString());
        produto.setUnidade(lerString());
    }

    public void lerItem(Item item) {
        try {
            lerDado(item);
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
            item.setIdItem(input.readInt());
        } catch (IOException ex) {
            Logger.getLogger(Leitura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void lerMapa(Mapa mapa) {
        try {
            lerDado(mapa);
            mapa.setNumero(lerString());
            mapa.setDescricao(lerString());
            mapa.setEmpresa(lerString());
            mapa.setValor(input.readLong());
            mapa.setTotal(input.readLong());
            mapa.setSaldo(input.readLong());
            mapa.setContrato(lerString());
        } catch (IOException ex) {
            Logger.getLogger(Leitura.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void lerContrato(Contrato contrato) {
        lerCusto(contrato);
    }

    public void lerNotaSimples(Nota nota) {
        try {
            lerCusto(nota);
            nota.setNumero(lerString());
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
    }

    public void lerNotaCompleto(Nota nota) {
        try {
            lerNotaSimples(nota);
            nota.setIdFaturamentoDireto(input.readInt());
            nota.setIdContrato(input.readInt());
            nota.setItens(lerItensNota());
            nota.setPagamentos(lerPagamentosNota());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void lerItemNota(ItemNota item) {
        try {
            lerItem(item);

            item.setIdContrato(input.readInt());
            item.setIdNota(input.readInt());
            item.setCfop(input.readInt());
            item.setCodigo(input.readInt());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ArrayList<Pagamento> lerPagamentosNota() {
        ArrayList<Pagamento> itens = new ArrayList<>();
        try {
            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                Pagamento pagamento = new Pagamento();
                lerPagamentoNota(pagamento);
                itens.add(pagamento);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itens;
    }

    public void lerMapaContrato(MapaContrato mapa) {
        try {
            lerMapa(mapa);
            mapa.setAssinatura(new java.util.Date(input.readLong()));
            mapa.setCriacao(new java.util.Date(input.readLong()));
            mapa.setObservacaoAssinatura(lerString());
            mapa.setObservacaoSistema(lerString());
            mapa.setFisico(lerString());
            mapa.setEncerramento(lerString());
            mapa.setObservacao(lerString());
            mapa.setIdContrato(input.readInt());

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                ItemContrato item = new ItemContrato();
                lerItensContrato(item);
                mapa.adicionarItem(item);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void lerItensContrato(ItemContrato item) {
        try {
            lerItem(item);
            
            item.setIdContrato(input.readInt());
            item.setIdMapa(input.readInt());
            
            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                ProdutoContrato produto = new ProdutoContrato();
                lerProdutosContrato(produto);
                item.adicionarProduto(produto);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void lerProdutosContrato(ProdutoContrato produto) {
        try {
            lerProduto(produto);
            produto.setQnt(input.readLong());
            produto.setPrecoUnitario(input.readLong());
            produto.setPrecoTotal(input.readLong());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void lerItemOrcamento(ItemOrcamento item) {
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
    }

    public void lerParametrosNota(Parametros parametro) {
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
    }

    public void lerFornecedor(Fornecedor fornecedor) {
        lerDado(fornecedor);
        fornecedor.setRazaoSocial(lerString());
        fornecedor.setNomeFantasia(lerString());
        fornecedor.setSigla(lerString());
        fornecedor.setTipoEmpresa(lerString());
        fornecedor.setCnpj(lerString());
        fornecedor.setInscEstadual(lerString());
        fornecedor.setInscMunicipal(lerString());
        fornecedor.setNatureza(lerString());
    }

    public void lerConta(Conta contaTemp) {
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

    }

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

    public void lerPagamentoNota(Pagamento pagamento) {
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
    }

    public void lerDiarioSimples(Diario diario) {
        try {
            diario.setId(input.readInt());
            diario.setPagamento(new java.util.Date(input.readLong()));
            diario.setAprovado(input.readBoolean());
            diario.setTotal(input.readLong());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<String> lerArrayString() {
        ArrayList<String> array = new ArrayList<>();
        try {
            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                array.add(lerString());
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

        return array;
    }

    public ArrayList<Integer> lerArrayInt() {
        ArrayList<Integer> array = new ArrayList<>();
        try {
            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                array.add(input.readInt());
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

        return array;
    }

    public ArrayList<ItemNota> lerItensNota() {
        ArrayList<ItemNota> itens = new ArrayList<>();
        try {
            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {

                ItemNota item = new ItemNota();
                lerItemNota(item);
                itens.add(item);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itens;
    }
  
    public void lerHistoricoPesquisa(HistoricoPesquisa historico) {
        historico.setItens(lerArrayString());
        historico.setCampo(lerString());
        historico.setValor(lerString());
    }
    

    public void lerHistorico(Historico historico) {
        try {
            historico.setIdEvento(input.readInt());
            historico.setId(input.readInt());
            historico.setIdConta(input.readInt());
            historico.setEvento(lerString());
            historico.setUsuario(lerString());
            historico.setHora(input.readLong());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Historico> lerHistoricos() {
        ArrayList<Historico> historico = new ArrayList<>();
        try {
            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                Historico temp = new Historico();
                lerHistorico(temp);
                historico.add(temp);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return historico;
    }

    public void lerAnexoNota(AnexoNota anexo) {
        try {
            anexo.setId(input.readInt());
            anexo.setNome(lerString());
            anexo.setCaminho(lerString());
            anexo.setTipo(lerString());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void lerNotaFinanceiro(NotaFinanceiro nota) {
        try {
            nota.setId(input.readInt());
            nota.setNumero(lerString());
            nota.setFornecedor(lerString());
            nota.setCnpj(lerString());
            nota.setVencimento(new java.util.Date(input.readLong()));
            nota.setVencimentoReal(new java.util.Date(input.readLong()));
            nota.setValor(input.readLong());
            nota.setDesconto(input.readLong());
            nota.setJuros(input.readLong());
            nota.setValorLiquido(input.readLong());
            nota.setTipo(lerString());
            nota.setBaixada(input.readBoolean());
            nota.setLinha(lerString());
            nota.setCodigo(lerString());
            nota.setIdNota(input.readInt());
            nota.setIdDiario(input.readInt());
            nota.setClassificada(input.readBoolean());
            nota.setFluxo(new java.util.Date(input.readLong()));
            nota.setVencimentoNota(new java.util.Date(input.readLong()));
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void lerDiarioCompleto(Diario diario) {
        try {
            diario.setId(input.readInt());
            diario.setPagamento(new java.util.Date(input.readLong()));
            diario.setAprovado(input.readBoolean());
            diario.setTotal(input.readLong());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void lerContato(Contato contato) {
        lerDado(contato);
        contato.setDescricao(lerString());
        contato.setTipo(lerString());
        contato.setNumero(lerString());
    }

    public void lerFluxo(Fluxo fluxo) {
        try {

            fluxo.setInclusao(new java.util.Date(input.readLong()));
            fluxo.setValor(input.readLong());
            fluxo.setNota(input.readInt());
            fluxo.setSaldo(input.readLong());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Fluxo> lerArrayFluxo() {
        ArrayList<Fluxo> fluxo = new ArrayList<>();
        try {
            int tamanho = input.readInt();

            for (int i = 0; i < tamanho; i++) {
                Fluxo temp = new Fluxo();
                lerFluxo(temp);
                fluxo.add(temp);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fluxo;
    }

    public ArrayList<NotaFinanceiro> lerNotasFinanceiro() {
        ArrayList<NotaFinanceiro> notas = new ArrayList<>();
        try {
            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                NotaFinanceiro nota = new NotaFinanceiro();
                lerNotaFinanceiro(nota);
                notas.add(nota);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    public void lerProdutoAlmoxarife(ProdutoAlmoxarife produto) {
        try {
            lerProduto(produto);
            produto.setComplemento(lerString());
            produto.setDestino(lerString());
            produto.setLocalizacao(lerString());
            produto.setEstoque(input.readLong());
        } catch (IOException ex) {
            Logger.getLogger(Leitura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
