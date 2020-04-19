package com.demo.base.loadsir;

import com.demo.base.R;
import com.kingja.loadsir.callback.Callback;

public class ErrorCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_error;
    }
}
