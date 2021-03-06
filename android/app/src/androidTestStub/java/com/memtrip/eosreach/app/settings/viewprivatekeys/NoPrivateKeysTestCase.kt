package com.memtrip.eosreach.app.settings.viewprivatekeys

import com.memtrip.eosreach.StubTestCase

class NoPrivateKeysTestCase : StubTestCase() {

    override fun test() {
        splashRobot
            .selectImportKey()
        importKeyRobot
            .selectSettingsButton()
        settingsRobot
            .verifySettingsScreen()
            .selectViewPrivateKeysSettingsItem()
            .verifyViewPrivateKeysScreen()
            .selectShowPrivateKeysButton()
            .verifyNoPrivateKeys()
    }
}