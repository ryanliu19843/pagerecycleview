package com.mdx.ryan.pagerecycleview.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.mdx.ryan.pagerecycleview.R;
import com.mdx.ryan.pagerecycleview.easeinterpolator.Ease;
import com.mdx.ryan.pagerecycleview.easeinterpolator.EasingInterpolator;


/**
 * Created by ryan on 2016/4/29.
 */
public class SPView extends View implements SwipRefresh {

    private Paint mPaint;
    private Path mPath;
    //笑脸    0
    private int mouth = 2;
    public int state = 1, status = 0;
    public int lcolor = 0xffffffff/*背景颜色*/, scolor = 0xffdcdcdc/*线条颜色*/, bcolor = 0xffff0000 /*动画颜色*/, tcolor = 0xffdcdcdc /*文字颜色*/;
    private ObjectAnimator fobjectAnimator, robjectAnimator, eobjectAnimator;
    private long loadtime = 0;
    private float lineStrokWidth = 1 /*边线宽度*/, circlyStroke = 10 /*圆的宽度*/, circlyw = 50/*园的半径*/, monthAng = 0, monthCirclyw = 0, offsetY = 0/*顶部偏移量*/, loffsetY = offsetY + circlyStroke;
    private float loadC = 0/*线条长度*/, circlyAng = 0 /*圆角度*/, lineW = 0/*当前线长度*/, over = 300/*超出高度*/, barH/*滑动高度*/, barT/*触摸位置*/, textSize = 50/*文字大小*/;
    private String state_a, state_b, state_c, state_d;
    public static Bitmap bitmap, topbitmap, allbitmap;

    public SPView(Context context) {
        super(context);
        init(context);
    }

    public SPView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SPView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SPView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPath = new Path();

        lineStrokWidth = context.getResources().getDimension(R.dimen.sp_linestrok_width);
        circlyStroke = context.getResources().getDimension(R.dimen.sp_circlystrok);
        circlyw = context.getResources().getDimension(R.dimen.sp_circly_width);
        offsetY = context.getResources().getDimension(R.dimen.sp_offsetY);
        loffsetY = offsetY + circlyStroke / 2;
        textSize = context.getResources().getDimension(R.dimen.sp_text_size);
        over = context.getResources().getDimension(R.dimen.sp_over_pull);
        monthCirclyw = circlyw / 5f * 3f;
        monthAng = context.getResources().getDimension(R.dimen.sp_month_ang);
        lcolor = context.getResources().getColor(R.color.sp_background);
        scolor = context.getResources().getColor(R.color.sp_slinecolor);
        bcolor = context.getResources().getColor(R.color.sp_anmcolor);
        tcolor = context.getResources().getColor(R.color.sp_textcolor);


        state_a = context.getString(R.string.sp_state_a);
        state_b = context.getString(R.string.sp_state_b);
        state_c = context.getString(R.string.sp_state_c);
        state_d = context.getString(R.string.sp_state_d);
        if (bitmap == null) {
            bitmap = ((BitmapDrawable) context.getResources().getDrawable(R.mipmap.fw_pull_down_n)).getBitmap();
            topbitmap = ((BitmapDrawable) context.getResources().getDrawable(R.mipmap.fw_pull_top_n)).getBitmap();
            allbitmap = ((BitmapDrawable) context.getResources().getDrawable(R.mipmap.fw_pull_all_n)).getBitmap();
        }
    }


    /**
     * 刷新完成
     */
    public void loadComplit() {
        if (fobjectAnimator != null) {
            fobjectAnimator.cancel();
        }
        if (robjectAnimator != null) {
            robjectAnimator.cancel();
        }
        mouth = 2;
        status = 0;
        endAmin();
    }


    /**
     * 设置当前高度和手指位置
     * @param mv
     * @param mt
     */
    @Override
    public void setH(float mv, float mt) {
        this.barH = mv;
        this.barT = mt;
        invalidate();
    }

    /**
     * 设置当前宽度和手指位置
     * @param mv
     * @param mt
     */
    @Override
    public void setW(float mv, float mt) {
        this.barH = mv;
        this.barT = mt;
        invalidate();
    }

    /**
     * 设置当前状态
     * @param state
     */
    @Override
    public void setState(int state) {
        if (state == SwipRefreshView.SRV_STATE_REFI && this.state != SwipRefreshView.SRV_STATE_REFI) {   //第一次进入正在刷新
            loadtime = System.currentTimeMillis();
            startAmin(); //播放正在刷新动画
        } else if (state == SwipRefreshView.SRV_STATE_REFE && this.state != SwipRefreshView.SRV_STATE_REFE) {   //刷新完成，第一次进入刷新完成
            loadComplit();
        }
        this.state = state;
        invalidate();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        setH(event.getY(), event.getY());
//        return super.onTouchEvent(event);
//    }

    @Override
    public void setOver(float over) {
        this.over = over;
    }

    @Override
    public void startPullload() {
        lineW = 0;
    }

    /**
     * 设置下拉动画，当自动下拉刷新是播放
     * @return
     */
    @Override
    public ObjectAnimator getPullAnimator() {
        ObjectAnimator retn = ObjectAnimator.ofInt(this, "ss", 0, 100);
        retn.setDuration(300);
        return retn;
    }

    /**
     * 设置加载结束动画，当加载结束是播放的动画
     * @return
     */
    @Override
    public ObjectAnimator getEndAnimator() {
        ObjectAnimator retn =ObjectAnimator.ofInt(this, "ss", 0, 100);
        retn.setDuration(450);
        return retn;
    }

    /**
     * 设置释放动画 当下拉释放后执行的动画
     * @return
     */
    @Override
    public ObjectAnimator getAnimator() {
        ObjectAnimator retn =ObjectAnimator.ofInt(this, "ss", 0, 100);
        retn.setDuration(450);
        retn.setInterpolator(new EasingInterpolator(Ease.BOUNCE_OUT));
        return retn;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        int w = getWidth();
        mPath.reset();
        mPaint.setAlpha(255);
        mPaint.setStyle(Paint.Style.FILL);  //设置填充样式
        mPaint.setStrokeWidth(lineStrokWidth);  //设置线条宽度
        mPaint.setColor(lcolor);   //设置背景颜色


        float bith = (w * 1f) / topbitmap.getWidth() * topbitmap.getHeight();
        float nowh = barH - bith;

        float bitah = (w * 1f) / topbitmap.getWidth() * topbitmap.getHeight();

        canvas.drawBitmap(allbitmap, new Rect(0, 0, allbitmap.getWidth(), allbitmap.getHeight()), new Rect(0, (int) (over), w, (int) (over + (w * 1f) / allbitmap.getWidth() * allbitmap.getHeight())), mPaint);

        canvas.drawBitmap(allbitmap, new Rect(0, 0, allbitmap.getWidth(), allbitmap.getHeight()), new Rect(0, (int) (over), w, (int) (over + (w * 1f) / allbitmap.getWidth() * allbitmap.getHeight())), mPaint);

        if (barH < over) {
            //设置背景块
            canvas.drawRect(0, 0, getWidth(), barH, mPaint);  //填充背景

            //画下边的边
            mPaint.setColor(scolor);

            canvas.drawBitmap(topbitmap, new Rect(0, 0, topbitmap.getWidth(), topbitmap.getHeight()), new Rect(0, (int) nowh, w, (int) (nowh + bith)), mPaint);

            canvas.drawLine(0, barH - circlyStroke, getWidth(), barH, mPaint);
        } else {
            //画上部分背景
            canvas.drawRect(0, 0, getWidth(), over, mPaint);

            nowh = over - bith;
            canvas.drawBitmap(topbitmap, new Rect(0, 0, topbitmap.getWidth(), topbitmap.getHeight()), new Rect(0, (int) nowh, w, (int) (nowh + bith)), mPaint);

            //画背景色
            mPaint.setColor(lcolor);
            mPaint.setStyle(Paint.Style.FILL);
            mPath.reset();
            mPath.moveTo(0, over);
            mPath.rCubicTo(barT, barH - over, barT, barH - over, getWidth(), 0);
            canvas.drawPath(mPath, mPaint);

            //画曲线的边
            mPaint.setColor(scolor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPath.reset();
            mPath.moveTo(0, over);
            mPath.rCubicTo(barT, barH - over, barT, barH - over, getWidth(), 0);
            canvas.drawPath(mPath, mPaint);

            canvas.save();
            mPaint.setColor(0x00000000);
            mPaint.setStyle(Paint.Style.FILL);
            mPath.reset();
            mPath.moveTo(0, over);
            mPath.rCubicTo(barT, barH - over, barT, barH - over, getWidth(), 0);
            canvas.clipPath(mPath);
            mPaint.setColor(0xffffffff);
            //画bitmap
            canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(0, (int) (over), w, (int) (over + (w * 1f) / bitmap.getWidth() * bitmap.getHeight())), mPaint);

            canvas.restore();
        }

        /*******************************************************显示文字*******************************************************/
        String str = "";
        switch (state) {
            case SwipRefreshView.SRV_STATE_PULL:
                str = state_a;
                break;
            case SwipRefreshView.SRV_STATE_RELR:
                str = state_b;
                break;
            case SwipRefreshView.SRV_STATE_REFI:
                str = state_c;
                break;
            case SwipRefreshView.SRV_STATE_REFE:
                str = state_d;
                break;
        }

        mPaint.setColor(tcolor);
        mPaint.setTextSize(textSize);
        float ty = barH < over ? barH : over;
        mPaint.setAlpha((int) (ty / over * 255));

        mPaint.setStyle(Paint.Style.FILL);
        Rect bounds = new Rect();


        mPaint.getTextBounds(str, 0, str.length(), bounds);
        int height = bounds.bottom + bounds.height();
        float textLength = mPaint.measureText(str);
        canvas.drawText(str, getWidth() / 2 - textLength / 2, ty - (ty / over) * (over / 2 - height), mPaint);


        /*******************************************************显示结束*******************************************************/

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(bcolor);  //设置动画颜色
        mPaint.setStrokeWidth(circlyStroke);

        if (state == SwipRefreshView.SRV_STATE_PULL || state == SwipRefreshView.SRV_STATE_RELR) {
            lineW = getWidth() - ((getWidth() / 4f * 3f) / (over * 1f)) * barH;
            if (barH > 0 && barH <= over) {
                canvas.drawLine(0, loffsetY, lineW, loffsetY, mPaint);
            } else if (barH > over) {
                lineW = getWidth() - (getWidth() / 4f * 3f) - ((getWidth() / 4f * 3f) / (over * 1f)) * (barH - over) / 15;
                if (barH > 0) {
                    canvas.drawLine(0, loffsetY, lineW, loffsetY, mPaint);
                }
            }
        }
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(circlyStroke);
        if (status == 0 && (state == SwipRefreshView.SRV_STATE_REFI || state == SwipRefreshView.SRV_STATE_REFE)) {
            w = w / 2;

            float sw = (w / 340f) * (loadC - w);
            float nw = loadC > w ? w : loadC;
            lineW = nw;
            if (sw < nw) {
                mPaint.setStrokeWidth(circlyStroke);
                canvas.drawLine(sw < 0 ? 0 : sw, loffsetY, nw + 1, loffsetY, mPaint);
            }
            mPaint.setStrokeWidth(circlyStroke);

            if (sw > 0) {
                getArc(canvas, w, circlyw + offsetY, circlyw, -90, (loadC - w), mPaint, circlyStroke);
                if (mouth == 2) {
                    getArc(canvas, w, circlyw + offsetY, monthCirclyw, -90 + (145 / 340f) * (loadC - w), 70 / 340f * (loadC - w), mPaint, circlyStroke);
                }
            }
        }
        if (status == 1) {

            if (System.currentTimeMillis() - loadtime > 8000) {
                mouth = 0;
            } else if (System.currentTimeMillis() - loadtime > 3000) {
                mouth = 1;
            }

            w = w / 2;
            getArc(canvas, w, circlyw + offsetY, circlyw, -90 + circlyAng, 340f, mPaint, circlyStroke);


            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(circlyStroke);
            if (mouth == 0) {
                mPaint.setStrokeWidth(circlyStroke);
                float lw = circlyw + offsetY + monthCirclyw * 2 - monthCirclyw / 4;
                getArc(canvas, w, lw, monthCirclyw, -90 - monthAng / 2, monthAng, mPaint, circlyStroke);
            } else if (mouth == 1) {
                mPaint.setStrokeWidth(circlyStroke);
                float lw = offsetY - monthCirclyw + circlyw * 2 + monthCirclyw / 4;
                canvas.drawLine(w - monthAng / 5, lw, w + monthAng / 5, lw, mPaint);
            } else if (mouth == 2) {
                getArc(canvas, w, circlyw + offsetY, monthCirclyw, 90 - monthAng / 2, monthAng, mPaint, circlyStroke);
            }
        }
    }

    private boolean isinit = false;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isinit) {
            isinit = false;
            startAmin();
        }
    }

    public void startAmin() {
        if (getWidth() == 0) {
            isinit = true;
            return;
        }
        if (fobjectAnimator == null) {
            final int size = (getWidth() / 2) + 340;
            fobjectAnimator = ObjectAnimator.ofInt(this, "ss", (int) lineW, size);
            fobjectAnimator.setDuration(500);
            fobjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int v = (int) animation.getAnimatedValue();
                    loadC = v;
                    invalidate();
                    if (v == size) {
                        status = 1;
                        loadingAmin();
                    }
                }
            });
        }
        fobjectAnimator.setIntValues((int) lineW, (int) (getWidth() / 2) + 340);
        fobjectAnimator.start();
    }

    public void endAmin() {
        if (eobjectAnimator == null) {
            eobjectAnimator = ObjectAnimator.ofInt(this, "ss", (int) (getWidth() / 2) + 340, 0);
            eobjectAnimator.setDuration(500);
            eobjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int v = (int) animation.getAnimatedValue();
                    loadC = v;
                    invalidate();
                }
            });
        }
        eobjectAnimator.setIntValues((int) (getWidth() / 2) + 340, 0);
        eobjectAnimator.start();
    }


    public void loadingAmin() {
        // bar
        if (robjectAnimator == null) {
            robjectAnimator = ObjectAnimator.ofInt(this, "ss", 0, 360);
            robjectAnimator.setRepeatMode(ObjectAnimator.RESTART);
            robjectAnimator.setRepeatCount(-1);
            robjectAnimator.setDuration(800);
            robjectAnimator.setInterpolator(new LinearInterpolator());
            robjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int v = (int) animation.getAnimatedValue();
                    circlyAng = v;
                    invalidate();
                }
            });
        }
        robjectAnimator.start();
    }


    public void getArc(Canvas canvas, float o_x, float o_y, float r, float startangel, float angel, Paint paint, float width) {
        mPaint.setStrokeWidth(width);
        RectF oval = new RectF();                     //RectF对象
        float fs = r - width / 2;
        oval.left = o_x - fs;                              //左边
        oval.top = o_y - fs;                                   //上边
        oval.right = o_x + fs;                             //右边
        oval.bottom = o_y + fs;                                //下边
        canvas.drawArc(oval, startangel, angel, false, paint);    //绘制圆弧
    }
}
