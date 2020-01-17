/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bancodados.modulo;

import BancoDados.Conexao;
import BancoDados.modulo.Modulo;
import classes.almoxarife.ProdutoAlmoxarife;
import java.util.ArrayList;

/**
 *
 * @author guilherme.machado
 */
public class ModuloProduto implements Modulo {

    private static ModuloProduto instancia;

    /**
     *
     * @return
     */
    public static ModuloProduto getInstancia() {
        if (instancia == null) {
            instancia = new ModuloProduto();
            instancia.iniciar();
        }
        return instancia;
    }

    @Override
    public void iniciar() {
    }

    @Override
    public void fechar() {
    }

    public ArrayList<ProdutoAlmoxarife> buscarUltimosProdutosAlmoxarife(Conexao banco, int limite) {
        return banco.buscarUltimosProdutosAlmoxarife(limite);
    }

    public ProdutoAlmoxarife incluirProdutoAlmoxarife(Conexao banco, int autor) {
        int id = banco.incluirProdutoAlmoxarife(autor);
        return banco.buscarProdutoAlmoxarife(id);
    }

    @Override
    public int setObject(Conexao conexao, String campo, int id, Object arg, int usuario) {
        switch (campo) {
            case "CODIGO":
                return alterarCodigoProduto(conexao, id, arg, usuario);
            case "DESCRICAO":
                return alterarDescricaoProduto(conexao, id, arg, usuario);
            case "COMPLEMENTO":
                return alterarComplementoProduto(conexao, id, arg, usuario);
            case "LOCALIZACAO":
                return alterarLocalizacaoProduto(conexao, id, arg, usuario);
            case "DESTINO":
                return alterarDestinoProduto(conexao, id, arg, usuario);
            case "UNIDADE":
                return alterarUnidadeProduto(conexao, id, arg, usuario);
            default:
                return 0;
        }
    }

    private int alterarCodigoProduto(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("PRODUTOESTOQUE", "CODIGO", id, arg, usuario);
    }

    private int alterarDescricaoProduto(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("PRODUTOESTOQUE", "DESCRICAO", id, arg, usuario);
    }

    private int alterarComplementoProduto(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("PRODUTOESTOQUE", "COMPLEMENTO", id, arg, usuario);
    }

    private int alterarLocalizacaoProduto(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("PRODUTOESTOQUE", "LOCALIZACAO", id, arg, usuario);
    }

    private int alterarDestinoProduto(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("PRODUTOESTOQUE", "DESTINO", id, arg, usuario);
    }

    private int alterarUnidadeProduto(Conexao conexao, int id, Object arg, int usuario) {
        return conexao.setObject("PRODUTOESTOQUE", "UNIDADE", id, arg, usuario);
    }

}
