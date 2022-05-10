package com.apextalos.cvitfusionclient.controllers;

import com.apextalos.cvitfusionclient.models.DiagramNodeModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiagramNodeControllerTest {
    @Test
    void name() {
        DiagramNodeController dnc = new DiagramNodeController();
        DiagramNodeModel model = dnc.getModel();
        assertNull(model);
    }
}