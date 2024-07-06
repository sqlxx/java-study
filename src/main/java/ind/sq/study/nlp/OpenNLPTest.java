package ind.sq.study.nlp;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.IOException;

public class OpenNLPTest {
    public static void main(String[] args) {
        var model = "opennlp-en-ud-ewt-sentence-1.0-1.9.3.bin";
        try (var modelIs = Thread.currentThread().getContextClassLoader().getResourceAsStream(model)) {
            var sentenceModel = new SentenceModel(modelIs);
            var sentenceDetector = new SentenceDetectorME(sentenceModel);
//            var doc = "1.1技术方案设计 贵公司进行防磨喷涂的锅炉均为循环流化床锅炉，作为一种新型燃烧技术，由于炉型结构和设计参 数等特点，在环保性能、燃烧效率、燃料适应性、高效脱硫及灰渣综合利用等方法，具有很大 的优越性。然而， CFB锅炉受热面的磨损问题十分严重， 贵公司决定采用超音速电弧金属喷涂技 术来解决锅炉受热面磨损问题，以减少锅炉的非计划停运。我公司技术服务组根据全国各电厂的 防磨防护情况，针对锅炉运行的详细状况、工艺参数以及失效因素，并依据我公司多年来从事 该行业的宝贵工程经验，经我公司技术服务组专业人员的认真分析研究，并对上述部位的防护 1问题分析： 锅炉卫燃带，炉膛出口处，焊缝处的磨损、减薄与气流中固体物料浓度、烟气速度、颗粒的特 性硬度和流道几何形状等密切相关， 而在CFB锅炉中， 固体物料的浓度巨大， 通常可达煤粉炉的 几十倍到上百倍， 并且烟气流速大， 颗粒硬且棱角尖锐， 因而在高速烟气的带动下， 对CFB锅 炉水冷壁等受热面部位的冲刷磨损极为严重；尤其在护墙根部水冷壁部位，由于位处密相区边 缘区，不但受到严重的高速高浓度含床料、燃料气流的强烈冲刷、磨损，而且存在严重的涡流 效应、切割效应和离心作用。涡流效应在炉膛四角部位，由于该处形成边壁流，物料汇集此处 较多，由于固体颗粒的惯性作用，局部磨损作用尤为明显，而切割效应体现在护墙根部水冷壁 处，其原因是由于防护墙的顶部提供了一个平台，当焦渣以较高的速度下降到该平台时产生反 弹，其中往水冷壁管侧反弹部分，对水冷壁管就产生了严重的切割效应，离心作用是由于颗粒 运行时受到烟气离心作用而引起。 其次还易受到高温氧化和硫酸盐及硫、硫化物的热腐蚀。水冷壁管具备了高温氧化和高温腐 蚀条件，其烟气温度高，且是富氧燃烧，实践证明，在300℃以上，管外表温度每升高50℃，腐 蚀速度增加1倍。锅炉在运行过程中受热面管表面首先发生高温氧化，表面生成Fe203，其次燃 料灰中的Naz 0和K 20与烟气中的SO 3化合生成硫酸盐， 其捕捉飞灰形成结渣和流渣， 此时烟气中 SO 3与Mz SO 4同管壁上的Fe 203反应生成复合硫酸盐MFe(SO 4) 2或Ma Fe(S 04) 3， 此复合硫酸盐受 高温又分解为疏松状氧化铁和硫酸盐沉积层，易被飞灰气流冲蚀带走，氧化腐蚀继续向管壁纵 深进行； 另外燃料中硫份， 经燃烧生成的S和HeS也对管壁会产生强烈的腐蚀， 与Fe反应生成 FeS。";
            var doc = "";
            var sentences = sentenceDetector.sentDetect(doc);

            for (var sentence : sentences) {
                System.out.println(sentence);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
