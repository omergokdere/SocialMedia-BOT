package com.alicanil;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.util.Arrays;
import java.util.List;
// share bolumu

public class ShareFragment extends Fragment implements View.OnClickListener{

    private Activity mActivity;
    private View mView;

    private Button mBtnShareImg; // paylasma yaptigimiz butonun tanimlanmasi
    private static final int SELECT_PICTURE = 1;  // secilen fotonun success dondurdugu deger
    private String dataPath;
    private CallbackManager mCallbackManager;
    private LoginManager manager;
    private String from = "text";

    // paylasim ekrani yani ikinci ic ekranin acilmasi share button bastigimiz yer
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_share, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCallbackManager = CallbackManager.Factory.create();
        manager = LoginManager.getInstance();
        manager.registerCallback(mCallbackManager,  new FacebookCallback<LoginResult>(){

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("ShareFragment", "Success");

                if(from.equals("image")){
                   pickPicture();
                }else{
                }
            }
            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
        initUI();
    }
    // giris yapildiktan sonra ki UI i aliyor ver butonun yerini idsini tanimliyor o acilan ekrandaki
    private void initUI(){
        mView = getView();
        mBtnShareImg = (Button)mView.findViewById(R.id.img_share);
        mBtnShareImg.setOnClickListener(this);
    }

    // onclick methodunde share image a basilirsa napacagini gosteriyor
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.img_share:{
                from = "image";
                manager.logInWithPublishPermissions(mActivity, Arrays.asList("publish_actions"));
            }
                break;
        }
    }
    // fotoyu alma ıslemlerının baslangıcı
    private void pickPicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        List<ResolveInfo> list = mActivity.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() <= 0) {
            Log.d("ShareFragment", "no image picker intent on this hardware");
            return;
        }
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, SELECT_PICTURE);
    }
    //share bolumu buranın altındakı kısım
    private void publishPicture(String path){
        Log.i("ShareFragment image", path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap image = BitmapFactory.decodeFile(path, options);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareApi.share(content, null);
        Toast.makeText(getActivity(),"You have successfully shared photo on Facebook!", Toast.LENGTH_LONG).show(); // ekrana msg gosteriyor basarılı sekılde upload edersek
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PICTURE )
        {
            Uri selectedImageUri = data.getData();
            dataPath = getPath(selectedImageUri);
            publishPicture(dataPath);
        }else   {        }
    }

    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        // burda ilk once medya dosyasından almaya calısıyoruz
        // sadece galerideki fotografı cekmemıze yarıyor
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = mActivity.getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }
}
