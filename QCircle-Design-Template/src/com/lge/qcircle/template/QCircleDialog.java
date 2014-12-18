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
 * Created by Yoav.
 */
public class QCircleDialog {
	Activity activity;
	QCircleTemplate activityTemplate;
	// Dialog properties
	String title;
	String text;
	String positiveButtonText;
	String negativeButtonText;
	Drawable image;
	View.OnClickListener positiveButtonListener;
	View.OnClickListener negativeButtonListener;
	DialogMode mode;
	View templateLayout;

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

	public static class Builder {
		private String title = null;
		private String text;
		private String positiveButtonText;
		private String negativeButtonText;
		private Drawable image = null;
		private View.OnClickListener positiveButtonListener;
		private View.OnClickListener negativeButtonListener = null;
		private QCircleDialog.DialogMode mode = DialogMode.Ok;

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setText(String text) {
			this.text = text;
			return this;
		}

		public Builder setImage(Drawable image) {
			this.image = image;
			return this;
		}

		public Builder setPositiveButtonListener(View.OnClickListener positiveButtonListener) {
			this.positiveButtonListener = positiveButtonListener;
			return this;
		}

		public Builder setNegativeButtonListener(View.OnClickListener negativeButtonListener) {
			this.negativeButtonListener = negativeButtonListener;
			return this;
		}

		public Builder setMode(QCircleDialog.DialogMode mode) {
			this.mode = mode;
			return this;
		}

		public void setPositiveButtonText(String positiveButtonText) {
			this.positiveButtonText = positiveButtonText;
		}

		public void setNegativeButtonText(String negativeButtonText) {
			this.negativeButtonText = negativeButtonText;
		}

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
		template.setTitle(title == null ? "" : title, Color.WHITE, activity.getResources().getColor(
				mode == DialogMode.Error ? R.color.dialog_title_background_color_error : R.color.dialog_title_background_color_regular));
		template.setTitleTextSize(17);
		RelativeLayout dialogLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.qcircle_dialog_layout, layout, false);
		if (text != null) {
			((TextView) dialogLayout.findViewById(R.id.text)).setText(text);
		}
		if (image != null) {
			((ImageView) dialogLayout.findViewById(R.id.image)).setImageDrawable(image);
		}
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
						template.unregisterReceiver();
						activity.finish();
					}
				});
				break;

		}
		if (mode != DialogMode.YesNo)
			dialogLayout.findViewById(R.id.negative).setVisibility(View.GONE);
		template.getLayoutById(TemplateTag.CONTENT).addView(dialogLayout);
		layout.addView(templateLayout = template.getView());

	}

	public void hide() {
		((RelativeLayout) activityTemplate.getLayoutById(TemplateTag.CONTENT).getParent()).removeView(templateLayout);
		templateLayout = null;
	}

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
