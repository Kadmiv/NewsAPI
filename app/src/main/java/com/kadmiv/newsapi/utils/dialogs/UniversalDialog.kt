package com.kadmiv.lesson_6.abstractions.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

class UniversalDialog : DialogFragment() {


    lateinit var doOnCancel: () -> Unit
    lateinit var alertDialog: AlertDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        alertDialog = AlertDialog.Builder(activity!!)
                .setView(binding.root)
                .create()
        return alertDialog
    }

    fun isShowing(): Boolean {
        return alertDialog.isShowing
    }

    companion object Builder {
        lateinit var binding: ViewDataBinding
    }

    override fun onCancel(dialog: DialogInterface?) {
        doOnCancel()
        super.onCancel(dialog)
    }

}