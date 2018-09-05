package com.memtrip.eosreach.app.transfer

import com.memtrip.eosreach.app.transfer.confirm.TransferConfirmActivity
import com.memtrip.eosreach.app.transfer.confirm.TransferConfirmActivityModule
import com.memtrip.eosreach.app.transfer.form.TransferFormActivity
import com.memtrip.eosreach.app.transfer.form.TransferFormActivityModule
import com.memtrip.eosreach.app.transfer.receipt.TransferReceiptActivity
import com.memtrip.eosreach.app.transfer.receipt.TransferReceiptActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class TransferModule {

    @ContributesAndroidInjector(modules = [TransferFormActivityModule::class])
    internal abstract fun contributeTransferActivity(): TransferFormActivity


    @ContributesAndroidInjector(modules = [TransferConfirmActivityModule::class])
    internal abstract fun contributeTransferConfirmActivity(): TransferConfirmActivity


    @ContributesAndroidInjector(modules = [TransferReceiptActivityModule::class])
    internal abstract fun contributeTransferReceiptActivity(): TransferReceiptActivity
}