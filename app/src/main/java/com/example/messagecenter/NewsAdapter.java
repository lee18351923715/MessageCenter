package com.example.messagecenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> mNewsList;

    private Map<Integer, Boolean> checkStatus = new HashMap<>();//用来记录所有checkbox的状态

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView newsTitleText;
        CheckBox titleCheckBox;
        TextView newsContent;
        TextView newsDate;

        public TextView tv_Delete;

        public LinearLayout layout_content;

        public ViewHolder(View view){
            super(view);
            newsTitleText = view.findViewById(R.id.news_title);
            titleCheckBox = view.findViewById(R.id.cb_button);
            newsContent = view.findViewById(R.id.news_content);
            newsDate = view.findViewById(R.id.news_date);
        }
    }

    public NewsAdapter(List<News> newsList){
        mNewsList = newsList;
        for(int i=0; i<=50; i++){
            //checkStatus.put(i,getNews().get(i).isReaded());
            if(i%2 == 0){
                checkStatus.put(i,false);
            }else {
                checkStatus.put(i,true);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        News news = mNewsList.get(position);
        holder.newsTitleText.setText(news.getTitle());
        holder.newsContent.setText(news.getMessage());
        holder.newsDate.setText(news.getTime().toString());
        //holder.layout_content.getLayoutParams().width = Utils.getScreenWidth(mContext);

        holder.titleCheckBox.setOnCheckedChangeListener(null);//清掉监听器
        holder.titleCheckBox.setChecked(checkStatus.get(position));//设置选中状态
        holder.titleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//再设置监听器
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkStatus.put(position, isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }
}
