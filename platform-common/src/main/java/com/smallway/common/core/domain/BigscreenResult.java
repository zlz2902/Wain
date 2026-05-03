package com.smallway.common.core.domain;

import java.util.LinkedHashMap;

/**
 * ����ǰ��Լ����{ success, data, msg }
 */
public class BigscreenResult extends LinkedHashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public static BigscreenResult ok(Object data) {
        BigscreenResult r = new BigscreenResult();
        r.put("success", true);
        r.put("data", data);
        r.put("msg", "ok");
        return r;
    }

    public static BigscreenResult okMsg(String msg, Object data) {
        BigscreenResult r = new BigscreenResult();
        r.put("success", true);
        r.put("data", data);
        r.put("msg", msg);
        return r;
    }

    public static BigscreenResult fail(String msg) {
        BigscreenResult r = new BigscreenResult();
        r.put("success", false);
        r.put("data", null);
        r.put("msg", msg);
        return r;
    }
}
