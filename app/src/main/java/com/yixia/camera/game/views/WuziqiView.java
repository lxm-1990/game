package com.yixia.camera.game.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.yixia.camera.game.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxm on 17/2/20.
 */

public class WuziqiView  extends View{

    private int mPanelWidth;
    private float mLineHeight;
    private int MAX_LINE = 10;
    private Paint mPaint = new Paint();

    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    private float radioPieceOfLineHight = 3*1.0f/4;
    private ArrayList<Point> mWhiteArray = new ArrayList<>();
    private ArrayList<Point> mBlackArray = new ArrayList<>();
    private boolean mIsWhite = true;
    private boolean mIsGameOver = false;
    private int MAX_COUNT_IN_LINE = 5;
    private GameListener mGameOver;

    public WuziqiView(Context context) {
        this(context,null);
    }

    public WuziqiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setBackgroundColor(0x44ff0000); //设置一个背景
        init();
    }

    public GameListener getmGameOver() {
        return mGameOver;
    }

    public void setmGameOver(GameListener mGameOver) {
        this.mGameOver = mGameOver;
    }

    public void reStart(){
        mIsGameOver = false;
        mWhiteArray.clear();
        mBlackArray.clear();
        invalidate();
    }

    private void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize,heightSize);

        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if(heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width,width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //设置一些和尺寸相关的配置

        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;
        int pieceWidth = (int)(mLineHeight * radioPieceOfLineHight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece,pieceWidth,pieceWidth,false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece,pieceWidth,pieceWidth,false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPieces(canvas);
        checkGameOver();
    }

    private void checkGameOver() {
        boolean whiteWin = checkFiveInLine(mWhiteArray);
        boolean blackWin = checkFiveInLine(mBlackArray);
        if (whiteWin || blackWin) {
            mIsGameOver = true;
            String text = whiteWin ? "白棋获胜":"黑棋获胜";
            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
            if (mGameOver != null) {
                mGameOver.gameOver();
            }
        }
    }

    private boolean checkFiveInLine(List<Point> points) {
        for (Point p : points) {
            int x = p.x;
            int y = p.y;
            if (checkHorizontal(x,y,points)){
                return true;
            }
            if (checkVertical(x,y,points)) {
                return true;
            }
            if (checkLeftDiagonal(x,y,points)){
                return true;
            }
            if (checkRightDiagonal(x,y,points)){
                return true;
            }
        }
        return false;
    }

    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if (points.contains(new Point(x - i ,y))) {
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE){
            return true;
        }
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if (points.contains(new Point(x + i ,y))) {
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE){
            return true;
        }
        return false;
    }

    private boolean checkVertical(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if (points.contains(new Point(x ,y-i))) {
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE){
            return true;
        }
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if (points.contains(new Point(x ,y + i))) {
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE){
            return true;
        }
        return false;
    }

    private boolean checkLeftDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if (points.contains(new Point(x - i ,y + i))) {
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE){
            return true;
        }
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if (points.contains(new Point(x + i ,y - i))) {
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE){
            return true;
        }
        return false;
    }

    private boolean checkRightDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if (points.contains(new Point(x - i ,y - i))) {
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE){
            return true;
        }
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if (points.contains(new Point(x + i ,y + i))) {
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE){
            return true;
        }
        return false;
    }

    private void drawPieces(Canvas canvas) {
        for (int i = 0,n = mWhiteArray.size();i<n;i++){
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece
                    ,(whitePoint.x + (1-radioPieceOfLineHight)/2)*mLineHeight
                    ,(whitePoint.y + (1-radioPieceOfLineHight)/2)*mLineHeight,null);
        }

        for (int i = 0,n = mBlackArray.size();i<n;i++){
            Point blackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece
                    ,(blackPoint.x + (1-radioPieceOfLineHight)/2)*mLineHeight
                    ,(blackPoint.y + (1-radioPieceOfLineHight)/2)*mLineHeight,null);
        }
    }

    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;

        for (int i = 0 ; i < MAX_LINE; i++){
            int startX = (int)(lineHeight / 2);
            int endX = (int)(w - lineHeight / 2);
            int y = (int)((0.5 + i) * lineHeight);
            canvas.drawLine(startX,y,endX,y,mPaint);
            canvas.drawLine(y,startX,y,endX,mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsGameOver) {
            return false;
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {

            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = getVaildPoint(x, y);
            if (mWhiteArray.contains(p) || mBlackArray.contains(p)) {
                return false;
            }
            if (mIsWhite) {
                mWhiteArray.add(p);
                mIsWhite = false;
            } else {
                mBlackArray.add(p);
                mIsWhite = true;
            }
            invalidate();
        }
        return true;
    }

    private Point getVaildPoint(int x, int y) {
        return new Point((int)(x / mLineHeight) ,(int)(y /mLineHeight));
    }

    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";

    @Override
    protected Parcelable onSaveInstanceState() {
        //储存白字，黑子的array
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER,mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY,mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY,mBlackArray);
        return bundle;

    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle){
            Bundle bundle = (Bundle)state;
            mIsGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    public interface GameListener {
        void gameOver();
    }
}
