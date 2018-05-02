package com.getective.kmpjual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class BaseActivity extends AppCompatActivity{
    protected void openNewActivity(Class activity) {
        startActivity(new Intent(this, activity));
        finish();
    }

    protected String getText(EditText editText){
        return editText.getText().toString().trim();
    }
}
