package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc460.team2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A MessageNamePrompt is a simple Activity that prompts the user for a message
 * name and returns it to the calling Activity.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0424
 */
public class MessageNamePrompt extends Activity {

	/** Handle for the EditText used to input the name */
	private EditText mNameField;

	/** Handle for the accept Button */
	private Button mAcceptBtn;

	/** Handle for the cancel Button */
	private Button mCancelBtn;

	/**
	 * Finishes with canceled status.
	 */
	private void doCancel() {
		setResult(RESULT_CANCELED);
		finish();
	}

	/**
	 * Assigns local handles and callbacks for widgets in the UI.
	 */
	private void loadWidgetsFromView() {
		mNameField = (EditText) findViewById(R.id.name_msg_edittext);
		if (getIntent().hasExtra("name")) {
			mNameField.setText(getIntent().getStringExtra("name"));
		}

		mAcceptBtn = (Button) findViewById(R.id.name_msg_acceptbtn);
		mAcceptBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mNameField.getText().length() > 0) {
					Intent i = new Intent();
					i.putExtra("name", mNameField.getText().toString());
					setResult(RESULT_OK, i);
					finish();
				} else {
					Toast.makeText(getParent(), "Please enter a name",
							Toast.LENGTH_SHORT).show();
				}
			}

		});

		mCancelBtn = (Button) findViewById(R.id.name_msg_cancelbtn);
		mCancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				doCancel();
			}

		});
	}

	@Override
	public void onBackPressed() {
		doCancel();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_name_prompt);
		loadWidgetsFromView();
	}
}
