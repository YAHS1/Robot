package com.yahs.king.robot;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.yahs.king.robot.bean.ChatMessage;
import com.yahs.king.robot.utils.NetUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TOTAL = "TOTAL";
    private final String MSG = "MSG";

    private ListView mMsgs;
    private ChatMessageAdapter mAdapter;
    private List<ChatMessage> mDatas;
    private SharedPreferences sp;

    private EditText mInputMsg;
    private Button mSendMsg;
    private Button bt_save;
    private Button bt_delete;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            // 等待接收，子线程完成数据的返回
            ChatMessage fromMessage = (ChatMessage) msg.obj;
            mDatas.add(fromMessage);
            mAdapter.notifyDataSetChanged();
            mMsgs.setSelection(mDatas.size()-1);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //隐藏标题
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        sp=getSharedPreferences("save",MODE_APPEND);

        initView();
        initDatas();
        // 初始化事件
        initListener();
        GetListData();



    }

    private void GetListData() {
        int lens= sp.getInt(TOTAL,0);
        for (int i=0;i<lens;i++){
            String msg = sp.getString(MSG + i, null);
            if (msg!=null){
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMsg(msg);
                mDatas.add(chatMessage);
            }
        }

    }

    //删除聊天记录
    private void DeleteData(){
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
    }


    //保存聊天记录
    private void SaveData(List<ChatMessage>list){
        for(int i =0;i<list.size();i++){
            sp.edit().putString(MSG+i, String.valueOf(mDatas.get(i).getMsg())).commit();
        }
        if (list.size()!=0){
            Toast.makeText(this, "聊天数据保存成功", Toast.LENGTH_SHORT).show();
        }
        sp.edit().putInt(TOTAL,list.size()).commit();
    }



    private void initListener()
    {

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveData(mDatas);
            }
        });

        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteData();
            }
        });
        mSendMsg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String toMsg = mInputMsg.getText().toString();
                if (TextUtils.isEmpty(toMsg))
                {
                    Toast.makeText(MainActivity.this, "发送消息不能为空！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                ChatMessage toMessage = new ChatMessage();
                toMessage.setDate(new Date());
                toMessage.setMsg(toMsg);
                toMessage.setType(ChatMessage.Type.OUTCOMING);
                mDatas.add(toMessage);
                mAdapter.notifyDataSetChanged();
                mMsgs.setSelection(mDatas.size()-1);

                mInputMsg.setText("");

                new Thread()
                {
                    public void run()
                    {
                        ChatMessage fromMessage = NetUtils.SendMessage(toMsg);
                        Message m = Message.obtain();
                        m.obj = fromMessage;
                        mHandler.sendMessage(m);
                    }
                }.start();

            }
        });
    }

    private void initDatas()
    {
        mDatas = new ArrayList<ChatMessage>();
        mDatas.add(new ChatMessage("您好，小瑞为您服务！", ChatMessage.Type.INCOMING));
        mAdapter = new ChatMessageAdapter(this, mDatas);
        mMsgs.setAdapter(mAdapter);
    }

    private void initView()
    {
        mMsgs = (ListView) findViewById(R.id.id_listview_msgs);
        mInputMsg = (EditText) findViewById(R.id.id_input_msg);
        mSendMsg = (Button) findViewById(R.id.id_send_msg);
        bt_save = (Button) findViewById(R.id.bt_save);
        bt_delete= (Button) findViewById(R.id.bt_delete);
    }
}
