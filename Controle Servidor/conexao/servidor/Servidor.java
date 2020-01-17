/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexao.Servidor;

import Conexao.Servidor.Cliente.Cliente;
import Configuracao.Referencia;
import GUI.principal.Principal;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author Guilherme
 */
public enum Servidor {

    /**
     *
     */
    INSTANCIA;

    private SSLServerSocket socket;
    private boolean executando = false;


    /*
     Guarda o ip + conexao
     */
    private ArrayList<Cliente> clientes = new ArrayList<>();

    /**
     *
     */
    public void iniciar() {
        new Thread(() -> INSTANCIA.configurar()).start();
    }

    private void configurar() {
        if (executando) {
            return;
        }
        
        Principal.setStatus("Criando conexao");
        System.setProperty("javax.net.ssl.keyStore", "C:\\Program Files\\Java\\jdk1.8.0_45\\jre\\lib\\security\\cacerts");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
        try {
            
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, null, null);
            
            SSLServerSocketFactory ssf = context.getServerSocketFactory();
            socket = (SSLServerSocket) ssf.createServerSocket(Integer.valueOf(Referencia.getInstancia().getPorta()));
            socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());
            Principal.setStatus("Ip: " + InetAddress.getLocalHost().getHostAddress() + ":" + socket.getLocalPort()
                    + "\nNome: " + InetAddress.getLocalHost().getHostName() + "\nConfigurando servidor");
            aguardarConexao();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     *
     * @param arg0
     */
    public void addCliente(Cliente arg0) {
        clientes.add(arg0);
        atualizarInfo();
    }

    /**
     *
     * @param arg0
     */
    public void remove(Cliente arg0) {
        if (clientes.contains(arg0)) {
            clientes.remove(arg0);
        }
        atualizarInfo();
    }

    private void atualizarInfo() {
        Principal.getInstancia().pessoas.setText("Pessoas conectadas: " + clientes.size());
    }

    private void aguardarConexao() {
        executando = true;
        atualizarInfo();
        while (executando) {
            Principal.setStatus("Aguardando conexao.");
            try {

                Cliente cliente = new Cliente((SSLSocket)socket.accept());
                cliente.start();
                addCliente(cliente);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     *
     */
    public void fechar() {
        Principal.setStatus("Fechando conexões");
        executando = false;

        clientes.forEach(s -> {
            s.fechar();
        });

        if (socket != null) {
            try {
                socket.close();
                Principal.getInstancia().pessoas.setText("");
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
