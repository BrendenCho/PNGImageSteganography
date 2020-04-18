/*

 */
package imagesteganography;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Brenden Cho
 */
public class Password {

    MainWindow mw;
    VBox mainBox = new VBox();
    HBox passBox = new HBox();
    Stage stage = new Stage();
    Scene scene = new Scene(mainBox, 300, 150);
    Text pass = new Text("Create Password");
    PasswordField passField = new PasswordField();
    Text passConfirm = new Text("Confirm Password");
    PasswordField passConfirmField = new PasswordField();
    Button bttn = new Button("Go");

    public Password(MainWindow mw) {
        this.mw = mw;
        bttn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                String password = passField.getText();
                String confirm = passField.getText();

                if (password.equals(confirm) && !password.equals("")) {
                    mw.getImageout().setPassword(password);
                    mw.getImageout().setEncrypt(true);
                    stage.close();
                } else {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setTitle("Brenden Cho");
                    if (password.equals("")) {
                        a.setContentText("Password is empty please try again");
                    } else {
                        a.setContentText("Passwords do not macth please try again");
                    }
                    a.show();
                }

            }

        });

        stage.setOnCloseRequest(e -> {

            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Brenden Cho");
            a.setContentText("Password was not entered. Encryption will not be applied.");
            a.show();
            mw.getImageout().setUp();

            mw.getCharsLeft().setText(Integer.toString(mw.getImageout().getMaxUserChars() + 1) + " left");
            mw.getImageOutText().setText("");
            mw.getEncryptRadio().setSelected(false);

        });

        passBox.getChildren().add(bttn);
        passBox.setAlignment(Pos.CENTER);

        passField.setMaxWidth(200);
        passConfirmField.setMaxWidth(200);

        mainBox.setSpacing(2);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.getChildren().add(pass);
        mainBox.getChildren().add(passField);
        mainBox.getChildren().add(passConfirm);
        mainBox.getChildren().add(passConfirmField);
        mainBox.getChildren().add(passBox);

        stage.setHeight(150);
        stage.setWidth(300);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Brenden Cho");
        stage.setScene(scene);
        stage.show();

    }

}
