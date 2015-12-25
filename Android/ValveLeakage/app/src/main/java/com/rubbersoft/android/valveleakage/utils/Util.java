package com.rubbersoft.android.valveleakage.utils;

import android.widget.Toast;

import com.rubbersoft.android.valveleakage.ValveLeakageApplication;

/**
 * Created by Muhammad Faizan Khan on 26/12/15.
 */
public class Util {


    public static void successToast(String message) {
        Toast.makeText(ValveLeakageApplication.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void successToast() {
        successToast("Process successful");
    }

    public static void failureToast(String message) {
        Toast.makeText(ValveLeakageApplication.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void failureToast() {
        failureToast("Process failed");
    }
}
