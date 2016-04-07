package com.ebksoft.flightbooking.utils;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupManager;
import android.app.backup.FileBackupHelper;

/**
 * Created by chauminhnhut on 4/7/16.
 */

public class BackupAgentUtils extends BackupAgentHelper {

    // The name of the SharedPreferences file
    static final String HIGH_SCORES_FILENAME = "com.ebksoft.flightbooking_preferences.xml";

    // A key to uniquely identify the set of backup data
    static final String FILES_BACKUP_KEY = "appSharePref";

    // Allocate a helper and add it to the backup agent
    @Override
    public void onCreate() {
        FileBackupHelper helper = new FileBackupHelper(this, HIGH_SCORES_FILENAME);
        addHelper(FILES_BACKUP_KEY, helper);
    }


    /*
    * Call this method to backup current share pref
    * */
    public void requestBackup() {
        BackupManager bm = new BackupManager(this);
        bm.dataChanged();
    }

}