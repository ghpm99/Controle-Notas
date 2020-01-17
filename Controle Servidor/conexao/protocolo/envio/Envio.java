/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexao.Protocolo.Envio;

import Classes.Base.Custo;
import Classes.Base.Item;
import Classes.Base.Mapa;
import Classes.Base.Produto;
import Classes.Contrato.Contrato;
import Classes.Contrato.Item.ItemContrato;
import Classes.Contrato.Mapa.MapaContrato;
import Classes.Contrato.Produto.ProdutoContrato;
import Classes.Financeiro.Diario;
import Classes.Financeiro.Fluxo;
import Classes.Financeiro.NotaFinanceiro;
import Classes.Fornecedor.Contato.Contato;
import Classes.Fornecedor.Fornecedor;
import Classes.Nota.Nota;
import Classes.Nota.anexo.AnexoNota;
import Classes.Nota.cobranca.Pagamento;
import Classes.Nota.item.ItemNota;
import Conexao.Protocolo.Leitura.Leitura;
import Conexao.Servidor.Cliente.Cliente;
import Seguranca.Login.Conta;
import Util.Dado;
import Util.Historico;
import Util.HistoricoPesquisa;
import Util.ItemOrcamento;
import Util.Parametros;
import classes.almoxarife.ProdutoAlmoxarife;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guilherme.machado
 */
public class Envio {

    /**
     *
     * @param dos
     * @param arg
     */
    public void enviarString(DataOutputStream dos, String arg) {
        try {
            if (arg == null) {
                arg = "";
            }
            dos.writeInt(arg.length());
            dos.writeChars(arg);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dos
     * @param dado
     */
    public void enviarDado(DataOutputStream dos, Dado dado) {
        try {
            dos.writeInt(dado.getId());
            dos.writeLong(dado.getInclusao().getTime());
            dos.writeLong(dado.getAtualizacao().getTime());
            enviarString(dos, dado.getAutor());
            dos.writeBoolean(dado.isCancelado());
            dos.writeBoolean(dado.isAtivo());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarCusto(DataOutputStream dos, Custo custo) {
        try {
            enviarDado(dos, custo);

            enviarString(dos, custo.getNumero());
            enviarString(dos, custo.getFornecedor());
            enviarString(dos, custo.getDescricao());
            dos.writeLong(custo.getValorTotal());
            dos.writeLong(custo.getGastoTotal());
            dos.writeLong(custo.getSaldoTotal());
            enviarString(dos, custo.getObservacao());
            enviarString(dos, custo.getContrato());
            dos.writeLong(custo.getEmissao().getTime());
            dos.writeInt(custo.getIdFornecedor());

        } catch (IOException ex) {
            Logger.getLogger(Leitura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarProduto(DataOutputStream dos, Produto produto) {

        enviarDado(dos, produto);

        enviarString(dos, produto.getCodigo());
        enviarString(dos, produto.getDescricao());
        enviarString(dos, produto.getUnidade());
    }

    public void enviarItem(DataOutputStream dos, Item item) {
        try {
            enviarDado(dos, item);

            enviarString(dos, item.getItem());
            enviarString(dos, item.getPlano());
            enviarString(dos, item.getTipo());
            enviarString(dos, item.getCusto());
            dos.writeLong(item.getValor());
            dos.writeLong(item.getSaldo());
            enviarString(dos, item.getDescricao());
            enviarString(dos, item.getUnidade());
            dos.writeLong(item.getQntTotal());
            dos.writeLong(item.getPrecoUnitario());
            dos.writeLong(item.getPrecoTotal());
            enviarString(dos, item.getNumeroMapa());
            enviarString(dos, item.getDescricaoMapa());
            dos.writeInt(item.getIdItem());

        } catch (IOException ex) {
            Logger.getLogger(Leitura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarMapa(DataOutputStream dos, Mapa mapa) {
        try {
            enviarDado(dos, mapa);

            enviarString(dos, mapa.getNumero());
            enviarString(dos, mapa.getDescricao());
            enviarString(dos, mapa.getEmpresa());
            dos.writeLong(mapa.getValor());
            dos.writeLong(mapa.getTotal());
            dos.writeLong(mapa.getSaldo());
            enviarString(dos, mapa.getContrato());

        } catch (IOException ex) {
            Logger.getLogger(Leitura.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param dos
     * @param array
     */
    public void enviarArrayString(DataOutputStream dos, ArrayList<String> array) {
        try {
            dos.writeInt(array.size());
            array.stream().forEach(s -> {
                try {
                    dos.writeInt(s.length());
                    dos.writeChars(s);
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dos
     * @param array
     */
    public void enviarArrayInt(DataOutputStream dos, ArrayList<Integer> array) {
        try {
            dos.writeInt(array.size());
            array.stream().forEach(s -> {
                try {
                    dos.writeInt(s);
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dos
     * @param historico
     */
    public void enviarHistoricoPesquisa(DataOutputStream dos, HistoricoPesquisa historico) {
        enviarArrayString(dos, historico.getItens());
        enviarString(dos, historico.getCampo());
        enviarString(dos, historico.getValor());
    }

    /**
     *
     * @param dos
     * @param item
     */
    public void enviarItemOrcamento(DataOutputStream dos, ItemOrcamento item) {

        try {
            dos.writeInt(item.getId());
            enviarString(dos, item.getItem());
            enviarString(dos, item.getPlanoConta());
            enviarString(dos, item.getDescricao());
            enviarString(dos, item.getUnidade());
            dos.writeLong(item.getQuantidadeTotal());
            dos.writeLong(item.getPrecoUnitario());
            dos.writeLong(item.getPrecoTotal());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param dos
     * @param nota
     */
    public void enviarNotaSimples(DataOutputStream dos, Nota nota) {
        try {

            enviarCusto(dos, nota);

            enviarString(dos, nota.getNumero());
            dos.writeLong(nota.getVencimento().getTime());
            dos.writeLong(nota.getVencimentoReal().getTime());
            dos.writeBoolean(nota.isPreNota());
            dos.writeBoolean(nota.isClassificada());
            dos.writeBoolean(nota.isLancada());
            enviarString(dos, nota.getFaturamentoDireto());
            enviarString(dos, nota.getSerie());
            enviarString(dos, nota.getTipo());
            dos.writeInt(nota.getIdPmWeb());
            enviarString(dos, nota.getStatus());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dos
     * @param nota
     */
    public void enviarNotaCompleta(DataOutputStream dos, Nota nota) {
        try {
            enviarNotaSimples(dos, nota);

            dos.writeInt(nota.getIdFaturamentoDireto());
            dos.writeInt(nota.getIdContrato());

            dos.writeInt(nota.getItens().size());
            nota.getItens().forEach(s -> {
                enviarItemNota(dos, s);
            });

            dos.writeInt(nota.getPagamentos().size());
            nota.getPagamentos().forEach(s -> {
                enviarPagamentoNota(dos, s);
            });

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dos
     * @param itemNota
     */
    public void enviarItemNota(DataOutputStream dos, ItemNota itemNota) {
        try {

            enviarItem(dos, itemNota);
            dos.writeInt(itemNota.getIdContrato());
            dos.writeInt(itemNota.getIdNota());
            dos.writeInt(itemNota.getCfop());
            dos.writeInt(itemNota.getCodigo());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarItensNota(DataOutputStream dos, ArrayList<ItemNota> itens) {
        try {
            dos.writeInt(itens.size());
            itens.stream().forEach((iten) -> enviarItemNota(dos, iten));
        } catch (IOException ex) {
            Logger.getLogger(Envio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dos
     * @param parametro
     */
    public void enviarParametrosNota(DataOutputStream dos, Parametros parametro) {
        try {
            enviarString(dos, parametro.getFornecedor());
            dos.writeLong(parametro.getEmissaoDe().getTime());
            dos.writeLong(parametro.getEmissaoAte().getTime());
            dos.writeLong(parametro.getVencimentoDe().getTime());
            dos.writeLong(parametro.getVencimentoAte().getTime());
            dos.writeLong(parametro.getValorDe());
            dos.writeLong(parametro.getValorAte());
            dos.writeBoolean(parametro.isClassificada());
            dos.writeBoolean(parametro.isAll());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dos
     * @param contrato
     */
    public void enviarContrato(DataOutputStream dos, Contrato contrato) {
        enviarCusto(dos, contrato);

    }

    /**
     *
     * @param dos
     * @param mapa
     */
    public void enviarMapaContrato(DataOutputStream dos, MapaContrato mapa) {
        try {
            enviarMapa(dos, mapa);

            dos.writeLong(mapa.getAssinatura().getTime());
            dos.writeLong(mapa.getCriacao().getTime());
            enviarString(dos, mapa.getObservacaoAssinatura());
            enviarString(dos, mapa.getObservacaoSistema());
            enviarString(dos, mapa.getFisico());
            enviarString(dos, mapa.getEncerramento());
            enviarString(dos, mapa.getObservacao());
            dos.writeInt(mapa.getIdContrato());

            dos.writeInt(mapa.getItens().size());
            mapa.getItens().forEach(s -> enviarItensContrato(dos, s));
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dos
     * @param item
     */
    public void enviarItensContrato(DataOutputStream dos, ItemContrato item) {
        try {
            enviarItem(dos, item);

            dos.writeInt(item.getIdContrato());
            dos.writeInt(item.getIdMapa());

            dos.writeInt(item.getProdutos().size());
            item.getProdutos().forEach(s -> enviarProdutosContrato(dos, s));
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dos
     * @param s
     */
    public void enviarProdutosContrato(DataOutputStream dos, ProdutoContrato s) {
        try {
            enviarProduto(dos, s);

            dos.writeLong(s.getQnt());//
            dos.writeLong(s.getPrecoUnitario());//
            dos.writeLong(s.getPrecoTotal());//
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dos
     * @param fornecedor
     */
    public void enviarFornecedor(DataOutputStream dos, Fornecedor fornecedor) {
        enviarDado(dos, fornecedor);
        enviarString(dos, fornecedor.getRazaoSocial());
        enviarString(dos, fornecedor.getNomeFantasia());
        enviarString(dos, fornecedor.getSigla());
        enviarString(dos, fornecedor.getTipoEmpresa());
        enviarString(dos, fornecedor.getCnpj());
        enviarString(dos, fornecedor.getInscEstadual());
        enviarString(dos, fornecedor.getInscMunicipal());
        enviarString(dos, fornecedor.getNatureza());
    }

    /**
     *
     * @param dos
     * @param conta
     */
    public void enviarConta(DataOutputStream dos, Conta conta) {
        try {

            dos.writeInt(conta.getId());
            dos.writeInt(conta.getNivel());

            enviarString(dos, conta.getLogin());
            enviarString(dos, conta.getNome());
            enviarString(dos, conta.getLogin());
            enviarString(dos, conta.getCategoria());
            enviarString(dos, conta.getSenha());
            enviarString(dos, conta.getEmail());

            dos.writeBoolean(conta.isBlock());
            dos.writeBoolean(conta.isCompra());
            dos.writeBoolean(conta.isAlmoxarife());
            dos.writeBoolean(conta.isAdministracao());
            dos.writeBoolean(conta.isEngenharia());
            dos.writeBoolean(conta.isFinanceiro());
            dos.writeBoolean(conta.isGerencia());
            dos.writeBoolean(conta.isLogado());

            dos.writeLong(conta.getUltimoLogin());
            dos.writeLong(conta.getExpira());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param dos
     * @param historico
     */
    public void enviarHistorico(DataOutputStream dos, Historico historico) {
        try {
            dos.writeInt(historico.getIdEvento());
            dos.writeInt(historico.getId());
            dos.writeInt(historico.getIdConta());
            enviarString(dos, historico.getEvento());
            enviarString(dos, historico.getUsuario());
            dos.writeLong(historico.getHora());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dos
     * @param historico
     */
    public void enviarHistoricos(DataOutputStream dos, ArrayList<Historico> historico) {
        try {
            dos.writeInt(historico.size());
            historico.forEach(s -> {
                enviarHistorico(dos, s);
            });
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dos
     * @param arquivo
     */
    public void enviarArquivo(DataOutputStream dos, File arquivo) {
        FileInputStream in = null;
        try {
            long tamanho = arquivo.length();

            byte[] bytes = new byte[1024 * 16];
            in = new FileInputStream(arquivo);

            enviarString(dos, arquivo.getName());
            dos.writeLong(tamanho);

            int read = 0;
            int totalRead = 0;
            int remaining = (int) tamanho;

            while ((read = in.read(bytes, 0, Math.min(bytes.length, remaining))) > 0) {
                totalRead += read;
                remaining -= read;
                //System.out.println("r " + read + " t " + totalRead + " re " + remaining + " ta " + tamanho);
                dos.write(bytes, 0, read);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     *
     * @param dos
     * @param anexo
     */
    public void enviarAnexoNota(DataOutputStream dos, AnexoNota anexo) {
        try {
            dos.writeInt(anexo.getId());
            enviarString(dos, anexo.getNome());
            enviarString(dos, anexo.getCaminho());
            enviarString(dos, anexo.getTipo());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dos
     * @param pagamento
     */
    public void enviarPagamentoNota(DataOutputStream dos, Pagamento pagamento) {
        try {
            dos.writeInt(pagamento.getId());
            dos.writeInt(pagamento.getIdNota());
            dos.writeLong(pagamento.getVencimento().getTime());
            dos.writeLong(pagamento.getVencimentoReal().getTime());
            dos.writeLong(pagamento.getValor());
            enviarString(dos, pagamento.getTipo());
            enviarString(dos, pagamento.getLinha());
            enviarString(dos, pagamento.getCodigo());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dos
     * @param nota
     */
    public void enviarNotaFinanceiro(DataOutputStream dos, NotaFinanceiro nota) {
        try {
            dos.writeInt(nota.getId());
            enviarString(dos, nota.getNumero());
            enviarString(dos, nota.getFornecedor());
            enviarString(dos, nota.getCnpj());
            dos.writeLong(nota.getVencimento().getTime());
            dos.writeLong(nota.getVencimentoReal().getTime());
            dos.writeLong(nota.getValor());
            dos.writeLong(nota.getDesconto());
            dos.writeLong(nota.getJuros());
            dos.writeLong(nota.getValorLiquido());
            enviarString(dos, nota.getTipo());
            dos.writeBoolean(nota.isBaixada());
            enviarString(dos, nota.getLinha());
            enviarString(dos, nota.getCodigo());
            dos.writeInt(nota.getIdNota());
            dos.writeInt(nota.getIdDiario());
            dos.writeBoolean(nota.isClassificada());
            dos.writeLong(nota.getFluxo().getTime());
            dos.writeLong(nota.getVencimentoNota().getTime());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param dos
     * @param diario
     */
    public void enviarDiarioSimples(DataOutputStream dos, Diario diario) {
        try {
            dos.writeInt(diario.getId());
            dos.writeLong(diario.getPagamento().getTime());
            dos.writeBoolean(diario.isAprovado());
            dos.writeLong(diario.getTotal());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dos
     * @param diario
     */
    public void enviarDiarioCompleto(DataOutputStream dos, Diario diario) {
        try {
            dos.writeInt(diario.getId());
            dos.writeLong(diario.getPagamento().getTime());
            dos.writeBoolean(diario.isAprovado());
            dos.writeLong(diario.getTotal());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dos
     * @param contato
     */
    public void enviarContato(DataOutputStream dos, Contato contato) {
        enviarDado(dos, contato);
        enviarString(dos, contato.getDescricao());
        enviarString(dos, contato.getTipo());
        enviarString(dos, contato.getNumero());
    }

    /**
     *
     * @param dos
     * @param fluxo
     */
    public void enviarFluxo(DataOutputStream dos, Fluxo fluxo) {
        try {
            dos.writeLong(fluxo.getInclusao().getTime());
            dos.writeLong(fluxo.getValor());
            dos.writeInt(fluxo.getNota());
            dos.writeLong(fluxo.getSaldo());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dos
     * @param fluxo
     */
    public void enviarArrayFluxo(DataOutputStream dos, ArrayList<Fluxo> fluxo) {
        try {
            dos.writeInt(fluxo.size());
            fluxo.forEach(s -> enviarFluxo(dos, s));
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarProdutoAlmoxarife(DataOutputStream dos,ProdutoAlmoxarife produto){
        try {
            enviarProduto(dos, produto);
            enviarString(dos, produto.getComplemento());
            enviarString(dos, produto.getDestino());
            enviarString(dos, produto.getLocalizacao());
            dos.writeLong(produto.getEstoque());
        } catch (IOException ex) {
            Logger.getLogger(Envio.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
}
