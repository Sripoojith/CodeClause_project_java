package com.codeclause;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;



public class Controller implements Initializable {
    @FXML
    private TextArea textArea;
    private Stage stage;
    private final FileChooser fileChooser = new FileChooser();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("text","*.txt")
        ,new FileChooser.ExtensionFilter("All","*.*"));
    }
    // Event handler for opening a file
    @FXML
    public void openFile() {
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            textArea.clear();
            readText(file);
        }
    }
    // Event handler for creating a new document
    @FXML
    public void New(){
        textArea.clear();
    }
    // Event handler for saving a document
    @FXML
    private void save() {
        try {
            fileChooser.setTitle("Save As");
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                PrintWriter savedText = new PrintWriter(file);
                BufferedWriter out = new BufferedWriter(savedText);
                out.write(textArea.getText());
                out.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Event handler for creating a new document
    private void readText(File file){
        String text;
        try {
            FileInputStream fis = new FileInputStream(file);
            int i = 0;
            while ((i = fis.read())!= -1) {
                textArea.appendText((char) i+"");
            }
            fis.close();
        }catch (IOException x){
        }
    }
    // Event handler for changing the font size of the text
    @FXML
    public void fontSize(ActionEvent e) {
        String choice = ((RadioMenuItem) e.getSource()).getId();
        switch (choice) {
            case "small":
                textArea.setStyle("-fx-font-size: 14px");
                break;
            case "default":
                textArea.setStyle("-fx-font-size: 22px");
                break;
            case "large":
                textArea.setStyle("-fx-font-size: 30px");
                break;
            default:
                textArea.setStyle("-fx-font-size: 22px");
        }
    }
    // Event handler for save as a pdf
    @FXML
    public void print() {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);

        // create a TextFlow with the contents of the TextArea
        TextFlow textFlow = new TextFlow(new Text(textArea.getText()));

        // calculate the scale to fit the TextFlow onto the page
        double scaleX = pageLayout.getPrintableWidth() / textFlow.prefWidth(-1);
        double scaleY = pageLayout.getPrintableHeight() / textFlow.prefHeight(-1);
        double scale = Math.min(scaleX, scaleY);

        // apply the scale to the TextFlow
        textFlow.getTransforms().add(new Scale(scale, scale));

        // create a PrinterJob and print the TextFlow
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean success = job.printPage(pageLayout, textFlow);
            if (success) {
                job.endJob();
            }
            // remove the scale from the TextFlow
            textFlow.getTransforms().remove(textFlow.getTransforms().size() - 1);
        }
    }
    @FXML
    public void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("About");
        alert.setHeaderText("My name is T.Sri poojith and This is my internship project");
        alert.setContentText("https://github.com/Sripoojith");
        alert.showAndWait();
    }
}