package com.ynov.myconfinement.ui.agenda;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ynov.myconfinement.R;
import com.ynov.myconfinement.ui.DatabaseManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AgendaFragment extends Fragment {

    private DatabaseManager databaseManager;
    private ArrayList<Picture> pictures;
    private PictureAdapter adapter;

    String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;

    private void updateList() {
        adapter.updatePicturesList((ArrayList<Picture>) databaseManager.getPictures());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_agenda, container, false);
        databaseManager = new DatabaseManager(getContext());

        FloatingActionButton fabAgenda = root.findViewById(R.id.fabAgenda);
        ListView listViewAgenda = root.findViewById(R.id.listViewAgenda);

        pictures = (ArrayList<Picture>) databaseManager.getPictures();
        adapter = new PictureAdapter(getActivity().getApplicationContext(), R.layout.fragment_picture_item, pictures);
        listViewAgenda.setAdapter(adapter);

        fabAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        return root;
    }

    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("TAKE_PICTURE", "Error while taking picture");
            }

            if (photoFile != null)
            {
                String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                Uri photoURI = FileProvider.getUriForFile(getContext(), "com.ynov.myconfinement.fileprovider", photoFile);
                databaseManager.insertPicture(new Picture(photoURI, currentDate, "Lyon"));

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                Toast.makeText(getContext(), "Agenda : image ajoutée.", Toast.LENGTH_SHORT).show();
                updateList();
            }
        }
    }

}