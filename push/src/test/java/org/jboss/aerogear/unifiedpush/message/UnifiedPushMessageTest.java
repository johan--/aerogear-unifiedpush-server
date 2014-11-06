/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.unifiedpush.message;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class UnifiedPushMessageTest {

    @Test
    public void createBroadcastMessage() {

        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "Howdy");
        messageObject.put("sound", "default");
        messageObject.put("badge", 2);
        messageObject.put("someKey", "someValue");

        container.put("message", messageObject);

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);

        assertEquals("Howdy", unifiedPushMessage.getAlert());
        assertEquals("default", unifiedPushMessage.getSound());
        assertEquals(2, unifiedPushMessage.getBadge());
        assertEquals("someValue", unifiedPushMessage.getData().get("someKey"));

        // no TTL:
        assertEquals(-1, unifiedPushMessage.getTimeToLive());

        // multiple access?
        assertEquals("Howdy", unifiedPushMessage.getAlert());
        assertEquals("someValue", unifiedPushMessage.getData().get("someKey"));

        assertNull(unifiedPushMessage.getSendCriteria().getAliases());
        assertNull(unifiedPushMessage.getSendCriteria().getDeviceTypes());
        assertNull(unifiedPushMessage.getSendCriteria().getCategories());
        assertNull(unifiedPushMessage.getSendCriteria().getVariants());
        assertNull(unifiedPushMessage.getSimplePush());
    }

    @Test
    public void createBroadcastMessageWithSimplePush() {

        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "Howdy");
        messageObject.put("sound", "default");
        messageObject.put("badge", 2);
        messageObject.put("someKey", "someValue");

        container.put("message", messageObject);
        container.put("simple-push", "version=123");

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);

        assertEquals("Howdy", unifiedPushMessage.getAlert());
        assertEquals("default", unifiedPushMessage.getSound());
        assertEquals(2, unifiedPushMessage.getBadge());
        assertEquals("someValue", unifiedPushMessage.getData().get("someKey"));

        // multiple access?
        assertEquals("Howdy", unifiedPushMessage.getAlert());
        assertEquals("someValue", unifiedPushMessage.getData().get("someKey"));

        assertNull(unifiedPushMessage.getSendCriteria().getAliases());
        assertNull(unifiedPushMessage.getSendCriteria().getDeviceTypes());
        assertNull(unifiedPushMessage.getSendCriteria().getCategories());
        assertNull(unifiedPushMessage.getSendCriteria().getVariants());
        assertEquals("version=123", unifiedPushMessage.getSimplePush());
    }

    @Test
    public void createBroadcastMessageWithIncorrectSimplePush() {

        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "Howdy");
        messageObject.put("sound", "default");
        messageObject.put("badge", 2);
        messageObject.put("someKey", "someValue");

        container.put("message", messageObject);
        container.put("simplePush", "version=123");

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);

        assertEquals("Howdy", unifiedPushMessage.getAlert());
        assertEquals("default", unifiedPushMessage.getSound());
        assertEquals(2, unifiedPushMessage.getBadge());
        assertEquals("someValue", unifiedPushMessage.getData().get("someKey"));

        // multiple access?
        assertEquals("Howdy", unifiedPushMessage.getAlert());
        assertEquals("someValue", unifiedPushMessage.getData().get("someKey"));

        assertNull(unifiedPushMessage.getSendCriteria().getAliases());
        assertNull(unifiedPushMessage.getSendCriteria().getDeviceTypes());
        assertNull(unifiedPushMessage.getSendCriteria().getCategories());
        assertNull(unifiedPushMessage.getSendCriteria().getVariants());
        assertNull(unifiedPushMessage.getSimplePush());
    }

    @Test
    public void noBadgePayload() {

        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "Howdy");
        messageObject.put("sound", "default");
        messageObject.put("someKey", "someValue");

        container.put("message", messageObject);

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);

        assertEquals("Howdy", unifiedPushMessage.getAlert());
        assertEquals(-1, unifiedPushMessage.getBadge());
    }

    @Test
    public void testTitle() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "howdy");
        messageObject.put("title", "I'm a Title");

        container.put("message", messageObject);

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);
        assertEquals("I'm a Title", unifiedPushMessage.getTitle());

    }

    @Test
    public void contentAvailable() {

        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "Howdy");
        messageObject.put("sound", "default");
        messageObject.put("someKey", "someValue");
        messageObject.put("content-available", true);

        container.put("message", messageObject);

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);

        assertEquals("Howdy", unifiedPushMessage.getAlert());
        assertEquals(-1, unifiedPushMessage.getBadge());
        assertTrue(unifiedPushMessage.isContentAvailable());
    }

    @Test
    public void noContentAvailable() {

        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "Howdy");
        messageObject.put("sound", "default");
        messageObject.put("someKey", "someValue");

        container.put("message", messageObject);

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);

        assertEquals("Howdy", unifiedPushMessage.getAlert());
        assertEquals(-1, unifiedPushMessage.getBadge());
        assertFalse(unifiedPushMessage.isContentAvailable());
    }

    @Test
    public void testAliasCriteria() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "Howdy");
        messageObject.put("sound", "default");
        messageObject.put("badge", 2);
        messageObject.put("someKey", "someValue");

        container.put("message", messageObject);
        container.put("simplePush", "version=123");

        // criteria:
        container.put("alias", Arrays.asList("foo@bar.org"));

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);
        assertNotNull(unifiedPushMessage.getSendCriteria().getAliases());
        assertEquals(1, unifiedPushMessage.getSendCriteria().getAliases().size());
        assertEquals("foo@bar.org", unifiedPushMessage.getSendCriteria().getAliases().get(0));
    }

    @Test
    public void testAction() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "howdy");
        messageObject.put("action", "View");

        container.put("message", messageObject);

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);
        assertEquals("View", unifiedPushMessage.getAction());

    }

    @Test
    public void testUrlArgs() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "howdy");
        messageObject.put("title", "I'm a Title");
        messageObject.put("url-args",Arrays.asList("Arg1", "Arg2"));

        container.put("message", messageObject);

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);
        assertEquals("[Arg1, Arg2]", unifiedPushMessage.getData().get("url-args").toString());
    }

    @Test
    public void testActionCategory() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "Howdy");
        messageObject.put("action-category", "POSTS");

        container.put("message", messageObject);

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);
        assertEquals("POSTS", unifiedPushMessage.getActionCategory());
    }

    @Test
    public void testMultipleAliasCriteria() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "Howdy");
        messageObject.put("sound", "default");
        messageObject.put("badge", 2);
        messageObject.put("someKey", "someValue");

        container.put("message", messageObject);
        container.put("simplePush", "version=123");

        // criteria:
        container.put("alias", Arrays.asList("foo@bar.org", "bar@foo.com"));

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);
        assertNotNull(unifiedPushMessage.getSendCriteria().getAliases());
        assertEquals(2, unifiedPushMessage.getSendCriteria().getAliases().size());
        assertTrue(unifiedPushMessage.getSendCriteria().getAliases().contains("foo@bar.org"));
        assertTrue(unifiedPushMessage.getSendCriteria().getAliases().contains("bar@foo.com"));
    }

    @Test
    public void testDeviceTypeCriteria() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "Howdy");
        messageObject.put("sound", "default");
        messageObject.put("badge", 2);
        messageObject.put("someKey", "someValue");

        container.put("message", messageObject);
        container.put("simplePush", "version=123");

        // criteria:
        container.put("deviceType", Arrays.asList("iPad"));

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);
        assertNotNull(unifiedPushMessage.getSendCriteria().getDeviceTypes());
        assertEquals(1, unifiedPushMessage.getSendCriteria().getDeviceTypes().size());
        assertEquals("iPad", unifiedPushMessage.getSendCriteria().getDeviceTypes().get(0));
    }

    @Test
    public void testDeviceTypesCriteria() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "Howdy");
        messageObject.put("sound", "default");
        messageObject.put("badge", 2);
        messageObject.put("someKey", "someValue");

        container.put("message", messageObject);
        container.put("simplePush", "version=123");

        // criteria:
        container.put("deviceType", Arrays.asList("iPad", "Android"));

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);
        assertNotNull(unifiedPushMessage.getSendCriteria().getDeviceTypes());
        assertEquals(2, unifiedPushMessage.getSendCriteria().getDeviceTypes().size());
        assertTrue(unifiedPushMessage.getSendCriteria().getDeviceTypes().contains("iPad"));
        assertTrue(unifiedPushMessage.getSendCriteria().getDeviceTypes().contains("Android"));
    }

    @Test
    public void testCategoriesCriteria() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "Howdy");
        messageObject.put("sound", "default");
        messageObject.put("badge", 2);
        messageObject.put("someKey", "someValue");

        container.put("message", messageObject);
        container.put("simplePush", "version=123");

        // criteria:
        container.put("categories", Arrays.asList("football"));

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);
        assertNotNull(unifiedPushMessage.getSendCriteria().getCategories());
        assertEquals(1, unifiedPushMessage.getSendCriteria().getCategories().size());
        assertEquals("football", unifiedPushMessage.getSendCriteria().getCategories().get(0));
    }

    @Test
    public void testMultipleCategoriesCriteria() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "Howdy");
        messageObject.put("sound", "default");
        messageObject.put("badge", 2);
        messageObject.put("someKey", "someValue");

        container.put("message", messageObject);
        container.put("simplePush", "version=123");

        // criteria:
        container.put("categories", Arrays.asList("soccer", "olympics"));

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);
        assertNotNull(unifiedPushMessage.getSendCriteria().getCategories());
        assertEquals(2, unifiedPushMessage.getSendCriteria().getCategories().size());
        assertTrue(unifiedPushMessage.getSendCriteria().getCategories().contains("olympics"));
        assertTrue(unifiedPushMessage.getSendCriteria().getCategories().contains("soccer"));
    }

    @Test
    public void testVariantsCriteria() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "Howdy");
        messageObject.put("sound", "default");
        messageObject.put("badge", 2);
        messageObject.put("someKey", "someValue");

        container.put("message", messageObject);
        container.put("simplePush", "version=123");

        // criteria:
        container.put("variants", Arrays.asList("abc-123-def-456"));

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);
        assertNotNull(unifiedPushMessage.getSendCriteria().getVariants());
        assertEquals(1, unifiedPushMessage.getSendCriteria().getVariants().size());
        assertEquals("abc-123-def-456", unifiedPushMessage.getSendCriteria().getVariants().get(0));
    }

    @Test
    public void testMultipleVariantsCriteria() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "Howdy");
        messageObject.put("sound", "default");
        messageObject.put("badge", 2);
        messageObject.put("someKey", "someValue");

        container.put("message", messageObject);
        container.put("simplePush", "version=123");

        // criteria:
        container.put("variants", Arrays.asList("abc-123-def-456", "456-abc-123-def-bar"));

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);
        assertNotNull(unifiedPushMessage.getSendCriteria().getVariants());
        assertEquals(2, unifiedPushMessage.getSendCriteria().getVariants().size());
        assertTrue(unifiedPushMessage.getSendCriteria().getVariants().contains("abc-123-def-456"));
        assertTrue(unifiedPushMessage.getSendCriteria().getVariants().contains("456-abc-123-def-bar"));
    }

    @Test
    public void testAllCriteria() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "Howdy");
        messageObject.put("sound", "default");
        messageObject.put("badge", 2);
        messageObject.put("someKey", "someValue");

        container.put("message", messageObject);
        container.put("simplePush", "version=123");

        // criteria:
        container.put("variants", Arrays.asList("abc-123-def-456", "456-abc-123-def-bar"));
        container.put("categories", Arrays.asList("soccer", "olympics"));
        container.put("deviceType", Arrays.asList("iPad", "Android"));
        container.put("alias", Arrays.asList("foo@bar.org", "bar@foo.com"));

        // parse it:
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);

        assertEquals(2, unifiedPushMessage.getSendCriteria().getAliases().size());
        assertTrue(unifiedPushMessage.getSendCriteria().getAliases().contains("foo@bar.org"));
        assertFalse(unifiedPushMessage.getSendCriteria().getAliases().contains("mrx@bar.org"));

        assertEquals(2, unifiedPushMessage.getSendCriteria().getDeviceTypes().size());
        assertTrue(unifiedPushMessage.getSendCriteria().getDeviceTypes().contains("Android"));
        assertFalse(unifiedPushMessage.getSendCriteria().getDeviceTypes().contains("iPhone"));

        assertEquals(2, unifiedPushMessage.getSendCriteria().getCategories().size());
        assertTrue(unifiedPushMessage.getSendCriteria().getCategories().contains("olympics"));
        assertFalse(unifiedPushMessage.getSendCriteria().getCategories().contains("Bundesliga"));

        assertEquals(2, unifiedPushMessage.getSendCriteria().getVariants().size());
        assertTrue(unifiedPushMessage.getSendCriteria().getVariants().contains("abc-123-def-456"));
        assertFalse(unifiedPushMessage.getSendCriteria().getVariants().contains("0815")) ;
    }

    @Test(expected = ClassCastException.class)
    public void testVariantCriteriaParseError() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        container.put("variants", "abc-123-def-456");
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);
    }

    @Test(expected = ClassCastException.class)
    public void testCategoriesCriteriaParseError() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        container.put("categories", "soccer");
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);
    }

    @Test(expected = ClassCastException.class)
    public void testDeviceTypeCriteriaParseError() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        container.put("deviceType", "iPad");
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);
    }

    @Test(expected = ClassCastException.class)
    public void testAliasCriteriaParseError() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        container.put("alias", "foo@bar.org");
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);
    }

    @Test(expected = ClassCastException.class)
    public void testMessageObjectParseError() {
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        container.put("message", "payload");
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);
    }

    @Test
    public void testMessageToJson() throws IOException {
        //given
        final Map<String, Object> container = new LinkedHashMap<String, Object>();
        final Map<String, Object> messageObject = new LinkedHashMap<String, Object>();

        messageObject.put("alert", "Howdy");
        messageObject.put("sound", "default");
        messageObject.put("badge", 2);
        messageObject.put("someKey", "someValue");

        container.put("message", messageObject);
        container.put("simplePush", "version=123");
        final UnifiedPushMessage unifiedPushMessage = new UnifiedPushMessage(container);

        //when
        String json = unifiedPushMessage.toJsonString();

        //then
        assertEquals("{" +
                "\"ipAddress\":\"null\"," +
                "\"clientIdentifier\":\"null\"," +
                "\"simplePush\":\"null\"," +
                "\"alert\":\"Howdy\"," +
                "\"title\":\"null\"," +
                "\"action\":\"null\"," +
                "\"action-category\":\"null\"," +
                "\"sound\":\"default\"," +
                "\"contentAvailable\":false," +
                "\"badge\":2," +
                "\"timeToLive\":-1," +
                "\"data\":{" +
                "\"someKey\":\"someValue\"" +
                "}" +
                "}", json);
    }
}
