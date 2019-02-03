package by.game.robots.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import by.game.core.Robot;
import by.game.core.Task;
import by.game.proxi.IGameActivityTracker;
import by.game.proxi.IGameWorld;
import by.game.robots.log.GameInfoLogger;
import by.game.robots.log.IGameLogger;
import by.game.robots.utils.JSONUtils;

@RestController
@RequestMapping("/main")
public class MainController {

	@Autowired
	private IGameWorld game;
	@Autowired
	private IGameActivityTracker tracker;
	@Autowired
	private IGameLogger gameLogger;

	@PostMapping("/newrobot")
	public void putOrder(@RequestBody String body) {
		Robot robot = JSONUtils.getRobot(body);
		if (robot == null) {
			tracker.log("I am sorry. I can`t read this: " + body);
		} else {
			this.game.addRobot(robot);
		}
	}

	@PostMapping("/newtask")
	public void putTask(@RequestBody String body) {
		Task task = JSONUtils.getTask(body);
		if (task == null) {
			tracker.log("I am sorry. I can`t read this: " + body);
		} else
			this.game.addTask(task);
	}

	@PostMapping("/taskfor")
	public void putTaskForRobot(@RequestBody String body, @RequestParam(required = true, name = "name") String robot) {
		Task task = JSONUtils.getTask(body);
		if (task == null) {
			tracker.log("I am sorry. I can`t read this: " + body);
		} else
			this.game.addTask(robot, task);
	}

	@PutMapping("/taskmaxtime")
	public void putMaxTaskTime(@RequestBody String body) {
		Long newValue = JSONUtils.getLongValueFromJsonObject(body);
		if (newValue != null) {
			long i = newValue.longValue();
			this.tracker.log("Setting new max task time: " + newValue.longValue());
			this.game.setMaxTaskTime(i);
		}
	}

	@PutMapping("/taskinterval")
	public void putTaskInterval(@RequestBody String body) {
		Long newValue = JSONUtils.getLongValueFromJsonObject(body);
		if (newValue != null) {
			long i = newValue.longValue();
			this.tracker.log("Setting new max taskinterval: " + i);
			this.game.setTaskInterval(i);
		}
	}

	@PutMapping("/taskthreshold")
	public void putMaxTaskAmountThreshold(@RequestBody String body) {
		
		Long newValue = JSONUtils.getLongValueFromJsonObject(body);
		if (newValue != null) {
			int i = newValue.intValue();
			this.tracker.log("Setting new threshold of amount of tasks: " + i);
			this.game.setTaskAmountThreshold(i);
		}
	}

	@GetMapping("/last_log_index")
	public String getLastLogIndex() {
		return Long.toString(this.gameLogger.lastIndex());
	}

	@GetMapping("/message")
	public String getLogMessage(@RequestParam(required = true, name = "id") long index) {
		return this.gameLogger.getLogMessage(index);
	}

	@GetMapping("/log_from_id")
	public String getLogMessagesFromLogId(@RequestParam(required = true, name = "id") long id) {
		return JSONUtils.getLogMessagesToJson((GameInfoLogger) this.gameLogger, id);
	}

	@GetMapping("/log_tail")
	public String getLogTail(@RequestParam(required = true, name = "amount") int amount) {
		long head = this.gameLogger.getHeadOfLogTailIndex(amount);
		return JSONUtils.getLogMessagesToJson((GameInfoLogger) this.gameLogger, head);
	}
}
