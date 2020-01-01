package com.byteblogs.helloblog.music.domain.bo;

import lombok.Data;

@Data
public class Music {
    private String name;
    private String url;
    private String artist;
    private String cover;
    private String lrc;
}
