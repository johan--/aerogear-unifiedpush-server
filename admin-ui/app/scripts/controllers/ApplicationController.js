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

angular.module('upsConsole').controller('ApplicationController',
  function($rootScope, $scope, $modal, pushApplication, Notifications, applications) {

  /*
   * INITIALIZATION
   */

  $scope.alerts = [];
  $scope.currentPage = 1;

  //let's show all the applications
  $rootScope.application = null;

  /*
   * PUBLIC METHODS
   */

  $scope.open = function (application) {
    var modalInstance = show(application, 'create-app.html');
    modalInstance.result.then(function (application) {
      pushApplication.create(application, function (newApp) {
        if ($scope.applications.length < 8) {
          $scope.applications.push(newApp);
        }
        $scope.totalItems = $scope.totalItems + 1;
        Notifications.success('Successfully created application "' + newApp.name + '".');
      }, function () {
        Notifications.error('Something went wrong...', 'danger');
      });
    });
  };

  $scope.edit = function (application) {
    var modalInstance = show(application, 'create-app.html');
    modalInstance.result.then(function (application) {
      var updateApplication = {
        name: application.name,
        description: application.description
      };
      pushApplication.update({appId: application.pushApplicationID}, updateApplication, function () {
        Notifications.success('Successfully edited application "' + application.name + '".');
      });
    });
  };

  $scope.remove = function (application) {
    var modalInstance = show(application, 'remove-app.html');
    modalInstance.result.then(function () {
      pushApplication.remove({appId: application.pushApplicationID}, function () {
        $scope.applications.splice($scope.applications.indexOf(application), 1);
        $scope.totalItems = $scope.totalItems - 1;
        Notifications.success('Successfully removed application "' + application.name + '".');
      });
    });
  };

  $scope.pageChanged = function () {
    $rootScope.isViewLoading = true;
    fetch($scope.currentPage).then(function() {
      $rootScope.isViewLoading = false;
    });
  };

  /*
   * PRIVATE METHODS
   */

  function fetch(pageNo) {
    return pushApplication.fetch(pageNo).then(update);
  }

  function modalController($scope, $modalInstance, application) {
    $scope.application = application;
    $scope.ok = function (application) {
      $modalInstance.close(application);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  }

  function show(application, template) {
    return $modal.open({
      templateUrl: 'views/dialogs/' + template,
      controller: modalController,
      resolve: {
        application: function () {
          return application;
        }
      }
    });
  }

  function update(data) {
    $scope.applications = data.page;
    $scope.totalItems = parseInt(data.total, 10);
  }

  update(applications);
});
