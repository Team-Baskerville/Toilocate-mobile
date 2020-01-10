package com.baskerville.toilocate.interfaces;

import android.content.DialogInterface;

public interface DialogCloseListener {
    public void handleDialogClose(DialogInterface dialog, boolean success);
}
