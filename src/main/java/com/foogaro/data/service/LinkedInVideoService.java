package com.foogaro.data.service;

import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LinkedInVideoService {

    private int MAX_ATTEMPTS = 5;
    private int attempts = 0;

    public String extractVideoId(String url) {
        //Pattern pattern = Pattern.compile("(?<=licdn.com/\")(.*)(?=/)(.*)(?=-)");
        Pattern pattern = Pattern.compile("(?<=licdn.com/)(.*)(?=/)(.*)(?=-live)");
        Matcher matcher = pattern.matcher(url);
        String result = null;
        if (matcher.find()) {
            result = matcher.group();
        }

        pattern = Pattern.compile("(?<=licdn.com/\")(.*)(?=/)(.*)(?=-)");
        matcher = pattern.matcher(url);
        if (matcher.find()) {
            result += matcher.group();
        }

        return result;
//        return "c2214c83-a038-43b4-8d6a-63e425a8c230/L4E60b2cb7a31c64000";
    }

    public List<String> extractQualityLevel() {
        List<String> result = new ArrayList<>();
        result.add("3200000");
        return result;
    }

    public List<String> extractFragmentIds() {
        List<String> result = new ArrayList<>();
        result.add("24304050");
        result.add("24484140");
        return result;
    }

    public byte[] extractFragment(String fragmentUrl) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (BufferedInputStream in = new BufferedInputStream(new URL(fragmentUrl).openStream())) {
            int bytesRead;
            byte[] data = new byte[1024];
            while ((bytesRead = in.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            buffer.flush();
            attempts = 0;
            return buffer.toByteArray();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Fragment URL is not valid: " + fragmentUrl, e);
        } catch (IOException e) {
            e.printStackTrace();
            attempts++;
            return extractFragment(fragmentUrl);
        }
    }

    public static void main(String[] args) {
        LinkedInVideoService service = new LinkedInVideoService();
        System.out.println(service.extractVideoId("https://streamweu-livectorprodmedia17-euwe.licdn.com/c50f2327-7fa7-4cc8-8a2c-35711eb5296d/L4E60a4445e9d065000-livemanifest.ism/manifest(format=m3u8-aapl-v3)"));
    }

}
