package com.example.multigps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ExampleActivity extends AppCompatActivity {

    private static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        StringRequest request = new StringRequest(Request.Method.POST, Constant.LOGIN, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getBoolean("success"))
                {
                    JSONObject data = object.getJSONObject("data");
                    JSONObject user = data.getJSONObject("user");
                    SharedPreferences userPref = this.getApplicationContext().getSharedPreferences("user", ctx.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("token", object.getString("token"));
                    editor.putString("name", user.getString("name"));
                    editor.putString("email", user.getString("email"));
                    editor.putString("photo", user.getString("photo"));
                    editor.apply();
                }
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                HashMap<String, String> map = new HashMap<>();
                map.put("email", "cubas@unitru.edu.pe");
                map.put("password", "12345678");
                return map;
            }
        };

        RequestQueue queue = new Volley().newRequestQueue(ctx.getApplicationContext());
        queue.add(request);

    }
}