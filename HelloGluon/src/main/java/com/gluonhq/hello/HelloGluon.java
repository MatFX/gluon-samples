/*
 * Copyright (c) 2019, 2021, Gluon
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of Gluon, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL GLUON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gluonhq.hello;

import com.gluonhq.attach.display.DisplayService;
import com.gluonhq.attach.orientation.OrientationService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.attach.util.Services;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Dimension2D;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import static com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class HelloGluon extends Application {

    private final AppManager appManager = AppManager.initialize(this::postInit);

    private Label label;
    
    private TextField textField;
    
    private Label labelOrientation;
    
    @Override
    public void init() {
    	
    	
    	//service test ios
    	Services.get(OrientationService.class).ifPresent(service -> 
    	{
    		 service.orientationProperty().addListener(new ChangeListener<Orientation>() {

    			@Override
    			public void changed(ObservableValue<? extends Orientation> observable, Orientation oldValue,
    					Orientation newValue)
    			{
    				System.out.println("newValue " + newValue);
    				System.out.println("oldValue " + oldValue);
    				if(!newValue.equals(oldValue))
    				{
    					//Bei Aenderung ist diese Info an die View weiterzugeben
    					View currentView = appManager.getView();
    					/*
    					if(currentView != null && currentView instanceof IOrientationChange)
    					{
    						BasicView.fillDefaultDimension();
    						
    						//herumgewurschtel für den Fall die Dimension passt nicht mit der Orientierung zusammen,
    						//ist bei meinem alten Android Handy passiert.
    						if(newValue == Orientation.VERTICAL && BasicView.defaultDimension.getWidth() > BasicView.defaultDimension.getHeight())
    						{
    							BasicView.defaultDimension = new Dimension2D(BasicView.defaultDimension.getHeight(), BasicView.defaultDimension.getWidth());
    						}
    						else if(newValue == Orientation.HORIZONTAL && BasicView.defaultDimension.getHeight() > BasicView.defaultDimension.getWidth())
    						{
    							BasicView.defaultDimension = new Dimension2D(BasicView.defaultDimension.getHeight(), BasicView.defaultDimension.getWidth());
    						}
    						((IOrientationChange)currentView).orientationChanged();
    					}*/
    					
    				}
    				
    			}
    			  
    		  });
    		
    	  });
    	
    	
    	
    	
        appManager.addViewFactory(HOME_VIEW, () -> {
            FloatingActionButton fab = new FloatingActionButton(MaterialDesignIcon.SEARCH.text,
                    e -> System.out.println("Search"));

            ImageView imageView = new ImageView(new Image(HelloGluon.class.getResourceAsStream("openduke.png")));

            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
            
            label = new Label("Hello, Gluon Mobile!");
            
            labelOrientation = new Label("Orientation label");
            
            
            //example content: 192.168.150.81 or localhost 
            textField = new TextField("localhost");
            
            
            Button button = new Button("Connection");
            button.setOnAction(new EventHandler<ActionEvent>() 
            {

				@Override
				public void handle(ActionEvent event) 
				{
					  label.setText("Connection test.");
					  connectToServer();
					  event.consume();
					
				}
            	
            });

          
            VBox root = new VBox(20, imageView, label, labelOrientation, textField, button);
            root.setAlignment(Pos.CENTER);

            View view = new View(root) {
                @Override
                protected void updateAppBar(AppBar appBar) {
                    appBar.setTitleText("Gluon Mobile");
                }
            };

            fab.showOn(view);

            return view;
        });
    }
    
    protected void connectToServer() 
    {
    	
    	new Thread(new Runnable()
		{

			@Override
			public void run() {
				try
				{
					
					javafx.application.Platform.runLater(() -> setTextValue("Build socket."));
					
					
					//Socket socket = new Socket("192.168.150.81", 4711);
					//Socket socket = new Socket("localhost", 4711);
					Socket socket = new Socket(textField.getText().trim(), 4711);
					
					final InputStream rein = socket.getInputStream();
					
					javafx.application.Platform.runLater(() -> setTextValue("got inputstream"));
					BufferedReader buff = new BufferedReader(new InputStreamReader(rein));
					 
					String val = "buff ready? "+buff.ready();
					 
					javafx.application.Platform.runLater(() -> setTextValue(val));
						
					String readedLine = buff.readLine();
					    
					while(readedLine != null)
					{
				    	readedLine = buff.readLine();
				    	
				    	String valueToSet = readedLine;
				    	
				    	javafx.application.Platform.runLater(() -> setTextValue(valueToSet));
				    	
				    	Thread.sleep(1000);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					String except = e.getMessage();
					javafx.application.Platform.runLater(() -> setTextValue(except));
				}
				
			}
    		
		}).start();
   
    }

	protected void setTextValue(String string) {
		label.setText(string);
	}

    @Override
    public void start(Stage stage) {
        appManager.start(stage);
    }

    private void postInit(Scene scene) {
        Swatch.LIGHT_GREEN.assignTo(scene);
        scene.getStylesheets().add(HelloGluon.class.getResource("styles.css").toExternalForm());

        if (Platform.isDesktop()) {
            Dimension2D dimension2D = DisplayService.create()
                    .map(DisplayService::getDefaultDimensions)
                    .orElse(new Dimension2D(640, 480));
            scene.getWindow().setWidth(dimension2D.getWidth());
            scene.getWindow().setHeight(dimension2D.getHeight());
        }
    }

    public static void main(String[] args) {
        launch();
    }

}