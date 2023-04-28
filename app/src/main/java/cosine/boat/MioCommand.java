package cosine.boat;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MioCommand {
    private Context context;
    private Listener listener;
    private Listener2 listener2;
    public MioCommand(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }
    public MioCommand(Context context, Listener2 listener2, int i) {
        this.context = context;
        this.listener2 = listener2;
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String[] s=(String[])msg.obj;
                    if (listener != null)
                        listener.onError(s[0], s[1]);
                    break;
                case 1:
                    String[] ss=(String[])msg.obj;
                    if (listener != null)
                        listener.onFinish(ss[0], ss[1]);
                    break;
                case 2:
                    if (listener2 != null)
                        listener2.onRun((String)msg.obj);
                    break;
                case 3:
                    if (listener2 != null)
                        listener2.onFinish((String)msg.obj);
                    break;
                case 4:
                    String[] sss=(String[])msg.obj;
                    if (listener2 != null)
                        listener2.onError(sss[0], sss[1]);
                    break;
            }
        }};
    public void cmd(final String cmd) {
        new Thread(new Runnable(){
                @Override
                public void run() {
                    try {

                        Process proc = Runtime.getRuntime().exec(cmd);

                        // 标准输入流（必须写在 waitFor 之前）
                        String inStr = consumeInputStream(proc.getInputStream());
                        // 标准错误流（必须写在 waitFor 之前）
                        String errStr = consumeInputStream(proc.getErrorStream());

                        int retCode = proc.waitFor();

                        if (retCode == 0) {
                            Message msg=new Message();
                            msg.what = 1;
                            String[] s=new String[2];
                            s[0] = inStr;
                            s[1] = cmd;
                            msg.obj = s;
                            handler.sendMessage(msg);
                        } else {
                            Message msg=new Message();
                            String[] s=new String[2];
                            s[0] = "执行失败:" + errStr;
                            s[1] = cmd;
                            msg.what = 0;
                            msg.obj = s;
                            handler.sendMessage(msg);
                        }

                    } catch (Exception e) {
                        System.out.println(e);
                        Message msg=new Message();
                        msg.what = 0;
                        String[] s=new String[2];
                        s[0] = "执行失败:" + e.toString();
                        s[1] = cmd;
                        msg.obj = s;
                        handler.sendMessage(msg);
                    }
                }
            }).start();
    }
    interface Listener {
        void onError(String err, String cmd);
        void onFinish(String result, String cmd);
    }
    public void cmd2(final String cmd) {
        new Thread(new Runnable(){
                @Override
                public void run() {
                    try {

                        Process proc = Runtime.getRuntime().exec(cmd);

                        // 标准输入流（必须写在 waitFor 之前）
                        consumeInputStream2(proc.getInputStream());
                        // 标准错误流（必须写在 waitFor 之前）
                        consumeInputStream2(proc.getErrorStream());

                        int retCode = proc.waitFor();

                        if (retCode == 0) {
                            Message msg=new Message();
                            msg.what = 3;
                            msg.obj = cmd;
                            handler.sendMessage(msg);
                        } else {
                            Message msg=new Message();
                            String[] s=new String[2];
                            s[0] = "错误代码:" + retCode;
                            s[1] = cmd;
                            msg.what = 4;
                            msg.obj = s;
                            handler.sendMessage(msg);
                        }

                    } catch (Exception e) {
                        System.out.println(e);
                        Message msg=new Message();
                        msg.what = 4;
                        String[] s=new String[2];
                        s[0] = "执行失败:" + e.toString();
                        s[1] = cmd;
                        msg.obj = s;
                        handler.sendMessage(msg);
                    }
                }
            }).start();
    }
    interface Listener2 {
        void onError(String err, String cmd);
        void onFinish(String cmd);
        void onRun(String info);
    }
    /**
     *   消费inputstream，并返回
     */
    public String consumeInputStream(InputStream is) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s ;
            StringBuilder sb = new StringBuilder();
            while ((s = br.readLine()) != null) {
                System.out.println(s);
                sb.append(s + "\n");
            }
            return sb.toString();
        } catch (IOException e) {}
        return "";
    }
    public void consumeInputStream2(InputStream is) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s ;
            while ((s = br.readLine()) != null) {
                Message msg=new Message();
                msg.what = 2;
                msg.obj = s;
                handler.sendMessage(msg);
            }
        } catch (IOException e) {}
    }

}
