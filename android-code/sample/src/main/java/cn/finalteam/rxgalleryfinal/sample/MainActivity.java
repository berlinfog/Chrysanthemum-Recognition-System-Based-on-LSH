package cn.finalteam.rxgalleryfinal.sample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.yalantis.ucrop.model.AspectRatio;

import java.io.File;
import java.util.*;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.finalteam.rxgalleryfinal.sample.imageloader.ImageLoaderActivity;
import cn.finalteam.rxgalleryfinal.ui.RxGalleryListener;
import cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity;
import cn.finalteam.rxgalleryfinal.ui.base.IMultiImageCheckedListener;
import cn.finalteam.rxgalleryfinal.ui.base.IRadioImageCheckedListener;
import cn.finalteam.rxgalleryfinal.ui.fragment.MediaGridFragment;
import cn.finalteam.rxgalleryfinal.utils.Logger;
import cn.finalteam.rxgalleryfinal.utils.PermissionCheckUtils;
/**
 * 示例
 *
 * @author KARL-dujinyang
 *         <p>
 *         openGallery 返回 void,如果想使用RxGalleryFinal对象，请在 openGallery() 之前返回 RxGalleryFinal 对象
 *         <p>
 *         <p>
 *         RxGalleryFinal radio = RxGalleryFinal
 *         with(MainActivity.this)
 *         image()
 *         radio();
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static String Image_path = null;
    private List<MediaBean> list = null;
    RadioButton mRbRadioIMG, mRbMutiIMG, mRbOpenC, mRbRadioVD, mRbMutiVD, mRbCropZD, mRbCropZVD;
    public static String Image_Path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_open_img).setOnClickListener(this);
        mRbRadioIMG = (RadioButton) findViewById(R.id.rb_radio_img);
        //多选事件的回调
        RxGalleryListener
                .getInstance()
                .setMultiImageCheckedListener(
                        new IMultiImageCheckedListener() {
                            @Override
                            public void selectedImg(Object t, boolean isChecked) {
                                Toast.makeText(getBaseContext(), isChecked ? "选中" : "取消选中", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void selectedImgMax(Object t, boolean isChecked, int maxSize) {
                                Toast.makeText(getBaseContext(), "你最多只能选择" + maxSize + "张图片", Toast.LENGTH_SHORT).show();
                            }
                        });
        //裁剪图片的回调
        RxGalleryListener
                .getInstance()
                .setRadioImageCheckedListener(
                        new IRadioImageCheckedListener() {
                            @Override
                            public void cropAfter(Object t) {
                                Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public boolean isActivityFinish() {
                                return false;
                            }
                        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_img:
                openImageSelect();
                break;

        }

    }

    /**
     * 设置 照片路径 和 裁剪路径
     */
    private void setPath() {
        RxGalleryFinalApi.setImgSaveRxSDCard("dujinyang");
        RxGalleryFinalApi.setImgSaveRxCropSDCard("dujinyang/crop");//裁剪会自动生成路径；也可以手动设置裁剪的路径；
    }

    /**
     * 直接裁剪  or  拍照并裁剪( 查看 onActivityResult())
     */
    private void openCrop() {
        if (mRbCropZD.isChecked()) {
            //直接裁剪
            String inputImg = "";
            Toast.makeText(MainActivity.this, "没有图片演示，请选择‘拍照裁剪’功能", Toast.LENGTH_SHORT).show();
            //  RxGalleryFinalApi.cropScannerForResult(MainActivity.this, RxGalleryFinalApi.getModelPath(), inputImg);//调用裁剪.RxGalleryFinalApi.getModelPath()为模拟的输出路径
        } else {
            //            RxGalleryFinalApi.openZKCamera(MainActivity.this);

            SimpleRxGalleryFinal.get().init(
                    new SimpleRxGalleryFinal.RxGalleryFinalCropListener() {
                        @NonNull
                        @Override
                        public Activity getSimpleActivity() {
                            return MainActivity.this;
                        }

                        @Override
                        public void onCropCancel() {
                            Toast.makeText(getSimpleActivity(), "裁剪被取消", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCropSuccess(@Nullable Uri uri) {
                            Toast.makeText(getSimpleActivity(), "裁剪成功：" + uri, Toast.LENGTH_SHORT).show();
                            Image_path = uri.getPath();
                            Logger.i("Image_path = "+Image_path);

                        }

                        @Override
                        public void onCropError(@NonNull String errorMessage) {
                            Toast.makeText(getSimpleActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
            ).openCamera();
        }
    }


    /**
     * 图片
     * 单选，多选，  直接打开相机
     */
    private void openImageSelect() {
        if (mRbRadioIMG.isChecked()) {
            openImageSelectRadioMethod(3);
        }
    }





    /**
     * 自定义单选
     */
    private void openRadio() {
        RxGalleryFinal
                .with(MainActivity.this)
                .image()
                .radio()
                .cropAspectRatioOptions(0, new AspectRatio("3:3", 30, 10))
                .crop()
                .imageLoader(ImageLoaderType.FRESCO)
                .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                        Toast.makeText(getBaseContext(), "选中了图片路径：" + imageRadioResultEvent.getResult().getOriginalPath(), Toast.LENGTH_SHORT).show();
                    }
                })
                .openGallery();
    }




    /**
     * OPEN 图片单选实现方法
     * <p>
     * 默认使用 第三个 ，如果运行sample,可自行改变Type，运行Demo查看效果
     */
    private void openImageSelectRadioMethod(int type) {
        RxGalleryFinalApi instance = RxGalleryFinalApi.getInstance(MainActivity.this);
        switch (type) {
            case 0:

                //打开单选图片，默认参数
                instance
                        .setImageRadioResultEvent(new RxBusResultDisposable<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                Logger.i("单选图片的回调");
                            }
                        }).open();

                break;
            case 1:

                //设置自定义的参数
                instance
                        .setType(RxGalleryFinalApi.SelectRXType.TYPE_IMAGE, RxGalleryFinalApi.SelectRXType.TYPE_SELECT_RADIO)
                        .setImageRadioResultEvent(new RxBusResultDisposable<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                Logger.i("单选图片的回调");
                            }
                        }).open();

                break;
            case 2:

                //快速打开单选图片,flag使用true不裁剪
                RxGalleryFinalApi
                        .openRadioSelectImage(MainActivity.this, new RxBusResultDisposable<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent o) throws Exception {
                                Logger.i("单选图片的回调");
                            }
                        }, true);

                break;
            case 3:

                //单选，使用RxGalleryFinal默认设置，并且带有裁剪
                instance
                        .openGalleryRadioImgDefault(
                                new RxBusResultDisposable<ImageRadioResultEvent>() {
                                    @Override
                                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                        Logger.i("只要选择图片就会触发");
                                        //MainActivity.Image_Path = RxGalleryFinalApi.getModelPath();
                                    }
                                })
                        .onCropImageResult(
                                new IRadioImageCheckedListener() {
                                    @Override
                                    public void cropAfter(Object t) {
                                        Logger.i("裁剪完成");
                                        MainActivity.Image_Path = MediaGridFragment.savePath;
                                        String context=MainActivity.Image_Path;
                                        Intent intent = CompareActivity.newIntent(MainActivity.this, context);
                                        startActivity(intent);
                                       // Intent intent = new Intent();
                                       // intent.setClass(MainActivity.this,CompareActivity.class);
                                        //startActivity(intent);
                                    }

                                    @Override
                                    public boolean isActivityFinish() {
                                        Logger.i("返回false不关闭，返回true则为关闭");
                                        return true;
                                    }
                                });

                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SimpleRxGalleryFinal.get().onActivityResult(requestCode, resultCode, data);
        //MainActivity.Image_Path = RxGalleryFinalApi.fileImagePath.getPath();
//        if (requestCode == RxGalleryFinalApi.TAKE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            Logger.i("拍照OK，图片路径:" + RxGalleryFinalApi.fileImagePath.getPath());
//            //刷新相册数据库
//            RxGalleryFinalApi.openZKCameraForResult(MainActivity.this, new MediaScanner.ScanCallback() {
//                @Override
//                public void onScanCompleted(String[] strings) {
//                    Logger.i(String.format("拍照成功,图片存储路径:%s", strings[0]));
//                    Logger.d("演示拍照后进行图片裁剪，根据实际开发需求可去掉上面的判断");
//                    RxGalleryFinalApi.cropScannerForResult(MainActivity.this, RxGalleryFinalApi.getModelPath(), strings[0]);//调用裁剪.RxGalleryFinalApi.getModelPath()为默认的输出路径
//                }
//            });
//        } else {
//            Logger.i("失敗");
//        }
    }
}