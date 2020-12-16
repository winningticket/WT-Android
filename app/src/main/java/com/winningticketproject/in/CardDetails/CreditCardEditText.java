package com.winningticketproject.in.CardDetails;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by yekmer
 */
public class CreditCardEditText extends EditText {

    private CreditCardBaseTextWatcher mTextWatcher;

    public CreditCardEditText(Context context) {
        super(context);
    }

    public CreditCardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CreditCardEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setTextWatcher(CreditCardBaseTextWatcher textWatcher) {
        this.mTextWatcher = textWatcher;
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        super.addTextChangedListener(watcher);
        if(watcher instanceof CreditCardBaseTextWatcher) {
            CreditCardBaseTextWatcher creditCardBaseTextWatcher = (CreditCardBaseTextWatcher) watcher;
            setTextWatcher(creditCardBaseTextWatcher);
        }
    }
}
