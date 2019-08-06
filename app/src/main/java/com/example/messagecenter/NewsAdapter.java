package com.example.messagecenter;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;

    private static final int MYLIVE_MODE_CHECK = 0;
    int mEditMode = MYLIVE_MODE_CHECK;

    public void setmEditMode(int mEditMode) {
        this.mEditMode = mEditMode;
        notifyDataSetChanged();
    }

    NewsContenterFragment newsContentFragment;

    private Context mContext;

    private Map<Integer, Integer> visibleMap = new HashMap<>();//用来记录是否显示阅读复选框

    /**
     * 数据源
     */
    private List<News> mNewsList = new ArrayList<>();

    public List<News> getmNewsList() {
        return mNewsList;
    }



    private Map<Integer, Boolean> checkStatus = new HashMap<>();//用来记录所有checkbox的状态
    private Map<Integer, Boolean> checkStatus_select = new HashMap<>();//用来记录所有选中的状态
    public Map<Integer, Boolean> getCheckStatus_select(){
        return checkStatus_select;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView newsTitleText;
        private CheckBox titleCheckBox;
        private TextView newsMessage;
        private TextView newsTime;
        private CheckBox selectCheckBox;

        public ViewHolder(View view) {
            super(view);
            newsTitleText = view.findViewById(R.id.news_title);
            titleCheckBox = view.findViewById(R.id.cb_button);
            newsMessage = view.findViewById(R.id.news_content);
            newsTime = view.findViewById(R.id.news_date);
            selectCheckBox = view.findViewById(R.id.select_button);
        }
    }


    public NewsAdapter(Context context, NewsContenterFragment fragment, List<News> list) {
        mContext = context;
        newsContentFragment = fragment;
        mNewsList = list;
        Cursor cursor = mContext.getContentResolver().query(MetaData.TableMetaData.CONTENT_URI, new String[]{"id",
                MetaData.TableMetaData.FIELD_FLAG, MetaData.TableMetaData.FIELD_TYPE}, null, null, null);
        while (cursor.moveToNext()) {
            int flag = cursor.getInt(cursor.getColumnIndex("flag"));//获取是否已读
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            if (flag == 0) {
                checkStatus.put(id, true);
                visibleMap.put(id, View.VISIBLE);
            } else if (flag == 1) {
                checkStatus.put(id, false);
                visibleMap.put(id, View.INVISIBLE);
            }
//            checkStatus_select.put(id,false);
        }
        cursor.close();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        CheckBox selectCheckedBox = view.findViewById(R.id.select_button);

        selectCheckedBox.setVisibility(View.INVISIBLE);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final News news = mNewsList.get(position);
        final int id = news.getId();
        checkStatus_select.put(id,news.isSelect());
        holder.newsTitleText.setText(news.getTitle());
        holder.newsMessage.setText(news.getMessage());
        holder.newsTime.setText(news.getTime());
        if (mEditMode == MYLIVE_MODE_CHECK) {//非编辑状态
            //=======================解决checkBox混乱问题==================================================
            holder.titleCheckBox.setOnCheckedChangeListener(null);//清掉监听器
            holder.titleCheckBox.setChecked(checkStatus.get(id));//设置选中状态

            holder.selectCheckBox.setVisibility(View.INVISIBLE);

            holder.titleCheckBox.setVisibility(visibleMap.get(id));
            holder.titleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//再设置监听器
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkStatus.put(id, isChecked);
                    if (isChecked) {
                        visibleMap.put(id, View.VISIBLE);
                    } else {
                        visibleMap.put(id, View.INVISIBLE);
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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox titleCheckBox = v.findViewById(R.id.cb_button);
                    News news = mNewsList.get(holder.getAdapterPosition());
                    checkStatus.put(news.getId(), true);
                    visibleMap.put(news.getId(), View.INVISIBLE);
                    if (news.getFlag() == 0) {
                        titleCheckBox.setChecked(false);
                    } else {
                        titleCheckBox.setVisibility(View.INVISIBLE);
                    }
                    newsContentFragment.refresh(news.getTitle(), news.getMessage(), news.getType());
                    if (news.getFlag() != 1) {
                        ContentValues values = new ContentValues();
                        values.put(MetaData.TableMetaData.FIELD_FLAG, 1);
                        Uri uri = Uri.parse(MetaData.TableMetaData.CONTENT_URI.toString() + "/" + news.getId());
                        mContext.getContentResolver().update(uri, values, null, null);
                    }
                }
            });
        } else {//编辑状态
            //=======================解决checkBox混乱问题==================================================
            holder.selectCheckBox.setOnCheckedChangeListener(null);//清掉监听器
            holder.selectCheckBox.setChecked(checkStatus_select.get(id));//设置选中状态

            holder.selectCheckBox.setVisibility(View.VISIBLE);
            holder.titleCheckBox.setVisibility(View.INVISIBLE);

            holder.selectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//再设置监听器
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkStatus_select.put(id,isChecked);
                    mOnItemClickListener.onItemClickListener(holder.getAdapterPosition(), mNewsList);
                }
            });
            if(checkStatus_select.get(id) == null){
                checkStatus_select.put(id,false);
            }
            holder.selectCheckBox.setChecked(checkStatus_select.get(id));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClickListener(holder.getAdapterPosition(), mNewsList);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClickListener(int pos,List<News> newsList);
    }
}
