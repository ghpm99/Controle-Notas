/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiPrincipal.Contrato;

import Conexao.Cliente;
import GuiPrincipal.Main;
import GuiPrincipal.Util.Mensagem;
import Util.Calcular;
import Util.CarregarGui;
import static Util.Conversao.dinheiro;
import static Util.Conversao.longToString;
import static Util.Conversao.quantidade;
import static Util.Conversao.stringToLong;
import Util.ItemOrcamento;
import Util.Modulos;
import Util.PaneController;
import classes.contrato.item.ItemContrato;
import classes.contrato.mapa.MapaContrato;
import classes.contrato.produto.ProdutoContrato;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Eduardo
 */
public class MapaCotacaoAlterarController implements Initializable, PaneController {

    /**
     * Initializes the controller class.
     */
    private BorderPane pai;
    @FXML
    private TextField numero;
    @FXML
    private DatePicker emissao;
    @FXML
    private TextField autor;
    @FXML
    private TextField total;
    @FXML
    private TextField fornecedor;
    @FXML
    private TextField descricao;
    @FXML
    private TextField gasto;
    @FXML
    private TextField contratoCampo;
    @FXML
    private TableView<ItemContrato> item;
    @FXML
    private TableView<ProdutoContrato> produto;
    @FXML
    private TableColumn itemOrcamentoItem;
    @FXML
    private TableColumn planoContaItem;
    @FXML
    private TableColumn descricaoItem;
    @FXML
    private TableColumn unidadeItem;
    @FXML
    private TableColumn quantidadeOrcadaItem;
    @FXML
    private TableColumn valorOrcadoUnidadeItem;
    @FXML
    private TableColumn valorOrcadoTotalItem;
    @FXML
    private TableColumn tipoItem;
    @FXML
    private TableColumn custoItem;
    @FXML
    private TableColumn codigoProduto;
    @FXML
    private TableColumn descricaoProduto;
    @FXML
    private TableColumn unidadeProduto;
    @FXML
    private TableColumn quantidadeProduto;
    @FXML
    private TableColumn menorUnidadeProduto;
    @FXML
    private TableColumn menorPrecoTotalProduto;
    @FXML
    private TableColumn quantidadeItem;
    @FXML
    private TableColumn valorUnidadeItem;
    @FXML
    private TableColumn valorTotalItem;
    @FXML
    private Label status;

    private MapaContrato mapa;

    private Modulos modulo = Modulos.MAPACOTACAO;

    private boolean editavel = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //Colunas item

        item.getSelectionModel().selectedItemProperty().addListener((obs, old, newSel) -> {
            if (newSel != null) {
                atualizarTabelaProduto();
            }
        });

        itemOrcamentoItem.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemContrato, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemContrato, String> c) {
                ItemOrcamento temp = Cliente.INSTANCIA.buscarItemOrcamento(c.getValue().getItem());
                c.getValue().setIdItem(temp.getId());
                c.getValue().setPlano(temp.getPlanoConta());
                c.getValue().setDescricao(temp.getDescricao());
                c.getValue().setUnidade(temp.getUnidade());

                return new SimpleStringProperty(c.getValue().getItem());
            }
        });
        planoContaItem.setCellValueFactory(new PropertyValueFactory<ItemContrato, String>("plano"));
        descricaoItem.setCellValueFactory(new PropertyValueFactory<ItemContrato, String>("descricao"));
        unidadeItem.setCellValueFactory(new PropertyValueFactory<ItemContrato, String>("unidade"));
        quantidadeOrcadaItem.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemContrato, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemContrato, String> c) {
                //Cliente.INSTANCIA.buscarItemOrcamento(c.getValue().getItem());
                return new SimpleStringProperty(longToString(quantidade, Cliente.INSTANCIA.buscarItemOrcamento(c.getValue().getItem()).getQuantidadeTotal()));
            }
        });
        valorOrcadoUnidadeItem.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemContrato, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemContrato, String> c) {
                //String[] temp = Cliente.INSTANCIA.buscarItemOrcamento(c.getValue().getItem());

                return new SimpleStringProperty(longToString(dinheiro, Cliente.INSTANCIA.buscarItemOrcamento(c.getValue().getItem()).getPrecoUnitario()));
            }
        });
        valorOrcadoTotalItem.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemContrato, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemContrato, String> c) {
                //String[] temp = Cliente.INSTANCIA.buscarItemOrcamento(c.getValue().getItem());
                return new SimpleStringProperty(longToString(dinheiro, Cliente.INSTANCIA.buscarItemOrcamento(c.getValue().getItem()).getPrecoTotal()));
            }
        });
        quantidadeItem.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemContrato, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemContrato, String> c) {
                return new SimpleStringProperty(longToString(quantidade, c.getValue().getQntTotal()));
            }
        });
        valorUnidadeItem.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemContrato, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemContrato, String> c) {
                return new SimpleStringProperty(longToString(dinheiro, c.getValue().getPrecoUnitario()));
            }
        });
        valorTotalItem.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemContrato, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemContrato, String> c) {
                return new SimpleStringProperty(longToString(dinheiro, c.getValue().getPrecoTotal()));
            }
        });
        tipoItem.setCellValueFactory(new PropertyValueFactory<ItemContrato, String>("tipo"));
        custoItem.setCellValueFactory(new PropertyValueFactory<ItemContrato, String>("custo"));
        codigoProduto.setCellValueFactory(new PropertyValueFactory<ProdutoContrato, String>("codigo"));
        descricaoProduto.setCellValueFactory(new PropertyValueFactory<ProdutoContrato, String>("descricao"));
        unidadeProduto.setCellValueFactory(new PropertyValueFactory<ProdutoContrato, String>("unidade"));
        quantidadeProduto.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProdutoContrato, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ProdutoContrato, String> c) {
                return new SimpleStringProperty(longToString(quantidade, c.getValue().getQnt()));
            }
        });
        menorUnidadeProduto.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProdutoContrato, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ProdutoContrato, String> c) {
                return new SimpleStringProperty(longToString(dinheiro, c.getValue().getPrecoUnitario()));
            }
        });
        menorPrecoTotalProduto.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProdutoContrato, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ProdutoContrato, String> c) {
                return new SimpleStringProperty(longToString(dinheiro, c.getValue().getPrecoTotal()));
            }
        });

        if (editavel) {
            editavel();
        }

        atualizar();
        if (mapa.getId() == 0) {
            try {
                atualizarContrato();
//                this.contrato = Cliente.INSTANCIA.incluirContrato(contrato);
            } catch (ParseException ex) {
                Logger.getLogger(MapaCotacaoAlterarController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     *
     * @param controller
     */
    @Override
    public void setPaneController(BorderPane controller) {
        this.pai = controller;
    }

    private void editavel() {
        unidadeProduto.setCellFactory(TextFieldTableCell.forTableColumn());
        unidadeProduto.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProdutoContrato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<ProdutoContrato, String> event) {
                event.getRowValue().setUnidade(event.getNewValue());
                atualizarTabelaProduto();
                requestFocusProduto();
            }
        });
        quantidadeProduto.setCellFactory(TextFieldTableCell.forTableColumn());
        quantidadeProduto.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProdutoContrato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<ProdutoContrato, String> event) {
                try {
                    event.getRowValue().setQnt(stringToLong(quantidade, event.getNewValue()));
                    atualizarTabelaProduto();
                    requestFocusProduto();
                } catch (ParseException ex) {
                    Logger.getLogger(MapaCotacaoAlterarController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menorUnidadeProduto.setCellFactory(TextFieldTableCell.forTableColumn());
        menorUnidadeProduto.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProdutoContrato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<ProdutoContrato, String> event) {
                try {
                    event.getRowValue().setPrecoUnitario(stringToLong(dinheiro, event.getNewValue()));
                    atualizarTabelaProduto();
                    requestFocusProduto();
                } catch (ParseException ex) {
                    Logger.getLogger(MapaCotacaoAlterarController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menorPrecoTotalProduto.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                if (!string.contains("R$")) {
                    string = "R$ " + string;
                }
                return string;
            }

        }));
        menorPrecoTotalProduto.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProdutoContrato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<ProdutoContrato, String> event) {
                try {
                    event.getRowValue().setPrecoTotal(stringToLong(dinheiro, event.getNewValue()));
                    atualizarTabelaProduto();
                    requestFocusProduto();
                } catch (ParseException ex) {
                    Logger.getLogger(MapaCotacaoAlterarController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        codigoProduto.setCellFactory(TextFieldTableCell.forTableColumn());
        codigoProduto.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProdutoContrato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<ProdutoContrato, String> event) {
                event.getRowValue().setCodigo(event.getNewValue());
                atualizarTabelaProduto();
                requestFocusProduto();
            }
        });
        descricaoProduto.setCellFactory(TextFieldTableCell.forTableColumn());
        descricaoProduto.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProdutoContrato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<ProdutoContrato, String> event) {
                event.getRowValue().setDescricao(event.getNewValue());
                atualizarTabelaProduto();
                requestFocusProduto();
            }
        });
        itemOrcamentoItem.setCellFactory(TextFieldTableCell.forTableColumn());
        itemOrcamentoItem.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ItemContrato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<ItemContrato, String> event) {
                event.getRowValue().setItem(event.getNewValue());
                atualizarTabelaItem();
                requestFocusItem();
            }
        });
        planoContaItem.setCellFactory(TextFieldTableCell.forTableColumn());
        planoContaItem.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ItemContrato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<ItemContrato, String> event) {
                event.getRowValue().setPlano(event.getNewValue());
                atualizarTabelaItem();
                requestFocusItem();
            }
        });
        descricaoItem.setCellFactory(TextFieldTableCell.forTableColumn());
        descricaoItem.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ItemContrato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<ItemContrato, String> event) {
                event.getRowValue().setDescricao(event.getNewValue());
                atualizarTabelaItem();
                requestFocusItem();
            }
        });
        unidadeItem.setCellFactory(TextFieldTableCell.forTableColumn());
        unidadeItem.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ItemContrato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<ItemContrato, String> event) {
                event.getRowValue().setUnidade(event.getNewValue());
                atualizarTabelaItem();
                requestFocusItem();
            }
        });
        custoItem.setCellFactory(TextFieldTableCell.forTableColumn());
        custoItem.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ItemContrato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<ItemContrato, String> event) {
                event.getRowValue().setCusto(event.getNewValue());
                atualizarTabelaItem();
                requestFocusItem();
            }
        });
        tipoItem.setCellFactory(TextFieldTableCell.forTableColumn());
        tipoItem.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ItemContrato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<ItemContrato, String> event) {
                event.getRowValue().setTipo(event.getNewValue());
                atualizarTabelaItem();
                requestFocusItem();
            }
        });
        quantidadeItem.setCellFactory(TextFieldTableCell.forTableColumn());
        quantidadeItem.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ItemContrato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<ItemContrato, String> event) {
                try {
                    event.getRowValue().setQntTotal(stringToLong(quantidade, event.getNewValue()));
                    event.getRowValue().setPrecoTotal((event.getRowValue().getPrecoUnitario() * event.getRowValue().getQntTotal()) / 100);
                    atualizarTabelaItem();
                    requestFocusItem();
                } catch (ParseException ex) {
                    Logger.getLogger(MapaCotacaoAlterarController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        valorUnidadeItem.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                if (!string.contains("R$")) {
                    string = "R$ " + string;
                }
                return string;
            }

        }));
        valorUnidadeItem.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ItemContrato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<ItemContrato, String> event) {
                try {
                    event.getRowValue().setPrecoUnitario(stringToLong(dinheiro, event.getNewValue()));
                    event.getRowValue().setPrecoTotal((event.getRowValue().getPrecoUnitario() * event.getRowValue().getQntTotal()) / 100);
                    atualizarTabelaItem();
                    requestFocusItem();
                } catch (ParseException ex) {
                    Logger.getLogger(MapaCotacaoAlterarController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        valorTotalItem.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                if (!string.contains("R$")) {
                    string = "R$ " + string;
                }
                return string;
            }

        }));
        valorTotalItem.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ItemContrato, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<ItemContrato, String> event) {
                try {
                    event.getRowValue().setPrecoTotal(stringToLong(dinheiro, event.getNewValue()));
                    atualizarTabelaItem();
                    calcular();
                    requestFocusItem();
                } catch (ParseException ex) {
                    Logger.getLogger(MapaCotacaoAlterarController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    @FXML
    private void adicionarItem() {
        if (!editavel) {
            return;
        }
        Cliente.INSTANCIA.incluirItemContrato(mapa.getId());
        atualizarTabelaItem();
    }

    @FXML
    private void adicionarProduto() {
        if (item.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        //mapa.getItens().get(item.getSelectionModel().getSelectedIndex()).getProdutos().add(new Produto(0,0, "", "", "", 0.0, 0.0, 0.0));
        //contrato.getMapas().get(mapa).getItens().get(item.getSelectionModel().getSelectedIndex()).adicionarProduto(new ProdutoContrato(0));
        atualizarTabelaProduto();
    }

    @FXML
    private void duplicarItem() {
        if (!editavel) {
            return;
        }
        if (item.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        ItemContrato itemcon = item.getSelectionModel().getSelectedItem();

        mapa.adicionarItem(itemcon);
        atualizarTabelaItem();
    }

    @FXML
    private void duplicarProduto() {
        if (!editavel) {
            return;
        }
        if (produto.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        atualizarTabelaProduto();
    }

    @FXML
    private void removerItem() {
        if (!editavel) {
            return;
        }
        if (item.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        if (item.getSelectionModel().getSelectedItem().getValor() > 0) {
            Mensagem mensagem = new Mensagem("Excluir Item", "Não foi possivel excluir.");
            mensagem.setTitulo("Excluir");
            mensagem.showDialog();
            return;
        }
        int index = mapa.getItens().indexOf(item.getSelectionModel().getSelectedItem());
        mapa.getItens().remove(index);
        atualizarTabelaItem();
    }

    @FXML
    private void removerProduto() {
        if (!editavel) {
            return;
        }
        if (produto.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        mapa.getItens().get(item.getSelectionModel().getSelectedIndex()).removerProduto(produto.getSelectionModel().getSelectedItem());
        atualizarTabelaProduto();
    }

    @FXML
    private void limparItens() {
        if (!editavel) {
            return;
        }
        mapa.getItens().clear();
        atualizarTabelaItem();
    }

    /**
     * Metodo para limpar todos os produtos do item selecionado
     */
    @FXML
    private void limparProdutos() {
        if (!editavel) {
            return;
        }
        if (item.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        mapa.getItens().get(item.getSelectionModel().getSelectedIndex()).getProdutos().clear();
        atualizarTabelaProduto();
    }

    /**
     *
     * @param mapa
     */
    public void setMapa(MapaContrato mapa) {
        this.mapa = mapa;
    }

    /**
     * Inclui um novo mapa
     */
    public void incluirMapa() {
        // MapaContrato mapa = new MapaContrato(0);
        // contrato.adicionarMapa(mapa);
        //setMapa(mapa);
    }

    private void atualizar() {
        numero.setText(mapa.getNumero());
        emissao.setValue(Instant.ofEpochMilli(mapa.getCriacao().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        autor.setText(mapa.getAutor());
        fornecedor.setText(mapa.getEmpresa());
        descricao.setText(mapa.getDescricao());
        contratoCampo.setText(mapa.getContrato());
        atualizarCamposValor();
        atualizarTabelaItem();
    }

    private void atualizarCamposValor() {
        total.setText(longToString(dinheiro, mapa.getTotal()));
        gasto.setText(longToString(dinheiro, mapa.getValor()));
    }

    @FXML
    private void salvar() {
        try {
            atualizarContrato();
            int retorno = 0;
            switch (retorno) {
                case -1:
                    status.setText("Falhou em atualizar");
                    break;
                case 0:
                    status.setText("Erro na atualização");
                    break;
                case 1:
                    status.setText("Atualizado com sucesso");
                    break;
                default:
                    status.setText("");
                    break;
            }

        } catch (ParseException ex) {
        }

    }

    @FXML
    private void cancelar() {
        new CarregarGui().voltar(pai, modulo);

    }

    private void atualizarTabelaItem() {
        item.getItems().clear();
        item.setItems(FXCollections.observableArrayList(mapa.getItens()));
        calcular();
    }

    @FXML
    private void atualizarTabelaProduto() {
        produto.getItems().clear();
        produto.setItems(FXCollections.observableArrayList(mapa.getItens().get(item.getSelectionModel().getSelectedIndex()).getProdutos()));
    }

    private void requestFocusItem() {
        item.requestFocus();
        item.getSelectionModel().select(0);
        item.getFocusModel().focus(0);
    }

    private void requestFocusProduto() {
        produto.requestFocus();
        produto.getSelectionModel().select(0);
        produto.getFocusModel().focus(0);
    }

    private void atualizarContrato() throws ParseException {
        mapa.setNumero(numero.getText());
        mapa.setCriacao(Date.from(Instant.from(emissao.getValue().atStartOfDay(ZoneId.systemDefault()))));
        mapa.setTotal(stringToLong(dinheiro, total.getText()));
        mapa.setEmpresa(fornecedor.getText());
        mapa.setDescricao(descricao.getText());
        mapa.setValor(stringToLong(dinheiro, gasto.getText()));
        mapa.getItens().stream().forEach(s -> {
            s.setNumeroMapa(numero.getText());
            s.setDescricaoMapa(descricao.getText());
        });
    }

    private void calcular() {
        Calcular.calcularMapaContrato(mapa);
        atualizarCamposValor();
    }

    /**
     *
     * @param modulo
     */
    @Override
    public void setModuloVoltar(Modulos modulo) {
        this.modulo = modulo;
    }

    @FXML
    private void dadosAdicionais() {
        try {
            FXMLLoader loader = new FXMLLoader(MapaCotacaoAlterarController.class.getResource("DadosAdicionaisMapa.fxml")
            );
            AnchorPane anchor = (AnchorPane) loader.load();
            DadosAdicionaisMapaController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Dados adicionais");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(Main.getInstancia().getStage());
            controller.setStage(stage);
            controller.setObservacao(mapa.getObservacao());
            controller.setObservacaoAssinatura(mapa.getObservacaoAssinatura());
            controller.setObservacaoSistema(mapa.getObservacaoSistema());
            controller.setFisico(mapa.getFisico());
            controller.setEncerramento(mapa.getEncerramento());
            Scene scene = new Scene(anchor);
            stage.setScene(scene);
            stage.showAndWait();
            mapa.setObservacao(controller.getObservacao());
            mapa.setObservacaoAssinatura(controller.getObservacaoAssinatura());
            mapa.setObservacaoSistema(controller.getObservacaoSistema());
            mapa.setFisico(controller.getFisico());
            mapa.setEncerramento(controller.getEncerramento());
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setEditavel(boolean arg) {
        this.editavel = arg;
    }

    @Override
    public void succeeded() {
    }

}
