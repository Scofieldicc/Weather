package com.example.administrator.weather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private AutoCompleteTextView autoCompleteTextView;
    private Button bt;
    private EditText et_pwd;
    private TextView reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.et_username);
        bt = (Button) findViewById(R.id.login);
        et_pwd = (EditText)findViewById(R.id.et_password);
        reg = findViewById(R.id.reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


        Intent intent = getIntent();
        autoCompleteTextView.setText(intent.getStringExtra("user"));
        et_pwd.setText(intent.getStringExtra("passwd"));

        autoCompleteTextView.addTextChangedListener(textWatcher);
        bt.setOnClickListener(new MyOnClickListener());
        SharedPreferences sp = getSharedPreferences("user",Context.MODE_PRIVATE);
        Map<String,String> map=(Map<String,String>)sp.getAll();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        ArrayList al = new ArrayList();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            al.add(next.getKey());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, al);
        autoCompleteTextView.setAdapter(adapter);
    }
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            SharedPreferences sp = getSharedPreferences("user",Context.MODE_PRIVATE);

            et_pwd.setText(sp.getString(autoCompleteTextView.getText().toString(),""));
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    private final class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            userSave();
            String name = autoCompleteTextView.getText().toString();
            String pass = et_pwd.getText().toString();
            if(name.equals("")||pass.equals("")){
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("错误")
                        .setMessage("账号或密码不能为空")
                        .setPositiveButton("确定",null)
                        .show();
            }
            else{
                Judge(name,pass);
            }


        }
        private void Judge(String name, String pass) {
            // TODO Auto-generated method stub
            String sql = "select * from user where name=? " +
                    "and password=?";
            SQLiteDatabase db = new dbHelper(getApplicationContext()).getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, new String[]{name, pass});
            if (cursor.getCount() <= 0) {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("错误")
                        .setMessage("账号或密码错误")
                        .setPositiveButton("确定", null)
                        .show();
            } else {
                Main();
            }
        }
        private void Main() {
            // TODO Auto-generated method stub
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }
    }

    private void userSave(){
        SharedPreferences sp = getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(autoCompleteTextView.getText().toString(),et_pwd.getText().toString());
        editor.commit();

    }

}

