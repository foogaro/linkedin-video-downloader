package com.foogaro.data.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/v1")
public class LinkedInVideoControllerOrig {

    private final Logger logger = LoggerFactory.getLogger(LinkedInVideoControllerOrig.class);

    @PostMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> download(final HttpServletRequest request,
                                                          final HttpServletResponse response) {

        String responseFilename = "linkedin-video-" + System.currentTimeMillis();
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment;filename=" + responseFilename + ".zip");

        StreamingResponseBody stream = out -> {

            final ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
            final ZipEntry zipEntry=new ZipEntry(responseFilename+".mp4");
            zipOut.putNextEntry(zipEntry);

            String postUrl = extractPostUrl();
            String videoId = extractVideoId();
            String qualityLevel = extractQualityLevel();
            String[] fragmentIds = extractFragmentIds();
            String[] fragmentUrls = extractFragmentUrls(videoId,qualityLevel,fragmentIds);

            for (String fragmentUrl : fragmentUrls) {
                System.out.println("Fragment URL: " + fragmentUrl);
                try (BufferedInputStream in = new BufferedInputStream(new URL(fragmentUrl).openStream())) {
                    byte dataBuffer[] = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                        zipOut.write(dataBuffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    logger.error("Exception while reading and streaming data {} ", e);
                }
            }
            zipOut.close();
        };
        logger.info("steaming response {} ", stream);
        return new ResponseEntity(stream, HttpStatus.OK);
    }

    private String extractVideoId() {
        return "c2214c83-a038-43b4-8d6a-63e425a8c230/L4E60b2cb7a31c64000";
    }

    private String extractQualityLevel() {
        return "3200000";
    }

    private String extractPostUrl() {
        return "https://www.linkedin.com/video/event/urn:li:ugcPost:6967491492307193856/";
    }

    private String[] extractFragmentIds() {
        return new String[]{"24304050","24484140"};
    }

    private String[] extractFragmentUrl(String videoId, String qualityLevel, String fragmentId) {
        return new String[]{"URL='https://livectorprodmedia17-euwe.licdn.com/"+videoId+"-livemanifest.ism/QualityLevels("+qualityLevel+")/Fragments(video="+fragmentId+",format=m3u8-aapl-v3,audiotrack=audio_und)'"};
    }

    private String[] extractFragmentUrls(String videoId, String qualityLevel, String[] fragmentIds) {
        String[] urls = new String[fragmentIds.length];
        int idx = 0;
        for (String fragmentId : fragmentIds) {
            urls[idx++] = "https://livectorprodmedia17-euwe.licdn.com/"+videoId+"-livemanifest.ism/QualityLevels("+qualityLevel+")/Fragments(video="+fragmentId+",format=m3u8-aapl-v3,audiotrack=audio_und)";
        }
        return urls;
    }
}
