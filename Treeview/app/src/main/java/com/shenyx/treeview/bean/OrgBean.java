package com.shenyx.treeview.bean;

import com.shenyx.treeview.utils.annotation.TreeNodeId;
import com.shenyx.treeview.utils.annotation.TreeNodeLabel;
import com.shenyx.treeview.utils.annotation.TreeNodePid;

/**
 * Created by pp on 2018/4/26.
 */

public class OrgBean {
    @TreeNodeId
    private int _id;
    @TreeNodePid
    private int parentId;
    @TreeNodeLabel
    private String name;

    public OrgBean(int _id, int parentId, String name) {
        this._id = _id;
        this.parentId = parentId;
        this.name = name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
