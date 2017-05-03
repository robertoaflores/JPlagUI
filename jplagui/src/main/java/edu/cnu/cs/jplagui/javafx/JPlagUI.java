package edu.cnu.cs.jplagui.javafx;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JPlagUI extends Application {

	@Override
	public void start(Stage stage) {
		stage.setTitle("JPlag User Interface");
	    stage.setOnCloseRequest( event->event.consume() );
	    try {
			Parent root  = FXMLLoader.load( getClass().getResource("JPlagUI.fxml"));
		    Scene  scene = new Scene( root ); 
		    stage.setScene( scene );

		} catch (IOException e) {
			e.printStackTrace();
		} 
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
