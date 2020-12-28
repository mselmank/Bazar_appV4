package cl.matiasselman_android.bazar_appv4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import cl.matiasselman_android.bazar_appv4.models.User;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etConfPassword, etPhone;

    private Button btnRegister;
    private ImageView imgLogo;
    private ProgressBar progressBar;

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    String name, email, pass, confpass, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Crear una cuenta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initVariables();

        // Login Button action
        findViewById(R.id.signInCreateAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                onBackPressed();
            }

        });

    }

    private void initVariables() {
        etName = findViewById(R.id.inputName);
        etEmail = findViewById(R.id.inputEmail);
        etPassword = findViewById(R.id.inputPass);
        etConfPassword = findViewById(R.id.inputConfPass);
        etPhone = findViewById(R.id.editTextPhone);
        btnRegister = findViewById(R.id.registerButton);
        imgLogo = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
    }

    public boolean validationFields() {
        boolean validation = true;

        if (!email.contains("@") || !email.contains(".")) {
            validation = false;
            etEmail.setError(getString(R.string.login_error_invalid_correo));
        }
        if (email.isEmpty()) {
            validation = false;
            etEmail.setError(getString(R.string.login_error_no_correo));
        }

        if (pass.length() < 8) {
            validation = false;
            etPassword.setError(getString(R.string.login_error_password_length));
        }
        if (pass.isEmpty()) {
            validation = false;
            etPassword.setError(getString(R.string.login_error_no_password));
        }

        if (confpass.length() < 8) {
            validation = false;
            etConfPassword.setError(getString(R.string.login_error_password_length));
        }
        if (confpass.isEmpty()) {
            validation = false;
            etConfPassword.setError(getString(R.string.login_error_no_password));
        }

        if (!confpass.equals(pass)) {
            validation = false;
            etPassword.setError(getString(R.string.login_error_no_password_equals));
            etConfPassword.setError(getString(R.string.login_error_no_password_equals));
        }

        if (phone.length() < 8) {
            validation = false;
            etPhone.setError(getString(R.string.login_error_invalid_telefono));
        }
        if (phone.isEmpty()) {
            validation = false;
            etPhone.setError(getString(R.string.login_error_no_telefono));
        }

        if (name.isEmpty()) {
            validation = false;
            etName.setError(getString(R.string.login_error_no_nombre));
        }

        if (!validation) setVisibilityButon(View.GONE, View.VISIBLE, true);
        return validation;
    }

    public void createUser(View view) {

        setVisibilityButon(View.VISIBLE, View.GONE, false);

        name = etName.getText().toString();
        email = etEmail.getText().toString();
        pass = etPassword.getText().toString();
        confpass = etConfPassword.getText().toString();
        phone = etPhone.getText().toString();

        if (!validationFields()) return;

        firebaseAuth = FirebaseAuth.getInstance();

        createAccount(name, email, pass, phone);
    }

    private void createAccount(String name, String email, String pass, String phone) {
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                createInfoUser(name, phone, email);
            } else {
                Toast.makeText(this, "Cuenta no pudo ser creada", Toast.LENGTH_SHORT).show();
                setVisibilityButon(View.GONE, View.VISIBLE, true);
            }
        });
    }

    public void createInfoUser(String name, String phone, String email) {
        firestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firestore.collection("Users");
        User usuario = new User(email, name, phone);
        collectionReference.document(email).set(usuario);
        Toast.makeText(this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(this::IrAlMenuPrincipal, 200);
    }

    public void setVisibilityButon(int pBar, int image, boolean button) {
        progressBar.setVisibility(pBar);
        imgLogo.setVisibility(image);
        btnRegister.setEnabled(button);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    void IrAlMenuPrincipal() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}