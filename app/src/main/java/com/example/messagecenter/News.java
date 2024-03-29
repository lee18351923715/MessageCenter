package com.example.messagecenter;

/**
 * 消息类
 * id       消息id
 * title    消息标题
 * message  消息内容
 * flag     标记消息是否已读 0/1
 * time     消息时间
 * type     消息类型  2/3/4/5/6/7/8
 */
public class News {

    public static final int NOT_READED = 0;//未读
    public static final int IS_READED = 1;//已读
    public static final int NO_BUTTON = 2;//没有任何按钮
    public static final int MAINTTENANCE_BUTON = 3;//保养按钮
    public static final int DETAILS = 4;//查看详情
    public static final int NAVIGATION = 5;//导航
    public static final int VEHICLE_INSPECTION = 6;//车检


    public News() {
    }

    private int id;

    private String title;

    private String message;

    private int flag;

    private String time;

    private int type;

    private boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public News(int id, String title, String message, int flag, String time, int type, boolean select) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.flag = flag;
        this.time = time;
        this.type = type;
        this.select = select;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", flag=" + flag +
                ", time='" + time + '\'' +
                ", type=" + type +
                '}';
    }
}

