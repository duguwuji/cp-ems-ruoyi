package com.cpems.oss.exception;

/**
 * OSS异常类
 *
 * @author cpems
 */
public class OssException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OssException(String msg) {
        super(msg);
    }

}
