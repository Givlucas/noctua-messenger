package com.messenger.noctua

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.messenger.data.AppViewModel
import com.messenger.msgServer.MsgServer
import com.messenger.tor.SampleApp
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MsgServerTests {
    @get:Rule
    val serviceRule = MsgServer()

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    val msg = "HELLO"
    val key = "aaa9402664f1a41f40ebbc52c9993eb66aeb366602958fdfaa283b71e64db123"

    @Test
    fun encrypt_decrypt(){
        val binder = serviceRule as MsgServer.LocalBinder
        val service: MsgServer = (binder as MsgServer.LocalBinder).getService()
        val encrypted = service.encrypt(msg, key)
        assert((msg == service.decrypt(encrypted, key)))
    }
}