/*
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
'use strict';

angular.module('upsConsole')
  .controller('RootController', function ($rootScope, Auth, $http) {
    $rootScope.isViewLoading = false;
    //Retrieve the current logged in username
    $rootScope.username = Auth.authz.idToken.preferred_username;

    $rootScope.accountManagement = function() {
      window.location = Auth.authz.authServerUrl + '/realms/aerogear/account?referrer=unified-push-server-js';
    };

    $rootScope.isProcessingData = function() {
      return $http.pendingRequests.some(function(config) {
        if (config.method !== 'GET') {
          console.log(config);
          return true;
        }
      });
    };

    $rootScope.$on('$routeChangeStart', function () {
      $rootScope.isViewLoading = true;
    });
    $rootScope.$on('$routeChangeSuccess', function (event, routeData) {
      $rootScope.isViewLoading = false;
      if (routeData.$$route && routeData.$$route.section) {
        $rootScope.section = routeData.$$route.section;
      }
    });
  }
);
