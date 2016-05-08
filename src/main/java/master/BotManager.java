package master;

import botconfigs.IRCBot;

/**
 * Created by JKyte on 4/30/2016. This is a test class and should not be run.
 */
public class BotManager {


    public static void main(String[] args) throws InterruptedException {

        IRCBot bot = new IRCBot(true);
        Thread t0 = new Thread(bot);
        t0.start();

        Thread.sleep(5000);

        System.out.println("Stopping bot.");
        bot.stopBot();
        t0.interrupt();

        Thread.sleep(5000);
        System.out.println("Starting bot.");
        bot = new IRCBot(true);
        t0 = new Thread(bot);
        t0.start();
    }

}
