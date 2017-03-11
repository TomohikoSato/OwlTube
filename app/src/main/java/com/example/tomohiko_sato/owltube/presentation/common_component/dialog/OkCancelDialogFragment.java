package com.example.tomohiko_sato.owltube.presentation.common_component.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class OkCancelDialogFragment extends DialogFragment {
	private DialogInteractionListener listener;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (!(context instanceof DialogInteractionListener)) {
			throw new UnsupportedOperationException("DialogInteractionListenerを実装してください");
		}

		listener = (DialogInteractionListener) context;
	}

	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);

		return new AlertDialog.Builder(getActivity())
				.setMessage("ダイアログ")
				.setPositiveButton("はい", (dialog, id) -> listener.onOkClicked())
				.setNegativeButton("キャンセル", (dialog, id) -> listener.onCancelClicked())
				.create();
	}

	public interface DialogInteractionListener {
		void onOkClicked();

		void onCancelClicked();
	}
}
