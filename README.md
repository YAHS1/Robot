##一、实现了这些功能
###1.基本的聊天功能
![](https://github.com/YAHS1/Robot/blob/master/chat.gif?raw=true)
###2.删除聊天记录
![](https://github.com/YAHS1/Robot/blob/master/delete.gif?raw=true)
###2.保存聊天记录
![](https://github.com/YAHS1/Robot/blob/master/SAVE.gif?raw=true)
##二、用到的东西
###1.使用了POST方法请求网络
###2.使用了listview展现消息
###3.使用了Handler、runnable、 Thread请求网络与UI操作
```java
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
    ......
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
   
```
###4.使用了 SharedPreference保存与删除数据
####4.1删除数据
```java
private void DeleteData(){
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
    }
```
####4.2保存数据
```java
private void SaveData(List<ChatMessage>list){
        for(int i =0;i<list.size();i++){
            sp.edit().putString(MSG+i, String.valueOf(mDatas.get(i).getMsg())).commit();
        }
        if (list.size()!=0){
            Toast.makeText(this, "聊天数据保存成功", Toast.LENGTH_SHORT).show();
        }
        sp.edit().putInt(TOTAL,list.size()).commit();
    }
