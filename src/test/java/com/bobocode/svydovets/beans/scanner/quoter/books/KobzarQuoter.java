package com.bobocode.svydovets.beans.scanner.quoter.books;

import com.bobocode.svydovets.beans.scanner.quoter.Quoter;

public class KobzarQuoter implements Quoter {

    public KobzarQuoter() {
    }

    @Override
    public void quote() {
        System.out.println("V SVOII KHATI SVOIA Y PRAVDA, I SYLA, I VOLIA");
    }
}
