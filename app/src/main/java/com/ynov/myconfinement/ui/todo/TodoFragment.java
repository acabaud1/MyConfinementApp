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
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ynov.myconfinement.R;
import com.ynov.myconfinement.ui.DatabaseManager;

import java.util.ArrayList;
import java.util.Calendar;

public class TodoFragment extends Fragment {

    private DatabaseManager databaseManager;
    private ArrayList<Task> tasks;
    private TaskAdapter adapter;

    private void updateList() {
        adapter.updateTasksList((ArrayList<Task>) databaseManager.getTasks());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_todo, container, false);
        View layout = inflater.inflate(R.layout.fragment_todo_add, container, false);
        databaseManager = new DatabaseManager(getContext());

        FloatingActionButton fab = root.findViewById(R.id.fabAgenda);
        SwipeMenuListView listView = root.findViewById(R.id.listViewAgenda);

        // Add dialog
        final EditText title = (EditText) layout.findViewById(R.id.title);
        final EditText deadline = (EditText) layout.findViewById(R.id.deadline);
        final Spinner category = (Spinner) layout.findViewById(R.id.category);

        tasks = (ArrayList<Task>) databaseManager.getTasks();
        adapter = new TaskAdapter(getActivity().getApplicationContext(), R.layout.fragment_todo_item, tasks);

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
                    title.setText("");
                    deadline.setText("");
                    dialog.dismiss();
                    updateList();
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
                deleteItem.setWidth(150);
                deleteItem.setIcon(R.drawable.ic_delete_black_24dp);
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                // index 0 = delete
                if(index == 0) {
                    databaseManager.deleteTask(tasks.get(position));
                    Toast.makeText(getContext(), "Tâche supprimée.", Toast.LENGTH_SHORT).show();
                    updateList();
                }
                return false;
            }
        });

        listView.setAdapter(adapter);

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