package app.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class Utils {
    public static double CAPTCHA_LIFETIME = 3;

    static byte[] convertImageToByteArray(BufferedImage image)  {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] byteImage = null;

        try{
            ImageIO.write(image, "png", outputStream);
            outputStream.flush();
            byteImage = outputStream.toByteArray();
            outputStream.close();
        }catch (IOException ex){
            System.out.println("Image IO throw IOException!");
        }

        return byteImage;
    }

}
