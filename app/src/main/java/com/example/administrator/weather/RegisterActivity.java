package com.example.administrator.weather;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private Button btnReg,btnCancel;
    private EditText name,pwd;
    dbHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getViewItem();
    }
    public void getViewItem(){
        name = (EditText)findViewById(R.id.name);
        pwd = (EditText)findViewById(R.id.pwd);
        btnReg = (Button)findViewById(R.id.reg);
        btnCancel = (Button)findViewById(R.id.cancel);


        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtName = name.getText().toString();
                String edtPass = pwd.getText().toString();
                if(!(edtName.equals("")  ||  edtPass.equals(""))){
                    if(addUser(edtName,edtPass)){
                        Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                        //跳转到登录成功界面
                        Intent intent =new Intent();
                        intent.putExtra("user",name.getText().toString());
                        intent.putExtra("passwd",pwd.getText().toString());
                        intent.setClass(RegisterActivity.this,LoginActivity.class);
                        //启动
                        startActivity(intent);
                        RegisterActivity.this.finish();

                    }
                    else{
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("注册失败").setMessage("注册失败")
                                .setPositiveButton("OK", null).show();
                    }
                }
                else{
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("账号密码不能为空").setMessage("账号密码不能为空")
                            .setPositiveButton("OK", null).show();
                }
            }

            private boolean addUser(String name, String pass) {
                // TODO Auto-generated method stub
                String str = "insert into user values(?,?)";

                database = new dbHelper(getApplicationContext());
                SQLiteDatabase db=database.getReadableDatabase();
                try{
                    db.execSQL(str,new String[]{name,pass});
                    return true;
                }
                catch(Exception e){
                    Log.i("RegisterActivity", "adduser-->error");

                }
                return false;
            }

        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("name","");
                intent.putExtra("pwd","");
                //启动
                intent.setClass(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
