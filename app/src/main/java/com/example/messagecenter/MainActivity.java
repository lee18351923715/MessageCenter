package com.example.messagecenter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NewsAdapter.OnItemClickListener {

    private static final int MYLIVE_MODE_CHECK = 0;
    private static final int MYLIVE_MODE_EDIT = 1;

    private boolean isSelectAll = false;
    private boolean editorStatus = false;
    private int index = 0;
    private int mEditMode = MYLIVE_MODE_CHECK;

    private List<News> mList = new ArrayList<>();

    Button editButton;//编辑
    Button selectAllButton;//全选
    Button readedButton;//已读
    Button deleteButton;//删除
    Button refresh;//刷新
    TextView noMessage;
    NewsContenterFragment newsContentFragment;
    RecyclerView newsTitleRecyclerView;
    LinearLayoutManager layoutManager;

    NewsAdapter adapter;
    TextView mTvSelectNum;
    Fragment f1;//删除时候出现的页面布局
    int num = 0;//未读消息数量

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        if(mList == null){
            editButton.setVisibility(View.INVISIBLE);
            noMessage.setVisibility(View.VISIBLE);
            newsTitleRecyclerView.setVisibility(View.INVISIBLE);
        }else {
            editButton.setVisibility(View.VISIBLE);
            noMessage.setVisibility(View.INVISIBLE);
            //newsTitleRecyclerView.setVisibility(View.VISIBLE);
            adapter = new NewsAdapter(this, newsContentFragment, mList);
            newsTitleRecyclerView = findViewById(R.id.news_title_recycler_view);
            layoutManager = new LinearLayoutManager(this);
            newsTitleRecyclerView.setLayoutManager(layoutManager);
            newsTitleRecyclerView.setAdapter(adapter);
            mTvSelectNum.setText(String.valueOf(num));
            initListener();
        }
//        Intent intent = new Intent("com.example.messagecenter.STARTSERVICE");
//        intent.setComponent(new ComponentName("com.example.messagecenter","com.example.messagecenter.StartService"));
//        sendBroadcast(intent);
    }
    /**
     * 编辑等按钮功能实现=======================================================================================================
     */
    public void init() {
        mList = getNews();
        editButton = findViewById(R.id.edit_button);
        selectAllButton = findViewById(R.id.selectall_button);
        readedButton = findViewById(R.id.readed_button);
        deleteButton = findViewById(R.id.delete_main_button);
        refresh = findViewById(R.id.refresh_button);
        noMessage = findViewById(R.id.no_message);
        mTvSelectNum = findViewById(R.id.tv_select_num);
        newsContentFragment = (NewsContenterFragment) getSupportFragmentManager().findFragmentById(R.id.news_content_fragment);
    }

    public void initListener() {
        editButton.setOnClickListener(this);
        selectAllButton.setOnClickListener(this);
        readedButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        refresh.setOnClickListener(this);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_button:
                adapter.setmEditMode(MYLIVE_MODE_EDIT);
                updataEditMode();
                break;
            case R.id.selectall_button:
                adapter.setmEditMode(MYLIVE_MODE_EDIT);
                adapter.setmEditMode(MYLIVE_MODE_EDIT);
                selectAllMain();
                break;
            case R.id.readed_button:
                adapter.setmEditMode(MYLIVE_MODE_EDIT);
                readed();
                break;
            case R.id.delete_main_button:
                adapter.setmEditMode(mEditMode);
                deleteVideo();
                break;
            case R.id.refresh_button:
                refresh();
        }
    }

    /**
     * 从数据库中初始化模拟新闻数据
     * 将数据库的信息按倒序方式输出
     */
    private List<News> getNews() {
        num = 0;
        List<News> mNewsList = new ArrayList<>();
        Cursor cursor = this.getContentResolver().query(MetaData.TableMetaData.CONTENT_URI, new String[]{"id", MetaData.TableMetaData.FIELD_TITLE, MetaData.TableMetaData.FIELD_MESSAGE,
                MetaData.TableMetaData.FIELD_FLAG, MetaData.TableMetaData.FIELD_TIME, MetaData.TableMetaData.FIELD_TYPE}, null, null, null);
        if (cursor == null) {
            Toast.makeText(this, "当前没有数据", Toast.LENGTH_SHORT).show();
            return null;
        }
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String message = cursor.getString(cursor.getColumnIndex("message"));
            int flag = cursor.getInt(cursor.getColumnIndex("flag"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            News news = new News(id, title, message, flag, time, type, false);
            mNewsList.add(news);
            if (flag == 0) {
                num++;
            }
        }
        cursor.close();
        Collections.reverse(mNewsList);
        return mNewsList;
    }

    /**
     * 根据选择的数量是否为0来判断按钮的是否可点击.
     */
    private void setBtnBackground(int size) {
        if (size != 0) {
            readedButton.setEnabled(true);
            readedButton.setTextColor(Color.parseColor("#FFFFFF"));
            deleteButton.setEnabled(true);
            deleteButton.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            readedButton.setEnabled(false);
            readedButton.setTextColor(Color.parseColor("#CCCCCC"));
            deleteButton.setEnabled(false);
            deleteButton.setTextColor(Color.parseColor("#CCCCCC"));
        }
    }

    /**
     * 全选和反选
     */
    private void selectAllMain() {
        if (adapter == null) {
            return;
        }
        if (!isSelectAll) {
            for (int i = 0; i < adapter.getmNewsList().size(); i++) {
                adapter.getmNewsList().get(i).setSelect(true);
            }
            index = adapter.getmNewsList().size();
            selectAllButton.setText("取消全选");
            isSelectAll = true;
        } else {
            for (int i = 0; i < adapter.getmNewsList().size(); i++) {
                adapter.getmNewsList().get(i).setSelect(false);
            }
            index = 0;
            selectAllButton.setText("全选");
            isSelectAll = false;
        }
        adapter.notifyDataSetChanged();
        setBtnBackground(index);

    }

    /**
     * 编辑逻辑
     */
    private void updataEditMode() {
        mEditMode = mEditMode == MYLIVE_MODE_CHECK ? MYLIVE_MODE_EDIT : MYLIVE_MODE_CHECK;
        if (mEditMode == MYLIVE_MODE_EDIT) {

            editButton.setText("取消");
            selectAllButton.setVisibility(View.VISIBLE);
            selectAllButton.setText("全选");
            readedButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            readedButton.setEnabled(false);
            readedButton.setTextColor(Color.parseColor("#CCCCCC"));
            deleteButton.setEnabled(false);
            deleteButton.setTextColor(Color.parseColor("#CCCCCC"));
            adapter.setmEditMode(MYLIVE_MODE_EDIT);
            editorStatus = true;
        } else {
            editButton.setText("编辑");
            selectAllButton.setVisibility(View.INVISIBLE);
            readedButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
            adapter.setmEditMode(MYLIVE_MODE_CHECK);
            editorStatus = false;
            index = 0;
            newsContentFragment.refresh("","",2);
            editButton.setVisibility(View.VISIBLE);
//            updataEditMode();
            editorStatus = false;

            for (int i = 0; i < adapter.getmNewsList().size(); i++) {
                adapter.getmNewsList().get(i).setSelect(false);
            }
            isSelectAll = false;
            adapter.notifyDataSetChanged();
        }
        adapter.setmEditMode(mEditMode);
    }

    /**
     * 删除逻辑
     */
    private void deleteVideo() {
        if (index == 0) {
            deleteButton.setEnabled(false);
            return;
        }
        selectAllButton.setVisibility(View.INVISIBLE);
        readedButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);
        editButton.setVisibility(View.INVISIBLE);
        //replaceFragment(f1);
        View view = newsContentFragment.getView();
        newsContentFragment.refresh("","您是否确认删除？",6);
        Button sure = view.findViewById(R.id.left_button);
        Button cancel = view.findViewById(R.id.right_button);
        sure.setText("确认删除");
        cancel.setText("取消");
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).isSelect()) {
                        delete(mList.get(i));
                        adapter.notifyDataSetChanged();
                        adapter.notifyItemChanged(i);
                        adapter.setmEditMode(MYLIVE_MODE_CHECK);
                        num--;
                    }
                }
                mList = getNews();
                adapter = new NewsAdapter(MainActivity.this, newsContentFragment, mList);
                newsTitleRecyclerView.setAdapter(adapter);
                mTvSelectNum.setText(String.valueOf(num));
                editorStatus = false;
                newsContentFragment.refresh("","",2);
                editButton.setVisibility(View.VISIBLE);
                updataEditMode();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //adapter = new NewsAdapter(MainActivity.this, newsContentFragment, getNews());
                //newsTitleRecyclerView.setAdapter(adapter);
                //mTvSelectNum.setText(String.valueOf(num));
                editorStatus = false;
                newsContentFragment.refresh("","",2);
                editButton.setVisibility(View.VISIBLE);
                updataEditMode();
            }
        });
    }

    @Override
    public void onItemClickListener(int pos, List<News> newsList) {
        if (editorStatus) {
            News news = newsList.get(pos);
            boolean isSelect = news.isSelect();
            if (!isSelect) {
                index++;
                news.setSelect(true);
                if (index == newsList.size()) {
                    isSelectAll = true;
                    selectAllButton.setText("取消全选");
                }
            } else {
                news.setSelect(false);
                index--;
                isSelectAll = false;
                selectAllButton.setText("取消全选");
            }
        }
        //mTvSelectNum.setText(String.valueOf(index));
        adapter.notifyDataSetChanged();
        setBtnBackground(index);

    }

    /**
     * 已读逻辑
     * 获取所有select=true的信息，将它们的flag更新为1
     */
    public void readed() {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).isSelect()) {
                Log.d("选中的id",i+"");
                modify(mList.get(i));
//                adapter.notifyDataSetChanged();
//                adapter.notifyItemChanged(i);
//                adapter.setmEditMode(MYLIVE_MODE_CHECK);
                num--;
            }
        }
        mList = getNews();
        adapter = new NewsAdapter(this, newsContentFragment, mList);
        newsTitleRecyclerView.setAdapter(adapter);
        mTvSelectNum.setText(String.valueOf(num));
        editorStatus = false;
        editButton.setText("编辑");
        selectAllButton.setVisibility(View.INVISIBLE);
        readedButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);
        adapter.setmEditMode(MYLIVE_MODE_CHECK);
    }

    public void modify(News news) {
        ContentValues values = new ContentValues();
        values.put(MetaData.TableMetaData.FIELD_FLAG, 1);
        Uri uri = Uri.parse(MetaData.TableMetaData.CONTENT_URI.toString() + "/" + news.getId());
        getContentResolver().update(uri, values, null, null);
    }

    public void delete(News news){
        Uri uri = Uri.parse("content://com.example.messagecenter.newsprovider/news/"+news.getId());
        ContentResolver cr = getContentResolver();
        //int delete = cr.delete(uri, "id>?", new String[]{"14"});
        cr.delete(uri, null, null);
    }

    public void refresh(){
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri newUri;
                /**
                 * 点击刷新随机发送一条信息
                 */
                int choice = (int)(Math.random()*10+1);
                switch (choice){
                    case 1:
                        ContentValues values1 = Utils.set("行程评分",
                                "本次行驶距离：xx公里；油耗：xx;急加速：xx次；急减速：xx次，急转弯：xx次。建议减速慢行，平稳行驶，可减少油耗，降低安全风险，祝您用车愉快！",
                                0,2);
                        newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values1);
                        break;
                    case 2:
                        ContentValues values2 = Utils.set("车辆保养提醒",
                                "尊敬的用户，累计行驶公里数，达到保养里程，请联系上汽大通官方4S店预约保养，谢谢。",0,3);
                        newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values2);
                        break;
                    case 3:
                        ContentValues values3 = Utils.set("保养预约到期提醒",
                                "尊敬的用户，您的爱车，在年月日时间有一次维保服务预约。预约门店地址：xxx，联系电话xxx。",0,4 );
                        newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values3);
                        break;
                    case 4:
                        ContentValues values4 = Utils.set("车检提醒","您的【车辆昵称】还有xx天要进行车检，请于x年x月x日前去完成车检，谢谢。",0,6);
                        newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values4);
                        break;
                    case 5:
                        ContentValues values5 = Utils.set("目的地推送","您收到来自xxx发送的目的地：上海市杨浦区军工路2500号。",0,5);
                        newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values5);
                        break;
                    case 6:
                        ContentValues values6 = Utils.set("行程提醒","15分钟后开车去公司。",0,5);
                        newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values6);
                        break;
                    case 7:
                        ContentValues values7 = Utils.set("低油量提醒","前油量偏低，点击前往附近加油站加油，保证车辆正常行驶",0,5);
                        newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values7);
                        break;
                    case 8:
                        ContentValues values8 = Utils.set("可续里程不足","您的车辆可续里程不足以到达目的地，请前往最近加油站加油。",
                                0,5);
                        newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values8);
                        break;
                    case 9:
                        ContentValues values9 = Utils.set("天气提醒","明天有雨，请记得带伞。",0,2);
                        newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values9);
                        break;
                    case 10:
                        ContentValues values10 = Utils.set("促销活动","运营商提供的活动消息体", 0, 4);
                        newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values10);
                        break;
                }
                Toast.makeText(MainActivity.this,"您收到一条消息，请尽快查收！",Toast.LENGTH_SHORT).show();
                mList = getNews();
                adapter = new NewsAdapter(MainActivity.this, newsContentFragment, mList);
                newsTitleRecyclerView.setAdapter(adapter);
                mTvSelectNum.setText(String.valueOf(num));
                editorStatus = false;
            }
        });
    }
}
