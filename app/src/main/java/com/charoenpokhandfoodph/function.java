package com.charoenpokhandfoodph;

import android.content.Context;
import android.content.Intent;

import com.airbnb.lottie.animation.content.Content;
import static maes.tech.intentanim.CustomIntent.customType;

public class function {

    public static void intent(Class<?> activity, Context context){
        Intent i = new Intent(context,activity);
        context.startActivity(i);
        customType(context,config.ltr);
    }
}
