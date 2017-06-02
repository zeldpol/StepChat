package com.example.rinsv.stepchat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URL;

import static com.example.rinsv.stepchat.R.id.tvUser;


public class MainActivity extends FragmentActivity {
    private static final String TAG = "ChatActivity";

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<Message> adapter;
    RelativeLayout activity_main;
    ImageButton buttonSend;
    private ListView listMessages;
    private EditText input;
    private LinearLayout liner;
    Intent intent;
    private boolean side = false;
    public String mainName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        setContentView(R.layout.activity_main);

        activity_main = (RelativeLayout)findViewById(R.id.activity_main);
        buttonSend = (ImageButton)findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                input = (EditText)findViewById(R.id.editText);
                if (!input.getText().toString().equals("")) {
                    FirebaseDatabase.getInstance().getReference().push()
                            .setValue(new Message(input.getText().toString(),
                                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), side));
                    input.setText("");
                }
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(), SIGN_IN_REQUEST_CODE);
            mainName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();


        } else {
            displayChat();
            mainName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }

    }

    private void displayChat() {

        listMessages = (ListView)findViewById(R.id.listView);


        adapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.item, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {

                  LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView textMessage, autor, timeMessage;
                textMessage = (TextView)v.findViewById(R.id.tvMessage);
                autor = (TextView)v.findViewById(tvUser);
                timeMessage = (TextView)v.findViewById(R.id.tvTime);


                autor.setText(model.getAutor());
                textMessage.setText(model.getTextMessage());
                timeMessage.setText(DateFormat.format("HH:mm", model.getTimeMessage()));

                if (autor.getText().equals(mainName)) {
                    textMessage.setBackgroundResource(R.drawable.bubble_a);
                    lp2.gravity = Gravity.RIGHT;
                    autor.setText("");
                    autor.setTextSize(5);


                }else {
                    textMessage.setBackgroundResource(R.drawable.bubble_b);
                    lp2.gravity = Gravity.LEFT;
                    autor.setTextSize(15);
                }

                autor.setLayoutParams(lp2);
                textMessage.setLayoutParams(lp2);
                timeMessage.setLayoutParams(lp2);

            }

        };

        listMessages.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listMessages.setAdapter(adapter);

        //to scroll the list view to bottom on data change
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listMessages.setSelection(adapter.getCount() - 1);
            }
        });

     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                Snackbar.make(activity_main, "Вход выполнен", Snackbar.LENGTH_SHORT).show();
                displayChat();
            } else {
                Snackbar.make(activity_main, "Вход не выполнен", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*if (item.getItemId() == R.id.menu_signout)
        {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Snackbar.make(activity_main, "Выход выполнен", Snackbar.LENGTH_SHORT).show();
                            finish();

                        }
                    });
        }

        */
        return true;
    }

}
