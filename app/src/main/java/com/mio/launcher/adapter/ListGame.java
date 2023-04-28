package com.mio.launcher.adapter;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mio.launcher.MioInfo;
import com.mio.launcher.MioUtils;
import com.mio.launcher.R;

import java.io.File;
import java.util.List;

public class ListGame extends BaseAdapter {
    private List<String> gamelist;
    private Context context;
    public ListGame(Context context,List<String> gamelist){
        this.context=context;
        this.gamelist=gamelist;
    }
    public void addVersion(String version){
        gamelist.add(version);
        this.notifyDataSetChanged();
    }
    public List<String> getList(){
        return gamelist;
    }
    @Override
    public int getCount() {
        return gamelist.size();
    }

    @Override
    public Object getItem(int position) {
        return gamelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.item_listview_game,parent,false);
        TextView name=convertView.findViewById(R.id.item_listview_game_name);
        name.setText(gamelist.get(position));
        ImageButton remove=convertView.findViewById(R.id.item_listview_game_remove);
        remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View p1) {
                AlertDialog dialog=new AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage("确定要删除：" + gamelist.get(position) + "吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dia, int which) {
                                MioUtils.deleteFile(new File(MioInfo.DIR_VERSIONS,gamelist.get(position)).getAbsolutePath());
                                gamelist.remove(gamelist.get(position));
                                ListGame.this.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                dialog.show();
            }
        });

        return convertView;
    }
}
