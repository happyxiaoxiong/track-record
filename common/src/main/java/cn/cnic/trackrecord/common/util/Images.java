package cn.cnic.trackrecord.common.util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Images {
    public static void createThumbnail(File src, File dest, int width) throws IOException {
        createThumbnail(src, new FileOutputStream(dest), width);
    }

    public static void createThumbnail(File src, OutputStream out, int width) throws IOException {
        Image srcImg = ImageIO.read(src);
        int height = (int) (srcImg.getHeight(null) * 1.0 * width / srcImg.getWidth(null));
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        result.getGraphics().drawImage(srcImg.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam param = JPEGCodec.getDefaultJPEGEncodeParam(result);
        param.setQuality(0.75f, true);
        encoder.encode(result, param);
        out.close();
    }

    public static void createThumbnail(File src, File dest) throws IOException {
        createThumbnail(src, dest, 1024);
    }

    public static void createThumbnail(File src, OutputStream out) throws IOException {
        createThumbnail(src, out, 1024);
    }
}
