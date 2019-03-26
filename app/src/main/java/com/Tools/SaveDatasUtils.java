package com.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import static android.content.Context.MODE_PRIVATE;

/**
 * desc:
 *
 * @author:Stone date:2015/12/18
 * time: 01:10
 * e-mail：962570483@qq.com
 */

public class SaveDatasUtils {

    private Context context;
    private String fileName;

    public SaveDatasUtils(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    /**
     * 根据key和预期的value类型获取value的值
     *
     * @param key
     * @param clazz
     * @return
     */
    public <T> T getValue(String key, Class<T> clazz) {
        if (context == null) {
            throw new RuntimeException("请先调用带有context，name参数的构造！");
        }
        SharedPreferences sp = this.context.getSharedPreferences(this.fileName, MODE_PRIVATE);
        return getValue(key, clazz, sp);
    }

    /**
     * 针对复杂类型存储<对象>
     *
     * @param key
     * @param object
     */
    public void setObject(String key, Object object) {
        SharedPreferences sp = this.context.getSharedPreferences(this.fileName, MODE_PRIVATE);

        //创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //创建字节对象输出流
        ObjectOutputStream out = null;
        try {
            //然后通过将字对象进行64转码，写入key值为key的sp中
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, objectVal);
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(String key) {
        SharedPreferences sp = this.context.getSharedPreferences(this.fileName, MODE_PRIVATE);
        if (sp.contains(key)) {
            String objectVal = sp.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            //一样通过读取字节流，创建字节流输入流，写入对象并作强制转换
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 对于外部不可见的过渡方法
     *
     * @param key
     * @param clazz
     * @param sp
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> T getValue(String key, Class<T> clazz, SharedPreferences sp) {
        T t;
        try {

            t = clazz.newInstance();

            if (t instanceof Integer) {
                return (T) Integer.valueOf(sp.getInt(key, 0));
            } else if (t instanceof String) {
                return (T) sp.getString(key, "");
            } else if (t instanceof Boolean) {
                return (T) Boolean.valueOf(sp.getBoolean(key, false));
            } else if (t instanceof Long) {
                return (T) Long.valueOf(sp.getLong(key, 0L));
            } else if (t instanceof Float) {
                return (T) Float.valueOf(sp.getFloat(key, 0L));
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
            Log.e("system", "类型输入错误或者复杂类型无法解析[" + e.getMessage() + "]");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e("system", "类型输入错误或者复杂类型无法解析[" + e.getMessage() + "]");
        }
        Log.e("system", "无法找到" + key + "对应的值");
        return null;
    }
    private static SharedPreferences.Editor obtainEditor(Context context,String fileName){
        SharedPreferences sp=context.getSharedPreferences(fileName, MODE_PRIVATE);
        //获得编译
        SharedPreferences.Editor editor = sp.edit();
        return editor;
    }

    private static SharedPreferences obtainSP(Context context,String fileName){
        SharedPreferences shareP=context.getSharedPreferences(fileName, MODE_PRIVATE);
        return shareP;
    }
    //使用方法如下：

    // public SaveDatasUtils obtainDatasUtils;

    // obtainDatasUtils=new SaveDatasUtils(this, "UoU");

//    listStr = new ArrayList<>();
//    for (int i = 0; i<10 ; i++){
//        listStr.add("" +i);
//    }
//    obtainDatasUtils.setObject("untilsName",listStr);  //存数组
//
//    beans = new Beans(1,"Stone","这是描述","15717914505","http://www.baidu.com");
//    obtainDatasUtils.setObject("untilsName",beans);  //存对象 对象class一定要继承Serializable
//    Beans bean = obtainDatasUtils.getObject("untilsName");
//
//    mapStr = new HashMap();
//    mapStr.put("name", "Stone");
//    mapStr.put("age", 18);
//    mapStr.put("email", "stoneUoU@163.com");
//    mapStr.put("place", "江西省南昌市高新技术开发区天祥大道289号南昌工程学院");
//
//    obtainDatasUtils.setObject("untilsName",mapStr);  //Map
//    Map res = obtainDatasUtils.getObject("untilsName");
//    System.out.println(res.get("place"));
//
//    obtainDatasUtils.setObject("untilsName",false);
//    System.out.println(obtainDatasUtils.getObject("untilsName"));


}



