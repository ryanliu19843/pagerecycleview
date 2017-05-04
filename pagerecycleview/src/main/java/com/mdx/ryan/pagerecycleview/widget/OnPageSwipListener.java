package com.mdx.ryan.pagerecycleview.widget;


import android.os.AsyncTask;
import android.text.TextUtils;

import com.mdx.ryan.pagerecycleview.MFRecyclerView;
import com.mdx.ryan.pagerecycleview.ada.CardAdapter;

import java.util.UUID;

/**
 * Created by ryan on 2016/6/21.
 */
public class OnPageSwipListener extends OnSwipLoadListener {


    //    protected ApiUpdate mApiUpdate;       //网络请求类
//    protected DataFormat mDataFormat;     //数据解释类
    protected int page = 1;               //当前页码
    //    private boolean useCache = true /*是否使用缓存*/, oneUseCaches = true /*第一次使用缓存*/;
    private boolean isLoading   /*是否正在加载*/, isReload /*是否是重新加载*/, ispageLoading /*是否在加载下一页*/;
    private String loadingId = UUID.randomUUID().toString()  /*请求id （防止多次请求数据错误）*/;
//    private int errortype = 0;   //错误类型
//    private boolean updateonly = false;  //是否只是更新数据。（下拉刷新默认是重新加载，还是更新）

    public SwipSyncTask asyncTask;
    public Object[] params;

    public OnPageSwipListener(SwipSyncTask asyncTask, Object... objs) {
        params = objs;
        this.asyncTask = asyncTask;
        this.asyncTask.setOnPageSwipListener(this);
    }

    /**
     * 初始化
     * @param context
     * @param api
     * @param dataFormat
     */
//    public OnPageSwipListener(Context context, ApiUpdate api, DataFormat dataFormat) {
//        this.context = context;
//        setApiUpdate(api);
//        setDataFormat(dataFormat);
//    }

    /**
     * 设置更新接口类
     * @param api
     */
//    public void setApiUpdate(ApiUpdate api) {
//        this.mApiUpdate = api;
//        this.mApiUpdate.setContext(context);
//        this.mApiUpdate.setParent(this);
//        this.mApiUpdate.setHavaPage(true);
//        errortype = this.mApiUpdate.getErrorType();
//        this.mApiUpdate.setErrorType(111);
//        useCache = this.mApiUpdate.isSaveAble();
//        this.mApiUpdate.setMethod("getMessage");
//    }


    /**
     * 设置数据解释类
     * @param dataFormat
     */
//    public void setDataFormat(DataFormat dataFormat) {
//        this.mDataFormat = dataFormat;
//    }


    /**
     * 开始加载数据
     *
     * @param page 页码
     */
    public void loadApiFrom(long page) {
        if (isReload) {
            loadingId = UUID.randomUUID().toString();  //设置请求id
        }
//        if (this.mApiUpdate == null) {   //判断是否存在请求类
//            return;
//        }
//        if (!havepage) {                  //判断是否有下一页
//            oneUseCaches = useCache;
//        }
//        this.mApiUpdate.setPage(page);     //设置页码
//        String[][] params = this.mDataFormat.getPageNext();   //设置自定义分页参数
//        if (params != null) {
//            this.mApiUpdate.setPageParams(params);
//        }
//        this.mApiUpdate.setSaveAble(oneUseCaches);    //设置是否允许保存数据
//        this.mApiUpdate.setSonId(loadingId);          //设置接口id
//        if (this.mApiUpdate.getUpdateOne() == null) {    //判断接口参数
//            throw new IllegalAccessError("no updateone exit");
//        } else {
//            this.mApiUpdate.loadUpdateOne();        //加载数据
//        }
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
        } else {
            page++;
        }
        /*播放结束动画*/
        recyclerView.endPullLoad();

    }

//    //接口处理类，主要处理接口返回值
//    public void getMessage(Son son) {
//        if (!son.getSonId().equals(loadingId)) {  //判断请求id是否一致
//            return;
//        }
//        if (recyclerView == null) {   //判断是否存在view
//            return;
//        }
//        if (isReload && !updateonly) {    //是否需要清理数据
//            recyclerView.clearAdapter();
//        }
//        isReload = false;
//        isLoading = false;
//        ispageLoading = false;
//        if (son.getError() == 0 || son.getType() % 1000 / 100 == 1 || errortype % 1000 / 100 == 1) {   //判断错误类型
//            try {
//                if (!updateonly) {     //
//                    CardAdapter cardAdapter = (CardAdapter) mDataFormat.getCardAdapter(context, son, page);
//                    havepage = mDataFormat.hasNext();
//                    if (!havepage) {
//                        recyclerView.endPage();
//                    } else {
//                        recyclerView.showPage();
//                    }
//                    recyclerView.addAdapter(cardAdapter);
//                } else {
//                    mDataFormat.updateCardAdapter(context, son, recyclerView.getMAdapter());
//                }
//            } catch (Exception e) {
//                havepage = false;
//                recyclerView.endPage();
//            }
//            if (son.getError() == 0) {
//                page++;
//            }
//        }
//        /*接口错误处理*/
//        if (recyclerView.getMAdapter() == null || recyclerView.getMAdapter().getItemCount() == 0) {
//            this.onDataState(3, son.getError(), son.msg);  //全屏显示错误
//        } else {
//            this.onDataState(0, son.getError(), son.msg);  //页尾显示错误
//        }
//
//        /*加载完成监听*/
//        if (onDataLoaded != null) {
//            onDataLoaded.onDataLoaded(son, page);
//        }
//        /*播放结束动画*/
//        recyclerView.endPullLoad();
//    }


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
//            if (mDataFormat != null) {
//                mDataFormat.reload();
            loadApiFrom(page);
//            }
            if (onDataLoaded != null) {
                onDataLoaded.onReload(page);
            }
        }
    }


    @Override
    public void onUpdateLoad(int type) {
//        if (!isLoading) {
//            this.updateonly = true;
//        }
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
//            recyclerView.canPull = false;
            loadApiFrom(page);
            if (onDataLoaded != null) {
                onDataLoaded.onPageLoad(page);
            }
        }
    }

}
