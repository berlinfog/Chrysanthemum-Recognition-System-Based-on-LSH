package cn.finalteam.rxgalleryfinal.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.bumptech.glide.Glide;
//import com.squareup.picasso.Request;

import cn.finalteam.rxgalleryfinal.sample.utils.Constant;
import cn.finalteam.rxgalleryfinal.sample.utils.FormFile;
import cn.finalteam.rxgalleryfinal.sample.utils.SocketHttpRequester;
import cn.finalteam.rxgalleryfinal.sample.utils.Constant;
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
import cn.finalteam.rxgalleryfinal.sample.utils.uploadImage;
import cn.finalteam.rxgalleryfinal.utils.MediaType;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import okhttp3.Headers;
//import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CompareActivity extends AppCompatActivity {

    private static final String TAG="CompareActivity";
    private File file;
    private String context;
    public static Intent newIntent(Context packageContext, String context){
        Intent intent=new Intent(packageContext,CompareActivity.class);
        intent.putExtra("CONTEXT",context);
        return intent;
    }

    public void uploadFile(File imageFile) {
        Log.i(TAG, "upload start");
        try {
            String requestUrl = "http://172.24.139.10:8080/upload/upload/execute.do";
            //请求普通信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", "张三");
            params.put("pwd", "zhangsan");
            params.put("age", "21");
            params.put("fileName", imageFile.getName());
            //上传文件
            cn.finalteam.rxgalleryfinal.sample.utils.FormFile formfile = new cn.finalteam.rxgalleryfinal.sample.utils.FormFile(imageFile.getName(), imageFile, "image", "application/octet-stream");

            SocketHttpRequester.post(requestUrl, params, formfile);
            Log.i(TAG, "upload success");
        } catch (Exception e) {
            Log.i(TAG, "upload error");
            e.printStackTrace();
        }
        Log.i(TAG, "upload end");
    }
    class UploadRunnable implements Runnable {
        public void run() {
            Log.i(TAG, "runnable run");
            uploadFile(file);
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button button = null;
        ImageView search_pic = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        button = (Button)findViewById(R.id.btn_search_pic);
        search_pic = (ImageView) findViewById(R.id.search_Image);
        //       TextView text = (TextView)findViewById(R.id.SearchPath);
        context=getIntent().getStringExtra("CONTEXT");

        //      text.setText("路径"+context);

        file = new File(""+context);
        Bitmap bitmap = BitmapFactory.decodeFile(""+context);
        search_pic.setImageBitmap(bitmap);
        /*
        //uploadFile(file);
        UploadRunnable upload=new UploadRunnable();
        //将实现的Runnable类的实例传入构造函数
        Thread thread=new Thread(upload);
        thread.start();
        //search_pic.setImageUri(MainActivity.Image_path);*/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage up = new uploadImage();
                up.type = -1;
                up.uploadImage(context);
                int type = -1;
                while(true){
                    if(up.type!=-1){
                        type = up.type;
                        break;
                    }
                }
                Intent intent = new Intent();
                Log.i(TAG, "type="+type);
                //int type = (int)(Math.random()*(5-0+1));
                switch (type){
                    case 0:intent.setClass(CompareActivity.this,FlowersActivity.class);break;
                    case 1:intent.setClass(CompareActivity.this,Flowers2Activity.class);break;
                    case 2:intent.setClass(CompareActivity.this,Flowers3Activity.class);break;
                    case 3:intent.setClass(CompareActivity.this,Flowers4Activity.class);break;
                    case 4:intent.setClass(CompareActivity.this,Flowers5Activity.class);break;
                    case 5:intent.setClass(CompareActivity.this,Flowers6Activity.class);break;
                }
                //intent.setClass(CompareActivity.this,Flowers2Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
