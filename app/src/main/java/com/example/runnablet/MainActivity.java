package com.example.runnablet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    TextView result;
    TextView taskThird;
    TextView time;
    Button button;
    Button startCounter;
    Button taskThirdButton;
    ProgressBar progressBar;
    Handler handler;
    int counterSeconds = 0;
    boolean Login;
    boolean Download;
    boolean counter;
    private Runnable counterRunnable = new Runnable() {
        @Override
        public void run() {
            time.setText("Seconds : " + ++counterSeconds);
            handler.postDelayed(this, 1000);
        }
    };

    class LoginTask implements Runnable {
        @Override
        public void run() {
            Random random = new Random(System.currentTimeMillis());
            try {
                Thread.sleep((random.nextInt(3) + 3) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            Login = random.nextBoolean();
        }
    }

    class DownloadImage implements Runnable {
        @Override
        public void run() {
            Random random = new Random(System.currentTimeMillis());
            try {
                Thread.sleep((random.nextInt(4) + 2) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Download = random.nextBoolean();
        }
    }

    public class Login implements Runnable{
        private LoginCheck validator;

        Login(LoginCheck validator) {
            this.validator = validator;
        }

        @Override
        public void run() {
            boolean isEmailValid = validator.checkMail(email.getText().toString());
            boolean isPasswordValid = validator.checkPass(password.getText().toString());

            final String stringResult;
            if (isEmailValid && isPasswordValid) {
                stringResult = "Success";
            } else {
                stringResult = "Try again!";
            }

            runOnUiThread(() -> {
                result.setVisibility(View.VISIBLE);
                result.setText(stringResult);
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        result = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        button.setOnClickListener((e) -> {
            new Thread(new Login(new LoginCheck())).start();
        });

        handler = new Handler();

        time = findViewById(R.id.Time);
        time.setText("");
        startCounter = findViewById(R.id.start);
        startCounter.setOnClickListener((e) -> {
            if (!counter) {
                handler.postDelayed(counterRunnable, 1000);
                startCounter.setText("Pause");
                counter = true;
            } else {
                handler.removeCallbacks(counterRunnable);
                startCounter.setText("Start");
                counter = false;
            }
        });

        taskThird = findViewById(R.id.textView3);
        taskThirdButton = findViewById(R.id.task3);
        progressBar = findViewById(R.id.progressBar);
        taskThirdButton.setOnClickListener((l) -> {
            progressBar.setVisibility(View.VISIBLE);
            taskThird.setVisibility(View.INVISIBLE);
            Thread thread1 = new Thread(new DownloadImage());
            Thread thread2 = new Thread(new LoginTask());
            thread1.start();
            thread2.start();

            new Thread(() -> {
                try {
                    thread1.join();
                    thread2.join();
                } catch (InterruptedException e) {
                    runOnUiThread(() -> taskThird.setText("Sommething happened please try again!"));
                    return;
                }

                if (Download && Login) {
                    runOnUiThread(() ->taskThird.setText("Success!"));
                } else {
                    runOnUiThread(() ->taskThird.setText("Unssuccessfull"));
                }

                runOnUiThread(() -> {
                    taskThird.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                });

            }).start();

        });

    }
}
