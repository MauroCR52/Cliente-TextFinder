package com.example.clientetextfinder;

import com.spire.doc.Document;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;


    public static String messageFromServer;
    public Cliente(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Error al crear cliente");
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessageToServer(String messageToServer) {
        try {
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error enviando mensaje al cliente");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    public void receiveMessageFromServer(VBox vBox) {
        ObservableList<String> list = FXCollections.observableArrayList();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected())
                    try {
                        messageFromServer = bufferedReader.readLine();
                        String arr[] = Cliente.messageFromServer.split(" ");
                        list.add(arr[0]);

                        Controller.addLabel(messageFromServer, vBox);

                        try {
                            leerdoc(arr[0]);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error recibiendo mensaje del cliente");
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }


            }
        }).start();

    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null)
                bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null)
                socket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void leerdoc(String doc) throws IOException {
        String path = "C:\\Users\\maulu\\IdeaProjects\\Text_Finder\\src\\main\\java\\Documentos\\";
        File file = new File(path + doc);
        int cont = 0;
        int index = doc.lastIndexOf(".");
        if (index > 0) {
            String extension = doc.substring(index + 1);
            if (extension.equals("docx")) {
                Document document = new Document();
                document.loadFromFile(path + doc);
                cont = document.getBuiltinDocumentProperties().getWordCount();
            } else if (extension.equals("txt")) {
                String line;
                FileReader fileReader = new FileReader(file.getPath());
                BufferedReader br = new BufferedReader(fileReader);
                while ((line = br.readLine()) != null) {
                    String words[] = line.split(" ");
                    cont += words.length;
                }
                br.close();
            } else {
                PDDocument document = PDDocument.load(file);
                //Instantiate PDFTextStripper class
                PDFTextStripper pdfStripper = new PDFTextStripper();
                //Retrieving text from PDF document
                String s = pdfStripper.getText(document);
                String cad[] = s.split("\r\n", -1);
                int i = 0;
                int z = 0;
                StringBuilder textoT = new StringBuilder();
                while (i != cad.length) {
                    String cad2[] = cad[i].split(" ", -1);
                    int j = 0;
                    while (j != cad2.length) {
                        if (cad2[j] != "") {
                            textoT.append(cad2[j]);
                            textoT.append(" ");
                        }
                        j += 1;
                    }
                    i += 1;
                }
                String[] cad3 = textoT.toString().split(" ", -1);
                while (z != cad3.length) {
                    if (cad3[z] != "") {
                        String posicion = String.valueOf(textoT.indexOf(cad3[z]));
                    }
                    z += 1;
                    cont += 1;
                }
                document.close();

            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHH");
            SimpleDateFormat fecha = new SimpleDateFormat("yyyy/MM/dd");

            Documento documento = new Documento(doc, path + doc, cont, Integer.parseInt(sdf.format(file.lastModified())), doc.substring(index + 1), fecha.format(file.lastModified()));
            Biblioteca.biblioteca.InsertarDocumento(documento);

        }
    }

}
