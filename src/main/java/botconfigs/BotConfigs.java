package botconfigs;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author JKyte
 *
 * An object that encapsulates the bot configurations file
 *
 */
public class BotConfigs {

    //	Core configs
    String botnick;
    String botpasswd;

    int heartbeat;

    //	IRC Connection Configs
    int ircport;
    String ircserver;

    //	Starting channels and ajoins
    String startChan;
    ArrayList<String> ajoins;
    //	Security Configurations
    String admin;
    HashSet<String> trustedUsers;
    //	Action configurations
    HashSet<String> greetingChans;
    HashSet<String> farewellChans;
    HashSet<String> storyChans;
    //  Determines if the bot starts a GUI
    private boolean headless;

    public String getBotnick() {
        return botnick;
    }

    public void setBotnick(String botnick) {
        this.botnick = botnick;
    }

    public String getBotpasswd() {
        return botpasswd;
    }

    public void setBotpasswd(String botpasswd) {
        this.botpasswd = botpasswd;
    }

    public int getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }

    public int getIrcport() {
        return ircport;
    }

    public void setIrcport(int ircport) {
        this.ircport = ircport;
    }

    public String getIrcserver() {
        return ircserver;
    }

    public void setIrcserver(String ircserver) {
        this.ircserver = ircserver;
    }

    public String getStartChan() {
        return startChan;
    }

    public void setStartChan(String startChan) {
        this.startChan = startChan;
    }

    public boolean isHeadless() {
        return headless;
    }

    public void setHeadless(boolean headless) {
        this.headless = headless;
    }

    public ArrayList<String> getAjoins() {
        return ajoins;
    }

    public void setAjoins(ArrayList<String> ajoins) {
        this.ajoins = ajoins;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public HashSet<String> getTrustedUsers() {
        return trustedUsers;
    }

    public void setTrustedUsers(HashSet<String> trustedUsers) {
        this.trustedUsers = trustedUsers;
    }

    public HashSet<String> getGreetingChans() {
        return greetingChans;
    }

    public void setGreetingChans(HashSet<String> greetingChans) {
        this.greetingChans = greetingChans;
    }

    public HashSet<String> getFarewellChans() {
        return farewellChans;
    }

    public void setFarewellChans(HashSet<String> farewellChans) {
        this.farewellChans = farewellChans;
    }

    public HashSet<String> getStoryChans() {
        return storyChans;
    }

    public void setStoryChans(HashSet<String> storyChans) {
        this.storyChans = storyChans;
    }

}
