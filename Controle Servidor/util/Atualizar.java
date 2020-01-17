/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import com.fincatto.documentofiscal.nfe310.classes.nota.NFNotaProcessada;
import com.fincatto.documentofiscal.parsers.DFParser;
import java.io.File;

/**
 * @author Guilherme
 */
public class Atualizar {

    /**
     *
     */
    public Atualizar() {
        atualizarNota();
    }

    private void atualizarNota() {
        NFNotaProcessada nota = new DFParser().notaProcessadaParaObjeto(new File("D:\\PERFIL\\Desktop\\XML\\33171002082558000199550030001423141006132681 - NF-e.xml"));
        System.out.println(nota.getNota().getInfo().getEmitente().getCnpj());
        System.out.println(nota.getNota().getInfo().getEmitente().getRazaoSocial());
        System.out.println(nota.getNota().getInfo().getIdentificacao().getNumeroNota());
        

        
    }


}
