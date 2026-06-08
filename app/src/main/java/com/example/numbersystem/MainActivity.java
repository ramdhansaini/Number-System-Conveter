package com.example.numbersystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText ed;
    private TextView tv, binaryTextView, octalTextView;
    private Spinner sp1, sp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //1. creat object
        Button bt1, bt2, bt3, bt4, bt5, bt6, bt7, btA, bt8, bt9, bt0, btB, btC, btD, btE, btF, btCl, btDot, btAC, btEqual;

        //2.Bind UI with java
        bt0 = findViewById(R.id.but0);
        bt1 = findViewById(R.id.but1);
        bt2 = findViewById(R.id.but2);
        bt3 = findViewById(R.id.but3);
        bt4 = findViewById(R.id.but4);
        bt5 = findViewById(R.id.but5);
        bt6 = findViewById(R.id.but6);
        bt7 = findViewById(R.id.but7);
        bt8 = findViewById(R.id.but8);
        bt9 = findViewById(R.id.but9);
        btA = findViewById(R.id.butA);
        btB = findViewById(R.id.butB);
        btC = findViewById(R.id.butC);
        btD = findViewById(R.id.butD);
        btE = findViewById(R.id.butE);
        btF = findViewById(R.id.butF);
        btCl = findViewById(R.id.butClear);
        btDot = findViewById(R.id.butDot);
        btAC = findViewById(R.id.butAC);
        btEqual = findViewById(R.id.butEqual);
        ed = findViewById(R.id.input);
        tv = findViewById(R.id.resultText);
        sp1 = findViewById(R.id.spinnerFrom);
        sp2 = findViewById(R.id.spinnerTo);
        binaryTextView = findViewById(R.id.binaryText);
        octalTextView = findViewById(R.id.octalText);

        //3. on click listener
        bt0.setOnClickListener(v -> ed.append("0"));
        bt1.setOnClickListener(v -> ed.append("1"));
        bt2.setOnClickListener(v -> ed.append("2"));
        bt3.setOnClickListener(v -> ed.append("3"));
        bt4.setOnClickListener(v -> ed.append("4"));
        bt5.setOnClickListener(v -> ed.append("5"));
        bt6.setOnClickListener(v -> ed.append("6"));
        bt7.setOnClickListener(v -> ed.append("7"));
        bt8.setOnClickListener(v -> ed.append("8"));
        bt9.setOnClickListener(v -> ed.append("9"));
        btA.setOnClickListener(v -> ed.append("A"));
        btB.setOnClickListener(v -> ed.append("B"));
        btC.setOnClickListener(v -> ed.append("C"));
        btD.setOnClickListener(v -> ed.append("D"));
        btE.setOnClickListener(v -> ed.append("E"));
        btF.setOnClickListener(v -> ed.append("F"));
        btDot.setOnClickListener(v -> ed.append("."));
        btAC.setOnClickListener(v -> {
            ed.setText("");
            tv.setText("");
            setResult("", "");
        });

        btEqual.setOnClickListener(v -> convert());

        //delect one element from the end
        btCl.setOnClickListener(v -> {
            String input = ed.getText().toString();
            if (!input.isEmpty()) {
                input = input.substring(0, input.length() - 1);
                ed.setText(input);
            }
            if (input.isEmpty()) {
                tv.setText("");
                setResult("", "");
            } else {
                convert();
            }
        });
    }

    private void setResult(String binary, String octal) {
        binaryTextView.setText(getString(R.string.binary_prefix, binary));
        octalTextView.setText(getString(R.string.octal_prefix, octal));
    }

    private void convert() {
        String input = ed.getText().toString().trim();
        if (input.isEmpty()) {
            tv.setText(R.string.enter_value);
            return;
        }
        if (input.equals(".")) {
            tv.setText("0");
            return;
        }
        String from = sp1.getSelectedItem().toString();
        String to = sp2.getSelectedItem().toString();
        try {
            double decimalValue = convertToDecimal(input, getRadix(from));
            String result = convertFromDecimal(decimalValue, getRadix(to));
            tv.setText(result);
            setResult(convertFromDecimal(decimalValue, 2), convertFromDecimal(decimalValue, 8));
        } catch (NumberFormatException e) {
            tv.setText(getString(R.string.invalid_input, from));
        }
    }

    private int getRadix(String type) {
        switch (type) {
            case "Binary": return 2;
            case "Octal": return 8;
            case "Decimal": return 10;
            case "Hexadecimal": return 16;
            default: return 10;
        }
    }

    private double convertToDecimal(String input, int radix) {
        if (!input.contains(".")) {
            return Long.parseLong(input, radix);
        }
        String[] parts = input.split("\\.");
        long intPart = parts[0].isEmpty() ? 0 : Long.parseLong(parts[0], radix);
        double fracPart = 0;
        if (parts.length > 1) {
            String fraction = parts[1];
            for (int i = 0; i < fraction.length(); i++) {
                int digit = Character.digit(fraction.charAt(i), radix);
                if (digit == -1) throw new NumberFormatException();
                fracPart += digit / Math.pow(radix, i + 1);
            }
        }
        return intPart + fracPart;
    }

    private String convertFromDecimal(double value, int radix) {
        long intPart = (long) value;
        double fracPart = value - intPart;

        String intStr;
        switch (radix) {
            case 2: intStr = Long.toBinaryString(intPart); break;
            case 8: intStr = Long.toOctalString(intPart); break;
            case 16: intStr = Long.toHexString(intPart).toUpperCase(); break;
            default: intStr = String.valueOf(intPart); break;
        }

        if (fracPart < 0.000000001) return intStr;

        StringBuilder sb = new StringBuilder(intStr);
        sb.append(".");
        for (int i = 0; i < 8; i++) {
            fracPart *= radix;
            int digit = (int) fracPart;
            sb.append(Integer.toString(digit, radix).toUpperCase());
            fracPart -= digit;
            if (fracPart < 0.000000001) break;
        }
        return sb.toString();
    }
}