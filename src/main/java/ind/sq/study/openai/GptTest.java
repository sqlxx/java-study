package ind.sq.study.openai;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

public class GptTest {

    public static void main(String[] args) {

        String azureOpenaiKey = System.getenv("OPENAI_API_KEY");
        String endpoint = System.getenv("OPENAI_API_BASE");

        String gptModelId = System.getenv("MODEL_NAME");
        String embeddingModelId = "embedding";
        System.out.println(endpoint);
        System.out.println(azureOpenaiKey);

        OpenAIClient client = new OpenAIClientBuilder().endpoint(endpoint).credential(new AzureKeyCredential(azureOpenaiKey)).buildClient();

        var chatMessages = new ArrayList<ChatRequestMessage>();
        var sysMsg = "你是一个可爱的小助手，今天是2024年3月20日";
        System.out.println("SYSTEM MESSAGE: " + sysMsg);
        chatMessages.add(new ChatRequestSystemMessage(sysMsg));
        var userMsg = "后天杭州的天气怎么样？";
        chatMessages.add(new ChatRequestUserMessage(userMsg));
        System.out.println("USER MESSAGE: " + userMsg);

        var watch = new StopWatch();
        var option = new ChatCompletionsOptions(chatMessages);
        var functionDef = new ChatCompletionsFunctionToolDefinitionFunction("getWeather").setDescription("获取指定城市的实况天气或者天气预报");
        functionDef.setParameters(getTemperatureParameters());
        var toolDefinition = new ChatCompletionsFunctionToolDefinition(functionDef);

        option.setTools(List.of(toolDefinition));

        var chatCompletions = getChatCompletions(watch, client, gptModelId, option);

//        System.out.printf("Model ID = %s is created at %s, cost %d. %n", chatCompletions.getId(), chatCompletions.getCreatedAt(), watch.getLastTaskTimeMillis()/1000);
        var choice = chatCompletions.getChoices().get(0);
        var assistantResponseMessage = choice.getMessage();


//        System.out.printf("Index: %d, ChatRole: %s.%n", choice.getIndex(), message.getRole());
        System.out.println("ASSISTANT MESSAGE: " + assistantResponseMessage.getContent());
        var chatRequestMsg = new ChatRequestAssistantMessage(assistantResponseMessage.getContent());
        String toolCallId = null;
        if (assistantResponseMessage.getToolCalls() != null) {
            chatRequestMsg.setToolCalls(assistantResponseMessage.getToolCalls());
            for(var toolCall : assistantResponseMessage.getToolCalls()) {
                toolCallId = toolCall.getId();
                var function = ((ChatCompletionsFunctionToolCall)toolCall).getFunction();
                System.out.println("FUNCTION CALL: " + function.getName());
                System.out.println("ARGUMENTS: " + function.getArguments());
            }
            System.out.println("--------");
        }

        chatMessages.add(chatRequestMsg);

        System.out.println("Calling function ....");

//        var functionResult = "{\"status\":\"1\",\"count\":\"1\",\"info\":\"OK\",\"infocode\":\"10000\",\"lives\":[{\"province\":\"浙江\",\"city\":\"杭州市\",\"adcode\":\"330100\",\"weather\":\"晴\",\"temperature\":\"14\",\"winddirection\":\"南\",\"windpower\":\"≤3\",\"humidity\":\"50\",\"reporttime\":\"2024-03-20 09:32:32\",\"temperature_float\":\"14.0\",\"humidity_float\":\"50.0\"}]}";
        var functionResult = "{\"status\":\"1\",\"count\":\"1\",\"info\":\"OK\",\"infocode\":\"10000\",\"forecasts\":[{\"city\":\"杭州市\",\"adcode\":\"330100\",\"province\":\"浙江\",\"reporttime\":\"2024-03-20 15:02:17\",\"casts\":[{\"date\":\"2024-03-20\",\"week\":\"3\",\"dayweather\":\"晴\",\"nightweather\":\"晴\",\"daytemp\":\"19\",\"nighttemp\":\"8\",\"daywind\":\"北\",\"nightwind\":\"北\",\"daypower\":\"1-3\",\"nightpower\":\"1-3\",\"daytemp_float\":\"19.0\",\"nighttemp_float\":\"8.0\"},{\"date\":\"2024-03-21\",\"week\":\"4\",\"dayweather\":\"多云\",\"nightweather\":\"晴\",\"daytemp\":\"20\",\"nighttemp\":\"11\",\"daywind\":\"北\",\"nightwind\":\"北\",\"daypower\":\"1-3\",\"nightpower\":\"1-3\",\"daytemp_float\":\"20.0\",\"nighttemp_float\":\"11.0\"},{\"date\":\"2024-03-22\",\"week\":\"5\",\"dayweather\":\"多云\",\"nightweather\":\"多云\",\"daytemp\":\"25\",\"nighttemp\":\"15\",\"daywind\":\"北\",\"nightwind\":\"北\",\"daypower\":\"1-3\",\"nightpower\":\"1-3\",\"daytemp_float\":\"25.0\",\"nighttemp_float\":\"15.0\"},{\"date\":\"2024-03-23\",\"week\":\"6\",\"dayweather\":\"小雨\",\"nightweather\":\"小雨\",\"daytemp\":\"27\",\"nighttemp\":\"17\",\"daywind\":\"南\",\"nightwind\":\"南\",\"daypower\":\"4\",\"nightpower\":\"4\",\"daytemp_float\":\"27.0\",\"nighttemp_float\":\"17.0\"}]}]}";
        var functionMsg = functionResult; //+ "\n ------\n字段含义如下: [city:城市名, province:省份名, date:天气预报的日期, temperature:温度, weather: 天气, humidity:湿度, windpower: 风力, winddirection: 风向, dayweather: 白天天气, nightweather: 晚上天气, daytemp: 最高温度, nighttemp: 最低温度 ] ";
        System.out.println("FUNCTION RESULT: " + functionMsg);
        var chatRequestFunctionResultMsg = new ChatRequestToolMessage(functionMsg, toolCallId);
        chatMessages.add(chatRequestFunctionResultMsg);


        var option2 = new ChatCompletionsOptions(chatMessages);

        chatCompletions = getChatCompletions(watch, client, gptModelId, option2);
        System.out.println("ASSISTANT MESSAGE: " + chatCompletions.getChoices().get(0).getMessage().getContent());


//        var textList = new ArrayList<String>();
//        textList.add("我想请3天假");
//        textList.add("我们老板叫什么名字?");
//
//        var embeddingOptions = new EmbeddingsOptions(textList);
//        var embeddings = client.getEmbeddings(embeddingModelId, embeddingOptions);
//        for (var embedding: embeddings.getData()) {
//            System.out.println(embedding.getEmbedding());
//        }


    }

    private static ChatCompletions getChatCompletions(StopWatch watch, OpenAIClient client, String gptModelId,
        ChatCompletionsOptions option) {
        watch.start();
        System.out.println("Start calling LLM");
        var chatCompletions = client.getChatCompletions(gptModelId, option);
        CompletionsUsage usage = chatCompletions.getUsage();
//        System.out.printf("Usage: number of prompt token is %d, number of completion token is %d, and number of total token is %d",
//            usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());

        watch.stop();
        System.out.println("Finished calling LLM, cost: " + watch.getLastTaskTimeMillis() + "ms");
        return chatCompletions ;
    }


    private static BinaryData getTemperatureParameters() {
        return BinaryData.fromObject(new TemperatureFunctionParam());
    }

    private static class TemperatureFunctionParam {
        @JsonProperty(value = "type")
        private final String type = "object";

        @JsonProperty(value = "properties")
        private final TemperatureFunctionProperties props = new TemperatureFunctionProperties();
    }

    private static class TemperatureFunctionProperties {
            @JsonProperty(value = "city")
            StringField city = new StringField("城市代码，支持以下城市：[杭州:330100;上海:310000;北京:110000;天津:120000]");
            @JsonProperty(value = "extensions")
            StringField extensions= new StringField("可取值“base”或者“all”，base:表示获取当时的天气情况，all:表示获取当天开始4天的天气预报");
    }

private static class StringField {
    @JsonProperty(value = "type")
    private final String type = "string";

    @JsonProperty(value = "description")
    private String description;

    @JsonCreator
    StringField(@JsonProperty(value = "description") String description) {
        this.description = description;
    }
}
}
