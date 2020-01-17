/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexao;

import Conexao.protocolo.envio.Envio;
import Conexao.protocolo.leitura.Leitura;
import GuiPrincipal.Main;
import GuiPrincipal.Util.Erro;
import Util.Historico;
import Util.HistoricoPesquisa;
import Util.ItemOrcamento;
import Util.Parametros;
import Util.Referencia;
import classes.almoxarife.ProdutoAlmoxarife;
import classes.conta.Conta;
import classes.contrato.Contrato;
import classes.contrato.mapa.MapaContrato;
import classes.financeiro.Diario;
import classes.financeiro.Fluxo;
import classes.financeiro.NotaFinanceiro;
import classes.fornecedor.Fornecedor;
import classes.fornecedor.contato.Contato;
import classes.nota.Nota;
import classes.nota.anexo.AnexoNota;
import classes.nota.cobranca.Pagamento;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author Guilherme
 */
public enum Cliente {

    INSTANCIA;

    private SSLSocket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private SimpleDateFormat sdf;
    private String sobre;
    private Timer time = new Timer();
    private long ultimo;
    private Envio envio;
    private Leitura leitura;
    /*
     3582 teste
     3584 versao final
     */
    private final int PORTA = 3582;

    public boolean liberado = false, alive, enviando;

    public String getSerial() {
        String serial = executarComando("wmic diskdrive get serialnumber");
        String serial2 = executarComando("wmic baseboard get serialnumber");
        String serial3 = executarComando("wmic bios get serialnumber");
        String serial4 = serial + serial2 + serial3;
        return criptrografar(serial4);
    }

    private String executarComando(String comando) {
        String retorno = "";
        try {
            Process p = Runtime.getRuntime().exec(comando);
            BufferedReader input2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String linha;
            input2.readLine();
            while ((linha = input2.readLine()) != null) {
                retorno += linha;
            }
            input2.close();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public void iniciar() {
        alive = true;
        try {
            connectToServer();
            getStreams();
            gerarProtocolos();
            processConnection();
            enviarOk();
            manterConexao();

        } catch (SocketException e) {
            liberado = false;
            System.out.println("Nao foi possivel conectar");
            //erro("Nao foi possivel conectar", true);
            Erro.getInstancia().setErro(e);
        } catch (IOException ex) {
            liberado = false;
            System.out.println("Nao foi possivel conectar");
        } catch (UnsupportedOperationException ex) {
            liberado = false;
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }
    }

    private void connectToServer() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, null, null);

        SSLSocketFactory sf = context.getSocketFactory();

        socket = (SSLSocket) sf.createSocket(Referencia.getInstancia().getIp(), PORTA);
        socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());
        //socket = new Socket("187.20.95.127", 3584);
        socket.setSoTimeout(3600000);
        socket.setKeepAlive(true);

        System.out.println("Conectou com " + socket.getInetAddress().getHostName());

    }

    private void getStreams() throws IOException {
        output = new DataOutputStream(socket.getOutputStream());
        output.flush();

        input = new DataInputStream(socket.getInputStream());

        System.out.println("Got I/O Streams");
    }

    private void gerarProtocolos() {
        envio = new Envio();
        leitura = new Leitura(input);
    }

    private void processConnection() throws IOException {

        do {
            liberado = input.readBoolean();
            System.out.println(liberado);
            sobre = leitura.lerString();
            if (!liberado) {
                return;
            }
        } while (!liberado);

    }

    private void manterConexao() {
        int tempo = 1;
        int periodo = (1000 * 30);

        time.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                long tempo = new Date().getTime();
                long diferença = tempo - ultimo;
                if (diferença > ((1000 * 60) * 2)) {
                    if (!enviando) {
                        sendData(new Request() {
                            @Override
                            public void run() throws IOException {
                                dos.writeInt(-1);
                            }
                        });

                    }
                }
            }
        }, tempo, periodo);
    }

    private synchronized void sendData(Request mensagem) {
        try {
            enviando = true;
            output.write(mensagem.getMensagem());
            output.flush();
            ultimo = new Date().getTime();
            enviando = false;
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Sem conexao");
        }

    }

    private void verificar(boolean arg0) {
        liberado = arg0;
    }

    public String getSobre() {
        return sobre;
    }

    public boolean liberado() {
        return liberado;
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

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
     metodos auxiliares
     */
    public void enviarOk() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(0);
            }
        });

    }

    public boolean login(String login, String senha, String banco) {
        boolean retorno = false;
        try {
            if (login.isEmpty() || senha.isEmpty() || banco.isEmpty()) {
                return false;
            }
            if (!liberado()) {
                Main.getInstancia().getAtualControler().setStatus("Não liberado");
                return false;
            }
            sendData(new Request() {
                @Override
                public void run() throws IOException {

                    dos.writeInt(1);
                    envio.enviarString(dos, login);
                    envio.enviarString(dos, criptrografar(senha));
                    envio.enviarString(dos, banco);
                }
            });

            retorno = input.readBoolean();

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;

    }

    public String getNomeConta() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(3);
            }
        });

        return leitura.lerString();

    }

    public Conta getNivelConta() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(4);
            }
        });
        Conta conta = new Conta();
        leitura.lerConta(conta);
        return conta;
    }

    public void fechar() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(5);
            }
        });

        Cliente.INSTANCIA.alive = false;
        System.exit(0);
    }

    public Nota incluirNota(int idFornecedor, int idContrato) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(6);
                dos.writeInt(idFornecedor);
                dos.writeInt(idContrato);
            }
        });
        Nota nota = new Nota();
        leitura.lerNotaCompleto(nota);
        return nota;
    }

    public HistoricoPesquisa buscarCampos(String tabela) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(9);
                envio.enviarString(dos, tabela);
            }
        });
        HistoricoPesquisa historico = new HistoricoPesquisa();
        leitura.lerHistoricoPesquisa(historico);
        return historico;
    }

    public ArrayList<Nota> buscarNota(String campo, String valor) {
        ArrayList<Nota> notas = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(10);
                    envio.enviarString(dos, campo);
                    envio.enviarString(dos, valor);
                }
            });

            int tamanho = input.readInt();

            for (int i = 0; i < tamanho; i++) {
                Nota nota = new Nota();
                leitura.lerNotaSimples(nota);
                notas.add(nota);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    public ArrayList<Nota> getUltimasNotas(int limite) {
        ArrayList<Nota> notas = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(11);
                    dos.writeInt(limite);
                }
            });

            int tamanho = input.readInt();

            for (int i = 0; i < tamanho; i++) {
                Nota nota = new Nota();
                leitura.lerNotaSimples(nota);
                notas.add(nota);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    public Nota buscarNota(int id) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(12);
                dos.writeInt(id);
            }
        });
        Nota nota = new Nota();
        leitura.lerNotaCompleto(nota);
        return nota;
    }

    public ArrayList<Contrato> buscarContratoSimples(String campo, String valor) {
        ArrayList<Contrato> contratos = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(13);
                    envio.enviarString(dos, campo);
                    envio.enviarString(dos, valor);
                }
            });

            int tamanho = input.readInt();

            for (int i = 0; i < tamanho; i++) {
                Contrato contrato = new Contrato();
                leitura.lerContrato(contrato);
                contratos.add(contrato);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contratos;
    }

    public ArrayList<Contrato> getUltimosContratos(int limite) {
        ArrayList<Contrato> contratos = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(14);
                    dos.writeInt(limite);
                }
            });

            int tamanho = input.readInt();

            for (int i = 0; i < tamanho; i++) {
                Contrato contrato = new Contrato();
                leitura.lerContrato(contrato);
                contratos.add(contrato);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contratos;
    }

    public Contrato buscarcontratoCompleto(int ID) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(15);
                dos.writeInt(ID);
            }
        });
        Contrato contrato = new Contrato();
        leitura.lerContrato(contrato);
        return contrato;
    }

    public ArrayList<Nota> buscarPreNotas() {
        ArrayList<Nota> notas = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(16);
                }
            });

            int tamanho = input.readInt();

            for (int i = 0; i < tamanho; i++) {
                Nota nota = new Nota();
                leitura.lerNotaSimples(nota);
                notas.add(nota);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    public String getString(String tabela, String campo, int id) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(26);
                envio.enviarString(dos, tabela);
                envio.enviarString(dos, campo);
                dos.writeInt(id);
            }
        });

        return leitura.lerString();
    }

    public int getInt(String tabela, String campo, int id) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(27);
                    envio.enviarString(dos, tabela);
                    envio.enviarString(dos, campo);
                    dos.writeInt(id);
                }
            });

            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public long getLong(String tabela, String campo, int id) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(28);
                    envio.enviarString(dos, tabela);
                    envio.enviarString(dos, campo);
                    dos.writeInt(id);
                }
            });

            return input.readLong();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public boolean getBoolean(String tabela, String campo, int id) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(29);
                    envio.enviarString(dos, tabela);
                    envio.enviarString(dos, campo);
                    dos.writeInt(id);
                }
            });

            return input.readBoolean();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

   public int setString(String tabela, String campo, int id, String arg) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(30);
                    envio.enviarString(dos, tabela);
                    envio.enviarString(dos, campo);
                    dos.writeInt(id);
                    envio.enviarString(dos, arg);
                }
            });

            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public int setInt(String tabela, String campo, int id, int arg) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(31);
                    envio.enviarString(dos, tabela);
                    envio.enviarString(dos, campo);
                    dos.writeInt(id);
                    dos.writeInt(arg);
                }
            });

            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public int setLong(String tabela, String campo, int id, long arg) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(32);
                    envio.enviarString(dos, tabela);
                    envio.enviarString(dos, campo);
                    dos.writeInt(id);
                    dos.writeLong(arg);
                }
            });

            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public int setFloat(String tabela, String campo, int id, float arg) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(54);
                    envio.enviarString(dos, tabela);
                    envio.enviarString(dos, campo);
                    dos.writeInt(id);
                    dos.writeFloat(arg);
                }
            });

            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public int setBoolean(String tabela, String campo, int id, boolean arg) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(33);
                    envio.enviarString(dos, tabela);
                    envio.enviarString(dos, campo);
                    dos.writeInt(id);
                    dos.writeBoolean(arg);
                }
            });

            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public int setDate(String tabela, String campo, int id, Date arg) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(34);
                    envio.enviarString(dos, tabela);
                    envio.enviarString(dos, campo);
                    dos.writeInt(id);
                    dos.writeLong(arg.getTime());
                }
            });

            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public Date getDate(String tabela, String campo, int id) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(35);
                    envio.enviarString(dos, tabela);
                    envio.enviarString(dos, campo);
                    dos.writeInt(id);
                }
            });

            return new Date(input.readLong());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return new Date();
        }
    }

    public ArrayList<String> buscarBancos() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(37);
            }
        });

        return leitura.lerArrayString();
    }

    public int lancarNota(int idNota) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(17);
                    dos.writeInt(idNota);
                }
            });

            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public ArrayList<Nota> buscarNotasContrato(int id) {
        ArrayList<Nota> notas = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(18);
                    dos.writeInt(id);
                }
            });

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                Nota nota = new Nota();
                leitura.lerNotaSimples(nota);
                notas.add(nota);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    public Contrato incluirContrato() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(19);
            }
        });
        Contrato contrato = new Contrato();
        leitura.lerContrato(contrato);
        return contrato;
    }

    public MapaContrato incluirMapacontrato(int id) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(20);
                dos.writeInt(id);
            }
        });
        MapaContrato mapa = new MapaContrato();
        leitura.lerMapaContrato(mapa);
        return mapa;
    }

    public boolean inativarContrato(int id) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(21);
                    dos.writeInt(id);
                }
            });

            return input.readBoolean();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    

    public int incluirItemContrato(int id) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(24);
                    dos.writeInt(id);
                }
            });           
            
            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public ItemOrcamento buscarItemOrcamento(String arg0) {

        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(25);
                envio.enviarString(dos, arg0);
            }
        });
        ItemOrcamento item = new ItemOrcamento();
        leitura.lerItemOrcamento(item);
        return item;
    }

    public ArrayList<Nota> relatorioNota(Parametros parametro) {
        ArrayList<Nota> notas = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(36);
                    envio.enviarParametrosNota(dos, parametro);
                }
            });

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                Nota nota = new Nota();
                leitura.lerNotaSimples(nota);
                notas.add(nota);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    public ArrayList<Fornecedor> buscarUltimosFornecedores(int limite) {
        ArrayList<Fornecedor> fornecedores = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(38);
                    dos.writeInt(limite);
                }
            });

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                Fornecedor fornecedor = new Fornecedor();
                leitura.lerFornecedor(fornecedor);
                fornecedores.add(fornecedor);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fornecedores;
    }

    public Fornecedor buscarFornecedorCompleto(int id) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(39);
                dos.writeInt(id);
            }
        });
        Fornecedor fornecedor = new Fornecedor();
        leitura.lerFornecedor(fornecedor);
        return fornecedor;
    }
    

    public ArrayList<Fornecedor> buscarFornecedorCampo(String campo, String valor) {
        ArrayList<Fornecedor> fornecedores = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(41);
                    envio.enviarString(dos, campo);
                    envio.enviarString(dos, valor);
                }
            });

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                Fornecedor fornecedor = new Fornecedor();
                leitura.lerFornecedor(fornecedor);
                fornecedores.add(fornecedor);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fornecedores;
    }

    public Fornecedor incluirFornecedor() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(42);
            }
        });
        Fornecedor fornecedor = new Fornecedor();
        leitura.lerFornecedor(fornecedor);
        return fornecedor;
    }

    public ArrayList<Contrato> buscarContratoProblema() {
        ArrayList<Contrato> contratos = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(43);
                }
            });

            int tamanho = input.readInt();

            for (int i = 0; i < tamanho; i++) {
                Contrato contrato = new Contrato();
                leitura.lerContrato(contrato);
                contratos.add(contrato);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contratos;
    }

    public ArrayList<Nota> buscarNotaProblema() {
        ArrayList<Nota> notas = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(44);
                }
            });

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                Nota nota = new Nota();
                leitura.lerNotaSimples(nota);
                notas.add(nota);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    public ArrayList<Nota> relatorioOrcamento(Parametros parametro) {
        ArrayList<Nota> notas = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(45);
                    envio.enviarParametrosNota(dos, parametro);
                }
            });

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                Nota nota = new Nota();
                leitura.lerNotaCompleto(nota);
                notas.add(nota);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    public ArrayList<Conta> listarContas() {
        ArrayList<Conta> contas = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(46);
                }
            });

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                Conta conta = new Conta();
                leitura.lerConta(conta);
                contas.add(conta);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contas;
    }
    

    public Conta incluirConta() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(48);
            }
        });
        Conta conta = new Conta();
        leitura.lerConta(conta);
        return conta;
    }

    public int atualizarSenhaConta(int id, String senha) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(49);
                    dos.writeInt(id);
                    envio.enviarString(dos, criptrografar(senha));
                }
            });

            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -2;
    }

    public ArrayList<ItemOrcamento> listarItensOrcamento() {
        ArrayList<ItemOrcamento> itens = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(50);
                }
            });

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                ItemOrcamento item = new ItemOrcamento();
                leitura.lerItemOrcamento(item);
                itens.add(item);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itens;
    }

    public int incluirItemOrcamento() {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(51);
                }
            });
            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    

    public ArrayList<Historico> buscarHistorico(String tabela, int id) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(53);
                envio.enviarString(dos, tabela);
                dos.writeInt(id);
            }
        });

        return leitura.lerHistoricos();
    }

    public ArrayList<Nota> buscarNotasFornecedor(int id) {
        ArrayList<Nota> notas = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(56);
                    dos.writeInt(id);
                }
            });

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                Nota nota = new Nota();
                leitura.lerNotaSimples(nota);
                notas.add(nota);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    public int anexarNota(int id, File arquivo, String tipo) {

        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(57);
                    dos.writeInt(id);
                    envio.enviarString(dos, tipo);
                    envio.enviarArquivo(dos, arquivo);
                }
            });

            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public ArrayList<AnexoNota> listarAnexosNota(int id) {
        ArrayList<AnexoNota> anexos = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(58);
                    dos.writeInt(id);
                }
            });

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                AnexoNota anexo = new AnexoNota();
                leitura.lerAnexoNota(anexo);
                anexos.add(anexo);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return anexos;
    }

    public File lerAnexoNota(int id, File caminho) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(59);
                dos.writeInt(id);
            }
        });

        return leitura.lerArquivo(caminho);
    }

    public int incluirPagamentoNota(int id) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(60);
                    dos.writeInt(id);
                }
            });            
            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public void removerPagamentoNota(Pagamento pagamento) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(61);
                dos.writeInt(pagamento.getId());
            }
        });

    }

    public int estornarNota(int id) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(62);
                    dos.writeInt(id);
                }
            });

            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public ArrayList<NotaFinanceiro> listarAbertasFinanceiro() {
        ArrayList<NotaFinanceiro> notas = new ArrayList<>();

        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(63);
                }
            });

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                NotaFinanceiro nota = new NotaFinanceiro();
                leitura.lerNotaFinanceiro(nota);
                notas.add(nota);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    public ArrayList<NotaFinanceiro> listarBaixadasFinanceiro() {
        ArrayList<NotaFinanceiro> notas = new ArrayList<>();

        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(64);
                }
            });

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                NotaFinanceiro nota = new NotaFinanceiro();
                leitura.lerNotaFinanceiro(nota);
                notas.add(nota);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    public ArrayList<Diario> listarDiarios(String status) {
        ArrayList<Diario> diarios = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(65);
                    envio.enviarString(dos, status);
                }
            });

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                Diario diario = new Diario();
                leitura.lerDiarioSimples(diario);
                diarios.add(diario);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return diarios;
    }

    public Diario incluirDiario() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(66);
            }
        });
        Diario diario = new Diario();
        leitura.lerDiarioSimples(diario);
        return diario;
    }

    public void incluirNotaEmDiario(int idNotaFinanceiro, int idDiario) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(67);
                dos.writeInt(idNotaFinanceiro);
                dos.writeInt(idDiario);
            }
        });

    }

    public Diario buscarDiario(int id) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(68);
                dos.writeInt(id);
            }
        });
        Diario diario = new Diario();
        leitura.lerDiarioCompleto(diario);
        return diario;
    }

    public void excluirNotaEmDiario(int idNotaFinanceiro, int idDiario) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(69);
                dos.writeInt(idNotaFinanceiro);
                dos.writeInt(idDiario);
            }
        });

    }

    public void atualizarDescontoNotaFinanceiro(int id, long valor) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(70);
                dos.writeInt(id);
                dos.writeLong(valor);
            }
        });

    }

    public void atualizarJurosNotaFinanceiro(int id, long juros) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(71);
                dos.writeInt(id);
                dos.writeLong(juros);
            }
        });

    }

    public void atualizarTipoNotaFinanceiro(int idNota, String tipo) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(72);
                dos.writeInt(idNota);
                envio.enviarString(dos, tipo);
            }
        });

    }

    public void atualizarLinhaNotaFinanceiro(int idNota, String linha) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(73);
                dos.writeInt(idNota);
                envio.enviarString(dos, linha);
            }
        });

    }

    public void atualizarCodigoNotaFinanceiro(int idNota, String codigo) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(74);
                dos.writeInt(idNota);
                envio.enviarString(dos, codigo);
            }
        });

    }

    public void atualizarValorLiquidoNotaFinanceiro(int idNota, long valor) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(75);
                dos.writeInt(idNota);
                dos.writeLong(valor);
            }
        });

    }

    public int postarDiario(int id) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(77);
                    dos.writeInt(id);
                }
            });

            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public ArrayList<NotaFinanceiro> listarNotasDividir(int idNota) {
        ArrayList<NotaFinanceiro> notas = new ArrayList<>();

        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(78);
                    dos.writeInt(idNota);
                }
            });

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                NotaFinanceiro nota = new NotaFinanceiro();
                leitura.lerNotaFinanceiro(nota);
                notas.add(nota);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    public long buscarValorLiquidoNota(int idNota) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(79);
                    dos.writeInt(idNota);
                }
            });

            return input.readLong();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int atualizarValorFianceiro(int nota, long valor) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(80);
                    dos.writeInt(nota);
                    dos.writeLong(valor);
                }
            });

            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public ArrayList<Contrato> listarContratosFornecedor(int idFornecedor) {
        ArrayList<Contrato> contratos = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(81);
                    dos.writeInt(idFornecedor);
                }
            });

            int tamanho = input.readInt();

            for (int i = 0; i < tamanho; i++) {
                Contrato contrato = new Contrato();
                leitura.lerContrato(contrato);
                contratos.add(contrato);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contratos;
    }

    public ArrayList<NotaFinanceiro> listarPagamentosFornecedor(int idFornecedor, boolean baixadas) {
        ArrayList<NotaFinanceiro> notas = new ArrayList<>();

        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(82);
                    dos.writeInt(idFornecedor);
                    dos.writeBoolean(baixadas);
                }
            });

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                NotaFinanceiro nota = new NotaFinanceiro();
                leitura.lerNotaFinanceiro(nota);
                notas.add(nota);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    public int incluirContatoFornecedor(int idFornecedor) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(83);
                    dos.writeInt(idFornecedor);
                }
            });
            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public ArrayList<Contato> listarContatoFornecedor(int idFornecedor) {
        ArrayList<Contato> contatos = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(84);
                    dos.writeInt(idFornecedor);
                }
            });

            int tamanho = input.readInt();

            for (int i = 0; i < tamanho; i++) {
                Contato contato = new Contato();
                leitura.lerContato(contato);
                contatos.add(contato);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contatos;
    }

    public boolean verificarNumeroNota(int idFornecedor, int idNota, String numero) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(55);
                    dos.writeInt(idFornecedor);
                    dos.writeInt(idNota);
                    envio.enviarString(dos, numero);
                }
            });

            return input.readBoolean();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean cancelarNota(int idNota) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(85);
                    dos.writeInt(idNota);
                }
            });

            return input.readBoolean();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public ArrayList<Fluxo> listarFluxo() {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(86);
            }
        });

        return leitura.lerArrayFluxo();
    }

    public ArrayList<Nota> listarNotasSubmetidas() {
        ArrayList<Nota> notas = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(87);
                }
            });

            int tamanho = input.readInt();

            for (int i = 0; i < tamanho; i++) {
                Nota nota = new Nota();
                leitura.lerNotaSimples(nota);
                notas.add(nota);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    public int workflowNota(int idPMWeb, String status) {
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(88);
                    dos.writeInt(idPMWeb);
                    envio.enviarString(dos, status);
                }
            });

            return input.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public ArrayList<Fluxo> buscarFluxoDetalhado(Parametros parametros) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(89);
                envio.enviarParametrosNota(dos, parametros);
            }
        });

        return leitura.lerArrayFluxo();
    }

    public ArrayList<Nota> listarNotasDiario(int idDiario) {
        ArrayList<Nota> notas = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(90);
                    dos.writeInt(idDiario);
                }
            });

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                Nota nota = new Nota();
                leitura.lerNotaCompleto(nota);
                notas.add(nota);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    public void avaliar(int avaliacao, String assunto, String campo) {
        sendData(new Request() {
            @Override
            public void run() throws IOException {
                dos.writeInt(91);
                dos.writeInt(avaliacao);
                envio.enviarString(dos, assunto);
                envio.enviarString(dos, campo);
            }
        });

    }

    public ArrayList<NotaFinanceiro> buscarNotasDiario(int id) {
        sendData(new Request() {

            @Override
            public void run() throws IOException {
                dos.writeInt(92);
                dos.writeInt(id);
            }
        });

        return leitura.lerNotasFinanceiro();
    }

    public ArrayList<ProdutoAlmoxarife> buscarUltimosProdutosAlmoxarife(int limite) {
        ArrayList<ProdutoAlmoxarife> produtos = new ArrayList<>();
        try {
            sendData(new Request() {
                @Override
                public void run() throws IOException {
                    dos.writeInt(93);
                    dos.writeInt(limite);
                }
            });

            int tamanho = input.readInt();
            for (int i = 0; i < tamanho; i++) {
                ProdutoAlmoxarife produto = new ProdutoAlmoxarife();
                leitura.lerProdutoAlmoxarife(produto);
                produtos.add(produto);
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return produtos;
    }

    public ProdutoAlmoxarife incluirProdutoAlmoxarife() {
        sendData(new Request() {

            @Override
            public void run() throws IOException {
                dos.writeInt(94);
            }
        });
        ProdutoAlmoxarife produto = new ProdutoAlmoxarife();
        leitura.lerProdutoAlmoxarife(produto);
        return produto;
    }

}
