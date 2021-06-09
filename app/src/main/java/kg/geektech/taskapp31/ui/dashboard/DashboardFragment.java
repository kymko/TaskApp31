package kg.geektech.taskapp31.ui.dashboard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import kg.geektech.taskapp31.R;
import kg.geektech.taskapp31.models.Task;
import kg.geektech.taskapp31.ui.home.TaskAdapter;

public class DashboardFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;
    private RecyclerView rvDashboard;
    Task task;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvDashboard = view.findViewById(R.id.rv_dashboard);
        firebaseFirestore = FirebaseFirestore.getInstance();

        Query query = firebaseFirestore.collection("task");

        // RecyclerOptions
        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions
                .Builder<Task>().
                setQuery(query, Task.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Task, TaskViewHolder>(options) {
            @NonNull
            @Override
            public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_task, parent, false);
                return new TaskViewHolder(view1);
            }

            @Override
            protected void onBindViewHolder(@NonNull TaskViewHolder holder, int position, @NonNull Task model) {
                holder.title.setText(model.getTitle());


                holder.bind(task);
            }
        };
//        rvDashboard.setHasFixedSize(true);
//        rvDashboard.setLayoutManager(new LinearLayoutManager(getContext()));
//        rvDashboard.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvDashboard.setLayoutManager(llm);
        rvDashboard.setAdapter( adapter );

    }

    private class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
        }

        public void bind(Task model) {

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
                    builder.setMessage("Удалить?");
                    builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference reference = db.collection("task").document();

                            reference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(requireActivity(), "item deleted!", Toast.LENGTH_SHORT).show();
                                        adapter.notifyDataSetChanged();
                                        rvDashboard.setAdapter(adapter);
                                    } else {
                                        Toast.makeText(requireActivity(), "error!!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });
                    builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                    return true;

                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

}