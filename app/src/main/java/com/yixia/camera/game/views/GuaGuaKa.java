package com.yixia.camera.game.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Switch;

import com.yixia.camera.game.R;

import static android.R.attr.bitmap;

/**
 * Created by lxm on 17/2/20.
 */

public class GuaGuaKa extends View{

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mOutterPaint;
    private int mLastX;
    private int mLastY;
    private Path mPath;
    private Bitmap mOutterBitmap;
    private Paint mBackPaint;
    private Rect mTextBound;
    private String mText;
    private int mTextSize;
    private int mTextColor;
    private volatile boolean mCompelete = false;
    private OnGuaguaKaCompleteListener mListener;

    public void setmListener(OnGuaguaKaCompleteListener mListener) {
        this.mListener = mListener;
    }

    public GuaGuaKa(Context context) {
        this(context,null);
    }

    public GuaGuaKa(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GuaGuaKa(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.GuaGuaKa,defStyleAttr,0);
        int n = a.getIndexCount();
        try {
            for (int i= 0;i<n;i++){
                int attr = a.getIndex(i);
                switch (attr){
                    case R.styleable.GuaGuaKa_text:
                        mText = a.getString(attr);
                        break;
                    case R.styleable.GuaGuaKa_textColor:
                        mTextColor = a.getColor(attr,0x000000);
                        break;
                    case R.styleable.GuaGuaKa_textSize:
                        mTextSize = (int) a.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,22,getResources().getDisplayMetrics()));
                        break;
                }
            }
        } finally {
            a.recycle();
        }
    }
    //进行一些初始化操作
    private void init() {
        mPath = new Path();
        mOutterPaint = new Paint();

        mBackPaint = new Paint();
        mText = "谢谢惠顾";
        mTextBound = new Rect();
        mTextSize = 40;
        mOutterBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.panel_bg);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        setupOutPaint();
        setupBackPaint();
        //mCanvas.drawColor(Color.parseColor("#c0c0c0"));
        mCanvas.drawRoundRect(new RectF(0,0,width,height),30,30,mOutterPaint);
        mCanvas.drawBitmap(mOutterBitmap,null,new Rect(0,0,width,height),null);
    }

    private void setupBackPaint() {
        mBackPaint.setColor(mTextColor);
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setTextSize(mTextSize);
        //获得当前画笔绘制的宽和高
        mBackPaint.getTextBounds(mText,0,mText.length(),mTextBound);
    }

    private void setupOutPaint() {
        //设置绘制path画笔的一些属性
        mOutterPaint.setColor(Color.parseColor("#c0c0c0"));
        mOutterPaint.setAntiAlias(true);
        mOutterPaint.setDither(true);
        mOutterPaint.setStrokeJoin(Paint.Join.ROUND);
        mOutterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOutterPaint.setStyle(Paint.Style.FILL);
        mOutterPaint.setStrokeWidth(20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawBitmap(bitmap,0,0,null);
        canvas.drawText(mText,getWidth()/2-mTextBound.width()/2
                ,getHeight()/2+mTextBound.height()/2
                ,mBackPaint);
        if (mCompelete){
            if (mListener != null){
                mListener.complete();
            }
        }
        if (!mCompelete) {
            drawPath();
            canvas.drawBitmap(mBitmap,0,0,null);
        }
        //super.onDraw(canvas);
    }

    private void drawPath() {
        mOutterPaint.setStyle(Paint.Style.STROKE);
        mOutterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(mPath,mOutterPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mPath.moveTo(mLastX,mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = Math.abs(x - mLastX);
                int dy = Math.abs(y - mLastY);
                if (dx > 3 || dy>3){
                    mPath.lineTo(x,y);
                }
                mLastX = x;
                mLastY = x;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //开启一个异步任务
                new Thread(mRunnable).start();
                break;
        }
        invalidate();
        return true;
    }
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int w = getWidth();
            int h = getHeight();
            float wipeArea = 0;
            float totalArea = w * h;
            Bitmap bitmap = mBitmap;
            int[] mPixels = new int[w*h];
            //获得bitmap上所有像素的信息
            mBitmap.getPixels(mPixels,0,w,0,0,w,h);
            for (int i = 0; i < w; i++){
                for (int j = 0; j<h;j++){
                    int index = i+j*w;
                    if (mPixels[index]==0){
                        wipeArea++;
                    }
                }
            }
            if (wipeArea > 0 && totalArea >0){
                int percent = (int) (wipeArea*100/totalArea);
                if (percent > 60) {
                    //清楚涂层区域
                    mCompelete = true;
                    postInvalidate();
                }
            }
        }
    };
    public interface OnGuaguaKaCompleteListener {
        void complete();
    }
    public void setText(String text){
        this.mText = text;
        mBackPaint.getTextBounds(mText,0,mText.length(),mTextBound);
    }
}
