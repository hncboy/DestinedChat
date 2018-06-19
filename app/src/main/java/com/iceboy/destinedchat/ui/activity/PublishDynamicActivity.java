package com.iceboy.destinedchat.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.adapter.AddDynamicImageAdapter;
import com.iceboy.destinedchat.app.cos.CosModel;
import com.iceboy.destinedchat.app.cos.IDataRequestListener;
import com.iceboy.destinedchat.model.Dynamic;
import com.iceboy.destinedchat.utils.StringUtils;
import com.iceboy.destinedchat.utils.ThreadUtils;
import com.tencent.cos.xml.utils.DigestUtils;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;

import org.apache.commons.codec.binary.Hex;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class PublishDynamicActivity extends BaseActivity {

    private static final String TAG = "PublishDynamicActivity";

    private CountDownLatch latch;
    private static final int PHOTO_REQUEST = 2;
    private AddDynamicImageAdapter mAdapter;
    private List<String> mPhotoUrlList = new ArrayList<>();
    private List<String> mPhotoList;

    @BindView(R.id.content)
    EditText mContent;

    @BindView(R.id.grid_view)
    GridView mGridView;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.toolbar_function1)
    ImageView mBack;

    @BindView(R.id.toolbar_function2)
    ImageView mToolbarPlus;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_publish_dynamic;
    }

    @Override
    protected void init() {
        initToolbar();
        mPhotoList = new ArrayList<>();
        mAdapter = new AddDynamicImageAdapter(mPhotoList, this);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chooseFromAlbum();
            }
        });
    }

    @OnClick({R.id.toolbar_function1, R.id.toolbar_function2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_function1:
                hideKeyBoard();
                finish();
                break;
            case R.id.toolbar_function2:
                uploadDynamic();
                break;
        }
    }

    private void initToolbar() {
        mTitle.setText(getString(R.string.publish_dynamic));
        mBack.setImageDrawable(getDrawable(R.drawable.ic_arrow_back_white_24dp));
        mToolbarPlus.setImageDrawable(getDrawable(R.drawable.ic_check_black_24dp));
    }

    private void chooseFromAlbum() {
        // 自由配置选项
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(true)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(getResources().getColor(R.color.colorPrimary))
                // “确定”按钮文字颜色
                .btnTextColor(Color.WHITE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#378cef"))
                // 返回图标ResId
                .backResId(R.drawable.ic_arrow_back_white_24dp)
                // 标题
                .title("选择照片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#4693EC"))
                // 裁剪大小。needCrop为true的时候配置
                .cropSize(1, 2, 200, 200)
                .needCrop(false)
                // 第一个是否显示相机，默认true
                .needCamera(true)
                // 最大选择图片数量，默认9
                .maxNum(9)
                .build();
        // 跳转到图片选择器
        ISNav.getInstance().toListActivity(this, config, PHOTO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO_REQUEST && data != null) {
                List<String> pathList = data.getStringArrayListExtra("result");
                uploadImage(pathList.toArray(new String[]{}));
                photoPath(pathList.toArray(new String[]{}));
            }

        }
    }

    /**
     * 上传图片
     *
     * @param paths
     */
    private void uploadImage(String[] paths) {
        // 设置一个数量锁
        latch = new CountDownLatch(paths.length);
        CosModel cosModel = new CosModel(getApplication());
        for (String path : paths) {
            //用作图片的名字
            String str = UUID.randomUUID().toString().replaceAll("-", "") + new Random().nextLong();
            String fileName = str.substring(1, 10);
            Log.i(TAG, "uploadImage: fileName = " + fileName);
            cosModel.uploadPic(fileName, path, new IDataRequestListener() {
                @Override
                public void loadSuccess(Object object) {
                    mPhotoUrlList.add((String) object);
                    latch.countDown();
                }
            });
        }
    }

    /**
     * 将所有图片路径放入mPhotoUrlList
     *
     * @param paths
     */
    public void photoPath(String[] paths) {
        mPhotoList.addAll(Arrays.asList(paths));
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 上传动态
     */
    private void uploadDynamic() {
        showProgress(getString(R.string.uploading));
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                if (latch != null) {
                    Log.i(TAG, "run: latch ");
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Dynamic dynamic = new Dynamic();
                dynamic.setWriter(BmobUser.getCurrentUser());
                Log.i(TAG, "run: BmobUser = " + BmobUser.getCurrentUser());
                dynamic.setText(mContent.getText().toString());
                dynamic.setPhotoList(mPhotoUrlList);
                Log.i(TAG, "run: mPhotoUrlList = " + mPhotoUrlList.size());
                dynamic.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        hideKeyBoard();
                        hideProgress();
                        finish();
                        if (e == null) {
                            toast(getString(R.string.create_dynamic_success));
                        } else {
                            toast(getString(R.string.create_dynamic_failed));
                        }
                    }
                });
            }
        });
    }
}
