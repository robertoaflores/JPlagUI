package edu.cnu.cs.jplagui.javafx;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class JPlagUI extends Application {
	private static JPlagUI singleton = null;
	@Override
	public void start(Stage stage) {
		singleton = this;
		stage.setTitle("JPlag User Interface");
//	    stage.setOnCloseRequest( event->event.consume() );
	    stage.setOnCloseRequest( new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				if (quitDialog() != ButtonType.YES) {
				    event.consume();
				}
			}
	    });
	    try {
			Parent root  = FXMLLoader.load( getClass().getResource("JPlagUI.fxml"));
		    Scene  scene = new Scene( root ); 
		    stage.setScene( scene );

		} catch (IOException e) {
			e.printStackTrace();
		} 
		stage.show();
	}
	public static JPlagUI getInstance() {
		return singleton;
	}
	public ButtonType quitDialog() {
		Alert dialog = new Alert( AlertType.CONFIRMATION, "Want to quit?", ButtonType.YES, ButtonType.NO );
		dialog.showAndWait();
		return dialog.getResult();
	}
	public void aboutDialog() {
		Alert      dialog     = new Alert( AlertType.INFORMATION, "", ButtonType.OK );
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getStylesheets().add( getClass().getResource("JPlagUI.css").toExternalForm() );
		dialogPane.getStyleClass().add("aboutDialog");
		dialog.setTitle("About");
	    dialog.setHeaderText("JPlagUI v0.1 (JavaFX)");
	    dialog.setContentText("By Roberto A. Flores (roberto.flores@cnu.edu)\nJPlag || Mathias Landhäußer's (mathias.landhaeusser@kit.edu) || https://github.com/jplag/");
	    dialog.showAndWait();		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
