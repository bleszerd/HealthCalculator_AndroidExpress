package bleszerd.com.github.healthcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListCalcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calc);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String type = extras.getString("type");

            new Thread(() -> {
                List<Register> registers = SqlHelper.getInstance(this).getRegisterBy(type);
                runOnUiThread(() -> {
                    RecyclerView recyclerList = findViewById(R.id.recycler_view_list);
                    recyclerList.setLayoutManager(new LinearLayoutManager(this));

                    ListCalcAdapter adapter = new ListCalcAdapter(registers);
                    adapter.setListener(register -> {
                        new AlertDialog.Builder(this)
                                .setTitle(R.string.delete_register)
                                .setPositiveButton(R.string.yes, (dialog, which) -> {
                                    boolean result = SqlHelper.getInstance(this).removeItem(register);
                                    Log.d("Test", "Deleting " + result);
                                    if (result) {
                                        adapter.registers.remove(register);
                                        adapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton(R.string.no, (dialog, which) -> {
                                })
                                .create()
                                .show();
                    });
                    recyclerList.setAdapter(adapter);
                    Log.d("Test", registers.toString());
                });
            }).start();

        }
    }

    class ListCalcAdapter extends RecyclerView.Adapter<ListCalcAdapter.ListCalcViewHolder> {
        List<Register> registers;
        OnDetailItemClickListener listener;

        private void updateRawRegisters(List<Register> registers){
            this.registers = registers;
        }

        public void setListener(OnDetailItemClickListener listener) {
            this.listener = listener;
        }

        public ListCalcAdapter(List<Register> registers) {
            this.registers = registers;
        }

        @NonNull
        @NotNull
        @Override
        public ListCalcActivity.ListCalcAdapter.ListCalcViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new ListCalcViewHolder(getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ListCalcActivity.ListCalcAdapter.ListCalcViewHolder holder, int position) {
            holder.bind(registers.get(position));
        }

        @Override
        public int getItemCount() {
            return registers.size();
        }

        class ListCalcViewHolder extends RecyclerView.ViewHolder {

            public ListCalcViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
            }

            public void bind(Register register) {
                itemView.setOnLongClickListener(v -> {
                    listener.onLongClick(register);
                    return true;
                });

                String formatted = "";
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pt", "BR"));
                    Date dateSaved = sdf.parse(register.createdDate);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss", new Locale("pt", "BR"));
                    formatted = dateFormat.format(dateSaved);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ((TextView) itemView).setText(getString(R.string.list_response, register.response, formatted));
            }
        }
    }
}