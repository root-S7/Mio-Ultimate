package com.mio.launcher;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShellServer extends Thread
{
    private Process process;
    private BufferedReader output;
    private Callback callback;
    String runtimePath = "/data/data/com.mio.launcher/app_runtime/j2re-image";

    public ShellServer(Callback callback, String shell, String home){
        this.callback = callback;

        ProcessBuilder pb = new ProcessBuilder(shell);
        pb.directory(new File(home));
        pb.redirectErrorStream(true);
        pb.environment().clear();

        try{
            process = pb.start();
            output = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder init = new StringBuilder();
            init.append("export HOME=" + home + "&&cd\n");
            init.append("export LD_LIBRARY_PATH="+runtimePath+"/lib/aarch64:"+runtimePath+"/lib/aarch64/jli:"+runtimePath+"/lib/aarch64/server:/system/lib64:$LD_LIBRARY_PATH\n");
            init.append("export JAVA_HOME="+runtimePath+"\n");
//            init.append("export JRE_HOME=.:$JAVA_HOME/jre\n");
//            init.append("export CLASSPATH=.:$JAVA_HOME/lib/tools.jar\n");
            init.append("export PATH="+new File(new File(home).getParent(),"bin").getAbsolutePath()+":$JAVA_HOME/bin:$PATH\n");
            if (!new File(runtimePath,"bin/java").canExecute()){
                init.append("chmod +x "+runtimePath+"/bin/*\n");
            }
            if (!new File(new File(home).getParent(),"bin").exists()){
                new File(new File(home).getParent(),"bin").mkdirs();
                init.append(new File(new File(runtimePath).getParent(),"busybox").getAbsolutePath()+" --install -s "+new File(new File(home).getParent(),"bin").getAbsolutePath()+"\n");
            }
            append(init.toString());
        }catch (IOException e){
            callback.output(e.toString() + "\n");
        }
    }
    public ShellServer(String home,String... cmd){
        this.callback = new Callback() {
            @Override
            public void output(String output) {

            }
        };
        ProcessBuilder pb = new ProcessBuilder("sh");
        pb.directory(new File(home));
        pb.redirectErrorStream(true);
        pb.environment().clear();

        try{
            process = pb.start();
            output = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder init = new StringBuilder();
            init.append("export HOME=" + home + "&&cd\n");
            List<String> list=new ArrayList<>(Arrays.asList(cmd));
            for (String s:list){

            }
            append(init.toString());
        }catch (IOException e){
            callback.output(e.toString() + "\n");
        }
    }

    public void append(String command){
        try{
            process.getOutputStream().write((command + "\n").getBytes());
            process.getOutputStream().flush();
        }catch (IOException e){
            callback.output(e.toString() + "\n");
        }
    }

    @Override
    public void run(){
        try{
            String line;
            while ((line = output.readLine()) != null)
                callback.output(line);
            output.close();
            process.getInputStream().close();
            process.getErrorStream().close();
            process.getOutputStream().close();
        }catch (IOException e){
            callback.output(e.toString() + "\n");
        }
        process.destroy();
    }

    public interface Callback{
        public abstract void output(String output);
    }
}


