package com.example.appispc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private EditText editTextTask;
    private TaskAdapter adapter;

    private TareaDAO tareaDAO;
    private long usuarioId;
    private ArrayList<Tarea> taskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tareaDAO = new TareaDAO(this);
        tareaDAO.abrir();

        usuarioId = getIntent().getLongExtra("usuarioId", -1);

        editTextTask = findViewById(R.id.editTextTask);
        Button buttonAddTask = findViewById(R.id.buttonAddTask);
        ListView listViewTasks = findViewById(R.id.listViewTasks);

        taskList = cargarTareas();
        adapter = new TaskAdapter(this, taskList);
        listViewTasks.setAdapter(adapter);

        buttonAddTask.setOnClickListener(v -> {
            String tituloTarea = editTextTask.getText().toString();
            if (!tituloTarea.isEmpty()) {
                long taskId = tareaDAO.agregarTarea(tituloTarea, usuarioId);
                if (taskId > 0) {
                    Tarea nuevaTarea = new Tarea(taskId, tituloTarea);
                    taskList.add(nuevaTarea);
                    adapter.notifyDataSetChanged();
                    editTextTask.setText("");
                } else {
                    Toast.makeText(HomeActivity.this, "Error al agregar tarea", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private ArrayList<Tarea> cargarTareas() {
        ArrayList<Tarea> tareas = new ArrayList<>();
        Cursor cursor = tareaDAO.obtenerTodasLasTareasPorUsuario(usuarioId);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_ID));
                @SuppressLint("Range") String titulo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK_TITLE));
                tareas.add(new Tarea(id, titulo));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tareas;
    }



    private class TaskAdapter extends ArrayAdapter<Tarea> {

        private final Context mContext;
        private final ArrayList<Tarea> mTaskList;

        public TaskAdapter(Context context, ArrayList<Tarea> taskList) {
            super(context, 0, taskList);
            mContext = context;
            mTaskList = taskList;
        }


        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if (listItem == null) {
                listItem = LayoutInflater.from(mContext).inflate(R.layout.item_task, parent, false);
            }

            final Tarea currentTask = mTaskList.get(position);

            TextView taskText = listItem.findViewById(R.id.task_text);
            taskText.setText(currentTask.getTitulo());

            Button editButton = listItem.findViewById(R.id.edit_button);
            editButton.setOnClickListener(v -> {
                Tarea tarea = mTaskList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Editar tarea");
                final EditText input = new EditText(mContext);
                input.setText(tarea.getTitulo());
                builder.setView(input);

                builder.setPositiveButton("Guardar", (dialog, which) -> {
                    String newTaskTitle = input.getText().toString();
                    if (tareaDAO.actualizarTarea(tarea.getId(), newTaskTitle)) {
                        tarea.setTitulo(newTaskTitle);
                        notifyDataSetChanged();
                        Toast.makeText(mContext, "Tarea actualizada", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

                builder.show();
            });


            Button deleteButton = listItem.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(v -> {
                Tarea tarea = mTaskList.get(position);
                if (tareaDAO.eliminarTarea(tarea.getId())) {
                    mTaskList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                }
            });

            return listItem;
        }
    }
}