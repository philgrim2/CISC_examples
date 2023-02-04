package edu.harrisburgu.cisc349.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity
{
    protected static final String TAG = "CalculatorActivity";
    protected double incomingValue = 0.0;
    protected double storedValue = 0.0;
    protected String currentOp = null;
    protected StringBuilder buffer = new StringBuilder();
    protected TextView output;
    protected Button redButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        output = findViewById(R.id.calculator_text_view);
        output.setText(Double.toString(incomingValue));

        Button b;
        NumberButtonClickListener listener = new NumberButtonClickListener();
        b = (Button) findViewById(R.id.zero_button);
        b.setOnClickListener(listener);
        b = (Button) findViewById(R.id.one_button);
        b.setOnClickListener(listener);
        b = (Button) findViewById(R.id.two_button);
        b.setOnClickListener(listener);
        b = (Button) findViewById(R.id.three_button);
        b.setOnClickListener(listener);
        b = (Button) findViewById(R.id.four_button);
        b.setOnClickListener(listener);
        b = (Button) findViewById(R.id.five_button);
        b.setOnClickListener(listener);
        b = (Button) findViewById(R.id.six_button);
        b.setOnClickListener(listener);
        b = (Button) findViewById(R.id.seven_button);
        b.setOnClickListener(listener);
        b = (Button) findViewById(R.id.eight_button);
        b.setOnClickListener(listener);
        b = (Button) findViewById(R.id.nine_button);
        b.setOnClickListener(listener);

        OperatorButtonClickListener opListener = new OperatorButtonClickListener();
        b = (Button) findViewById(R.id.add_button);
        b.setOnClickListener(opListener);
        b = (Button) findViewById(R.id.subtract_button);
        b.setOnClickListener(opListener);
        b = (Button) findViewById(R.id.multiply_button);
        b.setOnClickListener(opListener);
        b = (Button) findViewById(R.id.divide_button);
        b.setOnClickListener(opListener);

        b = (Button) findViewById(R.id.sign_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomingValue = -incomingValue;
                buffer.setLength(0);
                buffer.append(incomingValue == 0 ? "0" : Double.toString(incomingValue));
                output.setText(Double.toString(incomingValue));
            }
        });

        b = (Button) findViewById(R.id.point_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!buffer.toString().contains(".")) {
                    buffer.append(".");
                }
            }
        });

        b = (Button) findViewById(R.id.equals_button);
        View.OnClickListener eqListener = new EqualsListener();
        b.setOnClickListener(eqListener);
        b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                resetButtonBackground();
                currentOp = null;
                buffer.setLength(0);
                incomingValue = 0.0;
                storedValue = 0.0;
                output.setText(Double.toString(incomingValue));
                return true;
            }
        });
    }

    protected void resetButtonBackground()
    {
        if (null != redButton)
        {
            redButton.setBackgroundColor(getResources().getColor(R.color.purple_500, null));
        }
    }

    class NumberButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Button b = (Button) view;
            String val = b.getText().toString();
            buffer.append(val);
            incomingValue = Double.parseDouble(buffer.toString());
            output.setText(Double.toString(incomingValue));
        }
    }

    class OperatorButtonClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            resetButtonBackground();
            redButton = (Button)view;
            redButton.setBackgroundColor(getResources().getColor(R.color.red, null));
            String op = redButton.getText().toString();
            Log.d(TAG, op + " pressed.");
            resolveCurrentOp();
            currentOp = op;
            buffer.setLength(0);
            incomingValue = 0.0;
            buffer.append(0);
            output.setText(Double.toString(storedValue));
        }
    }

    class EqualsListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            resetButtonBackground();
            resolveCurrentOp();
            buffer.setLength(0);
            incomingValue = 0.0;
            buffer.append(0);
            output.setText(Double.toString(storedValue));
        }
    }

    private void resolveCurrentOp()
    {
        if (null != currentOp)
        {
            // First resolve the pending operation.
            switch(currentOp)
            {
                case "+":
                    storedValue = storedValue + incomingValue;
                    break;
                case "-":
                    storedValue = storedValue - incomingValue;
                    break;
                case "*":
                    storedValue = storedValue * incomingValue;
                    break;
                case "/":
                    storedValue = storedValue / incomingValue;
                    break;
            }
        }
        else
        {
            storedValue = incomingValue;
        }
    }
}