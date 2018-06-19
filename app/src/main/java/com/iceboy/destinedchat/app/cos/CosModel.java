package com.iceboy.destinedchat.app.cos;

import android.app.Application;
import android.os.AsyncTask;

import com.iceboy.destinedchat.app.DcApplication;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;

import java.io.InputStream;

/**
 * Created by hncboy on 2018/6/10.
 */
public class CosModel {
    private static final String BUCKET = "destinedchat";
    private static final String cosAvatarPath = "/avatar/";
    private static final String cosPhotoPath = "/photo/";

    private CosXmlService mService;

    public CosModel(Application application) {
        if (application instanceof DcApplication) {
            mService = ((DcApplication) (application)).getCosXmlService();
        } else {
            throw new RuntimeException("请传入DcApplication...");
        }
    }

    public void uploadPic(final String fileName, final InputStream is, final IDataRequestListener listener) {
        new AsyncTask<Object, Object, PutObjectResult>() {
            @Override
            protected PutObjectResult doInBackground(Object... objects) {
                PutObjectRequest putObjectRequest = null;
                try {
                    putObjectRequest = new PutObjectRequest(BUCKET, cosAvatarPath + fileName, is);
                    return mService.putObject(putObjectRequest);
                } catch (CosXmlClientException e) {
                    e.printStackTrace();
                    return null;
                } catch (CosXmlServiceException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(PutObjectResult o) {
                super.onPostExecute(o);
                if (o != null) {
                    listener.loadSuccess(o.accessUrl);
                }
            }
        }.execute();
    }

    public void uploadPic(final String fileName, final String path, final IDataRequestListener listener) {
        new AsyncTask<Object, Object, PutObjectResult>() {
            @Override
            protected PutObjectResult doInBackground(Object... objects) {
                PutObjectRequest putObjectRequest = null;
                try {
                    putObjectRequest = new PutObjectRequest(BUCKET, cosPhotoPath + fileName, path);
                    return mService.putObject(putObjectRequest);
                } catch (CosXmlClientException e) {
                    e.printStackTrace();
                    return null;
                } catch (CosXmlServiceException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(PutObjectResult o) {
                super.onPostExecute(o);
                if (o != null) {
                    listener.loadSuccess(o.accessUrl);
                }
            }
        }.execute();
    }
}
