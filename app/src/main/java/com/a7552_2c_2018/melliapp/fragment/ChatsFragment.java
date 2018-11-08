package com.a7552_2c_2018.melliapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.ChatActivity;
import com.a7552_2c_2018.melliapp.adapters.BuysAdapter;
import com.a7552_2c_2018.melliapp.adapters.ChatsAdapter;
import com.a7552_2c_2018.melliapp.adapters.ItemAdapter;
import com.a7552_2c_2018.melliapp.model.BuyItem;
import com.a7552_2c_2018.melliapp.model.ChatItem;
import com.a7552_2c_2018.melliapp.model.PostItem;
import com.a7552_2c_2018.melliapp.singletons.SingletonConnect;
import com.a7552_2c_2018.melliapp.singletons.SingletonUser;
import com.a7552_2c_2018.melliapp.utils.PopUpManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ChatsFragment extends Fragment {

    private static final String TAG = "ChatsFragment";

    List<ChatItem> chatList;

    @BindView(R.id.fcsRecycler) RecyclerView recyclerView;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chats, container, false);


        ButterKnife.bind(this, v);

        chatList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        getChats();

        final GestureDetector mGestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                try {
                    View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                    if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                        int position = recyclerView.getChildAdapterPosition(child);
                        ChatsAdapter aux = (ChatsAdapter) recyclerView.getAdapter();
                        String chatId = aux.getChatItem(position).getChatId();
                        String title = aux.getChatItem(position).getTitle();
                        Intent chatIntent = new Intent(getApplicationContext(), ChatActivity.class);
                        chatIntent.putExtra("chatId", chatId);
                        chatIntent.putExtra("title", title);
                        startActivity(chatIntent);
                        return true;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }
        });

        return v;
    }

    private void getChats() {
        String REQUEST_TAG = "getChats";
        String url = getString(R.string.remote_chats) +
                "userChats/" + SingletonUser.getInstance().getUser().getFacebookID() + ".json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                getChatsResponse(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, volleyError.toString());
            }
        });

        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(request,REQUEST_TAG);
    }

    private void getChatsResponse(String response) {
        Log.d(TAG, response);
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray chats = obj.getJSONArray("chats");
            for (int i=0; i<chats.length(); i++){
                addChat(chats.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addChat(final String chatId) {

        String REQUEST_TAG = "getChat";
        String url = getString(R.string.remote_chats) +
                "chats/" + chatId + ".json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                addChatResponse(s, chatId);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, volleyError.toString());
            }
        });

        SingletonConnect.getInstance(getApplicationContext()).addToRequestQueue(request,REQUEST_TAG);
    }

    private void addChatResponse(String s, String chatId) {
        Log.d(TAG, s);
        try {
            JSONObject obj = new JSONObject(s);
            ChatItem aux = new ChatItem();
            aux.setChatId(chatId);
            JSONArray pictures = obj.getJSONArray("picture");
            aux.setImage(pictures.getString(0));
            aux.setPublicationId(obj.getString("publicationId"));
            aux.setTitle(obj.getString("title"));
            chatList.add(aux);
            RecyclerView.Adapter mAdapter = new ChatsAdapter(chatList);
            recyclerView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}