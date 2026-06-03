package com.stardust.autojs.core.web;

import android.content.Context;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.stardust.autojs.annotation.ScriptClass;
import com.stardust.autojs.annotation.ScriptInterface;

import org.mozilla.javascript.Scriptable;

/**
 * Created by Stardust on 2017/4/1.
 */
@ScriptClass
public class InjectableWebView extends WebView {

    private InjectableWebClient mInjectableWebClient;

    public InjectableWebView(Context context, org.mozilla.javascript.Context jsCtx, Scriptable scriptable) {
        super(context);
        init(jsCtx, scriptable);
    }

    private void init(org.mozilla.javascript.Context jsCtx, Scriptable scriptable) {
        mInjectableWebClient = new InjectableWebClient(jsCtx, scriptable);
        setWebViewClient(mInjectableWebClient);
    }

    @ScriptInterface
    public void inject(String script, ValueCallback<String> callback) {
        mInjectableWebClient.inject(script, callback);
    }

    @ScriptInterface
    public void inject(String script) {
        mInjectableWebClient.inject(script);
    }

    @Override
    protected void onDetachedFromWindow() {
        // WebView 销毁前先加载空白页释放渲染资源，再清理子视图，最后销毁
        loadUrl("about:blank");
        removeAllViews();
        destroy();
        super.onDetachedFromWindow();
    }
}
