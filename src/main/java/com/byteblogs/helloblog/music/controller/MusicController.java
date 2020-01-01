package com.byteblogs.helloblog.music.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.helloblog.music.domain.bo.Music;
import com.byteblogs.helloblog.music.service.MusicService;
import com.byteblogs.helloblog.music.util.MusicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/music")
public class MusicController {
    @Autowired
    private MusicService musicService;

    @GetMapping("/music/v1/list")
    public Result getPlayList(){
        return musicService.getPlayList();
    }

}
