package pipes;

import botconfigs.IRCBot;
import msg.IRCMsg;

/**
 * Created by JKyte on 3/20/2016.
 */
public class ChannelInfoPipe extends BasePipe {

    private String targetChannel;

    public ChannelInfoPipe(IRCBot ircBot, String targetChannel) {
        super(ircBot);
        this.targetChannel = targetChannel;
    }

    @Override
    public void confirmNegativeResponse() {
        System.out.println("NO!");
        setActionFailure(true);
        outboundMsgQ.add(ircCommands.privmsg(startChan, "Channel " + targetChannel + " is already registered."));
    }

    @Override
    public void confirmPositiveResponse() {
        System.out.println("YES!");
        setActionSuccess(true);
        setPipeComplete(true);
        outboundMsgQ.add(ircCommands.privmsg(startChan, "Channel " + targetChannel + " is not registered."));
    }

    @Override
    public boolean sendAction() {
        outboundMsgQ.add(ircCommands.chanservInfo(targetChannel));
        return super.sendAction();
    }

    @Override
    protected boolean isNegativeResponse(IRCMsg msg) {
        //  We do NOT want to see 'Information for channel #channel:'
        return msg.getTrailing().contains("Information for channel");
    }

    @Override
    protected boolean isPositiveResponse(IRCMsg msg) {
        //  We want to see 'Channel #channel isn't registered.'
        return msg.getTrailing().contains("Channel") && msg.getTrailing().contains("isn't registered.");
    }

}
