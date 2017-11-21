package by.tuzau.andrey.rusandroidapp.data.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.tuzau.andrey.rusandroidapp.data.domain.AbstractFileSystemElement;
import by.tuzau.andrey.rusandroidapp.data.domain.FileElement;
import by.tuzau.andrey.rusandroidapp.data.domain.FolderElement;
import by.tuzau.andrey.rusandroidapp.data.domain.TypeElement;
import by.tuzau.andrey.rusandroidapp.data.service.exception.ServiceException;

/**
 * Created by User on 20.11.2017.
 */

public class FileService {

    private Context context;
    private Map<String, Boolean> mapIsAudioEmpty;


    public FileService(Context context) {
        this.context = context;
    }

    public FolderElement createFolder() throws ServiceException {
        Map<String, FolderElement> mapFolderElement = new HashMap<>();
        mapIsAudioEmpty = new HashMap<>();
        List<String> listAudioPath = getListAudioPath();
        FolderElement lastFolderElement = null;
        for (String path : listAudioPath) {
            String[] listName = path.split(File.separator);
            String pathCurrentFolder = "";
            for (int i = 1; i < listName.length - 1; i++) {
                String name = listName[i];
                pathCurrentFolder += File.separator + name;

                if (!mapFolderElement.containsKey(pathCurrentFolder)) {
                    FolderElement folderElement = new FolderElement(name, pathCurrentFolder);
                    folderElement.setCountAudioFiles(1);
                    if (lastFolderElement != null) {
                        lastFolderElement.addAbstractFileSystemElement(folderElement);
                        folderElement.setRootFolderElement(lastFolderElement);
                    }
                    lastFolderElement = folderElement;
                    mapFolderElement.put(pathCurrentFolder, folderElement);
                    mapIsAudioEmpty.put(pathCurrentFolder, true);
                } else {
                    lastFolderElement = mapFolderElement.get(pathCurrentFolder);
                    lastFolderElement.addCountAudioFiles(1);
                }
            }
            // Adding audio file
            String audioName = listName[listName.length - 1];
            lastFolderElement.addAbstractFileSystemElement(new FileElement(audioName,
                    pathCurrentFolder + File.separator + audioName));
            mapIsAudioEmpty.put(pathCurrentFolder, false);
        }

        FolderElement rootFolderElement = getRootFolderElement(lastFolderElement);
        reduceTreeFolderElement(rootFolderElement);

        rootFolderElement = getRootFolderElement(lastFolderElement);


        return rootFolderElement;
    }

    private FolderElement getRootFolderElement(FolderElement anyFolderElement) {
        while (anyFolderElement.getRootFolderElement() != null) {
            anyFolderElement = anyFolderElement.getRootFolderElement();
        }
        return anyFolderElement;
    }

    private void reduceTreeFolderElement(FolderElement rootFolderElement) {
        // If only one folder inside this folder and there is not any mp3 files
        boolean isAudioEmpty = mapIsAudioEmpty.get(rootFolderElement.getFullPath());
        if (isAudioEmpty) {
            int countFolder = rootFolderElement.getListFileSystemElement().size();
            if (countFolder == 1) {
                // Combine two folder in one
                FolderElement folderElement = (FolderElement) rootFolderElement.getListFileSystemElement().get(0);
                folderElement.setName(rootFolderElement.getName() + File.separator + folderElement.getName());
                FolderElement topFolderElement = rootFolderElement.getRootFolderElement();
                folderElement.setRootFolderElement(topFolderElement);
                if (topFolderElement != null) {
                    topFolderElement.addAbstractFileSystemElement(folderElement);
                    topFolderElement.getListFileSystemElement().remove(rootFolderElement);
                }
            }
        }
        int countItems = rootFolderElement.getListFileSystemElement().size();
        // Getting around all elements into folderElement
        for (int i = 0; i < countItems; i++) {
            AbstractFileSystemElement item = rootFolderElement.getListFileSystemElement().get(i);
            if (item.getTypeElement() == TypeElement.FOLDER) {
                reduceTreeFolderElement((FolderElement) item);
            }
        }
    }

    private List<String> getListAudioPath() {
        List<String> listPath = null;
        ContentResolver resolver = context.getContentResolver();
        // Data for the request
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.DATA};
        String selection = MediaStore.Audio.Media.DATA + " like '%.mp3'";
        Cursor cursor = resolver.query(uri, null, selection, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            listPath = new ArrayList<>();
            int pathId = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                listPath.add((String) cursor.getString(pathId));
            } while (cursor.moveToNext());
        }
        return listPath;
    }
}
