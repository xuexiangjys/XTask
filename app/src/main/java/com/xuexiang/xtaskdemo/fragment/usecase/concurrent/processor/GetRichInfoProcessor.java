/*
 * Copyright (C) 2022 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xtaskdemo.fragment.usecase.concurrent.processor;

import com.xuexiang.xtaskdemo.fragment.usecase.business.processor.AbstractProcessor;
import com.xuexiang.xtaskdemo.fragment.usecase.concurrent.entity.RichInfo;
import com.xuexiang.xui.widget.textview.LoggerTextView;

/**
 * 2.4 获取商品的富文本信息
 *
 * @author xuexiang
 * @since 3/22/22 1:21 AM
 */
public class GetRichInfoProcessor extends AbstractProcessor<RichInfo> {
    private String richId;

    public GetRichInfoProcessor(LoggerTextView logger, String richId) {
        super(logger);
        this.richId = richId;
    }

    @Override
    public RichInfo process() {
        log("[富文本信息查询]开始执行...");
        RichInfo info = getRichInfoById(richId);
        log("[富文本信息查询]执行完毕!");
        onProcessSuccess(info);
        return info;
    }

    private RichInfo getRichInfoById(String id) {
        // 模拟耗费的时间
        mockProcess(380);
        return new RichInfo(id)
                .setDescription("精选湖南插旗菜业古法土坑腌制，取上乘泡脚酸菜，让你尽享人间美味！")
                .setImgUrl("http://inews.gtimg.com/newsapp_bt/0/14650183855/641")
                .setVideoUrl("https://haokan.baidu.com/v?pd=wisenatural&vid=8687875510146074163");
    }
}
