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
import classes.relatorio.Relatorio;
import Util.CarregarGui;
import Util.Conversao;
import static Util.Conversao.dinheiro;
import Util.LoteNota;
import Util.PaneController;
import Util.Modulos;
import Util.Parametros;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
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
public class RelatorioOrcamentoController implements Initializable, PaneController{

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
    public void initialize(URL url, ResourceBundle rb){
        // TODO
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        notas = new ArrayList<Nota>();
        numero.setCellValueFactory(new PropertyValueFactory<Nota, String>("numero"));
        serie.setCellValueFactory(new PropertyValueFactory<Nota, String>("serie"));
        tipo.setCellValueFactory(new PropertyValueFactory<Nota, String>("tipo"));
        
        fornecedor.setCellValueFactory(new PropertyValueFactory<Nota, String>("fornecedor"));        
        emissao.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param){
                final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<>(sdf.format(param.getValue().getEmissao()));
                return simpleObject;
            }
        });
        emissao.setComparator(new Comparator<String>(){

            @Override
            public int compare(String o1, String o2){
                try{
                    Date d1 = sdf.parse(o1);
                    Date d2 = sdf.parse(o2);
                    return Long.compare(d1.getTime(), d2.getTime());
                } catch(Exception e){
                    e.printStackTrace();
                }
                return -1;
            }
        });
        vencimento.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param){
                final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<>(sdf.format(param.getValue().getVencimento()));
                return simpleObject;
            }
        });
        vencimentoReal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>(){

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> param){
                final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<>(sdf.format(param.getValue().getVencimentoReal()));
                return simpleObject;
            }
        });
        
        vencimento.setComparator(new Comparator<String>(){

            @Override
            public int compare(String o1, String o2){
                try{
                    Date d1 = sdf.parse(o1);
                    Date d2 = sdf.parse(o2);
                    return Long.compare(d1.getTime(), d2.getTime());
                } catch(Exception e){
                    e.printStackTrace();
                }
                return -1;
            }
        });
        valor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c){
                return new SimpleStringProperty(Conversao.longToString(dinheiro, c.getValue().getGastoTotal()));
            }
        });
        
        classificada.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c){
                return new SimpleStringProperty(c.getValue().isClassificada() ? "Nota classificada" : "Nota nao classificada");
            }
        });
        lancada.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Nota, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Nota, String> c){
                return new SimpleStringProperty(c.getValue().isLancada() ? "Nota Lançada" : "Nota em aberto");
            }
        });

        parametros = new Parametros();

    }

    /**
     *
     * @param controller
     */
    @Override
    public void setPaneController(BorderPane controller){
        this.pai = controller;
    }

    /**
     *
     */
    @FXML
    public void parametros(){
        try{
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

            if(controller.isOk()){
                atualizar();
            }

        } catch(IOException ex){
            Logger.getLogger(RelatorioNotaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void atualizar(){
        CarregarGui carregar = new CarregarGui();
        pai.setDisable(true);
        carregar.mostrar();
        total = 0;
        Task<Void> task = new Task<Void>(){

            @Override
            protected Void call() throws Exception{
                notas.clear();
//                ArrayList<Integer> temp = Cliente.INSTANCIA.relatorioNota(parametros);
//                temp.stream().forEach(s -> {
//                    //add(Cliente.INSTANCIA.buscarNota(s));
//                });
                Cliente.INSTANCIA.relatorioOrcamento(parametros).stream().forEach(s -> {
                    add(s);
                });
                return null;
            }

            @Override
            protected void succeeded(){
                super.succeeded(); //To change body of generated methods, choose Tools | Templates.
                status.setText(notas.size() + " resultados encontrados.Total:" + Conversao.longToString(dinheiro, total));
                atualizarTabela();
                atualizarGrafico();
                pai.setDisable(false);
                carregar.fechar();
            }

            @Override
            protected void failed(){
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

    private void add(Nota arg0){
        if(arg0.isCancelado()){
            return;
        }
        total += arg0.getGastoTotal();
        notas.add(arg0);
    }

    private void atualizarTabela(){
        Collections.sort(notas, new Comparator<Nota>(){

            @Override
            public int compare(Nota o1, Nota o2){

                return o1.getNumero().compareTo(o2.getNumero());
            }
        });
        Collections.sort(notas, new Comparator<Nota>(){

            @Override
            public int compare(Nota o1, Nota o2){

                return o1.getFornecedor().compareTo(o2.getFornecedor());
            }

        });
        Collections.sort(notas, new Comparator<Nota>(){

            @Override
            public int compare(Nota o1, Nota o2){
                return Long.compare(o1.getVencimentoReal().getTime(), o2.getVencimentoReal().getTime());
            }

        });

        nota.getItems().clear();
        
        nota.setItems(FXCollections.observableArrayList(notas));
        
    }

    private void atualizarGrafico(){
        grafico.getData().clear();
        Map<Long, Long> mapa = new HashMap<>();
        gerarMapa(mapa);
        XYChart.Series series = new XYChart.Series();

        Set<Long> keys = mapa.keySet();
        TreeSet<Long> sortedKeys = new TreeSet<>(keys);
        long a = 0;
        for(Long key : sortedKeys){
            series.getData().add(new XYChart.Data(sdf.format(new Date(key)), mapa.get(key)));
            a += mapa.get(key);
        }
        series.setName(Conversao.longToString(dinheiro, a));
        grafico.getData().add(series);
    }

    private void gerarMapa(Map<Long, Long> mapa){
        for(Nota temp : notas){
            if(mapa.containsKey(temp.getVencimento().getTime())){
                long a = mapa.get(temp.getVencimento().getTime()) + temp.getGastoTotal();
                mapa.put(temp.getVencimento().getTime(), a);
            } else{
                mapa.put(temp.getVencimento().getTime(), temp.getGastoTotal());
            }
        }
    }

    @FXML
    private void gerarRelatorio(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel", ".xlsx"));

        new Relatorio().orcamento(fileChooser.showSaveDialog(null), notas);
    }

    @FXML
    private void alterarLote(){
        try{
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
            if(!lote.isDataSelect() && !lote.isClassificadaSelect()){
                return;
            }            
            atualizarTabela();
        } catch(IOException ex){
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param modulo
     */
    @Override
    public void setModuloVoltar(Modulos modulo){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void succeeded() {
    }


}
