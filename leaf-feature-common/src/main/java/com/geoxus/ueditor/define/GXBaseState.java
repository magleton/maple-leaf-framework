package com.geoxus.ueditor.define;


import com.geoxus.ueditor.GXEncoder;

import java.util.HashMap;
import java.util.Map;

public class GXBaseState implements GXState {

    private boolean state = false;
    private String info = null;

    private Map<String, String> infoMap = new HashMap<String, String>();

    public GXBaseState() {
        this.state = true;
    }

    public GXBaseState(boolean state) {
        this.setState(state);
    }

    public GXBaseState(boolean state, String info) {
        this.setState(state);
        this.info = info;
    }

    public GXBaseState(boolean state, int infoCode) {
        this.setState(state);
        this.info = GXEditorResponseInfo.getStateInfo(infoCode);
    }

    @Override
    public boolean isSuccess() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setInfo(int infoCode) {
        this.info = GXEditorResponseInfo.getStateInfo(infoCode);
    }

    @Override
    public String toJSONString() {
        return this.toString();
    }

    @Override
    public String toString() {
        String key = null;
        String stateVal = this.isSuccess() ? GXEditorResponseInfo.getStateInfo(GXEditorResponseInfo.SUCCESS) : this.info;
        StringBuilder builder = new StringBuilder();
        builder.append("{\"state\": \"").append(stateVal).append("\"");
        for (String s : this.infoMap.keySet()) {
            key = s;
            builder.append(",\"").append(key).append("\": \"").append(this.infoMap.get(key)).append("\"");
        }
        builder.append("}");
        return GXEncoder.toUnicode(builder.toString());
    }

    @Override
    public void putInfo(String name, String val) {
        this.infoMap.put(name, val);
    }

    @Override
    public void putInfo(String name, long val) {
        this.putInfo(name, val + "");
    }

}
