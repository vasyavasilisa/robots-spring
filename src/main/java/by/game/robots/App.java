package by.game.robots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import by.game.core.Game;
import by.game.proxi.IGameActivityTracker;
import by.game.proxi.IGameWorld;


@SpringBootApplication
public class App 
{
    public static void main( String[] args )
    {
    	ApplicationContext context = SpringApplication.run(App.class);
    	context.getBean(IGameWorld.class)
    	.setIGameActivityTracker(context.getBean(IGameActivityTracker.class)).startGame();;
    }
   
    @Bean
    IGameWorld game(){
    	return Game.instance();
    }
}
