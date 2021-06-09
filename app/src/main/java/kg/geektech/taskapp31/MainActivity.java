package kg.geektech.taskapp31;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.profileFragment)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

//        Prefs prefs = new Prefs(this);
//        if (!prefs.isShown()) navController.navigate(R.id.boardFragment);
       // navController.navigate(R.id.phoneFragment);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                ArrayList<Integer> list = new ArrayList<>();
                list.add(R.id.navigation_home);
                list.add(R.id.navigation_dashboard);
                list.add(R.id.navigation_notifications);
                list.add(R.id.profileFragment);

                if (list.contains(destination.getId())) {
                    navView.setVisibility(View.VISIBLE);
                } else {
                    navView.setVisibility(View.GONE);
                }

                if (destination.getId() == R.id.boardFragment) {
                    getSupportActionBar().hide();
                } else {
                    getSupportActionBar().show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }
}






//                      3 Дз.
//        1. Добавить картинки для страниц
//        2. Добавить кнопку на последнюю страницу, по нажатию открыть Home
//        3. TabLayout
//        4. Добавить кнопку SKIP на самый верх страницы (она не должна двигаться)


//                      4 Дз.
//        1. В профиле добавить поле для ввода имени без кнопки сохранения
//        2. Дизайн профиля
//        3. Добавить в меню для очистки preferences


//                     5 Дз.
//        1. Добавить кнопку (menu) сортировки списка по алфавиту
//        2. На долгое нажатие удаление из бд


//                     6 Дз.
//        1. Показать вью для ввода кода смс
//        2. Обратный отсчет, по истечению показать обратно первое вью


//                     7 Дз.
//        1. В Dashboard показывать данные из FireStore
//        2. Чтоб можно было удалять через AlertDialog


//                     8 Дз.
//
//



