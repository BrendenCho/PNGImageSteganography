/*

 */
package imagesteganography;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Brenden Cho
 */
public class IsEncrypted {

    MainWindow mw;
    VBox mainBox = new VBox();
    HBox bttnBox = new HBox();
    Stage stage = new Stage();
    Scene scene = new Scene(mainBox, 220, 120);
    Button yes = new Button("Yes");
    Button no = new Button("No");
    Text is = new Text("Is the image encrypted?");
//pass
    VBox passBox = new VBox();
    Scene pass = new Scene(passBox, 220, 120);
    Text enter = new Text("Enter Password");
    PasswordField field = new PasswordField();
    Button passGo = new Button("Go");

    public IsEncrypted(MainWindow mw) {
        this.mw = mw;

        no.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                mw.getImagein().start();
                stage.close();
            }

        });

        yes.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                stage.setScene(pass);

            }

        });

        passGo.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                String temp = field.getText();
                if (temp.equals("")) {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setTitle("Brenden Cho");
                    a.setContentText("Password can't be blank");
                    a.show();
                } else {
                    mw.getImagein().setPassword(temp);
                    mw.getImagein().setEncrypt(true);
                    mw.getImagein().start();
                    stage.close();

                }

            }

        });

        field.setMaxWidth(180);
        enter.setTextAlignment(TextAlignment.LEFT);

        passBox.setAlignment(Pos.CENTER);
        passBox.setSpacing(5);
        passBox.setPadding(new Insets(5, 5, 5, 5));
        passBox.getChildren().add(enter);
        passBox.getChildren().add(field);
        passBox.getChildren().add(passGo);

        bttnBox.setSpacing(5);
        bttnBox.getChildren().add(yes);
        bttnBox.getChildren().add(no);
        bttnBox.setAlignment(Pos.CENTER);

        mainBox.setSpacing(5);
        mainBox.setPadding(new Insets(5, 5, 5, 5));
        mainBox.setAlignment(Pos.CENTER);
        mainBox.getChildren().add(is);
        mainBox.getChildren().add(bttnBox);

        stage.setOnCloseRequest(e -> {

            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Brenden Cho");
            a.setContentText("The window was closed. Decryption will not occur");
            a.show();
            mw.getImagein().reset();
        });

        stage.setWidth(220);
        stage.setHeight(120);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Brenden Cho");
        stage.show();

    }

}
