/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracao;

import BancoDados.Banco;
import BancoDados.Conexao;
import GUI.principal.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guilherme.machado
 */
public enum Configuracao {

    /**
     *
     */
    INSTANCIA;

    Connection connection = null;
    private boolean executando = false;

    /**
     *
     */
    public void iniciar() {
        if (executando) {
            return;
        }
        try {
            Principal.getInstancia().progressoBarra.setValue(0);
            Principal.setStatus("Carregando configuração");
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());

            String dburl = "jdbc:derby:" + Referencia.getInstancia().getConfiguracao() + ";";

            connection = DriverManager.getConnection(dburl, "", "");

            Principal.setStatus("Configurações carregadas");
            executando = true;
            Principal.getInstancia().progressoBarra.setValue(100);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            Principal.setStatus(ex.getMessage());
        }
    }

    //INSERT INTO ROOT.BANCOS (ID, CAMINHO, USUARIO, SENHA, NOME, ATIVO) 

    /**
     *
     * @return
     */
        public ArrayList<Banco> getBancos() {
        ArrayList<Banco> bancos = new ArrayList<>();
        try {

            String sql = "SELECT ID, CAMINHO, USUARIO, SENHA, NOME, ATIVO,PMWEB FROM BANCOS ORDER BY ID DESC";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                do {
                    bancos.add(new Banco(resultSet.getInt("ID"), resultSet.getString("CAMINHO"), resultSet.getString("USUARIO"), resultSet.getString("SENHA"),
                            resultSet.getString("NOME"), resultSet.getBoolean("ATIVO"),resultSet.getBoolean("PMWEB")));
                } while (resultSet.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Configuracao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bancos;
    }

    /**
     *
     * @param banco
     */
    public void addBanco(Banco banco) {
        try {
            String sql = "INSERT INTO ROOT.BANCOS (ID, CAMINHO, USUARIO, SENHA, NOME, ATIVO,PMWEB) "
                    + "VALUES (?, ?, ?,?,?,?,?)";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setInt(1, getUltimoId("BANCOS"));
            prepare.setString(2, banco.getCAMINHO());
            prepare.setString(3, banco.getUSUARIO());
            prepare.setString(4, banco.getSENHA());
            prepare.setString(5, banco.getNOME());
            prepare.setBoolean(6, banco.isATIVO());
            prepare.setBoolean(7, banco.isPMWEB());

            prepare.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param banco
     */
    public void atualizarBanco(Banco banco) {
        try {
            String sql = "UPDATE ROOT.BANCOS SET CAMINHO = ?, USUARIO = ?, SENHA = ?, NOME = ?, ATIVO = ?,PMWEB = ? WHERE ID = ?";
            PreparedStatement prepare = connection.prepareStatement(sql);

            prepare.setString(1, banco.getCAMINHO());
            prepare.setString(2, banco.getUSUARIO());
            prepare.setString(3, banco.getSENHA());
            prepare.setString(4, banco.getNOME());
            prepare.setBoolean(5, banco.isATIVO());
            prepare.setBoolean(6, banco.isPMWEB());
            prepare.setInt(7, banco.getID());

            prepare.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param tabela
     * @return
     * @throws SQLException
     */
    public int getUltimoId(String tabela) throws SQLException {
        int i = 1;
        String sql = "SELECT ID FROM " + tabela + " WHERE ID = (SELECT MAX(ID) FROM " + tabela + ")";

        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        if (resultSet.next()) {
            i = resultSet.getInt("ID");
            ++i;
        }
        return i;
    }

    /**
     *
     * @param evento
     * @param ip
     */
    public void salvarEvento(String evento, String ip) {
        try {
            String sql = "INSERT INTO ROOT.EVENTOS (ID, HORA, EVENTO, IP) "
                    + " VALUES (?, ?, ?, ?)";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setInt(1, getUltimoId("EVENTOS"));
            prepare.setLong(2, new Date().getTime());
            prepare.setString(3, evento);
            prepare.setString(4, ip);
            
            prepare.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Configuracao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     *
     */
    public void fechar() {
        try {
            if (connection != null) {
                connection.close();
                executando = false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
