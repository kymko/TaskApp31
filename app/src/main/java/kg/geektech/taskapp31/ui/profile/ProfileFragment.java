package kg.geektech.taskapp31.ui.profile;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import kg.geektech.taskapp31.Preference.Prefs;
import kg.geektech.taskapp31.R;
import kg.geektech.taskapp31.databinding.FragmentProfileBinding;

import static android.content.ContentValues.TAG;


public class ProfileFragment extends Fragment {

    //Create Binding
    FragmentProfileBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        // init Binding
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.profileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
                //binding.progressBar.setVisibility(View.VISIBLE);
            }
        });

        //Method for save editName with SharedPreference
        saveName();
//        Prefs prefs = new Prefs(getContext());
//        binding.profilePhoto.setImageURI(prefs.getAvatar());



    }

    public ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {

                    saveToFirebaseStorage(uri);

                    Prefs prefs = new Prefs(getContext());
                    prefs.saveImage(uri);

//                    Glide.with(requireActivity())
//                            .load(uri)
//                            .circleCrop()
//                            .into(binding.profilePhoto);
                    binding.profilePhoto.setImageURI(prefs.getAvatar());

                    showSelectedImage(uri);

                }

                private void showSelectedImage(Uri uri) {
                    String strUri = uri.toString();
                    Bundle bundle = new Bundle();
                    binding.profilePhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bundle.putString("action", strUri);


                            Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_avatarFragment, bundle);
                        }
                    });
                }

                private void saveToFirebaseStorage(Uri uri) {
                    binding.progressBar.setVisibility(View.VISIBLE);

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference riversRef = storageRef.child("images/" + uri);
                    UploadTask uploadTask = riversRef.putFile(uri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(requireContext(), "upload success", Toast.LENGTH_SHORT).show();
                            binding.progressBar.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

    private void saveName() {
        Prefs prefs = new Prefs(getContext());

        // calling method prefs.getString and get key word
        binding.editName.setText(prefs.getString("saveName"));
        binding.profilePhoto.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });

        binding.editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                //put key word
                prefs.putString("saveName", s.toString());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.sort_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            // calling method for clear preference
            case R.id.clear:
                Prefs prefs = new Prefs(getContext());
                prefs.clearPref();
        }
        return true;
    }

}