package com.receipt.invoice.stock.sirproject.Utils.HtmlService.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.receipt.invoice.stock.sirproject.Utils.HtmlService.domain.HtmlFile;

/**
 * This class creates your HTML its input is an array of Strings that in this scenario are used to show the persons name
 * Once the HTML is created it is saved to the file system
 * We then wrap this file in our own domain object type and send it back to the calling class
 *
 * @author paul.blundell
 */
public class CreateHtmlTask extends AsyncTask<String, Integer, HtmlFile>{

	// This is an interface so whoever started this task can be informed when it is finished
	public interface OnTaskFinishedListener {
		void onHtmlCreated(HtmlFile html);
	}

	// The finished listener
	private final OnTaskFinishedListener taskFinishedListener;
	private final File folder;

	// Let the listener be set in the constructor (making it obvious to anyone using this class they can be informed when it is finished)
	// Note they can still pass null to not listen
	public CreateHtmlTask(File storageFolder, OnTaskFinishedListener taskFinishedListener) {
		this.folder = storageFolder;
		this.taskFinishedListener = taskFinishedListener;
	}

	@SuppressLint("LongLogTag")
	@Override
	protected HtmlFile doInBackground(String... params) {
		// We are wrapping the File in our own domain object so we can add some convenience methods to it
		HtmlFile htmlFile;
		try {
			// Create whatever HTML you want here - don't forget to escape strings
			String link = params[0];
			StringBuilder builder = new StringBuilder();
			builder.append("<html>");
			builder.append("<head>");
			builder.append("</head>");
			builder.append("<body>");
			builder.append("<p>");
			builder.append("Hello ");
			builder.append("<a href=\"https://www.w3docs.com/\" class=\"button\">Click Here</a>");
			builder.append("<button type=\"button\">Click Me!</button>");
//			builder.append("<img src=\"http://developer.android.com/images/jb-android-4.1.png\"/>");
			builder.append("</body>");
			builder.append("</html>");
			String content = builder.toString();
			// Store the file


			String ss = "<!DOCTYPE html>\n" +
					"<html>\n" +
					"  <head>\n" +
					"    <title>Title of the document</title>\n" +
					"    <style>\n" +
					"      .button {\n" +
					"        background-color: #1c87c9;\n" +
					"        border: none;\n" +
					"        color: white;\n" +
					"        padding: 20px 34px;\n" +
					"        text-align: center;\n" +
					"        text-decoration: none;\n" +
					"        display: inline-block;\n" +
					"        font-size: 20px;\n" +
					"        margin: 4px 2px;\n" +
					"        cursor: pointer;\n" +
					"      }\n" +
					"    </style>\n" +
					"  </head>\n" +
					"  <body>\n" +
					"    <a href=\""+link+"\" class=\"button\">Cards</a>\n" +
					"  </body>\n" +
					"</html>";


			File file = createTempFile(folder, "Cards.html", ss);
			// Create our domain object wrapping the file
			htmlFile = new HtmlFile(file);
		} catch (IOException e) {
			Log.e("IOException - creating safe HtmlFile", ""+e);
			// Create a 'NullSafe' HtmlFile object if an error occurs
			htmlFile = new HtmlFile(null);
		}
		return htmlFile;
	}

	/**
	 * Creates a file - doesn't do any clean up
	 * @param folder - the folder to save the file in
	 * @param filename - the file name
	 * @param fileContent - the content to put in the file
	 * @return the created File
	 * @throws IOException - if anything goes wrong :-(
	 */
	private static File createTempFile(File folder, String filename, String fileContent) throws IOException {
		File f = new File(folder, filename);
		f.createNewFile();
		BufferedWriter buf = new BufferedWriter(new FileWriter(f));
		buf.append(fileContent);
		buf.close();
		return f;
    }

	@Override
	protected void onPostExecute(HtmlFile result) {
		super.onPostExecute(result);
		// Inform the class listening we have finished - sending back the completed Html File
		if(this.taskFinishedListener != null)
			this.taskFinishedListener.onHtmlCreated(result);
	}
}