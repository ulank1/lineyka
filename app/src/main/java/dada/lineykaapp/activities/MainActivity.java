package dada.lineykaapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.zxczxxas.BuildConfig;
import com.example.user.zxczxxas.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dada.lineykaapp.widget.RulerView;

public class MainActivity extends Activity {
    int CAMERA_REQUEST =1234;
    private static final int MULTIPLE_PERMISSION_REQUEST_CODE = 4;
    private static final int REQUEST_CODE = 556;

    File file,file3;

    ImageView imageView;

    RulerView mRulerView;

    boolean bool=true;
    Button bt_ok;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mRulerView = (RulerView) findViewById(R.id.ruler);
        Switch mSwitch= (Switch) findViewById(R.id.switch_unit);
        imageView= (ImageView) findViewById(R.id.img_photo);
        bt_ok = (Button) findViewById(R.id.bt_ok);

        Glide.with(this).load(R.drawable.aaaaaaaaa).into(imageView);

        checkPermissionsState();
        mRulerView.setVisibility(View.GONE);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRulerView.setRulerType(RulerView.RulerType.CM);
                } else {
                    mRulerView.setRulerType(RulerView.RulerType.INCH);
                }
                mRulerView.postInvalidate();
            }
        });


    }

    public void oncksdfsd(View view) {
        if (bool) {
            bt_ok.setText("Сохранить");
            mRulerView.setVisibility(View.VISIBLE);
            bool = false;
        }else {


            bt_ok.setText("Готово");

            String image=takeScreenshot();

            alertPush(image);





        }
    }


    public void alertPush(final String image){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
       LayoutInflater inflater = getLayoutInflater();

/*
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
*/

        View dialogView = inflater.inflate(R.layout.allert_dialog_basket, null);

        builder.setView(dialogView);

        LinearLayout btn_neutral =  dialogView.findViewById(R.id.dialog_neutral_btn3);
        LinearLayout okInPlace = dialogView.findViewById(R.id.okInPlace);
        final TextView pressure =(TextView) dialogView.findViewById(R.id.pressure);
        final EditText fio =(EditText) dialogView.findViewById(R.id.fio);
        final EditText description = (EditText) dialogView.findViewById(R.id.description);
        ImageView imageView = (ImageView) dialogView.findViewById(R.id.gandon);
        Log.e("IMAGE",image);
        Glide.with(MainActivity.this).load(new File(image)).into(imageView);
        pressure.setText(Shared.pressure);

        final AlertDialog dialog = builder.create();

        okInPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean bool1 = true;

                if (fio.getText().toString().equals("")) bool1 = false;
                if (description.getText().toString().equals("")) bool1 = false;

                if (bool1){
                    DataHelper dataHelper = new DataHelper(MainActivity.this);
                    D d = new D();
                    d.setImage(image);
                    d.setDescription(description.getText().toString());
                    d.setName(fio.getText().toString());
                    d.setPressure(Shared.pressure);
                    dataHelper.addPrice(d);
                    dialog.cancel();
                    mRulerView.setVisibility(View.GONE);
                    bool = true;
                }else {
                    Toast.makeText(MainActivity.this, "заполните поле", Toast.LENGTH_SHORT).show();
                }





            }
        });

        btn_neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss/cancel the alert dialog1
                dialog.cancel();

            }
        });
        dialog.show();
    }


    private String takeScreenshot() {

        Calendar calendar = Calendar.getInstance();

        String date;
        date = calendar.get(Calendar.DAY_OF_MONTH)+""+calendar.get(Calendar.MONTH)+""+calendar.get(Calendar.YEAR);
        date = date+""+calendar.get(Calendar.HOUR_OF_DAY)+""+calendar.get(Calendar.MINUTE)+""+calendar.get(Calendar.SECOND);
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/davlenie/");
        if (!file.exists()){
            file.mkdirs();
        }

        String mPath = Environment.getExternalStorageDirectory().toString() + "/davlenie/" + date + ".jpg";
        try {
            // image naming and path  to include sd card  appending name you choose for file


            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();


        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
        return mPath;
    }



    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                MainActivity.this);
        myAlertDialog.setTitle("Загрузить фотографии");
        myAlertDialog.setMessage("Выберите способ загрузки?");

        myAlertDialog.setPositiveButton("Галерея",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(intent, REQUEST_CODE);

                    }
                });

        myAlertDialog.setNegativeButton("Камера",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            dispatchTakePictureIntent(CAMERA_REQUEST);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
        myAlertDialog.show();
    }
    private void dispatchTakePictureIntent(int b) throws IOException {
        if (Build.VERSION.SDK_INT<=24) {
            Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            file3 = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file3);
            startActivityForResult(captureIntent, b);
        }else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                file3 = null;
                Log.e("GHJKL:", "sdfeurif");
                try {
                    file3 = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e("ERROR", "FILE");
                    return;
                }
                // Continue only if the File was successfully created
                if (file3 != null) {
                    // Uri photoURI = Uri.fromFile(createImageFile());
                    Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            createImageFile());

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, b);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());;
        String imageFileName = "jj"+timeStamp+".jpg";
        File file =new File(Environment.getExternalStorageDirectory(), imageFileName);

        return file;
    }
    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //  Uri res = data.getData();
        }

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK) {
                Uri resultUri = data.getData();
                    Glide.with(MainActivity.this).load(resultUri).into(imageView);

                if (Build.VERSION.SDK_INT>=24) {
                    Uri resultUri2 = data.getData();
                    try {
                        file3 = new File(getRealPathFromURI_API19(this, resultUri2));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    file3 = new File(getRealPathFromURI(resultUri));
                }

                }
            }
        if (resultCode==RESULT_OK&& requestCode==CAMERA_REQUEST){

                Glide.with(MainActivity.this).load(file3).into(imageView);
        }

    }


    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);
        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA};
        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    private void checkPermissionsState() {
        int cameraStatePermissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        int writeExternalStoragePermissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int readExternalStoragePermissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);


        if (cameraStatePermissionCheck == PackageManager.PERMISSION_GRANTED&&
                writeExternalStoragePermissionCheck == PackageManager.PERMISSION_GRANTED&&
                readExternalStoragePermissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.e("CheckPermission","TRUE");
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    },
                    MULTIPLE_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean somePermissionWasDenied = false;
                    for (int result : grantResults) {
                        if (result == PackageManager.PERMISSION_DENIED) {
                            somePermissionWasDenied = true;
                        }
                    }

                    if (somePermissionWasDenied) {
                        // Toast.makeText(this, "Cant load maps without all the permissions granted", Toast.LENGTH_SHORT).show();
                    } else {
                    }
                } else {
                    // Toast.makeText(this, "Cant load maps without all the permissions granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    public void onSelect(View view) {
        startDialog();
    }
int i=0;
    public void oncksdfsdss(View view) {
        i+=4;
        imageView.setRotation(i);
    }

    public void oncksdfsdssrdu(View view) {
        i-=4;
        imageView.setRotation(i);
    }
}
