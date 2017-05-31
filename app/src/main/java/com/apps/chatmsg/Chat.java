package com.apps.chatmsg;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {


    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    DatabaseReference databaseReference1,databaseReference2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout)findViewById(R.id.layout1);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        databaseReference1 = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.messages+UserDetails.username+"_"+UserDetails.chatWith);
        databaseReference2 = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.messages+UserDetails.chatWith+"_"+UserDetails.username);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("message",messageText);
                    map.put("user",UserDetails.username);
                    databaseReference1.push().setValue(map);
                    databaseReference2.push().setValue(map);
                }

                messageArea.setText("");
            }
        });


        databaseReference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Map map = dataSnapshot.getValue(Map.class);

                Map <String, String> map = (Map)dataSnapshot.getValue();

                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if (userName.equals(UserDetails.username)){
                    addMessageBox("You :- \n "+message,1);
                }else {
                    addMessageBox(UserDetails.chatWith+" :- \n "+message,2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addMessageBox(String s, int i) {
        TextView textView = new TextView(Chat.this);
        textView.setText(s);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5,5,5,5);


        if (i == 1){
            layoutParams.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.rounded_corner1);
        }else {
            layoutParams.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.rounded_corner2);
        }
        textView.setPadding(5,5,5,5);
        textView.setLayoutParams(layoutParams);

        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);

    }

}
