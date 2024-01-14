package ind.sq.study.wedo;

import org.apache.commons.codec.digest.DigestUtils;

public class sso {
    public static void main(String[] args) {
        String ssoSecret = "fkhzx4sojnxc5qxyksqy3da0o76pwvd4xsqpddy4fr8n8gkpnl";
        long timestamp = System.currentTimeMillis();
        String userId = "sq@wedoos.tech"; //工号或者邮箱或者手机
        String ssoId = "lv9tfoq0quhy3xofoiye";
        String ssoToken = DigestUtils.sha256Hex((ssoSecret + ":" + userId + ":" + timestamp).getBytes());
        // String baseUrl = "http://localhost:5173";
        String baseUrl = "https://app-dev.wedobot.com";
        String url = baseUrl + "/auth/sso?userId=" + userId + "&ssoId=" + ssoId + "&timestamp=" + timestamp + "&ssoToken=" + ssoToken;
        System.out.println(url);

    }
}
