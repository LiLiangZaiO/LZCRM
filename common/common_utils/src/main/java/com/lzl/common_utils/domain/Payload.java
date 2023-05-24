package com.lzl.common_utils.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 为了方便后期获取token中的用户信息，将token中载荷部分单独封装成一个对象
 *
 * @author CaoChenLei
 */
@Data
public class Payload<T> implements Serializable {
    private String id;
    private T userInfo;
    private Date expiration;
}

