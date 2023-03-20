package edu.harrisburgu.cisc349.loginexample;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class LoginFragment extends Fragment {
    RequestQueue queue;
    TextView usernameField;
    TextView passwordField;
    Button loginButton;

    String url = "http://192.168.0.29:5000/";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_login, container, false);
        loginButton = (Button) v.findViewById(R.id.login_button);
        usernameField = v.findViewById(R.id.user_input);
        passwordField = v.findViewById(R.id.password_input);

        queue = Volley.newRequestQueue(getContext());
        queue.start();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                String data = String.format("{ \"username\":\"%s\", \"password\":\"%s\" }", username, password);

                JsonRequest jsonRequest =
                        new JsonRequest(Request.Method.POST,
                                url, data,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("Login", "responded " + response);
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Login Error", "Error:" + error);
                            }
                        }) {
                            @Override
                            protected Response parseNetworkResponse(NetworkResponse response) {
                                String data = new String(response.data);
                                Response<String> res = Response.success(data, null);
                                Log.d("Login", "parseNetworkResponse called");
                                return res;
                            }
                        };
                queue.add(jsonRequest);
            }
        });

        return v;
    }

}
