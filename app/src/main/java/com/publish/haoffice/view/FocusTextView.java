package com.publish.haoffice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
public class FocusTextView extends TextView {

	public FocusTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FocusTextView(Context context) {
		super(context);
	}

	
	@Override
	public boolean isFocused() {
		return true;
	}
	
}
