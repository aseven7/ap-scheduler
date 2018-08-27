package com.tanuputra.apsch.test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.tanuputra.apsch.entity.ApEntityJobJSON;

// TODO: Auto-generated Javadoc
/**
 * The Class TestGsonParser.
 */
public class TestGsonParser {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Gson gson = new Gson();
		try {
			JsonElement json = gson.fromJson(new FileReader("E:/ap.json"), JsonElement.class);
			System.out.println(gson.toJson(json));
			System.out.println("---------");
			ApEntityJobJSON apJobJson = gson.fromJson(new FileReader("E:/ap.json"), ApEntityJobJSON.class);
			System.out.println(apJobJson.jobs.size());
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
