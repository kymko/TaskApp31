package kg.geektech.taskapp31.Preference;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private SharedPreferences preferences;

    public Prefs(Context context) {
        preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public void saveBoardState() {
        preferences.edit().putBoolean("isShown", true).apply();
    }

    public boolean isShown() {
        return preferences.getBoolean("isShown", false);
    }
}
