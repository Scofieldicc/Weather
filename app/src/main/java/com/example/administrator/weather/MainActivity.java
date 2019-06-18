package com.example.administrator.weather;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.administrator.weather.Bean.TodayWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler() ;
    private TextView lowestTem,wet,wind,tem,weather;
    private Button serach;
    private EditText searchET;
    private SimpleAdapter simpleAdapter,simpleAdapter2;
    private ArrayList<Map<String,Object>> list,list2;
    private ListView listView,recyclerview;
    private ArrayAdapter<String> arr_Adapter;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serach = findViewById(R.id.search);
       lowestTem = findViewById(R.id.lowestTem);
        searchET = findViewById(R.id.searchET);
        wet = findViewById(R.id.wet);
        wind = findViewById(R.id.wind);
        tem = findViewById(R.id.tem);
        weather = findViewById(R.id.weather);
        listView = findViewById(R.id.listView);
        recyclerview = findViewById(R.id.recyclerview);
        TodayWeather w = new TodayWeather();
        ll = findViewById(R.id.bg);
        ll.getBackground().setAlpha(100);


    }

    public void click(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                sendRequestWithHttpURLConnection();
            }
        }).start();
    }



    protected void sendRequestWithHttpURLConnection() {
        new Thread() {
            @Override
            public void run() {
                URL url;
                HttpURLConnection connection = null;
                try {
                    String cityName = searchET.getText().toString().trim();
                    url = new URL(
                            "http://v.juhe.cn/weather/index?format=2&cityname="
                                    + cityName
                                    + "&key=8b1a2022e5225930676934678efcce0b");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    // 下面对获取到的输入流进行读取
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    //parseWithJSON(response.toString());
                    ParseWeatherWithJSON(response.toString());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }.start();

    }

   private void ParseWeatherWithJSON(String response) {

        try {
            JSONObject jsonObject=new JSONObject(response);
            String resultcode=jsonObject.getString("resultcode");
            if(resultcode.equals("200")){
                JSONObject resultObject=jsonObject.getJSONObject("result");
                JSONObject Object=resultObject.getJSONObject("sk");

                lowestTem.setText(Object.getString("temp")+"℃");
                wet.setText("湿度："+Object.getString("humidity"));
                wind.setText(Object.getString("wind_direction")+":"+Object.getString("wind_strength"));

                Object =resultObject.getJSONObject("today");

                //更换背景图片
                String s = Object.getString("weather");
                //根据天气唯一标识fa获取准确数据，懒得写了。。。
                switch (s.substring(s.length()-1)){
                    case "云":
                        ll.setBackgroundResource(R.drawable.bg_yun);
                        break;
                    case "雨":
                        ll.setBackgroundResource(R.drawable.bg_yu);
                        break;
                    case "晴":
                        ll.setBackgroundResource(R.drawable.bg_qing);
                        break;
                    case "雪":
                        ll.setBackgroundResource(R.drawable.bg_xue);
                        break;
                    default:
                        ll.setBackgroundResource(R.drawable.bg_yun);
                        break;
                }

                tem.setText(Object.getString("temperature"));
                weather.setText(Object.getString("weather"));
              //  String[] data = {Object.getString("dressing_advice"),Object.getString("uv_index")};
               // arr_Adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,data);

               list = new ArrayList<Map<String, java.lang.Object>>();
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("text","晨练指数");
                map.put("content",Object.getString("exercise_index"));
                list.add(map);
                map = new HashMap<String, Object>();
                map.put("text","洗车指数");
                map.put("content",Object.getString("wash_index"));
                list.add(map);
                map = new HashMap<String, Object>();
                map.put("text","穿衣指数");
                map.put("content",Object.getString("dressing_index"));
                list.add(map);
                map = new HashMap<String, Object>();
                map.put("text","穿衣建议");
                map.put("content",Object.getString("dressing_advice"));
                list.add(map);
                simpleAdapter = new SimpleAdapter(this,list,R.layout.item,new String[]{"text","content"},new int[]{R.id.theType,R.id.theContent});


                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listView.setAdapter(simpleAdapter);
                            }
                        });
                    }
                }.start();


                JSONArray arr = resultObject.getJSONArray("future");
                list2 = new ArrayList<Map<String, java.lang.Object>>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject temp = (JSONObject) arr.get(i);
                    Map<String,Object> map2 = new HashMap<String, Object>();
                    String str = temp.getString("weather");
                    //根据天气唯一标识fa获取准确数据，懒得写了。。。
                    switch (str.substring(str.length()-1)){
                        case "阴":
                            map2.put("weatherFuture",R.drawable.yin);
                            break;
                        case "雨":
                            map2.put("weatherFuture",R.drawable.yu);
                            break;
                        case "晴":
                            map2.put("weatherFuture",R.drawable.qing);
                            break;
                        case "雪":
                            map2.put("weatherFuture",R.drawable.xue);
                            break;
                        default:
                            map2.put("weatherFuture",R.drawable.yin);
                            break;
                    }
                    map2.put("temperature",temp.getString("temperature"));
                    map2.put("date",temp.getString("date"));
                    Log.i("testf",temp.getString("date")+temp.getString("weather"));
                    list2.add(map2);
                }
                simpleAdapter2 = new SimpleAdapter(this,list2,R.layout.weather_item,new String[]{"date","weatherFuture","temperature"},new int[]{R.id.date,R.id.weatherFuture,R.id.temperature});


                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                recyclerview.setAdapter(simpleAdapter2);
                            }
                        });
                    }
                }.start();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    protected void parseWithJSON(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String version = jsonObject.getString("version");
                Log.d("MainActivity", "id=" + id + "name=" + name + "version="
                        + version);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //重写返回方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dialog();
            return false;
        }
        return false;
    }

    private void dialog() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("确定要退出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exit();
                        dialog.dismiss();
                        MainActivity.this.finish();

                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
    private void exit(){
        SharedPreferences sp=getSharedPreferences("sp",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean("state",true);
        editor.commit();

        finish();
    }

}
