/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexao.Servidor.Cliente;

import BancoDados.Conexao;
import BancoDados.GerenciadorBanco;
import BancoDados.modulo.ModuloConfiguracao;
import BancoDados.modulo.ModuloConta;
import BancoDados.modulo.ModuloContrato;
import BancoDados.modulo.ModuloControle;
import BancoDados.modulo.ModuloFinanceiro;
import BancoDados.modulo.ModuloFornecedor;
import BancoDados.modulo.ModuloNota;
import BancoDados.modulo.ModuloUtil;
import BancoDados.modulo.Modulos;
import Classes.Contrato.Contrato;
import Classes.Contrato.Mapa.MapaContrato;
import Classes.Financeiro.Diario;
import Classes.Financeiro.NotaFinanceiro;
import Classes.Fornecedor.Contato.Contato;
import Classes.Fornecedor.Fornecedor;
import Classes.Nota.Nota;
import Classes.Nota.anexo.AnexoNota;
import Classes.Nota.cobranca.Pagamento;
import Conexao.Protocolo.Envio.Envio;
import Conexao.Protocolo.Leitura.Leitura;
import Conexao.Servidor.Servidor;
import Configuracao.Configuracao;
import Configuracao.Referencia;
import GUI.principal.Principal;
import Rotina.Email.Status;
import Seguranca.Login.Conta;
import Util.ItemOrcamento;
import Util.Parametros;
import Util.Sobre;
import bancodados.modulo.ModuloProduto;
import classes.almoxarife.ProdutoAlmoxarife;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

/**
 * @author Guilherme
 */
public class Cliente extends Thread {

    /**
     *
     */
    protected Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private String validado, id = "", banco;
    private boolean isAlive, liberado, logado;
    private Conta conta;
    private Envio envio;
    private Leitura leitura;

    /**
     *
     * @param socket
     */
    public Cliente(SSLSocket socket) {
        this.socket = socket;
        try {
            socket.setSoTimeout(3600000);
            socket.setKeepAlive(true);
        } catch (SocketException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            logado = false;
            log("Cliente conectou, ip: " + getIP() + " Nome: " + socket.getInetAddress().getHostName());
            Configuracao.INSTANCIA.salvarEvento("Cliente conectou, ip: " + getIP() + " Nome: " + socket.getInetAddress().getHostName(), getIP());
            getStreams();
            gerarProtocolos();
            processConnection();
            processar();
        } catch (Exception e) {
            e.printStackTrace();
            fechar();
        } finally {
            fechar();
        }

    }

    private void getStreams() throws IOException {

        output = new DataOutputStream(socket.getOutputStream());
        output.flush();

        input = new DataInputStream(socket.getInputStream());

        log("Got I/O Streams");

    }

    private void gerarProtocolos() {
        envio = new Envio();
        leitura = new Leitura(input);
    }

    private void processConnection() {
        int tipo = -1;
        isAlive = true;
        liberado = true;

        enviarOk(liberado);
        do {
            try {
                tipo = input.readInt();

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }

        } while (tipo != 0);

        log("Liberado " + liberado);
    }

    private void processar() {
        do {
            try {
                processarProtocolo(input.readInt());
            } catch (IOException ex) {
                isAlive = false;
                ex.printStackTrace();
            }
        } while (isAlive && liberado);
    }

    /**
     * Fecha as conexoes
     */
    public void fechar() {
        log("Conexao fechada");
        Configuracao.INSTANCIA.salvarEvento("Cliente desconectou, ip: " + getIP() + " Nome: " + socket.getInetAddress().getHostName(), getIP());
        try {
            Servidor.INSTANCIA.remove(this);
            output.close();
            input.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            log(e.getMessage());
        }
    }

    private Conexao getBancoDeDados() {
        return GerenciadorBanco.INSTANCIA.getConexao(banco);
    }

    /**
     *
     * @return
     */
    public String getIP() {
        return socket.getInetAddress().getHostAddress();
    }

    /**
     *
     * @return
     */
    public String getID() {
        return id;
    }

    private String getUsuario() {
        return conta.getNome();
    }

    private String criptrografar(String senha) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");

            byte messageDigest[] = algorithm.digest(senha.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02X", 0xFF & b));
            }
            return hexString.toString();

        } catch (Exception e) {
            sendErro(e);
        }

        return null;
    }

    private void sendData(Request mensagem) {
        try {
            output.write(mensagem.getMensagem());
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void log(String mensagem) {
        Principal.setStatus(getIP() + " : " + mensagem);
    }

    /*
     Processa protocolo
     */
    private void processarProtocolo(int codigo) throws IOException {

        switch (codigo) {
            case -1:

                break;
            case 0:
                enviarStatus();
                break;
            case 1:
                processarLogin(leitura.lerString(), leitura.lerString(), leitura.lerString());
                break;
            case 2:
                ping();
                break;
            case 3:
                recuperarUsuario();
                break;
            case 4:
                getNivelConta();
                break;
            case 5:
                isAlive = false;
                break;
            case 6:
                incluirNota(input.readInt(), input.readInt());
                break;
            case 7:

                break;
            case 8:

                break;
            case 9:
                buscarCampos(leitura.lerString());
                break;
            case 10:
                buscarNotaSimples(leitura.lerString(), leitura.lerString());
                break;
            case 11:
                getUltimasNotas(input.readInt());
                break;
            case 12:
                buscarNota(input.readInt());
                break;
            case 13:
                buscarContratoSimples(leitura.lerString(), leitura.lerString());
                break;
            case 14:
                getUltimosContratos(input.readInt());
                break;
            case 15:
                buscarContrato(input.readInt());
                break;
            case 16:
                buscarPreNotas();
                break;
            case 17:
                lancarNota(input.readInt());
                break;
            case 18:
                buscarNotasContrato(input.readInt());
                break;
            case 19:
                incluirContrato();
                break;
            case 20:
                incluirMapaContrato(input.readInt());
                break;
            case 21:
                inativarContrato(input.readInt());
                break;
            case 22:
                atualizarContrato(leitura.lerContratoAtualizar());
                break;
            case 23:
                atualizarMapaContrato(leitura.lerMapaContrato());
                break;
            case 24:
                incluirItemContrato(input.readInt());
                break;
            case 25:
                buscarItemOrcamento(leitura.lerString());
                break;
            case 26:
                getString(leitura.lerString(), leitura.lerString(), input.readInt());
                break;
            case 27:
                getInt(leitura.lerString(), leitura.lerString(), input.readInt());
                break;
            case 28:
                getLong(leitura.lerString(), leitura.lerString(), input.readInt());
                break;
            case 29:
                getBoolean(leitura.lerString(), leitura.lerString(), input.readInt());
                break;
            case 30:
                setString(leitura.lerString(), leitura.lerString(), input.readInt(), leitura.lerString());
                break;
            case 31:
                setInt(leitura.lerString(), leitura.lerString(), input.readInt(), input.readInt());
                break;
            case 32:
                setLong(leitura.lerString(), leitura.lerString(), input.readInt(), input.readLong());
                break;
            case 33:
                setBoolean(leitura.lerString(), leitura.lerString(), input.readInt(), input.readBoolean());
                break;
            case 34:
                setDate(leitura.lerString(), leitura.lerString(), input.readInt(), new Date(input.readLong()));
                break;
            case 35:
                getDate(leitura.lerString(), leitura.lerString(), input.readInt());
                break;
            case 36:
                relatorioNotas(leitura.lerParametrosNota());
                break;
            case 37:
                buscarBancos();
                break;
            case 38:
                buscarUltimosFornecedores(input.readInt());
                break;
            case 39:
                buscarFornecedorCompleto(input.readInt());
                break;
            case 40:
                salvarFornecedor(leitura.lerFornecedorCompleto());
                break;
            case 41:
                buscarFornecedorCampo(leitura.lerString(), leitura.lerString());
                break;
            case 42:
                incluirFornecedor();
                break;
            case 43:
                buscarContratoProblema();
                break;
            case 44:
                buscarNotaProblema();
                break;
            case 45:
                relatorioOrcamento(leitura.lerParametrosNota());
                break;
            case 46:
                listarContas();
                break;
            case 47:
                atualizarConta(leitura.lerConta());
                break;
            case 48:
                incluirConta();
                break;
            case 49:
                atualizarSenhaConta(input.readInt(), leitura.lerString());
                break;
            case 50:
                listarItensOrcamento();
                break;
            case 51:
                incluirItemOrcamento();
                break;
            case 52:
                atualizarItemOrcamento(leitura.lerItemOrcamento());
                break;
            case 53:
                buscarHistorico(leitura.lerString(), input.readInt());
                break;
            case 54:
                setFloat(leitura.lerString(), leitura.lerString(), input.readInt(), input.readFloat());
                break;
            case 55:
                verificarNumeroNota(input.readInt(), input.readInt(), leitura.lerString());
                break;
            case 56:
                buscarNotasFornecedor(input.readInt());
                break;
            case 57:
                anexarNota(input.readInt(), leitura.lerString());
                break;
            case 58:
                listarAnexosNota(input.readInt());
                break;
            case 59:
                abrirAnexoNota(input.readInt());
                break;
            case 60:
                incluirPagamentoNota(input.readInt());
                break;
            case 61:
                removerPagamentoNota(leitura.lerPagamentoNota());
                break;
            case 62:
                estornarNota(leitura.lerNotaCompleto());
                break;
            case 63:
                listarAbertasFinanceiro();
                break;
            case 64:
                listarBaixadasFinanceiro();
                break;
            case 65:
                listarDiarios(leitura.lerString());
                break;
            case 66:
                incluirDiario();
                break;
            case 67:
                incluirNotaEmDiario(input.readInt(), input.readInt());
                break;
            case 68:
                buscarDiario(input.readInt());
                break;
            case 69:
                excluirNotaEmDiario(input.readInt(), input.readInt());
                break;
            case 70:
                atualizarDescontoNotaFinanceiro(input.readInt(), input.readLong());
                break;
            case 71:
                atualizarJurosNotaFinanceiro(input.readInt(), input.readLong());
                break;
            case 72:
                atualizarTipoNotaFinanceiro(input.readInt(), leitura.lerString());
                break;
            case 73:
                atualizarLinhaNotaFinanceiro(input.readInt(), leitura.lerString());
                break;
            case 74:
                atualizarCodigoNotaFinanceiro(input.readInt(), leitura.lerString());
                break;
            case 75:
                atualizarValorLiquidoNotaFinanceiro(input.readInt(), input.readLong());
                break;
            case 76:
                salvarDiario(leitura.lerDiarioSimples());
                break;
            case 77:
                postarDiario(input.readInt());
                break;
            case 78:
                listarNotasDividir(input.readInt());
                break;
            case 79:
                buscarValorLiquidoNota(input.readInt());
                break;
            case 80:
                atualizarValorFianceiro(input.readInt(), input.readLong());
                break;
            case 81:
                listarContratosFornecedor(input.readInt());
                break;
            case 82:
                listarPagamentosFornecedor(input.readInt(), input.readBoolean());
                break;
            case 83:
                incluirContatoFornecedor(input.readInt());
                break;
            case 84:
                listarContatoFornecedor(input.readInt());
                break;
            case 85:
                cancelarNota(input.readInt());
                break;
            case 86:
                listarFluxo();
                break;
            case 87:
                listarNotasSubmetidas();
                break;
            case 88:
                workflowNota(input.readInt(), leitura.lerString());
                break;
            case 89:
                buscarFluxoDetalhado(leitura.lerParametrosNota());
                break;
            case 90:
                listarNotasDiario(input.readInt());
                break;
            case 91:
                avaliar(input.readInt(), leitura.lerString(), leitura.lerString());
                break;
            case 92:
                buscarNotasDiario(input.readInt());
                break;
            case 93:
                buscarUltimosProdutosAlmoxarife(input.readInt());
                break;
            case 94:
                incluirProdutoAlmoxarife();
                break;
            default:
                break;
        }
    }

    private void enviarOk(boolean liberado) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeBoolean(liberado);
                String temp = liberado ? Sobre.getMensagem() + "\nIP:" + getIP() + "\nHost:"
                        + socket.getLocalAddress().getHostName() + " IP:"
                        + socket.getLocalAddress().getHostAddress() : "Ip já está conectado";
                envio.enviarString(dos, temp);
            }
        });

    }

    private void enviarStatus() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(0);
                dos.writeChars(Sobre.getMensagem() + "\nIP:" + getIP() + "\nHost:"
                        + socket.getLocalAddress().getHostName() + " IP:"
                        + socket.getLocalAddress().getHostAddress());
            }
        });
    }

    private void sendErro(Exception e) {
        log(e.getMessage());
        e.printStackTrace();
    }

    private void processarLogin(String login, String senha, String banco) {
        this.banco = banco;
        int temp = ModuloConta.getInstancia().logar(getBancoDeDados(), login, senha);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeBoolean(temp != -1);
            }
        });
        if (temp != -1) {
            conta = ModuloConta.getInstancia().buscarConta(getBancoDeDados(), temp);
            log("Usuario " + conta.getNome() + " logou");
            Configuracao.INSTANCIA.salvarEvento("Usuario " + conta.getNome() + " logou, ip: " + getIP() + " Nome: " + socket.getInetAddress().getHostName(), getIP());
            logado = true;
        } else {
            conta = new Conta();
            log("Usuario ou senha errada");
        }

    }

    private void recuperarUsuario() {
        String usuario = getUsuario();
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(usuario.length());
                dos.writeChars(usuario);
            }
        });

    }

    private void getNivelConta() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarConta(dos, conta);
            }
        });
    }

    private void buscarCampos(String tabela) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarHistoricoPesquisa(dos, ModuloConfiguracao.getInstancia().buscarCampos(getBancoDeDados(), conta.getId(), tabela));
            }
        });
    }

    private void buscarNotaSimples(String campo, String valor) {

        ArrayList<Nota> temp = ModuloNota.getInstancia().buscarNotaCampo(getBancoDeDados(), conta.getId(), campo, valor);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(temp.size());

                temp.forEach(s -> {
                    envio.enviarNotaSimples(dos, s);
                });
            }
        });

    }

    private void buscarNota(int id) {
        Nota temp = ModuloNota.getInstancia().buscarNotaId(getBancoDeDados(), id);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarNotaCompleta(dos, temp);
            }
        });
    }

    private void getString(String tabela, String campo, int id) {
        String retorno = (String) Modulos.getInstancia().buscarObject(getBancoDeDados(), tabela, campo, id);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(retorno.length());
                dos.writeChars(retorno);
            }
        });

    }

    private void getInt(String tabela, String campo, int id) {
        int retorno = (int) Modulos.getInstancia().buscarObject(getBancoDeDados(), tabela, campo, id);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(retorno);
            }
        });

    }

    private void getLong(String tabela, String campo, int id) {
        long retorno = (long) Modulos.getInstancia().buscarObject(getBancoDeDados(), tabela, campo, id);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeLong(retorno);
            }
        });

    }

    private void getBoolean(String tabela, String campo, int id) {
        boolean retorno = (boolean) Modulos.getInstancia().buscarObject(getBancoDeDados(), tabela, campo, id);

        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeBoolean(retorno);
            }
        });
    }

    private void setString(String tabela, String campo, int id, Object arg) {
        int retorno = Modulos.getInstancia().setObject(getBancoDeDados(), tabela, campo, id, arg, conta.getId());
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(retorno);
            }
        });

    }

    private void setInt(String tabela, String campo, int id, int arg) {
        int retorno = Modulos.getInstancia().setObject(getBancoDeDados(), tabela, campo, id, arg, conta.getId());
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(retorno);
            }
        });

    }

    private void setFloat(String tabela, String campo, int id, float arg) {
        int retorno = Modulos.getInstancia().setObject(getBancoDeDados(), tabela, campo, id, arg, conta.getId());
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(retorno);
            }
        });

    }

    private void setLong(String tabela, String campo, int id, long arg) {
        int retorno = Modulos.getInstancia().setObject(getBancoDeDados(), tabela, campo, id, arg, conta.getId());
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(retorno);
            }
        });
    }

    private void setBoolean(String tabela, String campo, int id, boolean arg) {
        int retorno = Modulos.getInstancia().setObject(getBancoDeDados(), tabela, campo, id, arg, conta.getId());
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(retorno);
            }
        });

    }

    private void setDate(String tabela, String campo, int id, Date arg) {
        int retorno = Modulos.getInstancia().setObject(getBancoDeDados(), tabela, campo, id, arg, conta.getId());
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(retorno);
            }
        });

    }

    private void getDate(String tabela, String campo, int id) {
        Date retorno = (Date) Modulos.getInstancia().buscarObject(getBancoDeDados(), tabela, campo, id);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeLong(retorno.getTime());
            }
        });

    }

    private void getUltimasNotas(int limite) {
        ArrayList<Nota> temp = ModuloNota.getInstancia().buscarNotaUltimos(getBancoDeDados(), limite);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(temp.size());

                temp.forEach(s -> {
                    envio.enviarNotaSimples(dos, s);
                });
            }
        });

    }

    private void buscarBancos() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarArrayString(dos, new ArrayList<>(GerenciadorBanco.INSTANCIA.getNomeBancos()));
            }
        });

    }

    private void incluirNota(int idFornecedor, int idContrato) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarNotaCompleta(dos, ModuloNota.getInstancia().incluirNota(getBancoDeDados(), idFornecedor, idContrato));
            }
        });

    }

    private void ping() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeLong(new java.util.Date().getTime());
            }
        });

    }

    private void getUltimosContratos(int limite) {
        ArrayList<Contrato> temp = ModuloContrato.getInstancia().buscarContratoUltimos(getBancoDeDados(), limite);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(temp.size());

                temp.forEach(s -> {
                    envio.enviarContrato(dos, s);
                });
            }
        });

    }

    private void buscarContratoSimples(String campo, String valor) {
        ArrayList<Contrato> temp = ModuloContrato.getInstancia().buscarContratoCampo(getBancoDeDados(), conta.getId(), campo, valor);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(temp.size());
                temp.forEach(s -> {
                    envio.enviarContrato(dos, s);
                });
            }
        });

    }

    private void buscarContrato(int id) {
        Contrato temp = ModuloContrato.getInstancia().buscarContrato(getBancoDeDados(), id);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarContrato(dos, temp);
            }
        });

    }

    private void buscarPreNotas() {
        ArrayList<Nota> temp = ModuloNota.getInstancia().buscarPreNotas(getBancoDeDados());
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(temp.size());

                temp.forEach(s -> {
                    envio.enviarNotaSimples(dos, s);
                });
            }
        });

    }

    private void lancarNota(int idNota) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(ModuloNota.getInstancia().lancarNota(getBancoDeDados(), idNota, conta.getId()));
            }
        });

    }

    private void buscarNotasContrato(int id) {
        ArrayList<Nota> temp = ModuloNota.getInstancia().buscarNotasContrato(getBancoDeDados(), id);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(temp.size());

                temp.forEach(s -> {
                    envio.enviarNotaSimples(dos, s);
                });
            }
        });

    }

    private void incluirContrato() {

        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarContrato(dos, ModuloContrato.getInstancia().incluirContrato(getBancoDeDados(), conta.getId()));
            }
        });
    }

    private void incluirMapaContrato(int id) {

        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarMapaContrato(dos, ModuloContrato.getInstancia().buscarMapaContrato(getBancoDeDados(), ModuloContrato.getInstancia().incluirMapaContrato(getBancoDeDados(), conta.getId(), id)));
            }
        });

    }

    private void inativarContrato(int id) {
        Contrato temp = ModuloContrato.getInstancia().buscarContrato(getBancoDeDados(), id);
        temp.setAtivo(!temp.isAtivo());
        ModuloContrato.getInstancia().atualizarContrato(getBancoDeDados(), temp, conta.getId());
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeBoolean(temp.isAtivo());
            }
        });

    }

    private void atualizarContrato(Contrato contrato) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(ModuloContrato.getInstancia().atualizarContrato(getBancoDeDados(), contrato, conta.getId()));
            }
        });

    }

    private void atualizarMapaContrato(MapaContrato mapa) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(ModuloContrato.getInstancia().atualizarMapaContrato(getBancoDeDados(), mapa));
            }
        });

    }

    private void incluirItemContrato(int id) {
        MapaContrato temp = ModuloContrato.getInstancia().buscarMapaContrato(getBancoDeDados(), id);
        int idItem = ModuloContrato.getInstancia().incluirItemContrato(getBancoDeDados(), conta.getId(), temp.getIdContrato(), temp.getId());

        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.write(idItem);
            }
        });

    }

    private void buscarItemOrcamento(String item) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarItemOrcamento(dos, ModuloControle.getInstancia().buscarItemOrcamento(getBancoDeDados(), id));
            }
        });

    }

    private void relatorioNotas(Parametros parametro) {
        ArrayList<Nota> temp = ModuloNota.getInstancia().buscarNotasRelatorio(getBancoDeDados(), parametro);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(temp.size());

                temp.forEach(s -> {
                    envio.enviarNotaSimples(dos, s);
                });
            }
        });

    }

    private void buscarUltimosFornecedores(int limite) {
        ArrayList<Fornecedor> temp = ModuloFornecedor.getInstancia().buscarUltimosFornecedores(getBancoDeDados(), limite);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(temp.size());
                temp.forEach(s -> {
                    envio.enviarFornecedor(dos, s);
                });
            }
        });

    }

    private void buscarFornecedorCompleto(int id) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarFornecedor(dos, ModuloFornecedor.getInstancia().buscarFornecedor(getBancoDeDados(), id));
            }
        });

    }

    private void salvarFornecedor(Fornecedor fornecedor) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(0);
            }
        });

    }

    private void buscarFornecedorCampo(String campo, String valor) {
        ArrayList<Fornecedor> temp = ModuloFornecedor.getInstancia().buscarFornecedorCampo(getBancoDeDados(), conta.getId(), campo, valor);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(temp.size());
                temp.forEach(s -> {
                    envio.enviarFornecedor(dos, s);
                });
            }
        });

    }

    private void incluirFornecedor() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarFornecedor(dos, ModuloFornecedor.getInstancia().incluirFornecedor(getBancoDeDados(), conta.getId()));
            }
        });

    }

    private void buscarContratoProblema() {
        ArrayList<Contrato> temp = ModuloContrato.getInstancia().buscarContratoProblema(getBancoDeDados());
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(temp.size());
                temp.forEach(s -> {
                    envio.enviarContrato(dos, s);
                });
            }
        });

    }

    private void buscarNotaProblema() {
        ArrayList<Nota> temp = ModuloNota.getInstancia().buscarNotaProblema(getBancoDeDados());
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(temp.size());

                temp.forEach(s -> {
                    envio.enviarNotaSimples(dos, s);
                });
            }
        });

    }

    private void relatorioOrcamento(Parametros parametros) {
        ArrayList<Nota> temp = ModuloNota.getInstancia().buscarNotasRelatorioOrcamento(getBancoDeDados(), parametros);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(temp.size());

                temp.forEach(s -> {
                    envio.enviarNotaCompleta(dos, s);
                });
            }
        });

    }

    private void listarContas() {

        ArrayList<Conta> contas = ModuloConta.getInstancia().listarContas(getBancoDeDados(), conta.getId(), conta.getNivel());
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(contas.size());
                contas.forEach(s -> {
                    envio.enviarConta(dos, s);
                });
            }
        });

    }

    private void atualizarConta(Conta conta) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(ModuloConta.getInstancia().atualizarConta(getBancoDeDados(), conta, conta.getId(), conta.getNivel()));
            }
        });

    }

    private void incluirConta() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarConta(dos, ModuloConta.getInstancia().incluirConta(getBancoDeDados(), conta.getId(), conta.getNivel()));
            }
        });

    }

    private void atualizarSenhaConta(int id, String senha) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(ModuloConta.getInstancia().atualizarSenhaConta(getBancoDeDados(), id, senha, conta.getId(), conta.getNivel()));
            }
        });

    }

    private void listarItensOrcamento() {
        ArrayList<ItemOrcamento> itens = ModuloControle.getInstancia().listarItensOrcamento(getBancoDeDados());
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(itens.size());
                itens.forEach(s -> {
                    envio.enviarItemOrcamento(dos, s);
                });
            }
        });

    }

    private void incluirItemOrcamento() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(ModuloControle.getInstancia().incluirItemOrcamento(getBancoDeDados(), conta.getId()));
            }
        });

    }

    private void atualizarItemOrcamento(ItemOrcamento item) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(ModuloControle.getInstancia().atualizarItemOrcamento(getBancoDeDados(), item));
            }
        });

    }

    private void buscarHistorico(String tabela, int id) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarHistoricos(dos, ModuloUtil.getInstancia().buscarHistorico(getBancoDeDados(), tabela, id));
            }
        });

    }

    private void buscarNotasFornecedor(int id) {
        ArrayList<Nota> temp = ModuloNota.getInstancia().buscarNotasFornecedor(getBancoDeDados(), id);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(temp.size());

                temp.forEach(s -> {
                    envio.enviarNotaSimples(dos, s);
                });
            }
        });

    }

    private void anexarNota(int id, String tipo) {
        String caminho = getBancoDeDados().getNomeBanco() + "\\notas\\" + tipo + "\\" + id + "\\";
        File arquivo = leitura.lerArquivo(new File(Referencia.getInstancia().getArquivos() + caminho));
        ModuloNota.getInstancia().incluirAnexoNota(getBancoDeDados(), id, caminho + arquivo.getName(), tipo, arquivo.getName(), conta.getId());
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(1);
            }
        });

    }

    /**
     *
     * @param id
     */
    public void listarAnexosNota(int id) {
        ArrayList<AnexoNota> anexos = ModuloNota.getInstancia().listarAnexosNota(getBancoDeDados(), id);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(anexos.size());
                anexos.forEach(s -> {
                    envio.enviarAnexoNota(dos, s);
                });
            }
        });

    }

    private void abrirAnexoNota(int id) {
        File arquivo = ModuloNota.getInstancia().buscarAnexoNota(getBancoDeDados(), id);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarArquivo(dos, arquivo);
            }
        });

    }

    private void incluirPagamentoNota(int id) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {

                dos.writeInt(ModuloFinanceiro.getInstancia().incluirPagamento(getBancoDeDados(), conta.getId(), id, true));
            }
        });

    }

    private void removerPagamentoNota(Pagamento pagamento) {
        ModuloFinanceiro.getInstancia().removerPagamentoNota(getBancoDeDados(), pagamento);
    }

    private void estornarNota(Nota nota) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(ModuloNota.getInstancia().estornarNota(getBancoDeDados(), nota, conta.getId()));
            }
        });

    }

    private void listarAbertasFinanceiro() {
        ArrayList<NotaFinanceiro> notas = ModuloFinanceiro.getInstancia().listarPagamentosAbertos(getBancoDeDados());
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(notas.size());
                notas.forEach(s -> {
                    envio.enviarNotaFinanceiro(dos, s);
                });
            }
        });

    }

    private void listarBaixadasFinanceiro() {
        ArrayList<NotaFinanceiro> notas = ModuloFinanceiro.getInstancia().listarBaixadasFinanceiro(getBancoDeDados());
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(notas.size());
                notas.forEach(s -> {
                    envio.enviarNotaFinanceiro(dos, s);
                });
            }
        });

    }

    private void listarDiarios(String status) {
        ArrayList<Diario> diarios = ModuloFinanceiro.getInstancia().listarDiarios(getBancoDeDados(), status);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(diarios.size());
                diarios.forEach(s -> {
                    envio.enviarDiarioSimples(dos, s);
                });
            }
        });

    }

    private void incluirDiario() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarDiarioSimples(dos, ModuloFinanceiro.getInstancia().incluirDiario(getBancoDeDados()));
            }
        });

    }

    private void incluirNotaEmDiario(int idNotaFinanceiro, int idDiario) {
        ModuloFinanceiro.getInstancia().incluirNotaEmDiario(getBancoDeDados(), idNotaFinanceiro, idDiario);
    }

    private void buscarDiario(int id) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarDiarioCompleto(dos, ModuloFinanceiro.getInstancia().buscarDiario(getBancoDeDados(), id));
            }
        });

    }

    private void excluirNotaEmDiario(int idNotaFinanceiro, int idDiario) {
        ModuloFinanceiro.getInstancia().excluirNotaEmDiario(getBancoDeDados(), idNotaFinanceiro, idDiario);
    }

    private void atualizarDescontoNotaFinanceiro(int id, long valor) {
        ModuloFinanceiro.getInstancia().atualizarDescontoNotaFinanceiro(getBancoDeDados(), id, valor);
    }

    /**
     *
     * @param id
     * @param juros
     */
    public void atualizarJurosNotaFinanceiro(int id, long juros) {
        ModuloFinanceiro.getInstancia().atualizarJurosNotaFinanceiro(getBancoDeDados(), id, juros);
    }

    /**
     *
     * @param idNota
     * @param tipo
     */
    public void atualizarTipoNotaFinanceiro(int idNota, String tipo) {
        ModuloFinanceiro.getInstancia().atualizarTipoNotaFinanceiro(getBancoDeDados(), idNota, tipo);
    }

    /**
     *
     * @param idNota
     * @param linha
     */
    public void atualizarLinhaNotaFinanceiro(int idNota, String linha) {
        ModuloFinanceiro.getInstancia().atualizarLinhaNotaFinanceiro(getBancoDeDados(), idNota, linha);
    }

    /**
     *
     * @param idNota
     * @param codigo
     */
    public void atualizarCodigoNotaFinanceiro(int idNota, String codigo) {
        ModuloFinanceiro.getInstancia().atualizarCodigoNotaFinanceiro(getBancoDeDados(), idNota, codigo);
    }

    /**
     *
     * @param id
     * @param valor
     */
    public void atualizarValorLiquidoNotaFinanceiro(int id, long valor) {
        ModuloFinanceiro.getInstancia().atualizarValorLiquidoNotaFinanceiro(getBancoDeDados(), id, valor);
    }

    private void salvarDiario(Diario diario) {
        ModuloFinanceiro.getInstancia().salvarDiario(getBancoDeDados(), diario);
    }

    private void postarDiario(int id) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(ModuloFinanceiro.getInstancia().postarDiario(getBancoDeDados(), id));
            }
        });

    }

    private void listarNotasDividir(int id) {
        ArrayList<NotaFinanceiro> notas = ModuloFinanceiro.getInstancia().listarNotasDividir(getBancoDeDados(), id);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(notas.size());
                notas.forEach(s -> {
                    envio.enviarNotaFinanceiro(dos, s);
                });
            }
        });

    }

    private void buscarValorLiquidoNota(int id) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeLong(ModuloFinanceiro.getInstancia().buscarValorLiquidoNota(getBancoDeDados(), id));
            }
        });

    }

    private void atualizarValorFianceiro(int nota, long valor) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(ModuloFinanceiro.getInstancia().atualizarValorFinanceiro(getBancoDeDados(), nota, valor));
            }
        });

    }

    private void listarContratosFornecedor(int idFornecedor) {
        ArrayList<Contrato> temp = ModuloContrato.getInstancia().listarContratosFornecedor(getBancoDeDados(), idFornecedor);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(temp.size());
                temp.forEach(s -> {
                    envio.enviarContrato(dos, s);
                });
            }
        });

    }

    private void listarPagamentosFornecedor(int idFornecedor, boolean baixada) {
        ArrayList<NotaFinanceiro> notas = ModuloFinanceiro.getInstancia().listarPagamentosFornecedor(getBancoDeDados(), idFornecedor, baixada);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(notas.size());
                notas.forEach(s -> {
                    envio.enviarNotaFinanceiro(dos, s);
                });
            }
        });

    }

    private void incluirContatoFornecedor(int idFornecedor) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(ModuloFornecedor.getInstancia().incluirContatoFornecedor(getBancoDeDados(), idFornecedor, conta.getId()));
            }
        });

    }

    private void listarContatoFornecedor(int idFornecedor) {
        ArrayList<Contato> contatos = ModuloFornecedor.getInstancia().listarContatoFornecedor(getBancoDeDados(), idFornecedor);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(contatos.size());
                contatos.forEach(s -> {
                    envio.enviarContato(dos, s);
                });
            }
        });

    }

    private void verificarNumeroNota(int idFornecedor, int idNota, String numero) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeBoolean(ModuloNota.getInstancia().verificarNumeroNota(getBancoDeDados(), idFornecedor, idNota, numero));
            }
        });

    }

    private void cancelarNota(int idNota) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeBoolean(ModuloNota.getInstancia().cancelarNota(getBancoDeDados(), conta.getId(), idNota));
            }
        });

    }

    private void listarFluxo() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarArrayFluxo(dos, ModuloFinanceiro.getInstancia().listarFluxo(getBancoDeDados()));
            }
        });

    }

    private void listarNotasSubmetidas() {
        ArrayList<Nota> temp = ModuloNota.getInstancia().listarNotasSubmetidas(getBancoDeDados());
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(temp.size());

                temp.forEach(s -> {
                    envio.enviarNotaSimples(dos, s);
                });
            }
        });

    }

    private void workflowNota(int idPMWeb, String status) {
        Status temp = new Status();
        temp.setId(idPMWeb);
        temp.setStatus(status);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(ModuloNota.getInstancia().workflow(getBancoDeDados(), temp, conta.getId()));
            }
        });

    }

    private void buscarFluxoDetalhado(Parametros parametros) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                envio.enviarArrayFluxo(dos, ModuloFinanceiro.getInstancia().buscarFluxoDetalhado(getBancoDeDados(), parametros));
            }
        });

    }

    private void listarNotasDiario(int idDiario) {
        ArrayList<Nota> temp = ModuloFinanceiro.getInstancia().listarNotasDiario(getBancoDeDados(), idDiario);
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(temp.size());

                temp.forEach(s -> {
                    envio.enviarNotaCompleta(dos, s);
                });
            }
        });
    }

    private void avaliar(int avaliacao, String assunto, String campo) {
        new Rotina.Email.EMail().enviarSugestao(avaliacao, assunto, campo, conta.getNome());
    }

    private void buscarNotasDiario(int id) {
        sendData(new Request() {

            @Override
            public void run() throws IOException {
                ArrayList<NotaFinanceiro> temp = ModuloFinanceiro.getInstancia().buscarNotasDiario(getBancoDeDados(), id);
                dos.writeInt(temp.size());
                temp.forEach(s -> {
                    envio.enviarNotaFinanceiro(dos, s);
                });
            }
        });
    }

    private void buscarUltimosProdutosAlmoxarife(int limite) {

        sendData(new Request() {

            @Override
            public void run() throws IOException {
                ArrayList<ProdutoAlmoxarife> produtos = ModuloProduto.getInstancia().buscarUltimosProdutosAlmoxarife(getBancoDeDados(), limite);

                dos.writeInt(produtos.size());
                produtos.forEach(s -> {
                    envio.enviarProdutoAlmoxarife(dos, s);
                });
            }
        });
    }

    private void incluirProdutoAlmoxarife() {
        sendData(new Request() {

            @Override
            public void run() throws IOException {
                envio.enviarProdutoAlmoxarife(dos, ModuloProduto.getInstancia().incluirProdutoAlmoxarife(getBancoDeDados(), conta.getId()));
            }
        });
    }
}
