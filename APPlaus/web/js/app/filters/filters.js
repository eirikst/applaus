var filters = angular.module('employeeApp.filters');

/**
 * Truncate Filter
 * @Param text
 * @Param length, default is 10
 * @Param end, default is "..."
 * @return string
 */
filters.filter('truncate', function () {
    return function (text, length) {
        if (isNaN(length))
            length = 10;

        end = "...";

        if (text.length <= length || text.length - end.length <= length) {
            return text;
        }
        else {
            return String(text).substring(0, length-end.length) + end;
        }

    };
});

filters.filter('reverseArr', function() {
  return function(items) {
    if (!angular.isArray(items)) {
        return false;
    }
    return items.slice().reverse();
  };
});

filters.filter('arrWithoutFirst', function() {
  return function(items) {
    if (!angular.isArray(items)) {
        return false;
    }
    return items.slice(1);
  };
});