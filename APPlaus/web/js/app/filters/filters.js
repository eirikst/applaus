var filters = angular.module('employeeApp.filters');

/**
 * Filtering strings that should not be longer than length characters. Adds
 *  ... in the end if more than length characters.
 * @Param text string in question 
 * @Param length, default 10
 * @return filtered string
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

/**
 * Reversing array
 * @param array an array
 * @return copy of the input array reversed
 */
filters.filter('reverseArr', function() {
  return function(array) {
    if (!angular.isArray(array)) {
        return false;
    }
    return array.slice().reverse();
  };
});

/**
 * Array without first element
 * @param array an array
 * @return new array copied from the old without its first element
 */
filters.filter('arrWithoutFirst', function() {
  return function(array) {
    if (!angular.isArray(array)) {
        return false;
    }
    return array.slice(1);
  };
});