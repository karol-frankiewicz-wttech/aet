/**
 * Automated Exploratory Tests
 *
 * Copyright (C) 2013 Cognifide Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cognifide.aet.job.common.utils;


import com.cognifide.aet.job.api.exceptions.ParametersException;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class ParamsHelper {

  /***
   * @param key property name that should store regexp
   * @param params map of parameters
   * @return Pattern  or null if there is no key in params map
   * @throws ParametersException if Pattern under key is provided but invalid
   */
  public static Pattern getAsPattern(String key, Map<String, String> params) throws ParametersException {
    Pattern result = null;
    if (params.containsKey(key)) {
      try {
        result = Pattern.compile(params.get(key));
      } catch (PatternSyntaxException e) {
        throw new ParametersException("errorPattern value is invalid regular-expression pattern.", e);
      }
    }
    return result;
  }

  /***
   * @param key property name that should sore textual representation of Integer
   * @param params map of parameters
   * @return integer or null if there is no key in params map
   * @throws ParametersException if value under key is provided but can not be parsed into Integer
   */
  public static Integer getParamAsInteger(String key, Map<String, String> params) throws ParametersException {
    Integer result = null;
    if (params.containsKey(key)) {
      try {
        result = Integer.parseInt(params.get(key));
      } catch (NumberFormatException e) {
        throw new ParametersException(
            "Provided line: " + params.get(result) + " is not a numeric value.", e);
      }
    }
    return result;
  }


  public static String getParamAsString(String key, Map<String, String> params) {
    return params.containsKey(key) ? params.get(key) : null;
  }

  /***
   * @param values array of objects
   * @throws ParametersException if all passed objects are null
   */
  public static void atLeastOneIsProvided(Object... values)
      throws ParametersException {
    if (!ObjectUtils.anyNotNull(values)) {
      throw new ParametersException("At least one parameter must be provided");
    }
  }

  /***
   *
   * @param params  array of objects
   * @throws ParametersException if none or more that one of the parameters are not null
   */
  public static void onlyOneIsProvided(Object... params) throws ParametersException {
    int counter = 0;
    for (Object param : params) {
      if (param != null) {
        counter++;
      }
    }
    if (counter != 1) {
      throw new ParametersException("Exactly one parameter must be set!");
    }
  }

  /***
   *
   * @param pattern
   * @param value
   * @return true if pattern is empty or if its match with given value, false otherwise
   */
  public static boolean matches(Pattern pattern, String value) {
    return pattern == null || pattern.matcher(value).matches();
  }


  public static boolean equalOrNotSet(Object expected, Object actual) {
    return expected == null || expected.equals(actual);
  }


  public static XPathExpression getParamAsXpath(String key, Map<String, String> params) throws ParametersException {
    XPathExpression result = null;
    if (params.containsKey(key)) {
      XPathFactory xPathfactory = XPathFactory.newInstance();
      XPath xpath = xPathfactory.newXPath();
      try {
        result = xpath.compile(params.get(key));
      } catch (XPathExpressionException e) {
        throw new ParametersException(e.getMessage(), e);
      }
    } else {
      throw new ParametersException("Valid XPath must be provided");
    }
    return result;
  }

  /**
   * @param key property name that should store plain text
   * @param params map of parameters
   * @return returns Pattern from quoted plain text under key property or null if there is no value with given key
   */
  public static Pattern getPatternFromPlainText(String key, Map<String, String> params) {
    String plainMessage = ParamsHelper.getParamAsString(key, params);
    return StringUtils.isNotBlank(plainMessage) ? Pattern.compile(Pattern.quote(plainMessage)) : null;
  }

  /***
   * @param primaryKey property name that should stores regexp pattern
   * @param secondaryKey property name that should store plain text
   * @param params map of parameters
   * @return pattern under primaryKey or if retrurns null Pattern from quoted plain text under secondaryKey
   * @throws ParametersException if Pattern under primaryKey is provided but invalid
   */

  public static Pattern getPatternFromPatternParameterOrPlainText(String primaryKey, String secondaryKey,
      Map<String, String> params) throws ParametersException {
    Pattern result = getAsPattern(primaryKey, params);
    return result != null ? result : getPatternFromPlainText(secondaryKey, params);
  }

}
