package com.apextalos.cvitfusionclient.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiagramNodeControllerTest {
    @Test
    void name() {
        DiagramNodeController dnc = new DiagramNodeController();
        assertNull(dnc.getModel());
    }
}