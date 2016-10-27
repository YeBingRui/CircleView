package com.example.youngkaaa.ycircleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by : youngkaaa on 2016/10/12.
 * Contact me : 645326280@qq.com
 */

public class CircleView extends View {
    private static final String TAG = "CircleView";

    private Context mContext;
    private int viewWidth;
    private int bitmapWidth;
    private BitmapShader bitmapShader;
    private Paint paint;
    private Matrix matrix;
    private int imgSrc;
    private Bitmap bitmap;
    private float scaleRatio;
    private int radius;
    private float leftPadding;
    private float topPadding;
    private ImageSizer imageSizer;

    public CircleView(Context context) {
        super(context);
        mContext=context;
        getValue(null);
        inits();
    }


    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        getValue(attrs);
        inits();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        getValue(attrs);
        inits();
    }

    private void getValue(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CircleView);
        imgSrc = typedArray.getResourceId(R.styleable.CircleView_imgSrc, -1);
        leftPadding = typedArray.getDimension(R.styleable.CircleView_leftPadding, -1);
        topPadding = typedArray.getDimension(R.styleable.CircleView_topPadding, -1);
        Log.d("kklog", "img =====>" + imgSrc);
        Log.d("kklog", "leftPadding =====>" + leftPadding);
        Log.d("kklog", "topPadding =====>" + topPadding);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        Log.d(TAG,"width==>"+width);
        Log.d(TAG,"height==>"+height);

        viewWidth = Math.min(width, height);    //set the view's height and width were equal forced!!
        setMeasuredDimension(viewWidth, viewWidth);
        radius = viewWidth / 2;
        Log.d(TAG,"viewWidth==>"+viewWidth);
//        if (imgSrc != -1) {
////            bitmap= imageSizer.decodeBitmapFromResource(mContext.getResources(),imgSrc,viewWidth,viewWidth);
//            bitmap = BitmapFactory.decodeResource(getResources(), imgSrc);
//        }
        Log.d(TAG,"##############onMeasure end##############");
    }

    public void setImgSrc(@DrawableRes int imgSrc) {
        this.imgSrc = imgSrc;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (imgSrc != -1) {
            bitmap = BitmapFactory.decodeResource(getResources(), imgSrc);
        }else{
            throw new IllegalArgumentException("not't set img resource");
        }
        //避免在onDraw()中new对象而降低效率，所以放在onLayout()中
        //因为invalidate()方法只会从onDraw()之后往下执行，不会再次执行该方法
        //而requestLayout()会从头开始执行！
        if (bitmap.getWidth() < bitmap.getHeight() && leftPadding != -1) {
            throw new IllegalArgumentException("you can't set leftPadding when img's width<height");
        } else if (bitmap.getWidth() > bitmap.getHeight() && topPadding != -1) {
            throw new IllegalArgumentException("you can't set topPadding when img's width>height");
        }
    }

    private void inits() {
        imageSizer=new ImageSizer();
        paint = new Paint();
        matrix = new Matrix();
        paint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        bitmapWidth = Math.min(bitmap.getWidth(), bitmap.getHeight());

        scaleRatio = viewWidth * 1.0f / bitmapWidth;   //计算图片缩放比例
        if (leftPadding != -1) {
            if (leftPadding + 2 * radius > bitmap.getWidth()) {
                throw new IllegalArgumentException("leftPadding is too large");
            } else {
                bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            }
        } else if (topPadding != -1) {
            if (topPadding + 2 * radius > bitmap.getHeight()) {
                throw new IllegalArgumentException("topPadding is too large");
            } else {
                bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            }
        } else {
            bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        }
        matrix.setScale(scaleRatio, scaleRatio);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        canvas.drawCircle(radius,radius,radius,paint);   //copy circle to bitmapCanvas
    }


    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int result = 0;
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 1000;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(size, result);
            }
        }
        return result;
    }


    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int result = 0;
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 1000;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(size, result);
            }
        }
        return result;
    }
}
