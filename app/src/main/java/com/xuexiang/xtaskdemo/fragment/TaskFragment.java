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

package com.xuexiang.xtaskdemo.fragment;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xtaskdemo.core.BaseSimpleListFragment;

import java.util.List;

/**
 * @author xuexiang
 * @since 1/20/22 12:24 AM
 */
@Page(name = "Task任务使用")
public class TaskFragment extends BaseSimpleListFragment {

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("简单串行任务");
        lists.add("简单并行任务");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch(position) {
            case 0:

                break;
            case 1:

                break;
            default:
                break;
        }

    }
}
