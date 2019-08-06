package com.example.messagecenter;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    NewsContenterFragment newsContentFragment;
    NewsAdapter adapter;
    TextView mTvSelectNum;
    Fragment f1;
    Button cancel;//取消删除操作

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        adapter = new NewsAdapter(this, newsContentFragment, mList);
        RecyclerView newsTitleRecyclerView = findViewById(R.id.news_title_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        newsTitleRecyclerView.setLayoutManager(layoutManager);
        newsTitleRecyclerView.setAdapter(adapter);
        initListener();
//        Intent intent = new Intent("com.example.messagecenter.STARTSERVICE");
//        intent.setComponent(new ComponentName("com.example.messagecenter","com.example.messagecenter.StartService"));
//        sendBroadcast(intent);


        //===============================数据库功能======================================

//        titleText = findViewById(R.id.news_title);
//        messageText = findViewById(R.id.news_message);
//
//        typeText = findViewById(R.id.news_type);
//        idText = findViewById(R.id.news_id);
//
//
//        /**
//         * 开机时收到广播向数据库中添加一条记录
//         */
//        addButton = findViewById(R.id.add_button);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                ContentValues values1 = new ContentValues();
////                values1.put("title",titleText.getText().toString());
////                values1.put("message",messageText.getText().toString());
////                values1.put("flag", 0);
////                values1.put("time",new Date().toLocaleString());
////                values1.put("type",Integer.valueOf(typeText.getText().toString()));
////                Uri newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values1);
////                newsId = newUri.getPathSegments().get(1);
//                //Toast.makeText(MainActivity.this,"添加数据成功",Toast.LENGTH_SHORT).show();
//                //Log.d("时间",new Date().toLocaleString());
//                Uri newUri;
//                ContentValues values1 = new ContentValues();
//                values1.put("title","行程评分");
//                values1.put("message","本次行驶距离：xx公里；油耗：xx;急加速：xx次；急减速：xx次，急转弯：xx次。建议减速慢行，平稳行驶，可减少油耗，降低安全风险，祝您用车愉快！");
//                values1.put("flag", 0);
//                values1.put("time",new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()).toString());
//                values1.put("type",2);
//                newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values1);
//
//                ContentValues values2 = new ContentValues();
//                values2.put("title","车辆保养提醒");
//                values2.put("message","尊敬的用户，累计行驶公里数，达到保养里程，请联系上汽大通官方4S店预约保养，谢谢。");
//                values2.put("flag", 0);
//                values2.put("time",new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()).toString());
//                values2.put("type",3);
//                newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values2);
//
//                ContentValues values3 = new ContentValues();
//                values3.put("title","保养预约到期提醒");
//                values3.put("message","尊敬的用户，您的爱车，在年月日时间有一次维保服务预约。预约门店地址：xxx，联系电话xxx。");
//                values3.put("flag", 0);
//                values3.put("time",new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()).toString());
//                values3.put("type",4);
//                newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values3);
//
//                ContentValues values4 = new ContentValues();
//                values4.put("title","车检提醒");
//                values4.put("message","您的【车辆昵称】还有xx天要进行车检，请于x年x月x日前去完成车检，谢谢。");
//                values4.put("flag", 0);
//                values4.put("time",new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()).toString());
//                values4.put("type",6);
//                newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values4);
//
//                ContentValues values5 = new ContentValues();
//                values5.put("title","目的地推送");
//                values5.put("message","您收到来自xxx发送的目的地：上海市杨浦区军工路2500号。");
//                values5.put("flag", 0);
//                values5.put("time",new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()).toString());
//                values5.put("type",5);
//                newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values5);
//
//                ContentValues values6 = new ContentValues();
//                values6.put("title","行程提醒");
//                values6.put("message","15分钟后开车去公司。");
//                values6.put("flag", 0);
//                values6.put("time",new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()).toString());
//                values6.put("type",5);
//                newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values6);
//
//                ContentValues values7 = new ContentValues();
//                values7.put("title","低油量提醒");
//                values7.put("message","前油量偏低，点击前往附近加油站加油，保证车辆正常行驶");
//                values7.put("flag", 0);
//                values7.put("time",new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()).toString());
//                values7.put("type",5);
//                newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values7);
//
//                ContentValues values8 = new ContentValues();
//                values8.put("title","可续里程不足");
//                values8.put("message","您的车辆可续里程不足以到达目的地，请前往最近加油站加油。");
//                values8.put("flag", 0);
//                values8.put("time",new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()).toString());
//                values8.put("type",5);
//                newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values8);
//
//                ContentValues values9 = new ContentValues();
//                values9.put("title","天气提醒");
//                values9.put("message","明天有雨，请记得带伞。");
//                values9.put("flag", 0);
//                values9.put("time",new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()).toString());
//                values9.put("type",2);
//                newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values9);
//
//                ContentValues values10 = new ContentValues();
//                values10.put("title","促销活动");
//                values10.put("message","运营商提供的活动消息体");
//                values10.put("flag", 0);
//                values10.put("time",new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()).toString());
//                values10.put("type",4);
//                newUri = getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values10);
//
//            }
//        });
//
//        /**
//         * 查询所有消息的功能：开机时自动查询出数据库所有的消息，并填充到主页面中
//         */
//        queryAllButton = findViewById(R.id.queryall_button);
//        queryAllButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Cursor cursor = getContentResolver().query(MetaData.TableMetaData.CONTENT_URI,new String[]{ "id",MetaData.TableMetaData.FIELD_TITLE,MetaData.TableMetaData.FIELD_MESSAGE,
//                MetaData.TableMetaData.FIELD_FLAG,MetaData.TableMetaData.FIELD_TIME,MetaData.TableMetaData.FIELD_TYPE},null,null,null);
//                if(cursor == null){
//                    Toast.makeText(MainActivity.this,"当前没有数据",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                while (cursor.moveToNext()){
//                    int id = cursor.getInt(cursor.getColumnIndex("id"));
//                    String title = cursor.getString(cursor.getColumnIndex("title"));
//                    String message = cursor.getString(cursor.getColumnIndex("message"));
//                    int flag = cursor.getInt(cursor.getColumnIndex("flag"));
//                    String time = cursor.getString(cursor.getColumnIndex("time"));
//                    int type = cursor.getInt(cursor.getColumnIndex("type"));
//                    Log.d("数据:",id+","+title+","+message+","+flag+","+time+","+type);
//                }
//                cursor.close();
//            }
//        });
//
//        /**
//         * 删除单条消息的功能：主要用于左滑删除中来删除单条消息
//         */
//        deleteButton = findViewById(R.id.delete_button);
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Uri uri = Uri.parse("content://com.example.messagecenter.newsprovider/news/"+idText.getText().toString());
//                ContentResolver cr = getContentResolver();
//                //int delete = cr.delete(uri, "id>?", new String[]{"14"});
//                cr.delete(uri, null, null);
//                Toast.makeText(MainActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        /**
//         * 查询单条消息的功能
//         */
//        queryButton = findViewById(R.id.query_button);
//        queryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        /**
//         * 修改功能  实现：点击一条消息，后台就将信息状态更为已读（即 flag=1）
//         */
//        updateButton = findViewById(R.id.update_button);
//        updateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ContentValues values = new ContentValues();
//                values.put(MetaData.TableMetaData.FIELD_FLAG,0);
//                Uri uri = Uri.parse(MetaData.TableMetaData.CONTENT_URI.toString()+"/"+idText.getText().toString());
//                getContentResolver().update(uri,values,null,null);
//            }
//        });
//
//
//        /**
//         * 根据前台传来的id数组实现批量删除
//         */
//        deleteTechButton = findViewById(R.id.detech_delete_button);
//        deleteTechButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int[] arr = new int[]{15,16};
//                for (int i : arr) {
//                    Uri uri = Uri.parse("content://com.example.messagecenter.newsprovider/news/"+i);
//                    ContentResolver cr = getContentResolver();
//                    cr.delete(uri, null, null);
//                }
//            }
//        });
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
        mTvSelectNum =findViewById(R.id.tv_select_num);
        newsContentFragment = (NewsContenterFragment) getSupportFragmentManager().findFragmentById(R.id.news_content_fragment);
    }

    public void initListener(){
        editButton.setOnClickListener(this);
        selectAllButton.setOnClickListener(this);
        readedButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_button:
                updataEditMode();
                break;
            case R.id.selectall_button:
                adapter.setmEditMode(MYLIVE_MODE_EDIT);
                selectAllMain();
                break;
            case R.id.readed_button:
                Toast.makeText(this, "已读！", Toast.LENGTH_SHORT).show();
                readed();
                break;
            case R.id.delete_main_button:
                Toast.makeText(this, "删除成功！", Toast.LENGTH_SHORT).show();
                deleteVideo();
                break;
        }
    }


    /**
     * 从数据库中初始化模拟新闻数据
     * 将数据库的信息按倒序方式输出
     */
    private List<News> getNews() {
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
        }else {
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
        if(adapter == null){
            return;
        }
        if (!isSelectAll) {
            for(int i=0; i<adapter.getmNewsList().size(); i++){
                adapter.getmNewsList().get(i).setSelect(true);
            }
            index = adapter.getmNewsList().size();
            selectAllButton.setText("取消全选");
            isSelectAll = true;
        }else {
            for(int i=0; i<adapter.getmNewsList().size(); i++){
                adapter.getmNewsList().get(i).setSelect(false);
            }
            index = 0;
            selectAllButton.setText("全选");
            isSelectAll = false;
        }
        mTvSelectNum.setText(String.valueOf(index));
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
            readedButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            readedButton.setEnabled(false);
            readedButton.setTextColor(Color.parseColor("#CCCCCC"));
            deleteButton.setEnabled(false);
            deleteButton.setTextColor(Color.parseColor("#CCCCCC"));
            adapter.setmEditMode(MYLIVE_MODE_EDIT);
            editorStatus = true;
        }else {
            editButton.setText("编辑");
            selectAllButton.setVisibility(View.INVISIBLE);
            readedButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
            adapter.setmEditMode(MYLIVE_MODE_CHECK);
            editorStatus = false;
            index = 0;
            mTvSelectNum.setText(String.valueOf(index));
            if(newsContentFragment==null){
                replaceFragment(newsContentFragment);
            }
        }
        adapter.setmEditMode(mEditMode);
    }

    /**
     *删除逻辑
     */
    private void deleteVideo() {
        if (index == 0){
            deleteButton.setEnabled(false);
            return;
        }
        replaceFragment(f1);
//        System.out.println(f1.getView()==null?"f1.getView()为空":"f1.getView()不为空");
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("=================================取消");
//            }
//        });
    }

    public void  replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.test,fragment);
        transaction.commit();

    }

    @Override
    public void onItemClickListener(int pos, List<News> newsList) {
        if(editorStatus){
            News news = newsList.get(pos);
            boolean isSelect = news.isSelect();
            if(!isSelect){
                index++;
                news.setSelect(true);
                if(index == newsList.size()){
                    isSelectAll = true;
                    selectAllButton.setText("取消全选");
                }
            }else {
                news.setSelect(false);
                index--;
                isSelectAll = false;
                selectAllButton.setText("取消全选");
            }
        }
        mTvSelectNum.setText(String.valueOf(index));
        adapter.notifyDataSetChanged();
        setBtnBackground(index);

    }

    /**
     * 已读逻辑
     * 获取所有select=true的信息，将它们的flag更新为1
     */
    public void readed(){
//        for (News news : mList) {
//            if(news.isSelect()==true){
//                modify(news);
//            }
//        }
        for(int i=0; i<mList.size(); i++){
            if(mList.get(i).isSelect()){
                modify(mList.get(i));
                adapter.notifyItemChanged(i);
            }
        }
        editorStatus = false;
        updataEditMode();

    }

    public void modify(News news){
        ContentValues values = new ContentValues();
        values.put(MetaData.TableMetaData.FIELD_FLAG, 1);
        Uri uri = Uri.parse(MetaData.TableMetaData.CONTENT_URI.toString() + "/" + news.getId());
        getContentResolver().update(uri, values, null, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        f1 = new DeleteFragment();
    }


}
