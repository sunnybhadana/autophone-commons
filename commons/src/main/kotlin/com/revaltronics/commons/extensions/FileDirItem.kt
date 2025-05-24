package com.revaltronics.commons.extensions

import android.content.Context
import com.revaltronics.commons.models.FileDirItem

fun FileDirItem.isRecycleBinPath(context: Context): Boolean {
    return path.startsWith(context.recycleBinPath)
}
