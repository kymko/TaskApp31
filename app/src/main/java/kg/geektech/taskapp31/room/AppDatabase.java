package kg.geektech.taskapp31.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import kg.geektech.taskapp31.Image;
import kg.geektech.taskapp31.models.Task;
import kg.geektech.taskapp31.ui.home.TaskAdapter;

@Database(entities = {Task.class, Image.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

}
