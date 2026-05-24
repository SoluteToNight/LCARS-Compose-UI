package com.lcars.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class LcarsTextTest {
    @Test
    fun lcarsLabel_usesStableUppercase() {
        assertEquals("SYS-SCAN", lcarsLabel("sys-scan"))
        assertEquals("INIT-I", lcarsLabel("init-i"))
    }
}
