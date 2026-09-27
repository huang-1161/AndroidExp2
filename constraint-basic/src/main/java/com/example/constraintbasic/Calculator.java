package com.example.constraintbasic;

/**
 * 极简四则运算求值工具。
 * 支持 + − × ÷ 与小数，按从左到右的顺序计算（不处理括号与优先级），
 * 界面上的按键序列正好符合这种顺序输入习惯。
 */
final class Calculator {

    private Calculator() {
    }

    /** 对形如 "12.5+3×2" 的表达式求值，出错时返回 "Error"。 */
    static String evaluate(String raw) {
        if (raw == null || raw.isEmpty()) {
            return "0.0";
        }
        // 输入框右侧若是运算符，先去掉
        String expr = raw.replace(" ", "");
        while (expr.length() > 0 && isOperator(expr.charAt(expr.length() - 1))) {
            expr = expr.substring(0, expr.length() - 1);
        }
        if (expr.isEmpty()) {
            return "0.0";
        }

        double result = 0;
        double current = 0;
        boolean hasValue = false;
        char pendingOp = '+';

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (isOperator(c)) {
                result = apply(result, current, pendingOp, hasValue);
                hasValue = true;
                pendingOp = c;
                current = 0;
            } else if (c >= '0' && c <= '9') {
                current = current * 10 + (c - '0');
            } else if (c == '.') {
                // 解析小数部分
                double scale = 0.1;
                i++;
                while (i < expr.length() && expr.charAt(i) >= '0' && expr.charAt(i) <= '9') {
                    current += (expr.charAt(i) - '0') * scale;
                    scale /= 10;
                    i++;
                }
                i--;
            } else {
                return "Error";
            }
        }
        result = apply(result, current, pendingOp, hasValue);

        if (Double.isNaN(result) || Double.isInfinite(result)) {
            return "Error";
        }
        // 整数则显示一位小数，与初始的 "0.0" 风格保持一致
        if (result == Math.rint(result) && Math.abs(result) < 1e15) {
            return String.format(java.util.Locale.US, "%.1f", result);
        }
        return String.valueOf(result);
    }

    private static double apply(double acc, double value, char op, boolean hasValue) {
        if (!hasValue) {
            // 第一个操作数之前没有运算符，直接作为结果
            return value;
        }
        switch (op) {
            case '+':
                return acc + value;
            case '\u2212': // −
                return acc - value;
            case '\u00D7': // ×
                return acc * value;
            case '\u00F7': // ÷
                return value == 0 ? Double.NaN : acc / value;
            default:
                return value;
        }
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '\u2212' || c == '\u00D7' || c == '\u00F7';
    }
}
