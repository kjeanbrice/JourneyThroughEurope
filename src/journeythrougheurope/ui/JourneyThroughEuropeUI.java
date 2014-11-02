/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.ui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Karl
 */
public class JourneyThroughEuropeUI extends Pane {

    private Stage primaryStage;
    private BorderPane mainPane;

    
    public BorderPane getMainPane() {
        return this.mainPane;
    }

    public void setStage(Stage stage) {
        primaryStage = stage;
    }

}
