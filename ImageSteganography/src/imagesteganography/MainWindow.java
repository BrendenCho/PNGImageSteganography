package imagesteganography;

import java.io.File;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Brenden Cho
 */
public class MainWindow {

    ImageOut imageout;
    ImageIn imagein;

    private TabPane tabPane = new TabPane();
    private Scene scene = new Scene(tabPane, 375, 800);
    private Stage stage = new Stage();
    private Tab imageIn = new Tab("Image Encrypt");
    private Tab imageOut = new Tab("Image Decrypt");
//imageOut
    private VBox outBox = new VBox();
    private HBox pngBox = new HBox();
    private Text pngLabel = new Text("PNG/JPG/JPEG file to encode");
    private Button pngSelectBttn = new Button("Choose Image");
    private Text pngName;
    private ProgressBar outProgress = new ProgressBar(0);
//imageIn
    private VBox inBox = new VBox();
    private HBox pngInBox = new HBox();
    private Text pngInText = new Text("Png to decode");
    private Button pngInSelectBttn = new Button("Select Image");
    private Text pngTextName;
    private ProgressBar inProgress = new ProgressBar(0);
    private Text note = new Text("  If your image is encrypted and you enter the wrong password the text will be blank");
//ImageInTextBox
    private VBox imageOutTextBoxControls = new VBox();
    private HBox imageOutTextBox = new HBox();
    private TextArea imageOutText = new TextArea();
    private ScrollPane imageOutTextPane = new ScrollPane();
    private Text enterText = new Text("Message Controls");
    private Button imageOutTextGoBttn = new Button("Encode");
    private Text charsLeft = new Text();
    private HBox encryptBox = new HBox();
    private RadioButton encryptRadio = new RadioButton();

    public MainWindow() {

        imageOutTextPane.setContent(imageOutText);

        imageOutTextGoBttn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                try {
                    String input = imageOutText.getText();
                    if (!input.equals("")) {
                        imageout.setUserInput(input);
                        imageout.start();
                    } else {
                        Alert a = new Alert(AlertType.INFORMATION);
                        a.setTitle("Brenden Cho");
                        a.setContentText("Please enter some text");
                        a.show();
                    }
                } catch (NullPointerException ee) {
                    Alert a = new Alert(AlertType.INFORMATION);
                    a.setTitle("Brenden Cho");
                    a.setContentText("Please select an image");
                    a.show();
                }
            }

        });

        pngSelectBttn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                try {
                    imageOutText.setText("");
                    outProgress.setProgress(0);
                    pngBox.getChildren().remove(pngName);
                    FileChooser fc = new FileChooser();
                    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("png/jpg/jpeg", "*.png", "*jpg", "*jpeg");
                    fc.getExtensionFilters().add(filter);
                    File image = fc.showOpenDialog(stage);
                    imageout = new ImageOut(image, MainWindow.this);
                    imageout.setImage(image);
                    imageout.setUp();
                    charsLeft.setText(Integer.toString(imageout.getMaxUserChars() + 1) + " left");
                    System.out.println(imageout.getMaxUserChars());
                    System.out.println(image.toString() + " set.");
                    pngName = new Text(imageout.getImage().toString());
                    pngBox.getChildren().add(pngName);

                } catch (Exception npe) {
                    System.out.println("No File Selected");
                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("No File was selected");
                    a.setTitle("Brenden Cho");
                    a.show();
                }
            }

        });

        pngInSelectBttn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                try {

                    pngInBox.getChildren().remove(pngTextName);
                    FileChooser fc = new FileChooser();
                    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("PNG File", "*.png");
                    fc.getExtensionFilters().add(filter);
                    File image = fc.showOpenDialog(stage);
                    imagein = new ImageIn(image, MainWindow.this);
                    System.out.println(image.toString() + " set.");
                    pngTextName = new Text(imagein.getImage().toString());
                    pngInBox.getChildren().add(pngTextName);
                    IsEncrypted i = new IsEncrypted(MainWindow.this);
                } catch (Exception npe) {
                    System.out.println("No File Selected");
                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("No File was selected");
                    a.setTitle("Brenden Cho");
                    a.show();
                }
            }

        });

        imageOutText.setOnKeyTyped(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent e) {
                try {
                    int curr = imageOutText.getText().length();
                    int left = imageout.getMaxUserChars() - curr;

                    if (left < 0) {
                        String text = imageOutText.getText();
                        while (left != 0) {
                            text = text.substring(0, text.length() - 1);
                            curr = text.length();
                            left = imageout.getMaxUserChars() - curr;
                        }
                        imageOutText.setText(text);
                    }
                    charsLeft.setText(Integer.toString(left) + " left");
                } catch (NullPointerException eee) {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Please Select an image");
                    a.setTitle("Brenden Cho");
                    a.show();
                }
            }

        });

        encryptRadio.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent e) {
                boolean status = encryptRadio.isSelected();
                try {
                    if (status == true) {
                        imageout.setUpEncrypt();
                        Password p = new Password(MainWindow.this);
                    } else {
                        imageout.setUp();
                    }
                    charsLeft.setText(Integer.toString(imageout.getMaxUserChars() + 1) + " left");
                    imageOutText.setText("");

                } catch (NullPointerException eeee) {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Please Select an image");
                    a.setTitle("Brenden Cho");
                    a.show();
                    encryptRadio.setSelected(false);
                }
            }

        });

        encryptBox.getChildren().add(new Text("Encrypt"));
        encryptBox.getChildren().add(encryptRadio);
        encryptBox.setAlignment(Pos.CENTER);
        encryptBox.setSpacing(5);

        imageOutTextBoxControls.setSpacing(5);
        imageOutTextBoxControls.setAlignment(Pos.TOP_CENTER);
        imageOutTextBoxControls.setPadding(new Insets(0, 5, 5, 5));
        imageOutTextBoxControls.getChildren().add(enterText);
        imageOutTextBoxControls.getChildren().add(encryptBox);
        imageOutTextBoxControls.getChildren().add(imageOutTextGoBttn);

        VBox ascii = new VBox();
        ascii.getChildren().add(new Text(""));
        ascii.getChildren().add(new Text("Non Ascii Characters will be Removed"));
        ascii.setPadding(new Insets(0, 5, 5, 5));
        ascii.setSpacing(5);
        ascii.setAlignment(Pos.TOP_CENTER);

        imageOutTextPane.setPadding(new Insets(5, 5, 5, 5));
        imageOutTextBox.setSpacing(5);
        imageOutTextBox.setPadding(new Insets(5, 5, 5, 5));
        imageOutTextBox.getChildren().add(imageOutTextPane);
        imageOutTextBox.getChildren().add(imageOutTextBoxControls);
        imageOutTextBox.getChildren().add(ascii);

        pngBox.setAlignment(Pos.CENTER_LEFT);
        pngBox.setPadding(new Insets(5, 5, 5, 5));
        pngBox.setSpacing(5);
        pngInBox.setPadding(new Insets(5, 5, 5, 5));
        pngInBox.setSpacing(5);
        pngInBox.setAlignment(Pos.BASELINE_LEFT);

        pngBox.getChildren().add(pngLabel);
        pngBox.getChildren().add(pngSelectBttn);
        pngInBox.getChildren().add(pngInText);
        pngInBox.getChildren().add(pngInSelectBttn);

        outProgress.setPadding(new Insets(5, 5, 5, 5));
        outProgress.setPrefWidth(500);
        inProgress.setPadding(new Insets(5, 5, 5, 5));
        inProgress.setPrefWidth(500);

        inBox.setPadding(new Insets(5, 5, 5, 5));
        inBox.setSpacing(5);
        inBox.getChildren().add(pngInBox);
        inBox.getChildren().add(inProgress);
        inBox.getChildren().add(note);

        outBox.setSpacing(5);
        outBox.setPadding(new Insets(5, 5, 5, 5));
        outBox.getChildren().add(pngBox);
        outBox.getChildren().add(imageOutTextBox);
        outBox.getChildren().add(charsLeft);
        outBox.getChildren().add(outProgress);

        imageIn.setClosable(false);
        imageOut.setClosable(false);

        imageIn.setContent(outBox);
        imageOut.setContent(inBox);

        tabPane.getTabs().add(imageIn);
        tabPane.getTabs().add(imageOut);

        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        stage.setTitle("Brenden Cho");
        stage.setResizable(false);
        stage.setWidth(800);
        stage.setHeight(375);
        stage.setScene(scene);
        stage.show();

    }

    public void copy() {
        imageout.copy();
    }

    public ProgressBar getOutProgress() {
        return outProgress;
    }

    public void setOutProgress(ProgressBar outProgress) {
        this.outProgress = outProgress;
    }

    public ProgressBar getInProgress() {
        return inProgress;
    }

    public void setInProgress(ProgressBar inProgress) {
        this.inProgress = inProgress;
    }

    public ImageOut getImageout() {
        return imageout;
    }

    public void setImageout(ImageOut imageout) {
        this.imageout = imageout;
    }

    public ImageIn getImagein() {
        return imagein;
    }

    public void setImagein(ImageIn imagein) {
        this.imagein = imagein;
    }

    public TextArea getImageOutText() {
        return imageOutText;
    }

    public void setImageOutText(TextArea imageOutText) {
        this.imageOutText = imageOutText;
    }

    public Text getCharsLeft() {
        return charsLeft;
    }

    public void setCharsLeft(Text charsLeft) {
        this.charsLeft = charsLeft;
    }

    public RadioButton getEncryptRadio() {
        return encryptRadio;
    }

    public void setEncryptRadio(RadioButton encryptRadio) {
        this.encryptRadio = encryptRadio;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Tab getImageIn() {
        return imageIn;
    }

    public void setImageIn(Tab imageIn) {
        this.imageIn = imageIn;
    }

    public Tab getImageOut() {
        return imageOut;
    }

    public void setImageOut(Tab imageOut) {
        this.imageOut = imageOut;
    }

    public VBox getOutBox() {
        return outBox;
    }

    public void setOutBox(VBox outBox) {
        this.outBox = outBox;
    }

    public HBox getPngBox() {
        return pngBox;
    }

    public void setPngBox(HBox pngBox) {
        this.pngBox = pngBox;
    }

    public Text getPngLabel() {
        return pngLabel;
    }

    public void setPngLabel(Text pngLabel) {
        this.pngLabel = pngLabel;
    }

    public Button getPngSelectBttn() {
        return pngSelectBttn;
    }

    public void setPngSelectBttn(Button pngSelectBttn) {
        this.pngSelectBttn = pngSelectBttn;
    }

    public Text getPngName() {
        return pngName;
    }

    public void setPngName(Text pngName) {
        this.pngName = pngName;
    }

    public VBox getInBox() {
        return inBox;
    }

    public void setInBox(VBox inBox) {
        this.inBox = inBox;
    }

    public HBox getPngInBox() {
        return pngInBox;
    }

    public void setPngInBox(HBox pngInBox) {
        this.pngInBox = pngInBox;
    }

    public Text getPngInText() {
        return pngInText;
    }

    public void setPngInText(Text pngInText) {
        this.pngInText = pngInText;
    }

    public Button getPngInSelectBttn() {
        return pngInSelectBttn;
    }

    public void setPngInSelectBttn(Button pngInSelectBttn) {
        this.pngInSelectBttn = pngInSelectBttn;
    }

    public Text getPngTextName() {
        return pngTextName;
    }

    public void setPngTextName(Text pngTextName) {
        this.pngTextName = pngTextName;
    }

    public VBox getImageOutTextBoxControls() {
        return imageOutTextBoxControls;
    }

    public void setImageOutTextBoxControls(VBox imageOutTextBoxControls) {
        this.imageOutTextBoxControls = imageOutTextBoxControls;
    }

    public HBox getImageOutTextBox() {
        return imageOutTextBox;
    }

    public void setImageOutTextBox(HBox imageOutTextBox) {
        this.imageOutTextBox = imageOutTextBox;
    }

    public ScrollPane getImageOutTextPane() {
        return imageOutTextPane;
    }

    public void setImageOutTextPane(ScrollPane imageOutTextPane) {
        this.imageOutTextPane = imageOutTextPane;
    }

    public Text getEnterText() {
        return enterText;
    }

    public void setEnterText(Text enterText) {
        this.enterText = enterText;
    }

    public Button getImageOutTextGoBttn() {
        return imageOutTextGoBttn;
    }

    public void setImageOutTextGoBttn(Button imageOutTextGoBttn) {
        this.imageOutTextGoBttn = imageOutTextGoBttn;
    }

    public HBox getEncryptBox() {
        return encryptBox;
    }

    public void setEncryptBox(HBox encryptBox) {
        this.encryptBox = encryptBox;
    }

}
