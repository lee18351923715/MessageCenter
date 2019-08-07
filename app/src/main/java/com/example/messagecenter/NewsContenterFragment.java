package com.example.messagecenter;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NewsContenterFragment extends Fragment {

    private View view;
    private Button refresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_content_frag,container,false);
        return view;
    }

    public void refresh(String newsTitle, String newsContent, int type){
        View visibilityLayout = view.findViewById(R.id.visibility_layout);
        visibilityLayout.setVisibility(View.VISIBLE);
        TextView newsTitleText = view.findViewById(R.id.news_title);
        TextView newsContentText = view.findViewById(R.id.news_content);
        Button leftButton = view.findViewById(R.id.left_button);
        Button rightButton = view.findViewById(R.id.right_button);
        newsTitleText.setText(newsTitle);//刷新新闻标题
        newsContentText.setText(newsContent);//刷新新闻内容
        switch(type){
            case 2:
                rightButton.setVisibility(View.INVISIBLE);
                leftButton.setVisibility(View.INVISIBLE);
                break;
            case 5:
                leftButton.setText("导航");
                leftButton.setVisibility(View.VISIBLE);
                rightButton.setVisibility(View.INVISIBLE);
                leftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(),"导航成功！!",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 6:
                leftButton.setText("我已车检");
                rightButton.setText("查看详情");
                leftButton.setVisibility(View.VISIBLE);
                rightButton.setVisibility(View.VISIBLE);
                leftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(),"成功车检！！",Toast.LENGTH_SHORT).show();
                    }
                });
                rightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(),"查看详情",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 3:
                leftButton.setText("前去保养");
                leftButton.setVisibility(View.VISIBLE);
                rightButton.setVisibility(View.INVISIBLE);
                leftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(),"保养成功！！",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 4:
                leftButton.setText("查看详情");
                leftButton.setVisibility(View.VISIBLE);
                rightButton.setVisibility(View.INVISIBLE);
                leftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(),"查看详情！！",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
