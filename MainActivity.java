package com.example.task;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private MyAsyncTask myAsyncTask;
    private Button load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        load = (Button) findViewById(R.id.btn_load);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        myAsyncTask = new MyAsyncTask(); //只能在UI 线程中创建
        myAsyncTask.execute();         //只能执行一次  多次执行会出现异常
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,ImagePage.class);
                startActivity(intent);
            }
        });
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (myAsyncTask != null && myAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            // cancel 方法只是将对应的AsyncTask 标记为取消状态  并不是取消线程的执行
            myAsyncTask.cancel(true);
        }
    }

    class MyAsyncTask extends AsyncTask<Void,Integer,Void> {

        @Override
        protected Void doInBackground(Void... params) {  //仅此方法是执行在后台线程中不能再这里修改UI 其他方法都执行在主线程中可以修改UI
            //模拟进度更新
            for (int i = 0; i < 100; i++) {
                if (isCancelled()) {
                    break;
                }
                publishProgress(i);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //获取进度更新
            super.onProgressUpdate(values);
            if (isCancelled()) {
                return;
            }
            progressBar.setProgress(values[0]);
        }
    }
}
