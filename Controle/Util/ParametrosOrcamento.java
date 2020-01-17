/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

/**
 * @author Guilherme
 */
public class ParametrosOrcamento{

    private String tarefa, planoCusto, fornecedor;

    /**
     *
     * @return
     */
    public String getTarefa(){
        return tarefa;
    }

    /**
     *
     * @param tarefa
     */
    public void setTarefa(String tarefa){
        if(tarefa.equals("")){
            tarefa = " ";
        }
        this.tarefa = tarefa;
    }

    /**
     *
     * @return
     */
    public String getPlanoCusto(){
        return planoCusto;
    }

    /**
     *
     * @param planoCusto
     */
    public void setPlanoCusto(String planoCusto){
        if(planoCusto.equals("")){
            planoCusto = " ";
        }
        this.planoCusto = planoCusto;
    }

    /**
     *
     * @return
     */
    public String getFornecedor(){
        return fornecedor;
    }

    /**
     *
     * @param fornecedor
     */
    public void setFornecedor(String fornecedor){
        if(fornecedor.equals("")){
            fornecedor = " ";
        }
        this.fornecedor = fornecedor;
    }

    @Override
    public String toString(){
        return tarefa + ":" + planoCusto + ":" + fornecedor + ":";
    }

}
