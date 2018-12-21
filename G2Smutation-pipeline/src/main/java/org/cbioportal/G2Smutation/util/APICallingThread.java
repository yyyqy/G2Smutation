package org.cbioportal.G2Smutation.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.cbioportal.G2Smutation.scripts.PdbScriptsPipelineMakeSQL;


public class APICallingThread extends Thread{
    final static Logger log = Logger.getLogger(PdbScriptsPipelineMakeSQL.class);

    private Thread t;
    private String threadName;
    APICallingAgent agent;
    List<String> snpIds;
    int agentNo;

    public APICallingThread(String name, APICallingAgent ag, List<String> snpIdsInput, int agentNoInput) {
        threadName = name;
        agent = ag;
        snpIds = snpIdsInput;
        agentNo = agentNoInput;        
    }

    @Override
    public void run() {
        synchronized (agent) {
            agent.work(snpIds, agentNo);
        }
        log.info("Thread " + threadName + " exiting.");
    }

    @Override
    public void start() {
        log.info("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

}

