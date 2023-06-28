package com.example.todocaruyer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    //private String[] tasks = {"Tâche 1", "Tâche 2", "Tâche 3", "Tâche 4", "Tâche 5"};
    private ArrayList<String> tasks = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    public static final String WRITE_EXTERNAL_STORAGE = null;

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

        //permet de recharger la liste des task si elle a été save
        loadListFromFile();

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete_selected) {
            SparseBooleanArray checkedPositions = listView.getCheckedItemPositions();
            int itemCount = adapter.getCount();

            for (int i = itemCount - 1; i >= 0; i--) {
                if (checkedPositions.get(i)) {
                    tasks.remove(i);
                }
            }
            listView.clearChoices();
            adapter.notifyDataSetChanged();
            return true;
        }else if (item.getItemId() == R.id.action_save_list) {
            saveListToFile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveListToFile() {
        try {
            // Ouvrir le fichier en écriture
            File file = new File(Environment.getExternalStorageDirectory(), "saveTaskList.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            // Écrire les éléments de la liste dans le fichier
            for (String task : tasks) {
                writer.write(task);
                writer.newLine();
            }
            // Fermer le fichier
            writer.close();
            Toast.makeText(this, "Liste sauvegardée", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de la sauvegarde de la liste", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadListFromFile() {
        try {
            // Ouverture du fichier texte dans le répertoire de stockage externe
            File file = new File(Environment.getExternalStorageDirectory(), "saveTaskList.txt");
            // Création du reader pour lire le contenu du fichier
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            // Lecture de chaque ligne du fichier et ajout de la tâche à la liste
            while ((line = reader.readLine()) != null) {
                tasks.add(line);
            }
            // Fermeture du reader
            reader.close();
            // Notification de l'adaptateur que les données ont changé
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Liste chargée", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors du chargement de la liste", Toast.LENGTH_SHORT).show();
        }
    }

}