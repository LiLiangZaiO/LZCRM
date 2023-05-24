package com.lzl.common_utils.utils;


import com.lzl.common_utils.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 响应工具类
 *
 * @author CaoChenLei
 */
public class ResponseUtils {
    private static final Logger logger = LoggerFactory.getLogger(ResponseUtils.class);

    /**
     * 向浏览器响应一个json字符串
     *
     * @param response 响应对象
     * @param status   状态码
     * @param msg      响应信息
     */
    public static void write(HttpServletResponse response, int status, String msg) {
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.setStatus(status);
            PrintWriter out = response.getWriter();
            out.write(JsonUtils.toString(R.error().code(status).message(msg)));
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("响应出错：" + msg, e);
        }
    }

    /**
     * 谷粒学院的方法
     * @param response
     * @param r
     */
    public static void out(HttpServletResponse response, R r) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/json");
            //重点：一定要设置状态码，才能才无配置拦截的请求下，不输出这个--直接报错返回
            response.setStatus(r.getCode());
            PrintWriter out = response.getWriter();
            out.write(JsonUtils.toString(r));
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("响应出错：" + r.getMessage(), e);
        }
    }
}