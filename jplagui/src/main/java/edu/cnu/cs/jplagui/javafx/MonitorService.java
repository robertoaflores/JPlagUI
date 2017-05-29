package edu.cnu.cs.jplagui.javafx;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

public class MonitorService extends Service<Void> {
    private  Stage         stage;
    private  Service<Void> service;

    public MonitorService(Dialog<?> dialog, Service<Void> service) {
    	this( (Stage)dialog.getDialogPane().getScene().getWindow(), service );
    }
    public MonitorService(Stage stage, Service<Void> service) {
		this.stage   = stage;
		this.service = service;
        this.service.stateProperty().addListener((obs,old,now)-> {
        	if (now==State.SUCCEEDED) {
        		end();
        	}
        });
        this.stage.setOnCloseRequest( e->end() );
    }

    @Override
    protected Task<Void> createTask() {
         Task<Void> showTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(()->{
                	stage  .show();
                	service.restart();
                });
                return null;
            }
        };
        showTask.stateProperty().addListener( (obs,old,now)-> {
        	switch (now) {
        	case FAILED   :
        	case CANCELLED: end();
			default       : // nothing
        	}
        });
        return showTask;
    }
    public void end() {
    	Platform.runLater( new Task<Void>() {
    		@Override
    		protected Void call() throws Exception {
    			stage  .hide();
    			service.cancel();
    			return null;
    		}
    	});
    }
}