package uz.java.spring_boot_application.util;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
@Component
public class ImageUtils {
    @Async
    public void compressImage(String original, String compress, Integer minWidth, Integer minHeight) throws IOException {
        File input = new File(original);
        BufferedImage image = ImageIO.read(input);

        File output = new File(compress);
        OutputStream out = new FileOutputStream(output);

        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(out);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.5f);
        }

        writer.write(null, new IIOImage(image, null, null), param);

        out.close();
        ios.close();
        writer.dispose();

        compressImage(compress, minWidth, minHeight);
    }

    private void compressImage(String path, Integer minWidth, Integer minHeight) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(path));
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        /*
         * Check min width and min height
         */
        if (minWidth != null && minHeight != null) {
            if (originalImage.getWidth() > minWidth && originalImage.getHeight() > minHeight) {
                if ((originalImage.getWidth() / minWidth) > (originalImage.getHeight() / minHeight)) {
                    width = (int) ((float) width / ((float) originalImage.getHeight() / (float) minHeight));
                    height = minHeight;
                } else {
                    width = minWidth;
                    height = (int) ((float) height / ((float) originalImage.getWidth() / (float) minWidth));
                }
            }
        }

        BufferedImage resizedImage = resizeImage(originalImage, type, width, height);
        ImageIO.write(resizedImage, "jpg", new File(path));
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

}
