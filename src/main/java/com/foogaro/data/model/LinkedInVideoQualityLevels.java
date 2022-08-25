package com.foogaro.data.model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkedInVideoQualityLevels {

/*
    REQUEST
        https://streamweu-livectorprodmedia17-euwe.licdn.com/c50f2327-7fa7-4cc8-8a2c-35711eb5296d/L4E60a4445e9d065000-livemanifest.ism/manifest(format=m3u8-aapl-v3)
    RESPONSE
        #EXTM3U
        #EXT-X-VERSION:3
        #EXT-X-STREAM-INF:BANDWIDTH=555936,RESOLUTION=768x432,CODECS="avc1.64001e,mp4a.40.5"
        QualityLevels(400000)/Manifest(video,format=m3u8-aapl-v3,audiotrack=audio_und,filter=L4E60a4445e9d065000-trimming_filter_c50f2327-7fa7-4cc8-8a2c-35711eb5296d)
        #EXT-X-STREAM-INF:BANDWIDTH=964736,RESOLUTION=960x540,CODECS="avc1.64001f,mp4a.40.5"
        QualityLevels(800000)/Manifest(video,format=m3u8-aapl-v3,audiotrack=audio_und,filter=L4E60a4445e9d065000-trimming_filter_c50f2327-7fa7-4cc8-8a2c-35711eb5296d)
        #EXT-X-STREAM-INF:BANDWIDTH=1782336,RESOLUTION=1024x576,CODECS="avc1.64001f,mp4a.40.5"
        QualityLevels(1600000)/Manifest(video,format=m3u8-aapl-v3,audiotrack=audio_und,filter=L4E60a4445e9d065000-trimming_filter_c50f2327-7fa7-4cc8-8a2c-35711eb5296d)
        #EXT-X-STREAM-INF:BANDWIDTH=3417536,RESOLUTION=1280x720,CODECS="avc1.64001f,mp4a.40.5"
        QualityLevels(3200000)/Manifest(video,format=m3u8-aapl-v3,audiotrack=audio_und,filter=L4E60a4445e9d065000-trimming_filter_c50f2327-7fa7-4cc8-8a2c-35711eb5296d)
        #EXT-X-STREAM-INF:BANDWIDTH=138976,CODECS="mp4a.40.5"
        QualityLevels(128000)/Manifest(audio_und,format=m3u8-aapl-v3,filter=L4E60a4445e9d065000-trimming_filter_c50f2327-7fa7-4cc8-8a2c-35711eb5296d)
*/

    private List<LinkedInVideoQualityLevel> qualityLevels = new ArrayList<>();

    public LinkedInVideoQualityLevels(String url) {
        processUrl(url);
    }

    private void processUrl(String url) {
        try (BufferedInputStream bis = new BufferedInputStream(new URL(url).openStream())) {
            BufferedReader br = new BufferedReader(new InputStreamReader(bis));

            String responseLine;
            LinkedInVideoQualityLevel.Builder builder = new LinkedInVideoQualityLevel.Builder();
            while ((responseLine = br.readLine()) != null) {
                String bandwith = extractBandwith(responseLine);
                if (bandwith != null) builder.bandwitdh(bandwith);
                String resolution = extractResolution(responseLine);
                if (resolution != null) builder.resolution(resolution);
                String codecs = extractCodecs(responseLine);
                if (codecs != null) builder.codecs(codecs);
                String qualityLevel = extractQualityLevel(responseLine);
                if (qualityLevel != null) {
                    builder.qualityLevel(qualityLevel);
                    qualityLevels.add(builder.build());
                    builder = new LinkedInVideoQualityLevel.Builder();
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String extractBandwith(String metadata) {
        Pattern pattern = Pattern.compile("(?<=BANDWIDTH=)(.*?)(?=,)");
        Matcher matcher = pattern.matcher(metadata);
        String result = null;
        if (matcher.find()) {
            result = matcher.group();
        }
        return result;
    }

    private String extractResolution(String metadata) {
        Pattern pattern = Pattern.compile("(?<=RESOLUTION=)(\\d+)x(\\d+)");
        Matcher matcher = pattern.matcher(metadata);
        String result = null;
        if (matcher.find()) {
            result = matcher.group();
        }
        return result;
    }

    private String extractCodecs(String metadata) {
        Pattern pattern = Pattern.compile("(?<=CODECS=\")(.*)(?=\")");
        Matcher matcher = pattern.matcher(metadata);
        String result = null;
        if (matcher.find()) {
            result = matcher.group();
        }
        return result;
    }

    private String extractQualityLevel(String metadata) {
        Pattern pattern = Pattern.compile("(?<=QualityLevels\\()\\d+");
        Matcher matcher = pattern.matcher(metadata);
        String result = null;
        if (matcher.find()) {
            result = matcher.group();
        }
        return result;
    }

    public List<LinkedInVideoQualityLevel> getQualityLevels() {
        Collections.sort(qualityLevels, Comparator.comparingInt(LinkedInVideoQualityLevel ::getQualityLevelAsInt));
        return qualityLevels;
    }

    public static void main(String[] args) {
        //LinkedInVideoFragments app = new LinkedInVideoFragments("Fragments(video=93067200,format=m3u8-aapl-v3,audiotrack=audio_und)");
        LinkedInVideoQualityLevels app = new LinkedInVideoQualityLevels("https://streamweu-livectorprodmedia17-euwe.licdn.com/c50f2327-7fa7-4cc8-8a2c-35711eb5296d/L4E60a4445e9d065000-livemanifest.ism/manifest(format=m3u8-aapl-v3)");
        List<LinkedInVideoQualityLevel> list = app.getQualityLevels();
        System.out.println(app.extractBandwith("#EXT-X-STREAM-INF:BANDWIDTH=555936,RESOLUTION=768x432,CODECS=\"avc1.64001e,mp4a.40.5\""));
        System.out.println(app.extractResolution("#EXT-X-STREAM-INF:BANDWIDTH=555936,RESOLUTION=768x432,CODECS=\"avc1.64001e,mp4a.40.5\""));
        System.out.println(app.extractCodecs("#EXT-X-STREAM-INF:BANDWIDTH=555936,RESOLUTION=768x432,CODECS=\"avc1.64001e,mp4a.40.5\""));
        System.out.println(app.extractQualityLevel("QualityLevels(400000)/Manifest(video,format=m3u8-aapl-v3,audiotrack=audio_und,filter=L4E60a4445e9d065000-trimming_filter_c50f2327-7fa7-4cc8-8a2c-35711eb5296d)"));
        System.out.println(app.getQualityLevels().get(0));
    }
}
