package com.ynov.myconfinement.ui.todo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ynov.myconfinement.R;
import com.ynov.myconfinement.ui.DatabaseManager;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class TodoFragment extends Fragment {

    private TodoViewModel galleryViewModel;
    private DatabaseManager databaseManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        galleryViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_todo, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        SwipeMenuListView listView = root.findViewById(R.id.listView);

        View layout = inflater.inflate(R.layout.fragment_todo_add, container, false);
        databaseManager = new DatabaseManager(getContext());

        // Add dialog
        final EditText title = (EditText) layout.findViewById(R.id.title);
        final EditText deadline = (EditText) layout.findViewById(R.id.deadline);
        final Spinner category = (Spinner) layout.findViewById(R.id.category);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(layout);

        builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String bTitle = title.getText().toString();
                String bDeadline = deadline.getText().toString();

                if(TextUtils.isEmpty(bTitle) || TextUtils.isEmpty(bDeadline)) {
                    Toast.makeText(getContext(), "Vous devez indiquer un titre et une deadline.", Toast.LENGTH_SHORT).show();
                } else {
                    Task task = new Task(bTitle, bDeadline, category.getSelectedItem().toString());
                    databaseManager.insertTask(task);
                    Toast.makeText(getContext(), "Tâche ajoutée.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();

        // Floating button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(90);
                deleteItem.setIcon(R.drawable.ic_delete_black_24dp);
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                // index 0 = delete
                return false;
            }
        });

        // Date & Time pickers
        final Calendar c = Calendar.getInstance();

        final TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                deadline.setText(deadline.getText().toString() + " " + hours + ":" + minutes);
            }
        };

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                int mHours = c.get(Calendar.HOUR_OF_DAY);
                int mMinutes = c.get(Calendar.MINUTE);
                deadline.setText(day + "/" + month + "/" + year);

                TimePickerDialog dialog = new TimePickerDialog(getContext(),
                        timeSetListener, mHours + 1, mMinutes, true);
                dialog.show();
            }
        };

        deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        dateSetListener, mYear, mMonth, mDay);
                dialog.show();
            }
        });

        return root;
    }
}