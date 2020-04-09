package com.ynov.myconfinement.ui.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class TodoFragment extends Fragment {

    private TodoViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        galleryViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_todo, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        SwipeMenuListView listView = root.findViewById(R.id.listView);

        View layout = inflater.inflate(R.layout.fragment_todo_add, container, false);

        final EditText title = (EditText) layout.findViewById(R.id.title);
        final EditText deadline = (EditText) layout.findViewById(R.id.deadline);
        final Spinner category = (Spinner) layout.findViewById(R.id.category);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(layout);

        builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // save info where you want it
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Create a todo list entry", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                dialog.show();
            }
        });

        //final TextView textView = root.findViewById(R.id.text_todo);
        /*galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                /*SwipeMenuItem openItem = new SwipeMenuItem(getActivity().getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                openItem.setWidth(90);
                openItem.setTitle("Open");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);*/

                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(90);
                deleteItem.setIcon(R.drawable.ic_delete_black_24dp);
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        listView.setMenuCreator(creator);

        // handle click
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                // index 0 = delete
                return false;
            }
        });

        return root;
    }
}