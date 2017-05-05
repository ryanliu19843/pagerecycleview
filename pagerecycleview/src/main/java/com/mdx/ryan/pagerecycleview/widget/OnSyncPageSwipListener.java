package com.mdx.ryan.pagerecycleview.widget;


import android.os.AsyncTask;
import android.text.TextUtils;

import com.mdx.ryan.pagerecycleview.MFRecyclerView;
import com.mdx.ryan.pagerecycleview.ada.CardAdapter;

import java.util.UUID;

/**
 * Created by ryan on 2016/6/21.
 */
public class OnSyncPageSwipListener extends OnSwipLoadListener {


    protected int page = 1;               //当前页码
    private boolean isLoading   /*是否正在加载*/, isReload /*是否是重新加载*/, ispageLoading /*是否在加载下一页*/;
    private String loadingId = UUID.randomUUID().toString()  /*请求id （防止多次请求数据错误）*/;

    public SwipSyncTask asyncTask;
    public Object[] params;

    public OnSyncPageSwipListener(SwipSyncTask asyncTask, Object... objs) {
        params = objs;
        this.asyncTask = asyncTask;
        this.asyncTask.setOnPageSwipListener(this);
    }


    /**
     * 开始加载数据
     *
     * @param page 页码
     */
    public void loadApiFrom(long page) {
        if (isReload) {
            loadingId = UUID.randomUUID().toString();  //设置请求id
        }
        if (asyncTask != null) {
            Asynctask asynctask=new Asynctask(loadingId);
            asynctask.execute(page,params);
        }
    }


    public class Asynctask extends AsyncTask<Object, Integer, CardAdapter> {
        public String id;

        public Asynctask(String id) {
            this.id = id;
        }

        @Override
        protected CardAdapter doInBackground(Object... params) {
            if (asyncTask != null) {
                return asyncTask.doInBackground(params);
            }
            return null;
        }

        @Override
        protected void onPostExecute(CardAdapter result) {
            if (asyncTask != null && loadingId.equals(id)) {
                asyncTask.onPostExecute(result);
            }
        }


        protected void onPreExecute() {
            if (asyncTask != null) {
                asyncTask.onPreExecute();
            }
        }
    }

    /**
     * 清理状态
     */
    public void clear() {
        if (asyncTask != null) {
            asyncTask.intermit();
        }
        recyclerView.endPullLoad();
        recyclerView = null;
        onDataLoaded = null;
    }


    public void setAdapter(CardAdapter cardAdapter, boolean hasepage, String errmsg) {
        if (recyclerView == null) {   //判断是否存在view
            return;
        }
        if(isReload) {
            recyclerView.clearAdapter();
        }
        isReload = false;
        isLoading = false;
        ispageLoading = false;

        recyclerView.addAdapter(cardAdapter);

        havepage = hasepage;
        if (!havepage) {
            recyclerView.endPage();
        } else {
            recyclerView.showPage();
        }

        /*加载完成监听*/
        if (onDataLoaded != null) {
            onDataLoaded.onDataLoaded(cardAdapter, page);
        }

        if (!TextUtils.isEmpty(errmsg)) {
            if (recyclerView.getMAdapter() == null || recyclerView.getMAdapter().getItemCount() == 0) {
                this.onDataState(3, 1, errmsg);  //全屏显示错误
            } else {
                this.onDataState(0, 1, errmsg);  //页尾显示错误
            }
            this.havepage=false;
        } else {
            page++;
        }
        /*播放结束动画*/
        recyclerView.endPullLoad();

    }



    /**
     * 设置数据加载状态
     *
     * @param state
     * @param error
     * @param msg
     */
    @Override
    public void onDataState(int state, int error, String msg) {
        if (recyclerView.getParent() instanceof MFRecyclerView) {
            MFRecyclerView mfRecyclerView = (MFRecyclerView) recyclerView.getParent();
            mfRecyclerView.setLoadingState(state, error, msg);
        }
    }


    /**
     * 下拉加载监听
     *
     * @param type
     */
    @Override
    public void onSwipLoad(int type) {
//        if (!isLoading) {
//            this.updateonly = false;
//        }
        swipload(type);
    }


    /**
     * 下拉加载
     *
     * @param type
     */

    public void swipload(int type) {
        if (!isLoading) {
            isReload = true;
            isLoading = true;
            havepage = false;
            page = 1;
            havepage = false;
            if (type == 0) {
                if (recyclerView.getMAdapter() == null || recyclerView.getMAdapter().getItemCount() == 0) {
                    this.onDataState(1, 0, "");
                }
            } else {
                this.onDataState(0, 0, "");
            }
            loadApiFrom(page);
            if (onDataLoaded != null) {
                onDataLoaded.onReload(page);
            }
        }
    }


    @Override
    public void onUpdateLoad(int type) {
        swipload(type);
    }

    @Override
    public void onSwipStateChange(int state, float mv, float mt) {

    }

    @Override
    public void onPageLoad() {
        if (havepage && !ispageLoading) {
            ispageLoading = true;
            recyclerView.swipRefreshLoadingView.setState(0, "");
            loadApiFrom(page);
            if (onDataLoaded != null) {
                onDataLoaded.onPageLoad(page);
            }
        }
    }

}
