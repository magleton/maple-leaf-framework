package com.geoxus.ueditor.define;

import com.geoxus.ueditor.GXEncoder;

import java.util.*;

/**
 * 多状态集合状态
 * 其包含了多个状态的集合, 其本身自己也是一个状态
 */
public class GXMultiState implements GXState {

    private boolean state = false;
    private String info = null;
    private Map<String, Long> intMap = new HashMap<String, Long>();
    private Map<String, String> infoMap = new HashMap<String, String>();
    private List<String> stateList = new ArrayList<String>();

    public GXMultiState(boolean state) {
        this.state = state;
    }

    public GXMultiState(boolean state, String info) {
        this.state = state;
        this.info = info;
    }

    public GXMultiState(boolean state, int infoKey) {
        this.state = state;
        this.info = GXEditorResponseInfo.getStateInfo(infoKey);
    }

    @Override
    public boolean isSuccess() {
        return this.state;
    }

    public void addState(GXState state) {
        stateList.add(state.toJSONString());
    }

    /**
     * 该方法调用无效果
     */
    @Override
    public void putInfo(String name, String val) {
        this.infoMap.put(name, val);
    }

    @Override
    public String toJSONString() {
        String stateVal = this.isSuccess() ? GXEditorResponseInfo.getStateInfo(GXEditorResponseInfo.SUCCESS) : this.info;
        StringBuilder builder = new StringBuilder();
        builder.append("{\"state\": \"").append(stateVal).append("\"");
        // 数字转换
        Iterator<String> iterator = this.intMap.keySet().iterator();
        while (iterator.hasNext()) {
            stateVal = iterator.next();
            builder.append(",\"").append(stateVal).append("\": ").append(this.intMap.get(stateVal));
        }
        iterator = this.infoMap.keySet().iterator();
        while (iterator.hasNext()) {
            stateVal = iterator.next();
            builder.append(",\"").append(stateVal).append("\": \"").append(this.infoMap.get(stateVal)).append("\"");
        }
        // 添加双引号，解决前端不能解析的问题
        builder.append(", \"list\": [");
        iterator = this.stateList.iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next()).append(",");
        }
        if (!this.stateList.isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append(" ]}");
        return GXEncoder.toUnicode(builder.toString());
    }

    @Override
    public void putInfo(String name, long val) {
        this.intMap.put(name, val);
    }
}
