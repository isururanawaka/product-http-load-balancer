package org.wso2.carbon.gateway.httploadbalancer.outbound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.gateway.httploadbalancer.context.LoadBalancerConfigContext;

import org.wso2.carbon.messaging.CarbonCallback;
import org.wso2.carbon.messaging.CarbonMessage;


/**
 * An instance of this class has a reference to an LBOutboundEndpoint.
 * <p>
 * We need few weight related attributes for Weighted algorithms.
 * <p>
 * NOTE: Inside LB all Weighted Algorithms must use this Only.
 */
public class WeightedLBOutboundEndpoint {

    private static final Logger log = LoggerFactory.getLogger(WeightedLBOutboundEndpoint.class);

    private LBOutboundEndpoint lbOutboundEndpoint;

    private int maxWeight = 1; // Set by user in configuration. By default it is 1.
    private int currentWeight = 0; // To keep track of requests forwarded in currentWeightsWindow.


    public WeightedLBOutboundEndpoint(LBOutboundEndpoint lbOutboundEndpoint, int weight) {
        this.lbOutboundEndpoint = lbOutboundEndpoint;
        this.maxWeight = weight;

        if (log.isDebugEnabled()) {
            log.debug("OutboundEndpoint : " + this.lbOutboundEndpoint.getName()
                    + " Weight : " + this.maxWeight);
        }
    }

    public String getName() {

        return this.lbOutboundEndpoint.getName();
    }

    public LBOutboundEndpoint getLbOutboundEndpoint() {
        return this.lbOutboundEndpoint;
    }

    private void incrementCurrentWeight() {
        this.currentWeight++;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }

    /**
     * @param carbonMessage
     * @param carbonCallback
     * @param context
     * @return
     * @throws Exception NOTE: When this algorithm mode is chosen, all requests are sent through this method only.
     *                   So currentWeight will be incremented in both the cases.
     *                   (i.e.) In Endpoint chosen by persistence and in endpoint chosen by algorithm.
     */
    public boolean receive(CarbonMessage carbonMessage, CarbonCallback carbonCallback,
                           LoadBalancerConfigContext context) throws Exception {


        synchronized (getLock()) {
            this.incrementCurrentWeight(); //  Increments currentRequests for this WeightedLBOutboundEndpoint

        }
        this.lbOutboundEndpoint.receive(carbonMessage, carbonCallback, context);
        return false;
    }

    public void resetWeight() {
        this.currentWeight = 0;
    }

    public Object getLock() {
        return this.lbOutboundEndpoint.getLock();
    }
}
