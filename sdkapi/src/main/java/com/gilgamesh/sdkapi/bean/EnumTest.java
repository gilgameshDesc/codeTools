package com.gilgamesh.sdkapi.bean;

/**
 * @author Gilgamesh
 * @date 2022/11/23
 */
public enum EnumTest {
    INVITE("invite"),
    UPDATE("update"),
    BYE("bye"),
    ACK("ack");

    private String value;
    EnumTest(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
