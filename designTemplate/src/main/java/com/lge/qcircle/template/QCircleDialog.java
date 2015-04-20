package com.lge.qcircle.template;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * The {@code QCircleDialog} class provides a Dialog for Quick Circle.<P>
 * It cannot be instantiated by a constructor.
 * Use {@link com.lge.qcircle.template.QCircleDialog.Builder} instead.<P>
 * Created by Yoav.
 *
 * @author Yoav Sternberg
 */
public class QCircleDialog {
	Activity activity;
	QCircleTemplate activityTemplate;
	// Dialog properties
	String title;
	CharSequence text;
	String positiveButtonText;
	String negativeButtonText;
	Drawable image;
	View.OnClickListener positiveButtonListener;
	View.OnClickListener negativeButtonListener;
	DialogMode mode;
	View templateLayout;

	/**
	 * private constructor.
	 *
	 * @param builder
	 */
	private QCircleDialog(Builder builder) {
		this.title = builder.title;
		this.text = builder.text;
		this.positiveButtonText = builder.positiveButtonText;
		this.negativeButtonText = builder.negativeButtonText;
		this.image = builder.image;
		this.positiveButtonListener = builder.positiveButtonListener;
		this.negativeButtonListener = builder.negativeButtonListener;
		this.mode = builder.mode;
	}

	/**
	 * The {@code QCircleDialog.Builder} class is a builder to create a Dialog for Quick Circle.
	 *
	 * @author Yoav Sternberg
	 */
	public static class Builder {
		private String title = null;
		private CharSequence text;
		private String positiveButtonText;
		private String negativeButtonText;
		private Drawable image = null;
		private View.OnClickListener positiveButtonListener;
		private View.OnClickListener negativeButtonListener = null;
		private DialogMode mode = DialogMode.Ok;

		//sujin.cho
		private QCircleTitle mTitle = null;

		/**
		 * sets title of the Dialog.<P>
		 *
		 * @param title text of the title. It will be displayed on the top of the dialog.
		 * @return a Builder with title text
		 */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		/**
		 * sets main text of the dialog.<P>
		 *
		 * @param text main text. It will be displayed on the middle of the dialog.
		 * @return a Builder with main text
		 */
		public Builder setText(CharSequence text) {
			this.text = text;
			return this;
		}

		/**
		 * sets main image of the dialog.<P>
		 *
		 * @param image main image as a Drawable. It will be displayed on the bottom of the main text.
		 * @return a Builder with main image
		 */
		public Builder setImage(Drawable image) {
			this.image = image;
			return this;
		}

		/**
		 * sets a listener for positive button.
		 *
		 * @param positiveButtonListener lister to set
		 * @return a Builder with the listener for positive button
		 */
		public Builder setPositiveButtonListener(View.OnClickListener positiveButtonListener) {
			this.positiveButtonListener = positiveButtonListener;
			return this;
		}

		/**
		 * sets a listener for negative button.
		 *
		 * @param negativeButtonListener listener to set
		 * @return a Builder with the listener for negative button
		 */
		public Builder setNegativeButtonListener(View.OnClickListener negativeButtonListener) {
			this.negativeButtonListener = negativeButtonListener;
			return this;
		}

		/**
		 * sets a mode fot eh dialog.
		 *
		 * @param mode dialog mode. see {@link com.lge.qcircle.template.QCircleDialog.DialogMode}.
		 * @return a Builder with the dialog mode.
		 */
		public Builder setMode(DialogMode mode) {
			this.mode = mode;
			return this;
		}

		/**
		 * sets a text for positive button.
		 *
		 * @param positiveButtonText text for positive button
		 */
		public Builder setPositiveButtonText(String positiveButtonText) {
			this.positiveButtonText = positiveButtonText;
			return this;
		}

		/**
		 * sets a text for negative button.
		 *
		 * @param negativeButtonText text for negative button
		 */
		public Builder setNegativeButtonText(String negativeButtonText) {
			this.negativeButtonText = negativeButtonText;
			return this;
		}

		/**
		 * creates a dialog.<P>
		 * It should be called when setting of the dialog is done by the Builder.
		 *
		 * @return a dialog created by the Builder.
		 */
		public QCircleDialog create() {
			return new QCircleDialog(this);
		}
	}

	/**
	 * Show the dialog
	 *
	 * @param activity         The activity
	 * @param activityTemplate The template that the activity uses
	 */
	public void show(final Activity activity, QCircleTemplate activityTemplate) {
		this.activity = activity;
		this.activityTemplate = activityTemplate;
		RelativeLayout layout = (RelativeLayout) activityTemplate.getLayoutById(TemplateTag.CONTENT).getParent();
		final QCircleTemplate template = new QCircleTemplate(activity);
		QCircleTitle qCircleTitle = new QCircleTitle(activity, title == null ? "" : title, Color.WHITE,
				activity.getResources().getColor(mode == DialogMode.Error ? R.color.dialog_title_background_color_error : R.color.dialog_title_background_color_regular));
		qCircleTitle.setTextSize(17f);
		template.addElement(qCircleTitle);
		RelativeLayout dialogLayout = (RelativeLayout) activity.getLayoutInflater()
				.inflate(mode == DialogMode.YesNo ? R.layout.qcircle_dialog_layout_yes_no : R.layout.qcircle_dialog_layout, layout, false);
		if (text != null) {
			((TextView) dialogLayout.findViewById(R.id.text)).setText(text);
		}
		if (image != null) {
			((ImageView) dialogLayout.findViewById(R.id.image)).setImageDrawable(image);
		} else dialogLayout.findViewById(R.id.image).setVisibility(View.GONE);
		switch (mode) {
			case YesNo:
				Button negativeButton = (Button) dialogLayout.findViewById(R.id.negative);
				if (negativeButtonText != null) {
					negativeButton.setText(negativeButtonText);
				}
				negativeButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (negativeButtonListener != null) negativeButtonListener.onClick(v);
						hide();
					}
				});
			case Ok:
				Button positiveButton = (Button) dialogLayout.findViewById(R.id.positive);
				if (positiveButtonText != null) {
					positiveButton.setText(positiveButtonText);
				}
				positiveButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (positiveButtonListener != null) positiveButtonListener.onClick(v);
						hide();
					}
				});
				break;
			case Error:
				@SuppressLint("CutPasteId")
				Button errorButton = (Button) dialogLayout.findViewById(R.id.positive);
				if (negativeButtonText != null) {
					errorButton.setText(negativeButtonText);
				}
				errorButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (negativeButtonListener != null) negativeButtonListener.onClick(v);
//						template.unregisterReceiver();
						activity.finish();
					}
				});
				break;

		}
		template.getLayoutById(TemplateTag.CONTENT).addView(dialogLayout);
		layout.addView(templateLayout = template.getView());
	}

	/**
	 * hides the dialog.
	 */
	public void hide() {
		((RelativeLayout) activityTemplate.getLayoutById(TemplateTag.CONTENT).getParent()).removeView(templateLayout);
		templateLayout = null;
	}

	/**
	 * The {@code DialogMode} enumerates modes which are
	 * supported by {@link com.lge.qcircle.template.QCircleDialog}.
	 */
	public enum DialogMode {
		/**
		 * A dialog with yes and no buttons.
		 */
		YesNo,
		/**
		 * A dialog with ok button only.
		 */
		Ok,
		/**
		 * An error dialog. Shows only back button, which finish the activity.
		 */
		Error
	}
}
