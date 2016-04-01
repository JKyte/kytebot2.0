package pipelines;

import core.GenericHashMap;
import msg.IRCMsg;

import java.util.Map.Entry;

/**
 * Created by JKyte on 3/28/2016.
 */
public class Pipelines extends GenericHashMap {

    @Override
    public void iterateAcrossObjects(IRCMsg msg) {
        for (Entry<String, Object> object : hashMap.entrySet()) {
            Pipeline entry = (Pipeline) object.getValue();

            entry.executePipeline(msg);

            if ((entry.isPipelineFailed() && entry.isPipelineCompleted()) ||
                    (entry.isPipelineCompleted())) {
                hashMap.remove(object.getKey());
            }
        }
    }
}
