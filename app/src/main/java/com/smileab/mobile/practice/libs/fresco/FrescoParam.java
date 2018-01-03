package com.smileab.mobile.practice.libs.fresco;

import android.content.Context;
import android.graphics.PointF;

import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.smileab.mobile.practice.common.AppApplication;

public class FrescoParam {

    private FrescoParam() {
    }

    public static Builder withSmart(String uri) {
        return new Builder(uri, SizeMode.S);
    }

    public static Builder withMiddle(String uri) {
        return new Builder(uri, SizeMode.M);
    }

    public static Builder withLarge(String uri) {
        return new Builder(uri, SizeMode.L);
    }

    public static Builder withOrigin(String uri) {
        return new Builder(uri, SizeMode.O);
    }

    public static Builder withAsset(String res) {
        return new Builder(Builder.IMAGE_ASSET_PREFIX + res, SizeMode.O);
    }

    public static Builder withResource(int res) {
        return new Builder(Builder.IMAGE_RES_PREFIX + res, SizeMode.O);
    }


    public static class Builder {

        private static final String IMAGE_FORMAT_GIF = ".gif";

        public static final String IMAGE_RES_PREFIX = "res://com.dankegongyu/";
        public static final String IMAGE_ASSET_PREFIX = "asset://assets/";


        // 图片地址
        private String URI = "";
        // 图片尺寸
        private int sizeMode = SizeMode.L;

        // 宽
        private int width = 0;
        // 高
        private int height = 0;

        //是否是圆形。 圆形和圆角冲突。
        private boolean isCircle = false;

        // 圆角半径
        private float radiusLeftTop; // 左上
        private float radiusLeftBottom;// 左下
        private float radiusRightTop;// 右上
        private float radiusRightBottom;//右下
        private boolean isSetRoundingParamsByCode = false; // 是否用代码设置过圆角或圆形

        // 描边颜色
        private int bordeColor = 0xFFFFFFFF;
        // 描边宽度
        private int bordeWidth = -1;

        // 默认图
        private int defaultImageID = 0;

        // 缩放类型
        private ScalingUtils.ScaleType scaleType = ScalingUtils.ScaleType.CENTER_CROP;

        // 缩放类型时的焦点。
        private PointF scaleFocusPoint = null;

        // 自动旋转图片方向
        private boolean autoRotateEnabled = true;

        // 对于gif,webp图像，是否自定播放动画
        private boolean autoPlayAnimEnabled = true;

        // 图片加载失败时 是否可以点击重新加载
        private boolean clickToRetryEnabled = false;

        // 后处理器. 处理图片的大小，缩放。
        private FrescoConfigConstants.RatioScalePostprocessor postprocessor;

        // 进度条
        private ProgressBarDrawable progressBarDrawable;

        // 图片下载时的监听。
        private FrescoConfigConstants.FrescoControllerListener controllerListener;

        // 是否开启高斯模糊. 这个是在controllerListener中实现的。
        private boolean blurEnable;
        // radius 值越大越模糊，取值范围1~100
        private int blurRadius;

        private Context context;

        private Builder() {
        }

        private Builder(String uri, int sizeMode) {

            context = AppApplication.getInstance().getApplicationContext();

            width = context.getResources().getDisplayMetrics().widthPixels;
            height = context.getResources().getDisplayMetrics().heightPixels;

            setURI(uri, sizeMode);
        }


        // 获得加载地址

        public String getURI() {
            return URI;
        }


        // 设置加载图片地址
        private Builder setURI(String uri, int sizeMode) {
            this.URI = uri;
            this.sizeMode = sizeMode;


            if (uri != null) {
                if (uri.startsWith("/storage/") || uri.startsWith("/system") || uri.startsWith("/mnt")) {
                    this.URI = "file://" + uri;
                }
                switch (sizeMode) {
                    case SizeMode.O:
                        width = width / 1;
                        height = height / 1;
                        break;
                    case SizeMode.S:
                        width = width / 4;
                        height = height / 4;
                        break;
                    case SizeMode.M:
                        width = width / 3;
                        height = height / 3;
                        break;
                    case SizeMode.L:
                        width = width / 2;
                        height = height / 2;
                        break;
                }

            }

            if (this.URI == null) {
                this.URI = "file://";
            }
            return this;
        }


        // 获得加载的图片大小
        public int getSizeMode() {
            return sizeMode;
        }

        // 设置加载的图片大小
        public Builder setSizeMode(int sizeMode) {
            this.sizeMode = sizeMode;
            return this;
        }

        // 宽
        public int getWidth() {
            return width;
        }

        // 宽
        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        // 高
        public int getHeight() {
            return height;
        }

        // 高
        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }


        // 是否是圆形
        public boolean isCircle() {
            return isCircle;
        }

        // 设置圆形
        public Builder setCircle(boolean isCircle) {
            isSetRoundingParamsByCode = true;
            this.isCircle = isCircle;
            return this;
        }


        // 获得圆角半径。 左上
        public float getRadiusLeftTop() {
            return radiusLeftTop;
        }

        // 获得圆角半径。 左下
        public float getRadiusLeftBottom() {
            return radiusRightTop;
        }

        // 获得圆角半径。 右上
        public float getRadiusRightTop() {
            return radiusLeftBottom;
        }

        // 获得圆角半径。 左下
        public float getRadiusRightBottom() {
            return radiusRightBottom;
        }

        // 设置圆角半径
        public Builder setRoundedCornerRadius(float radius) {
            setRoundedCornerRadius(radius, radius, radius, radius);
            return this;
        }

        // 设置圆角半径
        public Builder setRoundedCornerRadius(float leftTop, float leftBottom, float rightTop, float rightBottom) {
            isSetRoundingParamsByCode = true;

            radiusLeftTop = leftTop;
            radiusLeftBottom = leftBottom;
            radiusRightTop = rightTop;
            radiusRightBottom = rightBottom;
            return this;
        }

        public boolean isSetRoundingParamsByCode() {
            return isSetRoundingParamsByCode;
        }

        // 获得描边颜色
        public int getBordeColor() {
            return bordeColor;
        }

        // 获得描边宽度
        public int getBordeWidth() {
            return bordeWidth;
        }

        // 描边 颜色和宽度 设置
        public Builder setBorder(int color, int width) {
            this.bordeColor = color;
            this.bordeWidth = width;
            return this;
        }

        // 默认图
        public int getDefaultImage() {
            return defaultImageID;
        }

        // 设置默认图
        public Builder setDefaultImage(int resID) {
            this.defaultImageID = resID;
            return this;
        }

        // 实际图的缩放模式
        public ScalingUtils.ScaleType getScaleType() {
            return scaleType;
        }

        // 实际图的缩放模式
        public Builder setScaleType(ScalingUtils.ScaleType scaleType) {
            this.scaleType = scaleType;
            return this;
        }


        // 缩放模式时的中心点
        public PointF getScaleFocusPoint() {
            return scaleFocusPoint;
        }

        /**
         * 设置图片缩放模式时的中心点
         *
         * @param x 范围0~1 0屏幕左边 1屏幕右边
         * @param y 范围0~1 0屏幕上边 1屏幕下边
         * @return
         */
        public Builder setScaleFocusPoint(float x, float y) {
            scaleFocusPoint = new PointF(x, y);
            return this;
        }


        // 自动旋转图片方向
        public boolean isAutoRotateEnabled() {
            return autoRotateEnabled;
        }

        // 设置自动旋转图片方向
        public Builder setAutoRotateEnabled(boolean autoRotateEnabled) {
            this.autoRotateEnabled = autoRotateEnabled;
            return this;
        }

        // 播放动画
        public boolean isAutoPlayAnimEnabled() {
            return autoPlayAnimEnabled;
        }

        // 设置自动播放gif webp动画
        public Builder setAutoPlayAnimEnabled(boolean auto) {
            this.autoPlayAnimEnabled = auto;
            return this;
        }

        // 获得点击重新加载状态
        public boolean getClickToRetryEnabled() {
            return clickToRetryEnabled;
        }

        // 加载图片失败时 点击重新加载
        public Builder setClickToRetryEnabled(boolean retry) {
            this.clickToRetryEnabled = retry;
            return this;
        }

        // 获得后处理器
        public BasePostprocessor getPostprocessor() {
            return postprocessor;
        }

        // 设置后处理器
        public Builder setPostprocessor(FrescoConfigConstants.RatioScalePostprocessor postprocessor) {
            this.postprocessor = postprocessor;
            return this;
        }

        // 获取加载图片时的进度条
        public ProgressBarDrawable getProgressBarDrawable() {
            return progressBarDrawable;
        }

        // 设置加载图片时的进度条
        public Builder setProgressBarDrawable(ProgressBarDrawable progressBarDrawable) {
            this.progressBarDrawable = progressBarDrawable;
            return this;
        }

        // 图片下载时的监听
        public BaseControllerListener getControllerListener() {
            return controllerListener;
        }

        // 设置图片下载时的监听
        public Builder setControllerListener(FrescoConfigConstants.FrescoControllerListener controllerListener) {
            this.controllerListener = controllerListener;
            return this;
        }


        // 是否开启高斯模糊
        public boolean isBlurEnable() {
            return blurEnable;
        }

        // 设置开启高斯模糊
        public Builder setBlurEnable(boolean blurEnable) {
            this.blurEnable = blurEnable;
            return this;
        }

        // 高斯模糊 模糊范围1-100
        public int getBlurRadius() {
            return blurRadius;
        }

        // 设置高斯模糊 模糊范围1-100
        public Builder setBlurRadius(int blurRadius) {
            blurRadius = Math.max(1, blurRadius);
            blurRadius = Math.min(100, blurRadius);
            this.blurRadius = blurRadius;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (!(o instanceof Builder)) {
                return false;
            }

            Builder builder = (Builder) o;
            if (builder.getURI().equals(getURI())) {
                return true;
            }
            return false;

        }
    }

    public interface SizeMode {
        //        public final static
        int O = 0;// 默认是原图
        int S = 1;// 小图
        int M = 2;// 中图
        int L = 3;// 大图
    }

}


//  远程图片 http://, https:// HttpURLConnection <br>
//  本地文件 file:// FileInputStream<br>
//  Content provider content:// ContentResolver<br>
//  asset目录下的资源 asset:// AssetManager<br>
//  res目录下的资源 res:// ,"res://包名(实际可以是任何字符串甚至留空)/" + R.drawable.ic_launcher<br>
//

//    <!--
//    fadeDuration 						加载图片动画时间 图片从无到有的过度时间
//    viewAspectRatio						图片宽高比例=宽/高
//    actualImageScaleType				实际加载图片的缩放类型 默认都是centercrop 没有特殊情况不用动
//    backgroundImage						背景图片
//
//    placeholderImage					占位图  drawable、color
//    placeholderImageScaleType			占位图缩放类型 ScalingUtils.ScaleType.FIT_CENTER
//
//    retryImage							点击重新加载的图片，图片加载失败时显示
//    retryImageScaleType					点击重新加载图缩放类型 ScalingUtils.ScaleType.FIT_CENTER
//
//    failureImage						失败图
//    failureImageScaleType				失败图缩放类型 ScalingUtils.ScaleType.FIT_CENTER
//
//    progressBarImage					进度条
//    progressBarImageScaleType			进度条缩放类型
//    progressBarAutoRotateInterval		自动旋转间隔
//
//    overlayImage						叠加图
//    pressedStateOverlayImage			叠加图-手指按下时
//
//    roundAsCircle						true 圆形，默认flase
//    roundedCornerRadius					圆角半径（四角） roundAsCircle=false时
//    roundTopLeft						圆角开关 左上 如果预览中没有效果 就在真机中查看效果
//    roundTopRight						圆角开关 右上
//    roundBottomRight					圆角开关 右下
//    roundBottomLeft						圆角开关 左下
//    roundWithOverlayColor				圆形覆盖颜色
//    roundingBorderWidth					描边宽度
//    roundingBorderColor 				描边颜色
//    -->