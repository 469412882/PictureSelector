package com.luck.pictureselector;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

public class PhotoFragmentActivity extends AppCompatActivity {
    private PhotoFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simp);
        // 在部分低端手机，调用单独拍照时内存不足时会导致activity被回收，所以不重复创建fragment
        if (savedInstanceState == null) {
            // 添加显示第一个fragment
            fragment = new PhotoFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.tab_content, fragment,
                    PictureConfig.FC_TAG).show(fragment)
                    .commit();
        } else {
            fragment = (PhotoFragment) getSupportFragmentManager()
                    .findFragmentByTag(PictureConfig.FC_TAG);
        }
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        AndPermission.with(this).permission(Manifest.permission.WRITE_EXTERNAL_STORAGE).onGranted(new Action() {
            @Override
            public void onAction(List<String> permissions) {
                PictureFileUtils.deleteCacheDirFile(PhotoFragmentActivity.this);
            }
        }).onDenied(new Action() {
            @Override
            public void onAction(List<String> permissions) {
                Toast.makeText(PhotoFragmentActivity.this,
                        getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
            }
        }).start();
    }

}
