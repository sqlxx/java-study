package ind.sq.study.poi;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.ParagraphStyle;
import com.deepoove.poi.data.style.Style;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by sqlxx on 2024/12/13.
 */
public class wordProcessor {
    static Style tableTextStyle = Style.builder().buildFontSize(12).build();
    static ParagraphStyle tableParagraphStyle = ParagraphStyle.builder().withSpacingRule(LineSpacingRule.EXACT).withSpacing(18).build();
    static double tableRowHeight = 0.1524;
    public static void main(String[] args) throws IOException {
        var map = new HashMap<String, Object>();
        map.put("全称", "杭州超级智能科技有限公司");
        map.put("简称", "超级智能");

        var header = Rows.of(cell("股东名称"), cell("认缴出资额"), cell("认缴比例"), cell("实缴金额"), cell("出资方式")).rowAtleastHeight(tableRowHeight).create();
        var row1 = Rows.of(cell("小王"), cell("100"), cell("20%"),cell("100"), cell("货币")).rowAtleastHeight(tableRowHeight).create();
        var row2 = Rows.of(cell("小张"), cell("400"), cell("80%"), cell("400"), cell("货币")).rowAtleastHeight(tableRowHeight).create();
        map.put("股份情况表", Tables.create(header, row1, row2));
        try (var stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("poi_template.docx")) {
            if (stream == null) {
                throw new FileNotFoundException("File not found");
            }

            XWPFTemplate.compile(stream).render(map).writeToFile("/tmp/out.docx");

        }
    }

    static CellRenderData cell(String text) {
        return Cells.of().addParagraph(Paragraphs.of(Texts.of(text).style(tableTextStyle).create()).paraStyle(tableParagraphStyle).create()).create();
    }
}
