package com.example.appispc;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);

        usuarioDAO = new UsuarioDAO(this);

        Button registroButton = findViewById(R.id.buttonRegister);
        registroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        usuarioDAO.abrir();

        if (usuarioDAO.usuarioExistente(username)) {
            Toast.makeText(this, "El nombre de usuario ya está registrado. Por favor, elige otro.", Toast.LENGTH_SHORT).show();
        } else {
            long id = usuarioDAO.agregarUsuario(username, password);

            if (id > 0) {
                Toast.makeText(this, "¡Registro exitoso! Bienvenido " + username, Toast.LENGTH_SHORT).show();

                // Crear un Intent para ir a la actividad LoginActivity
                Intent intent = new Intent(this, LoginActivity.class);

                // Iniciar la nueva actividad
                startActivity(intent);

                // Cerrar la actividad actual (RegistroActivity)
                finish();
            } else {
                Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show();
            }
        }

        usuarioDAO.cerrar();
    }
}

