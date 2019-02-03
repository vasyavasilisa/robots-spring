package by.game.robots.utils;

import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import by.game.core.ARobot;
import by.game.core.BRobot;
import by.game.core.CRobot;
import by.game.core.Robot;
import by.game.core.Task;
import by.game.robots.log.GameInfoLogger;

public class JSONUtils {

	public static Robot getRobot(String json) {
		JSONParser parser = new JSONParser();
		JSONObject ob = null;
		try {
			ob = (JSONObject) parser.parse(json);
		} catch (ParseException e) {
			return null;
		}
		String type = (String) ob.get("type");
		if (type == null)
			return null;
		String name = (String) ob.get("name");
		switch (type) {
		case "A":
			return new ARobot(name);
		case "B":
			return new BRobot(name);
		case "C":
			return new CRobot(name);
		default:
			return null;
		}
	}

	public static Task getTask(String json) {
		JSONParser parser = new JSONParser();
		JSONObject ob = null;
		try {
			ob = (JSONObject) parser.parse(json);
		} catch (ParseException e) {
			return null;
		}
		String type = (String) ob.get("type");
		if (type == null)
			return null;
		return new Task(type);
	}

	public static Long getLongValueFromJsonObject(String json){
		JSONParser parser = new JSONParser();
		JSONObject ob = null;
		Long value = null;
		try {
			ob = (JSONObject) parser.parse(json);
			value = (Long) ob.get("value");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public static String singleObjectToJsonArray(Object o){
		if(o==null)return null;
		JSONArray json = new JSONArray();
		json.add(o.toString());
		return json.toJSONString();
	}
	
	public static String getLogMessagesToJson(GameInfoLogger logger, long id){
		JSONArray json = new JSONArray();
		long tail = logger.lastIndex();
		if(id>tail)return json.toJSONString();
		while(id<tail){
			JSONObject ob = new JSONObject();
			ob.put("message", logger.getLogMessage(id));
			ob.put("id", id++);
			json.add(ob);
			
		}
		return json.toJSONString();
	}

}
