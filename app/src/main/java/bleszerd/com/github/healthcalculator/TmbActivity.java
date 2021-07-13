package bleszerd.com.github.healthcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TmbActivity extends AppCompatActivity {

    private TextView editHeight;
    private TextView editWeight;
    private TextView editAge;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmb);

        editHeight = findViewById(R.id.edit_tmb_height);
        editWeight = findViewById(R.id.edit_tmb_weight);
        editAge = findViewById(R.id.edit_tmb_age);
        spinner = findViewById(R.id.spinner_tbm_lifestyle);

        Button btnSend = findViewById(R.id.tmb_btn_calc);
        btnSend.setOnClickListener(v -> {
            if (!validateForm()) {
                Toast.makeText(this, R.string.field_missing, Toast.LENGTH_SHORT).show();
                return;
            }

            String sHeight = editHeight.getText().toString();
            String sWeight = editWeight.getText().toString();
            String sAge = editAge.getText().toString();

            int height = Integer.parseInt(sHeight);
            int weight = Integer.parseInt(sWeight);
            int age = Integer.parseInt(sAge);

            double result = calculateTmb(height, weight, age);
            double tmb = tmbResponse(result);
            Log.d("Teste", "" + tmb);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.tmb_response, tmb))
                    .setPositiveButton(android.R.string.ok, (dDialog1, which) -> {
                    })
                    .setNegativeButton(R.string.save, (dDialog2, which) -> {
                        new Thread(() -> {
                            long calcId = SqlHelper.getInstance(this).addItem("tmb", tmb    );

                            runOnUiThread(() -> {
                                if (calcId > 0) {
                                    Toast.makeText(this, R.string.saved_registry, Toast.LENGTH_SHORT).show();
                                    openListCalcActivity();
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

    private double calculateTmb(int height, int weight, int age) {
        return 66 + (weight * 13.8) + (5 * height) - (6.8 * age);
    }

    private double tmbResponse(double tmb) {
        int index = spinner.getSelectedItemPosition();
        switch (index) {
            case 0:
                return tmb * 1.2;
            case 1:
                return tmb * 1.375;
            case 2:
                return tmb * 1.55;
            case 3:
                return tmb * 1.725;
            case 4:
                return tmb * 1.9;
            default:
                return 0;
        }
    }

    private boolean validateForm() {
        return (!editHeight.getText().toString().isEmpty()
                && !editWeight.getText().toString().isEmpty()
                && !editAge.getText().toString().isEmpty()
                && !editHeight.getText().toString().startsWith("0")
                && !editWeight.getText().toString().startsWith("0")
                && !editAge.getText().toString().startsWith("0"));
    }

    private void openListCalcActivity() {
        Intent intent = new Intent(TmbActivity.this, ListCalcActivity.class);
        intent.putExtra("type", "tmb");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_list:
                openListCalcActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}