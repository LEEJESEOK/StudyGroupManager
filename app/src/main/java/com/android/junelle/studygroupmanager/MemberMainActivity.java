package com.android.junelle.studygroupmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemberMainActivity extends AppCompatActivity {

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "ID";
    private static final String TAG_NAME = "NAME";
    private static final String TAG_PURPOSE = "purpose";
    private static final String TAG_CONTENTS = "contents";

    TabHost tabHost;

    ListView listView1, listView2, listView3;
    ArrayList<HashMap<String, String>> studyGroupList1, studyGroupList2, studyGroupList3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_main);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else {
            Toast.makeText(getApplication(), SharedPrefManager.getInstance(this).getUsername() + "님 환영합니다.", Toast.LENGTH_LONG).show();
        }

        // start of Tab Setting
        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost.newTabSpec("joined");
        ts1.setContent(R.id.content1);
        ts1.setIndicator("가입한 그룹");
        tabHost.addTab(ts1);

        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost.newTabSpec("created");
        ts2.setContent(R.id.content2);
        ts2.setIndicator("개설한 그룹");
        tabHost.addTab(ts2);

        // 세 번째 Tab. (탭 표시 텍스트:"TAB 3"), (페이지 뷰:"content3")
        TabHost.TabSpec ts3 = tabHost.newTabSpec("advertised");
        ts3.setContent(R.id.content3);
        ts3.setIndicator("홍보 중인 그룹");
        tabHost.addTab(ts3);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
        // end of Tab Setting

        // start ListView Setting
        // 가입한 그룹
        listView1 = findViewById(R.id.listView1);
        studyGroupList1 = new ArrayList<>();
        getDBData(Constants.URL_READ_JOIN_GROUP);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        // 개설한 그룹
        listView2 = findViewById(R.id.listView2);
        studyGroupList2 = new ArrayList<>();
        getDBData(Constants.URL_READ_CREATE_GROUP);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        // 홍보 중인 그룹
        listView3 = findViewById(R.id.listView3);
        studyGroupList3 = new ArrayList<>();
        getDBData(Constants.URL_READ_AD_GROUP);
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.member_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSetting:
                startActivity(new Intent(this, SettingsActivity.class));
                Toast.makeText(this, "You Clicked setting", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuLogout:
                SharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, InitActivity.class));
                break;
        }

        return true;
    }

    private void getDBData(String url) {

        final int id = SharedPrefManager.getInstance(this).getUserID();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("message")) {
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

                params.put("id", String.valueOf(id));

                return params;
            }
        };
    }
/*
    private void getDBData(String url) {
        final String email =

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("email", email);

                return params;
            }
        };
    }*//*
    private void getDBData(String string) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                BufferedReader bufferedReader;

                try {
                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    if (conn != null) {
                        conn.setConnectTimeout(10000);
                        conn.setUseCaches(false);
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            String json;
                            while ((json = bufferedReader.readLine()) != null) {
                                sb.append(json).append("\n");
                            }
                        }
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return "Exception :" + e.getMessage();
                }
            }

            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(string);
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            groupArray = jsonObj.getJSONArray(TAG_RESULTS);


            for (int i = 0; i < groupArray.length(); i++) {
                JSONObject c = groupArray.getJSONObject(i);

                String name = c.getString(TAG_NAME);
                String purpose = c.getString(TAG_PURPOSE);
                String contents = c.getString(TAG_CONTENTS);

                HashMap<String, String> group = new HashMap<>();

                group.put(TAG_NAME, name);
                group.put(TAG_PURPOSE, purpose);
                group.put(TAG_CONTENTS, contents);

                studyGroupList3.add(group);
            }

            ListAdapter adapter = new SimpleAdapter(
                    MemberMainActivity.this, studyGroupList3, R.layout.member_main_list_item,
                    new String[]{TAG_NAME, TAG_PURPOSE, TAG_CONTENTS},
                    new int[]{R.id.textViewName, R.id.textViewPurpose, R.id.textViewContents}
            );

            listView3.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
*/
}