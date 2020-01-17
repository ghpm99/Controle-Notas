/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexao.protocolo.envio;

import Conexao.Cliente;
import Util.HistoricoPesquisa;
import Util.Parametros;
import classes.nota.anexo.AnexoNota;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guilherme.machado
 */
public class Envio {

    public void enviarString(DataOutputStream dos, String arg) {
        try {
            if (arg == null) {
                arg = "";
            }
            dos.writeInt(arg.length());
            dos.writeChars(arg);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarArrayString(DataOutputStream dos, ArrayList<String> array) {
        try {
            dos.writeInt(array.size());
            array.stream().forEach(s -> {
                try {
                    dos.writeInt(s.length());
                    dos.writeChars(s);
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarArrayInt(DataOutputStream dos, ArrayList<Integer> array) {
        try {
            dos.writeInt(array.size());
            array.stream().forEach(s -> {
                try {
                    dos.writeInt(s);
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarHistoricoPesquisa(DataOutputStream dos, HistoricoPesquisa historico) {
        enviarArrayString(dos, historico.getItens());
        enviarString(dos, historico.getCampo());
        enviarString(dos, historico.getValor());
    }

    public void enviarParametrosNota(DataOutputStream dos, Parametros parametro) {
        try {
            enviarString(dos, parametro.getFornecedor());
            dos.writeLong(parametro.getEmissaoDe().getTime());
            dos.writeLong(parametro.getEmissaoAte().getTime());
            dos.writeLong(parametro.getVencimentoDe().getTime());
            dos.writeLong(parametro.getVencimentoAte().getTime());
            dos.writeLong(parametro.getValorDe());
            dos.writeLong(parametro.getValorAte());
            dos.writeBoolean(parametro.isClassificada());
            dos.writeBoolean(parametro.isAll());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarArquivo(DataOutputStream dos, File arquivo) {
        FileInputStream in = null;
        try {
            long tamanho = arquivo.length();

            byte[] bytes = new byte[1024 * 16];
            in = new FileInputStream(arquivo);

            enviarString(dos, arquivo.getName());
            dos.writeLong(tamanho);

            int read = 0;
            int totalRead = 0;
            int remaining = (int) tamanho;

            while ((read = in.read(bytes, 0, Math.min(bytes.length, remaining))) > 0) {
                totalRead += read;
                remaining -= read;
                //System.out.println("r " + read + " t " + totalRead + " re " + remaining + " ta " + tamanho);
                dos.write(bytes, 0, read);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void enviarAnexoNota(DataOutputStream dos, AnexoNota anexo) {
        try {
            dos.writeInt(anexo.getId());
            enviarString(dos, anexo.getNome());
            enviarString(dos, anexo.getCaminho());
            enviarString(dos, anexo.getTipo());

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
