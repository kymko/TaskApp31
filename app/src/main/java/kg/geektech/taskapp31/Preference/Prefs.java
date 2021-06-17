package kg.geektech.taskapp31.Preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

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

    public void clearPref(){
        preferences.edit().clear().commit();
    }

    public void putString(String autoSave, String s) {
        preferences.edit().putString(autoSave, s).apply();
    }

    public String getString(String autoSave){
        return preferences.getString(autoSave, "");

    }
    public void saveImage(Uri uri){
        preferences.edit().putString("avatar",uri.toString()).apply();
    }
    public Uri getAvatar(){
        return Uri.parse(preferences.getString("avatar",null));

    }
}
