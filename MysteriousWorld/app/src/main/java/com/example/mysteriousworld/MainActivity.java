package com.example.mysteriousworld;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private Button btSend;
    private ImageView img1;
    private float mx, my;
    private Drawable fox_f, fox_r, fox_l, fox_b;
    private Drawable chili_f, chili_r, chili_l, chili_b;
    private float xNegative = 1, solution = 1;
    private float RR = 100;
    final Handler handler = new Handler();
    private Runnable myRunnable, myRunnable1;
    final int delay = 100; // 1000 milliseconds == 1 second
    private TextView tvTimeSkill1, tvTimeSkill2, tvTimeSkill3;
    private float v = 0;
    CustomePoint pT, pS, pC;
    private float xDown = 0, yDown = 0;
    float distanceX, distanceY;
    private ProgressBar progressBar1, progressBar2, progressBar3;
    private RelativeLayout btOmnidirectional, rlMain, rlCharacter;
    private float xCenter, yCenter;
    private Button btTimeCoolDownSkill1, btTimeCoolDownSkill2,btTimeCoolDownSkill3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mw_cradle);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        addControls();
        addEvents();
    }

    private void addControls() {
        btTimeCoolDownSkill1 = findViewById(R.id.btTimeCoolDownSkill1);
        btTimeCoolDownSkill2 = findViewById(R.id.btTimeCoolDownSkill2);
        btTimeCoolDownSkill3 = findViewById(R.id.btTimeCoolDownSkill3);
        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar3 = findViewById(R.id.progressBar3);
        tvTimeSkill1 = findViewById(R.id.tvTimeSkill1);
        tvTimeSkill2 = findViewById(R.id.tvTimeSkill2);
        tvTimeSkill3 = findViewById(R.id.tvTimeSkill3);
        rlCharacter = findViewById(R.id.rlCharacter);
        rlMain = findViewById(R.id.rlMain);
        btOmnidirectional = findViewById(R.id.btOmnidirectional);
        pS = new CustomePoint();
        pC = new CustomePoint(rlCharacter.getX(), rlCharacter.getY());
        fox_f =(Drawable)getResources().getDrawable(R.mipmap.fox_f);
        fox_r =(Drawable)getResources().getDrawable(R.mipmap.fox_r);
        fox_l =(Drawable)getResources().getDrawable(R.mipmap.fox_l);
        fox_b =(Drawable)getResources().getDrawable(R.mipmap.fox_b);
        chili_r =(Drawable)getResources().getDrawable(R.mipmap.chili_r);
        chili_l =(Drawable)getResources().getDrawable(R.mipmap.chili_l);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addEvents() {
        btTimeCoolDownSkill1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadRemainTime(7, btTimeCoolDownSkill1, tvTimeSkill1);
            }
        });
        btTimeCoolDownSkill2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadRemainTime(10, btTimeCoolDownSkill2, tvTimeSkill2);
            }
        });
        btTimeCoolDownSkill3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadRemainTime(40, btTimeCoolDownSkill3, tvTimeSkill3);
            }
        });

        btOmnidirectional.post(new Runnable() {
            @Override
            public void run() {
                xCenter = btOmnidirectional.getX();
                yCenter = btOmnidirectional.getY();
                pT = new CustomePoint(xCenter, yCenter);
                pC = new CustomePoint(rlCharacter.getX(), rlCharacter.getY());
            }
        });

        btOmnidirectional.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                myRunnable = new Runnable() {
                    public void run() {
                        if (btOmnidirectional.getX() != xCenter || btOmnidirectional.getY() != yCenter) {
                            float dX = btOmnidirectional.getX()-xCenter;
                            float dY = btOmnidirectional.getY()-yCenter;
                            rlCharacter.setX(rlCharacter.getX()+dX/15);
                            rlCharacter.setY(rlCharacter.getY()+dY/15);
                            if (btOmnidirectional.getX() > xCenter) {
                                loadImage(rlCharacter,6);
                            } else {
                                loadImage(rlCharacter,4);
                            }
                        }
                        handler.postDelayed(this, 6);
                    }
                };
                handler.postDelayed(myRunnable,6);
                rlMain.invalidate();
                return true;
            }
        });
        btOmnidirectional.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        xDown = event.getX();
                        yDown = event.getY();
                        System.out.println(xDown);
                        System.out.println(yDown);
                        break;
                    case MotionEvent.ACTION_MOVE:
                            // out of range move
                            // move button catch and button show
                            distanceX = event.getX() - xDown;
                            distanceY = event.getY() - yDown;
                            btOmnidirectional.setX(btOmnidirectional.getX()+distanceX);
                            btOmnidirectional.setY(btOmnidirectional.getY()+distanceY);
                            pS.setX(btOmnidirectional.getX());
                            pS.setY(btOmnidirectional.getY());
                            xNegative = pS.getX();
                            float dk = (btOmnidirectional.getX()-xCenter)*(btOmnidirectional.getX()-xCenter)+(btOmnidirectional.getY()-yCenter)*(btOmnidirectional.getY()-yCenter);
                            if(dk > RR*RR) {
                                CustomePoint pNewOmni = distanceOfOmnidirectional(pT, pS, RR);
                                btOmnidirectional.setX(pNewOmni.getX());
                                btOmnidirectional.setY(pNewOmni.getY());
                            }
                        System.out.println(event.getX());
                        System.out.println(event.getY());
                            break;
                    case MotionEvent.ACTION_UP:
                        btOmnidirectional.setX(xCenter);
                        btOmnidirectional.setY(yCenter);
                        handler.removeCallbacks(myRunnable);
                        break;
                }
                rlMain.invalidate();
                return false;
            }
        });
    }


    private CustomePoint distanceOfOmnidirectional(CustomePoint pT, CustomePoint pS, float R) {
        float a, b, c, d, e;
        CustomePoint pAnswer = new CustomePoint();
        if (pS.getX() != pT.getX()) {
            a = (pS.getY() - pT.getY()) / (pS.getX() - pT.getX());
            b = pT.getY() - a * pT.getX();
            c = a * a + 1;
            d = -2*(a*a+1)*pT.getX();
            e = (a * a + 1) * pT.getX() * pT.getX() - R * R;
            float x3 = giaiPTBac2(c,d,e);
            pAnswer.setX(x3);
            pAnswer.setY(a*x3+b);
            return pAnswer;
        } else {
            if (btOmnidirectional.getY() < yCenter) {
                pAnswer.setX(xCenter);
                pAnswer.setY(yCenter-99);
            }
            if (btOmnidirectional.getY() > yCenter) {
                pAnswer.setX(xCenter);
                pAnswer.setY(yCenter + 99);
            }
            return pAnswer;
        }
    }
    public float giaiPTBac2(float a, float b, float c) {
        // kiểm tra các hệ số
        if (a == 0) {
            if (b == 0) {
                System.out.println("Phương trình vô nghiệm 1!");
                return xCenter;
            } else {
//                System.out.println("Phương trình có một nghiệm: "
//                        + "x = " + (-c / b));
                return -c / b;
            }
        }
        // tính delta
        float delta = b*b - 4*a*c;
        float x1;
        float x2;
        // tính nghiệm
        if (delta > 0) {
            x1 = (float) ((-b + Math.sqrt(delta)) / (2*a));
            x2 = (float) ((-b - Math.sqrt(delta)) / (2*a));
//            System.out.println("Phương trình có 2 nghiệm là: "
//                    + "x1 = " + x1 + " và x2 = " + x2);
            return (xNegative>=xCenter)?x1:x2;
        } else if (delta == 0) {
            x1 = (-b / (2 * a));
            return x1;
        } else {
            System.out.println( "vn1"+ a + " " + b + " " + c);
            System.out.println("Phương trình vô nghiệm!");
            return xCenter;
        }
    }

    public void loadRemainTime(int duration, Button bt, TextView tv) {
        bt.setEnabled(false);
        bt.setAlpha(0.3f);
        CountDownTimer cTimer =  new CountDownTimer(duration*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                tv.setText(((millisUntilFinished)/1000+1)+"");
            }
            public void onFinish() {
                tv.setText("");
                bt.setEnabled(true);
                bt.setAlpha(0.7f);
            }
        }.start();
    }
    private void loadImage(RelativeLayout rlCharacter, int variable) {
        switch (variable ) {
            case  2:
                break;
            case  4:
                rlCharacter.setBackground(chili_l);
                break;
            case  6:
                rlCharacter.setBackground(chili_r);
                break;
            case  8:
                break;
            default:
                break;
        }
    }
}