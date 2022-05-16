package com.example.mastermindmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button button;
    SharedPreferences gameSettings;
    SharedPreferences.Editor gameSettingsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameSettings = MainActivity.this.getSharedPreferences(getString(R.string.pref_class),Context.MODE_PRIVATE);
        gameSettingsEditor = gameSettings.edit();

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGameScreen();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.config_menu, menu);
        boolean isRep = gameSettings.getBoolean(getString(R.string.toggle_repeat),false);
        if (isRep) {
            menu.findItem(R.id.isRepeat).setTitle("Disable repetitions");
        } else {
            menu.findItem(R.id.isRepeat).setTitle("Enable repetitions");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.grid4:
                gameSettingsEditor.putInt(getString(R.string.var_count),4);
                gameSettingsEditor.commit();
                Log.d("MenuItem","4");
                return true;
            case R.id.grid6:
                gameSettingsEditor.putInt(getString(R.string.var_count),6);
                gameSettingsEditor.commit();
                Log.d("MenuItem","6");
                return true;
            case R.id.grid8:
                gameSettingsEditor.putInt(getString(R.string.var_count),8);
                gameSettingsEditor.commit();
                Log.d("MenuItem","8");
                return true;
            case R.id.val8:
                gameSettingsEditor.putInt(getString(R.string.const_count),8);
                gameSettingsEditor.commit();
                return true;
            case R.id.val10:
                gameSettingsEditor.putInt(getString(R.string.const_count),10);
                gameSettingsEditor.commit();
                return true;
            case R.id.val12:
                gameSettingsEditor.putInt(getString(R.string.const_count),12);
                gameSettingsEditor.commit();
                return true;
            case R.id.val16:
                gameSettingsEditor.putInt(getString(R.string.const_count),16);
                gameSettingsEditor.commit();
                return true;
            case R.id.isRepeat:
                boolean isRep = gameSettings.getBoolean(getString(R.string.toggle_repeat),false);
                gameSettingsEditor.putBoolean(getString(R.string.toggle_repeat),!isRep);
                gameSettingsEditor.commit();
                if (!isRep == true) {
                    item.setTitle("Disable repetitions");
                } else {
                    item.setTitle("Enable repetitions");
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openGameScreen(){
        Intent intent = new Intent(this,GameActivity.class);
        startActivity(intent);
    }
}