package com.android.junelle.studygroupmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class InitActivity extends AppCompatActivity {

    Button buttonLogin, buttonJoin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_init);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.init_status_bar));

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();

            startActivity(new Intent(this, MemberMainActivity.class));

            return;
        }

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                startActivityForResult(intent, 0x0010);
            }
        });

        buttonJoin = findViewById(R.id.buttonJoin);
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);

                startActivityForResult(intent, 0x0001);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // result for JoinActivity
        if (requestCode == 0x0001) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                intent.putExtra("email", data.getStringExtra("email"));

                startActivityForResult(intent, 0x0010);
            }
        }

        // result for LoginActivity
        if (requestCode == 0x0010) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(getApplicationContext(), MemberMainActivity.class);

                intent.putExtra("email", data.getStringExtra("email"));

                startActivity(intent);
            }
        }
    }
}