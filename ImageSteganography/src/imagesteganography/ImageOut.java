package imagesteganography;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressBar;
import javax.imageio.ImageIO;

/**
 *
 * @author Brenden Cho
 */
public class ImageOut extends Thread {

    MainWindow mw;
    File image;
    ImageIO in;
    BufferedImage bi;
    byte arr[];
    int width;
    int height;
    String userInput = "";
    int maxUserChars;
    boolean encrypt = false;
    String password;

    public void run() {
        try {
            if (encrypt != true) {
                userInput = userInput.replaceAll("[^\\x00-\\x7F]", "");
                copy();
            } else {
                Encrypt e = new Encrypt();
                userInput = userInput.replaceAll("[^\\x00-\\x7F]", "");
                userInput = e.encrypt(password, userInput);
                copy();
            }

        } catch (InvalidCharException e) {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Brenden Cho");
            a.setContentText("Message Could not be encoded. Only Ascii characters are allowed");
            a.show();
        }
    }

    public ImageOut(File image, MainWindow mw) {
        this.image = image;
        this.mw = mw;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public void setUpEncrypt() {
        try {
            bi = ImageIO.read(image);
            System.out.println("Image read");
            width = bi.getWidth();
            height = bi.getHeight();
            maxUserChars = (((((width * height) * 3) / 4) - 9) / 24) * 15;
        } catch (IOException e) {
        }
    }

    public void setUp() {
        try {
            bi = ImageIO.read(image);
            System.out.println("Image read");
            width = bi.getWidth();
            height = bi.getHeight();
            maxUserChars = (((width * height) * 3) / 4) - 9;
        } catch (IOException e) {
        }
    }

    public void copy() {

        userInput = String.format("%09d", userInput.length()) + userInput;
        encode();

    }

    public void encode() {
        int x = 0;
        int y = 0;
        int counter = 1;
        int add = updateAdd();
        int pixel = getPixel(0, 0);
        int a = pixel >> 24 & 0xff;
        int r = pixel >> 16 & 0xff;
        int g = pixel >> 8 & 0xff;
        int b = pixel & 0xff;
        String bin = Integer.toBinaryString(add);

        while (bin.length() != 8) {
            bin = "0" + bin;
        }

        while (userInput.length() != 0 || bin.length() != 0) {

            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    mw.getOutProgress().setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                }

            });

            if (counter == 1) {
                r = combine(bin.substring(0, 2), Integer.toBinaryString(r));

                updatePixel(pixel, x, y, a, r, g, b);

                bin = new String(updateString(bin));

                counter++;
            } else if (counter == 2) {
                g = combine(bin.substring(0, 2), Integer.toBinaryString(g));
                updatePixel(pixel, x, y, a, r, g, b);

                bin = new String(updateString(bin));

                counter++;

            } else {
                b = combine(bin.substring(0, 2), Integer.toBinaryString(b));
                updatePixel(pixel, x, y, a, r, g, b);

                bin = new String(updateString(bin));

                counter++;
            }

            if (bin.equals("") && userInput.length() != 0) {
                add = updateAdd();

                bin = Integer.toBinaryString(add);

                while (bin.length() != 8) {
                    bin = "0" + bin;
                    if (bin.length() > 8) {
                        throw new InvalidCharException("Invalid char");
                    }

                }

            }

            if (counter > 3) {

                y++;
                if (y == height) {
                    y = 0;
                    x++;
                }

                pixel = getPixel(x, y);

                a = pixel >> 24 & 0xff;
                r = pixel >> 16 & 0xff;
                g = pixel >> 8 & 0xff;
                b = pixel & 0xff;

                counter = 1;

            }

        }

        String fileName = image.getName();
        boolean done = false;
        int t = 0;

        while (done == false) {
            if (fileName.charAt(t) == '.') {
                fileName = fileName.substring(0, t);
                done = true;
            } else {
                t++;
            }
        }

        System.out.println("Image encoded");

        File outputfile = new File(image.getParent() + "\\" + fileName + "Output.png");

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                mw.getOutProgress().setProgress(100);
                Alert aaa = new Alert(AlertType.INFORMATION);
                aaa.setTitle("Brenden Cho");
                aaa.setContentText("Image Encoded. Image can be found in the same location of the file you selected.");
                aaa.show();

            }

        });

        try {
            ImageIO.write(bi, "png", outputfile);
        } catch (IOException e) {

        }
        reset();
    }

    public int getPixel(int x, int y) {
        return bi.getRGB(x, y);
    }

    public void updatePixel(int pixel, int x, int y, int a, int r, int g, int b) {
        pixel = (a << 24) | (r << 16) | (g << 8) | b;
        bi.setRGB(x, y, pixel);
    }

    public int combine(String add, String b) {

        while (b.length() != 8) {

            b = "0" + b;
        }

        b = b.substring(0, 6);
        b = b + add;

        int temp = Integer.parseInt(b, 2);

        int rv = temp;

        return rv;
    }

    public int updateAdd() {
        char temp = userInput.charAt(0);
        userInput = userInput.substring(1, userInput.length());
        return (int) temp;
    }

    public String updateString(String s) {
        if (s.length() == 2) {
            s = "";
        } else {
            s = s.substring(2, s.length());
        }
        return s;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public int getMaxUserChars() {
        return maxUserChars;
    }

    public void setMaxUserChars(int maxUserChars) {
        this.maxUserChars = maxUserChars;
    }

    public void reset() {

        encrypt = false;
        password = null;
        image = null;
        bi = null;

        Runnable r = new Runnable() {

            public void run() {
                mw.getImageOutText().setText("");
                mw.getPngName().setText("");
                mw.getCharsLeft().setText("");
                mw.getEncryptRadio().setSelected(false);
                mw.getOutProgress().setProgress(0);

            }

        };

        Platform.runLater(r);

    }

    public MainWindow getMw() {
        return mw;
    }

    public void setMw(MainWindow mw) {
        this.mw = mw;
    }

    public ImageIO getIn() {
        return in;
    }

    public void setIn(ImageIO in) {
        this.in = in;
    }

    public BufferedImage getBi() {
        return bi;
    }

    public void setBi(BufferedImage bi) {
        this.bi = bi;
    }

    public byte[] getArr() {
        return arr;
    }

    public void setArr(byte[] arr) {
        this.arr = arr;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
