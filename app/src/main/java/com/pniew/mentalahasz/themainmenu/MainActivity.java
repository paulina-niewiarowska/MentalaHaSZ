package com.pniew.mentalahasz.themainmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TintableCheckedTextView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pniew.mentalahasz.model.database.entities.ArtPeriod;
import com.pniew.mentalahasz.model.database.entities.Type;
import com.pniew.mentalahasz.thelistofthings.ListOfThingsActivity;
import com.pniew.mentalahasz.thegallery.choosing.ChooseActivity;
import com.pniew.mentalahasz.R;
import com.pniew.mentalahasz.thetest.TestActivity;
import com.pniew.mentalahasz.utils.Permissions;
import com.pniew.mentalahasz.web.WebActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.pniew.mentalahasz.utils.CallsStringsIntents.*;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonLearn = findViewById(R.id.button_learn);
        buttonLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemsToLoad = new Intent(MainActivity.this, ChooseActivity.class);
                itemsToLoad.putExtra(LOAD, ART_PERIOD);
                itemsToLoad.putExtra(CALLED_BY, MAIN_MENU);
                MainActivity.this.startActivity(itemsToLoad);
            }
        });

        Button buttonTest = findViewById(R.id.button_test);
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        });

        Button buttonWeb = findViewById(R.id.button_learn_from_web);
        buttonWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Permissions.showPhoneStatePermission(MainActivity.this, Manifest.permission.INTERNET, Permissions.PERMISSION_INTERNET, getString(R.string.web_needs_internet_permission));
            }
        });

        LifecycleOwner owner = this;
        Button buttonAddCard = findViewById(R.id.button_add_new_card);
        buttonAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddPictureActivity(MainActivity.this, MAIN_MENU, NULL, NULL);
            }

        });


        Button buttonEditThings = findViewById(R.id.button_show_all_things);
        buttonEditThings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toDo = new Intent(MainActivity.this, ListOfThingsActivity.class);
                MainActivity.this.startActivity(toDo);
            }
        });


    }

    private void showDialog() {
        new AlertDialog.Builder(MainActivity.this, R.style.HaSZDialogTheme)
                .setTitle("Cannot find periodization items")
                .setMessage("Looks like you lack some periodization items.\n" +
                        "To be able to add new picture you have to have at least one Art Period and at least one Type of item.\n" +
                        "Please add some periodization items before you come here to add new pictures.")
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Permissions.PERMISSION_INTERNET:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted
                    Permissions.showPhoneStatePermission(MainActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Permissions.PERMISSION_WRITE_EXTERNAL_STORAGE,
                            getString(R.string.web_needs_write_storage_permission));
                } else if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            case Permissions.PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted
                    Intent intent = new Intent(MainActivity.this, WebActivity.class);
                    MainActivity.this.startActivity(intent);
                } else if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.HaSZDialogTheme);
            builder.setTitle("About").setMessage(R.string.about)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }
}