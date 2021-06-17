package kg.geektech.taskapp31.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import kg.geektech.taskapp31.R;
import kg.geektech.taskapp31.interfaces.OnItemClickListener;
import kg.geektech.taskapp31.models.Task;
import kg.geektech.taskapp31.ui.home.TaskAdapter;

public class DashboardFragment extends Fragment {

    private RecyclerView rvDashboard;
    private TaskAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TaskAdapter();

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvDashboard = view.findViewById(R.id.rv_dashboard);

        initRecycler();
        getDataFromFirestore();

    }

    private void getDataFromFirestore() {

        FirebaseFirestore.getInstance()
                .collection("task")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        List<Task> list = new ArrayList<>();

                        for (DocumentSnapshot snapshot:snapshots){
                            String docId = snapshot.getId();
                            String title = snapshot.getString("title");
                            Task task = new Task(title);
                            task.setDocId(docId);
                            list.add(task);
                        }
                        adapter.addItems(list);
                    }
                });
    }


    private void initRecycler() {
        rvDashboard.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int positon) {

                deleteFromFirestore(positon);
            }
        });
    }

    private void deleteFromFirestore(int position) {
        Task task = adapter.getItem(position);
        FirebaseFirestore.getInstance()
                .collection("task")
                .document(task.getDocId())
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                adapter.remove(position);
            }
        });

    }
}