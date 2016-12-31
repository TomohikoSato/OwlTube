package com.example.tomohiko_sato.owltube.presentation.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.tomohiko_sato.owltube.R;

public class SettingActivity extends AppCompatActivity {

	public static void startSettingActivity(Context context) {
		context.startActivity(new Intent(context, SettingActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
	}

}
