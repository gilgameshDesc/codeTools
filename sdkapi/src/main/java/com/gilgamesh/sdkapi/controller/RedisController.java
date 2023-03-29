package com.gilgamesh.sdkapi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.gilgamesh.sdkapi.bean.numsBean;
import com.gilgamesh.sdkapi.dao.ApiMapper;
import com.gilgamesh.sdkapi.utils.NetUtil;
import com.gilgamesh.sdkapi.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Gilgamesh
 * @date 2022/9/8
 */
@RestController
@RequestMapping(value = "/redisApi")
public class RedisController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RedisController.class);

    @Resource
    private RedisUtils redisUtils;

    @RequestMapping(value = "getExts", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getExts(@RequestBody String reqData) {
        Long start = System.currentTimeMillis();
        JSONObject request = JSONObject.parseObject(reqData);
        logger.info("Request getNums-查询号码接口.[{}]", request.toJSONString());
        try {
            System.out.println("queryApi3||" + (System.currentTimeMillis() - start));
            List<Object> keys = new ArrayList();
            for (int i = 129550; i < 129651; i++) {
                String key = "extensionMonitorInfo:extension_monitor_info_" + i + "@test.com";
                keys.add(key);
            }
            System.out.println("queryApi2||" + (System.currentTimeMillis() - start));
            List<Object> values = redisUtils.multiGet(keys);
            System.out.println("queryApi4||" + (System.currentTimeMillis() - start));
            System.out.println(values);
            return getSuccMap();

        } catch (Exception e) {
            logger.error("Response getNums-查询号码接口 异常", e);
            return getErrorMap("系统异常，请联系管理员");
        }
    }


    @RequestMapping(value = "getExtsX", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getExtsX(@RequestBody String reqData) {
        Long start = System.currentTimeMillis();
        JSONObject request = JSONObject.parseObject(reqData);
        logger.info("Request getNums-查询号码接口.[{}]", request.toJSONString());
        try {
            System.out.println("queryApi1||" + (System.currentTimeMillis() - start));
            List<String> getKeys = redisUtils.scan("extensionMonitorInfo:extension_monitor_info_12955*", 10L);
            System.out.println("queryApi2||" + (System.currentTimeMillis() - start));
            System.out.println(getKeys.size());
            System.out.println(getKeys);
            List<Object> keys = new ArrayList<Object>(getKeys);
            System.out.println("queryApi3||" + (System.currentTimeMillis() - start));
//            for (int i = 129550; i < 129651; i++) {
//                String key = "extensionMonitorInfo:extension_monitor_info_" + i + "@test.com";
//                keys.add(key);
//            }
//            System.out.println("queryApi2||"+(System.currentTimeMillis()-start));
            List<Object> values = redisUtils.multiGet(keys);
            System.out.println("queryApi4||" + (System.currentTimeMillis() - start));
            System.out.println(values.size());
            System.out.println(values);
            return getSuccMap();

        } catch (Exception e) {
            logger.error("Response getNums-查询号码接口 异常", e);
            return getErrorMap("系统异常，请联系管理员");
        }
    }


    @RequestMapping(value = "getExtsLua", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getExtsLua(@RequestBody String reqData) {
        Long start = System.currentTimeMillis();
        JSONObject request = JSONObject.parseObject(reqData);
        logger.info("Request getNums-查询号码接口.[{}]", request.toJSONString());
        try {

            List<String> getKeys = redisUtils.scan("extensionMonitorInfo:extension_monitor_info_*", 2L);
            String luaScript = "local cursor = 0\n" +
                    "local res = redis.call(\"SCAN\", cursor,\"MATCH\", KEYS[1],\"COUNT\", 10);" +
                    "\n" +
                    "return res[1]";
            String[] keys = {"extensionMonitorInfo:extension_monitor_info_*"};

            redisUtils.executeLuaScript(luaScript, String.class, keys, "10");

            return getSuccMap();

        } catch (Exception e) {
            logger.error("Response getNums-查询号码接口 异常", e);
            return getErrorMap("系统异常，请联系管理员");
        }
    }
}
