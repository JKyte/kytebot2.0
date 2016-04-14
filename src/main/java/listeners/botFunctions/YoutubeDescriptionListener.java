package listeners.botFunctions;

import botconfigs.IRCBot;
import io.YoutubeTitleJsonParser;
import listeners.BaseListener;
import msg.IRCMsg;

import java.util.ArrayList;

/**
 * Created by JKyte on 4/13/2016.
 */
public class YoutubeDescriptionListener extends BaseListener {

    private String target;

    private String USER_URL;
    private String VID_URL = "VID_URL";
    private String PATTERN = "https://www.youtube.com/watch?v=";
    private String REQUEST_URL = "https://www.youtube.com/oembed?url=VID_URL&format=json";

    public YoutubeDescriptionListener(IRCBot ircbot) {
        super(ircbot);
    }

    @Override
    public boolean listen(IRCMsg msg) {

        if (null == msg.getTrailing()) {
            return false;   //  Always fail fast
        }

        //  If MSG has a youtube link...
        if (msg.getTrailing().contains(PATTERN)) {

            setTarget(msg);

            //  Find the link in the msg
            for (String chunk : msg.getTrailing().split(" ")) {

                if (chunk.contains(PATTERN)) {
                    log.info("Chunk: " + chunk);
                    USER_URL = chunk;
                    break;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private void setTarget(IRCMsg msg) {
        target = msg.getArgs()[0];
        if (target.equals(botnick)) {
            target = msg.getFromNick();
        }
    }

    @Override
    public void doAction() {

        //  Setup REQUEST_URL
        String fullURL = REQUEST_URL.replace(VID_URL, USER_URL);
        log.info("Request URL: " + fullURL);

        //  Call to the youtubes
        YoutubeTitleJsonParser parser = new YoutubeTitleJsonParser(null);
        parser.fetchJson(fullURL);

        ArrayList<String> lines = parser.buildResponse();
        for (String line : lines) {
            outboundMsgQ.add(ircCommands.privmsg(target, line));
        }

        target = null;
        USER_URL = null;
    }

    @Override
    public boolean listenerFinished() {
        return false;
    }
}
