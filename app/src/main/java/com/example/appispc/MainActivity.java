package com.example.appispc;

import android.content.Intent;
import android.os.Bundle;
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
        registroButton.setOnClickListener(v -> registrarUsuario());

        Button loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);

            startActivity(intent);
        });
    }

    private void registrarUsuario() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        usuarioDAO.abrir();

        if (usuarioDAO.usuarioExistente(username)) {
            Toast.makeText(this, "El nombre de usuario ya está registrado. Por favor, elige otro.", Toast.LENGTH_SHORT).show();
        } else if (!validarPassword(password)) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres, una letra mayúscula, un número y un carácter especial", Toast.LENGTH_SHORT).show();
        } else {
            long id = usuarioDAO.agregarUsuario(username, password);

            if (id > 0) {
                Toast.makeText(this, "¡Registro exitoso! Bienvenido " + username, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, LoginActivity.class);

                startActivity(intent);

                finish();
            } else {
                Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show();
            }
        }

        usuarioDAO.cerrar();
    }

    private boolean validarPassword(String password) {
        if (password.length() < 6) {
            return false;
        }

        boolean contieneMayuscula = false;
        boolean contieneNumero = false;
        boolean contieneEspecial = false;
        String caracteresEspeciales = "!@#$%^&*()_-+=[]{}|:;,.<>?";

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                contieneMayuscula = true;
            } else if (Character.isDigit(c)) {
                contieneNumero = true;
            } else if (caracteresEspeciales.contains(String.valueOf(c))) {
                contieneEspecial = true;
            }
        }

        return contieneMayuscula && contieneNumero && contieneEspecial;
    }
}