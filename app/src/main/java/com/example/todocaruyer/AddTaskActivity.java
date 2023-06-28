package com.example.todocaruyer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTaskActivity extends AppCompatActivity {

    private EditText labelNewTask;
    private Button btnValid;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        labelNewTask = findViewById(R.id.newTask);
        btnValid = findViewById(R.id.btnValid);
        btnCancel = findViewById(R.id.btnCancel);

        btnValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTask = labelNewTask.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("newTask", newTask);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }
}