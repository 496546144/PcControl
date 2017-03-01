package com.hbung.pccontrol;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements SuperSocket.OnCallback
        , TouchView.OnCallback {
    @BindView(R.id.topIB)
    ImageButton topIB;
    @BindView(R.id.bottomIB)
    ImageButton bottomIB;
    @BindView(R.id.leftIB)
    ImageButton leftIB;
    @BindView(R.id.rigthIB)
    ImageButton rigthIB;
    @BindView(R.id.bottomRoot)
    RelativeLayout bottomRoot;
    SuperSocket superSocket;
    ProgressDialog dialog;
    @BindView(R.id.konggeButton)
    Button konggeButton;
    @BindView(R.id.escButton)
    Button escButton;
    @BindView(R.id.touchView)
    TouchView touchView;

    JsonHelp jsonHelp;
    @BindView(R.id.linkButton)
    Button linkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        jsonHelp = new JsonHelp();
        superSocket = new SuperSocket(this);
        superSocket.setOnCallback(this);
        initView();
    }

    private void initView() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        dialog.setMessage("正在搜索局域网中的电脑");
        startSocket();
        touchView.setOnCallback(this);
    }

    private void startSocket() {
        dialog.show();
        superSocket.start();
    }

    @Override
    public void onLinkComplete(boolean isSuccess) {
        dialog.dismiss();
        if (!isSuccess) {
            linkButton.setText("重新连接");
            linkButton.setEnabled(true);
            showNoLink();
        } else {
            Snackbar.make(bottomIB, "链接成功", Snackbar.LENGTH_LONG).show();
            linkButton.setText("已连接");
            linkButton.setEnabled(false);
        }
    }

    private void showNoLink() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("抱歉，当前局域网未找到电脑，请确定电脑和手机在同一局域网，并且电脑端的程序已经打开");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @OnClick({R.id.linkButton, R.id.topIB, R.id.bottomIB, R.id.leftIB, R.id.rigthIB, R.id.konggeButton, R.id.escButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linkButton://重新链接
                if (!superSocket.isLinkSuccess) {
                    startSocket();
                }
                break;
            case R.id.topIB:
                superSocket.send(jsonHelp.getKey(JavaKeyEvent.VK_UP));
                break;
            case R.id.bottomIB:
                superSocket.send(jsonHelp.getKey(JavaKeyEvent.VK_DOWN));
                break;
            case R.id.leftIB:
                superSocket.send(jsonHelp.getKey(JavaKeyEvent.VK_LEFT));
                break;
            case R.id.rigthIB:
                superSocket.send(jsonHelp.getKey(JavaKeyEvent.VK_RIGHT));
                break;
            case R.id.konggeButton:
                superSocket.send(jsonHelp.getKey(JavaKeyEvent.VK_SPACE));
                break;
            case R.id.escButton:
                superSocket.send(jsonHelp.getKey(JavaKeyEvent.VK_ESCAPE));
                break;
        }
    }


    @Override
    public void onMove(MoveData moveData) {
        if (superSocket.isLinkSuccess) {
            superSocket.send(jsonHelp.getMove(moveData));
        }
    }
}
