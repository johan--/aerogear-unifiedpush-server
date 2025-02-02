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
package org.jboss.aerogear.unifiedpush.service;

import org.jboss.aerogear.unifiedpush.api.PushApplication;
import org.jboss.aerogear.unifiedpush.dao.PageResult;
import org.jboss.aerogear.unifiedpush.service.dashboard.Application;
import org.jboss.aerogear.unifiedpush.service.dashboard.ApplicationVariant;
import org.jboss.aerogear.unifiedpush.service.dashboard.DashboardData;

import java.util.List;

/**
 * Base of the implementation for the admin/developer view
 */
public interface PushSearchService {

    /**
     * Finder that returns all pushApplication object for the given owner/developer.
     */
    PageResult<PushApplication> findAllPushApplicationsForDeveloper(Integer page, Integer pageSize);

    /**
     * Finder that returns an actual PushApplication, identified by its ID and its owner/developer.
     */
    PushApplication findByPushApplicationIDForDeveloper(String pushApplicationID);

    /**
     * See that variant exists for loggedin developer
     */
    boolean existsVariantIDForDeveloper(String variantID);

    /**
     * Receives the dashboard data for the given user
     */
    DashboardData loadDashboardData();

    /**
     * Loads all the Variant objects where we did notice some failures on sending
     * for the given user
     */
    List<ApplicationVariant> getVariantsWithWarnings();

    /**
     * Loads all the Variant objects with the most received messages
     */
    List<Application> getTopThreeLastActivity();


}
