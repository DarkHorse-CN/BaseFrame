package com.darkhorse.baseframe.interfaces

import android.app.Dialog

interface IWhetherDialogListener {
        fun confirm(dialog: Dialog)
        fun deny(dialog: Dialog)
    }