package com.bobocode.svydovets.beans.scanner.quoter.services;

import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.KobzarQuoter;

public class QuoterService {

    private HarryPotterQuoter harryPotterQuoter;

    private KobzarQuoter kobzarQuoter;

    public QuoterService() {
    }

    public QuoterService(HarryPotterQuoter harryPotterQuoter, KobzarQuoter kobzarQuoter) {
        this.harryPotterQuoter = harryPotterQuoter;
        this.kobzarQuoter = kobzarQuoter;
    }
}