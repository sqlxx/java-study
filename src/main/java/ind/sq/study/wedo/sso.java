package ind.sq.study.wedo;

import org.apache.commons.codec.digest.DigestUtils;

public class sso {
    public static void main(String[] args) {
        String ssoSecret = "h3bb1whhtuvceyqkblgfimt7tt32lhmp";
        long timestamp = System.currentTimeMillis();
        String userId = "10002"; //工号或者邮箱或者手机
        String ssoId = "vj60hzxupenw4zwpmq9a";
        String ssoToken = DigestUtils.sha256Hex((ssoSecret + ":" + userId + ":" + timestamp).getBytes());
        String baseUrl = "http://localhost:5173";
        String url = baseUrl + "/auth/sso?userId=" + userId + "&ssoId=" + ssoId + "&timestamp=" + timestamp + "&ssoToken=" + ssoToken;
        System.out.println(url);

    }
}
