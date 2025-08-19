package su.trident.cenchnats.util.numbers;

public class NumberUtil
{
    public static String toRoman(int num)
    {
        if (num < 1 || num > 3999) {
            throw new IllegalArgumentException("Число должно быть от 1 до 3999");
        }

        if (num == 1) {
            return ""; // для чар 1 уровня вернет пустую строку
        }

        final int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        final String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        final StringBuilder roman = new StringBuilder();

        for (int i = 0; i < values.length && num > 0; i++) {
            int count = num / values[i];
            if (count > 0) {
                roman.append(symbols[i].repeat(count));
                num %= values[i];
            }
        }

        return roman.toString();
    }
}
