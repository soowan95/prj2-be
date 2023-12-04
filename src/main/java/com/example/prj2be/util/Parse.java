package com.example.prj2be.util;

public class Parse {
  public static Integer hangulCode(String s) {
    int i = s.charAt(0);
    if (i >= 44032 && i < 44620) return 1; // ㄱ
    else if (i >= 44620 && i < 45208) return 15; // ㄲ
    else if (i >= 45208 && i < 45796) return 2; // ㄴ
    else if (i >= 45796 && i < 46384) return 3; // ㄷ
    else if (i >= 46384 && i < 46972) return 16; // ㄸ
    else if (i >= 46972 && i < 47560) return 4; // ㄹ
    else if (i >= 47560 && i < 48148) return 5; // ㅁ
    else if (i >= 48148 && i < 48736) return 6; // ㅂ
    else if (i >= 48736 && i < 49324) return 17; // ㅃ
    else if (i >= 49324 && i < 49912) return 7; // ㅅ
    else if (i >= 49912 && i < 50500) return 18; // ㅆ
    else if (i >= 50500 && i < 51088) return 8; // ㅇ
    else if (i >= 51088 && i < 51676) return 9; // ㅈ
    else if (i >= 51676 && i < 52264) return 19; // ㅉ
    else if (i >= 52264 && i < 52852) return 10; // ㅊ
    else if (i >= 52852 && i < 53440) return 11; // ㅋ
    else if (i >= 53440 && i < 54028) return 12; // ㅌ
    else if (i >= 54028 && i < 54616) return 13; // ㅍ
    else if (i >= 54616 && i < 55204) return 14; // ㅎ
    return 0;
  }
}
