package com.shenyx.treeview.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.shenyx.treeview.utils.Node;
import com.shenyx.treeview.utils.TreeHelper;

import java.util.List;

/**
 * Created by pp on 2018/4/26.
 */

public abstract class TreeListViewAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<Node> mAllNodes;
    protected List<Node> mVisibleNodes;
    protected LayoutInflater mInflate;

    protected ListView mTree;
    /**
     * 设置Node的点击回调
     *
     * @author zhy
     *
     */
    public interface OnTreeNodeClickListener
    {
        void onClick(Node node, int position);
    }

    private OnTreeNodeClickListener mListener;

    public void setOnTreeNodeClickListener(OnTreeNodeClickListener mListener)
    {
        this.mListener = mListener;
    }

    public TreeListViewAdapter(Context mContext, List<T> datas, ListView mTree, int defaultExpandLevel) throws IllegalAccessException {
        this.mContext = mContext;
        this.mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
        this.mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
        this.mInflate = LayoutInflater.from(mContext);
        this.mTree = mTree;
        mTree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expandOrCollapse(position);

                if(mListener != null){
                    mListener.onClick(mVisibleNodes.get(position), position);
                }
            }
        });
    }

    /**
     * 点击展开或收拢
     * @param position
     */
    private void expandOrCollapse(int position){
        Node n = mVisibleNodes.get(position);
        if(n != null){
            if(n .isLeaf()){
                return;
            }
            n.setExpand(!n.isExpand());
            mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mVisibleNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mVisibleNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = mVisibleNodes.get(position);
        convertView = getConvertView(node, position, convertView, parent);
        convertView.setPadding(node.getLevel() * 30, 3, 3, 3);
        return convertView;
    }

    public abstract View getConvertView(Node node, int position, View convertView, ViewGroup parent);
}
