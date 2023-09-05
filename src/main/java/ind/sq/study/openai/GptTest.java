package ind.sq.study.openai;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.util.StopWatch;

import java.util.ArrayList;

public class GptTest {

    public static void main(String[] args) {

        String azureOpenaiKey = System.getenv("OPENAI_API_KEY");
        String endpoint = System.getenv("OPENAI_API_BASE");

        String gptModelId = System.getenv("MODEL_NAME");
        String embeddingModelId = "embedding";
        System.out.println(endpoint);
        System.out.println(azureOpenaiKey);

        OpenAIClient client = new OpenAIClientBuilder().endpoint(endpoint).credential(new AzureKeyCredential(azureOpenaiKey)).buildClient();

        var chatMessages = new ArrayList<ChatMessage>();
        chatMessages.add(new ChatMessage(ChatRole.SYSTEM, "You are a helpful assistant for business, and you can categorize questions to following category: IT, Finance, HR"));
        chatMessages.add(new ChatMessage(ChatRole.USER, "我想请3天假"));
        chatMessages.add(new ChatMessage(ChatRole.ASSISTANT, "It's a HR query"));
        chatMessages.add(new ChatMessage(ChatRole.USER, "我想修电脑"));
        chatMessages.add(new ChatMessage(ChatRole.ASSISTANT, "It's a IT query"));
        chatMessages.add(new ChatMessage(ChatRole.USER, "我家的猫叫什么名字？"));
        var watch = new StopWatch();
        watch.start();
        var chatCompletions = client.getChatCompletions(gptModelId, new ChatCompletionsOptions(chatMessages));
        watch.stop();

        System.out.printf("Model ID = %s is created at %s, cost %d. %n", chatCompletions.getId(), chatCompletions.getCreatedAt(), watch.getLastTaskTimeMillis()/1000);
        for (var choice : chatCompletions.getChoices()) {
            ChatMessage message = choice.getMessage();
            System.out.printf("Index: %d, ChatRole: %s.%n", choice.getIndex(), message.getRole());
            System.out.println("Message");
            System.out.println(message.getContent());
        }

        System.out.println();

        CompletionsUsage usage = chatCompletions.getUsage();
        System.out.printf("Usage: number of prompt token is %d, number of completion token is %d, and number of total token is %d",
                usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());


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
}
