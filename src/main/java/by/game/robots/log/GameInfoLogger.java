package by.game.robots.log;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import by.game.proxi.IGameActivityTracker;

@Component
public class GameInfoLogger implements IGameActivityTracker, IGameLogger{

	private int maxLogSize = 1000;
	private Long lastLogIndex = 0L;
	private Map<Long,String>logMemory = new HashMap<>();
	
	@Override
	public void log(String message) {
		synchronized(this){
			this.logMemory.put(this.lastLogIndex++, message);
			if(this.logMemory.size()>this.maxLogSize)
				this.logMemory.remove(this.lastLogIndex-this.maxLogSize);
		}	
	}

	@Override
	public long lastIndex(){
		synchronized(this.lastLogIndex){
			return this.lastLogIndex;
		}
	}
	
	@Override
	public String getLogMessage(long index){
		return this.logMemory.get(index);
	}

	@Override
	public long getHeadOfLogTailIndex(int amountOfMessages) {
		long result = 0;
		synchronized(this.lastLogIndex){
			result =  this.lastLogIndex-amountOfMessages;
		}
		if(result<0)result=0;
		return result;
	}

	
	
	

}
