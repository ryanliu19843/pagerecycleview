package com.mdx.ryan.pagetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;

import com.mdx.ryan.pagerecycleview.MFRecyclerView;
import com.mdx.ryan.pagerecycleview.ada.Card;
import com.mdx.ryan.pagerecycleview.ada.CardAdapter;
import com.mdx.ryan.pagetest.card.CardGroup;
import com.mdx.ryan.pagetest.card.CardText;
import com.mdx.ryan.pagerecycleview.widget.OnSyncPageSwipListener;
import com.mdx.ryan.pagerecycleview.widget.SwipSyncTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public Button reload;
    public Button error;
    public Button group;
    public Button clean;
    public MFRecyclerView recycleview;
    public SwipSyncTask swipSyncTask;
    public OnSyncPageSwipListener onPageSwipListener;
    public boolean  isgroup = false,ispage=false;
    public int iserror = 0;
    public Button haspage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findVMethod();


        swipSyncTask = new SwipSyncTask() {
            @Override
            protected CardAdapter doInBackground(Object... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<Card> list = new ArrayList<>();
                for (int i = 0; i < 25; i++) {

                    if (i % 10 == 0 && isgroup) {
                        CardGroup card = new CardGroup("group" + i);
                        card.setShowType(1);
                        list.add(card);
                    } else {
                        CardText card = new CardText("1111" + i);
                        list.add(card);
                    }
                }
                if (iserror==1) {
                    error = "加载错误";
                    return null;
                }else if(iserror==2){
                    error = "加载错误";
                }
                this.haspage=ispage;
                return new CardAdapter(MainActivity.this, list);
            }

            @Override
            public void intermit() {

            }
        };
        onPageSwipListener = new OnSyncPageSwipListener(swipSyncTask);

        recycleview.setOnSwipLoadListener(onPageSwipListener);
        recycleview.pullLoad();

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleview.pullLoad();
            }
        });

        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iserror=(iserror+1)%3;
                recycleview.pullLoad();
            }
        });

        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isgroup) {
                    isgroup = false;
                } else {
                    isgroup = true;
                }
                recycleview.pullLoad();
            }
        });


        haspage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ispage) {
                    ispage = false;
                    recycleview.getRecyclerView().endPage();
                } else {
                    ispage = true;
                    recycleview.getRecyclerView().showPage();
                }

            }
        });

        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleview.clearAdapter();
            }
        });
    }

    private void findVMethod() {
        reload = (Button) findViewById(R.id.reload);
        error = (Button) findViewById(R.id.error);
        group = (Button) findViewById(R.id.group);
        clean = (Button) findViewById(R.id.clean);
        recycleview = (MFRecyclerView) findViewById(R.id.recycleview);
        haspage = (Button) findViewById(R.id.haspage);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
    }
}
