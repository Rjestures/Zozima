package code.fragment;


import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.internal.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;
import com.zozima.android.BuildConfig;
import com.zozima.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import code.basic.AddMarginActivity;
import code.common.SimpleHTTPConnection;
import code.main.MainActivity;
import code.notification.NotificationActivity;
import code.order.OrderListActivity;
import code.profile.Abouts;
import code.profile.AddLungbuage;
import code.profile.EditProfile;
import code.profile.HelpActivity;
import code.basic.SplashActivity;
import code.database.AppSettings;
import code.profile.PrivacyPolicy;
import code.profile.TermsCondition;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;
import code.view.BaseFragment;
import code.view.CustomTextView;
import code.wishlistSharedCatlogue.MySharedCatalogs;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static code.utils.AppUtils.getEncoded64ImageStringFromBitmap;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends BaseFragment {

    //CustomTextView
    TextView tvProfile, tvNumber, tvName, tvLogout, tvRate, tvNotification, tvHelp, tvsharedcatalog;

    //RelaytiveLayout
    RelativeLayout RllOrder, rllabouts, rladdlanguage, rlSettings, rltermscondtion, rlPrivacy, rllfeedback;

    //ImageView
    ImageView ivdrop, ivlees;

    //Linearlayout
    LinearLayout llsetting;

    CircleImageView profile_image;

    //GalleryCamera
    private static final int SELECT_PICTURE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Zozima";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    // For image
    String picturePath = "", filename = "", ext = "";
    Bitmap rotatedBitmap;
    Uri picUri;
    String encodedImage = "";
    TextView tvVersion;
    private Uri fileUri;
    private Bitmap bitmap;


    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        findViewById(view);

        return view;
    }

    //findViewById
    private void findViewById(View view) {


        //ImageView
        ivdrop = view.findViewById(R.id.ivdrop);
        ivlees = view.findViewById(R.id.ivless);
        tvVersion = view.findViewById(R.id.tvVersion);
        profile_image = view.findViewById(R.id.profile_image);

        //CUstomTextview
        tvProfile = view.findViewById(R.id.edt_profile);
        tvNotification = view.findViewById(R.id.notification);
        tvNumber = view.findViewById(R.id.number);
        tvName = view.findViewById(R.id.name);
        tvLogout = view.findViewById(R.id.logout);
        tvRate = view.findViewById(R.id.ctvrate);
        tvHelp = view.findViewById(R.id.help);
        tvsharedcatalog = view.findViewById(R.id.tvsharedcatalog);

        //RelaytiveLayout
        RllOrder = view.findViewById(R.id.order);
        rllabouts = view.findViewById(R.id.rllabouts);
        rladdlanguage = view.findViewById(R.id.rladdlanguage);
        rltermscondtion = view.findViewById(R.id.rltermscondtion);
        rlSettings = view.findViewById(R.id.rlSettings);
        llsetting = view.findViewById(R.id.llsetting);
        rlPrivacy = view.findViewById(R.id.rlPrivacy);
        rllfeedback = view.findViewById(R.id.rllfeedback);

        tvNumber.setText(getString(R.string.countryCode) + AppSettings.getString(AppSettings.userMobile));


        tvVersion.setText(AppSettings.getString(AppSettings.versionName));

        tvName.setText(AppSettings.getString(AppSettings.profilename));
        if (SimpleHTTPConnection.isNetworkAvailable()) {
            GetProfileImage();
        } else {
            AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
        }


        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, EditProfile.class));
            }

        });

        ivlees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llsetting.setVisibility(View.GONE);
                ivlees.setVisibility(View.GONE);
                ivdrop.setVisibility(View.VISIBLE);

            }

        });

        ivdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llsetting.setVisibility(View.VISIBLE);
                ivlees.setVisibility(View.VISIBLE);
                ivdrop.setVisibility(View.GONE);


            }

        });

        rlSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llsetting.setVisibility(View.VISIBLE);
                ivlees.setVisibility(View.VISIBLE);
                ivdrop.setVisibility(View.GONE);
            }

        });


        rltermscondtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mActivity, TermsCondition.class));

            }

        });


        rlPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mActivity, PrivacyPolicy.class));

            }

        });

        rllfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mActivity, FeedBack.class));

            }

        });


        rllabouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, Abouts.class));
            }

        });

        tvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, HelpActivity.class));
            }
        });

        RllOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, OrderListActivity.class));
            }
        });

        tvsharedcatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, MySharedCatalogs.class));
            }
        });
        rladdlanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, AddLungbuage.class));
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
                alertDialogBuilder.setMessage(getString(R.string.aresurelogout));
                alertDialogBuilder.setPositiveButton(getString(R.string.Yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                AppSettings.clearSharedPreference();
                                Intent I = new Intent(mActivity, SplashActivity.class);
                                startActivity(I);
                                arg0.dismiss();
                                onResume();
                            }
                        });

                alertDialogBuilder.setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

/*
                final Dialog dialog = new Dialog(mActivity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.tvLogout);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.CENTER;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.show();

                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                //TextView
                TextView tvYes = (TextView) dialog.findViewById(R.id.tvOk);
                TextView tvCancel = (TextView) dialog.findViewById(R.id.tvcancel);

                dialog.show();

                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
               AppSettings.clearSharedPreference();
              Intent I=new Intent(mActivity, SplashActivity.class);
              startActivity(I);

                        dialog.dismiss();


                        onResume();
                    }
                });

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });*/


        });
        tvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRejectDialog();
            }
        });
        tvNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NotificationActivity.class));
            }
        });


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    AlertCameraGallery();
                } else {
                    AppUtils.showToastSort(mActivity, getString(R.string.give_permission));
                }
            }
        });


    }

    private void acceptRejectDialog() {

        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ratingbar);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
        RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
        TextView ratingsubmit = (TextView) dialog.findViewById(R.id.ratingsubmit);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        //   tvHeader.setText("Confirmation Alert...!!!");

        dialog.show();
        ratingsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("pagePath", 1);
        startActivity(intent);

    }

    private boolean checkAndRequestPermissions() {

        int writeStorage = ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int openCamera = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (openCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (writeStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(mActivity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    private void AlertCameraGallery() {
        final BottomSheetDialog dialog = new BottomSheetDialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_camera_gallery);
        dialog.setCancelable(true);
        dialog.show();
        RelativeLayout rrCancel = dialog.findViewById(R.id.rr_cancel);
        LinearLayout llCamera = dialog.findViewById(R.id.llCamera);
        LinearLayout llGallery = dialog.findViewById(R.id.llGallery);
        LinearLayout llRemovePhoto = dialog.findViewById(R.id.llRemovePhotto);

        rrCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                captureImage();
                dialog.dismiss();
            }
        });

        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PICTURE);

              /*  if (Build.VERSION.SDK_INT < 19) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), SELECT_PICTURE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(intent, SELECT_PICTURE);
                }*/
                dialog.dismiss();

            }
        });

        llRemovePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
                alertDialogBuilder.setMessage(getString(R.string.removeprofile));
                alertDialogBuilder.setPositiveButton(getString(R.string.removee),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                RemoveProfilePicture();

                                dialog.dismiss();
                            }
                        });

                alertDialogBuilder.setNegativeButton(getString(R.string.cancele), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

    }

    private void captureImage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile(MEDIA_TYPE_IMAGE));
            /*AppSettings.putString(AppSettings.profile_pic, String.valueOf(fileUri));*/
            Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            it.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(it, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } else {
            // create Intent to take a picture and return control to the calling application
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }

    }

    //onActivityResult
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                if (fileUri == null) {

                    fileUri = Uri.parse(AppSettings.getString(AppSettings.profile_pic));
                    picturePath = fileUri.getPath();

                } else {
                    if (!fileUri.equals(""))
                        picturePath = fileUri.getPath();
                }

                filename = picturePath.substring(picturePath.lastIndexOf("/") + 1);

                String selectedImagePath = picturePath;

                ext = "jpg";

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 500;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(selectedImagePath, options);

                Matrix matrix = new Matrix();
                matrix.postRotate(getImageOrientation(picturePath));
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                byte[] ba = bao.toByteArray();

                encodedImage = getEncoded64ImageStringFromBitmap(rotatedBitmap);
                profile_image.setPadding(0, 0, 0, 0);
                profile_image.setImageBitmap(rotatedBitmap);


            }
        } else if (requestCode == SELECT_PICTURE) {
            if (data != null) {

                try {
                    picUri = data.getData();
                    Uri contentURI = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = mActivity.getContentResolver().query(contentURI, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    System.out.println("Image Path : " + picturePath);
                    cursor.close();

                    filename = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                    ext = getFileType(picturePath);
                    String selectedImagePath = picturePath;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(selectedImagePath, options);
                    final int REQUIRED_SIZE = 500;
                    int scale = 1;
                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)

                        scale *= 2;

                    options.inSampleSize = scale;
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFile(selectedImagePath, options);

                    Matrix matrix = new Matrix();
                    matrix.postRotate(getImageOrientation(picturePath));
                    rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                    byte[] ba = bao.toByteArray();

                    encodedImage = getEncoded64ImageStringFromBitmap(rotatedBitmap);
                    profile_image.setPadding(0, 0, 0, 0);
                    profile_image.setImageBitmap(rotatedBitmap);


                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
            else {

                Toast.makeText(mActivity, getString(R.string.unabletoselectimage), Toast.LENGTH_SHORT).show();

                Log.v("imaaage", String.valueOf(SELECT_PICTURE));

            }

        }

    }


    public Uri getOutputMediaFileUri(int type) {
        return FileProvider.getUriForFile(mActivity, mActivity.getPackageName() + ".provider", getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        UpdateProfilePicture(imgString);
        return imgString;
    }

    public static int getImageOrientation(String imagePath) {
        int rotate = 0;
        try {

            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static String getFileType(String path) {
        String fileType = null;
        fileType = path.substring(path.indexOf('.', path.lastIndexOf('/')) + 1).toLowerCase();
        return fileType;
    }

    //GetProfileImage
    private void GetProfileImage() {
        AppUtils.showRequestDialog(mActivity);
        AppUtils.hideSoftKeyboard(mActivity);
        String url = AppUrls.GetProfilePicture;

        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("user_id", AppSettings.getString(AppSettings.userId));
            Log.v("findObject", String.valueOf(json));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(url)
                .addJSONObjectBody(json)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppUtils.hideDialog();
                        Log.v("getProfile", String.valueOf(response));
                        try {
                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
                            String resMsg = jsonObject1.getString("resMsg");
                            String profile_pic = jsonObject1.getString("profile_pic");
                            AppSettings.putString(AppSettings.profile_pic, profile_pic);

                            if (jsonObject1.getString("resCode").equalsIgnoreCase("1")) {
                                Picasso.get().load(AppSettings.getString(AppSettings.profile_pic)).into(profile_image);
                            } else {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();
                        Log.v("ggfh", String.valueOf(anError));


                    }

                });
    }


    //UpdateProfilePicture
    private void UpdateProfilePicture(String imgString) {
        AppUtils.hideSoftKeyboard(mActivity);
        String url = AppUrls.UpdateProfilePicture;

        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("user_id", AppSettings.getString(AppSettings.userId));
            jsonObject.put("profile_pic", imgString);
            Log.v("findObject", String.valueOf(json));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(url)
                .addJSONObjectBody(json)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppUtils.hideDialog();
                        Log.v("getProfile", String.valueOf(response));
                        try {
                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);
                            String resMsg = jsonObject1.getString("resMsg");

                            if (jsonObject1.getString("resMsg").equalsIgnoreCase("1")) {
                                Toast.makeText(mActivity, resMsg, Toast.LENGTH_SHORT).show();
                            } else {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();
                        Log.v("ggfh", String.valueOf(anError));


                    }

                });
    }


    //RemoveProfilePicture
    private void RemoveProfilePicture() {
        AppUtils.hideSoftKeyboard(mActivity);
        String url = AppUrls.RemoveProfilePicture;

        final JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            json.put(AppConstants.projectName, jsonObject);
            jsonObject.put("user_id", AppSettings.getString(AppSettings.userId));
            Log.v("findObject", String.valueOf(json));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(url)
                .addJSONObjectBody(json)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppUtils.hideDialog();
                        Log.v("getProfile", String.valueOf(response));
                        try {
                            JSONObject jsonObject1 = response.getJSONObject(AppConstants.projectName);

                            if (jsonObject1.getString("resCode").equalsIgnoreCase("1")) {
                                Picasso.get().load(R.drawable.user_image).into(profile_image);
                                Toast.makeText(mActivity, "Profile photo removed", Toast.LENGTH_SHORT).show();
                            } else {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        AppUtils.hideDialog();
                        Log.v("ggfh", String.valueOf(anError));


                    }

                });
    }


}



