package info.pauek.shoppinglist;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShoppingListActivity extends AppCompatActivity {

    List<ShoppingItem> items;

    // Referències a elements de la pantalla
    private RecyclerView items_view;
    private ImageButton btn_add;
    private EditText edit_box;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        readItemList();

        items_view = findViewById(R.id.items_view);
        btn_add = findViewById(R.id.btn_add);
        edit_box = findViewById(R.id.edit_box);

        adapter = new Adapter();

        items_view.setLayoutManager(new LinearLayoutManager(this));
        items_view.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
        items_view.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveItemList();
    }

    private void saveItemList() {
        try {
            FileOutputStream outputStream = openFileOutput("items.txt", MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);

            for (int i = 0; i < items.size(); i++) {
                ShoppingItem item = items.get(i);
                writer.write(String.format("%s;%b\n", item.getName(), item.isChecked()));
            }
            writer.close();
        }
        catch (FileNotFoundException e) {
            Log.e("ShoppingList", "saveItemList: FileNotFoundException");
        }
        catch (IOException e) {
            Log.e("ShoppingList", "saveItemList: IOException");
        }
    }

    private void readItemList() {
        try {
            FileInputStream inputStream = openFileInput("items.txt");
            InputStreamReader reader = new InputStreamReader(inputStream);
            Scanner scanner = new Scanner(reader);
            items = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                items.add(new ShoppingItem(parts[0], parts[1].equals("true")));
            }
        }
        catch (FileNotFoundException e) {
            Log.e("ShoppingList", "readItemList: FileNotFoundException");
        }
    }

    public void onAddItem(View view) {
        String name = edit_box.getText().toString();
        items.add(new ShoppingItem(name));
        adapter.notifyItemInserted(items.size()-1);
        edit_box.getText().clear();
    }

    private void onClickItem(int pos) {
        items.get(pos).toggleChecked();
        adapter.notifyItemChanged(pos);
    }

    private void onItemLongClick(int pos) {
        items.remove(pos);
        adapter.notifyItemRemoved(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    onClickItem(pos);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    onItemLongClick(pos);
                    return true;
                }
            });
        }
    }

    public class Adapter extends RecyclerView.Adapter<ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
            ShoppingItem item = items.get(pos);
            holder.checkbox.setText(item.getName());
            holder.checkbox.setChecked(item.isChecked());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
