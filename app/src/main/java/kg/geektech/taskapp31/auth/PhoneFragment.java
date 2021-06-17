package kg.geektech.taskapp31.auth;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import kg.geektech.taskapp31.R;
import kg.geektech.taskapp31.VerifyPhoneActivity;

public class PhoneFragment extends Fragment {

    private EditText editPhone;
    private Button btnContinue;
    private ProgressBar progressBar;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String code;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editPhone = view.findViewById(R.id.editPhone);
        progressBar = view.findViewById(R.id.progressBar);
        btnContinue = view.findViewById(R.id.btnContinue);
        view.findViewById(R.id.btnContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPhone.getText().toString().trim().isEmpty()) {
                    Toast.makeText(requireContext(), "Введите номер телефона", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                btnContinue.setVisibility(View.INVISIBLE);

                requestSMS();

            }
        });

        initCallbacks();
    }

    private void initCallbacks() {

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                code = phoneAuthCredential.getSmsCode();

                Log.e("Phone", "onVerificationCompleted");
                btnContinue.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressBar.setVisibility(View.GONE);
                btnContinue.setVisibility(View.INVISIBLE);
                Log.e("Phone", "onVerificationFailed: " + e.getMessage());

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(), VerifyPhoneActivity.class);
                intent.putExtra("mobile", editPhone.getText().toString());
                    intent.putExtra("verificationId",verificationId);
                startActivity(intent);
            }
        };
    }

    private void requestSMS() {
        String phone = editPhone.getText().toString();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

}