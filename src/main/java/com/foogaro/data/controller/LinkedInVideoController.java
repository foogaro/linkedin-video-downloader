package com.foogaro.data.controller;

import com.foogaro.data.model.LinkedInVideo;
import com.foogaro.data.model.LinkedInVideoFragments;
import com.foogaro.data.model.LinkedInVideoQualityLevels;
import com.foogaro.data.service.LinkedInVideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("")
public class LinkedInVideoController {

    private final Logger logger = LoggerFactory.getLogger(LinkedInVideoController.class);

    private final LinkedInVideoService service;

    @Autowired
    public LinkedInVideoController(LinkedInVideoService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String main(Model model) {
        return "redirect:/fetch-video";
    }


    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("linkedInVideo", new LinkedInVideo());
        return "forward:/fetch-video";
    }

    @GetMapping("/fetch-video")
    public String fetch_video(Model model) {
        model.addAttribute("linkedInVideo", new LinkedInVideo());
        return "fetch-video";
    }

    @PostMapping("/fetch")
    public String fetch(@ModelAttribute("linkedInVideo") LinkedInVideo linkedInVideo, Model model) {
        model.addAttribute("linkedInVideo", linkedInVideo);
        model.addAttribute("qualityLevels",
                new LinkedInVideoQualityLevels(linkedInVideo.getQualityUrl()).getQualityLevels());
        return "download-video";
    }

    //@PostMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PostMapping("/download")
    public ResponseEntity<StreamingResponseBody> download(@ModelAttribute("linkedInVideo") LinkedInVideo linkedInVideo, Model model,
                                                          final HttpServletResponse response) {

        String responseFilename = "linkedin-video-" + System.currentTimeMillis();
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment;filename=" + responseFilename + ".zip");

        StreamingResponseBody stream = out -> {

            final ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
            final ZipEntry zipEntry=new ZipEntry(responseFilename+".mp4");
            zipOut.putNextEntry(zipEntry);

            String videoId = service.extractVideoId(linkedInVideo.getQualityUrl());
            logger.info("Video ID: {}", videoId);
            String qualityLevel = linkedInVideo.getQualityLevel();
            logger.info("Quality Level: {}", qualityLevel);
            LinkedInVideoFragments linkedInVideoFragments = new LinkedInVideoFragments(linkedInVideo.getFragmentsUrl());
            List<String> fragmentIds = linkedInVideoFragments.getFragmentIds();
            logger.info("Number of video fragments: {}", fragmentIds.size());
            String[] fragmentUrls = extractFragmentUrls(videoId,qualityLevel,fragmentIds.toArray(new String[0]));

            for (String fragmentUrl : fragmentUrls) {
                logger.trace("Video Fragment URL: " + fragmentUrl);
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
        logger.info("Steaming video download as: {}", responseFilename + ".zip");
        return new ResponseEntity(stream, HttpStatus.OK);
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
