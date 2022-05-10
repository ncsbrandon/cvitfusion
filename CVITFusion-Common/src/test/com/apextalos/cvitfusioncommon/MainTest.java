package com.apextalos.cvitfusioncommon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void test1() {
        Main main = new Main();
        assertEquals(69, main.test());
    }
}