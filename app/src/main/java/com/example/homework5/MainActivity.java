package com.example.homework5;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity{

    ExecutorService threadPool;
    static int storedComplexity;
    ArrayList<Double>  numbers = new ArrayList<>();
    static int newInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar complexity  = findViewById(R.id.seekBar);
        complexity.setMax(20);
        Button generate = findViewById(R.id.buttonGenerate);
        TextView complexityOut = findViewById(R.id.complexityNum);
        threadPool = Executors.newFixedThreadPool(2);
        complexity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                complexityOut.setText(progress+" Times");
                storedComplexity = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threadPool.execute(new DoWork());

            }
        });
    }

    class DoWork implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < storedComplexity; i++) {
                numbers.add(HeavyWork.getNumber());
                System.out.println(HeavyWork.getNumber());
                newInt = i+1;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProgressBar progBar = findViewById(R.id.progressBar);
                        TextView progressLbl = findViewById(R.id.retrievedNum);
                        progressLbl.setText(Integer.toString(newInt) + "/" + Integer.toString(storedComplexity));
                        if(100%storedComplexity != 0) {
                            if(newInt == storedComplexity){
                                int remainder = 100%storedComplexity;
                                progBar.setProgress((newInt * (100 / storedComplexity)) + (remainder));
                            }
                            else {
                                progBar.setProgress(newInt * (100 / storedComplexity));
                            }
                        }
                        else{
                            progBar.setProgress(newInt * (100 / storedComplexity));
                        }
                        ArrayAdapter<Double> numAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1,numbers);
                        ListView numsLv = findViewById(R.id.listView);
                        numsLv.setAdapter(numAdapter);

                    }
                });
            }
        }
    }

}