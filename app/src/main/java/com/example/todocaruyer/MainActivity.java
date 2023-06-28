package com.example.todocaruyer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    //private String[] tasks = {"Tâche 1", "Tâche 2", "Tâche 3", "Tâche 4", "Tâche 5"};
    private ArrayList<String> tasks = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private static final int REQUEST_CODE_ADD_TASK = 1;

    private ActivityResultLauncher<Intent> addTaskLaucher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String newTask = data.getStringExtra("newTask");
                    if (newTask != null && !newTask.isEmpty()) {
                        tasks.add(newTask);
                        adapter.notifyDataSetChanged();
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "Ajout de tâche annulé", Toast.LENGTH_SHORT).show();
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //permet de récupérer et d'afficher la liste des tâches
        listView = findViewById(R.id.listView);
        tasks = new ArrayList<>(); // Initialisez la liste des tâches si nécessaire
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, tasks);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //permet de cliquer sur le btn flottant
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                addTaskLaucher.launch(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String task = tasks.get(position);
                Toast.makeText(MainActivity.this, "Task: " + task, Toast.LENGTH_SHORT).show();
            }
        });
    }

}