package com.pyesmeadow.george.currencyconverter.saves;

import com.pyesmeadow.george.currencyconverter.util.ResourceUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SavesManager {

	private ArrayList<Save> saves;

	private void serializeCurrencies()
	{
		JSONArray savesArrayJSON = new JSONArray();

		for (Save save : saves)
		{
			JSONObject saveJSON = save.serializeToJSON();
			savesArrayJSON.add(saveJSON);
		}

		try (FileWriter writer = new FileWriter(ResourceUtil.getCurrencyList()))
		{
			savesArrayJSON.writeJSONString(writer);
			writer.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void deserializeCurrencies()
	{
		File savesFile = ResourceUtil.getSavesList();

		ArrayList<Save> saveList = new ArrayList<>();

		try
		{
			// Parse JSON
			FileReader reader = new FileReader(savesFile);
			JSONParser parser = new JSONParser();
			JSONArray savesArrayJSON = (JSONArray) parser.parse(reader);

			for (Object saveJSON : savesArrayJSON)
			{
				saveList.add(Save.deserializeFromJSON((JSONObject) saveJSON));
			}

			this.saves = saveList;
		}
		catch (IOException | ParseException e)
		{
			e.printStackTrace();
		}
	}

}
