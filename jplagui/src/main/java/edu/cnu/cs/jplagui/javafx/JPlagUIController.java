package edu.cnu.cs.jplagui.javafx;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;

public class JPlagUIController {

    @FXML private ResourceBundle resources;
    @FXML private URL            location;
    @FXML private ScrollPane     scroll;
    @FXML private ChoiceBox<String>   language;
    @FXML private CheckBox       recurse;
    @FXML private CheckBox       webBrowser;
    @FXML private Button         browse;
    @FXML private Button         run;
    
    @FXML
    void browsePressed(ActionEvent event) {

    }

    @FXML
    void languageSelected(MouseEvent event) {

    }

    @FXML
    void menuAbout(ActionEvent event) {

    }

    @FXML
    void menuQuit(ActionEvent event) {
//    	Node  source = (Node) event .getSource();
//    	Stage stage  = (Stage)source.getScene().getWindow();
//    	stage.close();
    	Platform.exit();
    }

    @FXML
    void recurseSelected(ActionEvent event) {

    }

    @FXML
    void runPressed(ActionEvent event) {

    }

    @FXML
    void webBrowserSelected(ActionEvent event) {

    }

    @FXML
    void initialize() {
    	language  .setItems( FXCollections.observableArrayList( "java17","java15","java15dm","java12","java11","python3","c/c++","c#-1.2","char","text","scheme" ));
    	language  .getSelectionModel().select( 0 );
    	recurse   .setSelected( true );
    	webBrowser.setSelected( true );
    }
}
