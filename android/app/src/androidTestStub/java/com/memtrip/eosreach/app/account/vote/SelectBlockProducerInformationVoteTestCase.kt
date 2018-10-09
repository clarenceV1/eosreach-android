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
package com.memtrip.eosreach.app.account.vote

import com.memtrip.eosreach.StubTestCase

class SelectBlockProducerInformationVoteTestCase : StubTestCase() {

    override fun test() {
        importKeyOrchestra.go("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3")
        accountRobot
            .verifyAccountScreen()
            .verifyAvailableBalance()
            .selectVoteTab()
        voteRobot
            .verifyNotVotedScreen()
            .selectVoteForProducers()
            .verifyVoteForProducersScreen()
            .selectAddBlockProducerFromList()
        blockProducerRobot
            .verifyBlockProducerListScreen()
            .verifyBlockProducerListFirstAccountRow()
            .selectBlockProducerListFirstAccountRowInformation()
            .verifyViewBlockProducerInformationScreen()
            .selectViewOwnerAccount()
        accountRobot
            .verifyAccountScreen()
    }
}