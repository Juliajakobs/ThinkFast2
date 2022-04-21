package com.example.thinkFast.networking;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thinkFast.Category;
import com.example.thinkFast.Question;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkManager {

    //private static final String BASE_URL = "https://quiz-app-b.herokuapp.com/";
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static NetworkManager mInstance;
    private static RequestQueue mQueue;
    private Context mContext;

    public static synchronized NetworkManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkManager(context);
        }
        return mInstance;
    }
    //Constructor for the NetworkManager
    private NetworkManager(Context context) {
        mContext = context;
        mQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mQueue;
    }

    // Returns a list of all questions from backend db.
    public void getQuestions(NetworkCallback<List<Question>> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "questions", response -> {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Question>>(){}.getType();
                    List<Question> questions = gson.fromJson(response, listType);
                    callback.onSuccess(questions);
                }, error -> callback.onFailure(error.toString())
        );
        mQueue.add(request);
    }

    // Returns a list of questions from a category (int categoryID).
    public void getQuestionsByCategory(int id, final NetworkCallback<List<Question>> callback){
        String url = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath("questions")
                .appendPath(String.valueOf(id))
                .build().toString();

        StringRequest request = new StringRequest(
                Request.Method.GET, url, response -> {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Question>>(){}.getType();
                    List<Question> questionsByCat = gson.fromJson(response, listType);
                    callback.onSuccess(questionsByCat);
                }, error -> callback.onFailure(error.toString())
        );
        mQueue.add(request);
    }

    // Return a list of all categories from backend db.
    public void getCategories(NetworkCallback<List<Category>> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "categories", response -> {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Category>>(){}.getType();
                    List<Category> categories = gson.fromJson(response, listType);
                    callback.onSuccess(categories);
                }, error -> callback.onFailure(error.toString())
        );
        mQueue.add(request);
    }
    public void postScore(String username, String score){
       StringRequest stringRequest = new StringRequest(Request.Method.POST,BASE_URL + "saveScore",
               response -> Log.d("NetworkManager","Success"),
               error -> Log.d("Networkmanager","Error")){
           @Override
           protected Map<String,String>getParams()throws AuthFailureError{
               Map<String,String> params = new HashMap<>();
               params.put("score",score);
               params.put("username",username);
               return params;
           }
       };
        mQueue.add(stringRequest);
    }
 }