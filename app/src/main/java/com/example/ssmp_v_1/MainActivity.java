package com.example.ssmp_v_1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;

import static com.example.ssmp_v_1.utils.NetworkUtils.generateURL;
import static com.example.ssmp_v_1.utils.NetworkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {

    private EditText et_search_number_call; // Номер вызова
    private EditText et_search_date; // Дата вызова
    private EditText et_search_fio; // Фио в вызове
    private Spinner et_search_close_event; // статус вызова
    private Button b_search_clear; // Кнопка очистить
    private Button b_search_send; // Кнопка отправить
    private TextView et_search_result; // Список вызовов
    String[] search_ssmp_list = { "Все", "Активные", "Завершенные"}; // Выпадающие список в форме



    class QueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = getResponseFromURL(urls[0]);
            }catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response){
            et_search_result.setText(response);
            try {
                JSONArray jsonArray = new JSONArray(response); // получаем ответ от сервера
                TableLayout tblLayout = null;
                tblLayout = (TableLayout) findViewById(R.id.tableLayout);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject list = jsonArray.getJSONObject(i);
                    TableRow tableRow = new TableRow(MainActivity.this);
                    tableRow.setLayoutParams(new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    TextView textView1 = new TextView(MainActivity.this);
                    TextView textView2 = new TextView(MainActivity.this);
                    TextView textView3 = new TextView(MainActivity.this);
                    TextView textView4 = new TextView(MainActivity.this);
                    textView1.setTextColor(Color.WHITE);
                    textView2.setTextColor(Color.WHITE);
                    textView3.setTextColor(Color.WHITE);
                    textView4.setTextColor(Color.WHITE);
                    textView1.setPadding(5, 5, 5, 5);
                    textView2.setPadding(5, 5, 5, 5);
                    textView3.setPadding(5, 5, 5, 5);
                    textView4.setPadding(5, 5, 5, 5);

                    textView1.setText(list.getString("name"));
                    textView2.setText(list.getString("content"));
                    textView3.setText(list.getString("text"));
                    textView4.setText(list.getString("like"));


                    tableRow.addView(textView1);
                    tableRow.addView(textView2);
                    tableRow.addView(textView3);
                    tableRow.addView(textView4);
//                    tableRow.generateLayoutParams(id)
                    tblLayout.addView(tableRow, i);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.et_search_close_event);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, search_ssmp_list);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        // получаем значение из формы
        et_search_number_call = findViewById(R.id.et_search_number_call);
        et_search_date = findViewById(R.id.et_search_date);
        et_search_fio = findViewById(R.id.et_search_fio);
        et_search_close_event = findViewById(R.id.et_search_close_event);
        b_search_clear = findViewById(R.id.b_search_clear);
        b_search_send = findViewById(R.id.b_search_send);
        et_search_result = findViewById(R.id.et_search_result);


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL generatedURL = null;
                try {
                    String number = et_search_number_call.getText().toString();
                    String date = et_search_date.getText().toString();
                    String fio = et_search_fio.getText().toString();
//                    String status = (String)et_search_close_event.getItemAtPosition();
//                    String status = et_search_close_event.getText().toString();
                    generatedURL = generateURL(number,date,fio,"spinner");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                et_search_result.setText(generatedURL.toString());
                new QueryTask().execute(generatedURL);
            }
        };


        b_search_send.setOnClickListener(onClickListener);







//
//        tableRow.setClickable(true);
//        tableRow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                et_search_result.setText("dsdsd");
//////                TableRow t = (TableRow) v;
//////                TextView firstTextView = (TextView) t.getChildAt(0);
//////                TextView secondTextView = (TextView) t.getChildAt(1);
//////                String firstText = firstTextView.getText().toString();
//////                String secondText = secondTextView.getText().toString();
//////
////                et_search_result.setText(tblLayout.toString());
//            }
//        });

    }

}