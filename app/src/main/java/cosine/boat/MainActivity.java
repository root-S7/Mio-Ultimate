package cosine.boat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.mio.launcher.MioInfo;
import com.mio.launcher.R;
import com.mio.launcher.ShellServer;

import java.io.File;

public class MainActivity extends Activity { 
    EditText 命令;
    EditText 输出;
    ShellServer shell;
    Message msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);
        File home=new File(MioInfo.DIR_DATA,"files/home");
        home.mkdirs();
        命令 = findViewById(R.id.activitymainEditText1);
        输出 = findViewById(R.id.activitymainEditText2);
        msg=new Message();
        shell=new ShellServer(new ShellServer.Callback() {
            @Override
            public void output(String output) {
                输出.post(()->{
                   输出.append(output+"\n");
                   输出.setSelection(输出.getText().toString().length());
                });
            }
        },"sh",home.getAbsolutePath());
        shell.start();
        输出.setOnClickListener((v)->{
            showKeyboard();
        });
        命令.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!命令.getText().toString().equals("")&&命令.getText().toString().endsWith("\n")){
                    if (命令.getText().toString().contains("clear")){
                        输出.setText("");
                        return;
                    }
                    shell.append(命令.getText().toString());
                    输出.append(命令.getText().toString());
                    命令.setText("");
                }
            }
        });
    }
    public void showKeyboard() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        命令.requestFocusFromTouch();
        命令.requestFocus();
    }
} 
