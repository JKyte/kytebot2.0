package pipes;

import botconfigs.IRCBot;
import botconfigs.IRCCommands;
import msg.IRCMsg;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by JKyte on 3/18/2016.
 */
public abstract class BasePipe implements Pipe {

    private final long MAX_DURATION_IN_MILLIS = 5 * 60 * 1000;   //  5 minutes = 5 minutes * 60 seconds * 1000 millis
    protected IRCCommands ircCommands;
    protected ConcurrentLinkedQueue<String> outboundMsgQ;
    protected String startChan;
    private boolean isActivePipe;
    private boolean actionSent;
    private boolean actionSuccess;
    private boolean actionFailure;
    private boolean pipeComplete;
    private boolean isLastPipe;
    private long start;

    public BasePipe(IRCBot ircBot) {
        this.ircCommands = ircBot.getIrcCommands();
        this.outboundMsgQ = ircBot.getOutboundMsgQ();
        this.startChan = ircBot.getConfigs().getStartChan();
    }

    /**
     * Executing a pipe involves two stages, sending an action then checking messages for a response.
     *
     * @param msg
     * @return
     */
    @Override
    public boolean executePipe(IRCMsg msg) {

        if (!isActionSent()) {
            sendAction();
        } else {
            //  Check for A) positive response or B) negative responses
            if (isPositiveResponse(msg)) {

                confirmPositiveResponse();

            } else if (isNegativeResponse(msg)) {

                confirmNegativeResponse();
            } else {

                //  Else check for a timeout
                checkClock();
            }
        }

        return isActivePipe;
    }

    protected abstract void confirmNegativeResponse();

    protected abstract void confirmPositiveResponse();

    public boolean sendAction() {
        startClock();
        setActionSent(true);
        return true;
    }

    protected abstract boolean isNegativeResponse(IRCMsg msg);

    protected abstract boolean isPositiveResponse(IRCMsg msg);

    //  Helper methods for timing.
    private void startClock() {
        start = System.currentTimeMillis();
    }

    private boolean checkClock() {
        long currentDuration = System.currentTimeMillis() - start;
        return currentDuration <= MAX_DURATION_IN_MILLIS;
    }

    public boolean isActivePipe() {
        return isActivePipe;
    }

    public void setIsActivePipe(boolean isActivePipe) {
        this.isActivePipe = isActivePipe;
    }

    public boolean isActionSent() {
        return actionSent;
    }

    public void setActionSent(boolean actionSent) {
        this.actionSent = actionSent;
    }

    public boolean isActionSuccess() {
        return actionSuccess;
    }

    public void setActionSuccess(boolean actionSuccess) {
        this.actionSuccess = actionSuccess;
    }

    public boolean isActionFailure() {
        return actionFailure;
    }

    public void setActionFailure(boolean actionFailure) {
        this.actionFailure = actionFailure;
    }

    public boolean isPipeComplete() {
        return pipeComplete;
    }

    public void setPipeComplete(boolean pipeComplete) {
        this.pipeComplete = pipeComplete;
    }

    public boolean isLastPipe() {
        return isLastPipe;
    }

    public void setIsLastPipe(boolean isLastPipe) {
        this.isLastPipe = isLastPipe;
    }
}
