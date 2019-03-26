package com.Tools;
//创建单例类：

//饿汉式
//public class SingleIns {
//    private static final SingleIns mSingleIns = new SingleIns();
//    private SingleIns(){}
//    public static SingleIns getInstance(){
//        return mSingleIns;
//    }
//}


//懒汉式
public class SingleIns {
    private static SingleIns mInstance;

    private SingleIns() {
    }

    public static synchronized SingleIns getInstance () {
        if (mInstance == null) {
            mInstance = new SingleIns();
        }
        return mInstance;
    }
}


//Double Check Lock（DCL）双重检查锁
//public class SingleIns {
//    private static  SingleIns mSingleIns ;
//    private SingleIns(){}
//    public static  SingleIns getInstance(){
//        if (mSingleIns==null){
//            synchronized (SingleIns.class){
//                if (mSingleIns==null){
//                    mSingleIns=new SingleIns();
//                }
//            }
//        }
//        return mSingleIns;
//    }
//}


//枚举单例
//public enum SingleIns{
//    INSTANCE;
//    public void doThing(){
//        System.out.println(this.hashCode());
//    }
//}
//SingleIns singleIns = SingleIns.INSTANCE;


//静态内部类
//import java.io.ObjectStreamException;
//public class SingleIns {
//    private SingleIns(){}
//    public static  SingleIns getInstance(){
//        return SingleInsHolder.mSingleIns;
//    }
//    private static class SingleInsHolder{
//        private static final SingleIns mSingleIns = new SingleIns();
//    }
//
//    private Object readRedolve() throws ObjectStreamException {
//        return SingleInsHolder.mSingleIns;
//    }
//}
