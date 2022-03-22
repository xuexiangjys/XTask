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

package com.xuexiang.xtaskdemo.fragment.usecase.concurrent.entity;

import androidx.annotation.NonNull;

/**
 * 富文本信息
 *
 * @author xuexiang
 * @since 3/22/22 1:13 AM
 */
public class RichInfo {

    private String id;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 图片链接
     */
    private String imgUrl;

    /**
     * 视频链接
     */
    private String videoUrl;

    public RichInfo(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public RichInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RichInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public RichInfo setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public RichInfo setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "富文本编号:" + id
                + ", 描述信息:" + description
                + ", 图片链接:" + imgUrl
                + ", 视频链接:" + videoUrl;
    }
}
