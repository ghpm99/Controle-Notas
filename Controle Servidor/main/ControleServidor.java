/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import BancoDados.GerenciadorBanco;
import Conexao.Servidor.Servidor;
import Configuracao.Configuracao;
import Configuracao.Referencia;
import GUI.bancodados.NovoBanco;
import GUI.principal.Principal;
import Util.Log;

/**
 *
 * @author Guilherme
 */
public class ControleServidor{

    /**
     *
     * @param args
     */
    public static void main(String[] args){        
        try {
            Log.INSTANCIA.iniciar();
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NovoBanco.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            Log.INSTANCIA.escrever(ex.getMessage());
        }
        
        Principal.getInstancia().setVisible(true);
        Configuracao.INSTANCIA.iniciar();
        
        if(Referencia.getInstancia().getAutoiniciar()){
            GerenciadorBanco.INSTANCIA.iniciar();
            Servidor.INSTANCIA.iniciar();
            
        }
        
    }

}
