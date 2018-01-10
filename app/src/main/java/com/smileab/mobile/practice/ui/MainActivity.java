package com.smileab.mobile.practice.ui;

import android.Manifest;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import com.smileab.mobile.practice.R;
import com.smileab.mobile.practice.bean.RepairInitDataResp;
import com.smileab.mobile.practice.cell.ClassifyCell;
import com.smileab.mobile.practice.config.Constants;
import com.smileab.mobile.practice.utils.FileIOUtils;
import com.smileab.mobile.practice.utils.GsonUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.classify_cell)
    ClassifyCell cellClassify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


//        new RxPermissions(this)
//                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                .subscribe(new Observer<Boolean>() {
//
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull Boolean aBoolean) {
//                        if (!aBoolean) {
//                            return;
//                        }
//
                        File file = new File(Constants.APP_DISK_DIR, "text.txt");
                        String data = FileIOUtils.readFile2String(file);
                        RepairInitDataResp resp = GsonUtil.fromJson(data, RepairInitDataResp.class);

                        cellClassify.setData(resp);
//
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });


    }

}
