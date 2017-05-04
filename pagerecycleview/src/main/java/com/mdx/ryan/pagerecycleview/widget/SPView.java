package com.mdx.ryan.pagerecycleview.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.mdx.ryan.pagerecycleview.R;


/**
 * Created by ryan on 2016/4/29.
 */
public class SPView extends View implements SwipRefresh {

    private Paint mPaint;
    private Path mPath;
    //笑脸    0
    private int mouth = 2;
    public int state = 1, status = 0;
    private int lcolor = 0xffffffff/*背景颜色*/, scolor = 0xffdcdcdc/*线条颜色*/, bcolor = 0xffff0000 /*动画颜色*/, tcolor = 0xffdcdcdc /*文字颜色*/;
    private ObjectAnimator fobjectAnimator, robjectAnimator, eobjectAnimator;
    private long loadtime = 0;
    private float lineStrokWidth = 1 /*边线宽度*/, lineStroke = 10 /*移动线条宽度*/, circlyStroke = 10 /*圆的宽度*/, circlyw = 50/*园的半径*/, monthAng = 0, monthCirclyw = 0, offsetY = 0/*顶部偏移量*/, loffsetY = offsetY + circlyStroke / 4;
    private float loadC = 0/*线条长度*/, circlyAng = 0 /*圆角度*/, lineW = 0/*当前线长度*/, over = 300/*超出高度*/, barH/*滑动高度*/, barT/*触摸位置*/, textSize = 50/*文字大小*/;
    private String state_a, state_b, state_c, state_d;
    public static Bitmap bitmap,topbitmap,allbitmap;

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
        mPath = new Path();

        lineStrokWidth = context.getResources().getDimension(R.dimen.sp_linestrok_width);
        lineStroke = context.getResources().getDimension(R.dimen.sp_linestrok);
        circlyStroke = context.getResources().getDimension(R.dimen.sp_circlystrok);
        circlyw = context.getResources().getDimension(R.dimen.sp_circly_width);
        offsetY = context.getResources().getDimension(R.dimen.sp_offsetY);
        loffsetY = offsetY + circlyStroke / 4;
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
            topbitmap=((BitmapDrawable) context.getResources().getDrawable(R.mipmap.fw_pull_top_n)).getBitmap();
            allbitmap=((BitmapDrawable) context.getResources().getDrawable(R.mipmap.fw_pull_all_n)).getBitmap();
        }
    }


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

    @Override
    public void setH(float mv, float mt) {
        this.barH = mv;
        this.barT = mt;
        invalidate();
    }

    @Override
    public void setW(float mv, float mt) {
        this.barH = mv;
        this.barT = mt;
        invalidate();
    }

    @Override
    public void setState(int state) {
        if (state == SwipRefreshView.SRV_STATE_REFI && this.state != SwipRefreshView.SRV_STATE_REFI) {
            loadtime = System.currentTimeMillis();
            startAmin();
        } else if (state == SwipRefreshView.SRV_STATE_REFE) {
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
    public void setLinw(float over) {
        lineW = over;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        mPath.reset();
        mPaint.setAlpha(255);
        mPaint.setStyle(Paint.Style.FILL);  //设置填充样式
        mPaint.setStrokeWidth(lineStrokWidth);  //设置线条宽度
        mPaint.setColor(lcolor);   //设置背景颜色

        float bith=(w * 1f) / topbitmap.getWidth() * topbitmap.getHeight();
        float nowh=barH-bith;

        float bitah=(w * 1f) / topbitmap.getWidth() * topbitmap.getHeight();

        canvas.drawBitmap(allbitmap, new Rect(0, 0, allbitmap.getWidth(), allbitmap.getHeight()), new Rect(0, (int) (over), w, (int) (over + (w * 1f) / allbitmap.getWidth() * allbitmap.getHeight())), mPaint);

        canvas.drawBitmap(allbitmap, new Rect(0, 0, allbitmap.getWidth(), allbitmap.getHeight()), new Rect(0, (int) (over), w, (int) (over + (w * 1f) / allbitmap.getWidth() * allbitmap.getHeight())), mPaint);

        if (barH < over) {
            //设置背景块
            canvas.drawRect(0, 0, getWidth(), barH, mPaint);  //填充背景

            //画下边的边
            mPaint.setColor(scolor);





            canvas.drawBitmap(topbitmap, new Rect(0, 0, topbitmap.getWidth(), topbitmap.getHeight()), new Rect(0, (int)nowh, w, (int) (nowh + bith)), mPaint);

            canvas.drawLine(0, barH - lineStrokWidth, getWidth(), barH, mPaint);
        } else {
            //画上部分背景
            canvas.drawRect(0, 0, getWidth(), over, mPaint);

            nowh=over-bith;
            canvas.drawBitmap(topbitmap, new Rect(0, 0, topbitmap.getWidth(), topbitmap.getHeight()), new Rect(0, (int)nowh, w, (int) (nowh + bith)), mPaint);

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
        mPaint.setStrokeWidth(lineStroke / 2);
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
                mPaint.setStrokeWidth(circlyStroke / 2);
                canvas.drawLine(sw < 0 ? 0 : sw, loffsetY, nw, loffsetY, mPaint);
            }
            mPaint.setStrokeWidth(circlyStroke);

            if (sw > 0) {
                getArc(canvas, w, circlyw + offsetY, circlyw, -90, (loadC - w), mPaint);
                if (mouth == 2) {
                    getArc(canvas, w, circlyw + offsetY, monthCirclyw, -90 + (145 / 340f) * (loadC - w), 70 / 340f * (loadC - w), mPaint);
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
            getArc(canvas, w, circlyw + offsetY, circlyw, -90 + circlyAng, 340f, mPaint);


            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(circlyStroke);
            if (mouth == 0) {
                mPaint.setStrokeWidth(circlyStroke);
                float lw = circlyw + offsetY + monthCirclyw * 2 - monthCirclyw / 4;
                getArc(canvas, w, lw, monthCirclyw, -90 - monthAng / 2, monthAng, mPaint);
            } else if (mouth == 1) {
                mPaint.setStrokeWidth(circlyStroke / 2);
                float lw = offsetY - monthCirclyw + circlyw * 2 + monthCirclyw / 4;
                canvas.drawLine(w - monthAng / 5, lw, w + monthAng / 5, lw, mPaint);
            } else if (mouth == 2) {
                mPaint.setStrokeWidth(circlyStroke);
                getArc(canvas, w, circlyw + offsetY, monthCirclyw, 90 - monthAng / 2, monthAng, mPaint);
            }
        }
    }

    private void showbitmap(Canvas canvas,Bitmap bitmap,Paint paint,float top,float left,int type){

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


    public void getArc(Canvas canvas, float o_x, float o_y, float r, float startangel, float angel, Paint paint) {
        canvas.save();
        RectF rect = new RectF(o_x - r, o_y - r, o_x + r, o_y + r);
        Path path = new Path();
        path.moveTo(o_x, o_y);
        float endangel = startangel + angel;
        path.lineTo((float) (o_x + r * Math.cos(startangel * Math.PI / 180)), (float) (o_y + r * Math.sin(startangel * Math.PI / 180)));
        path.lineTo((float) (o_x + r * Math.cos(endangel * Math.PI / 180)), (float) (o_y + r * Math.sin(endangel * Math.PI / 180)));
        path.addArc(rect, startangel, angel);
        canvas.clipPath(path);
        canvas.drawCircle(o_x, o_y, r, paint);
        canvas.restore();
    }
}
