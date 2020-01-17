/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDados;

import BancoDados.Conexao;
import GUI.principal.Principal;
import Rotina.Tarefas;
import Util.Atualizar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author guilherme.machado
 */
public enum GerenciadorBanco {

    /**
     *
     */
    INSTANCIA;

    private Map<String, Conexao> bancos = new HashMap<>();
    private boolean iniciado = false;

    /**
     *
     */
    public void iniciar() {
        if (iniciado) {
            return;
        }
        new Thread(() -> {
            Principal.getInstancia().progressoBarra.setValue(0);
            ArrayList<Banco> bancosTemp = Configuracao.Configuracao.INSTANCIA.getBancos();
            int individual = 80 / bancosTemp.size();
            bancosTemp.forEach(s -> {
                if (s.isATIVO()) {
                    Principal.getInstancia().progressoBarra.setValue(Principal.getInstancia().progressoBarra.getValue() + individual);
                    Principal.setStatusBanco("Banco:" + s.getNOME() + " Caminho:" + s.getCAMINHO());
                    bancos.put(s.getNOME(), new Conexao(s.getNOME(), s.getCAMINHO(), s.getUSUARIO(), s.getSENHA(),s.isPMWEB()));
                }

            });
            iniciado = true;
            Principal.getInstancia().progressoBarra.setValue(100);

            Iterator ite = bancos.keySet().iterator();
            while (ite.hasNext()) {
                if (!bancos.get((String) ite.next()).executando) {
                    ite.remove();
                }
            }
            Tarefas.INSTANCIA.iniciar();
           new Atualizar();
        }).start();

    }

    /**
     *
     * @return
     */
    public Set<String> getNomeBancos() {
        return bancos.keySet();
    }

    /**
     *
     * @param nome
     * @return
     */
    public synchronized Conexao getConexao(String nome) {
        return bancos.get(nome);
    }

    /**
     *
     * @param banco
     */
    public void addBanco(Banco banco) {
        bancos.put(banco.getNOME(), new Conexao(banco.getNOME(), banco.getCAMINHO(), banco.getUSUARIO(), banco.getSENHA(),banco.isPMWEB()));
        Configuracao.Configuracao.INSTANCIA.addBanco(banco);
    }

    /**
     *
     */
    public void fechar() {
        Principal.setStatusBanco("Fechando bancos");
        bancos.values().forEach(s -> {
            s.fechar();
        });
        bancos.clear();
        iniciado = false;
        Principal.getInstancia().progressoBarra.setValue(0);
    }

}
