package ind.sq.study.ocr;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.ocr_api20210707.AsyncClient;
import com.aliyun.sdk.service.ocr_api20210707.models.RecognizeAdvancedRequest;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;

import java.util.HashMap;

public class AliOcrTest {
    public static void main(String[] args) {

        var accessKey = System.getenv("ALI_CLOUD_ACCESS_KEY_ID");
        var accessKeySecret = System.getenv("ALI_CLOUD_ACCESS_KEY_SECRET");
        var endpoint = "ocr-api.cn-hangzhou.aliyuncs.com";

        var provider = StaticCredentialProvider.create(Credential.builder()
            .accessKeyId(accessKey).accessKeySecret(accessKeySecret)
            .build());

        try (var client = AsyncClient.builder().credentialsProvider(provider).overrideConfiguration(
                ClientOverrideConfiguration.create().setEndpointOverride(endpoint)).build()) {
            var request = RecognizeAdvancedRequest.builder().needRotate(true).outputCharInfo(false).outputFigure(true).
                outputTable(true).url("https://www.maycur.com/templates/default/ly/ly_img/nin1.png").build();


            var response = client.recognizeAdvanced(request);
            var resp = response.get();
            var gson = new Gson();
            System.out.println(gson.toJson(gson.fromJson(resp.getBody().getData(), HashMap.class)));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
