package com.memtrip.eosreach.ochestra

class ImportKeyOrchestra : Orchestra() {

    fun go() {
        splashRobot
            .selectImportKey()
        importKeyRobot
            .typePrivateKey("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3")
            .selectImportButton()
    }
}