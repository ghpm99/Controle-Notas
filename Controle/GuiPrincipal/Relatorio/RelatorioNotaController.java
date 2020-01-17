/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Relatorio;

import Conexao.Cliente;
import GuiPrincipal.Main;
import GuiPrincipal.Nota.AlterarLoteNotaController;
import classes.nota.Nota;
import classes.relatorio.NotaAnexo;
import classes.relatorio.Relatorio;
import Util.CarregarGui;
import Util.Conversao;
import static Util.Conversao.dinheiro;
import Util.LoteNota;
import Util.Modulos;
import Util.PaneController;
import Util.Parametros;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class RelatorioNotaController implements Initializable, PaneController {

    private BorderPane pai;

    private Parametros parametros;
    @FXML
    private TableView<Nota> nota;
    @FXML
    private TableColumn numero;
    @FXML
    private TableColumn serie;
    @FXML
    private TableColumn tipo;
    @FXML
    private TableColumn fornecedor;
    @FXML
    private TableColumn emissao;
    @FXML
    private TableColumn vencimento;
    @FXML
    private TableColumn vencimentoReal;
    @FXML
    private TableColumn valor;
    @FXML
    private TableColumn classificada;
    @FXML
    private TableColumn lancada;
    @FXML
    private TableView<Nota> nota1;
    @FXML
    private TableColumn numero1;
    @FXML
    private TableColumn serie1;
    @FXML
    private TableColumn tipo1;
    @FXML
    private TableColumn fornecedor1;
    @FXML
    private TableColumn emissao1;
    @FXML
    private TableColumn vencimento1;
    @FXML
    private TableColumn vencimentoReal1;
    @FXML
    private TableColumn valor1;
    @FXML
    private TableColumn classificada1;
    @FXML
    private TableColumn aprovado;
    @FXML
    private TableColumn lancada1;
    private ArrayList<Nota> notas;
    @FXML
    private DatePicker vencimentoAlterar;
    @FXML
    private CheckBox classificarAlterar;
    
    private SimpleDateFormat sdf;
    @FXML
    private LineChart grafico;
    @FXML
    private Label status;
    private long total;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        notas = new ArrayList<Nota>();
        numero.setCellValueFactory(new PropertyValueFactory<Nota, String>("numero"));
        serie.setCellValueFactory(new PropertyValueFactory<Nota, String>("serie"));
        tipo.setCellValueFactory(new PropertyValueFactory<Nota, String>("tipo"));
        serie1.setCellValueFactory(new PropertyValueFactory<Nota, String>("serie"));
        tipo1.setCellValueFactory(new PropertyValueFactory<Nota, String>("tipo"));
        fornecedor.setCellValueFactory(new PropertyValueFactory<Nota, String>("fornecedor"));
        aprovado.setCellValueFactory(new PropertyValueFactory<Nota, String>("status"));
        emissao.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<>(sdf.format(param.getValue().getEmissao()));
                return simpleObject;
            }
        });
        emissao.setComparator(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                try {
                    Date d1 = sdf.parse(o1);
                    Date d2 = sdf.parse(o2);
                    return Long.compare(d1.getTime(), d2.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return -1;
            }
        });
        vencimento.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<>(sdf.format(param.getValue().getVencimento()));
                return simpleObject;
            }
        });
        vencimentoReal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<>(sdf.format(param.getValue().getVencimentoReal()));
                return simpleObject;
            }
        });
        vencimentoReal1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<>(sdf.format(param.getValue().getVencimentoReal()));
                return simpleObject;
            }
        });
        vencimento.setComparator(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                try {
                    Date d1 = sdf.parse(o1);
                    Date d2 = sdf.parse(o2);
                    return Long.compare(d1.getTime(), d2.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return -1;
            }
        });
        valor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c) {
                return new SimpleStringProperty(Conversao.longToString(dinheiro, c.getValue().getGastoTotal()));
            }
        });

        classificada.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c) {
                return new SimpleStringProperty(c.getValue().isClassificada() ? "Nota classificada" : "Nota nao classificada");
            }
        });
        lancada.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c) {
                return new SimpleStringProperty(c.getValue().isLancada() ? "Nota Lançada" : "Nota em aberto");
            }
        });

        numero1.setCellValueFactory(new PropertyValueFactory<Nota, String>("numero"));
        fornecedor1.setCellValueFactory(new PropertyValueFactory<Nota, String>("fornecedor"));
        emissao1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<>(sdf.format(param.getValue().getEmissao()));
                return simpleObject;
            }
        });
        emissao1.setComparator(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                try {
                    Date d1 = sdf.parse(o1);
                    Date d2 = sdf.parse(o2);
                    return Long.compare(d1.getTime(), d2.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return -1;
            }
        });
        vencimento1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param) {
                final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<>(sdf.format(param.getValue().getVencimento()));
                return simpleObject;
            }
        });
        vencimento1.setComparator(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                try {
                    Date d1 = sdf.parse(o1);
                    Date d2 = sdf.parse(o2);
                    return Long.compare(d1.getTime(), d2.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return -1;
            }
        });
        valor1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c) {
                return new SimpleStringProperty(Conversao.longToString(dinheiro, c.getValue().getGastoTotal()));
            }
        });

        classificada1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c) {
                return new SimpleStringProperty(c.getValue().isClassificada() ? "Nota classificada" : "Nota nao classificada");
            }
        });
        lancada1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c) {
                return new SimpleStringProperty(c.getValue().isLancada() ? "Nota Lançada" : "Nota em aberto");
            }
        });
        //nota1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        nota1.setRowFactory(s -> {
            TableRow<Nota> linha = new TableRow<>();
            linha.setOnMouseClicked(evento -> {
                if (!linha.isEmpty()) {

                    Nota nota = linha.getItem();
                    //nota.setNumero("XXXXX");

                    //System.out.println(notas.indexOf(nota));
                    if (vencimentoAlterar.getValue() != null) {
                        nota.setVencimentoReal(Date.from(Instant.from(vencimentoAlterar.getValue().atStartOfDay(ZoneId.systemDefault()))));
                        Cliente.INSTANCIA.setDate("NOTA", "VENCIMENTOREAL", nota.getId(), nota.getVencimentoReal());
                    }
                    if (!classificarAlterar.isIndeterminate()) {
                        nota.setClassificada(classificarAlterar.isSelected());
                        Cliente.INSTANCIA.setBoolean("NOTA", "CLASSIFICADA", nota.getId(), nota.isClassificada());
                    }

                    //notas.set(notas.indexOf(nota), Cliente.INSTANCIA.incluirNota(nota));
                    atualizarTabela();
                }
            });
            return linha;

        });
        parametros = new Parametros();

    }

    /**
     *
     * @param controller
     */
    @Override
    public void setPaneController(BorderPane controller) {
        this.pai = controller;
    }

    /**
     *
     */
    @FXML
    public void parametros() {
        try {
            parametros = new Parametros();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Modulos.RELATORIOPARAMETROS.getCaminho()));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Parametros");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            RelatorioParametrosController controller = loader.getController();
            controller.setStage(stage);
            controller.setParametro(parametros);

            stage.showAndWait();

            if (controller.isOk()) {
                atualizar();
            }

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void atualizar() {
        CarregarGui carregar = new CarregarGui();
        pai.setDisable(true);
        carregar.mostrar();
        total = 0;
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                notas.clear();
//                ArrayList<Integer> temp = Cliente.INSTANCIA.relatorioNota(parametros);
//                temp.stream().forEach(s -> {
//                    //add(Cliente.INSTANCIA.buscarNota(s));
//                });
                Cliente.INSTANCIA.relatorioNota(parametros).stream().forEach(s -> {
                    add(s);
                });
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded(); //To change body of generated methods, choose Tools | Templates.
                status.setText(notas.size() + " resultados encontrados.Total:" + Conversao.longToString(dinheiro, total));
                atualizarTabela();
                atualizarGrafico();
                pai.setDisable(false);
                carregar.fechar();
            }

            @Override
            protected void failed() {
                super.failed(); //To change body of generated methods, choose Tools | Templates.
                pai.setDisable(false);
                carregar.fechar();
                status.setText("Falhou.");
                getException().printStackTrace();
            }

        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }

    private void add(Nota arg0) {
        if (arg0.isCancelado()) {
            return;
        }
        total += arg0.getGastoTotal();
        notas.add(arg0);
    }

    private void atualizarTabela() {
        Collections.sort(notas, (Nota o1, Nota o2) -> o1.getNumero().compareTo(o2.getNumero()));
        Collections.sort(notas, (Nota o1, Nota o2) -> o1.getFornecedor().compareTo(o2.getFornecedor()));
        Collections.sort(notas, (Nota o1, Nota o2) -> Long.compare(o1.getVencimentoReal().getTime(), o2.getVencimentoReal().getTime()));

        nota.getItems().clear();
        nota1.getItems().clear();
        nota.setItems(FXCollections.observableArrayList(notas));
        nota1.setItems(FXCollections.observableArrayList(notas));
    }

    private void atualizarGrafico() {
        grafico.getData().clear();
        Map<Long, Long> mapa = new HashMap<>();
        gerarMapa(mapa);
        XYChart.Series series = new XYChart.Series();

        Set<Long> keys = mapa.keySet();
        TreeSet<Long> sortedKeys = new TreeSet<>(keys);
        long a = 0;
        for (Long key : sortedKeys) {
            series.getData().add(new XYChart.Data(sdf.format(new Date(key)), mapa.get(key)));
            a += mapa.get(key);
        }
        series.setName(Conversao.longToString(dinheiro, a));
        grafico.getData().add(series);
    }

    private void gerarMapa(Map<Long, Long> mapa) {
        for (Nota temp : notas) {
            if (mapa.containsKey(temp.getVencimento().getTime())) {
                long a = mapa.get(temp.getVencimento().getTime()) + temp.getGastoTotal();
                mapa.put(temp.getVencimento().getTime(), a);
            } else {
                mapa.put(temp.getVencimento().getTime(), temp.getGastoTotal());
            }
        }
    }

    @FXML
    private void gerarRelatorio() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel", ".xlsx"));

        new Relatorio().nota(fileChooser.showSaveDialog(null), notas);
    }

    @FXML
    private void alterarLote() {
        try {
            LoteNota lote = new LoteNota();
            FXMLLoader loader = new FXMLLoader(AlterarLoteNotaController.class.getResource("AlterarLoteNota.fxml"));
            AnchorPane anchor = (AnchorPane) loader.load();
            AlterarLoteNotaController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Alterar lote");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            controller.setLoteNota(lote, stage);
            Scene scene = new Scene(anchor);
            stage.setScene(scene);
            stage.showAndWait();
            if (!lote.isDataSelect() && !lote.isClassificadaSelect()) {
                return;
            }
            nota1.getSelectionModel().getSelectedItems().stream().forEach(s -> {
                if (lote.isClassificadaSelect()) {
                    s.setClassificada(lote.isClassificada());
                    Cliente.INSTANCIA.setBoolean("NOTA", "CLASSIFICADA", s.getId(), lote.isClassificada());
                }
                if (lote.isDataSelect()) {
                    s.setVencimentoReal(lote.getData());
                    Cliente.INSTANCIA.setDate("NOTA", "VENCIMENTOREAL", s.getId(), lote.getData());
                }
                //Cliente.INSTANCIA.incluirNota(s);

            });
            atualizarTabela();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param modulo
     */
    @Override
    public void setModuloVoltar(Modulos modulo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void succeeded() {
    }

    @FXML
    private void exportarNota() {
        ArrayList<String> tiposAnexo = new ArrayList<>();
        ArrayList<String> tiposNota = new ArrayList<>();
        ArrayList<NotaAnexo> notasAnexo = new ArrayList<>();
        notas.forEach(s -> {
            NotaAnexo notaTemp = new NotaAnexo();
            notaTemp.setId(s.getId());
            notaTemp.setNumero(s.getNumero());
            notaTemp.setTipo(s.getTipo());
            notasAnexo.add(notaTemp);
        });
        try {            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ExportarAnexo.fxml"));
            AnchorPane anchor = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle("Exportar Notas");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            Scene scene = new Scene(anchor);

            stage.setScene(scene);

            ExportarAnexoController controller = loader.getController();
            controller.setStage(stage);
            controller.setArrayTipoAnexo(tiposAnexo);
            controller.setArrayTipoNota(tiposNota);

            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", ".pdf"));

        new Relatorio().exportarNotas(notasAnexo, pai, fileChooser.showSaveDialog(null), tiposNota, tiposAnexo);
    }

}
