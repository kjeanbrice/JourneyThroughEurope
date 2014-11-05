/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import journeythrougheurope.application.Main.JourneyThroughEuropePropertyType;
import properties_manager.PropertiesManager;

/**
 *
 * @author Karl
 */
public class JourneyThroughEuropeErrorHandler 
{
 
    private Stage primaryStage;
    
    public JourneyThroughEuropeErrorHandler(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
    }
    
    
     public void processError(JourneyThroughEuropePropertyType errorType) {
        // GET THE FEEDBACK TEXT
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String errorFeedbackText = props.getProperty(errorType);

        // NOTE THAT WE'LL USE THE SAME DIALOG TITLE FOR ALL ERROR TYPES
        String errorTitle = props.getProperty(JourneyThroughEuropePropertyType.ERROR_DIALOG_TITLE_TEXT);

        // POP OPEN A DIALOG TO DISPLAY TO THE USER
        //JOptionPane.showMessageDialog(window, errorFeedbackText, errorTitle, JOptionPane.ERROR_MESSAGE);
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        dialogStage.setTitle(errorTitle);
        VBox vbox = new VBox();
        vbox.setSpacing(10.0);
        Label errLabel = new Label(errorFeedbackText);
        Button errButton = new Button("confirm");
        vbox.getChildren().addAll(errLabel, errButton);

        Scene scene = new Scene(vbox, 50, 30);
        dialogStage.setScene(scene);
        dialogStage.show();
    }
}
