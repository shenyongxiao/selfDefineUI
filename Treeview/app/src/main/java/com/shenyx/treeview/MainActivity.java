package com.shenyx.treeview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.shenyx.treeview.adapter.SimpleTreeListViewAdapter;
import com.shenyx.treeview.bean.FileBean;
import com.shenyx.treeview.bean.OrgBean;
import com.shenyx.treeview.utils.Node;
import com.shenyx.treeview.utils.adapter.TreeListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ListView mTree;
    private SimpleTreeListViewAdapter<OrgBean> mAdapter;
    private List<FileBean> mDatas;
    private List<OrgBean> mDatas2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTree = findViewById(R.id.id_listview);

        initDatas();

        try {
            mAdapter = new SimpleTreeListViewAdapter<OrgBean>(this, mDatas2, mTree, 0);
            mTree.setAdapter(mAdapter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        initEvent();
    }
    private void initEvent(){
        mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
            @Override
            public void onClick(Node node, int position) {
                if(node.isLeaf()){
                    Toast.makeText(MainActivity.this, node.getName(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mTree.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this).setTitle("add node")
                        .setView(editText)
                        .setPositiveButton("sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(TextUtils.isEmpty(editText.getText().toString())){
                                    return;
                                }
                                mAdapter.addExtraNode(position, editText.getText().toString())
                                ;
                            }
                        })
                        .setNegativeButton("cancel", null).show();
                return true;
            }
        });
    }


    private void initDatas()
    {
        mDatas = new ArrayList<FileBean>();
        FileBean bean = new FileBean(1, 0, "根目录1");
        mDatas.add(bean);
        bean = new FileBean(2, 0, "根目录2");
        mDatas.add(bean);
        bean = new FileBean(3, 0, "根目录3");
        mDatas.add(bean);
        bean = new FileBean(4, 1, "根目录1-1");
        mDatas.add(bean);
        bean = new FileBean(5, 1, "根目录1-2");
        mDatas.add(bean);
        bean = new FileBean(6, 5, "根目录1-2-1");
        mDatas.add(bean);
        bean = new FileBean(7, 3, "根目录3-1");
        mDatas.add(bean);
        bean = new FileBean(8, 3, "根目录3-2");
        mDatas.add(bean);

        // initDatas
        mDatas2 = new ArrayList<OrgBean>();
        OrgBean bean2 = new OrgBean(1, 0, "根目录1");
        mDatas2.add(bean2);
        bean2 = new OrgBean(2, 0, "根目录2");
        mDatas2.add(bean2);
        bean2 = new OrgBean(3, 0, "根目录3");
        mDatas2.add(bean2);
        bean2 = new OrgBean(4, 1, "根目录1-1");
        mDatas2.add(bean2);
        bean2 = new OrgBean(5, 1, "根目录1-2");
        mDatas2.add(bean2);
        bean2 = new OrgBean(6, 5, "根目录1-2-1");
        mDatas2.add(bean2);
        bean2 = new OrgBean(7, 3, "根目录3-1");
        mDatas2.add(bean2);
        bean2 = new OrgBean(8, 3, "根目录3-2");
        mDatas2.add(bean2);

    }
}
