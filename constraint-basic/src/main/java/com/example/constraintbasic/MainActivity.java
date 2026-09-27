package com.example.constraintbasic;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 实验2-3 约束布局（一）：计算器界面。
 * 界面完全由 ConstraintLayout（水平链 + 垂直链）搭建，此处只补充按键交互，
 * 使 "0.0" 输入框能真正响应点击。
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView display;

    /** 当前输入表达式，初始值即文档截图中的 0.0 */
    private final StringBuilder expression = new StringBuilder("0.0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);

        int[] ids = {
                R.id.key0, R.id.key1, R.id.key2, R.id.key3,
                R.id.key4, R.id.key5, R.id.key6, R.id.key7,
                R.id.key8, R.id.key9, R.id.keyDot,
                R.id.keyAdd, R.id.keySub, R.id.keyMul, R.id.keyDiv,
                R.id.keyEq
        };
        for (int id : ids) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        String tag = ((TextView) v).getText().toString();

        if (v.getId() == R.id.keyEq) {
            expression.setLength(0);
            expression.append(Calculator.evaluate(display.getText().toString()));
        } else if (isOperator(tag)) {
            // 连续输入运算符时用新的运算符替换旧的
            if (expression.length() > 0 && isOperator(lastChar())) {
                expression.setCharAt(expression.length() - 1, tag.charAt(0));
            } else {
                expression.append(tag);
            }
        } else if (".".equals(tag)) {
            // 一个小数段里只允许出现一个小数点
            int lastOp = lastOperatorIndex();
            if (expression.indexOf(".", lastOp + 1) < 0) {
                expression.append('.');
            }
        } else {
            // 数字：如果当前段是单个 0，则替换掉它
            int lastOp = lastOperatorIndex();
            String tail = expression.substring(lastOp + 1);
            if ("0".equals(tail)) {
                expression.setLength(expression.length() - 1);
            }
            expression.append(tag);
        }
        display.setText(expression.toString());
    }

    private boolean isOperator(String s) {
        return "+".equals(s) || "\u2212".equals(s) || "\u00D7".equals(s) || "\u00F7".equals(s);
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '\u2212' || c == '\u00D7' || c == '\u00F7';
    }

    private char lastChar() {
        return expression.charAt(expression.length() - 1);
    }

    private int lastOperatorIndex() {
        for (int i = expression.length() - 1; i >= 0; i--) {
            if (isOperator(expression.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
}
