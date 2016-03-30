package pipelines;

import msg.IRCMsg;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by JKyte on 3/28/2016. This class acts as a wrapper around a HashMap of Pipeline
 */
public class Pipelines {

    private ConcurrentHashMap<String, Pipeline> pipelines;

    public Pipelines() {
        pipelines = new ConcurrentHashMap<>();
    }

    public synchronized boolean put(String pipelineName, Pipeline pipeline) {
        if (!pipelines.containsKey(pipelineName)) {
            pipelines.put(pipelineName, pipeline);
            return true;
        } else {
            return false;
        }
    }

    public Pipeline get(String pipelineName) {
        return pipelines.get(pipelineName);
    }

    public boolean contains(String pipelineName) {
        return pipelines.containsKey(pipelineName);
    }

    public void executePipelines(IRCMsg msg) {
        for (Entry<String, Pipeline> entry : pipelines.entrySet()) {

            entry.getValue().executePipeline(msg);

            if ((entry.getValue().isPipelineFailed() && entry.getValue().isPipelineCompleted()) ||
                    (entry.getValue().isPipelineCompleted())) {
                pipelines.remove(entry.getKey());
            }
        }
    }
}
