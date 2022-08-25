package com.foogaro.data.model;

import java.io.Serializable;
import java.util.StringJoiner;


public class LinkedInVideoQualityLevel implements Serializable {

    private final String bandwitdh;
    private final String resolution;
    private final String codecs;
    private final String qualityLevel;

    private LinkedInVideoQualityLevel(Builder builder) {
        this.bandwitdh = builder.bandwitdh;
        this.resolution = builder.resolution;
        this.codecs = builder.codecs;
        this.qualityLevel = builder.qualityLevel;
    }

    public String getBandwitdh() {
        return bandwitdh;
    }

    public String getResolution() {
        return resolution;
    }

    public String getCodecs() {
        return codecs;
    }

    public String getQualityLevel() {
        return qualityLevel;
    }

    public int getQualityLevelAsInt() {
        try {
            return Integer.parseInt(qualityLevel);
        } catch (Throwable t) {
            return 0;
        }
    }

    public static class Builder {
        private String bandwitdh;
        private String resolution;
        private String codecs;
        private String qualityLevel;

        public Builder() {}

        public Builder bandwitdh(String bandwitdh) {
            this.bandwitdh = bandwitdh;
            return this;
        }

        public Builder resolution(String resolution) {
            this.resolution = resolution;
            return this;
        }

        public Builder codecs(String codecs) {
            this.codecs = codecs;
            return this;
        }

        public Builder qualityLevel(String qualityLevel) {
            this.qualityLevel = qualityLevel;
            return this;
        }

        public LinkedInVideoQualityLevel build() {
            return new LinkedInVideoQualityLevel(this);
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LinkedInVideoQualityLevel.class.getSimpleName() + "[", "]")
                .add("qualityLevel='" + qualityLevel + "'")
                .add("resolution='" + resolution + "'")
                .add("bandwitdh='" + bandwitdh + "'")
                .add("codecs='" + codecs + "'")
                .toString();
    }

    public String description() {
        return "Quality: '" + qualityLevel + "' - Resolution: '" + resolution + "' - Codecs: '" + codecs + "'";
    }
}
