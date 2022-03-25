package ind.sq.study.pdf;

import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sun Qin
 * @since 2020/11/30
 */
public class TestMain {
    public static void main(String[] args) throws IOException {

        try (var doc = PDDocument.load(new File("/Users/sqlxx/Downloads/pf.pdf"))) {
            Splitter splitter = new Splitter();
            var docs = splitter.split(doc);
            System.out.println("Splitted to " + docs.size());

            // TODO： 如果每页的高度可能不一致，以下语句需要放到循环中
            var stripper = TextStripperFactory.getStripper("XXX", doc.getPage(0).getMediaBox().getHeight());

            for (int i = 0; i < docs.size(); i ++) {
                var currentPageDoc = docs.get(i);
                var receipts = stripper.getReceipts(currentPageDoc);
                System.out.println(receipts.size() + ": " + receipts);

                var lineSeparatorYs = stripper.getLineSeparatorYs();
                System.out.println("Separators: " + lineSeparatorYs.size());
                if (lineSeparatorYs.size() != receipts.size() + 1) {
                    System.out.println("There is something wrong happens !!");
                }

                if (lineSeparatorYs.size() == 2) { // No need to separator this page
                    currentPageDoc.save(new File("/Users/sqlxx/Downloads/pf" + i +".pdf"));
                } else {
                    var page = currentPageDoc.getPage(0);
                    for (int j = 0; j < lineSeparatorYs.size() - 1; j++) {
                        page.setCropBox(new PDRectangle(
                            new BoundingBox(0f, lineSeparatorYs.get(j + 1), page.getMediaBox().getWidth(),
                                lineSeparatorYs.get(j))));
                        currentPageDoc.save(new File("/Users/sqlxx/Downloads/pf" + i + "-" + j + ".pdf"));
                    }
                }
            }
        }
    }
}

class TextStripperFactory {
    static FilteredTextStripper getStripper(String bankCode, float height) throws IOException {
        //TODO: could cache the Stripper if necessary, please note the stripper is NOT thread safe
        switch(bankCode) {
            case "XXX":
                return new FilteredTextStripper(height, "打印渠道", -20, new XXXBankPdfReceiptParser());
            default:
                throw new InvalidParameterException("\"" + bankCode + "\" is not a valid bank code");
        }
    }
}

interface BankPdfReceiptParser {
    List<BankReceipt> parse(String text);
}

class BankReceipt {
    private String account;

    BankReceipt(String str) {
        this.account = str; // For testing purpose

    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "BankReceipt{" + "account='" + account + '\'' + '}';
    }
}

class XXXBankPdfReceiptParser implements BankPdfReceiptParser {

    @Override
    public List<BankReceipt> parse(String text) {
        var receiptStrs = text.split("\\n \\n");

        return Arrays.stream(receiptStrs).map(BankReceipt::new).collect(Collectors.toList());
    }
}


/**
 * The stripper is not thread safe
 */
class FilteredTextStripper extends PDFTextStripper {

    private final String separatorStr;
    private final float margin;

    private List<Float> lineSeparatorYs = new ArrayList<>();
    private StringBuilder buffer = new StringBuilder();
    private float currentY;
    private BankPdfReceiptParser parser;

    /**
     * Instantiate a new PDFTextStripper object.
     *
     * @throws IOException If there is an error loading the properties.
     */
    public FilteredTextStripper(float pageHeight, String separatorStr, float margin, BankPdfReceiptParser receiptParser) throws IOException {
        super();
        this.lineSeparatorYs.add(pageHeight);
        this.separatorStr = separatorStr;
        this.margin = margin;
        this.parser = receiptParser;
    }


    @Override
    protected void processTextPosition(TextPosition text) {

        var unicode = text.getUnicode();
        if (unicode.length() > 1) {
            var actualUnicode = unicode.substring(0, 1);
            processSeparation(actualUnicode, text.getEndY());
            super.processTextPosition(new TextPosition(text.getRotation(), text.getPageWidth(),
                text.getPageHeight(), text.getTextMatrix(), text.getEndX(), text.getEndY(), text.getHeight(),
                text.getIndividualWidths()[0], text.getWidthOfSpace(), actualUnicode,
                text.getCharacterCodes(), text.getFont(), text.getFontSize(), (int) text.getFontSizeInPt()));

        } else {
            processSeparation(unicode, text.getEndY());
            super.processTextPosition(text);
        }
    }

    private void processSeparation(String unicode, float endY) {

        if (buffer.length() == 0) {
            if (separatorStr.startsWith(unicode)) {
                currentY = endY + margin;
                buffer.append(unicode);
            }
        } else {
            buffer.append(unicode);
            var tempStr = buffer.toString();
            if (!separatorStr.startsWith(tempStr)) {
                buffer.delete(0, buffer.length());
            } else if (separatorStr.equals(tempStr)) {
                buffer.delete(0, buffer.length());
                lineSeparatorYs.add(currentY);
            }
        }
    }

    public List<Float> getLineSeparatorYs() {
        return lineSeparatorYs;
    }

    private void reset() {
        this.buffer.delete(0, buffer.length());
        var height = this.lineSeparatorYs.get(0);
        this.lineSeparatorYs.clear();
        this.lineSeparatorYs.add(height);
    }

    public List<BankReceipt> getReceipts(PDDocument doc) {
        reset();

        try {
            return this.parser.parse(this.getText(doc));

        } catch (IOException ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }


}
