package bleszerd.com.github.healthcalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ImcActivity extends AppCompatActivity {

    private TextView editHeight;
    private TextView editWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);

        editHeight = findViewById(R.id.edit_imc_height);
        editWeight = findViewById(R.id.edit_imc_weight);

        Button btnSend = findViewById(R.id.imc_btn_calc);

        btnSend.setOnClickListener(v -> {
            if (!validateForm()) {
                Toast.makeText(this, R.string.field_missing, Toast.LENGTH_SHORT).show();
                return;
            }

            String sHeight = editHeight.getText().toString();
            String sWeight = editWeight.getText().toString();

            int height = Integer.parseInt(sHeight);
            int weight = Integer.parseInt(sWeight);

            double result = calculateImc(height, weight);
            int imcResponseId = imcResponse(result);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.imc_response, result))
                    .setMessage(imcResponseId)
                    .setPositiveButton(android.R.string.ok, (dDialog1, which) -> {
                    })
                    .setNegativeButton(R.string.save, (dDialog2, which) -> {
                        new Thread(() -> {
                            long calcId = SqlHelper.getInstance(this).addItem("imc", result);

                            runOnUiThread(() -> {
                                if (calcId > 0) {
                                    Toast.makeText(this, R.string.saved_registry, Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(ImcActivity.this, ListCalcActivity.class);
                                    intent.putExtra("type", "imc");
                                    startActivity(intent);
                                }
                            });
                        }).start();
                    })
                    .create();

            dialog.show();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editHeight.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(editWeight.getWindowToken(), 0);
        });
    }

    private boolean validateForm() {
        return (!editHeight.getText().toString().isEmpty()
                && !editWeight.getText().toString().isEmpty()
                && !editHeight.getText().toString().startsWith("0")
                && !editWeight.getText().toString().startsWith("0"));
    }

    private int imcResponse(double imc) {
        if (imc < 15) {
            return R.string.imc_severely_low_weight;
        } else if (imc < 16) {
            return R.string.imc_very_low_weight;
        } else if (imc < 18.5) {
            return R.string.imc_low_weight;
        } else if (imc < 25) {
            return R.string.normal;
        } else if (imc < 30) {
            return R.string.imc_high_weight;
        } else if (imc < 35) {
            return R.string.imc_so_high_weight;
        } else if (imc < 40) {
            return R.string.imc_severely_high_weight;
        } else {
            return R.string.imc_extreme_weight;
        }
    }

    private double calculateImc(int height, int weight) {
        double rawHeight = (double) height / 100;
        rawHeight *= rawHeight;

        return weight / rawHeight;
    }
}