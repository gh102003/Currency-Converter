package com.pyesmeadow.george.currencyconverter.save;

import com.pyesmeadow.george.currencyconverter.main.CurrencyConverter;
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
import java.util.Iterator;

public class SaveManager implements Iterable<Save> {

	private ArrayList<Save> saves;

	public SaveManager()
	{
		saves = new ArrayList<>();

		// Sync JSON with ArrayList
		deserializeSaves();
		serializeSaves();
	}

	public void addSave(Save save)
	{
		saves.add(save);
		serializeSaves();

		CurrencyConverter.frame.panelSaves.repopulateSaves();
	}

	public boolean removeSave(Save save)
	{
		boolean existed = saves.remove(save);

		serializeSaves();
		CurrencyConverter.frame.panelSaves.repopulateSaves();

		return existed;
	}

	private void serializeSaves()
	{
		JSONArray savesArrayJSON = new JSONArray();

		for (Save save : saves)
		{
			JSONObject saveJSON = save.serializeToJSON();
			savesArrayJSON.add(saveJSON);
		}

		try (FileWriter writer = new FileWriter(ResourceUtil.getSavesList()))
		{
			savesArrayJSON.writeJSONString(writer);
			writer.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void deserializeSaves()
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

	@Override
	public Iterator<Save> iterator()
	{
		return saves.iterator();
	}
}
