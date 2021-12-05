package com.nera.lang_0x42a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private InterstitialAd mInterstitialAd;

    private String programm = "";
    private String parsing = "";
    private int col = 0;
    private int parsed = 0;
    private static int max_size = 64000;
    private static int[] memory = new int[max_size];
    private static int[] saved_memory = new int[max_size];;
    private boolean run = false;
    private boolean debug = false;
    private double c = 0;

    private Button button_run;
    private EditText edit_programm;
    private LinearLayout linear_input;
    private TextView textview_output;
    private TextView textview_debug;
    private EditText edit_input_char;

    private AlertDialog.Builder dia;

    public void run_task(){
        try {
            while (run) {
                if (!((parsed + 1) > parsing.length())) {
                    _execute(parsing.substring(parsed, parsed + 1));
                    parsed++;
                } else {
                    run = false;
                    button_run.setVisibility(View.VISIBLE);
                    edit_programm.setVisibility(View.VISIBLE);
                    textview_debug.setText(String.format("%s\n Программа завершена", textview_debug.getText()));
                }
            }
        } catch (Exception E){
            textview_debug.setText(String.format("%s\n ERROR: %s", textview_debug.getText(), E.toString()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linear_menu = findViewById(R.id.linear_menu);
        ScrollView vscroll_main = findViewById(R.id.vscroll_main);
        button_run = findViewById(R.id.button_run);
        Button button_info = findViewById(R.id.button_info);
        Button button_sample = findViewById(R.id.button_sample);
        Button button_enter = findViewById(R.id.button_enter);
        LinearLayout linear_main = findViewById(R.id.linear_main);
        edit_programm = findViewById(R.id.edit_programm);
        linear_input = findViewById(R.id.linear_input);
        textview_output = findViewById(R.id.textview_output);
        textview_debug = findViewById(R.id.textview_debug);
        edit_input_char = findViewById(R.id.edit_input_char);
        dia = new AlertDialog.Builder(this);
        linear_input.setVisibility(View.GONE);

        button_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                }
                button_run.setVisibility(View.GONE);
                edit_programm.setVisibility(View.GONE);
                textview_output.setText("");
                textview_debug.setText("");
                col = 0;
                parsed = 0;
                programm = edit_programm.getText().toString().trim();
                parsing = programm;
                run = true;
                debug = false;
                memory = new int[64000];
                run_task();

            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,getString(R.string.i_ads), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
        });

        button_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                dia.setTitle("Справка");
                dia.setMessage(
                        getString(R.string.InfoRu)
                );
                dia.create().show();
            }
        });

        button_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (!(edit_input_char.getText().toString().equals(""))) {
                    try{
                        run = true;
                        linear_input.setVisibility(View.GONE);
                        String temp_str = edit_input_char.getText().toString();
                        char temp_char_array[] = temp_str.toCharArray();
                        char temp_char = temp_char_array[0];
                        memory[col] = temp_char;
                        run_task();
                    } catch (Exception E){
                        textview_debug.setText(String.format("%s\n ERROR: %s", textview_debug.getText(), E.toString()));
                    }
                }
            }
        });

        button_sample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                edit_programm.setText(
                        getString(R.string.SampleHelloWorld)
                       );
            }
        });


    }

    private void initializeLogic() {
        linear_input.setVisibility(View.GONE);
    }

    public void _execute(final String _c) {
        try {
            switch (_c) {
                case "D": {
                    if (debug) {
                        textview_debug.setText(textview_debug.getText().toString().concat("\nОтладка отключена\n"));
                    } else {
                        textview_debug.setText(textview_debug.getText().toString().concat("\nОтладка включена\n"));
                    }
                    debug = !debug;
                    break;
                }
                case "~": {
                    if (debug) {
                        textview_debug.setText(textview_debug.getText().toString().concat("~"));
                    }
                    break;
                }
                case "P": {
                    // todo
                    if (debug) {
                        textview_debug.setText(textview_debug.getText().toString().concat("\nPush\n"));
                    }
                    break;
                }
                case "I": {
                    // todo
                    if (debug) {
                        textview_debug.setText(textview_debug.getText().toString().concat("\nExec\n"));
                    }
                    break;
                }
                case "S": {
                    if (debug) {
                        textview_debug.setText(textview_debug.getText().toString().concat("\nSaved\n"));
                    }
                    saved_memory = memory.clone();
                    break;
                }
                case "L": {
                    if (debug) {
                        textview_debug.setText(textview_debug.getText().toString().concat("\nLoaded\n"));
                    }
                    memory = saved_memory.clone();
                    break;
                }
                case "E": {
                    run = false;
                    button_run.setVisibility(View.VISIBLE);
                    edit_programm.setVisibility(View.VISIBLE);
                    break;
                }
                case "J": {
                    if (parsing.substring(parsed, (int) parsing.length()).contains("$")) {
                        parsed = parsing.substring(parsed, (int) parsing.length()).indexOf("$");
                        if (debug) {
                            textview_debug.setText(textview_debug.getText().toString().concat("\nПереход на метку\n"));
                        }
                    } else {
                        textview_debug.setText(textview_debug.getText().toString().concat("\nОшибка! Метка не найдена\n"));
                    }
                    break;
                }
                case "$": {
                    if (debug) {
                        textview_debug.setText(textview_debug.getText().toString().concat("\nМетка\n"));
                    }
                    break;
                }
                default: {
                    _execute_nbf(_c);
                    break;
                }
            }
        } catch (Exception E){
            textview_debug.setText(String.format("%s\n ERROR: %s", textview_debug.getText(), E.toString()));
        }
    }


    public void _execute_nbf(final String _c) {
        try {
            switch(_c) {
                case "+": {
                    memory[col]++;
                    if (memory[col] == 65537) {
                        memory[col] = 0;
                    }
                    break;
                }
                case "-": {
                    memory[col]--;
                    if (memory[col] < 0) {
                        memory[col] = 65536;
                    }
                    break;
                }
                case ">": {
                    col++;
                    if (col == max_size) {
                        col = 0;
                    }
                    break;
                }
                case "<": {
                    col--;
                    if (col < 0) {
                        col = max_size;
                    }
                    break;
                }
                case ".": {
                    String temp_str = String.valueOf((char)(memory[col]));
                    textview_output.setText(textview_output.getText().toString().concat(temp_str));
                    break;
                }
                case ",": {
                    run = false;
                    edit_input_char.setText("");
                    linear_input.setVisibility(View.VISIBLE);
                    break;
                }
                case "Q": {
                    textview_output.setText(textview_output.getText().toString().concat(programm));
                    break;
                }
                case "C": {
                    memory = new int[max_size];
                    break;
                }
                case "[": {

                    if (memory[col] == 0) {
                        parsed++;
                        while((c > 0) || parsing.charAt(parsed) != ']') {
                            if (parsing.charAt(parsed) == '[') {
                                c++;
                            }
                            if (parsing.charAt(parsed) == ']') {
                                c--;
                            }
                            parsed++;
                        }
                    }
                    break;
                }
                case "]": {

                    if (!(memory[col] == 0)) {
                        parsed--;
                        while((c > 0) || parsing.charAt(parsed) != '[') {
                            if (parsing.charAt(parsed) == ']') {
                                c++;
                            }
                            if (parsing.charAt(parsed) == '[') {
                                c--;
                            }
                            parsed--;
                        }
                        parsed--;
                    }
                    Thread.sleep(1);
                    break;
                }
                default: {
                    if (debug) {
                        textview_debug.setText(textview_debug.getText().toString().concat(_c));
                    }
                    break;
                }
            }
        } catch (Exception E){
            textview_debug.setText(String.format("%s\n ERROR: %s", textview_debug.getText(), E.toString()));
        }
    }


    public void _execute_function(final double _function, final double _param1, final double _param2) {

    }
}