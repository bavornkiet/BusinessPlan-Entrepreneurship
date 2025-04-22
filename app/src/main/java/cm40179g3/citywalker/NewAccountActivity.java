package cm40179g3.citywalker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import cm40179g3.citywalker.util.InputValidationException;
import cm40179g3.citywalker.util.Validation;

public class NewAccountActivity extends AppCompatActivity {

    public static class CancelDialogFragment extends DialogFragment {

        private final NewAccountActivity activity;

        public CancelDialogFragment(NewAccountActivity activity) {
            this.activity = activity;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("Cancel account setup?")
                    .setMessage("Any information you entered here will be discarded.")
                    .setPositiveButton("Yes", (dialog, id) -> {
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                    })
                    .setNegativeButton("No", (dialog, id) -> {})
                    .create();
        }
    }

    private EditText txtEmail;
    private EditText txtDisplayName;
    private EditText txtPassword;
    private EditText txtConfirmPassword;
    private Button btnCreate;
    private Button btnBack;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        txtEmail = findViewById(R.id.new_account_txtEmail);
        txtDisplayName = findViewById(R.id.new_account_txtDisplayName);
        txtPassword = findViewById(R.id.new_account_txtPassword);
        txtConfirmPassword = findViewById(R.id.new_account_txtConfirmPassword);
        btnCreate = findViewById(R.id.new_account_btnCreate);
        btnBack = findViewById(R.id.new_account_btnBack);

        btnCreate.setOnClickListener(view -> {
            try {
                validateInput();
            } catch (InputValidationException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            String email    = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            onAccountCreated();
                        } else {
                            Exception e = task.getException();
                            assert e != null;
                            Log.e("NewAccountActivity", "Authentication failed", e);
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnBack.setOnClickListener(view -> {
            new CancelDialogFragment(this)
                    .show(getSupportFragmentManager(), "cancel");
        });
    }

    private void onAccountCreated() {
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;

        // Set display name
        String name = txtDisplayName.getText().toString();
        user.updateProfile(new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build())
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Exception e = task.getException();
                        assert e != null;
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.w("NewAccountActivity", "Failed to set display name", e);
                    }
                    createUserDocument(user);
                });
    }

    private void createUserDocument(FirebaseUser user) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("steps", 0);
        userData.put("displayName", user.getDisplayName());

        firestore.collection("Users")
                .document(user.getUid())
                .set(userData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Welcome to City Walk, " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Exception e = task.getException();
                        assert e != null;
                        Log.w("NewAccountActivity", "Failed to set display name", e);
                    }
                    startActivity(new Intent(this, MainActivity.class));
                });
    }

    private void validateInput() throws InputValidationException {
        validateEmailAddress();
        validateDisplayName();
        validatePassword();
    }

    private void validatePassword() throws InputValidationException {
        final String password = txtPassword.getText().toString();

        Validation.validatePassword(password);

        // Double-entry verification
        if (!password.equals(txtConfirmPassword.getText().toString())) {
            throw new InputValidationException("Passwords don't match");
        }
    }

    private void validateDisplayName() throws InputValidationException {
        final String name = txtDisplayName.getText().toString();
        final int maxLength = 256;
        if (name.isBlank()) {
            throw new InputValidationException("Display name is blank");
        }
        if (!name.strip().equals(name)) {
            throw new InputValidationException("Display name begins/ends in white space");
        }
        if (name.length() > maxLength) {
            throw new InputValidationException("Display name exceeds " + maxLength + " character limit");
        }
    }

    private void validateEmailAddress() throws InputValidationException {
        Validation.validateEmail(txtEmail.getText().toString());
    }

}