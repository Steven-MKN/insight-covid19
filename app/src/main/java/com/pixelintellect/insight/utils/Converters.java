package com.pixelintellect.insight.utils;

import java.text.DecimalFormat;

public class Converters {
  /**
   * Converts a String with an integer value to a formatted String with 'K' for thousands and 'M' for millions
   * @param s a String with an integer value
   * @return String
   */
  public static String tallyToFormatString(String s){
    // decimal format, to 1 decimal place
    DecimalFormat df = new DecimalFormat("#.#");

    // try to cast and format the String, else return the input string as is.
    try {
      final int count = Integer.parseInt(s);
      final int ONE_MILLION = 1_000_000;
      final int ONE_THOUSAND = 1_000;
      String toReturn;

      if (count >= ONE_MILLION) {
        toReturn = df.format((double) count / (double) ONE_MILLION);
        toReturn += "M";
      } else if (count >= ONE_THOUSAND){
        toReturn = df.format((double) count / (double) ONE_THOUSAND);
        toReturn += "K";
      } else {
        toReturn = String.valueOf(count);
      }

      return toReturn;

    } catch (Exception e){
      return s;
    }

  }
}
