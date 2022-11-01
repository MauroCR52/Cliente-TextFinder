package com.example.clientetextfinder;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.swing.*;
import java.awt.Desktop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ScrollPane ScrollMain;
    @FXML
    private Button enviarButton;
    @FXML
    private TextField mensajeText;
    @FXML
    private VBox mensajeVBOX;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Button agregarButton;
    @FXML
    private MenuItem fechaButton;
    @FXML
    private ListView<String> listView;
    @FXML
    private MenuItem nomButton;
    @FXML
    private MenuItem tamButton;

    @FXML
    private Button abrirButton;
    @FXML
    private Button verButton;

    private Cliente cliente;

    private String documento;

    @FXML
    private Button borrarButton;


    public static ObservableList<String> list = FXCollections.observableArrayList();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            cliente = new Cliente(new Socket("localhost", 3421));
            System.out.println("Conectado a servidor");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mensajeVBOX.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                ScrollMain.setVvalue((Double) newValue);
            }
        });
        cliente.receiveMessageFromServer(mensajeVBOX);
        enviarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Biblioteca.biblioteca.borrarLista();
                listView.getItems().clear();
                String messageToSend = mensajeText.getText();
                if (!messageToSend.isEmpty()) {
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                    hBox.setPadding(new Insets(5, 5, 5, 10));
                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle("-fx-color: rgb(239,242,255); -fx-background-color: rgb(15,125,242);-fx-background-radius: 20px");
                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    text.setFill(Color.color(0.934, 0.945, 0.996));
                    hBox.getChildren().add(textFlow);
                    mensajeVBOX.getChildren().add(hBox);
                    cliente.sendMessageToServer(messageToSend);
                    mensajeText.clear();
                }
            }
        });
    }

    public static void addLabel(String msgFromServer, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));
        Text text = new Text(msgFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233,233,235);-fx-background-radius: 20px");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }


    @FXML
    void verDocs() {
        ObservableList<String> list = FXCollections.observableArrayList();
        Nodo_Biblioteca actual = Biblioteca.biblioteca.head;
        while (actual != null) {
            list.add(actual.getData().getNombre()+"  "+"Fecha: "+actual.getData().getFechadef()+ "  "+"Tamaño: "+actual.getData().getTamano());
            actual = actual.next;
        }
        listView.setItems(list);
    }
    @FXML
    void abrirDoc(ActionEvent event) throws IOException {
        documento = listView.getSelectionModel().getSelectedItem();
        String arr[] = documento.split(" ", 2);
        Desktop.getDesktop().open(new File("C:\\Users\\maulu\\IdeaProjects\\Text_Finder\\src\\main\\java\\Documentos\\"+arr[0]));
    }

    @FXML
    void ordenarFecha(ActionEvent event) {
        Biblioteca.biblioteca.ordenar_fecha();
        verDocs();
    }

    @FXML
    void ordenarNombre(ActionEvent event) {
        Nodo_Biblioteca n = Biblioteca.biblioteca.head;
        while (n.next != null)
            n = n.next;
        Biblioteca.biblioteca.ordenar_nombre(Biblioteca.biblioteca.head, n);
        verDocs();
    }

    @FXML
    void ordenarTamano(ActionEvent event) {
        Biblioteca.biblioteca.listaRadix();
        verDocs();
    }

    @FXML
    void agregarDocs(ActionEvent event) throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(null);
        File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
        Files.copy(Paths.get(file.getAbsolutePath()), Paths.get("C:\\Users\\maulu\\IdeaProjects\\Text_Finder\\src\\main\\java\\Documentos\\"+file.getName()), StandardCopyOption.REPLACE_EXISTING);
    }


    @FXML
    void borrarDoc(ActionEvent event) {
        documento = listView.getSelectionModel().getSelectedItem();
        String arr[] = documento.split(" ", 2);
        File file = new File("C:\\Users\\maulu\\IdeaProjects\\Text_Finder\\src\\main\\java\\Documentos\\"+arr[0]);
        if (file.delete()){
            System.out.println("Archivo borrado");
        }
        else {
            System.out.println("Error al borrar");
        }
        Biblioteca.biblioteca.borrar(arr[0]);
        verDocs();


    }
}