package main.java.com.pantrypal.services;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenAiService {

    private static final String OPENAI_API_KEY = "​​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\n" + //
                "";
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";


    public String generateRecipe(String ingredients) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String prompt = "I have these ingredients: " + ingredients + ". What can I cook? Give a detailed recipe.";

            String requestBody = """
            {
              "model": "gpt-3.5-turbo",
              "messages": [{"role": "user", "content": "%s"}],
              "temperature": 0.7
            }
            """.formatted(prompt);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_URL))
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.body());
            return json.get("choices").get(0).get("message").get("content").asText();

        } catch (Exception e) {
            return "Sorry, I couldn't generate a recipe. Please try again.";
        }
    }
}
