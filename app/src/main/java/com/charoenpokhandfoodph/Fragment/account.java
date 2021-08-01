package com.charoenpokhandfoodph.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

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
import com.charoenpokhandfoodph.connection.con_mobile_change_password;
import com.charoenpokhandfoodph.connection.con_mobile_change_password_verify;
import com.charoenpokhandfoodph.connection.con_mobile_change_time;
import com.charoenpokhandfoodph.connection.con_mobile_change_title;
import com.charoenpokhandfoodph.function;
import com.charoenpokhandfoodph.modal.completedlist;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;

import com.suke.widget.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private String stropentime,strclosetime;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private CountDownTimer mCountDownTimer;
    private String SpecialCode = null;


    private CardView frameOne, frameTwo, frameThree, frameFour;
    private boolean isAtLeast8 = false, hasUppercase = false, hasNumber = false, hasSymbol;
    private String str_opentime,str_closetime;



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
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.full_screen_dialog);
                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.change_password_verify, null);

                            MaterialButton back = view.findViewById(R.id.back);
                            back.setOnClickListener(v1 -> dialog.dismiss());

                            TextInputEditText currentpassword = view.findViewById(R.id.currentpassword);
                            MaterialButton verify = view.findViewById(R.id.verify);
                            verify.setOnClickListener(v1 -> {
                                String getcurrentPassword = currentpassword.getText().toString();

                                if(getcurrentPassword.isEmpty()){
                                    currentpassword.requestFocus();
                                    function.toast(v1.getContext(),"Enter your password");
                                }
                                else{
                                    Response.Listener<String> responsep = response2 -> {
                                        try {
                                            JSONObject jsonResponses = new JSONObject(response2);
                                            boolean successs = jsonResponses.getBoolean("success");
                                            if(successs){
                                                dialog.dismiss();
                                                bottomsheetEditor(3,"Change Password",null,null,null,null,null,null);
                                            }
                                            else{
                                               function.toast(v1.getContext(),"Invalid Password");
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    };
                                    Response.ErrorListener errorListener = error -> {

                                    };
                                    con_mobile_change_password_verify get = new con_mobile_change_password_verify(function.getUID(),getcurrentPassword,responsep,errorListener);
                                    get.setRetryPolicy(new DefaultRetryPolicy(
                                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    RequestQueue queue = Volley.newRequestQueue(getContext());
                                    queue.add(get);
                                }
                            });



                            builder.setView(view);
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
                        });

                        cardtime.setOnClickListener(v -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.full_screen_dialog);
                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.change_open_close_time, null);

                            MaterialButton back = view.findViewById(R.id.back);
                            back.setOnClickListener(v1 -> dialog.dismiss());

                            TabLayout tablayout = view.findViewById(R.id.tablayout);
                            TimePicker timeopen = view.findViewById(R.id.timeopen);
                            TimePicker timeclose = view.findViewById(R.id.timeclose);
                            MaterialButton savetime = view.findViewById(R.id.savetime);
                            tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                @Override
                                public void onTabSelected(TabLayout.Tab tab) {
                                    if(tab.getPosition() == 0){
                                        timeopen.setVisibility(View.VISIBLE);
                                        timeclose.setVisibility(View.GONE);
                                    }
                                    else{
                                        timeopen.setVisibility(View.GONE);
                                        timeclose.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {

                                }

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {

                                }
                            });


                            timeopen.setIs24HourView(false);
                            timeclose.setIs24HourView(false);
                            timeopen.setOnTimeChangedListener((view1, hourOfDay, minute) -> {
                                String am_pm = "";
                                Calendar datetime = Calendar.getInstance();
                                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                datetime.set(Calendar.MINUTE, minute);

                                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                                    am_pm = "AM";
                                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                                    am_pm = "PM";

                                String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";
                                str_opentime = strHrsToShow+":"+getDD(datetime.get(Calendar.MINUTE))+" "+am_pm;
                            });


                            timeclose.setOnTimeChangedListener((view1, hourOfDay, minute) -> {
                                String am_pm = "";
                                Calendar datetime = Calendar.getInstance();
                                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                datetime.set(Calendar.MINUTE, minute);

                                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                                    am_pm = "AM";
                                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                                    am_pm = "PM";

                                String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";
                                str_closetime = strHrsToShow+":"+getDD(datetime.get(Calendar.MINUTE))+" "+am_pm;
                            });

                            try {

                                timeopen.setCurrentHour(Integer.valueOf(object.getString("opentime").split(":")[0]));
                                timeclose.setCurrentHour(Integer.valueOf(object.getString("closetime").split(":")[0]));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            savetime.setOnClickListener(v1 -> {
//                                function.toast(v1.getContext(), str_opentime + " " + str_closetime);
                                new SweetAlertDialog(v1.getContext(), SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Are you sure?")
                                        .setContentText("You want to update your opening and closing time to your branch? " + str_opentime + " - " +str_closetime)
                                        .setConfirmText("YES")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                Response.Listener<String> response = response1 -> {
                                                    try {
                                                        JSONObject jsonResponse = new JSONObject(response1);
                                                        boolean success = jsonResponse.getBoolean("success");
                                                        if(success){
                                                            new SweetAlertDialog(v.getContext(),SweetAlertDialog.SUCCESS_TYPE)
                                                                    .setTitleText("Changed Successfully!")
                                                                    .showCancelButton(false)
                                                                    .show();
                                                           dialog.dismiss();
                                                           data();

                                                        }


                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                };
                                                Response.ErrorListener errorListener = error -> {

                                                };
                                                con_mobile_change_time get = new con_mobile_change_time(function.getUID(),str_opentime,str_closetime,response,errorListener);
                                                get.setRetryPolicy(new DefaultRetryPolicy(
                                                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                RequestQueue queue = Volley.newRequestQueue(getContext());
                                                queue.add(get);
                                            }
                                        })
                                        .setCancelButton("NO", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();


                            });


                            builder.setView(view);
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

    private String getDD(int num) {
        return num > 9 ? "" + num : "0" + num;
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

        //4
        MaterialButton savepassword = view.findViewById(R.id.savepassword);
        frameOne = view.findViewById(R.id.frameOne);
        frameTwo = view.findViewById(R.id.frameTwo);
        frameThree = view.findViewById(R.id.frameThree);
        frameFour = view.findViewById(R.id.frameFour);
        TextInputEditText password = view.findViewById(R.id.password);
        TextInputEditText newpassword = view.findViewById(R.id.confirmpassword);


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
                     new SweetAlertDialog(v.getContext(),SweetAlertDialog.SUCCESS_TYPE)
                             .setTitleText("Changed Successfully!")
                             .showCancelButton(false)
                             .show();

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
                        new SweetAlertDialog(v.getContext(),SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Changed Successfully!")
                                .showCancelButton(false)
                                .show();

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

        else if(type == 3){
             conPassword.setVisibility(View.VISIBLE);
             password.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        registrationDataCheck(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
             savepassword.setOnClickListener(v->{
                 String getpassword = password.getText().toString();
                 String getnewPassword = newpassword.getText().toString();

                 if(getpassword.isEmpty()){
                     password.requestFocus();
                     function.toast(v.getContext(),"Enter new password");
                 }
                 else if(!getpassword.equals(getnewPassword)){
                     newpassword.requestFocus();
                     function.toast(v.getContext(),"Password not match");
                 }
                 else{
                     Response.Listener<String> responsep = response2 -> {
                         try {
                             JSONObject jsonResponses = new JSONObject(response2);
                             boolean successs = jsonResponses.getBoolean("success");
                             if(successs){
                                bottomSheetDialog.dismiss();
                                 new SweetAlertDialog(v.getContext(),SweetAlertDialog.SUCCESS_TYPE)
                                         .setTitleText("Changed Successfully!")
                                         .showCancelButton(false)
                                         .show();
                             }

                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     };
                     Response.ErrorListener errorListener = error -> {

                     };
                     con_mobile_change_password get = new con_mobile_change_password(function.getUID(),getnewPassword,responsep,errorListener);
                     get.setRetryPolicy(new DefaultRetryPolicy(
                             DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                             DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                             DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                     RequestQueue queue = Volley.newRequestQueue(getContext());
                     queue.add(get);
                 }
             });



         }

        bottomSheetDialog.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.show();
    }

    @SuppressLint("ResourceType")
    private void registrationDataCheck(String password) {


        if (password.length() >= 8) {
            isAtLeast8 = true;
            frameOne.setCardBackgroundColor(Color.parseColor(getString(R.color.success_stroke_color)));
        } else {
            isAtLeast8 = false;
            frameOne.setCardBackgroundColor(Color.parseColor("#A4AA9D"));

        }
        if (password.matches("(.*[A-Z].*)")) {
            hasUppercase = true;
            frameTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.success_stroke_color)));
        } else {
            hasUppercase = false;
            frameTwo.setCardBackgroundColor(Color.parseColor("#A4AA9D"));
        }
        if (password.matches("(.*[0-9].*)")) {
            hasNumber = true;
            frameThree.setCardBackgroundColor(Color.parseColor(getString(R.color.success_stroke_color)));
        } else {
            hasNumber = false;
            frameThree.setCardBackgroundColor(Color.parseColor("#A4AA9D"));
        }
        if (password.matches("^(?=.*[_.()@]).*$")) {
            hasSymbol = true;
            frameFour.setCardBackgroundColor(Color.parseColor(getString(R.color.success_stroke_color)));
        } else {
            hasSymbol = false;
            frameFour.setCardBackgroundColor(Color.parseColor("#A4AA9D"));
        }


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
