package com.pixelintellect.insight.utils;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;

public class Converters {
  /**
   * Converts an integer String to a formatted String with 'K' for thousands and 'M' for millions
   * @param s a String with an integer value
   * @return String
   */
  public static String tallyToFormatString(String s){
    DecimalFormat df = new DecimalFormat("#.#");

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
