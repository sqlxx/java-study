package ind.sq.study.pdf;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Pdf2ImgConverter {
    public List<File> convertToImg(InputStream pdfStream, String outputFileNamePrefix, String format) throws IOException {
        var imageList = new ArrayList<File>();

        try (var pdDocument = Loader.loadPDF(new RandomAccessReadBuffer(pdfStream))) {
            var pdfRender = new PDFRenderer(pdDocument);
            for (int page = 0; page < pdDocument.getNumberOfPages(); page++) {
//                var bufferedImg = pdfRender.renderImage(page, 1, ImageType.RGB);
                var bufferedImg = pdfRender.renderImageWithDPI(page, 150);
                var outputFile = new File(outputFileNamePrefix + "-" + page + "." + format);
                ImageIO.write(bufferedImg, format, outputFile);
                imageList.add(outputFile);
            }
            System.out.println(pdDocument.getNumberOfPages() + " pages processed");
        }

        return imageList;
    }

    public static void main(String[] args) throws IOException {
       var converter = new Pdf2ImgConverter();
       var classloader = Thread.currentThread().getContextClassLoader();
       try (var pdfStream = classloader.getResourceAsStream("田英飞翔稻壳合同-6.20（单章）.pdf")) {
           long start = System.currentTimeMillis();
           var imgs = converter.convertToImg(pdfStream, "/Users/sqlxx/Downloads/ocr/田英飞翔稻壳合同-6.20（单章）", "png");
           System.out.println("Cost " + (System.currentTimeMillis() - start) + "ms");
       }

    }

}
