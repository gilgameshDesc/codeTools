package com.gilgamesh.sdkapi.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.gilgamesh.sdkapi.bean.numsBean;
import com.gilgamesh.sdkapi.dao.ApiMapper;
import com.gilgamesh.sdkapi.utils.NetUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Gilgamesh
 * @date 2022/9/8
 */
@RestController
@RequestMapping(value = "/api")
public class ApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Resource
    private ApiMapper apiMapper;

    @RequestMapping(value = "getNums", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getNums(@RequestBody String reqData, HttpServletResponse httpServletResponse) {
        Long start = System.currentTimeMillis();
        JSONObject request = JSONObject.parseObject(reqData);
        logger.info("Request getNums-查询号码接口.[{}]", request.toJSONString());
        try {
            System.out.println("getNums1||"+(System.currentTimeMillis()-start));
            String lastNum = request.getString("lastNum");
            if(StringUtils.isEmpty(lastNum)||lastNum.equals("-")){
                lastNum= "";
            }
            List<numsBean> numdata= apiMapper.selectNums(lastNum);
            System.out.println("getNums2||"+(System.currentTimeMillis()-start));
            System.out.println(numdata.size());
            return getSuccMap(numdata);

        } catch (Exception e) {
            logger.error("Response getNums-查询号码接口 异常", e);
            return getErrorMap("系统异常，请联系管理员");
        }
    }

    @RequestMapping(value = "insertNums", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> insertNums(@RequestBody String reqData) {
        JSONObject request = JSONObject.parseObject(reqData);
        logger.info("Request insertNums-查询号码接口.[{}]", request.toJSONString());
        try {
            for (int i = 20300002; i < 20350001; i++) {
                String number = String.valueOf(i);
                String lastNum = number.substring(7);
                apiMapper.insertNumPBX(number);
            }
            return getSuccMap();
        } catch (Exception e) {
            logger.error("Response insertNums-查询号码接口 异常", e);
            return getErrorMap("系统异常，请联系管理员");
        }
    }

    @RequestMapping(value = "queryApi", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryApi(@RequestBody String reqData) {
        Long start = System.currentTimeMillis();
        JSONObject request = JSONObject.parseObject(reqData);
        logger.info("Request queryApi-查询号码接口.[{}]", request.toJSONString());
        try {
            System.out.println("queryApi1||"+(System.currentTimeMillis()-start));
            String lastNum = request.getString("lastNum");
            if(StringUtils.isEmpty(lastNum)){
                lastNum= "";
            }
            JSONObject xx = new JSONObject();
            xx.put("lastNum",lastNum);
            NetUtil.send("http://172.20.11.14:8088/sdkapi/api/getNums","POST",xx.toString(),"application/json");
            System.out.println("queryApi2||"+(System.currentTimeMillis()-start));
            return getSuccMap();
        } catch (Exception e) {
            logger.error("Response queryApi-查询号码接口 异常", e);
            return getErrorMap("系统异常，请联系管理员");
        }
    }
}
