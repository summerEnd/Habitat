package com.sumauto.common;


import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.sp.lib.R;
import com.sumauto.util.ToastUtil;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * 对TextView中的text进行检验
 */
@SuppressWarnings("unused")
public class LoginManager {
    private ArrayList<ICheck> checks = new ArrayList<>();

    /**
     * 对textView字符的长度进行检验
     *
     * @param tv         目标TextView
     * @param minLength  长度最小值
     * @param maxLength  长度最大值
     * @param failNotice 失败文字
     */
    public LoginManager addLengthCheck(TextView tv, int minLength, int maxLength, String failNotice) {
        return add(new LengthCheck(tv, minLength, maxLength, failNotice));
    }

    /**
     * 与{@link LoginManager#addEmptyCheck(TextView, String) addEmptyCheck(TextView,String)}相同，用TextView的hint作为failNotice
     *
     * @see LoginManager#addEmptyCheck(TextView, String)
     */
    public LoginManager addEmptyCheck(TextView textView) {
        return addEmptyCheck(textView, textView.getHint().toString());
    }

    /**
     * @see EmptyCheck
     */
    public LoginManager addEmptyCheck(TextView textView, String failNotice) {
        return add(new EmptyCheck(textView, failNotice));

    }

    /**
     * 对两个textView字符是否相等进行检验
     *
     * @param textView   目标TextView，如果检测不通过，这个textView将产生动画效果
     * @param another    另一个TextView
     * @param failNotice 失败文字
     */
    public LoginManager addEqualCheck(TextView textView, TextView another, String failNotice) {

        return add(new EqualCheck(textView, another, failNotice));
    }


    public LoginManager addPatterCheck(TextView tv, String pattern, String failNotice) {
        return add(new PatternCheck(tv, pattern, failNotice));
    }

    public LoginManager add(ICheck check) {
        checks.add(check);
        return this;
    }

    /**
     * @return true 测试通过,false 不通过
     */
    public boolean start() {
        for (ICheck check : checks) {
            if (!check.doCheck()) {
                return false;
            }
        }
        return true;
    }


    /**
     * Created by user1 on 2015/5/14.
     */
    public static class EmptyCheck extends TextViewCheck {

        public EmptyCheck(TextView textView, String failNotice) {
            super(textView, failNotice);
        }

        @Override
        protected boolean onCheck(TextView textView) {
            return !TextUtils.isEmpty(textView.getText().toString());
        }
    }

    /**
     * Created by user1 on 2015/5/14.
     */
    public static class EqualCheck extends TextViewCheck {
        TextView another;

        /**
         * 对两个textView字符是否相等进行检验
         *
         * @param textView   目标TextView，如果检测不通过，这个textView将产生动画效果
         * @param another    另一个TextView
         * @param failNotice 失败文字
         */
        public EqualCheck(TextView textView, TextView another, String failNotice) {
            super(textView, failNotice);
            this.another = another;
        }

        @Override
        protected boolean onCheck(TextView textView) {
            return textView.getText().toString().equals(another.getText().toString());
        }
    }

    /**
     * Created by user1 on 2015/5/14.
     */
    public static class LengthCheck extends TextViewCheck {
        int start;
        int end;

        /**
         * 对textView字符的长度进行检验
         *
         * @param textView 目标TextView
         * @param start    长度最小值
         * @param end      长度最大值
         */
        public LengthCheck(TextView textView, int start, int end, String failNotice) {
            super(textView, failNotice);
            this.start = start;
            this.end = end;
        }

        @Override
        protected boolean onCheck(TextView textView) {
            String text = textView.getText().toString();
            return text.length() >= start && text.length() <= end;
        }
    }

    /**
     * Created by user1 on 2015/5/14.
     */
    public static class PatternCheck extends TextViewCheck {
        String pattern;

        /**
         * 正则检验
         *
         * @param textView   目标TextView
         * @param pattern    匹配的正则表达式
         * @param failNotice 失败文字
         */
        public PatternCheck(TextView textView, String pattern, String failNotice) {
            super(textView, failNotice);
            this.pattern = pattern;
        }


        @Override
        protected boolean onCheck(TextView textView) {
            Pattern p = Pattern.compile(pattern);
            return p.matcher(textView.getText().toString()).matches();
        }
    }

    public abstract static class TextViewCheck implements ICheck {
        protected TextView  textView;
        protected Animation shake;
        private   String    failNotice;

        public TextViewCheck(TextView textView) {
            this(textView, textView.getHint().toString());
        }

        public TextViewCheck(TextView textView, String failNotice) {
            this.textView = textView;
            this.failNotice = failNotice;
        }

        @Override
        public final boolean doCheck() {
            if (!onCheck(textView)) {
                if (shake == null) {
                    shake = AnimationUtils.loadAnimation(textView.getContext(), R.anim.shake);
                }
                textView.startAnimation(shake);
                textView.requestFocus();
                ToastUtil.toast(failNotice);
                return false;
            }
            return true;
        }

        protected abstract boolean onCheck(TextView textView);
    }

    public interface ICheck {
        boolean doCheck();
    }
}
