package com.apextalos.cvitfusioncommon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class.getSimpleName());

    public int test() {
        logger.info("Commons INFO");
        return 69;
    }
}
