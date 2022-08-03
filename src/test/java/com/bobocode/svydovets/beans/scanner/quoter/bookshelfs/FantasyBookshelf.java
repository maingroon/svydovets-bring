package com.bobocode.svydovets.beans.scanner.quoter.bookshelfs;

import com.bobocode.svydovets.annotation.Bean;
import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.beans.scanner.quoter.books.DiscworldQuoter;
import com.bobocode.svydovets.beans.scanner.quoter.books.DuneQuoter;

//@Configuration()
public class FantasyBookshelf implements Bookshelf {

    @Bean("discworld")
    public DiscworldQuoter getDiscworldQuoter() {
        return new DiscworldQuoter();
    }

    @Bean
    public DuneQuoter getHarryPotterQuoter() {
        return new DuneQuoter();
    }

}
