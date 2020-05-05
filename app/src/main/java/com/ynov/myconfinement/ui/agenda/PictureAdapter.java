package com.ynov.myconfinement.ui.agenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ynov.myconfinement.R;

import java.util.ArrayList;

public class PictureAdapter extends ArrayAdapter<Picture> {

    private final Context _context;
    private ArrayList<Picture> _pictures;

    public PictureAdapter(Context context, int resource, ArrayList<Picture> pictures) {
        super(context, resource, pictures);
        _context = context;
        _pictures = pictures;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_picture_item, parent, false);
        } else {
            convertView = (RelativeLayout) convertView;
        }

        ImageView viewPicture = (ImageView) convertView.findViewById(R.id.picture);
        TextView viewDate = (TextView) convertView.findViewById(R.id.date);
        TextView viewLocation = (TextView) convertView.findViewById(R.id.location);

        viewPicture.setImageURI(_pictures.get(position).getUri());
        viewDate.setText(_pictures.get(position).getDate());
        viewLocation.setText(_pictures.get(position).getLocation());

        return convertView;
    }

    public void updatePicturesList(ArrayList<Picture> newPictures) {
        _pictures.clear();
        _pictures.addAll(newPictures);
        this.notifyDataSetChanged();
    }

}
