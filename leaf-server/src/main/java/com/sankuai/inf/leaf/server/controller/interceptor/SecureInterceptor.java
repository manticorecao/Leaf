/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 manticorecao@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */


package com.sankuai.inf.leaf.server.controller.interceptor;

import com.sankuai.inf.leaf.common.PropertyFactory;
import com.sankuai.inf.leaf.server.exception.LeafServerException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Properties;

import static com.sankuai.inf.leaf.server.Constants.LEAF_SECURE_ENABLE;
import static com.sankuai.inf.leaf.server.Constants.LEAF_SECURE_HTTP_HEADER;
import static com.sankuai.inf.leaf.server.Constants.LEAF_SECURE_SECRET;

/**
 * SecureInterceptor
 *
 * @author caobin
 * @version 1.0
 * @date 2022.01.05
 */
@Component
public class SecureInterceptor implements HandlerInterceptor {

    private boolean secure;

    private String secret;


    public SecureInterceptor(){
        // Load Secure Config
        Properties properties = PropertyFactory.getProperties();
        secure = Boolean.valueOf(properties.getProperty(LEAF_SECURE_ENABLE));
        if (secure) {
            secret = properties.getProperty(LEAF_SECURE_SECRET);
            if (secret == null || secret.trim().length() == 0) {
                throw new IllegalArgumentException("secret is required.");
            }
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (secure) {
            if (!secret.equals(httpServletRequest.getHeader(LEAF_SECURE_HTTP_HEADER))){
                throw new LeafServerException("incorrect secret");
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
