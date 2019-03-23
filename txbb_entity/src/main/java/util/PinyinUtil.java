package util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Arrays;

public class PinyinUtil {

    public static String ToPinyin(String text) {

        if (text == null || text.trim() == "") {
            return null;
        }

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        //不要声调
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        char[] chars = text.toCharArray();

        StringBuilder pinyin = new StringBuilder();

        for (char c : chars) {
            try {
                String[] strings = PinyinHelper.toHanyuPinyinStringArray(c, format);
                if (strings != null && strings.length > 0) {
                    pinyin.append(strings[0]);
                }else {
                    pinyin.append(c);
                }

            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
        }

        return pinyin.toString();
    }


    public static void main(String[] args) {
        System.out.println(ToPinyin("*（……（……好"));
    }
}
