/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rotina;

import Configuracao.Referencia;
import GUI.principal.Principal;
import Rotina.Email.EMail;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Guilherme
 */
public enum Tarefas {

    /**
     *
     */
    INSTANCIA;

    private boolean executando = Referencia.getInstancia().getTarefa();
    private final Timer time = new Timer();

    /**
     *
     */
    public void iniciar() {
        
        
        if (INSTANCIA.executando) {
            return;
        }

        int tempo = 1;
        int periodo = ((1000 * 60) * 60) * 2;

        time.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                INSTANCIA.executar();
            }
        }, tempo, periodo);
        INSTANCIA.executando = true;
    }

    private void executar() {
        tarefas();
    }

    private void tarefas() {
        Principal.getInstancia().progressoBarra.setValue(0);
        atualizarPmweb();
        Principal.setStatus("Terminou tarefas");
        Principal.getInstancia().progressoBarra.setValue(100);
    }

    /**
     *
     */
    public void fechar() {
        INSTANCIA.executando = false;
        time.cancel();
    }

    private void atualizarPmweb() {
        
        new EMail().iniciar();
    }

}
