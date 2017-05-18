package edu.cnu.cs.jplagui.javafx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class JPlagUIController {
    @FXML private TextField         fieldLocation;
    @FXML private Button            buttonBrowse;
    @FXML private ChoiceBox<String> comboLanguage;
    @FXML private Button            buttonRun;
    @FXML private CheckBox          checkRecurse;
    @FXML private CheckBox          checkWebBrowser;
    @FXML private TextArea          areaOutput;

    @FXML
    void buttonBrowsePressed(ActionEvent event) {
    	System.out.println("browse folder");
    }

    @FXML
    void buttonRunPressed(ActionEvent event) {
    	System.out.println("run");
    }

    @FXML
    void checkRecurseSelected(ActionEvent event) {
    	System.out.println("recursive");
    }

    @FXML
    void checkWebBrowserSelected(ActionEvent event) {
    	System.out.println("web browser");
    }

    @FXML
    void comboLanguageSelected(MouseEvent event) {
    	System.out.println("language");
    }

    @FXML
    void menuAbout(ActionEvent event) {
//    	System.out.println("about");
    	JPlagUI.getInstance().aboutDialog();
    }

    @FXML
    void menuQuit(ActionEvent event) {
//    	System.out.println("quit");
    	if (JPlagUI.getInstance().quitDialog() == ButtonType.YES) {
    		Platform.exit();
    	}
    }

    @FXML
    void initialize() {
    	System.out.println("initializing");
    	comboLanguage.setItems( FXCollections.observableArrayList( "java17","java15","java15dm","java12","java11","python3","c/c++","c#-1.2","char","text","scheme" ));
    	comboLanguage.getSelectionModel().select( 0 );
    }
}
