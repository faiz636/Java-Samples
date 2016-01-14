package com.rubbersoft.android.valveleakage.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.rubbersoft.android.valveleakage.services.CoreLeakageService;

public class BootCompleteReceiver extends BroadcastReceiver {
    public BootCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, CoreLeakageService.class));
        Toast.makeText(context,"reciver",Toast.LENGTH_SHORT).show();
    }
}
