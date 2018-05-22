package com.android.junelle.studygroupmanager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JoinActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextPasswordCheck, editTextPhone;
    private Button buttonJoin;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordCheck = findViewById(R.id.editTextPasswordCheck);
        editTextPhone = findViewById(R.id.editTextPhone);

        buttonJoin = findViewById(R.id.buttonJoin);
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextEmail.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "이메일 주소를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editTextPassword.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editTextPassword.getText().toString().length() < 4 || editTextPassword.getText().toString().length() > 16) {
                    Toast.makeText(getApplicationContext(), "비밀번호는 4~16자로 입력가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!editTextPasswordCheck.getText().toString().equals(editTextPassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "입력한 비밀번호가 서로 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editTextUsername.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                registerUser();
            }
        });

        progressDialog = new ProgressDialog(this);
    }

    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString();

        progressDialog.setMessage("Registering user...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("message")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(JoinActivity.this);
                                if (jsonObject.getString("message").equals("User registered successfully")) {
                                    alertBuilder.setTitle("알림");
                                    alertBuilder.setMessage("성공적으로 등록되었습니다!");
                                    alertBuilder.setCancelable(true);
                                    alertBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();

                                            intent.putExtra("email", email);

                                            setResult(RESULT_OK, intent);
                                            finish();
                                        }
                                    });
                                    AlertDialog dialog = alertBuilder.create();
                                    dialog.show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("ERROR", error.toString());

                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        Toast.makeText(getApplicationContext(), "서버와 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("phone", phone);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}