package ind.sq.study.wechaty;

import com.aliyun.core.utils.StringUtils;
import io.github.wechaty.MessageListener;
import io.github.wechaty.Wechaty;
import io.github.wechaty.WechatyOptions;
import io.github.wechaty.schemas.PuppetOptions;
import io.github.wechaty.utils.QrcodeUtils;

public class WeChatyTest {
    public static void main(String[] args) {
        var token = "";
        var options = new WechatyOptions();
        options.setName("test-bot");
        options.setPuppet("WeChat");
        options.setPuppetOptions(new PuppetOptions());
        Wechaty bot = Wechaty.instance(options);
        bot.onScan((qrcode, statusScanStatus, data) -> {
            System.out.println(QrcodeUtils.getQr(qrcode));
            System.out.println("Online Image: https://wechaty.github.io/qrcode/" + qrcode);

        });

        bot.onMessage(message -> {
            var from = message.from();
            var room = message.room();

            var text = message.text();

            if (StringUtils.equals(text, "#ding")) {
                if (room != null) {
                    room.say("dong");
                } else {
                    from.say("dong");
                }
            }
        });

        bot.start(true);
    }
}
