/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.relatorio;

import Conexao.Cliente;
import classes.contrato.Contrato;
import classes.financeiro.Diario;
import classes.financeiro.Fluxo;
import classes.financeiro.NotaFinanceiro;
import GuiPrincipal.Relatorio.RelatorioNotaController;
import GuiPrincipal.Util.CarregarController;
import classes.nota.anexo.AnexoNota;
import classes.nota.item.ItemNota;
import classes.nota.Nota;
import Util.CarregarGui;
import Util.Conversao;
import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;
import Util.Log;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.BorderPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Guilherme
 */
public class Relatorio {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");

    public void orcamento(File arquivo, ArrayList<Nota> orcamento) {
        FileOutputStream fos = null;
        Workbook wb = null;
        GregorianCalendar dataCal = new GregorianCalendar();
        if (arquivo == null) {
            return;
        }
        try {

            fos = new FileOutputStream(arquivo);
            wb = new XSSFWorkbook();
            Sheet aba = wb.createSheet("Relatorio");

            Row cabecalho = aba.createRow(0);

            CellStyle dataStyle = wb.createCellStyle();
            CreationHelper createHelper = wb.getCreationHelper();
            dataStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));

            Cell celula = cabecalho.createCell(0);
            celula.setCellValue("Resumo de notas fiscais Ts-21 Participacoes - Atualizado em " + sdf.format(new Date()));
            aba.addMergedRegion(CellRangeAddress.valueOf("$A$1:$O$1"));
            Row cabecalho2 = aba.createRow(1);

            Cell item = cabecalho2.createCell(0);
            item.setCellValue("Item de Orcamento");

            Cell plano = cabecalho2.createCell(1);
            plano.setCellValue("Plano de Contas");

            Cell fornecedor = cabecalho2.createCell(2);
            fornecedor.setCellValue("Fornecedor");

            Cell nota = cabecalho2.createCell(3);
            nota.setCellValue("Numero");

            Cell serie = cabecalho2.createCell(4);
            serie.setCellValue("Serie");

            Cell valorHistorico = cabecalho2.createCell(5);
            valorHistorico.setCellValue("Valor");

            Cell emissao = cabecalho2.createCell(6);
            emissao.setCellValue("Data Emissao");

            Cell pagamento = cabecalho2.createCell(7);
            pagamento.setCellValue("Data Pagamento");

            Iterator ite = orcamento.iterator();
            int rownum = 2;
            while (ite.hasNext()) {
                Nota temp = (Nota) ite.next();
                dataCal.setTime(temp.getVencimentoReal());
                for (ItemNota itemNota : temp.getItens()) {
                    if (itemNota.getPrecoTotal() == 0) {
                        continue;
                    }
                    Row linha = aba.createRow(rownum);

                    Cell itemNota2 = linha.createCell(0);
                    itemNota2.setCellValue(itemNota.getItem());

                    Cell planoNota = linha.createCell(1);
                    planoNota.setCellValue(itemNota.getPlano());

                    Cell fornecedorNota = linha.createCell(2);
                    fornecedorNota.setCellValue(temp.getFornecedor());

                    Cell notaNota = linha.createCell(3);
                    notaNota.setCellValue(temp.getNumero());

                    Cell serieNota = linha.createCell(4);
                    serieNota.setCellValue(temp.getSerie());

                    Cell valorHistoricoNota = linha.createCell(5);
                    valorHistoricoNota.setCellValue(Conversao.longToString(dinheiro, itemNota.getPrecoTotal()));

                    Cell emissaoNota = linha.createCell(6);
                    emissaoNota.setCellStyle(dataStyle);
                    emissaoNota.setCellValue(sdf.format(temp.getEmissao()));

                    Cell pagamentoNota = linha.createCell(7);
                    pagamentoNota.setCellStyle(dataStyle);
                    pagamentoNota.setCellValue(sdf.format(temp.getVencimentoReal()));

                    rownum++;
                }
            }
            aba.autoSizeColumn(0);
            aba.autoSizeColumn(1);
            aba.autoSizeColumn(2);
            aba.autoSizeColumn(3);
            aba.autoSizeColumn(4);
            aba.autoSizeColumn(5);
            aba.autoSizeColumn(6);
            aba.autoSizeColumn(7);
            wb.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                wb.close();
            } catch (IOException ex) {
                Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        abrirArquivo(arquivo);
    }

    public void nota(File arquivo, ArrayList<Nota> nota) {

        FileOutputStream fos = null;
        Workbook wb = null;
        if (arquivo == null) {
            return;
        }
        try {
            fos = new FileOutputStream(arquivo);
            wb = new XSSFWorkbook();
            Sheet aba = wb.createSheet("Relatorio");

            CellStyle dataStyle = wb.createCellStyle();
            CreationHelper createHelper = wb.getCreationHelper();
            dataStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));

            Row cabecalho = aba.createRow(0);

            criarCelula(cabecalho, 0, "Resumo de notas fiscais Ts-21 Participacoes - Atualizado em " + sdf.format(new Date()));

            aba.addMergedRegion(CellRangeAddress.valueOf("$A$1:$I$1"));

            Row cabecalho2 = aba.createRow(1);

            criarCelula(cabecalho2, 0, "Numero");
            criarCelula(cabecalho2, 1, "Serie");
            criarCelula(cabecalho2, 2, "Tipo");
            criarCelula(cabecalho2, 3, "Fornecedor");
            criarCelula(cabecalho2, 4, "Vencimento");
            criarCelula(cabecalho2, 5, "Vencimento Real");
            criarCelula(cabecalho2, 6, "Valor");
            criarCelula(cabecalho2, 7, "Status");
            criarCelula(cabecalho2, 8, "Classificada");

            Iterator ite = nota.iterator();
            int i = 2;
            while (ite.hasNext()) {
                Nota temp = (Nota) ite.next();
                Row linha = aba.createRow(i);

                criarCelula(linha, 0, temp.getNumero());
                criarCelula(linha, 1, temp.getSerie());
                criarCelula(linha, 2, temp.getTipo());
                criarCelula(linha, 3, temp.getFornecedor());
                criarCelula(linha, 4, temp.getVencimento(), dataStyle);
                criarCelula(linha, 5, temp.getVencimentoReal(), dataStyle);
                criarCelula(linha, 6, Conversao.longToString(dinheiro, temp.getGastoTotal()));
                criarCelula(linha, 7, temp.getStatus());
                criarCelula(linha, 8, temp.isClassificada() ? "Sim" : "Em Aberto");

                i++;
            }
            aba.autoSizeColumn(0);
            aba.autoSizeColumn(1);
            aba.autoSizeColumn(2);
            aba.autoSizeColumn(3);
            aba.autoSizeColumn(4);
            aba.autoSizeColumn(5);
            aba.autoSizeColumn(6);
            aba.autoSizeColumn(7);
            aba.autoSizeColumn(8);
            wb.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                wb.close();
            } catch (IOException ex) {
                Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
        abrirArquivo(arquivo);
    }

    public void imprimirContrato(File caminho, Contrato contrato) {
        if (caminho == null) {
            return;
        }
        try {
            Document documento = new Document();
            PdfWriter.getInstance(documento, new FileOutputStream(caminho));
            Formatter formatter = new Formatter();
            documento.open();

            String divisor = "-------------------------------------------------------------------------------------------------------------------------------------------------";

            formatter.format("%s\nID %4d Numero %-16s Fornecedor %-16s | %tD \n", divisor, contrato.getId(), contrato.getNumero(), contrato.getFornecedor(), Calendar.getInstance());
            formatter.format("%s\nDescrição: %s\n", divisor, contrato.getDescricao());
            formatter.format("%s\nValor total do contrato: %17s gasto atual: %17s saldo atual: %17s\n", divisor, longToString(dinheiro, contrato.getValorTotal()),
                    longToString(dinheiro, contrato.getGastoTotal()), longToString(dinheiro, contrato.getSaldoTotal()));
            formatter.format("%s\nMapas\n%s\n", divisor, divisor);
            contrato.getMapas().stream().forEach((mapa) -> {
                formatter.format("Mapa: %20s Descrição: %s Total: %17s Gasto: %17s Atual: %17s\n", mapa.getNumero(), mapa.getDescricao(), longToString(dinheiro, mapa.getTotal()),
                        longToString(dinheiro, mapa.getValor()), longToString(dinheiro, mapa.getSaldo()));
                formatter.format("%s%15s\t%15s%40s%12s%12s%17s%17s%17s\n", "+---", "Item",
                        "Plano", "Descrição", "Tipo", "Custo", "Total", "Gasto", "Atual");
                mapa.getItens().stream().forEach((item) -> {
                    formatter.format("%s%15s\t%15s%40s%12s%12s%17s%17s%17s\n", "+---",
                            item.getItem(), item.getPlano(), item.getDescricao().length() >= 40 ? item.getDescricao().substring(0, 40) : item.getDescricao(), item.getTipo(),
                            item.getCusto(), longToString(dinheiro, item.getPrecoTotal()), longToString(dinheiro, item.getValor()),
                            longToString(dinheiro, item.getSaldo()));
                });
                formatter.format("%s\n", divisor);
            });
            formatter.format("%s\n", divisor);
            //documento.add(criarTableContrato(contrato));
            documento.add(new Paragraph(formatter.toString(), new Font(FontFamily.COURIER, 6, Font.NORMAL)));
            documento.close();
            Desktop.getDesktop().print(caminho);
        } catch (DocumentException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void imprimirNota(File caminho, Nota nota) {
        if (caminho == null) {
            return;
        }
        try {
            Document documento = new Document();
            PdfWriter.getInstance(documento, new FileOutputStream(caminho));
            Formatter formatter = new Formatter();
            documento.open();
            DecimalFormat numero = new DecimalFormat("00000");

            String divisor = "-------------------------------------------------------------------------------------------------------------------------------------------------";

            formatter.format("%s\nID:%4d Numero:%-16s Fornecedor:%-16s Faturamento direto:%-16s Contrato:%9s| Emissão:%td/%tm/%ty \n", divisor, nota.getId(), nota.getNumero(), nota.getFornecedor(),
                    nota.getFaturamentoDireto(), ("CON" + numero.format(nota.getIdContrato())), nota.getEmissao(), nota.getEmissao(), nota.getEmissao());
            formatter.format("%s\nDescrição: %s\n", divisor, nota.getDescricao());
            formatter.format("%s\nValor total do contrato: %-17s gasto atual: %-17s saldo atual: %-17s\n", divisor, longToString(dinheiro, nota.getValorTotal()),
                    longToString(dinheiro, nota.getGastoTotal()), longToString(dinheiro, nota.getSaldoTotal()));
            formatter.format("%s\nItens:\n%s\n", divisor, divisor);

            nota.getItens().forEach(s -> {
                if (s.getPrecoTotal() != 0) {
                    imprimirItemNota(formatter, s);
                }
            });

            formatter.format("%s\n", divisor);

            formatter.format("Total:%s\n", longToString(dinheiro, nota.getGastoTotal()));
            formatter.format("Observação:%s\n", nota.getObservacao());
            formatter.format("Vencimento:%td/%tm/%ty Cancelada:%-23s Classificada:%-23s Lançada:%-23s Pré-nota:%-23s\n", nota.getVencimento(), nota.getVencimento(), nota.getVencimento(),
                    nota.isCancelado() ? "Cancelada" : "Ativa", nota.isClassificada() ? "Classificada" : "Não classificada",
                    nota.isLancada() ? "Lançada" : "Em aberto", nota.isPreNota() ? "Sim" : "Não");
            formatter.format("%s\n", divisor);
            //documento.add(criarTableContrato(contrato));
            documento.add(new Paragraph(formatter.toString(), new Font(FontFamily.COURIER, 6, Font.NORMAL)));
            documento.close();
            Desktop.getDesktop().print(caminho);
        } catch (DocumentException | FileNotFoundException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void imprimirItemNota(Formatter formatter, ItemNota item) {
        formatter.format("%-15s %-15s %-75s %-16s\n", item.getItem(), item.getPlano(), (item.getDescricao().length() > 73 ? item.getDescricao().substring(0, 73) : item.getDescricao()),
                longToString(dinheiro, item.getPrecoTotal()));
    }

    private PdfPTable criarTableContrato(Contrato con) {
        PdfPTable tabela = new PdfPTable(4);
        tabela.setWidthPercentage(100);
        PdfPCell cell = criarCelula("ID: " + con.getId());
        cell.setColspan(4);
        tabela.addCell(cell);
        tabela.addCell("1");
        tabela.addCell("Numero: " + con.getNumero());
        tabela.addCell("Fornecedor: " + con.getFornecedor());

        return tabela;
    }

    private PdfPCell criarCelula(String arg0) {
        return new PdfPCell(new Phrase(arg0));
    }

    public void imprimirDiario(File caminho, Diario diario) {
        if (caminho == null) {
            return;
        }
        try {
            Document documento = new Document();
            documento.setPageSize(PageSize.A4.rotate());
            PdfWriter writer = PdfWriter.getInstance(documento, new FileOutputStream(caminho));
            Formatter formatter = new Formatter();
            documento.open();

            String divisor = "-------------------------------------------------------------------------------------------------------------------------------------------------";

            formatter.format("ID:%4d Data Pagamento:%s Valor Total: %s\n", diario.getId(), sdf.format(diario.getPagamento()), longToString(dinheiro, diario.getTotal()));

            formatter.format("%s\n", divisor);
            formatter.format("%-10s %-70s %-15s %-11s %-11s %-16s %-16s %-16s %-16s\n", "Numero", "Fornecedor", "CNPJ", "Vencimento",
                    "Vencimento Real", "Valor", "Desconto", "Juros", "Valor Liquido");

            long totalValor = 0, totalDesconto = 0, totalJuros = 0, totalLiquido = 0;

            totalValor = diario.getNotas().stream().map((d) -> d.getValor()).reduce(totalValor, (accumulator, _item) -> accumulator + _item);
            totalDesconto = diario.getNotas().stream().map((d) -> d.getDesconto()).reduce(totalDesconto, (accumulator, _item) -> accumulator + _item);
            totalJuros = diario.getNotas().stream().map((d) -> d.getJuros()).reduce(totalJuros, (accumulator, _item) -> accumulator + _item);
            totalLiquido = diario.getNotas().stream().map((d) -> d.getValorLiquido()).reduce(totalLiquido, (accumulator, _item) -> accumulator + _item);

            diario.getNotas().forEach(s -> imprimirNotaDiario(formatter, s));
            formatter.format("%s\n", divisor);
            formatter.format("%-10s %-107s %-16s %-16s %-16s %-16s\n", "Total", "", longToString(dinheiro, totalValor), longToString(dinheiro, totalDesconto),
                    longToString(dinheiro, totalJuros), longToString(dinheiro, totalLiquido));
            documento.add(new Paragraph(formatter.toString(), new Font(FontFamily.COURIER, 6, Font.NORMAL)));
            documento.close();
            Desktop.getDesktop().print(caminho);
        } catch (DocumentException | FileNotFoundException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void imprimirNotaDiario(Formatter formatter, NotaFinanceiro nota) {
        formatter.format("%-10s %-70s %-15s %-11s %-11s %-16s %-16s %-16s %-16s\n", nota.getNumero(), nota.getFornecedor(), nota.getCnpj(), sdf1.format(nota.getVencimento()),
                sdf1.format(nota.getVencimentoReal()), longToString(dinheiro, nota.getValor()), longToString(dinheiro, nota.getDesconto()),
                longToString(dinheiro, nota.getJuros()), longToString(dinheiro, nota.getValorLiquido()));
    }

    public void exportarNotas(ArrayList<NotaAnexo> notas, BorderPane pai, File arquivoSalvar, ArrayList<String> tiposNota, ArrayList<String> tiposAnexo) {

        if (arquivoSalvar == null) {
            return;
        }
        CarregarGui carregar = new CarregarGui();
        pai.setDisable(true);
        carregar.mostrar(new CarregarController() {

            @Override
            public void run() {
                try {
                    Log log = new Log();
                    log.iniciar(arquivoSalvar.getAbsolutePath());
                    setStatus("Iniciando");
                    log.escrever("Iniciando");
                    circuloCarregar1.setProgress(0);
                    barraCarregar2.setProgress(0);
                    ArrayList<InputStream> arquivos = new ArrayList<>();
                    String caminho = arquivoSalvar.getParent() + "\\" + new Date().getTime() + "\\";

                    ArrayList<NotaAnexo> notasTemp = new ArrayList<>();
                    setStatus("Filtrando notas");
                    log.escrever("Filtrando notas");
                    if (tiposNota != null) {
                        tiposNota.forEach(s -> {
                            notas.forEach(a -> {
                                if (a.getTipo().equals(s)) {
                                    notasTemp.add(a);
                                }
                            });
                        });
                    } else {
                        notas.forEach(a -> {
                            notasTemp.add(a);
                        });
                    }
                    double total = notasTemp.size();
                    double atual = 1;
                    setStatus(total + " notas encontradas");
                    log.escrever(total + " notas encontradas");
                    for (NotaAnexo s : notasTemp) {
                        circuloCarregar1.setProgress(atual / total);
                        barraCarregar2.setProgress((atual / total) / 2);

                        ArrayList<AnexoNota> anexosTemp = Cliente.INSTANCIA.listarAnexosNota(s.getId());

                        tiposAnexo.stream().forEach((a) -> {
                            int i = 0;
                            for (AnexoNota b : anexosTemp) {
                                if (b.getTipo().equals(a)) {
                                    i++;
                                    if (b.getNome().toUpperCase().endsWith(".PDF")) {
                                        try {
                                            String caminhoTemp = caminho + new Date().getTime() + "\\";
                                            arquivos.add(new FileInputStream(Cliente.INSTANCIA.lerAnexoNota(b.getId(), new File(caminhoTemp))));
                                            setStatus(b.getNome() + " incluso com sucesso.");
                                            log.escrever(b.getNome() + " incluso com sucesso.");
                                        } catch (FileNotFoundException ex) {
                                            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }

                            }
                            if (i == 0) {
                                setStatus("Nota " + s.getNumero() + " sem anexo tipo:" + a);
                                log.escrever("Nota " + s.getNumero() + " sem anexo tipo:" + a);
                            }
                        });

                        atual++;
                    }
                    setStatus("Pdfs baixados...gerando arquivo");
                    log.escrever("Pdfs baixados...gerando arquivo");
                    barraCarregar2.setProgress(0.5);
                    mergePdfFiles(arquivos, new FileOutputStream(arquivoSalvar));
                    setStatus("Arquivo gerado");
                    log.escrever("Arquivo gerado");
                    barraCarregar2.setProgress(0.75);
                    setStatus("Excluindo arquivos temporarios");
                    log.escrever("Excluindo arquivos temporarios");
                    excluirCaminho(new File(caminho));
                    log.fechar();
                    barraCarregar2.setProgress(1);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void sucesso() {
                setStatus("Concluido");
                pai.setDisable(false);
                abrirArquivo(arquivoSalvar);
            }

            @Override
            public void falha() {
                setStatus("Falhou");
                pai.setDisable(false);
            }

        });

    }

    private void excluirArquivo(File arquivo) {
        arquivo.delete();
    }

    private void excluirCaminho(File caminho) {
        if (caminho.isDirectory()) {
            File[] caminhos = caminho.listFiles();
            for (File a : caminhos) {
                excluirCaminho(a);
            }
            caminho.delete();
        } else {
            excluirArquivo(caminho);
        }
    }

    public void mergePdfFiles(List<InputStream> inputPdfList, OutputStream outputStream) throws Exception {
        //Create document and pdfReader objects.
        Document document = new Document();
        List<PdfReader> readers = new ArrayList<>();
        int totalPages = 0;

        //Create pdf Iterator object using inputPdfList.
        Iterator<InputStream> pdfIterator = inputPdfList.iterator();

        // Create reader list for the input pdf files.
        while (pdfIterator.hasNext()) {
            InputStream pdf = pdfIterator.next();
            PdfReader pdfReader = new PdfReader(pdf);
            readers.add(pdfReader);
            totalPages = totalPages + pdfReader.getNumberOfPages();
        }

        // Create writer for the outputStream
        PdfCopy copy = new PdfSmartCopy(document, outputStream);

        //Open document.
        document.open();

        //Contain the pdf data.
        PdfContentByte pageContentByte = copy.getDirectContent();

        PdfImportedPage pdfImportedPage;
        int currentPdfReaderPage = 1;
        Iterator<PdfReader> iteratorPDFReader = readers.iterator();

        // Iterate and process the reader list.
        while (iteratorPDFReader.hasNext()) {
            PdfReader pdfReader = iteratorPDFReader.next();
            //Create page and add content.
            while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
                document.newPage();
                pdfImportedPage = copy.getImportedPage(pdfReader, currentPdfReaderPage);
                copy.addPage(pdfImportedPage);
                pageContentByte.addTemplate(pdfImportedPage, 0, 0);
                currentPdfReaderPage++;
            }
            currentPdfReaderPage = 1;
        }

        //Close document and outputStream.
        copy.close();
        outputStream.flush();
        document.close();
        outputStream.close();

    }

    private CellStyle criarDataStyle(Workbook wb, String format) {
        CellStyle dataStyle = wb.createCellStyle();
        CreationHelper createHelper = wb.getCreationHelper();
        dataStyle.setDataFormat(createHelper.createDataFormat().getFormat(format));
        return dataStyle;
    }

    public void exportarFluxo(File arquivo, ArrayList<Fluxo> fluxo, boolean detalhado) {
        FileOutputStream fos = null;
        Workbook wb = null;
        if (arquivo == null) {
            return;
        }
        try {
            fos = new FileOutputStream(arquivo);
            wb = new XSSFWorkbook();
            Sheet aba = wb.createSheet("Relatorio");

            CellStyle dataStyle = criarDataStyle(wb, "dd/mm/yyyy");
            CellStyle moneyStyle = criarDataStyle(wb, "R$ #,##0.00;[Red]-R$ #,##0.00");

            Row cabecalho = aba.createRow(0);

            criarCelula(cabecalho, 0, "Resumo de fluxos Ts-21 Participacoes - Atualizado em " + sdf.format(new Date()));

            aba.addMergedRegion(CellRangeAddress.valueOf("$A$1:$D$1"));

            Row cabecalho2 = aba.createRow(1);

            criarCelula(cabecalho2, 0, "Dia");
            criarCelula(cabecalho2, 1, "Valor");
            criarCelula(cabecalho2, 2, "Diario");
            criarCelula(cabecalho2, 3, "Saldo");

            Iterator ite = fluxo.iterator();
            int i = 2;
            while (ite.hasNext()) {
                Fluxo temp = (Fluxo) ite.next();
                Row linha = aba.createRow(i);

                criarCelula(linha, 0, temp.getInclusao(), dataStyle);
                criarCelula(linha, 1, ((double) temp.getValor() / 100), moneyStyle);
                criarCelula(linha, 2, "DC-" + new DecimalFormat("00000").format(temp.getNota()));
                criarCelula(linha, 3, ((double) temp.getSaldo() / 100), moneyStyle);

                if (detalhado) {
                    exportarNotasDiario(wb, temp, dataStyle, moneyStyle);
                }

                i++;
            }
            aba.autoSizeColumn(0);
            aba.autoSizeColumn(1);
            aba.autoSizeColumn(2);
            aba.autoSizeColumn(3);

            wb.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                wb.close();
            } catch (IOException ex) {
                Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
        abrirArquivo(arquivo);
    }

    private void exportarNotasDiario(Workbook wb, Fluxo fluxo, CellStyle dataStyle, CellStyle moneyStyle) {
        String numeroTemp = new DecimalFormat("00000").format(fluxo.getNota());
        Sheet aba = wb.createSheet("DC-" + numeroTemp);
        Row cabecalho = aba.createRow(0);
        criarCelula(cabecalho, 0, "Notas Diario DC-" + numeroTemp + " detalhado - Atualizado em " + sdf.format(new Date()));
        aba.addMergedRegion(CellRangeAddress.valueOf("$A$1:$H$1"));

        Row cabecalho2 = aba.createRow(1);

        criarCelula(cabecalho2, 0, "Numero nota");
        criarCelula(cabecalho2, 1, "Plano de Contas");
        criarCelula(cabecalho2, 2, "Fornecedor");
        criarCelula(cabecalho2, 3, "Serie");
        criarCelula(cabecalho2, 4, "Valor");
        criarCelula(cabecalho2, 5, "Data Emissao");
        criarCelula(cabecalho2, 6, "Data Vencimento");
        criarCelula(cabecalho2, 7, "Data Pagamento");
        criarCelula(cabecalho2, 8, "Classificada");

        ArrayList<Nota> notas = Cliente.INSTANCIA.listarNotasDiario(fluxo.getNota());
        GregorianCalendar dataCal = new GregorianCalendar();

        Iterator ite = notas.iterator();
        int rownum = 2;
        while (ite.hasNext()) {
            Nota temp = (Nota) ite.next();
            dataCal.setTime(temp.getVencimentoReal());
            for (ItemNota itemNota : temp.getItens()) {
                if (itemNota.getPrecoTotal() == 0) {
                    continue;
                }
                Row linha = aba.createRow(rownum);

                criarCelula(linha, 0, temp.getNumero());
                criarCelula(linha, 1, itemNota.getPlano());
                criarCelula(linha, 2, temp.getFornecedor());
                criarCelula(linha, 3, temp.getSerie());
                criarCelula(linha, 4, ((double) itemNota.getPrecoTotal() / 100), moneyStyle);
                criarCelula(linha, 5, temp.getEmissao(), dataStyle);
                criarCelula(linha, 6, temp.getVencimento(), dataStyle);
                criarCelula(linha, 7, fluxo.getInclusao(), dataStyle);
                criarCelula(linha, 8, temp.isClassificada() ? "Sim" : "Nao");

                rownum++;
            }
        }
        aba.autoSizeColumn(0);
        aba.autoSizeColumn(1);
        aba.autoSizeColumn(2);
        aba.autoSizeColumn(3);
        aba.autoSizeColumn(4);
        aba.autoSizeColumn(5);
        aba.autoSizeColumn(6);
        aba.autoSizeColumn(7);
    }

    private Cell criarCelula(Row linha, int index, String conteudo) {
        Cell celula = linha.createCell(index);
        celula.setCellValue(conteudo);
        return celula;
    }

    private Cell criarCelula(Row linha, int index, Date conteudo, CellStyle dataStyle) {
        Cell celula = linha.createCell(index);
        celula.setCellStyle(dataStyle);
        celula.setCellValue(conteudo);
        return celula;
    }

    private Cell criarCelula(Row linha, int index, double valor, CellStyle dataStyle) {
        Cell celula = linha.createCell(index);
        celula.setCellStyle(dataStyle);
        celula.setCellValue(valor);
        return celula;
    }

    public void exportarDiario(File caminho, Diario diario) {
        FileOutputStream fos = null;
        Workbook wb = null;
        if (caminho == null) {
            return;
        }
        try {
            fos = new FileOutputStream(caminho);
            wb = new XSSFWorkbook();
            Sheet aba = wb.createSheet("Relatorio");

            CellStyle dataStyle = criarDataStyle(wb, "dd/mm/yyyy");
            CellStyle moneyStyle = criarDataStyle(wb, "R$ #,##0.00;[Red]-R$ #,##0.00");

            Row cabecalho = aba.createRow(0);

            criarCelula(cabecalho, 0, "Diario DC-" + new DecimalFormat("00000").format(diario.getId()) + " - Ts-21 Participacoes - Atualizado em " + sdf.format(new Date()));

            aba.addMergedRegion(CellRangeAddress.valueOf("$A$1:$D$1"));

            Row cabecalho2 = aba.createRow(1);

            criarCelula(cabecalho2, 0, "Numero");
            criarCelula(cabecalho2, 1, "Fornecedor");
            criarCelula(cabecalho2, 2, "CNPJ");
            criarCelula(cabecalho2, 3, "Fluxo");
            criarCelula(cabecalho2, 4, "Vencimento Nota");
            criarCelula(cabecalho2, 5, "Vencimento");
            criarCelula(cabecalho2, 6, "Vencimento Real");
            criarCelula(cabecalho2, 7, "Classificada");
            criarCelula(cabecalho2, 8, "Valor");
            criarCelula(cabecalho2, 9, "Desconto");
            criarCelula(cabecalho2, 10, "Juros");
            criarCelula(cabecalho2, 11, "Valor Liquido");
            criarCelula(cabecalho2, 12, "Tipo");

            Iterator ite = diario.getNotas().iterator();
            int i = 2;
            while (ite.hasNext()) {
                NotaFinanceiro temp = (NotaFinanceiro) ite.next();
                Row linha = aba.createRow(i);

                criarCelula(linha, 0, temp.getNumero());
                criarCelula(linha, 1, temp.getFornecedor());
                criarCelula(linha, 2, temp.getCnpj());
                criarCelula(linha, 3, temp.getFluxo(), dataStyle);
                criarCelula(linha, 4, temp.getVencimentoNota(), dataStyle);
                criarCelula(linha, 5, temp.getVencimento(), dataStyle);
                criarCelula(linha, 6, temp.getVencimentoReal(), dataStyle);
                criarCelula(linha, 7, temp.isClassificada() ? "Sim" : "Nao");
                criarCelula(linha, 8, ((double) temp.getValor() / 100), moneyStyle);
                criarCelula(linha, 9, ((double) temp.getDesconto() / 100), moneyStyle);
                criarCelula(linha, 10, ((double) temp.getJuros() / 100), moneyStyle);
                criarCelula(linha, 11, ((double) temp.getValorLiquido() / 100), moneyStyle);
                criarCelula(linha, 12, temp.getTipo());

                i++;
            }
            aba.autoSizeColumn(0);
            aba.autoSizeColumn(1);
            aba.autoSizeColumn(2);
            aba.autoSizeColumn(3);
            aba.autoSizeColumn(4);
            aba.autoSizeColumn(5);
            aba.autoSizeColumn(6);
            aba.autoSizeColumn(7);
            aba.autoSizeColumn(8);
            aba.autoSizeColumn(9);
            aba.autoSizeColumn(10);
            aba.autoSizeColumn(11);
            aba.autoSizeColumn(12);

            wb.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                wb.close();
            } catch (IOException ex) {
                Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        abrirArquivo(caminho);
    }
    
    private void abrirArquivo(File caminho){
        try {
            Desktop.getDesktop().open(caminho);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
