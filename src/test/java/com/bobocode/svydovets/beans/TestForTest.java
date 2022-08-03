package com.bobocode.svydovets.beans;

import com.bobocode.svydovets.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestForTest {

    @org.junit.jupiter.api.Test
    public void testReturnTwo() {
        var test = new Test();
        assertEquals(2, test.returnTwo());
    }

}
