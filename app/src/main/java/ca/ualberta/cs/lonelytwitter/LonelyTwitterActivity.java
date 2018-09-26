package ca.ualberta.cs.lonelytwitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LonelyTwitterActivity extends Activity {

	private static final String FILENAME = "file.sav";
	private EditText bodyText;
	private ListView oldTweetsList;

	ArrayList<Tweet> tweetList;
	ArrayAdapter<Tweet> adapter;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) { // first thing that runs
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


		bodyText = (EditText) findViewById(R.id.body);
		Button saveButton = (Button) findViewById(R.id.save);
		oldTweetsList = (ListView) findViewById(R.id.oldTweetsList);

		saveButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				setResult(RESULT_OK);
				String text = bodyText.getText().toString();

				Tweet tweet = new NormalTweet(text);
				tweetList.add(tweet);



				saveInFile(); //saving the text in to the file
				adapter.notifyDataSetChanged();


			}
		});
	}

	@Override
	protected void onStart() { //gets called by itself just after on create
		super.onStart();
		loadFromFile();
		//adapter.notifyDataSetChanged();
		//String[] tweets = loadFromFile();
		adapter = new ArrayAdapter<Tweet>(this,
				R.layout.list_item, tweetList); //creating an adapter = an interface which connects your list view to the data
		oldTweetsList.setAdapter(adapter);
	}

	private void loadFromFile() {

		try {
			FileInputStream fis = openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis)); // read each line out of the file

			Gson gson = new Gson();//library we will use to save objects
			Type listType = new TypeToken<ArrayList<NormalTweet>>(){}.getType();

			tweetList = gson.fromJson(in, listType);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			tweetList = new ArrayList<Tweet>();
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void saveInFile() {
		try {



		    FileOutputStream fos = openFileOutput(FILENAME,
					Context.MODE_PRIVATE);//PREVIOUSLY DEALING WITH STRINGS NOW DEALING WITH OBJECTS...THIS FILE WHICH WE ARE SAVING TO CAN ONLY BE USED BY OUR APPLICATION

			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
			Gson gson = new Gson();
			gson.toJson(tweetList, out);
			out.flush();

			fos.close();
		} catch (FileNotFoundException e) {
			tweetList = new ArrayList<Tweet>();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}