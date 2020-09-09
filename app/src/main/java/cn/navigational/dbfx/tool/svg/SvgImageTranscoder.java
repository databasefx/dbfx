package cn.navigational.dbfx.tool.svg;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import java.awt.image.BufferedImage;

/**
 * A tool for svg transform to Javafx image
 *
 * @author yangkui
 * @since 1.0
 */
public class SvgImageTranscoder extends ImageTranscoder {
    private BufferedImage img = null;

    @Override
    public BufferedImage createImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void writeImage(BufferedImage img, TranscoderOutput output) {
        this.img = img;
    }

    public BufferedImage getImg() {
        return img;
    }

    /**
     * Svg picture transform to Javafx Image object
     *
     * @param url Target path
     * @return {@link Image} Javafx Image object
     */
    public static Image svgToImage(String url) {
        var transcoder = new SvgImageTranscoder();
        var in = ClassLoader.getSystemResourceAsStream(url);
        var transIn = new TranscoderInput(in);
        try {
            transcoder.transcode(transIn, null);
        } catch (TranscoderException e) {
            throw new RuntimeException(e);
        }
        return SwingFXUtils.toFXImage(transcoder.getImg(), null);
    }

    /**
     * Svg picture transform to Javafx ImageView
     *
     * @param url Target path
     * @return {@link ImageView}
     */
    public static ImageView svgToImageView(String url) {
        var image = svgToImage(url);
        return new ImageView(image);
    }
}
