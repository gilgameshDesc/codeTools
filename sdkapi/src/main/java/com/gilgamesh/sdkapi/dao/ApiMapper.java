package com.gilgamesh.sdkapi.dao;

import com.gilgamesh.sdkapi.bean.numsBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Gilgamesh
 * @date 2022/9/8
 */
@Mapper
public interface ApiMapper {
    void insertNum(@Param("number") String number, @Param("lastNum") String lastNum);
    List<numsBean> selectNums(@Param("lastNum") String lastNum);

    void insertNumPBX(@Param("number") String number);
}
