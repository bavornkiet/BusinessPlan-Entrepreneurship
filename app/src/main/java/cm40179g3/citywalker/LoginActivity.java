package cm40179g3.citywalker;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

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
            String emailAddress = txtEmailAddr.getText().toString();
            String password     = txtPassword.getText().toString();

            auth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Log.i("Sign in", "Sign in successful!");
                    FirebaseUser user = auth.getCurrentUser();
                } else {
                    Log.w("Sign in", "Sign in failed!");
                }
            });
        });
    }
}