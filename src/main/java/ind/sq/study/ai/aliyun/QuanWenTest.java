package ind.sq.study.ai.aliyun;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.tools.FunctionDefinition;
import com.alibaba.dashscope.tools.ToolFunction;
import com.alibaba.dashscope.utils.JsonUtils;

import java.util.Arrays;

public class QuanWenTest {
    public static void main(String[] args) throws NoApiKeyException, InputRequiredException {
        String apiKey = System.getenv("DASHSCOPE_API_KEY");
        var generation = new Generation();

        var systemPrompt = """
            今天是2024-04-10
            -------
            你是中梁内部知识问答助手，具有丰富的中梁内部系统知识，可以通过查询知识库来回答员工的问题。。
            你必须使用中梁知识库回答所有问题，如果在知识库中照不到答案，则回答“无法检索到相关信息，请提交工单”。
            """;

        var userPrompt = "流程提交失败怎么办？";

        var functionName = "query_knowledges";
        var functionDesc = """
            知识库查询工具，请使用本知识库的返回来回答用户问题
            包括:
            - 中梁知识库: 中梁知识库，可以用于查询中梁流程等相关问题
            """;

        var functionParam = "{\"type\":\"object\",\"properties\":{\"query\":{\"description\":\"用户提出的问题, 请尽量精确得描述该问题, 不要出现无关的信息\",\"type\":\"string\"}},\"required\":[\"query\"]}";



        var functionDef = FunctionDefinition.builder().name(functionName).description(functionDesc).parameters(
            JsonUtils.parseString(functionParam).getAsJsonObject()).build();
        var tool = ToolFunction.builder().function(functionDef).build();

        var sysMsg = Message.builder().role(Role.SYSTEM.getValue()).content(systemPrompt).build();
        var userMsg = Message.builder().role(Role.USER.getValue()).content(userPrompt).build();

        var param = GenerationParam.builder().apiKey(apiKey).model("qwen-max").messages(Arrays.asList(sysMsg, userMsg))
            .resultFormat(GenerationParam.ResultFormat.TEXT).topP(0.95).temperature(0.7f).tools(Arrays.asList(tool)).build();

        System.out.println("Request:" + param);
        long start = System.currentTimeMillis();
        var result = generation.call(param);
        System.out.println("================================");
        System.out.println(result.getOutput());
        System.out.println("Cost " + (System.currentTimeMillis() - start) + "ms");

    }
}
