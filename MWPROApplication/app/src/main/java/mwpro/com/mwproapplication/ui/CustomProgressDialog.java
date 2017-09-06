package mwpro.com.mwproapplication.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import mwpro.com.mwproapplication.R;

public class CustomProgressDialog extends ProgressDialog{
	
//	private Animation animation;
	private Context context;
//	private ImageView imageView;
	private TextView textView;
	private String msg;
	
	public static ProgressDialog ctor(Context context, String msg) {
		CustomProgressDialog dialog = new CustomProgressDialog (context, msg);

		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		
		return dialog;
	}

	public CustomProgressDialog(Context context, String msg) {
		super(context);
		this.msg = msg;
	}
	
	public CustomProgressDialog(Context context, int theme){
		 super(context, theme);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.customprogressdialogview);

		ColorDrawable drawable = new ColorDrawable(Color.TRANSPARENT);


		this.getWindow().setBackgroundDrawable(drawable);
	}
	
	@Override
	public void show() {
		super.show();
		
	/*	ImageView imageView = (ImageView) findViewById(R.id.animation);
	    imageView.setBackgroundResource(R.drawable.spinnerwhite);
	    
	    textView = (TextView) findViewById(R.id.message);
	    textView.setText(msg);
	    
	    animation = AnimationUtils.loadAnimation(super.getContext(), R.anim.rotate);
	    imageView.startAnimation(animation);*/
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
	}
	
}
