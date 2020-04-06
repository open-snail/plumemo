package com.byteblogs.plumemo.music.service.impl;

import com.byteblogs.common.base.domain.Result;
import com.byteblogs.plumemo.music.service.MusicService;
import com.byteblogs.plumemo.music.util.MusicUtil;
import org.springframework.stereotype.Service;


@Service
public class MusicServiceImpl implements MusicService {

    @Override
    public Result getPlayList() {
      return Result.createWithModels(MusicUtil.getPlayList());
    }
}
