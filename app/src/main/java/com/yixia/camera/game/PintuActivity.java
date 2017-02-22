package com.yixia.camera.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class PintuActivity extends AppCompatActivity {

    private GridLayout mGridLayout;
    private ImageView[][] imageViews = new ImageView[3][5];
    private ImageView null_image_view;
    private GestureDetector detector;
    private ImageView mImageView;
    private boolean isGameStart = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pintu);

        detector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                int type = getDir(motionEvent.getX(),motionEvent1.getX(),motionEvent.getY(),motionEvent1.getY());
                changeDir(type);
                return true;
            }
        });
        initView();
        randomMove();
        isGameStart = true;
    }

    //随机打乱顺序
    private void randomMove(){
        for (int i = 0;i < 10;++i) {
            int type = (int)(Math.random()*4) + 1;
            changeDir(type,false);
        }
    }
    private int getDir(float startX,float endX,float startY,float endY){
        boolean isLeftOrRight = Math.abs(startX-endX)>Math.abs(startY-endY) ? true : false;
        int type = 0;
        if (isLeftOrRight){
            boolean isLeft = startX - endX > 0 ? true:false;
            if (isLeft) {
                type = 3;
            } else {
                type = 4;
            }
        } else {
            boolean isUp = startY - endY > 0 ? true : false;
            if (isUp) {
                type = 1;
            } else {
                type = 2;
            }
        }
        return type;
    }

    private void changeDir(int type){
       changeDir(type,true);
    }
    private void changeDir(int type,boolean isAnimal){
        ImageData null_data = (ImageData) null_image_view.getTag();
        int new_x = null_data.x;
        int new_y = null_data.y;
        if (type == 1) {
            new_x++;
        }else if (type == 2){
            new_x--;
        } else if (type == 3){
            new_y++;
        } else {
            new_y--;
        }
        if (new_x >= 0 && new_x <=imageViews.length-1 && new_y >=0 && new_y <=imageViews[0].length-1){
            if (isAnimal) {
                changeDataByImage(imageViews[new_x][new_y]);
            } else {
                changeDataByImage(imageViews[new_x][new_y],false);
            }
        }
    }

    private void initView() {

        mGridLayout = (GridLayout) findViewById(R.id.gridLayout);
        mGridLayout.setColumnCount(imageViews[0].length);
        mGridLayout.setRowCount(imageViews.length);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setImageBitmap(decodeSampledBitmapFromResouce(getResources(),R.drawable.image,400,400));
        int screen_width = getResources().getDisplayMetrics().widthPixels;
        Bitmap bigMap = decodeSampledBitmapFromResouce(getResources(),R.drawable.image,screen_width,imageViews.length/imageViews[0].length*screen_width);
        int width = bigMap.getWidth()/imageViews[0].length;
        for (int i = 0; i < imageViews.length;++i) {
            for (int j = 0;j < imageViews[0].length;++j) {
                imageViews[i][j] = new ImageView(this);
                Bitmap bm = Bitmap.createBitmap(bigMap,j*width,i*width,width,width);
                imageViews[i][j].setImageBitmap(bm);
                imageViews[i][j].setPadding(2,2,2,2);
                imageViews[i][j].setTag(new ImageData(bm,i,j));
                mGridLayout.addView(imageViews[i][j]);
                imageViews[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view != null_image_view) {
                            boolean isHave = isHasNullInThisImageView((ImageView) view);
                            if (isHave) {
                                changeDataByImage((ImageView) view);
                            }
                        }
                    }
                });
            }
        }
        SetNullImageView(imageViews[0][0]);
    }

    //设置某个方块为空方块
    public  void SetNullImageView(ImageView imageview){
        imageview.setImageBitmap(null);
        null_image_view = imageview;
    }

    public Bitmap decodeSampledBitmapFromResouce(Resources res,int resId,int reqWidth,int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,resId,options);
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res,resId,options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSimpleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height/2;
            final int halfWidth = width/2;
            while ((halfHeight/inSimpleSize) >= reqHeight
                    && (halfWidth/inSimpleSize)>=reqWidth){
                inSimpleSize *= 2;
            }
        }
        return inSimpleSize;
    }
    private boolean isHasNullInThisImageView(ImageView imageview){
        ImageData null_data = (ImageData) null_image_view.getTag();
        ImageData imag_data = (ImageData) imageview.getTag();
        if (null_data.x  == imag_data.x -1 && null_data.y == imag_data.y){
            //空白方块在点击方块的上边
            return true;
        } else if (null_data.x == imag_data.x+1 && null_data.y == imag_data.y) {
            //空白方块在点击方块的下边
            return true;
        } else if (null_data.x == imag_data.x && null_data.y == imag_data.y - 1) {
            //空白方块在点击方块的左边
            return true;
        } else if (null_data.x == imag_data.x && null_data.y == imag_data.y + 1) {
            //空白方块在点击方块的右边
            return true;
        }
        return false;
    }

    public void changeDataByImage(final ImageView imageView){
        changeDataByImage(imageView,true);
    }
    public void changeDataByImage(final ImageView imageView,boolean isAnimal){

        if (!isAnimal) {
            //交换数据
            ImageData cur_data = (ImageData) imageView.getTag();
            ImageData null_data = (ImageData) null_image_view.getTag();
            null_image_view.setImageBitmap(cur_data.bp);
            null_data.bp = cur_data.bp;
            null_data.p_x = cur_data.p_x;
            null_data.p_y = cur_data.p_y;
            SetNullImageView(imageView);
            isGameOver();
            return;
        }

        TranslateAnimation translateAnimation = null;
        if (imageView.getX() > null_image_view.getX()){
            translateAnimation = new TranslateAnimation(0.1f,-imageView.getWidth(),0.1f,0.1f);
        } else if (imageView.getX()<null_image_view.getX()){
            translateAnimation = new TranslateAnimation(0.1f,imageView.getWidth(),0.1f,0.1f);
        } else if (imageView.getY() > null_image_view.getY()){
            translateAnimation = new TranslateAnimation(0.1f,0.1f,0.1f,-imageView.getWidth());
        } else {
            translateAnimation = new TranslateAnimation(0.1f,0.1f,0.1f,imageView.getWidth());
        }
        translateAnimation.setDuration(70);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.clearAnimation();
                //交换数据
                ImageData cur_data = (ImageData) imageView.getTag();
                ImageData null_data = (ImageData) null_image_view.getTag();
                null_image_view.setImageBitmap(cur_data.bp);
                null_data.bp = cur_data.bp;
                null_data.p_x = cur_data.p_x;
                null_data.p_y = cur_data.p_y;
                SetNullImageView(imageView);
                isGameOver();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(translateAnimation);
    }

    private void isGameOver(){
        if (!isGameStart) {
            return;
        }
        boolean isGameOver = true;
        for (int i = 0;i<imageViews.length;++i) {
            for (int j = 0;j<imageViews[0].length;++j) {
                if (null_image_view == imageViews[i][j]){
                    continue;
                }
                ImageData data = (ImageData) imageViews[i][j].getTag();
                if (!data.isTrue()){
                    isGameOver = false;
                    break;
                }
            }
        }
        if (isGameOver) {
            Toast.makeText(this,"游戏结束",Toast.LENGTH_SHORT).show();
        }
    }
    class ImageData{
        public int x;
        public int y;
        public Bitmap bp;
        public int p_x;
        public int p_y;


        public ImageData(Bitmap bp, int x, int y) {
            this.bp = bp;
            this.p_x = x;
            this.p_y = y;
            this.x = x;
            this.y = y;
        }

        public boolean isTrue(){
            return (x==p_x) && (y==p_y);
        }
    }
}
