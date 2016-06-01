package org.wso2.carbon.gateway.httploadbalancer.constants;

/**
 * Constants for Load Balancer.
 */
public class LoadBalancerConstants {

    /**
     * Config Keys.
     */
    public static final String ALGORITHM_NAME = "algorithmName";
    public static final String PERSISTENCE_TYPE = "persistenceType";

    // As of now, this timeout value will be used only with "LB_COOKIE" based persistence.
    public static final String PERSISTENCE_SESSION_TIME_OUT = "sessionTimeOut";

    public static final String SSL_TYPE = "sslType";

    // As of now, only passive is supported.
    public static final String HEALTH_CHECK_TYPE = "healthCheckType";

    // Amount of time for which LB has to wait for response from OutboundEndpoint.
    public static final String HEALTH_CHECK_REQUEST_TIMEOUT = "requestTimeout";

    // no of request failure or timeouts to mark an OutboundEndpoint as unhealthy.
    public static final String HEALTH_CHECK_UNHEALTHY_RETRIES = "unHealthyRetries";

    // no of successful responses to be received from an OutboundEndpoint to mark it as healthy again.
    public static final String HEALTH_CHECK_HEALTHY_RETRIES = "healthyRetries";

    // Scheduled time interval after which LB has to check if an Outboundendpoint is healthy again.
    public static final String HEALTH_CHECK_HEALTHY_CHECK_INTERVAL = "healthyCheckInterval";


    /**
     * LB Algorithm related Constants.
     */

    public static final String ROUND_ROBIN = "ROUND_ROBIN";
    public static final String LEAST_CONNECTIONS = "LEAST_CONNECTIONS";
    public static final String LEAST_RESPONSE_TIME = "LEAST_RESPONSE_TIME";


    /**
     * Session Persistence related Constants.
     */
    public static final String NO_PERSISTENCE = "NO_PERSISTENCE";

    //Session Timeout is handled by application cookie.
    public static final String APPLICATION_COOKIE = "APPLICATION_COOKIE";

    //Session Timeout is handled by LB_COOKIE.
    // Use this mode ONLY if application doesn't use its own cookies.
    public static final String LB_COOKIE = "LB_COOKIE";

    public static final String CLIENT_IP_ADDRESS = "CLIENT_IP_ADDRESS";

    //This will be used in cookie to OutboundEndpoint maps.
    // TODO:If there is any security concern, ObjectID or any other meaningless
    // TODO:string can be stored instead of EP1,EP2 etc.
    public static final String COOKIE_PREFIX = "EP";

    //This will be used as cookie name.
    public static final String LB_COOKIE_NAME = "LB_COOKIE";

    //This will be used to write cookie in response header.
    public static final String SET_COOKIE_HEADER = "Set-Cookie";

    //This will be used to read cookie if any from request header.
    public static final String COOKIE_HEADER = "Cookie";

    //This will be used as delimiter between Existing cookies & LB_COOKIE.
    public static final String LB_COOKIE_DELIMITER = "---";

    // eg: LB_COOKIE:EP1.
    public static final String COOKIE_NAME_VALUE_SEPARATOR = ":";

    //Cookie HttpOnly.
    public static final String HTTP_ONLY = "HttpOnly";

    //Cookie Secure.
    public static final String SECURE = "secure";




    /**
     * SSL Support related Constants.
     */
    public static final String NO_SSL = "NO_SSL";
    public static final String SSL_OFFLOAD = "SSL_OFFLOAD";
    public static final String END_TO_END = "END_TO_END";

    /**
     * Health Checking related Constants.
     */
    public static final String PASSIVE_HEALTH_CHECK = "PASSIVE";

    /**
     * TODO: Check these values with mentor. Should there be specific values based on type..?
     */
    public static final int MAX_TIMEOUT_VAL = 18000000; //5 hours.

    public static final int DEFAULT_TIMEOUT = 120000; //2 mins

    public static final int DEFAULT_RETRIES = 3;
}
