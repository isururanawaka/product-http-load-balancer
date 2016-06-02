package org.wso2.carbon.gateway.httploadbalancer.mediator;


//import org.apache.http.impl.cookie.BasicClientCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.gateway.core.flow.Mediator;
import org.wso2.carbon.gateway.httploadbalancer.algorithm.LoadBalancerConfigContext;
import org.wso2.carbon.gateway.httploadbalancer.constants.LoadBalancerConstants;
import org.wso2.carbon.gateway.httploadbalancer.utils.CommonUtil;
import org.wso2.carbon.messaging.CarbonCallback;
import org.wso2.carbon.messaging.CarbonMessage;

//import java.util.Date;


/**
 * Callback related to LoadBalancerMediator.
 * In case of cookie persistence, appropriate cookie will be appended with response here.
 */
public class LoadBalancerMediatorCallBack implements CarbonCallback {

    private static final Logger log = LoggerFactory.getLogger(LoadBalancerMediatorCallBack.class);

    //Incoming callback.
    private CarbonCallback parentCallback;

    //LoadBalancer Mediator.
    private Mediator mediator;

    //LoadBalancerConfigContext context.
    private LoadBalancerConfigContext context;

    public LoadBalancerMediatorCallBack(CarbonCallback parentCallback,
                                        Mediator mediator, LoadBalancerConfigContext context) {

        this.parentCallback = parentCallback;
        this.mediator = mediator;
        this.context = context;

    }

    @Override
    public void done(CarbonMessage carbonMessage) {

        /**
         log.info("Inside LB mediator call back...");
         Map<String, String> transHeaders = carbonMessage.getHeaders();
         log.info("Transport Headers...");
         log.info(transHeaders.toString() + "\n\n");

         Map<String, Object> prop = carbonMessage.getProperties();
         log.info("Properties...");
         log.info(prop.toString());
         **/

        if (parentCallback instanceof LoadBalancerMediatorCallBack) {

            if (context.getPersistence().equals(LoadBalancerConstants.APPLICATION_COOKIE)) {

                /**
                 ///////////////////////////////////////////////////////////////////////////////////
                 //TODO: Just for testing. Remove this block later.
                 BasicClientCookie serverCookie = new BasicClientCookie("JSESSIONID", "ghsgsdgsg");

                 Date expiration = new Date(new Date().getTime() + 3600 * 1000);

                 serverCookie.setExpiryDate(expiration);

                 String finalCookie =
                 serverCookie.getName() + "=" + serverCookie.getValue()
                 +
                 "; expires =" + serverCookie.getExpiryDate() +
                 "; HTTPOnly";
                 carbonMessage.setHeader(LoadBalancerConstants.SET_COOKIE_HEADER, finalCookie);

                 ///////////////////////////////////////////////////////////////////////////////////////
                 **/


                /**Checking if there is any cookie already available in response from BE. **/

                if (carbonMessage.getHeader(LoadBalancerConstants.SET_COOKIE_HEADER) != null) { //Cookie exists.

                    //Appending LB_COOKIE along with existing cookie.
                    carbonMessage.setHeader(LoadBalancerConstants.SET_COOKIE_HEADER,
                            CommonUtil.addLBCookieToExistingCookie(
                                    carbonMessage.getHeader(LoadBalancerConstants.SET_COOKIE_HEADER),
                                    CommonUtil.getCookieValue(carbonMessage, context)));

                } else { //There is no cookie in response from BE.

                    //Adding LB specific cookie.
                    log.error("BE endpoint doesn't has it's own cookie. LB will insert it's own cookie for" +
                            "the sake of maintaining persistence. ");

                    carbonMessage.setHeader(LoadBalancerConstants.SET_COOKIE_HEADER,
                            CommonUtil.getSessionCookie(CommonUtil.
                                    getCookieValue(carbonMessage, context), false));

                }

                parentCallback.done(carbonMessage);

            } else if (context.getPersistence().equals(LoadBalancerConstants.LB_COOKIE)) {

                /**
                 ///////////////////////////////////////////////////////////////////////////////////
                 //TODO: Just for testing. Remove this block later.
                 BasicClientCookie serverCookie = new BasicClientCookie("JSESSIONID", "ghsgsdgsg");

                 Date expiration = new Date(new Date().getTime() + 3600 * 1000);

                 serverCookie.setExpiryDate(expiration);

                 String finalCookie =
                 serverCookie.getName() + "=" + serverCookie.getValue();
                 // +
                 //   "; expires =" + serverCookie.getExpiryDate() +
                 //  "; HTTPOnly";
                 carbonMessage.setHeader(LoadBalancerConstants.SET_COOKIE_HEADER, finalCookie);

                 ///////////////////////////////////////////////////////////////////////////////////////
                 **/

                if (carbonMessage.getHeader(LoadBalancerConstants.SET_COOKIE_HEADER) != null) { //Cookie exists.

                    log.error("BE endpoint has it's own cookie, LB will DISCARD this. If" +
                            "you want your application cookie to be used, please choose " +
                            LoadBalancerConstants.APPLICATION_COOKIE +
                            " mode of persistence");
                }

                //Adding LB specific cookie.
                carbonMessage.setHeader(LoadBalancerConstants.SET_COOKIE_HEADER,
                        CommonUtil.getSessionCookie(CommonUtil.getCookieValue(carbonMessage, context), false));

                parentCallback.done(carbonMessage);

            } else { //for NO_PERSISTENCE and IP BASED as of now.

                parentCallback.done(carbonMessage);
            }

        } else if (mediator.hasNext()) { // If Mediator has a sibling after this

            try {
                mediator.next(carbonMessage, parentCallback);
            } catch (Exception e) {
                log.error("Error while mediating from Callback", e);
            }
        }

    }
}