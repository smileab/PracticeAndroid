package com.smileab.mobile.practice.libs.fresco;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.image.CloseableAnimatedImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.smileab.mobile.practice.config.Constants;
import com.smileab.mobile.practice.libs.blur.BitmapBlurHelper;

import java.io.File;

/**
 * Fresco图片库配置
 */
public class FrescoConfigConstants {

    /**
     * 是否使用OKhttp
     */
    private static final boolean USE_OKHTTP = false;
    private static int screen_w, screen_h;

    /**
     * 分配的可用内存
     */
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();
    /**
     * 使用的缓存数量
     */
    private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;

    /**
     * 小图极低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
     */
    private static final int MAX_SMALL_DISK_VERYLOW_CACHE_SIZE = 5 * ByteConstants.MB;
    /**
     * 小图低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
     */
    private static final int MAX_SMALL_DISK_LOW_CACHE_SIZE = 10 * ByteConstants.MB;
    /**
     * 小图磁盘缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
     */
    private static final int MAX_SMALL_DISK_CACHE_SIZE = 20 * ByteConstants.MB;

    /**
     * 默认图极低磁盘空间缓存的最大值
     */
    private static final int MAX_DISK_CACHE_VERYLOW_SIZE = 10 * ByteConstants.MB;
    /**
     * 默认图低磁盘空间缓存的最大值
     */
    private static final int MAX_DISK_CACHE_LOW_SIZE = 30 * ByteConstants.MB;
    /**
     * 默认图磁盘缓存的最大值
     */
    private static final int MAX_DISK_CACHE_SIZE = 50 * ByteConstants.MB;

    /**
     * 小图所放路径的文件夹名
     */
    private static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "imagepipeline_s_cache";
    /**
     * 默认图所放路径的文件夹名
     */
    private static final String IMAGE_PIPELINE_CACHE_DIR = "imagepipeline_cache";

    /**
     * 应用的磁盘文件夹
     */
    //sdcard路径
//    public static final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
//    public static final String APP_DISK_DIR = SDCARD + File.separator + "Practice" + File.separator;
    private static final String APP_DISK_DIR = Constants.APP_DISK_DIR;

    private static ImagePipelineConfig sImagePipelineConfig;

    private FrescoConfigConstants() {
    }

    /**
     * fresco 图片库 初始化 最好在application中进行
     *
     * @param context
     */
    public static void initialize(Context context) {
        Fresco.initialize(context, getImagePipelineConfig(context));// 图片缓存初始化配置
        screen_w = context.getResources().getDisplayMetrics().widthPixels;
        screen_h = context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 初始化配置，单例
     */
    private static ImagePipelineConfig getImagePipelineConfig(Context context) {
        if (sImagePipelineConfig == null) {
            sImagePipelineConfig = configureCaches(context);
        }
        return sImagePipelineConfig;
    }

    /**
     * 初始化配置
     */
    private static ImagePipelineConfig configureCaches(Context context) {

        File dirFile = new File(APP_DISK_DIR);

        // 内存配置
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(FrescoConfigConstants.MAX_MEMORY_CACHE_SIZE, // 内存缓存中总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE, // 内存缓存中图片的最大数量。
                FrescoConfigConstants.MAX_MEMORY_CACHE_SIZE / 10, // 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE, // 内存缓存中准备清除的总图片的最大数量。
                Integer.MAX_VALUE); // 内存缓存中单个图片的最大大小。

        // 修改内存图片缓存数量，空间策略
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {

            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };

        // 小图片的磁盘配置
        DiskCacheConfig diskSmallCacheConfig = DiskCacheConfig.newBuilder(context).setBaseDirectoryPath(dirFile)// (context.getApplicationContext().getCacheDir())//
                // 缓存图片基路径
                .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)// 文件夹名
                // .setCacheErrorLogger(cacheErrorLogger)//日志记录器用于日志错误的缓存。
                // .setCacheEventListener(cacheEventListener)//缓存事件侦听器。
                // .setDiskTrimmableRegistry(diskTrimmableRegistry)//类将包含一个注册表的缓存减少磁盘空间的环境。
                .setMaxCacheSize(FrescoConfigConstants.MAX_SMALL_DISK_CACHE_SIZE)// 默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_SMALL_DISK_LOW_CACHE_SIZE)// 缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_SMALL_DISK_VERYLOW_CACHE_SIZE)// 缓存的最大大小,当设备极低磁盘空间
                // .setVersion(version)
                .build();

        // 默认图片的磁盘配置
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context).setBaseDirectoryPath(dirFile)// (Environment.getExternalStorageDirectory().getAbsoluteFile())//
                // 缓存图片基路径
                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)// 文件夹名
                // .setCacheErrorLogger(cacheErrorLogger)//日志记录器用于日志错误的缓存。
                // .setCacheEventListener(cacheEventListener)//缓存事件侦听器。
                // .setDiskTrimmableRegistry(diskTrimmableRegistry)//类将包含一个注册表的缓存减少磁盘空间的环境。
                .setMaxCacheSize(FrescoConfigConstants.MAX_DISK_CACHE_SIZE)// 默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE)// 缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERYLOW_SIZE)// 缓存的最大大小,当设备极低磁盘空间
                // .setVersion(version)
                .build();

        // 缓存图片配置
        ImagePipelineConfig.Builder configBuilder = null;
        if (USE_OKHTTP) {
            // 需要imagepipeline-okhttp-v0.9.0.jar
            // configBuilder = OkHttpImagePipelineConfigFactory.newBuilder(context, HttpUtil.getOkHttpClient());
        } else {
            configBuilder = ImagePipelineConfig.newBuilder(context);
        }
        configBuilder
                // .setAnimatedImageFactory(AnimatedImageFactory
                // animatedImageFactory)//图片加载动画
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)// 内存缓存配置（一级缓存，已解码的图片）
                // .setCacheKeyFactory(cacheKeyFactory)//缓存Key工厂
                // .setEncodedMemoryCacheParamsSupplier(encodedCacheParamsSupplier)//内存缓存和未解码的内存缓存的配置（二级缓存）
                // .setExecutorSupplier(executorSupplier)//线程池配置
                // .setImageCacheStatsTracker(imageCacheStatsTracker)//统计缓存的命中率
                // .setImageDecoder(ImageDecoder imageDecoder) //图片解码器配置
                // .setIsPrefetchEnabledSupplier(Supplier<Boolean>
                // isPrefetchEnabledSupplier)//图片预览（缩略图，预加载图等）预加载到文件缓存
                .setMainDiskCacheConfig(diskCacheConfig)// 磁盘缓存配置（总，三级缓存）
                // .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
                // //内存用量的缩减,有时我们可能会想缩小内存用量。比如应用中有其他数据需要占用内存，不得不把图片缓存清除或者减小
                // 或者我们想检查看看手机是否已经内存不够了。
                // .setNetworkFetcher(networkFetcher)//自定的网络层配置：如OkHttp，Volley
                // .setPoolFactory(poolFactory)//线程池工厂配置
                // .setProgressiveJpegConfig(progressiveJpegConfig)//渐进式JPEG图
                // .setRequestListeners(requestListeners)//图片请求监听
                .setSmallImageDiskCacheConfig(diskSmallCacheConfig)// 磁盘缓存配置（小图片，可选～三级缓存的小图优化缓存）
                .setResizeAndRotateEnabledForNetwork(true)// 调整和旋转是否支持网络图片
                .setDownsampleEnabled(true)
        // mDownsampleEnabled——设置EncodeImage解码时是否解码图片样图，必须和ImageRequest的ResizeOptions一起使用
        // 作用就是在图片解码时根据ResizeOptions所设的宽高的像素进行解码，这样解码出来可以得到一个更小的Bitmap。
        // 通过在Decode图片时，来改变采样率来实现得，使其采样ResizeOptions大小。
        // ResizeOptions和DownsampleEnabled参数都不影响原图片的大小，影响的是EncodeImage的大小，
        // 进而影响Decode出来的Bitmap的大小，ResizeOptions须和此参数结合使用是因为单独使用ResizeOptions的话只支持JPEG图，
        // 所以需支持png、jpg、webp需要先设置此参数。
        ;
        return configBuilder.build();
    }

    /**
     * 按比例压缩 后处理器。
     */
    public static class RatioScalePostprocessor extends BasePostprocessor {

        private int max = 4096;// 最大尺寸

        public RatioScalePostprocessor() {
//            this.max = max;
        }

        @Override
        public CloseableReference<Bitmap> process(Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {
            try {
                int src_w = sourceBitmap.getWidth();// 原始宽度
                int src_h = sourceBitmap.getHeight();// 原始高度
                float raito = 1.0F * src_w / src_h;// raito=宽/高
                int w = 0;
                int h = 0;
                if (src_w > src_h && src_w > max) {
                    // 宽大于高并且宽大于最大值
                    w = max;
                    h = (int) (max / raito);

                    int min_h = 240;
                    if (h < min_h) {
                        int tmp_h = h;
                        h = src_h;
                        while (true) {
                            h = h / 2;
                            if (h < min_h) {
                                h = h * 2;
                                break;
                            }
                        }
                        if (h < src_h) {
                            w = (int) (h * raito);
                        } else {
                            h = tmp_h;
                        }
                    }

                } else if (src_h > src_w && src_h > max) {
                    h = max;
                    w = (int) (max * raito);

                    int min_w = 240;
                    if (w < min_w) {
                        int tmp_w = w;
                        w = src_w;
                        while (true) {
                            w = w / 2;
                            if (w < min_w) {
                                w = w * 2;
                                break;
                            }
                        }
                        if (w < src_w) {
                            h = (int) (w / raito);
                        } else {
                            w = tmp_w;
                        }
                    }
                }
                CloseableReference<Bitmap> bitmapRef = null;

                try {
                    if (w > 0 && h > 0 && src_w > w && src_h > h && !sourceBitmap.isRecycled()) {
                        bitmapRef = bitmapFactory.createScaledBitmap(sourceBitmap, w, h, false);
                    } else {
                        bitmapRef = bitmapFactory.createBitmap(sourceBitmap);
                    }

                    return CloseableReference.cloneOrNull(bitmapRef);
                } finally {
                    CloseableReference.closeSafely(bitmapRef);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }


    // 下载监听
    public static abstract class FrescoControllerListener extends BaseControllerListener<Object> {

        private FrescoParam.Builder builder;

        public FrescoControllerListener(FrescoParam.Builder builder) {
            this.builder = builder;
        }

        public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
            if (imageInfo != null && imageInfo instanceof ImageInfo) {
                try {
                    boolean isGif = CloseableAnimatedImage.class.isInstance(imageInfo);
                    ImageInfo imageinfo = (ImageInfo) imageInfo;
                    int width = imageinfo.getWidth();
                    int height = imageinfo.getHeight();
                    float ratio = width * 1.0F / (height == 0 ? width : height);

                    if (!isGif && builder != null && builder.isBlurEnable()) {
                        CloseableStaticBitmap csb = (CloseableStaticBitmap) imageInfo;
                        BitmapBlurHelper.blur(csb.getUnderlyingBitmap(), builder.getBlurRadius());
                    }

                    handle(imageinfo, isGif, width, height, ratio);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public abstract void handle(ImageInfo imageInfo, boolean isGif, int width, int height, float _ratio);

    }

}