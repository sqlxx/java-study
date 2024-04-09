package ind.sq.study.ai.aliyun;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;

import java.util.Arrays;

public class QuanWenTest {
    public static void main(String[] args) throws NoApiKeyException, InputRequiredException {
        String apiKey = System.getenv("DASHSCOPE_API_KEY");
        var generation = new Generation();

        var sysMsg = Message.builder().role(Role.SYSTEM.getValue()).content("请基于以下人物背景回答用户问题: 你是一个程序员小助手, 精通各种编程语言和算法.").build();
        var userMsg = Message.builder().role(Role.USER.getValue()).content("你是谁？").build();
        var param = GenerationParam.builder().apiKey(apiKey).model("qwen-plus").messages(Arrays.asList(sysMsg, userMsg))
            .resultFormat(GenerationParam.ResultFormat.MESSAGE).topP(0.8).temperature(1.0f).build();
        long start = System.currentTimeMillis();
        var result = generation.call(param);
        System.out.println(result.getOutput());
        System.out.println("Cost " + (System.currentTimeMillis() - start) + "ms");

    }
}
