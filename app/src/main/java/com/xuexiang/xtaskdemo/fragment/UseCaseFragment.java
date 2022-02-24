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
import com.xuexiang.xtaskdemo.core.BaseContainerFragment;
import com.xuexiang.xtaskdemo.fragment.usecase.AppStartFragment;
import com.xuexiang.xtaskdemo.fragment.usecase.ComplexBusinessFragment;

/**
 * @author xuexiang
 * @since 2/23/22 12:21 AM
 */
@Page(name = "应用场景\n列举常用的使用案例")
public class UseCaseFragment extends BaseContainerFragment {

    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                AppStartFragment.class,
                ComplexBusinessFragment.class
        };
    }
}