package com.example.messagecenter;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    NewsContenterFragment newsContentFragment;

    private Context mContext;

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

    public NewsAdapter(Context context, NewsContenterFragment fragment){
        mContext = context;
        newsContentFragment = fragment;
        mNewsList = getNews();
        Cursor cursor = mContext.getContentResolver().query(MetaData.TableMetaData.CONTENT_URI,new String[]{ "id",
                MetaData.TableMetaData.FIELD_FLAG,MetaData.TableMetaData.FIELD_TYPE},null,null,null);
        while (cursor.moveToNext()){
            int flag = cursor.getInt(cursor.getColumnIndex("flag"));//获取是否已读
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            if(flag == 0){
                checkStatus.put(id,true);
                visibleMap.put(id,View.VISIBLE);
            }else if(flag == 1){
                checkStatus.put(id,false);
                visibleMap.put(id, View.INVISIBLE);
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
                checkStatus.put(news.getId(),false);
                visibleMap.put(news.getId(),View.INVISIBLE);
                if(news.getFlag() == 0){
                    titleCheckBox.setChecked(false);
                }else {
                    titleCheckBox.setVisibility(View.INVISIBLE);
                }
               newsContentFragment.refresh(news.getTitle(),news.getMessage(), news.getType());
                if(news.getFlag()!=1){
                    ContentValues values = new ContentValues();
                    values.put(MetaData.TableMetaData.FIELD_FLAG,1);
                    Uri uri = Uri.parse(MetaData.TableMetaData.CONTENT_URI.toString()+"/"+news.getId());
                    mContext.getContentResolver().update(uri,values,null,null);
                }

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final News news = mNewsList.get(position);
        final int id = news.getId();
        holder.newsTitleText.setText(news.getTitle());
        holder.newsMessage.setText(news.getMessage());
        holder.newsTime.setText(news.getTime());
        //=======================解决checkBox混乱问题==================================================
        holder.titleCheckBox.setOnCheckedChangeListener(null);//清掉监听器
        holder.titleCheckBox.setChecked(checkStatus.get(id));//设置选中状态
        holder.titleCheckBox.setVisibility(visibleMap.get(id));

        holder.titleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//再设置监听器
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkStatus.put(id, isChecked);
                if(isChecked){
                    visibleMap.put(id,View.VISIBLE);
                }else {
                    visibleMap.put(id,View.INVISIBLE);
                    holder.titleCheckBox.setVisibility(View.INVISIBLE);
                }

            }
        });
        // 设置CheckBox的状态
        if (checkStatus.get(id) == null) {
            checkStatus.put(id, false);
        }
        holder.titleCheckBox.setChecked(checkStatus.get(id));
        holder.titleCheckBox.setVisibility(visibleMap.get(id));

    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }
    /**
     * 从数据库中初始化模拟新闻数据
     * 将数据库的信息按倒序方式输出
     */
    private List<News> getNews() {
        List<News> mNewsList = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(MetaData.TableMetaData.CONTENT_URI,new String[]{ "id",MetaData.TableMetaData.FIELD_TITLE,MetaData.TableMetaData.FIELD_MESSAGE,
                MetaData.TableMetaData.FIELD_FLAG,MetaData.TableMetaData.FIELD_TIME,MetaData.TableMetaData.FIELD_TYPE},null,null,null);
        if(cursor == null){
            Toast.makeText(mContext,"当前没有数据",Toast.LENGTH_SHORT).show();
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





}
