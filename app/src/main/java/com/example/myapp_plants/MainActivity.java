package com.example.myapp_plants;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    CustomAdapter adapter;
    ListView lv;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.create_db();
    }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.about_settings:
                    myShowToast("");
                    return true;
                case R.id.author_settings:
                    myShowToast("cool.araby@gmail.com");
                    return true;
                case R.id.program_settings:
                    myShowToast("Не забудьте оценить приложение");
                    return true;
            }
            //headerView.setText(item.getTitle());
            return super.onOptionsItemSelected(item);
        }

    private void myShowToast(String message) {
        View layout = getLayoutInflater().inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));

        TextView text = layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private ArrayList getDataPlants()
    {
        ArrayList<Plants> plants=new ArrayList<>();
        Plants plant;
        //=new Plants();
        db = databaseHelper.open();
        Cursor c = db.rawQuery("select *from "+ DatabaseHelper.TABLE, null);
        String S="";
        if (c.moveToFirst()) {
            int name = c.getColumnIndex(DatabaseHelper.COLUMN_NAME);
            int photo = c.getColumnIndex(DatabaseHelper.COLUMN_Photo);
            do {
                plant=new Plants();
                plant.setName(c.getString(name).trim());;
                plant.setImage(c.getString(photo).trim());
                plants.add(plant);
            } while (c.moveToNext());
        }
        return plants;
    }

    private ArrayList getDataPlants(String keyWord)
    {
        ArrayList<Plants> plants=new ArrayList<>();
        Plants plant;
        db = databaseHelper.open();
        String sql="select * from "+ DatabaseHelper.TABLE+" where  " +DatabaseHelper.COLUMN_NAME +" Like '%"+keyWord+"%'";// Like '%"+keyWord+"%'";
        Log.d("sql",sql);
        Cursor c = db.rawQuery(sql, null);
        String S="";
        if (c.moveToFirst()) {
            int Name = c.getColumnIndex("name");
            int photo = c.getColumnIndex("photo");
            do {
                plant=new Plants();
                plant.setName(c.getString(Name).trim());
                plant.setImage(c.getString(photo).trim());
                plants.add(plant);
            } while (c.moveToNext());
        }
        return plants;
    }
    @Override
    public void onResume() {
        super.onResume();
        db = databaseHelper.open();
        lv= (ListView) findViewById(R.id.lv);
        adapter=new CustomAdapter(this,getDataPlants());
        lv.setAdapter(adapter);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        userCursor.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem search_item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setFocusable(false);
        searchView.setQueryHint("Поиск");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                db = databaseHelper.open();
                lv= (ListView) findViewById(R.id.lv);
                adapter=new CustomAdapter(MainActivity.this,getDataPlants(s.toString()));
                lv.setAdapter(adapter);
                return false;
            }
        });
        return true;
    }
}
