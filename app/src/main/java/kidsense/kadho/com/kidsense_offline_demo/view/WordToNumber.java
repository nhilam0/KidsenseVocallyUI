package kidsense.kadho.com.kidsense_offline_demo.view;

import java.util.HashMap;
import java.util.Map;

public class WordToNumber {
    public static final String[] DIGITS = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    public static final String[] TENS = {"twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
    public static final String[] TEENS = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
    public static final String[] MAGNITUDES = {"hundred", "thousand", "million", "billion"};
    public static final String[] ZERO = {"zero", "oh"};

    Map<String, type_pair> mappings;

    enum TYPES {digit, ten, teen, magnitude, zero};

    class type_pair {
        public int value;
        public TYPES type;
        type_pair(int value, TYPES type) {
            this.value = value;
            this.type = type;
        }
    }

    public WordToNumber() {
        this.mappings = new HashMap<>();
        fillMap();
    }

    private void fillMap() {
        for(int i = 0; i < DIGITS.length; ++i) {
            mappings.put(DIGITS[i], new type_pair(i+1, TYPES.digit));
        }

        for(int i = 0; i < TENS.length; ++i) {
            mappings.put(TENS[i], new type_pair(10*(i+2), TYPES.ten));
        }

        int teens = 10;
        for(int i = 0; i < TEENS.length; ++i) {
            mappings.put(TEENS[i], new type_pair(teens, TYPES.teen));
            teens++;
        }

        mappings.put("zero", new type_pair(0, TYPES.zero));
        mappings.put("oh", new type_pair(0, TYPES.zero));

        mappings.put(MAGNITUDES[0], new type_pair(100, TYPES.magnitude));
        mappings.put(MAGNITUDES[1], new type_pair(1000, TYPES.magnitude));
        mappings.put(MAGNITUDES[2], new type_pair(1000000, TYPES.magnitude));
        mappings.put(MAGNITUDES[3], new type_pair(1000000000, TYPES.magnitude));


    }

    //replaces each number substring in the input with its corresponding
    //integer representation of the number
    public String replaceNums(String input) {
        String[] splits = input.split(" ");

        for(int i = 0; i < splits.length; ++i) {
            if(mappings.containsKey(splits[i].toLowerCase())) {
                String sub = "";
                for(;i < splits.length; ++i) {
                    if(mappings.containsKey(splits[i].toLowerCase())) {
                        sub += splits[i] + " ";
                    }else {
                        break;
                    }
                }
                input = input.replace(sub.trim(), convertToNumber(sub));
            }
        }

        return input;
    }

    //Converts number strings to integer strings
    //ex: eight zero five = 805; nine hundred two = 902 etc.
    public String convertToNumber(String input) {
        String[] split = input.split(" ");

        long num = 0;

        type_pair prev = null;
        for(String s : split) {
            s = s.toLowerCase();

            if(mappings.containsKey(s)) {
                type_pair pair = mappings.get(s);

                if(pair.type == TYPES.zero) {
                    String temp = (Long.toString(num) + "0");
                    num = Long.parseLong(temp);
                    prev = pair;

                }else if(pair.type == TYPES.magnitude) {

                    num *= pair.value;
                    prev = pair;

                }else {
                    if(prev == null) {
                        prev = pair;
                        num += pair.value;
                    }else {
                        if(prev.type != TYPES.magnitude && prev.type != TYPES.ten) {
                            String temp = (Long.toString(num) + Integer.toString(pair.value));

                            num = Long.parseLong(temp);

                            prev = pair;
                        }else {
                            num += pair.value;
                            prev = pair;
                        }
                    }
                }
            }
        }

        return Long.toString(num);
    }
}