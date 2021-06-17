package kg.geektech.taskapp31;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerifyPhoneActivity extends AppCompatActivity {

    private EditText editCode;
    private ProgressBar progressBar;
    private Button btnVerify;
    private String verificationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        editCode = findViewById(R.id.editCode);
        progressBar = findViewById(R.id.progressBar2);
        btnVerify = findViewById(R.id.btnVerify);

        verificationId = getIntent().getStringExtra("verificationId");

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editCode.getText().toString().trim().isEmpty()) {
                    Toast.makeText(VerifyPhoneActivity.this, "Пустое значение!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = editCode.getText().toString();
                if (verificationId != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    btnVerify.setVisibility(View.VISIBLE);


                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            btnVerify.setVisibility(View.VISIBLE);
                            if (task.isSuccessful()) {
                                NavController navController = Navigation.findNavController(VerifyPhoneActivity.this, R.id.nav_host_fragment);
                                navController.navigate(R.id.navigation_home);
                            } else {
                                Toast.makeText(VerifyPhoneActivity.this, "Не правильно веден код!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}