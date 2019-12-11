package com.example.bookreminder.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.example.bookreminder.MainActivity;
import com.example.bookreminder.R;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);

        TextView textView = findViewById(R.id.loginTx);
        TextView tV = findViewById(R.id.registerTx);

        String text = "Già Registrato? Clicca Qua";
        String text1 = "Registrati Ora";

        SpannableString ss = new SpannableString(text);
        SpannableString ss2 = new SpannableString(text1);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(LoginActivity.this, LoginFrameActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };


        ss.setSpan(clickableSpan1, 15, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(clickableSpan2, 11, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        tV.setText(ss2);
        tV.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
