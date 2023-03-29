package com.gilgamesh.sdkapi.controller;


import com.gilgamesh.sdkapi.config.ErrorCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * ACTION基类
 */
public class BaseController {
    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    public Map<String, Object> getSuccMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("resultCode", ErrorCodeEnum.SUCCESS.getCode());
        map.put("resultMsg", ErrorCodeEnum.SUCCESS.getMessage());
        return map;
    }

    public Map<String, Object> getSuccMap(Object data) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("resultCode", ErrorCodeEnum.SUCCESS.getCode());
        map.put("resultMsg", ErrorCodeEnum.SUCCESS.getMessage());
        map.put("data", data);
        return map;
    }

    public Map<String, Object> getErrorMap(String msg) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("resultCode", ErrorCodeEnum.ERROR.getCode());
        map.put("resultMsg", msg);

        return map;
    }

    public Map<String, Object> getErrorMap(ErrorCodeEnum code) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("resultCode", code.getCode());
        map.put("resultMsg", code.getMessage());
        return map;
    }


}
