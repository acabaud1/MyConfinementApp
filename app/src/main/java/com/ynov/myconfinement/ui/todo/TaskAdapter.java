package com.ynov.myconfinement.ui.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ynov.myconfinement.R;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<Task> {

    private final Context _context;
    private ArrayList<Task> _tasks;

    public TaskAdapter(Context context, int resource, ArrayList<Task> tasks) {
        super(context, resource, tasks);
        _context = context;
        _tasks = tasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_todo_item, parent, false);
        } else {
            convertView = (RelativeLayout) convertView;
        }

        TextView viewTitle = (TextView) convertView.findViewById(R.id.title);
        TextView viewDeadline = (TextView) convertView.findViewById(R.id.deadline);
        TextView viewCategory = (TextView) convertView.findViewById(R.id.category);

        viewTitle.setText(_tasks.get(position).getTitle());
        viewDeadline.setText(_tasks.get(position).getDeadline());
        viewCategory.setText(_tasks.get(position).getCategory());

        return convertView;
    }

    public void updateTasksList(ArrayList<Task> newTasks) {
        _tasks.clear();
        _tasks.addAll(newTasks);
        this.notifyDataSetChanged();
    }

}
