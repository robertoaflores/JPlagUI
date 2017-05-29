package edu.cnu.cs.jplagui.javafx;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import edu.cnu.cs.jplagui.common.Common;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class JPlagUIController {
    private Window mainWindow = null;
    
    @FXML private TextField         fieldLocation;
    @FXML private Button            buttonBrowse;
    @FXML private ChoiceBox<String> comboLanguage;
    @FXML private Button            buttonRun;
    @FXML private CheckBox          checkRecurse;
    @FXML private CheckBox          checkWebBrowser;
    @FXML private TextArea          areaOutput;

    @FXML
    void buttonBrowsePressed(ActionEvent event) {
		String string = fieldLocation.getText();
		File   start  = string.isEmpty() ? null : Paths.get( string ).toFile();

		DirectoryChooser chooser = new DirectoryChooser();
		if (start != null) {
			chooser.setInitialDirectory( start );
		}
		File selected = chooser.showDialog( mainWindow );
    	if (selected != null) {
    		fieldLocation.setText( selected.getAbsolutePath() );
    		buttonRun    .setDisable( false );
    		buttonRun    .requestFocus();
    	}
    }

    @FXML
    void buttonRunPressed(ActionEvent event) {
    	Alert         dialog = new Alert( AlertType.CONFIRMATION, "Running JPlag", ButtonType.CANCEL );
    	Service<Void> jplag  = new Service<Void>() {
    		@Override
    		protected Task<Void> createTask() {
    			return new Task<Void>() {
    				@Override
    				protected Void call() throws Exception {
    					areaOutput.setText( "Running JPlag..." );

    					String  path        = fieldLocation.getText();    					
    					String  language    = comboLanguage.getValue();
    					boolean isRecursive = checkRecurse .isSelected();
    					boolean inBrowser   = checkWebBrowser.isSelected();

    					String  output      = Common.runJPlag( path, language, isRecursive );
    					
    					if (!isCancelled()) {
    						areaOutput.setText( output );

    						if (inBrowser) {
    							String jplag = path + "/jplag";
    							Path   index = Paths.get( jplag + "/index.html" );
    							if (index.toFile().exists()) {
    								java.awt.Desktop.getDesktop().browse( index.toUri() );
    							}
    						}
    					}
    					return null;
    				}
    			};
    		}
    	};
    	MonitorService monitor = new MonitorService( dialog, jplag );
    	jplag.stateProperty().addListener((obs, old, now)->{
    		switch (now) {
    		case CANCELLED:	areaOutput.setText( "JPlag cancelled." ); break;
    		case FAILED   : areaOutput.setText( "JPlag failed." );    break;
    		default       : // nothing
    		}
    		if (jplag.getException() != null) {
    			Throwable exception = jplag.getException();
    			System.out.printf( "JPlag threw exception [%s] %s%n", exception.getClass().getSimpleName(), exception.getMessage() );
    		}
    	});
    	Button close = (Button)dialog.getDialogPane().lookupButton( ButtonType.CANCEL );
    	close.setOnAction( e->monitor.end() );

    	monitor.start();
    }

    @FXML
    void menuAbout(ActionEvent event) {
    	aboutDialog();
    }
    @FXML
    void menuQuit(ActionEvent event) {
    	if (quitDialog() == ButtonType.YES) {
    		Platform.exit();
    	}
    }

    @FXML
    void initialize() {
		buttonRun.sceneProperty().addListener( (a,b,c)-> {
			if (b==null && c!=null)
				c.windowProperty().addListener( (d,e,f)-> {
					if (e==null && f!=null) {
						mainWindow = f;
						mainWindow.setOnCloseRequest( g-> {
							if (quitDialog() != ButtonType.YES)
								g.consume();
						});
					}
				});
		});
	    buttonRun      .setDisable ( true );
    	comboLanguage  .setItems( FXCollections.observableArrayList( "java17","java15","java15dm","java12","java11","python3","c/c++","c#-1.2","char","text","scheme" ));
    	comboLanguage  .getSelectionModel().select( 0 );
    	checkRecurse   .setSelected( true );
    	checkWebBrowser.setSelected( true );
	    buttonBrowse   .requestFocus();
    }

	private ButtonType quitDialog() {
		Alert dialog = new Alert( AlertType.CONFIRMATION, "Want to quit?", ButtonType.YES, ButtonType.NO );
		dialog.showAndWait();
		return dialog.getResult();
	}
	private void aboutDialog() {
		Alert      dialog     = new Alert( AlertType.INFORMATION, "", ButtonType.OK );
		DialogPane dialogPane = dialog.getDialogPane();
		
		dialogPane.getStylesheets().add( getClass().getResource("JPlagUI.css").toExternalForm() );
		dialogPane.getStyleClass() .add( "aboutDialog" );
		
		dialog.setTitle      ("About");
	    dialog.setHeaderText ("JPlagUI v0.1 (JavaFX)");
	    dialog.setContentText("JPlagUI by Roberto A. Flores (roberto.flores@cnu.edu)\nJPlag by Mathias Landhäußer's (mathias.landhaeusser@kit.edu) <https://github.com/jplag/>");
	    dialog.showAndWait();		
	}
}
