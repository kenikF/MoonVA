package org.kenikf;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Recognize {
    public void recognize() throws IOException {
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 60000, 16, 2, 4, 44100, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine microphone;
        SourceDataLine speakers;

        JsonParser parser = new JsonParser();
        Answer answer = new Answer();

        try (Model model = new Model("D:\\Programming\\Java\\MoonVA\\src\\main\\java\\org\\kenikf\\model");
            Recognizer recognizer = new Recognizer(model, 120000)) {
                System.out.println("Говорите..");

                try {
                    microphone = (TargetDataLine) AudioSystem.getLine(info);
                    microphone.open(format);
                    microphone.start();

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    int numBytesRead;
                    int CHUNK_SIZE = 1024;
                    int bytesRead = 0;

                    DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
                    byte[] b = new byte[4096];

                    while(bytesRead <= 100000000) {
                        numBytesRead = microphone.read(b, 0, CHUNK_SIZE);
                        bytesRead += numBytesRead;

                        out.write(b, 0, numBytesRead);

                        if (recognizer.acceptWaveForm(b, numBytesRead)) {
                            JsonObject jsonObject = parser.parse(recognizer.getResult()).getAsJsonObject();
                            String text = jsonObject.get("text").getAsString();
                            if(text.equals("стоп")) {
                                break;
                            }
                            if(text != "") {
                                answer.answer(text);
                            }
                        }
                    }
                    microphone.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
}
