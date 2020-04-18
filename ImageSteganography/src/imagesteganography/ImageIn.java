/*

 */
package imagesteganography;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javax.imageio.ImageIO;

/**
 *
 * @author Brenden Cho
 */
public class ImageIn extends Thread {

    MainWindow mw;
    File image;
    BufferedImage bi;
    String userReturnString = "";
    int numChars;
    boolean valid = true;
    int x = 0;
    int y = 0;
    int counter = 1;
    String password;
    boolean encrypt = false;

    public ImageIn(File image, MainWindow mw) {
        this.image = image;
        this.mw = mw;
    }

    public void run() {
        copy();
    }

    public void copy() {

        try {
            bi = ImageIO.read(image);
            numChars = Integer.parseInt(getNumUserChars());

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    mw.getInProgress().setProgress(ProgressBar.INDETERMINATE_PROGRESS);

                }
            });
            decode();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    mw.getInProgress().setProgress(100);

                }
            });

            if (encrypt == true) {

                Encrypt e = new Encrypt();
                userReturnString = e.decrypt(password, userReturnString);

            }

            final String temp = userReturnString;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    TextResult tr = new TextResult(temp);
                }
            });

            reset();

        } catch (IOException e) {

        }

    }

    public void decode() {
        char c;
        String binary = "";

        int pixel = bi.getRGB(x, y);

        for (int i = 0; i < (numChars * 4); i++) {

            if (counter == 1) {
                binary = binary + readPixel(pixel, counter);
                counter++;
            } else if (counter == 2) {
                binary = binary + readPixel(pixel, counter);
                counter++;
            } else {
                binary = binary + readPixel(pixel, counter);
                counter++;
            }

            if (counter > 3) {
                counter = 1;
                y++;

                if (y == bi.getHeight()) {
                    x++;
                    y = 0;

                }

                pixel = bi.getRGB(x, y);

            }

            if (binary.length() == 8) {
                int value = Integer.parseInt(binary, 2);
                c = (char) value;
                userReturnString = userReturnString + c;

                binary = "";
            }

        }

    }

    public String getNumUserChars() {
        String chars = "";
        String binary = "";

        int pixel = bi.getRGB(0, 0);

        while (chars.length() != 9) {

            if (counter == 1) {
                binary = binary + readPixel(pixel, counter);
                counter++;
            } else if (counter == 2) {
                binary = binary + readPixel(pixel, counter);
                counter++;
            } else {
                binary = binary + readPixel(pixel, counter);
                counter++;
            }

            if (counter > 3) {
                counter = 1;
                y++;
                pixel = bi.getRGB(0, y);

            }

            if (binary.length() == 8) {

                int value = Integer.parseInt(binary, 2);

                char c = (char) value;

                if (value >= 48 && value <= 57) {

                    chars = chars + c;
                } else {
                    throw new ImageInException("Image not Encoded");
                }

                binary = "";
            }

        }

        return chars;

    }

    public String readPixel(int pixel, int num) {
        int value;
        String temp;
        if (num == 1) {
            value = (pixel >> 16) & 0xff;

        } else if (num == 2) {
            value = (pixel >> 8) & 0xff;

        } else {
            value = pixel & 0xff;

        }
        temp = Integer.toBinaryString(value);

        while (temp.length() != 8) {
            temp = "0" + temp;
        }

        temp = temp.substring(6, 8);

        return temp;
    }

    public void reset() {
        encrypt = false;
        password = null;
        image = null;
        bi = null;
        Platform.runLater(new Runnable() {

            public void run() {
                mw.getInProgress().setProgress(0);
                mw.getPngTextName().setText("");

            }

        });

    }

    public void setImage(File image) {
        this.image = image;
    }

    public File getImage() {
        return image;
    }

    public BufferedImage getBi() {
        return bi;
    }

    public void setBi(BufferedImage bi) {
        this.bi = bi;
    }

    public String getUserReturnString() {
        return userReturnString;
    }

    public void setUserReturnString(String userReturnString) {
        this.userReturnString = userReturnString;
    }

    public int getNumChars() {
        return numChars;
    }

    public void setNumChars(int numChars) {
        this.numChars = numChars;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MainWindow getMw() {
        return mw;
    }

    public void setMw(MainWindow mw) {
        this.mw = mw;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

}
