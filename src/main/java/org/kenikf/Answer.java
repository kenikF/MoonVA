package org.kenikf;

import io.github.artemnefedov.javaai.dto.language.ChatMessage;
import io.github.artemnefedov.javaai.service.OpenAI;
import io.github.artemnefedov.javaai.service.impl.OpenAIImplementation;
import io.github.cdimascio.dotenv.Dotenv;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Answer {
    Dotenv dotenv = Dotenv.load();
    OpenAI openAI = new OpenAIImplementation(dotenv.get("API_KEY_GPT"));

    List<ChatMessage> messages = new ArrayList<>();

    private static final String PREFIX = "Moon: ";

    public void answer(String text) {
        System.out.println("Вы сказали: " + text);
        if(text.equals("привет")) {
            System.out.println(PREFIX + "Привет, как у тебя дела?");
        } else if(text.equals("спрос вопросов")) {
            System.out.println(PREFIX + "Вопросы успешно были сброшены!");
            messages.clear();
        } else if(text.equals("ты кто")) {
            System.out.println(PREFIX + "Я - голосовой помощник MoonVA");
        } else if(text.equals("время")) {
            LocalTime now = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
            System.out.println(PREFIX + "Текущее время: " + now.format(formatter));
        } else if(text.equals("открыть браузер")) {
            try {
                Desktop.getDesktop().browse(new URI("https://www.google.com"));
                System.out.println(PREFIX + "Браузер был успешно открыт!");
            } catch(Exception e) {
                System.out.println(PREFIX + "К сожалению, произошла ошибка! ");
                e.printStackTrace();
            }
        } else if(text.contains("найди видео") || text.contains("найти видео")) {
            try {
                String query = text.substring(12);
                Desktop.getDesktop().browse(new URI("https://www.youtube.com/results?search_query=" + URLEncoder.encode(query, StandardCharsets.UTF_8)));
                System.out.println(PREFIX + "Видео по запросу \"" + query + "\" было успешно найдено!");
            } catch(Exception e) {
                System.out.println(PREFIX + "К сожалению, произошла ошибка! ");
                e.printStackTrace();
            }
        } else {
            if(!text.trim().isEmpty()) {
                try {
                    messages.add(new ChatMessage("user", text));
                    String chatResponse = openAI.chat(messages);
                    System.out.println("ChatGPT: " + chatResponse);
                } catch(NullPointerException e) {
                    System.out.println("Извините, но от вас слишком много запросов!");
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
