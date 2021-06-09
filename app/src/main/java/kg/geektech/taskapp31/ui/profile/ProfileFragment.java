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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import kg.geektech.taskapp31.App;
import kg.geektech.taskapp31.Image;
import kg.geektech.taskapp31.Preference.Prefs;
import kg.geektech.taskapp31.R;
import kg.geektech.taskapp31.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    //Create Binding
    FragmentProfileBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        // init Binding
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;


    }


    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();

                    StorageReference riversRef = storageRef.child("images/" + uri.getLastPathSegment());
                    UploadTask uploadTask;
                    uploadTask = riversRef.putFile(uri);

                    Glide.with(requireActivity())
                            .load(uri)
                            .circleCrop()
                            .into(binding.profilePhoto);


                    String strUri = uri.toString();
                    Bundle bundle = new Bundle();
                    binding.profilePhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bundle.putString("action",strUri);

                            App.getAppDatabase().taskDao().insert(new Image(strUri));

                            Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_avatarFragment,bundle);
                        }
                    });


                }
            });

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.profileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");

            }
        });

        //Method for save editName with SharedPreference
        saveName();


    }

    private void saveName() {
        Prefs prefs = new Prefs(getContext());

        // calling method prefs.getString and get key word
        binding.editName.setText(prefs.getString("saveName"));

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