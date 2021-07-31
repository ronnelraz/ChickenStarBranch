package com.charoenpokhandfoodph.Fragment;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.charoenpokhandfoodph.R;
import com.charoenpokhandfoodph.adapter.completedAdapter;
import com.charoenpokhandfoodph.connection.con_branch_settings;
import com.charoenpokhandfoodph.connection.con_loadCompletedOrder;
import com.charoenpokhandfoodph.connection.con_mobile_change_contact;
import com.charoenpokhandfoodph.connection.con_mobile_change_contact_verify;
import com.charoenpokhandfoodph.connection.con_mobile_change_name;
import com.charoenpokhandfoodph.connection.con_mobile_change_title;
import com.charoenpokhandfoodph.function;
import com.charoenpokhandfoodph.modal.completedlist;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;

import com.suke.widget.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import hari.bounceview.BounceView;
import in.aabhasjindal.otptextview.OtpTextView;

public class account extends Fragment {
    private static final long START_TIME_IN_MILLIS = 180000; //

    private TextView title,fullname,contact,username,time;
    private SwitchButton switchbtn;
    private BottomSheetBehavior bottomSheetBehavior;
    private MaterialRadioButton materialRadioButton;
//    private CardView cardtitle,cardname,cardcontact,carduser,cardpassword,cardtime;\
    @BindView(R.id.cardtitle) CardView cardtitle;
    @BindView(R.id.cardname) CardView cardname;
    @BindView(R.id.cardcontact) CardView cardcontact;
    @BindView(R.id.carduser) CardView carduser;
    @BindView(R.id.cardpassword) CardView cardpassword;
    @BindView(R.id.cardtime) CardView cardtime;
    private boolean oktitle = true;
    AlertDialog dialog;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private CountDownTimer mCountDownTimer;
    private String SpecialCode = null;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.frag_account,parent,false);
        ButterKnife.bind(this,view);
        title = view.findViewById(R.id.title);
        fullname = view.findViewById(R.id.fullname);
        contact = view.findViewById(R.id.contact);
        username = view.findViewById(R.id.username);
        time = view.findViewById(R.id.time);
        switchbtn = view.findViewById(R.id.switchbtn);

        data();

        return view;
    }



    protected void data(){
        Response.Listener<String> response = response1 -> {
            try {
                JSONObject jsonResponse = new JSONObject(response1);
                boolean success = jsonResponse.getBoolean("success");
                JSONArray array = jsonResponse.getJSONArray("data");

                if(success){

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        title.setText(object.getString("title"));
                        fullname.setText(object.getString("fname") + " " + object.getString("mname")  + " " + object.getString("lname")  + " " + object.getString("exname"));
                        contact.setText(function.phoneMask(object.getString("cellphone")));
                        username.setText(object.getString("username"));
                        time.setText(object.getString("opentime") +" - "+object.getString("closetime"));

                        boolean isActive = object.getString("status").equals("0") ? true : false;
                        switchbtn.setChecked(isActive);

                        cardtitle.setOnClickListener(v -> {
                            try {
                                bottomsheetEditor(0,"Change Title",object.getString("title"),object.getString("fname"),object.getString("mname"),object.getString("lname"),object.getString("exname"),object.getString("cellphone"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                        cardname.setOnClickListener(v -> {
                            try {
                                bottomsheetEditor(1,"Change Name",object.getString("title"),object.getString("fname"),object.getString("mname"),object.getString("lname"),object.getString("exname"),object.getString("cellphone"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                        cardcontact.setOnClickListener(v -> {
                            try {
                                bottomsheetEditor(2,"Change Contact Number",object.getString("title"),object.getString("fname"),object.getString("mname"),object.getString("lname"),object.getString("exname"),object.getString("cellphone"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                        cardpassword.setOnClickListener(v -> {
                            function.toast(v.getContext(),"ask password first");
                        });



                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {

        };
        con_branch_settings get = new con_branch_settings(function.getUID(),response,errorListener);
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(get);
    }



    protected void bottomsheetEditor(int type,String EditType,String title,String getfname,String getmname,String getlname,String getexname,String getcontact){


        View view = LayoutInflater.from(getActivity()).inflate(R.layout.bottomsheet_account,null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(),R.style.BottomSheetDialog);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        LinearLayout linearLayout = view.findViewById(R.id.root);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
//        params.height = getScreenHeight();
//        linearLayout.setLayoutParams(params);
        TextView headertitle = view.findViewById(R.id.titleHeader);
        headertitle.setText(EditType);
        MaterialButton back = view.findViewById(R.id.back);
        back.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });


         LinearLayout contitle = view.findViewById(R.id.conTitle);
         LinearLayout conname = view.findViewById(R.id.conname);
         LinearLayout concontact = view.findViewById(R.id.concontact);
         LinearLayout conPassword = view.findViewById(R.id.conpassword);


         //1
        RadioGroup titlegroup = view.findViewById(R.id.titlegroup);
        MaterialRadioButton mr = view.findViewById(R.id.mr);
        MaterialRadioButton mrs = view.findViewById(R.id.mrs);
        MaterialButton savetitle = view.findViewById(R.id.savetitle);
        //2
        MaterialButton savename = view.findViewById(R.id.savename);
        TextInputEditText fname = view.findViewById(R.id.fname);
        fname.setText(getfname);
        TextInputEditText mname = view.findViewById(R.id.mname);
        mname.setText(getmname);
        TextInputEditText lname = view.findViewById(R.id.lname);
        lname.setText(getlname);
        TextInputEditText exname = view.findViewById(R.id.exname);
        exname.setText(getexname);

        //3
        MaterialButton savecontact = view.findViewById(R.id.savecontact);
        TextInputEditText contact = view.findViewById(R.id.contact);
        contact.setText(getcontact);


         if(type == 0){
             contitle.setVisibility(View.VISIBLE);

             if(title.equals("MR")){
                 mr.setChecked(true);
                 mrs.setChecked(false);
             }else{
                 mr.setChecked(false);
                 mrs.setChecked(true);
             }

             savetitle.setOnClickListener(v -> {
                 int selectedId = titlegroup.getCheckedRadioButtonId();
                 materialRadioButton = view.findViewById(selectedId);
                 String getSelected = materialRadioButton.getText().toString().equals("MR") ? "MR" : "MS/MRS";
                 boolean ok = updateTitle(getSelected);
                 if(ok){
                     bottomSheetDialog.dismiss();
                     data();
                     function.toast(v.getContext(),"Updated Successfully!");

                 }

             });
         }
        else if(type == 1){
            conname.setVisibility(View.VISIBLE);
            savename.setOnClickListener(v -> {
                String strfname = fname.getText().toString();
                String strmname = mname.getText().toString();
                String strlname = lname.getText().toString();
                String strexname = exname.getText().toString();

                if(strfname.isEmpty()){
                    function.toast(v.getContext(),"Please enter your first name");
                    fname.requestFocus();
                }
                else{
                    boolean ok = updateName(strfname,strmname,strlname,strexname);
                    if(ok){
                        bottomSheetDialog.dismiss();
                        data();
                        function.toast(v.getContext(),"Updated Full Name");

                    }
                }

            });

        }
        else if(type == 2){
            concontact.setVisibility(View.VISIBLE);
             savecontact.setOnClickListener(v -> {
                 String strcontact = contact.getText().toString();
                 if(strcontact.isEmpty()){
                     function.toast(v.getContext(),"Please enter contact number");
                     contact.requestFocus();
                 }
                 else if(contact.length() <= 10){
                     function.toast(v.getContext(),"Please enter correct contact number");
                     contact.requestFocus();
                 }
                 else{
                     if(!strcontact.equals(getcontact)){

                         SpecialCode = GenerateCode();
                         sendcode(strcontact,SpecialCode);

                         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.full_screen_dialog);
                         View viewdialog = LayoutInflater.from(getActivity()).inflate(R.layout.change_number_verify, null);

                         TextView msgcontent = viewdialog.findViewById(R.id.msgcontent);
                         TextView timer = viewdialog.findViewById(R.id.timer);
                         MaterialButton resend = viewdialog.findViewById(R.id.resend);
                         msgcontent.setText("Please type the verification code sent to \n" + strcontact);
                         MaterialButton backdialog = viewdialog.findViewById(R.id.back);
                         MaterialButton verify = viewdialog.findViewById(R.id.verify);
                         OtpTextView code = viewdialog.findViewById(R.id.code);


                         backdialog.setOnClickListener(v1 -> {
                             dialog.dismiss();
                             mTimeLeftInMillis = START_TIME_IN_MILLIS;
                             updateCountDownText(timer);
                         });


                        verify.setOnClickListener(v1 ->{
                            String getcode = code.getOTP();
                            if(getcode.equals(SpecialCode)){
                                updatecontact(strcontact);
                                bottomSheetDialog.dismiss();
                                data();
                                new SweetAlertDialog(v1.getContext(),SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Changed Successfully!")
                                        .showCancelButton(false)
                                        .show();
                            }
                            else{
                                function.toast(v.getContext(),"Invalid code");
                            }
                        });

                         mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                             @Override
                             public void onTick(long millisUntilFinished) {
                                 mTimeLeftInMillis = millisUntilFinished;
                                 updateCountDownText(timer);
                             }
                             @Override
                             public void onFinish() {
                                 timer.setText("If you didn't receive a code");
                                 resend.setEnabled(true);
                             }
                         }.start();

                         resend.setOnClickListener(v1 -> {
                             resend.setEnabled(false);
                             mTimeLeftInMillis = START_TIME_IN_MILLIS;
                             updateCountDownText(timer);
                             SpecialCode = GenerateCode();
                             sendcode(strcontact,SpecialCode);

                             function.toast(v1.getContext(),strcontact);
                             mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                                 @Override
                                 public void onTick(long millisUntilFinished) {
                                     mTimeLeftInMillis = millisUntilFinished;
                                     updateCountDownText(timer);
                                 }
                                 @Override
                                 public void onFinish() {
                                     timer.setText("If you didn't receive a code");
                                     resend.setEnabled(true);
                                 }
                             }.start();
                         });


                         builder.setView(viewdialog);
                         dialog = builder.create();
                         BounceView.addAnimTo(dialog);
                         dialog.setCancelable(false);
                         dialog.setCanceledOnTouchOutside(false);
                         WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                         lp.copyFrom(dialog.getWindow().getAttributes());
                         lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                         lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                         dialog.show();
                         dialog.getWindow().setAttributes(lp);

                     }
                     else{
                         bottomSheetDialog.dismiss();
                         data();
                     }
                 }
             });

         }

        bottomSheetDialog.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.show();
    }

    public static String GenerateCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(99999);
        return String.format("%05d", number);
    }

    private void updateCountDownText(TextView textView) {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textView.setText("If you didn't receive a code within "+timeLeftFormatted);
    }

    public boolean updateTitle(String title){
        Response.Listener<String> response = response1 -> {
            try {
                JSONObject jsonResponse = new JSONObject(response1);
                boolean success = jsonResponse.getBoolean("success");
                oktitle = success;
                if(success){
                    oktitle = success;
                }
                else{
                    oktitle = success;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {

        };
        con_mobile_change_title get = new con_mobile_change_title(function.getUID(),title,response,errorListener);
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(get);
        return oktitle;
    }

    public void sendcode(String number,String code){
        Response.Listener<String> response = response1 -> {
            try {
                JSONObject jsonResponse = new JSONObject(response1);
                boolean success = jsonResponse.getBoolean("success");
                oktitle = success;
                if(success){
                    Log.d("CODE",code);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {

        };
        con_mobile_change_contact_verify get = new con_mobile_change_contact_verify(number,code,response,errorListener);
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(get);

    }


    public void updatecontact(String number){
        Response.Listener<String> response = response1 -> {
            try {
                JSONObject jsonResponse = new JSONObject(response1);
                boolean success = jsonResponse.getBoolean("success");
                oktitle = success;
                if(success){
                   dialog.dismiss();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {

        };
        con_mobile_change_contact get = new con_mobile_change_contact(function.getUID(),number,response,errorListener);
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(get);

    }

    public boolean updateName(String fname,String mname,String lname,String exname){
        Response.Listener<String> response = response1 -> {
            try {
                JSONObject jsonResponse = new JSONObject(response1);
                boolean success = jsonResponse.getBoolean("success");
                oktitle = success;
                if(success){
                    oktitle = success;
                }
                else{
                    oktitle = success;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {

        };
        con_mobile_change_name get = new con_mobile_change_name(function.getUID(),fname,mname,lname,exname,response,errorListener);
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(get);
        return oktitle;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

}
