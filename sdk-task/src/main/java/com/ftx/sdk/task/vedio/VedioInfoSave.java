package com.ftx.sdk.task.vedio;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 描述：视频信息同步类
 *
 * @auth:xiaojun.yin
 * @createTime 2019-05-09 08:15
 */
@Component
public class VedioInfoSave {

    /**
     * 同步
     */
    @Scheduled(cron = "2 3 4 * * ?")
    void synchronizedVedioInfoToDb() {
       //TODO 在这里添加定时要做的

    }


}
