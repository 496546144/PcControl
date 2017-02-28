package com.hbung.pccontrol;

import org.json.JSONException;
import org.json.JSONStringer;

/**
 * 作者　　: 李坤
 * 创建时间:2017/2/28　16:55
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class JsonHelp {
    public String getKey(int keyCode) {
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object();
            jsonStringer.key("code");
            jsonStringer.value(keyCode);
            jsonStringer.key("action");
            jsonStringer.value(1);
            jsonStringer.endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStringer.toString();
    }
}
