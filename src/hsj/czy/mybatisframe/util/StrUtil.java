package hsj.czy.mybatisframe.util;

public class StrUtil {

    /**
     * 获取随机字符串
     * 
     * @param size 长度
     * @param type number:数字 ， english：纯英文，other：混合
     * @return
     */
    public static String RadomCode(int size, String type) {
        String[] code = null;
        if (type != null && type == "number") {
            code = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
        } else if (type != null && type == "english") {
            code = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "P", "Q", "R",
                    "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
                    "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
        } else {
            code = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "A", "B", "C", "D", "E", "F", "G",
                    "H", "I", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b",
                    "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w",
                    "x", "y", "z" };
        }
        Integer length = code.length;
        int random = 0;
        StringBuffer randomStr = new StringBuffer();
        if (size <= 0) size = 6;
        if (size > 32) size = 32;
        for (int i = 0; i < size; i++) {
            random = (int) (Math.random() * length);
            randomStr.append(code[random]);
        }
        return randomStr.toString();
    }
}
