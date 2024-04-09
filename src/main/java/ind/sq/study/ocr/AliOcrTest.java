package ind.sq.study.ocr;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.ocr_api20210707.AsyncClient;
import com.aliyun.sdk.service.ocr_api20210707.models.RecognizeAdvancedRequest;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;
import ind.sq.study.pdf.Pdf2ImgConverter;

import java.io.*;
import java.util.HashMap;

public class AliOcrTest {
    public static void main(String[] args) throws IOException {

        var accessKey = System.getenv("ALI_CLOUD_ACCESS_KEY_ID");
        var accessKeySecret = System.getenv("ALI_CLOUD_ACCESS_KEY_SECRET");
        var endpoint = "ocr-api.cn-hangzhou.aliyuncs.com";
//        var pdfFile = new File("/Users/sqlxx/Downloads/ocr/永辉.pdf");
        var pdfFile = new File("/Users/sqlxx/Downloads/ocr/田英飞翔稻壳合同-6.20（单章）.pdf");

        var provider = StaticCredentialProvider.create(Credential.builder()
            .accessKeyId(accessKey).accessKeySecret(accessKeySecret)
            .build());

        var pdf2ImgConverter = new Pdf2ImgConverter();
        var imgList = pdf2ImgConverter.convertToImg(new FileInputStream(pdfFile), pdfFile.getPath().replace(".pdf", ""), "png");

        try (var client = AsyncClient.builder().credentialsProvider(provider).overrideConfiguration(
                ClientOverrideConfiguration.create().setEndpointOverride(endpoint)).build()) {
            StringBuffer content =new StringBuffer();

            for (var img: imgList) {
                var request =
                    RecognizeAdvancedRequest.builder().needRotate(true).outputCharInfo(false).outputFigure(true)
                        .outputTable(true).body(new FileInputStream(img)).build();


                var response = client.recognizeAdvanced(request);
                var resp = response.get();
                var gson = new Gson();
                var valueMap = gson.fromJson(resp.getBody().getData(), HashMap.class);
                System.out.println(gson.toJson(valueMap));
                content.append(valueMap.get("content"));
            }

            System.out.println(content);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
