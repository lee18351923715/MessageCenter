package com.example.messagecenter;

import android.app.AlertDialog;
import android.content.ContentValues;
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
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class NewsContenterFragment extends Fragment {

    private View view;
    private Button refresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_content_frag,container,false);

        refresh = view.findViewById(R.id.refresh_button);

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
                        newUri = getActivity().getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values1);
                        break;
                    case 2:
                        ContentValues values2 = Utils.set("车辆保养提醒",
                                "尊敬的用户，累计行驶公里数，达到保养里程，请联系上汽大通官方4S店预约保养，谢谢。",0,3);
                        newUri = getActivity().getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values2);
                        break;
                    case 3:
                        ContentValues values3 = Utils.set("保养预约到期提醒",
                                "尊敬的用户，您的爱车，在年月日时间有一次维保服务预约。预约门店地址：xxx，联系电话xxx。",0,4 );
                        newUri = getActivity().getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values3);
                        break;
                    case 4:
                        ContentValues values4 = Utils.set("车检提醒","您的【车辆昵称】还有xx天要进行车检，请于x年x月x日前去完成车检，谢谢。",0,6);
                        newUri = getActivity().getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values4);
                        break;
                    case 5:
                        ContentValues values5 = Utils.set("目的地推送","您收到来自xxx发送的目的地：上海市杨浦区军工路2500号。",0,5);
                        newUri = getActivity().getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values5);
                        break;
                    case 6:
                        ContentValues values6 = Utils.set("行程提醒","15分钟后开车去公司。",0,5);
                        newUri = getActivity().getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values6);
                        break;
                    case 7:
                        ContentValues values7 = Utils.set("低油量提醒","前油量偏低，点击前往附近加油站加油，保证车辆正常行驶",0,5);
                        newUri = getActivity().getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values7);
                        break;
                    case 8:
                        ContentValues values8 = Utils.set("可续里程不足","您的车辆可续里程不足以到达目的地，请前往最近加油站加油。",
                                0,5);
                        newUri = getActivity().getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values8);
                        break;
                    case 9:
                        ContentValues values9 = Utils.set("天气提醒","明天有雨，请记得带伞。",0,2);
                        newUri = getActivity().getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values9);
                        break;
                    case 10:
                        ContentValues values10 = Utils.set("促销活动","运营商提供的活动消息体", 0, 4);
                        newUri = getActivity().getContentResolver().insert(MetaData.TableMetaData.CONTENT_URI,values10);
                        break;
                }
                Toast.makeText(getContext(),"您收到一条消息，请尽快查收！",Toast.LENGTH_SHORT).show();
                FragmentManager contentFragmentManager = getFragmentManager();

            }
        });
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

//    public void setRefreshBtnListenrt(View.OnClickListener onClickListener){
//        refresh.setOnClickListener(onClickListener);
//    }

}
