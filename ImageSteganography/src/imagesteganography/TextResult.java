/*

 */
package imagesteganography;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Brenden Cho
 */
public class TextResult {

    TextArea textarea = new TextArea();
    ScrollPane scrollpane = new ScrollPane(textarea);
    Stage stage = new Stage();
    Scene scene = new Scene(scrollpane, 1000, 500);

    public TextResult(String s) {
        textarea.setEditable(false);
        scrollpane.setFitToHeight(true);
        scrollpane.setFitToWidth(true);
        textarea.setText(s);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Brenden Cho");
        stage.setWidth(1000);
        stage.setHeight(500);
        stage.setResizable(true);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.show();
    }

}
