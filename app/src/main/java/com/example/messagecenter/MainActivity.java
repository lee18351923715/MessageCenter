package com.example.messagecenter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private String newsId;

    private Button addButton;
    private Button queryButton;
    private Button deleteButton;
    private Button updateButton;
    private Button queryAllButton;
    private Button deleteTechButton;

    private EditText titleText;
    private EditText messageText;
    private EditText typeText;
    private EditText idText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //===============================设置编辑功能====================================
//        final Button editButton = findViewById(R.id.edit_button);
//        final Button selectAllButton = findViewById(R.id.selectall_button);
//        final Button readedButton = findViewById(R.id.readed_button);
//        final Button deleteButton = findViewById(R.id.delete_main_button);
//        final CheckBox cb_button = findViewById(R.id.cb_button);
//        /**
//         * 点击编辑按钮
//         */
//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                editButton.setText("取消");
//                selectAllButton.setVisibility(View.VISIBLE);
//                readedButton.setVisibility(View.VISIBLE);
//                deleteButton.setVisibility(View.VISIBLE);
//                readedButton.setEnabled(false);
//                readedButton.setTextColor(Color.parseColor("#CCCCCC"));
//                deleteButton.setEnabled(false);
//                deleteButton.setTextColor(Color.parseColor("#CCCCCC"));
//                //cb_button.setChecked(false);
//                /**
//                 * 点击全选按钮
//                 */
//                selectAllButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        readedButton.setEnabled(true);
//                        readedButton.setTextColor(Color.parseColor("#F8F8FF"));
//                        deleteButton.setEnabled(true);
//                        deleteButton.setTextColor(Color.parseColor("#F8F8FF"));
//
//                    }
//                });
//            }
//        });

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
}
