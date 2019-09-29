package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;

public class MainActivity extends AppCompatActivity {


    List<String> items;
    Button btnadd;
    EditText etItem;
    RecyclerView rvItem;
    ItemsAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnadd = findViewById(R.id.btnadd);
        etItem = findViewById(R.id.etItem);
        rvItem = findViewById(R.id.rvItem);

        items = new ArrayList<>();
        LoadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "item was removed", Toast.LENGTH_SHORT).show();
                saveItems();

            }
        };
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        rvItem.setAdapter(itemsAdapter);
        rvItem.setLayoutManager(new LinearLayoutManager(this));

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }
    private File getDatafile() {
            return new File(getFilesDir(),"data.txt");
        }

        private void LoadItems() {
            try {
                items = new ArrayList<>(FileUtils.readLines(getDatafile(), Charset.defaultCharset()));
            } catch (IOException e) {
                Log.e("MainActivity", "error reading items", e);
                items = new ArrayList<>();
            }
        }
        private void saveItems() {
            try {
                FileUtils.writeLines(getDatafile(), items);
            } catch (IOException e) {
                Log.e("MainActivity", "error writing items", e);
            }
        }

    }
