package com.smileab.mobile.practice.libs.fresco;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.WriterCallback;
import com.facebook.common.internal.Closeables;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferInputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.smileab.mobile.practice.common.app.AppApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FrescoImageHelper {

    // 原图
    public static void getImage(String uri, SimpleDraweeView view) {
        FrescoParam.Builder param = FrescoParam.withOrigin(uri);
        getImageForCustom(param, view);
    }

    // 小图
    public static void getImageForSmall(String uri, SimpleDraweeView view) {
        FrescoParam.Builder param = FrescoParam.withSmart(uri);
        getImageForCustom(param, view);
    }

    // 中图
    public static void getImageForMiddle(String uri, SimpleDraweeView view) {
        FrescoParam.Builder param = FrescoParam.withMiddle(uri);
        getImageForCustom(param, view);
    }

    // 大图
    public static void getImageForLarge(String uri, SimpleDraweeView view) {
        FrescoParam.Builder param = FrescoParam.withLarge(uri);
        getImageForCustom(param, view);
    }

    // 压缩后的原始图。一般压缩在屏幕的宽高范围内。
    public static void getImageForOriginal(String uri, SimpleDraweeView view) {
        FrescoParam.Builder param = FrescoParam.withOrigin(uri);
        getImageForCustom(param, view);
    }

    // 不压缩的原始图。回调在main thread
    public static void getImageForOriginal(final String uri, final FrescoResultListener<String> listener) {
        downloadImageForOriginal(uri, listener, true);
    }

    // 不压缩的原始图。回调在子线程
    public static void getImageForOriginal(final String uri, final FrescoResultListener<String> listener, boolean callbackOnUiThread) {
        downloadImageForOriginal(uri, listener, callbackOnUiThread);
    }

    // resource资源
    public static void getImageForResource(int res, SimpleDraweeView view) {
        FrescoParam.Builder param = FrescoParam.withResource(res);
        getImageForCustom(param, view);
    }

    // Asset 资源
    public static void getImageForAsset(String res, SimpleDraweeView view) {
        FrescoParam.Builder param = FrescoParam.withAsset(res);
        getImageForCustom(param, view);
    }


    /**
     * 获取图片 加载到SimpleDraweeView中，支持Gif图片，自动释放内存
     *
     * @param param
     * @param view  需要用SimpleDraweeView来替代ImageView
     */
    public static void getImageForCustom(FrescoParam.Builder param, SimpleDraweeView view) {

        if (param == null || view == null) {
            return;
        }
        try {
            setDefaultHierarchy(param, view);
            ImageRequest imageRequest = getDefaultImageRequest(view, param);
            DraweeController draweeController = getDefaultDraweeController(imageRequest, param, view);
            view.setController(draweeController);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停网络请求
     */
    public static void pause() {
        try {
            Fresco.getImagePipeline().pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 恢复网络请求
     */
    public static void resume() {
        try {
            Fresco.getImagePipeline().resume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当前网络请求是否处于暂停状态
     *
     * @return
     */
    public static boolean isPaused() {
        return Fresco.getImagePipeline().isPaused();
    }

    /**
     * 清除某一个图片的bitmap缓存
     *
     * @param param
     */
    public static void evictFromMemoryCache(FrescoParam.Builder param) {
        try {
            Fresco.getImagePipeline().evictFromMemoryCache(Uri.parse(param.getURI()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除某一个图片的disk缓存
     *
     * @param param
     */
    public static void evictFromDiskCache(FrescoParam.Builder param) {
        try {
            Fresco.getImagePipeline().evictFromDiskCache(Uri.parse(param.getURI()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除某一个图片的缓存。 bitmap和disk
     *
     * @param param
     */
    public static void evictFromCache(FrescoParam.Builder param) {
        try {
            Fresco.getImagePipeline().evictFromCache(Uri.parse(param.getURI()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除所有缓存。bitmap
     */
    public static void clearMemoryCaches() {
        try {
            Fresco.getImagePipeline().clearMemoryCaches();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除所有缓存。disk
     */
    public static void clearDiskCaches() {
        try {
            Fresco.getImagePipeline().clearDiskCaches();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除所有缓存。bitmap和disk
     */
    public static void clearCaches() {
        try {
            Fresco.getImagePipeline().clearCaches();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 内存中是否有图片
     */
    public static boolean isMemoryCachedImage(FrescoParam.Builder param) {
        if (param == null) {
            return false;
        }
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        return imagePipeline.isInBitmapMemoryCache(Uri.parse(param.getURI()));
    }

    /**
     * 从bitmap内存缓存中获取
     *
     * @param param
     * @return
     */
    public static Bitmap getMemoryCachedImage(FrescoParam.Builder param) {
        ImageRequest imageRequest = getDefaultImageRequest(null, param);
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline().//
                fetchImageFromBitmapCache(imageRequest, null);
        CloseableReference<CloseableImage> imageReference = null;
        try {
            imageReference = dataSource.getResult();
            if (imageReference != null) {
                try {
                    CloseableImage image = imageReference.get();
                    if (image instanceof CloseableBitmap) {
                        return ((CloseableBitmap) image).getUnderlyingBitmap();
                    }
                } finally {
                    CloseableReference.closeSafely(imageReference);
                }
            }
        } finally {
            if (dataSource != null) {
                dataSource.close();
            }
        }
        return null;
    }

    /**
     * 磁盘中是否有图片
     *
     * @param param
     * @return
     */
    public static boolean isDiskCachedImage(FrescoParam.Builder param) {
        if (param == null) {
            return false;
        }
        CacheKey cacheKey = getCacheKeyEncoded(param);
        return (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey) ||//
                ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey));
    }

    /**
     * 从disk缓存中获取图片
     *
     * @param param
     * @return
     */
    public static Bitmap getDiskCachedImage(FrescoParam.Builder param) {
        try {
            File file = getImageDiskCacheFile(param.getURI());
            if (file != null) {
                BitmapFactory.Options newOpts = new BitmapFactory.Options();
                newOpts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getAbsolutePath(), newOpts);
                int w = newOpts.outWidth;
                int h = newOpts.outHeight;
                float hh = 1024;
                float ww = 1024;
                int SCALE = 1;
                if (w >= h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
                    SCALE = (int) (newOpts.outWidth / ww);
                } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
                    SCALE = (int) (newOpts.outHeight / hh);
                }
                if (SCALE <= 1) {
                    SCALE = 1;
                }
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize = SCALE;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static List<FrescoParam.Builder> needPreloadImages = new ArrayList<>();

    public static void addNeedPreloadImage(FrescoParam.Builder fp) {
        if (!needPreloadImages.contains(fp)) {
            needPreloadImages.add(fp);
        }
    }

    /**
     * 保存需要预加载的图片记录
     */
    public static void saveNeedPreloadImages() {
        SharedPreferences spUtil = AppApplication.getInstance().getApplicationContext().getSharedPreferences("Preload_Image", Context.MODE_PRIVATE);
        String result = "";
        try {
            if (needPreloadImages.size() > 0) {
                JSONArray arr = new JSONArray();
                for (FrescoParam.Builder fp : needPreloadImages) {
                    JSONObject json = new JSONObject();
                    json.put("url", fp.getURI());
                    json.put("mode", fp.getSizeMode());
                    arr.put(json);
                }
                result = arr.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor = spUtil.edit();
        editor.clear();
        editor.putString("preload_imgs", result);
        editor.commit();
    }

    public static void checkNeedPreloadImages() {
        needPreloadImages.clear();
        SharedPreferences spUtil = AppApplication.getInstance().getApplicationContext().getSharedPreferences("Preload_Image", Context.MODE_PRIVATE);

        String result = spUtil.getString("preload_imgs", "");
        if (!TextUtils.isEmpty(result)) {
            try {
                JSONArray arr = new JSONArray(result);
                if (arr.length() > 0) {
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject json = arr.optJSONObject(i);
                        int mode = json.optInt("mode");
                        FrescoParam.Builder fp;
                        if (mode == FrescoParam.SizeMode.S) {
                            fp = FrescoParam.withSmart(json.optString("url"));
                        } else if (mode == FrescoParam.SizeMode.M) {
                            fp = FrescoParam.withMiddle(json.optString("url"));
                        } else if (mode == FrescoParam.SizeMode.L) {
                            fp = FrescoParam.withLarge(json.optString("url"));
                        } else {
                            fp = FrescoParam.withOrigin(json.optString("url"));
                        }
                        ImageRequest imageRequest = getDefaultImageRequest(null, fp);
                        Fresco.getImagePipeline().prefetchToBitmapCache(imageRequest, null);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 把一个本地图片添加成指定网络URL的图片缓存
     *
     * @param localImgPath 本地图片路径
     * @param netImgUrl    本地图片对应的网络地址
     * @return 是否成功
     */
    public static void addLocalImgToDiskCache(String localImgPath, String netImgUrl) {
        try {
            if (!TextUtils.isEmpty(localImgPath) && !TextUtils.isEmpty(netImgUrl)) {
                final File localFile = new File(localImgPath);
                if (localFile != null && localFile.exists()) {
                    CacheKey cacheKey = getCacheKeyEncoded(netImgUrl);
                    if (!ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
                        ImagePipelineFactory.getInstance().getMainFileCache().insert(cacheKey, new WriterCallback() {

                            @Override
                            public void write(OutputStream output) {
                                copyLocalFileToDiskCache(localFile, output);
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 拷贝本地文件到指定的输出流中，OutputStream不用关闭，这是回调中传过来的参数，框架会自己维护
    private static void copyLocalFileToDiskCache(File localFile, OutputStream outputStream) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(localFile);
            byte[] buffer = new byte[1024 * 4];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void copyDataToDiskCache(InputStream inputStream, OutputStream outputStream) {
        try {
            byte[] buffer = new byte[1024 * 4];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 设置Hierarchy
    private static void setDefaultHierarchy(FrescoParam.Builder param, SimpleDraweeView view) {

        RoundingParams rp = view.getHierarchy().getRoundingParams();
        if (rp == null) {
            rp = new RoundingParams();
        }
        // 设置圆角、圆形
        if (param.isSetRoundingParamsByCode()) {
            rp.setRoundAsCircle(param.isCircle());
            if (!param.isCircle()) {
                rp.setCornersRadii(param.getRadiusLeftTop(), param.getRadiusRightTop(),//
                        param.getRadiusRightBottom(), param.getRadiusLeftBottom());
            }
            view.getHierarchy().setRoundingParams(rp);
        }

        // 设置描边颜色、宽度
        if (param.getBordeWidth() >= 0 && rp != null) {
            rp.setBorder(param.getBordeColor(), param.getBordeWidth());
            view.getHierarchy().setRoundingParams(rp);
        }

        // 默认图片（占位图）
        if (param.getDefaultImage() > 0) {
            view.getHierarchy().setPlaceholderImage(param.getDefaultImage());
        }

        // view缩放模式
        view.getHierarchy().setActualImageScaleType(param.getScaleType());

        // scaletype模式的焦点
        if (param.getScaleFocusPoint() != null) {
            view.getHierarchy().setActualImageFocusPoint(param.getScaleFocusPoint());
        }

        // 进度条
        if (param.getProgressBarDrawable() != null) {
            view.getHierarchy().setProgressBarImage(param.getProgressBarDrawable());
        }

    }

    // 设置ImageRequest
    private static ImageRequest getDefaultImageRequest(SimpleDraweeView view, FrescoParam.Builder fp) {
        ImageRequest imageRequest = null;
        Uri _uri = fp == null ? Uri.parse("") : Uri.parse(fp.getURI());
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(_uri);
        if (fp != null) {
            builder.setAutoRotateEnabled(fp.isAutoRotateEnabled());//自动旋转图片方向
        }
        // builder.setImageDecodeOptions(getImageDecodeOptions())// 图片解码库
        // builder.setImageType(ImageType.SMALL)//图片类型，设置后可调整图片放入小图磁盘空间还是默认图片磁盘空间
        // builder.setLocalThumbnailPreviewsEnabled(true)//缩略图预览，影响图片显示速度（轻微）
        builder.setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH);// 请求经过缓存级别
        if (fp != null && fp.getPostprocessor() != null) {
            builder.setPostprocessor(fp.getPostprocessor());//自定义后处理器
        } else {
            builder.setPostprocessor(new FrescoConfigConstants.RatioScalePostprocessor());//后处理器缩放图片
        }
        // builder.setProgressiveRenderingEnabled(true)//渐进加载，主要用于渐进式的JPEG图，影响图片显示速度（普通）

        if (fp != null && fp.getWidth() > 0 && fp.getHeight() > 0 && fp.getSizeMode() != FrescoParam.SizeMode.O) {
            builder.setResizeOptions(new ResizeOptions(fp.getWidth(), fp.getHeight()));
        } else if (view != null && view.getLayoutParams() != null && view.getLayoutParams().width > 0 && view.getLayoutParams().height > 0) {
            builder.setResizeOptions(new ResizeOptions(view.getLayoutParams().width, view.getLayoutParams().height));
        } else if (view != null && view.getMeasuredWidth() > 0 && view.getMeasuredHeight() > 0) {
            builder.setResizeOptions(new ResizeOptions(view.getMeasuredWidth(), view.getMeasuredHeight()));
        } else if (fp != null && fp.getSizeMode() == FrescoParam.SizeMode.O) {
            int size = Math.max(fp.getWidth(), fp.getHeight());
            builder.setResizeOptions(new ResizeOptions(size, size));
        }

        imageRequest = builder.build();

        return imageRequest;
    }


    // 设置DraweeController
    private static DraweeController getDefaultDraweeController(ImageRequest imageRequest, FrescoParam.Builder param, SimpleDraweeView view) {
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder();
        // .reset()//重置
        if (param != null) {
            builder.setAutoPlayAnimations(param.isAutoPlayAnimEnabled());// 自动播放图片动画
        }
        // builder.setCallerContext(callerContext);//回调
        if (param != null && param.getControllerListener() != null) {
            builder.setControllerListener(param.getControllerListener());// 监听图片下载完毕等
        } else {
            builder.setControllerListener(new FrescoConfigConstants.FrescoControllerListener(param) {

                @Override
                public void handle(ImageInfo imageInfo, boolean isGif, int width, int height, float _ratio) {

                }
            });// 监听图片下载完毕等
        }

        // builder.setDataSourceSupplier(dataSourceSupplier);//数据源
        // builder.setFirstAvailableImageRequests(firstAvailableImageRequests);//本地图片复用，可加入ImageRequest数组
        builder.setImageRequest(imageRequest);
        // builder.setLowResImageRequest(ImageRequest.fromUri(lowResUri));//先下载显示低分辨率的图
        builder.setOldController(view.getController());// DraweeController复用

        if (param != null) {
            builder.setTapToRetryEnabled(param.getClickToRetryEnabled());// 点击重新加载图
        }

        DraweeController draweeController = builder.build();
        return draweeController;

    }


    /**
     * 获得原始图片
     *
     * @param uri      请求的地址
     * @param listener 返回图片本地地址。 因为下载的是原始图片，所以图片可能会非常大。一般的imageview无法加载。所以返回本地地址，根据特定需要
     *                 处理。
     */
    private static void downloadImageForOriginal(final String uri, final FrescoResultListener<String> listener, final boolean onUiThread) {

        if (TextUtils.isEmpty(uri)) {
            return;
        }
        ImageRequest imageRequest = null;
        ImageRequestBuilder requestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri));
        requestBuilder.setAutoRotateEnabled(true);//自动旋转图片方向
        imageRequest = requestBuilder.build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        DataSource<CloseableReference<PooledByteBuffer>>
                dataSource = imagePipeline.fetchEncodedImage(imageRequest, null);

        DataSubscriber<CloseableReference<PooledByteBuffer>> dataSubscriber =
                new BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {
                    @Override
                    protected void onNewResultImpl(final DataSource<CloseableReference<PooledByteBuffer>> dataSource) {

                        if (!dataSource.isFinished()) {
                            return;
                        }

                        handle(dataSource);

                    }

                    private void handle(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {

                        CloseableReference<PooledByteBuffer> imageReference = dataSource.getResult();
                        if (imageReference != null) {
                            CloseableReference<PooledByteBuffer> closeableReference = imageReference.clone();
                            try {
                                PooledByteBuffer pooledByteBuffer = closeableReference.get();
                                final InputStream inputStream = new PooledByteBufferInputStream(pooledByteBuffer);

                                CacheKey cacheKey = getCacheKeyEncoded(uri);
                                if (!ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
                                    ImagePipelineFactory.getInstance().getMainFileCache().insert(cacheKey, new WriterCallback() {

                                        @Override
                                        public void write(OutputStream output) {
                                            copyDataToDiskCache(inputStream, output);
                                        }
                                    });
                                }

                                try {
                                } finally {
                                    Closeables.closeQuietly(inputStream);
                                }

                                callback();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                CloseableReference.closeSafely(closeableReference);
                                CloseableReference.closeSafely(imageReference);
                            }


                        }
                    }

                    private void callback() {

                        if (listener != null) {
                            final File file = getImageDiskCacheFile(uri);
                            if (onUiThread) {
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        String result = "";
                                        if (file != null && !TextUtils.isEmpty(file.getAbsolutePath())) {
                                            result = file.getAbsolutePath();
                                        }
                                        listener.onNewResultImpl(result);
                                    }
                                });
                            } else {
                                String result = "";
                                if (file != null && !TextUtils.isEmpty(file.getAbsolutePath())) {
                                    result = file.getAbsolutePath();
                                }
                                listener.onNewResultImpl(result);
                            }


                        }
                    }


                    @Override
                    public void onProgressUpdate(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                        int progress = (int) (dataSource.getProgress() * 100 + 0.5f);
                        if (listener != null) {
                            listener.onProgressUpdate(progress);
                        }
                    }

                    @Override
                    protected void onFailureImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                        Throwable t = dataSource.getFailureCause();
                        if (listener != null) {
                            listener.onFailureImpl(t);
                        }
                    }
                };

        dataSource.subscribe(dataSubscriber, getExecutor());

    }

    /**
     * 通过param获取硬盘缓存的key
     *
     * @param param
     * @return
     */
    private static CacheKey getCacheKeyEncoded(FrescoParam.Builder param) {
        ImageRequest request = getDefaultImageRequest(null, param);
        return DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(request, null);
    }

    /**
     * 通过URL获取硬盘缓存的key
     *
     * @param url
     * @return
     */
    private static CacheKey getCacheKeyEncoded(String url) {
        ImageRequest request = ImageRequest.fromUri(url);
        return DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(request, null);
    }


    /**
     * 获取图片缓存文件
     *
     * @param url
     * @return
     */
    private static File getImageDiskCacheFile(String url) {
        CacheKey cacheKey = getCacheKeyEncoded(url);
        File localFile = null;
        if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
            BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
            localFile = ((FileBinaryResource) resource).getFile();
        } else if (ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey)) {
            BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageFileCache().getResource(cacheKey);
            localFile = ((FileBinaryResource) resource).getFile();
        }
        return localFile;
    }


    private static ExecutorService executor = null;

    private static ExecutorService getExecutor() {
        if (executor == null)
            executor = Executors.newFixedThreadPool(2);
        return executor;
    }

    public static String getCacheSize() {
        String cache = "0.0MB";
        Fresco.getImagePipelineFactory().getMainFileCache().trimToMinimum();
        long cacheSize = Fresco.getImagePipelineFactory().getMainFileCache().getSize();
        if (cacheSize > 0) {
//            float cacheSizeTemp1 = changToDecimal(Math.round(cacheSize / 1024));
            float cacheSizeTemp2 = changToDecimal(Math.round((cacheSize / 1024) / 1024));
//            if (cacheSizeTemp1 < 1) {
//                cache = cacheSize + "B";
//            } else if ((cacheSizeTemp1 >= 1) && (cacheSizeTemp2 < 1)) {
//                cache = cacheSizeTemp1 + "KB";
//            } else
            if (cacheSizeTemp2 >= 1) {
                cache = cacheSizeTemp2 + "MB";
            }
        }
        return cache;
    }

    /**
     * 保留一位小数
     */
    private static float changToDecimal(float in) {
        DecimalFormat df = new DecimalFormat("0.0");
        String out = df.format(in);
        return Float.parseFloat(out);
    }
}
