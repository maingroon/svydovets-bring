package com.bobocode.svydovets.beans.scanner.quoter.bookshelfs;

import com.bobocode.svydovets.annotation.Bean;
import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.Kobzar;
import com.bobocode.svydovets.beans.scanner.quoter.books.KobzarQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.services.QuoterService;

@Configuration
public class UkrainianBooksShelf {

//    @Bean
//    public ZakharBerkut zakharBerkut(Quoter quoter) {
//        return new ZakharBerkut(quoter);
//    }

    @Bean
    public Kobzar kobzar(QuoterService quoterService) {
        return new Kobzar(quoterService);
    }

    @Bean
    public KobzarQuoter kobzarQuoter() {
        return new KobzarQuoter();
    }

    @Bean
    public QuoterService quoterService(HarryPotterQuoter harryPotterQuoter, KobzarQuoter kobzarQuoter) {
        return new QuoterService(harryPotterQuoter, kobzarQuoter);
    }
}
