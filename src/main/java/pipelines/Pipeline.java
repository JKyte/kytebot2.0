package pipelines;

import msg.IRCMsg;
import pipes.BasePipe;

import java.util.ArrayList;

/**
 * Created by JKyte on 3/18/2016.
 */
public class Pipeline {

    ArrayList<BasePipe> pipes;
    private boolean pipelineFailed;
    private boolean pipelineCompleted;


    public Pipeline() {
        pipes = new ArrayList<>();
    }

    public void executePipeline(IRCMsg msg) {
        // do stuff
        BasePipe pipe;
        for (int ii = 0; ii < pipes.size(); ii++) {
            pipe = pipes.get(ii);

            if (!pipe.isActivePipe()) {
                // Fail fast
                continue;
            }

            pipe.executePipe(msg);

            //  Check if current pipe is finished
            if (pipe.isPipeComplete() && pipe.isActionSuccess()) {
                //  Move on in the pipeline
                if (!pipe.isLastPipe()) {
                    pipe.setIsActivePipe(false);
                    pipes.get(ii + 1).setIsActivePipe(true);
                }

            } else if (pipe.isPipeComplete() && pipe.isActionFailure()) {
                //  Fail the Pipeline and flag for removal
            }

            //  Check to see if pipeline is complete
            if (pipe.isLastPipe() && pipe.isPipeComplete()) {

                if (pipe.isActionSuccess()) {
                    setPipelineCompleted(true);
                } else {
                    setPipelineCompleted(true);
                    setPipelineFailed(true);
                }
            }

        }
    }

    public boolean isPipelineFailed() {
        return pipelineFailed;
    }

    public void setPipelineFailed(boolean pipelineFailed) {
        this.pipelineFailed = pipelineFailed;
    }

    public boolean isPipelineCompleted() {
        return pipelineCompleted;
    }

    public void setPipelineCompleted(boolean pipelineCompleted) {
        this.pipelineCompleted = pipelineCompleted;
    }
}
