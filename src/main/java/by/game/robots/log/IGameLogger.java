package by.game.robots.log;

public interface IGameLogger {
	public long lastIndex();
	public String getLogMessage(long index);
	public long getHeadOfLogTailIndex(int amountOfMessages);
}
