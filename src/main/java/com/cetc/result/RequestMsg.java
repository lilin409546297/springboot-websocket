package com.cetc.result;

/**
 * 接收消息
 * @param <T>
 */
public class RequestMsg<T> {

    private T body;

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
