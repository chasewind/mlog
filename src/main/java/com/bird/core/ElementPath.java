package com.bird.core;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 类ElementPath.java的实现描述
 * </p>
 * <p>
 * 元素XPath纪录
 * </p>
 * 
 * @author dongwei.ydw 2016年4月8日 上午10:08:49
 */
public class ElementPath {

    ArrayList<String> partList = new ArrayList<String>();

    public ElementPath(){
    }

    public ElementPath(List<String> list){
        partList.addAll(list);
    }

    /**
     * 元素标签的处理，包含单节点以及复合节点
     * 
     * @param pathStr
     */
    public ElementPath(String pathStr){
        if (pathStr == null) {
            return;
        }

        String[] partArray = pathStr.split("/");
        if (partArray == null) return;

        for (String part : partArray) {
            if (part.length() > 0) {
                partList.add(part);
            }
        }
    }

    public ElementPath duplicate() {
        ElementPath p = new ElementPath();
        p.partList.addAll(this.partList);
        return p;
    }

    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof ElementPath)) {
            return false;
        }

        ElementPath r = (ElementPath) o;

        if (r.size() != size()) {
            return false;
        }

        int len = size();

        for (int i = 0; i < len; i++) {
            if (!equalityCheck(get(i), r.get(i))) {
                return false;
            }
        }

        return true;
    }

    private boolean equalityCheck(String x, String y) {
        return x.equalsIgnoreCase(y);
    }

    public List<String> getCopyOfPartList() {
        return new ArrayList<String>(partList);
    }

    public void push(String s) {
        partList.add(s);
    }

    public String get(int i) {
        return (String) partList.get(i);
    }

    public void pop() {
        if (!partList.isEmpty()) {
            partList.remove(partList.size() - 1);
        }
    }

    public String peekLast() {
        if (!partList.isEmpty()) {
            int size = partList.size();
            return (String) partList.get(size - 1);
        } else {
            return null;
        }
    }

    public int size() {
        return partList.size();
    }

    protected String toStableString() {
        StringBuilder result = new StringBuilder();
        for (String current : partList) {
            result.append("[").append(current).append("]");
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return toStableString();
    }
}
