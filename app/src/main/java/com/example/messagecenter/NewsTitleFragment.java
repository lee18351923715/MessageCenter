package com.example.messagecenter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsTitleFragment extends Fragment implements View.OnClickListener {

    Button editButton;//编辑
    Button selectAllButton;//全选
    Button readedButton;//已读
    Button deleteButton;//删除
    CheckBox cb_button;//复选框
    View view = null;
    NewsContenterFragment newsContenterFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_title_frag,container,false);
        RecyclerView newsTitleRecyclerView = view.findViewById(R.id.news_title_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        init();
        newsContenterFragment=new NewsContenterFragment();
        newsTitleRecyclerView.setLayoutManager(layoutManager);
        NewsAdapter adapter = new NewsAdapter(getNews());
        newsTitleRecyclerView.setAdapter(adapter);
        return view;
    }

    /**
     * 编辑等按钮功能实现
     */
    public void init(){
        editButton = view.findViewById(R.id.edit_button);
        selectAllButton = view.findViewById(R.id.selectall_button);
        readedButton = view.findViewById(R.id.readed_button);
        deleteButton = view.findViewById(R.id.delete_main_button);
        cb_button = view.findViewById(R.id.cb_button);
        editButton.setOnClickListener(this);
        selectAllButton.setOnClickListener(this);
        readedButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_button:
                if(editButton.getText().equals("编辑")) {
                    editButton.setText("取消");
                    selectAllButton.setVisibility(View.VISIBLE);
                    readedButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                    readedButton.setEnabled(false);
                    readedButton.setTextColor(Color.parseColor("#CCCCCC"));
                    deleteButton.setEnabled(false);
                    deleteButton.setTextColor(Color.parseColor("#CCCCCC"));
                } else {
                    editButton.setText("编辑");
                    selectAllButton.setVisibility(View.INVISIBLE);
                    readedButton.setVisibility(View.INVISIBLE);
                    deleteButton.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.selectall_button:
                if(selectAllButton.getText().equals("全选")) {
                    selectAllButton.setText("取消全选");
                    readedButton.setEnabled(true);
                    readedButton.setTextColor(Color.parseColor("#FFFFFF"));
                    deleteButton.setEnabled(true);
                    deleteButton.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    selectAllButton.setText("全选");
                    readedButton.setEnabled(false);
                    readedButton.setTextColor(Color.parseColor("#CCCCCC"));
                    deleteButton.setEnabled(false);
                    deleteButton.setTextColor(Color.parseColor("#CCCCCC"));
                }
                break;
            case R.id.readed_button:
                Toast.makeText(getActivity(),"已读！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete_main_button:
                Toast.makeText(getActivity(),"删除成功！",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    /**
     * 从数据库中初始化模拟新闻数据
     * 将数据库的信息按倒序方式输出
     */
    private List<News> getNews() {
        List<News> mNewsList = new ArrayList<>();
        Cursor cursor = getActivity().getContentResolver().query(MetaData.TableMetaData.CONTENT_URI,new String[]{ "id",MetaData.TableMetaData.FIELD_TITLE,MetaData.TableMetaData.FIELD_MESSAGE,
               MetaData.TableMetaData.FIELD_FLAG,MetaData.TableMetaData.FIELD_TIME,MetaData.TableMetaData.FIELD_TYPE},null,null,null);
        if(cursor == null){
            Toast.makeText(getActivity(),"当前没有数据",Toast.LENGTH_SHORT).show();
            return null;
        }
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String message = cursor.getString(cursor.getColumnIndex("message"));
            int flag = cursor.getInt(cursor.getColumnIndex("flag"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            News news = new News(id,title,message,flag,time,type);
            mNewsList.add(news);
        }
        cursor.close();
        Collections.reverse(mNewsList);
        return mNewsList;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 新建一个NewsAdapter作为RecyclerView的适配器
     */
    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
        private Map<Integer, Integer> visibleMap = new HashMap<>();//用来记录是否显示复选框

        /**
         * 数据源
         */
        private List<News> mNewsList;

        private Map<Integer, Boolean> checkStatus = new HashMap<>();//用来记录所有checkbox的状态

        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView newsTitleText;
            private CheckBox titleCheckBox;
            private TextView newsMessage;
            private TextView newsTime;

            public ViewHolder(View view){
                super(view);
                newsTitleText = view.findViewById(R.id.news_title);
                titleCheckBox = view.findViewById(R.id.cb_button);
                newsMessage = view.findViewById(R.id.news_content);
                newsTime = view.findViewById(R.id.news_date);
            }
        }

        public NewsAdapter(List<News> newsList){
            mNewsList = newsList;
            Cursor cursor = getActivity().getContentResolver().query(MetaData.TableMetaData.CONTENT_URI,new String[]{ "id",
                    MetaData.TableMetaData.FIELD_FLAG,MetaData.TableMetaData.FIELD_TYPE},null,null,null);
            int i=0;
            while (cursor.moveToNext()){
                int flag = cursor.getInt(cursor.getColumnIndex("flag"));//获取是否已读
                if(flag == 0){
                    checkStatus.put(i,true);
                    visibleMap.put(i,View.VISIBLE);
                    i++;
                }else if(flag == 1){
                    checkStatus.put(i,false);
                    visibleMap.put(i,View.INVISIBLE);
                    i++;
                }
            }
            cursor.close();
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
            final ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox titleCheckBox = view.findViewById(R.id.cb_button);
                    News news = mNewsList.get(holder.getAdapterPosition());
                    if(news.getFlag() == 0){
                        titleCheckBox.setChecked(false);
                    }else {
                        titleCheckBox.setVisibility(View.INVISIBLE);
                    }
                    NewsContenterFragment newsContentFragment = (NewsContenterFragment)getFragmentManager().findFragmentById(R.id.news_content_fragment);
                    newsContentFragment.refresh(news.getTitle(),news.getMessage(), news.getType());
//                    if(news.getFlag()!=1){
//                        ContentValues values = new ContentValues();
//                        values.put(MetaData.TableMetaData.FIELD_FLAG,1);
//                        Uri uri = Uri.parse(MetaData.TableMetaData.CONTENT_URI.toString()+"/"+(mNewsList.size()-news.getId()));
//                        getActivity().getContentResolver().update(uri,values,null,null);
//                        Toast.makeText(getContext(),news.getId()+"阅读完毕",Toast.LENGTH_SHORT).show();
//                    }

                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            final News news = mNewsList.get(position);

            holder.newsTitleText.setText(news.getTitle());
            holder.newsMessage.setText(news.getMessage());
            holder.newsTime.setText(news.getTime());
            //=======================解决checkBox混乱问题==================================================
            holder.titleCheckBox.setOnCheckedChangeListener(null);//清掉监听器
            holder.titleCheckBox.setChecked(checkStatus.get(position));//设置选中状态
            holder.titleCheckBox.setVisibility(visibleMap.get(position));

            holder.titleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//再设置监听器
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 checkStatus.put(position, isChecked);
                 if(isChecked){
                     visibleMap.put(position,View.VISIBLE);
                 }else {
                     visibleMap.put(position,View.INVISIBLE);
                     holder.titleCheckBox.setVisibility(View.INVISIBLE);
                 }

             }
         });
            // 设置CheckBox的状态
            if (checkStatus.get(position) == null) {
                checkStatus.put(position, false);
            }
            holder.titleCheckBox.setChecked(checkStatus.get(position));
            holder.titleCheckBox.setVisibility(visibleMap.get(position));
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }

    }

}
