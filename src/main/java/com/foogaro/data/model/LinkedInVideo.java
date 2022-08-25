package com.foogaro.data.model;

import java.io.Serializable;
import java.util.StringJoiner;

public class LinkedInVideo implements Serializable {

    private String postUrl;
    private String qualityUrl;
    private String fragmentsUrl;
    private String fragmentUrl;
    private String qualityLevel;

    public LinkedInVideo() {
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getQualityUrl() {
        return qualityUrl;
    }

    public void setQualityUrl(String qualityUrl) {
        this.qualityUrl = qualityUrl;
    }

    public String getFragmentsUrl() {
        return fragmentsUrl;
    }

    public void setFragmentsUrl(String fragmentsUrl) {
        this.fragmentsUrl = fragmentsUrl;
    }

    public String getFragmentUrl() {
        return fragmentUrl;
    }

    public void setFragmentUrl(String fragmentUrl) {
        this.fragmentUrl = fragmentUrl;
    }

    public String getQualityLevel() {
        return qualityLevel;
    }

    public void setQualityLevel(String qualityLevel) {
        this.qualityLevel = qualityLevel;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LinkedInVideo.class.getSimpleName() + "[", "]")
                .add("postUrl='" + postUrl + "'")
                .add("qualityUrl='" + qualityUrl + "'")
                .add("fragmentsUrl='" + fragmentsUrl + "'")
                .add("fragmentUrl='" + fragmentUrl + "'")
                .add("qualityLevel='" + qualityLevel + "'")
                .toString();
    }
}
