package com.yahs.king.robot;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.yahs.king.robot.utils.NetUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.yahs.king.robot", appContext.getPackageName());
        String res = NetUtils.post("给我讲个笑话");
        Log.e("TAG", res);
        res = NetUtils.post("给我讲个鬼故事");
        Log.e("TAG", res);
        res = NetUtils.post("你好");
        Log.e("TAG", res);
        res = NetUtils.post("你真美");
        Log.e("TAG", res);


    }


}
