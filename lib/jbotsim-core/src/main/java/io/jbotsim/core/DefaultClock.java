/*
 * This file is part of JBotSim.
 *
 *    JBotSim is free software: you can redistribute it and/or modify it
 *    under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Authors:
 *    Arnaud Casteigts        <arnaud.casteigts@labri.fr>
 */

package io.jbotsim.core;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>The {@link DefaultClock} is JBotSim default implementation of the {@link Clock}.</p>
 *
 * <p>Implementation remarks:</p>
 * <ul>
 *     <li>It uses a {@link Thread} to perform the clock.</li>
 *     <li>The {@link Thread} will sleep for the specified time between each round.</li>
 *     <li>The {@link Thread} will keep the application alive until it is interrupted or the application is killed.</li>
 * </ul>
 *
 */
public class DefaultClock extends Clock implements Runnable {
    volatile boolean running;
    Thread timer;

    final Lock lock = new ReentrantLock();
    final Condition shouldRunCondition = lock.newCondition();

    private int delay = 0;

    public DefaultClock(ClockManager manager) {
        super(manager);
        running = false;
        timer = new Thread(this);
    }

    @Override
    public int getTimeUnit() {
        return delay;
    }

    @Override
    public void setTimeUnit(int delay) {
        this.delay = delay;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void start() {
        running = true;
        timer.start();
    }

    @Override
    public void pause() {
        running = false;

        // wait for the run loop actually to be paused
        lock.lock();
        lock.unlock();
    }

    @Override
    public void resume() {
        if(!running) {
            lock.lock();
            try {
                running = true;
                shouldRunCondition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void run() {
        boolean interrupted = false;

        while (!interrupted) {
            lock.lock();
            try {

                while (!running)
                    shouldRunCondition.await();

                if(delay != 0)
                    Thread.sleep(delay);

                if (running)
                    manager.onClock();

            } catch (InterruptedException e) {
                e.printStackTrace();
                interrupted = true;
            } finally {
                lock.unlock();
            }
        }
    }
}
