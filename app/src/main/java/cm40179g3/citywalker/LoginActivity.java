package cm40179g3.citywalker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cm40179g3.citywalker.util.InputValidationException;
import cm40179g3.citywalker.util.Validation;

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmailAddr;
    private EditText txtPassword;
    private Button btnLogin;
    private Button btnNewAccount;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();

        btnLogin = findViewById(R.id.btnLogin);
        btnNewAccount = findViewById(R.id.btnNewAccount);
        txtEmailAddr = findViewById(R.id.txtEmailAddr);
        txtPassword = findViewById(R.id.txtPassword);

        btnLogin.setOnClickListener(view -> {
            try {
                validateInput();
            } catch (InputValidationException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            String emailAddress = txtEmailAddr.getText().toString();
            String password     = txtPassword.getText().toString();

            auth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    assert user != null;

                    Toast.makeText(this, "Welcome back to City Walk, " + user.getDisplayName() + "!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    Exception e = task.getException();
                    assert e != null;
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnNewAccount.setOnClickListener(view -> {
            startActivity(new Intent(this, NewAccountActivity.class));
        });
    }

    private void validateInput() throws InputValidationException  {
        Validation.validateEmail(txtEmailAddr.getText().toString());
        Validation.validatePassword(txtPassword.getText().toString());
    }
}