/*
Copyright (C) 2018-present memtrip

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.memtrip.eosreach.app.account.balance

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.memtrip.eosreach.R
import com.memtrip.eosreach.api.balance.AccountBalanceList
import com.memtrip.eosreach.api.balance.ContractAccountBalance
import com.memtrip.eosreach.app.MviFragment
import com.memtrip.eosreach.app.ViewModelFactory
import com.memtrip.eosreach.app.account.AccountFragmentPagerAdapter
import com.memtrip.eosreach.app.account.AccountParentRefresh
import com.memtrip.eosreach.app.account.AccountTheme
import com.memtrip.eosreach.app.manage.ManageCreateAccountActivity.Companion.manageCreateAccountIntent
import com.memtrip.eosreach.uikit.Interaction
import com.memtrip.eosreach.uikit.gone
import com.memtrip.eosreach.uikit.visible
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.account_balance_fragment.*
import kotlinx.android.synthetic.main.account_balance_fragment.view.*
import javax.inject.Inject

abstract class BalanceFragment
    : MviFragment<BalanceIntent, BalanceRenderAction, BalanceViewState, BalanceViewLayout>(), BalanceViewLayout {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var render: BalanceViewRenderer

    private lateinit var adapter: AccountBalanceListAdapter
    private lateinit var accountName: String
    private lateinit var accountBalanceList: AccountBalanceList
    private lateinit var accountParentRefresh: AccountParentRefresh

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        accountParentRefresh = context as AccountParentRefresh
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.account_balance_fragment, container, false)

        accountName = accountName(arguments!!)
        accountBalanceList = accountBalanceListExtra(arguments!!)

        val adapterInteraction: PublishSubject<Interaction<ContractAccountBalance>> = PublishSubject.create()
        adapter = AccountBalanceListAdapter(context!!, adapterInteraction)
        view.balance_list_recyclerview.adapter = adapter

        return view
    }

    override fun intents(): Observable<BalanceIntent> = Observable.merge(
        Observable.just(BalanceIntent.Init(accountBalanceList)),
        RxView.clicks(balance_airdrop_button).map { BalanceIntent.ScanForAirdropTokens(accountName) },
        RxView.clicks(balance_create_account).map { BalanceIntent.NavigateToCreateAccount },
        adapter.interaction.map { BalanceIntent.NavigateToActions(it.data) }
    )

    override fun layout(): BalanceViewLayout = this

    override fun model(): BalanceViewModel = getViewModel(viewModelFactory)

    override fun render(): BalanceViewRenderer = render

    override fun showBalances(balances: List<ContractAccountBalance>) {
        balance_list_group.visible()
        balance_airdrop_progress_group.gone()
        adapter.clear()
        adapter.populate(balances)
    }

    override fun showEmptyBalance() {
        balance_list_group.gone()
        balance_airdrop_progress_group.gone()
        balance_empty_group.visible()
    }

    override fun showAirdropError(message: String) {

        model().publish(BalanceIntent.Idle)

        AlertDialog.Builder(context!!)
            .setMessage(message)
            .setPositiveButton(R.string.app_dialog_positive_button, null)
            .create()
            .show()
    }

    override fun showAirdropProgress() {
        balance_empty_group.gone()
        balance_list_group.gone()
        balance_airdrop_progress_group.visible()
    }

    override fun showAirdropSuccess() {
        accountParentRefresh.triggerRefresh(AccountFragmentPagerAdapter.Page.BALANCE)
    }

    override fun navigateToCreateAccount() {
        model().publish(BalanceIntent.Idle)
        startActivity(manageCreateAccountIntent(context!!))
    }

    companion object {

        private const val ACCOUNT_NAME = "ACCOUNT_NAME"
        private const val ACCOUNT_BALANCES = "ACCOUNT_BALANCES"

        private fun accountBalanceListExtra(bundle: Bundle): AccountBalanceList =
            bundle.getParcelable(ACCOUNT_BALANCES)

        private fun accountName(bundle: Bundle): String =
            bundle.getString(ACCOUNT_NAME)

        fun newInstance(
            accountName: String,
            accountBalances: AccountBalanceList,
            accountTheme: AccountTheme
        ): BalanceFragment {
            return when (accountTheme) {
                AccountTheme.DEFAULT -> {
                    with(DefaultBalanceFragment()) {
                        arguments = with(Bundle()) {
                            putString(ACCOUNT_NAME, accountName)
                            putParcelable(ACCOUNT_BALANCES, accountBalances)
                            this
                        }
                        this
                    }
                }
                AccountTheme.READ_ONLY -> {
                    with(ReadOnlyBalanceFragment()) {
                        arguments = with(Bundle()) {
                            putString(ACCOUNT_NAME, accountName)
                            putParcelable(ACCOUNT_BALANCES, accountBalances)
                            this
                        }
                        this
                    }
                }
            }
        }
    }
}
