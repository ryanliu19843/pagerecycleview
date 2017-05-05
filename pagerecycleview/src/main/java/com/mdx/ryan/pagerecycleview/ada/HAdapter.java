package com.mdx.ryan.pagerecycleview.ada;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.mdx.ryan.pagerecycleview.viewhold.MViewHold;
import com.mdx.ryan.pagerecycleview.widget.SwipRefreshLoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ryan on 2016/4/26.
 */
public class HAdapter extends MAdapter {

    private RecyclerView.Adapter iAdapter;
    private OnLoadingLast onLoadingLast;
    private List<MViewHold> footviews = new ArrayList<>(), headviews = new ArrayList<>();
    private int lastviewtype = -9900000;
    private HashMap<Integer, MViewHold> mIntViewHold = new HashMap<>();

    public HAdapter(Context context, RecyclerView.Adapter iAdapter) {
        super(context, new Card[]{});
        this.iAdapter = iAdapter;
        if (iAdapter instanceof MAdapter) {
            ((MAdapter) iAdapter).hAdapter = this;
            this.overCard = ((MAdapter) iAdapter).overCard;
        }
    }

    public void setAdapter(RecyclerView.Adapter iAdapter) {
        this.iAdapter = iAdapter;
        if (iAdapter instanceof MAdapter) {
            ((MAdapter) iAdapter).setRecyclerView( recyclerView);
            ((MAdapter) iAdapter).hAdapter = this;
            this.overCard = ((MAdapter) iAdapter).overCard;
        }
    }

    public void setOnLoadingLast(OnLoadingLast onLoadingLast) {
        this.onLoadingLast = onLoadingLast;
    }

    public List<MViewHold> getFootviews() {
        return footviews;
    }

    public List<MViewHold> getHeadviews() {
        return headviews;
    }

    public void addFoot(MViewHold viewHold) {
        footviews.add(viewHold);
        lastviewtype--;
        viewHold.id = lastviewtype;
        resetMap();
        notifyDataSetChanged();
    }

    public void addFoot(MViewHold viewHold, int index) {
        footviews.add(index, viewHold);
        lastviewtype--;
        viewHold.id = lastviewtype;
        resetMap();
        notifyDataSetChanged();
    }


    public void addHead(MViewHold viewHold) {
        headviews.add(viewHold);
        lastviewtype--;
        viewHold.id = lastviewtype;
        resetMap();
        notifyDataSetChanged();
    }

    public void addHead(MViewHold viewHold, int index) {
        headviews.add(index, viewHold);
        lastviewtype--;
        viewHold.id = lastviewtype;
        resetMap();
        notifyDataSetChanged();
    }

    public void removeFoot(MViewHold viewHold) {
        footviews.remove(viewHold);
        resetMap();
        notifyDataSetChanged();
    }

    public void removeFoot(int ind) {
        footviews.remove(ind);
        resetMap();
        notifyDataSetChanged();
    }


    public void removeHead(MViewHold viewHold) {
        headviews.remove(viewHold);
        resetMap();
        notifyDataSetChanged();
    }

    public void removeHead(int ind) {
        headviews.remove(ind);
        resetMap();
        notifyDataSetChanged();
    }

    public void resetMap() {
        mIntViewHold.clear();
        for (MViewHold mh : headviews) {
            mIntViewHold.put(mh.id, mh);
        }
        for (MViewHold mh : footviews) {
            mIntViewHold.put(mh.id, mh);
        }
    }


    public RecyclerView.Adapter getAdapter() {
        return this.iAdapter;
    }


    public int getiPosion(int posion) {
        return posion + headviews.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int headind = position - headviews.size();
        int footind = position - headviews.size() - getIcount();
        int ind = position - headviews.size();
        if (headind >= 0 && footind < 0 && iAdapter != null) {
            iAdapter.onBindViewHolder(holder, ind);
        } else {
            if (holder instanceof MViewHold) {
                ((MViewHold) holder).animateShow();
            }
        }
        boolean isruned = false;
        if (holder.itemView instanceof SwipRefreshLoadingView) {
            if (onLoadingLast != null) {
                onLoadingLast.onLoadingLast(this, position);
                isruned = true;
            }
        }
//        if (position == headviews.size() + getIcount() + footviews.size() - 1 && !isruned) {
//            if (onLoadingLast != null) {
//                onLoadingLast.onLoadingLast(this, position);
//            }
//        }
    }

    public static interface OnLoadingLast {
        void onLoadingLast(HAdapter adapter, int posion);
    }

    private RecyclerView.LayoutParams getlayout(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams == null) {
            StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(
                    StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT, StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT);
            params.setFullSpan(true);
            return params;
        } else {
            StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(
                    layoutParams.width, layoutParams.height);
            params.setFullSpan(true);
            return params;
        }
    }

    public int getSpanSize(int position) {
        int headind = position - headviews.size();
        int footind = position - headviews.size() - getIcount();
        int ind = position - headviews.size();
        if (headind >= 0 && footind < 0 && iAdapter != null) {
            if (iAdapter instanceof MAdapter) {
                return ((MAdapter) iAdapter).getSpanSize(ind);
            }
        }
        if (recyclerView != null) {
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                return ((GridLayoutManager) recyclerView.getLayoutManager()).getSpanCount();
            }
        }
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType < -9900000) {
            int ind = viewType;
            return mIntViewHold.get(ind);
        }
        return iAdapter.onCreateViewHolder(parent, viewType);
    }


    public Object get(int position) {
        int headind = position - headviews.size();
        int footind = position - headviews.size() - getIcount();
        int ind = position - headviews.size();

        if (headind < 0) {
            return headviews.get(position);
        } else if (footind >= 0) {
            return footviews.get(footind);
        } else {
            if (iAdapter instanceof MAdapter) {
                return ((MAdapter) iAdapter).get(ind);
            } else {
                return null;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int headind = position - headviews.size();
        int footind = position - headviews.size() - getIcount();
        int ind = position - headviews.size();

        if (headind < 0) {
            MViewHold mViewHold = headviews.get(position);
            return mViewHold.id;
        } else if (footind >= 0) {
            MViewHold mViewHold = footviews.get(footind);
            return mViewHold.id;
        } else {
            return iAdapter.getItemViewType(ind);
        }
    }


    public void AddAll(List<Card> list) {
        if (iAdapter instanceof MAdapter) {
            ((MAdapter) iAdapter).AddAll(list);
        }
    }

    public void AddAll(MAdapter list) {
        if (iAdapter instanceof MAdapter) {
            ((MAdapter) iAdapter).AddAll(list);
        }
    }

    public void AddAllOnBegin(MAdapter list) {
        if (iAdapter instanceof MAdapter) {
            ((MAdapter) iAdapter).AddAllOnBegin(list);
        }
    }

    public void add(Object item) {
        if (iAdapter instanceof MAdapter) {
            ((MAdapter) iAdapter).add(item);
        }
    }

    public void add(int ind, Object item) {
        if (iAdapter instanceof MAdapter) {
            ((MAdapter) iAdapter).add(ind, item);
        }
    }

    public void remove(Object item) {
        if (iAdapter instanceof MAdapter) {
            ((MAdapter) iAdapter).remove(item);
        }
    }

    public void remove(int posion) {
        if (iAdapter instanceof MAdapter) {
            ((MAdapter) iAdapter).remove(posion);
        }
    }

    public int getPosion(Card card) {
        if (iAdapter instanceof MAdapter) {
            return ((MAdapter) iAdapter).getPosion(card) + headviews.size();
        }
        return 0;
    }

    public void clear() {
        if (iAdapter instanceof MAdapter) {
            ((MAdapter) iAdapter).clear();
        }
    }

    public List<Card> getList() {
        if (iAdapter instanceof MAdapter) {
            return ((MAdapter) iAdapter).getList();
        }
        return super.getList();
    }


    @Override
    public int getItemCount() {
        return footviews.size() + headviews.size() + getIcount();
    }

    private int getIcount() {
        if (iAdapter == null) {
            return 0;
        }
        return iAdapter.getItemCount();
    }
}
