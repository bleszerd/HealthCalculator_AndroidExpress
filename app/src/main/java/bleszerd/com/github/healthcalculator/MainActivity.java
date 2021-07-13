package bleszerd.com.github.healthcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<MainItem> mainItems = new ArrayList<>();
        mainItems.add(new MainItem(1, R.drawable.ic_imc_btn, R.string.imc, R.color.white));
        mainItems.add(new MainItem(2, R.drawable.ic_tmb_btn, R.string.tmb,R.color.white));

        recyclerMain = findViewById(R.id.main_recycler);
        recyclerMain.setLayoutManager(new GridLayoutManager(this, 2));
        MainAdapter adapter = new MainAdapter(mainItems);
        adapter.setListener(id -> {
            switch (id) {
                case 1:
                    startActivity(new Intent(MainActivity.this, ImcActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(MainActivity.this, TmbActivity.class));
                    break;
            }
        });
        recyclerMain.setAdapter(adapter);
    }

    private class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
        private List<MainItem> mainItems;
        private OnItemClickListener listener;

        public MainAdapter(List<MainItem> mainItems) {
            this.mainItems = mainItems;
        }

        public void setListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @org.jetbrains.annotations.NotNull
        @Override
        public MainActivity.MainAdapter.MainViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
            return new MainViewHolder(getLayoutInflater().inflate(R.layout.main_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull MainActivity.MainAdapter.MainViewHolder holder, int position) {
            MainItem currentMainItem = mainItems.get(position);
            holder.bind(currentMainItem);
        }

        @Override
        public int getItemCount() {
            return mainItems.size();
        }

        private class MainViewHolder extends RecyclerView.ViewHolder {

            public MainViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
                super(itemView);
            }

            public void bind(MainItem mainItem) {
                TextView textView = itemView.findViewById(R.id.item_txt_name);
                ImageView iconView = itemView.findViewById(R.id.item_img_icon);
                LinearLayout buttonRootView = itemView.findViewById(R.id.root_button);

                buttonRootView.setOnClickListener(v -> {
                    listener.onClick(mainItem.getId());
                });

                textView.setText(mainItem.getTitle());
                iconView.setImageResource(mainItem.getDrawableId());
                buttonRootView.setBackgroundColor(mainItem.getColor());
            }
        }
    }
}