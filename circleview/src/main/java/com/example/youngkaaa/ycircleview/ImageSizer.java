package com.example.youngkaaa.ycircleview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

import java.io.FileDescriptor;

/**
 * Created by : youngkaaa on 2016/10/18.
 * Contact me : 645326280@qq.com
 */

public class ImageSizer {
    private static final String TAG="ImageSizer";

    //空的构造方法
    public ImageSizer(){}

    /**
     * 通过图片资源id来加载图片
     *
     * @param resources   Resources资源，用来获得图片资源
     * @param resId      资源id，结合上面的两者一起用来获得图片资源
     * @param finalWidth   最终所需的宽度
     * @param finalHeight  最终所需的高度
     * @return  已经缩放为最终宽度、高度的位图
     */
    public Bitmap decodeBitmapFromResource(Resources resources,@DrawableRes int resId,
                                           int finalWidth,int finalHeight){
        //先新建一个BitmapFactory.Options对象
        BitmapFactory.Options options=new BitmapFactory.Options();
        //该属性开启时，BitmapFactory.decodeResource 这句就只会加载图片的宽和高，
        // 而不会加载内容，所以说这步不会花费太多时间
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(resources,resId,options);
        options.inSampleSize=calculateInSampleSize(options,finalWidth,finalHeight);
        options.inJustDecodeBounds=false;
        //该属关闭时，BitmapFactory.decodeResource 就会加载图片的所有资源了，
        //具体加载的属性（宽高）都由传入的options来决定
        //而决定宽高的因素就是采样率inSampleSize,具体不解释请查阅相关资料
        return BitmapFactory.decodeResource(resources,resId,options);
    }

    /**
     * 计算inSampleSize
     *
     * @param options 通过该参数获得原图片的宽高信息
     * @param width  要缩放的最终宽度
     * @param height  要缩放的最终高度
     * @return   返回计算好的inSampleSize
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int width, int height) {
        int outWidth=options.outWidth;
        int outHeight=options.outHeight;
        int inSampleSize=1;
        //如果其中一个大于就都缩小
        if(outWidth>width || outHeight>height){
            int tempWidth=outWidth/2;
            int tempHeight=outHeight/2;
            //如果现在的(tempWidth/inSampleSize)仍然大于最终所需的宽度（高度同理）
            while ((tempWidth/inSampleSize)>=width
                    && (tempHeight/inSampleSize)>=height){
                //满足2的指数形式，即2、4、8、16
                inSampleSize*=2;
            }
        }
        return inSampleSize;
    }

    public Bitmap decodeBitmapFromFileDescriptor(FileDescriptor fileDescriptor,int finalWidth,
                                                 int finalHeight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=false;
        BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
        options.inSampleSize=calculateInSampleSize(options,finalWidth,finalHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
    }
}
