package com.foogaro.data.model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkedInVideoFragments {

/*
    REQUEST
        https://streamweu-livectorprodmedia17-euwe.licdn.com/c50f2327-7fa7-4cc8-8a2c-35711eb5296d/L4E60a4445e9d065000-livemanifest.ism/QualityLevels(3200000)/Manifest(video,format=m3u8-aapl-v3,audiotrack=audio_und,filter=L4E60a4445e9d065000-trimming_filter_c50f2327-7fa7-4cc8-8a2c-35711eb5296d)
    RESPONSE
        #EXTM3U
        #EXT-X-VERSION:3
        #EXT-X-PLAYLIST-TYPE:VOD
        #EXT-X-ALLOW-CACHE:NO
        #EXT-X-MEDIA-SEQUENCE:517
        #EXT-X-TARGETDURATION:3
        #EXT-X-PROGRAM-DATE-TIME:2022-08-12T08:59:44.080Z
        #EXTINF:2.001000,no-desc
        Fragments(video=93067200,format=m3u8-aapl-v3,audiotrack=audio_und)
        #EXTINF:2.003000,no-desc
        Fragments(video=93247290,format=m3u8-aapl-v3,audiotrack=audio_und)
        #EXTINF:1.998000,no-desc
        Fragments(video=93427560,format=m3u8-aapl-v3,audiotrack=audio_und)
        ...
        #EXTINF:2.027000,no-desc
        Fragments(video=480246210,format=m3u8-aapl-v3,audiotrack=audio_und)
        #EXTINF:1.983000,no-desc
        Fragments(video=480428640,format=m3u8-aapl-v3,audiotrack=audio_und)
        #EXTINF:1.992000,no-desc
        Fragments(video=480607110,format=m3u8-aapl-v3,audiotrack=audio_und)
        #EXT-X-ENDLIST
*/

    private List<String> fragmentIds = new ArrayList<>();

    public LinkedInVideoFragments(String url) {
        processFragmentUrl(url);
    }

    private void processFragmentUrl(String fragmentUrl) {
        try (BufferedInputStream bis = new BufferedInputStream(new URL(fragmentUrl).openStream())) {
            BufferedReader br = new BufferedReader(new InputStreamReader(bis));
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                String fragmentId = extractFragmentId(responseLine);
                if (fragmentId != null) {
                    fragmentIds.add(fragmentId);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String extractFragmentId(String metadata) {
        Pattern pattern = Pattern.compile("(?<=video=)\\d+");
        Matcher matcher = pattern.matcher(metadata);
        String fragmentId = null;
         if (matcher.find()) {
            fragmentId = matcher.group();
        }
        return fragmentId;
    }

    public List<String> getFragmentIds() {
        return fragmentIds;
    }

    public static void main(String[] args) {
        //LinkedInVideoFragments app = new LinkedInVideoFragments("Fragments(video=93067200,format=m3u8-aapl-v3,audiotrack=audio_und)");
        LinkedInVideoFragments app = new LinkedInVideoFragments("https://streamweu-livectorprodmedia17-euwe.licdn.com/c50f2327-7fa7-4cc8-8a2c-35711eb5296d/L4E60a4445e9d065000-livemanifest.ism/QualityLevels(3200000)/Manifest(video,format=m3u8-aapl-v3,audiotrack=audio_und,filter=L4E60a4445e9d065000-trimming_filter_c50f2327-7fa7-4cc8-8a2c-35711eb5296d)");
        System.out.println(app.getFragmentIds().get(0));
    }
}
