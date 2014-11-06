/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.unifiedpush.message;

import java.util.Iterator;
import java.util.Map;

/**
 * Contains the data of the JSON payload that has been sent to the
 * RESTful Sender endpoint.
 * 
 * <p>
 * For details have a look at the <a href="http://aerogear.org/docs/specs/aerogear-push-messages/">Message Format Specification</a>.
 */
public class UnifiedPushMessage {

    private String ipAddress;
    private String clientIdentifier;

    private final SendCriteria criteria;

    private final String simplePush;
    private final String actionCategory;
    private final String alert;
    private final String title;
    private final String action;
    private final String sound;
    private final boolean contentAvailable;
    private final int badge;
    private final int timeToLive;

    private final Map<String, Object> data;

    /**
     * Messages are submitted as flexible JSON maps, like:
     * <pre>
     *   {
     *     "alias" : ["someUsername"],
     *     "deviceType" : ["someDevice"],
     *     "categories" : ["someCategories"],
     *     "variants" : ["someVariantIDs"],
     *     "ttl" : 3600,
     *     "message":
     *     {
     *       "key":"value",
     *       "key2":"other value",
     *       "alert":"HELLO!",
     *       "title":"Title",
     *       "action-category":"some value",
     *       "sound":"default",
     *       "badge":2,
     *       "content-available" : true
     *     },
     *     "url-args":["arg1", "arg2"],
     *     "simple-push":"version=123"
     *   }
     * </pre>
     * This class give some convenient methods to access the query components (<code>alias</code> or <code>deviceType</code>),
     * the <code>simple-push</code> value or some <i>highlighted</i> keywords.
     */
    @SuppressWarnings("unchecked")
    public UnifiedPushMessage(Map<String, Object> data) {
        // extract all the different criteria
        this.criteria = new SendCriteria(data);

        // ======= Payload ====
        // the Android/iOS payload of the actual message:
        this.data = (Map<String, Object>) data.remove("message");
        // if 'native' message object is around, let's extract some data:
        if (this.data != null) {
            // remove the desired keywords:
            // special key words (for APNs)
            this.alert = (String) this.data.remove("alert"); // used in AGDROID as well
            this.title = (String) this.data.remove("title");
            this.action = (String) this.data.remove("action");
            this.sound = (String) this.data.remove("sound");
            this.actionCategory = (String) this.data.remove("action-category");

            Boolean contentValue = (Boolean) this.data.remove("content-available");
            if (contentValue == null) {
                this.contentAvailable = false;
            }
            else {
                this.contentAvailable = contentValue.booleanValue();
            }

            Integer badgeVal = (Integer) this.data.remove("badge");
            if (badgeVal == null) {
                this.badge = -1;
            } else {
                this.badge = badgeVal;
            }
        } else {
            // satisfy the final
            this.alert = null;
            this.title = null;
            this.action = null;
            this.sound = null;
            this.actionCategory = null;
            this.badge = -1;
            this.contentAvailable = false;
        }

        // time to live value:
        Integer timeToLiveValue = (Integer) data.remove("ttl");
        if (timeToLiveValue == null) {
            this.timeToLive = -1;
        } else {
            this.timeToLive = timeToLiveValue;
        }

        // SimplePush values:
        this.simplePush = (String) data.remove("simple-push");

    }

    /**
     * Returns the object that contains all the submitted query criteria.
     */
    public SendCriteria getSendCriteria() {
        return criteria;
    }

    /**
     * Returns the SimplePush specific version number.
     */
    public String getSimplePush() {
        return simplePush;
    }

    /**
     * Returns the value of the 'alert' key from the submitted payload.
     * This key is recognized in native iOS, without any API invocation and
     * on AeroGear's GCM offerings.
     *
     * Android users that are not using AGDROID can read the value as well,
     * but need to call specific APIs to show the 'alert' value.
     */
    public String getAlert() {
        return alert;
    }

    /**
     * Returns the value of the 'title' key from the submitted payload.
     * This key is recognized in Safari, without any API invocation and
     * on AeroGear's GCM offerings.
     *
     * Android users that are not using AGDROID can read the value as well,
     * but need to call specific APIs to show the 'alert' value.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the value of the 'action' key from the submitted payload.
     * This key is recognized in Safari, without any API invocation and
     * on AeroGear's GCM offerings.
     *
     * Android users that are not using AGDROID can read the value as well,
     * but need to call specific APIs to show the 'alert' value.
     */
    public String getAction() {
        return action;
    }

    /**
     * Returns the value of the 'action-category', which is used on the client (iOS for now),
     * to invoke a certain "user action" on the device, based on the push message. Implemented for iOS8
     */
    public String getActionCategory() {
        return actionCategory;
    }

    /**
     * Returns the value of the 'ttl' key from the submitted payload.
     * This key is recognized for the Android and iOS Push Notification Service.
     *
     * If the 'ttl' key has not been specified on the submitted payload, this method will return -1.
     */
    public int getTimeToLive() {
        return timeToLive;
    }

    /**
     * Returns the value of the 'sound' key from the submitted payload.
     * This key is recognized in native iOS, without any API invocation.
     *
     * Android users can read the value as well, but need to call specific
     * APIs to play the referenced 'sound' file.
     */
    public String getSound() {
        return sound;
    }

    /**
     * Returns the value of the 'badge' key from the submitted payload.
     * This key is recognized in native iOS, without any API invocation.
     *
     * Android users can read the value as well, but need to call specific
     * APIs to show the 'badge number'.
     */
    public int getBadge() {
        return badge;
    }

    /**
     * Used for in iOS specific feature, to indicate if content (for Newsstand or silent messages) has marked as
     * being available
     *
     * Not supported on other platforms.
     */
    public boolean isContentAvailable() {
        return contentAvailable;
    }

    /**
     * Returns a Map, representing any other key-value pairs that were send
     * to the RESTful Sender API.
     *
     * This map usually contains application specific data, like:
     * <pre>
     *  "sport-news-channel15" : "San Francisco 49er won last game"
     * </pre>
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * The IP address from the agent that did issue the push message request.
     */
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * The Client Identifier showing who triggered the Push Notification
     */
    public String getClientIdentifier() { return clientIdentifier; }

    public void setClientIdentifier(String clientIdentifier) { this.clientIdentifier = clientIdentifier; }

    public String toJsonString() {
        return "{" +
                "\"ipAddress\":\"" + ipAddress + "\"," +
                "\"clientIdentifier\":\"" + clientIdentifier + "\"," +
                "\"simplePush\":\"" + simplePush + "\"," +
                "\"alert\":\"" + alert + "\"," +
                "\"title\":\"" + title + "\"," +
                "\"action\":\"" + action + "\"," +
                "\"action-category\":\"" + actionCategory + "\"," +
                "\"sound\":\"" + sound + "\"," +
                "\"contentAvailable\":" + contentAvailable + "," +
                "\"badge\":" + badge + "," +
                "\"timeToLive\":" + timeToLive + "," +
                "\"data\":" + toJson(data) +
                "}";
    }

    private String toJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (map != null) {
            for (Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, Object> entry = iterator.next();
                sb.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
                if (iterator.hasNext()) {
                    sb.append(",");
                }
            }
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "[alert=" + alert + ", title=" + title + ", data=" + data+ ", criteria="
                + criteria + ", sound=" + sound + ", action=" + action + ", action-category=" + actionCategory + ", badge=" + badge + ", time-to-live="
                + timeToLive + ", simplePush=" + simplePush + ", content-available=" + contentAvailable +"]";
    }
}
