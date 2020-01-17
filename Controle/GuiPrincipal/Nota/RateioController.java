/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Nota;

import classes.nota.item.ItemNota;
import classes.nota.item.ItemNota;
import classes.nota.Nota;
import classes.nota.Nota;
import Util.Conversao;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class RateioController implements Initializable{

    private ArrayList<ItemNota> itemArray = new ArrayList<ItemNota>();

    private Nota nota;

    @FXML
    private TableView<ItemNota> itens;

    @FXML
    private TableColumn item;

    @FXML
    private TableColumn mapa;

    @FXML
    private TableColumn valorRateado;

    @FXML
    private TableColumn valorTotal;

    @FXML
    private TextField valor;

    @FXML
    private TableColumn plano;
    private Stage stage;

    private long totalOld, totalNew;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // TODO
        item.setCellValueFactory(new PropertyValueFactory<ItemNota, String>("item"));
        plano.setCellValueFactory(new PropertyValueFactory<ItemNota, String>("plano"));
        mapa.setCellValueFactory(new PropertyValueFactory<ItemNota, String>("numeroMapa"));
        valorTotal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemNota, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemNota, String> c){
                return new SimpleStringProperty(longToString(dinheiro, c.getValue().getPrecoTotal()));
            }
        });
        valorRateado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemNota, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemNota, String> c){
                //System.out.println("C: " + c.getValue().getPrecoTotal() + "total:" + totalOld + "Total new:" + totalNew);

                double a = ((double) c.getValue().getPrecoTotal() / (double) totalOld) * (double) totalNew;

                //System.out.println(a);

                return new SimpleStringProperty(longToString(dinheiro, (long) a));
            }
        });
        valor.setPromptText("Digite o valor total");
        valor.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            editarTexto(newValue);
        });
        valor.setText(longToString(dinheiro, 0));
    }

    private void editarTexto(String newValue){
        String temp = longToString(dinheiro, newValue.isEmpty() ? 0 : Long.valueOf(newValue.replaceAll("[^0-9]", "")));
        //System.out.println(temp);        
        valor.setText(temp);
        valor.positionCaret(temp.length());
    }

    @FXML
    void salvar(ActionEvent event){
        itemArray.stream().forEach(s -> {
            s.setPrecoTotal((long) (((double) s.getPrecoTotal() / (double) totalOld) * (double) totalNew));
            if(s.getQntTotal() > 0){
                s.setPrecoUnitario((long) (((double) s.getPrecoTotal() / (double) s.getQntTotal()) * 100));
            }
        });
        stage.close();
    }

    @FXML
    void calcular(ActionEvent event){
        try{
            totalNew = Conversao.stringToLong(dinheiro, valor.getText());
            //System.out.println(totalNew);
            atualizarTabela();
        } catch(ParseException ex){
            Logger.getLogger(RateioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setNota(Nota nota){
        this.nota = nota;
        atualizarTabela();
    }

    private void atualizarTabela(){
        itemArray.clear();
        itens.getItems().clear();
        totalOld = 0;
        nota.getItens().stream().forEach(i -> {
                if(i.getPrecoTotal() > 0){
                    totalOld += i.getPrecoTotal();
                    itemArray.add(i);
                }
            
        });
        itens.setItems(FXCollections.observableArrayList(itemArray));
        //System.out.println("Atualizou");
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

}
