/*
 * Copyright 2008 Vitor Costa
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.code.jdde.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.google.code.jdde.ddeml.WinAPI;

/**
 * {@code MessageLoop} represents an abstraction to the Windows message loop
 * architecture. It listens continuously to native messages and transfers the
 * message to the associated callback procedure. It also centralizes native
 * calls to the DDE API, since they must run from the same thread that
 * registered the application with the Dynamic Data Exchange Management Library.
 * <p>
 * Once a message loop has been terminated, it cannot be started again.
 * 
 * @author Vitor Costa
 */
public class MessageLoop {

	/** Native id of this thread. **/
	private int threadId;
	
	/** The thread associated with this message loop. */
	private final Thread loopThread;

	/** Flag telling if this thread should terminate. */
	private boolean terminateRequested = false;

	/** List of tasks to be executed in the loop thread. */
	private final List<Runnable> pendingTasks;
	
	/** Last throwable caught in the loop thread */
	private Throwable lastThrowableCaught;

	/** The message used to wake up the loop thread. */
	private static int wakeUpMessage = 0x7FFF;

	/**
	 * Creates a new message loop and the associated thread. The thread is
	 * started by this constructor, making this object ready to be used.
	 */
	public MessageLoop() {
		pendingTasks = new ArrayList<Runnable>();

		loopThread = new Thread(new LoopThread());
		loopThread.start();
	}

	/**
	 * Returns the last throwable caught in the loop thread.
	 * 
	 * @return the last throwable caught in the loop thread.
	 */
	public Throwable getLastThrowableCaught() {
		return lastThrowableCaught;
	}

	/**
	 * Wakes up the loop thread by posting a native thread message.
	 */
	private void wakeUp() {
		if (threadId != 0) {
			WinAPI.PostThreadMessage(threadId, wakeUpMessage, 0, 0);
		}
	}
	
	/**
	 * Raises an internal flag that will result on this thread dying the next
	 * time it goes through the dispatch loop. The thread executes all pending
	 * tasks before dying.
	 */
	public void terminate() {
		terminateRequested = true;
		wakeUp();
	}

	/**
	 * Schedules the task for execution, wakes up the loop thread and returns
	 * immediately. As a result, {@code task.run()} will be executed
	 * asynchronously on the loop thread. This will happen after all pending
	 * tasks have been processed.
	 * 
	 * @param task
	 *            the task to be executed.
	 */
	public void invokeLater(final Runnable task) {
		synchronized (pendingTasks) {
			pendingTasks.add(task);
		}
		wakeUp();
	}

	/**
	 * Schedules the task for execution and blocks until all pending tasks have
	 * been processed. As a result, {@code doRun.run()} will be executed
	 * synchronously on the loop thread. This will happen after all pending
	 * tasks have been processed.
	 * 
	 * @param task
	 *            the task to be executed.
	 */
	public void invokeAndWait(final Runnable task) {
		if (Thread.currentThread() == loopThread) {
			// Inside the loop thread. No need to schedule execution.
			task.run();
		} else {
			// Used to deliver the notification that the task is executed.
			final CountDownLatch latch = new CountDownLatch(1);

			// Uses the invokeLater method with a newly created task.
			this.invokeLater(new Runnable() {
				public void run() {
					task.run();
					latch.countDown();
				}
			});
			// Wait for the task to complete.
			
			try {
				latch.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Executes all tasks queued for execution on the loop thread.
	 */
	private void executePendingTasks() {
		synchronized (pendingTasks) {
			for (int i = 0; i < pendingTasks.size(); i++) {
				Runnable task = pendingTasks.get(i);
				
				try {
					task.run();
				} catch (Throwable t) {
					lastThrowableCaught = t; 
					// Cannot let exceptions destroy the loop thread.
					// TODO: Create some kind of error handling.
				}
			}
			pendingTasks.clear();
		}
	}

	/**
	 * Actual implementation of the message loop thread.
	 */
	private class LoopThread implements Runnable {
		
		public void run() {
			threadId = WinAPI.GetCurrentThreadId();

			while (true) {
				// Execute all pending tasks.
				executePendingTasks();

				// Terminate if it was requested to.
				if (terminateRequested) {
					return;
				}

				try {
					// Wait on a native message loop.
					WinAPI.WaitOnMessageLoop(wakeUpMessage);
				} catch (Throwable t) {
					lastThrowableCaught = t;
					// Cannot let exceptions destroy the loop thread.
					// TODO: Create some kind of error handling.
				}
			}
		}
		
	}
	
}