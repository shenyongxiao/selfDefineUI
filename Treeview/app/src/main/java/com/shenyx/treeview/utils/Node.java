package com.shenyx.treeview.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pp on 2018/4/26.
 */

public class Node {

    private int id;
    private int pid = 0;
    private String name;

    /**
     * 树的层级
     */
    private int level;
    /**
     * 是否是展开
     */
    private boolean isExpand = false;

    private int icon;

    private Node parent;

    private List<Node> children = new ArrayList<>();

    public Node() {
    }

    public Node(int id, int pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isExpand() {
        return isExpand;
    }


    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    /**
     * 是否是根节点
     * @return
     */
    public boolean isRoot(){
        return  parent == null;
    }

    /**
     * 判断当前父节点的收缩状态
     * @return
     */
    public boolean isParentExpand(){
        if(parent  == null){
            return false;
        }
        return parent.isExpand();
    }

    /**
     * 判断是否是叶子节点
     * @return
     */
    public boolean isLeaf(){
        return children.size() == 0;
    }

    /**
     * 获取当前的层级
     * @return
     */
    public int getLevel(){
        return parent == null ? 0 : parent.getLevel() +1;
    }

    public void setExpand(boolean isExpand){
        this.isExpand = isExpand;
        if(!isExpand){
            for(Node node : children){
                node.setExpand(false);
            }
        }
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", isExpand=" + isExpand +
                ", icon=" + icon +
                '}';
    }
}
