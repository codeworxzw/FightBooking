package com.ebksoft.fightbooking.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Quản lý luồng (Thread) cho các request lấy dữ liệu bên ngoài Internet
 * 
 * @author Chau Minh Nhut(nhut.cm@felixvn.com)
 * 
 */
public class ThreadManager {

	public static final int PRIORITY_NORMAL = 0;

	public static final int PRIORITY_BLOCKING = 1;

	private static final Object mLock = new Object();

	private static ThreadManager mInstance;

	private static final int CORE_POOL_SIZE = 8;

	private static final int MAXIMUM_POOL_SIZE = 8;

	private static final int KEEP_ALIVE_TIME = 2;

	private static final TimeUnit KEEP_ALIVE_TIME_UNIT;

	private static final int MAX_QUEUE = 100;

	private static int NUMBER_OF_CORES = Runtime.getRuntime()
			.availableProcessors();

	private final BlockingQueue<Runnable> mNormalTaskQueue;

	private final ThreadPoolExecutor mTaskThreadPool;

	private final BlockingQueue<Runnable> mUrgentTaskQueue;

	private final ThreadPoolExecutor mUrgentTaskThreadPool;

	private Handler mUICallbackHandler;

	private Collection<Future<?>> futuresNormal = new LinkedList<Future<?>>();

	private Collection<Future<?>> futuresUrgent = new LinkedList<Future<?>>();

	static {
		KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

		mInstance = new ThreadManager();
	}

	public static ThreadManager getInstance() {
		synchronized (mLock) {
			return mInstance;
		}
	}

	private ThreadManager() {

		mNormalTaskQueue = new LinkedBlockingQueue<Runnable>(MAX_QUEUE);
		mTaskThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,
				MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
				mNormalTaskQueue);
		mTaskThreadPool.allowCoreThreadTimeOut(true);

		mUrgentTaskQueue = new LinkedBlockingQueue<Runnable>(MAX_QUEUE);
		mUrgentTaskThreadPool = new ThreadPoolExecutor(NUMBER_OF_CORES,
				NUMBER_OF_CORES, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
				mUrgentTaskQueue);
		mUrgentTaskThreadPool.allowCoreThreadTimeOut(true);
		mUrgentTaskThreadPool.setThreadFactory(new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setPriority(Thread.MAX_PRIORITY);
				return t;
			}
		});

		mUICallbackHandler = new Handler(Looper.getMainLooper());
	}

	/**
	 * Callback dữ liệu request từ internet
	 * 
	 * @param resultCallback
	 * @param result
	 * @param continueWaiting
	 */
	public <T extends Object> void callbackOnUIThread(
			final DataRequestCallback<T> resultCallback, final T result,
			final boolean continueWaiting) {
		mUICallbackHandler.post(new Runnable() {
			@Override
			public void run() {
				resultCallback.onResult(continueWaiting);
			}
		});
	}

//	public <T extends Object> void callbackOnUIThread(
//			final DBResultCallback<T> resultCallback, final T result,
//			final boolean continueWaiting) {
//		mUICallbackHandler.post(new Runnable() {
//			@Override
//			public void run() {
//				resultCallback.onResult(continueWaiting);
//			}
//		});
//	}

	public void execute(Runnable runnable) {
		execute(runnable, PRIORITY_NORMAL);
	}

	public void execute(Runnable runnable, int priority) {
		if (priority == PRIORITY_BLOCKING) {
			if (mUrgentTaskThreadPool.getActiveCount() < NUMBER_OF_CORES) {
				mUrgentTaskThreadPool.execute(runnable);
			} else {
				mTaskThreadPool.execute(runnable);
			}
		} else {
			mTaskThreadPool.execute(runnable);
		}
	}

	public void sunmit(Runnable runnable) {
		submit(runnable, PRIORITY_NORMAL);
	}

	public void submit(Runnable runnable, int priority) {
		if (priority == PRIORITY_BLOCKING) {
			if (mUrgentTaskThreadPool.getActiveCount() < NUMBER_OF_CORES) {
				futuresUrgent.add(mUrgentTaskThreadPool.submit(runnable));
			} else {
				futuresNormal.add(mTaskThreadPool.submit(runnable));
			}
		} else {
			futuresNormal.add(mTaskThreadPool.submit(runnable));
		}
	}

	public void cancel(Runnable runnable) {
		mTaskThreadPool.remove(runnable);
		mUrgentTaskThreadPool.remove(runnable);
	}

	public boolean isQueueEmpty(boolean isUrgent) {
		if (isUrgent) {
			return mUrgentTaskQueue.isEmpty();
		}
		return mNormalTaskQueue.isEmpty();
	}

	private Collection<Future<?>> getAllDoingTask(boolean isUrgent) {
		if (isUrgent)
			return futuresUrgent;
		return futuresNormal;
	}

	/**
	 * Kiem tra cac tat cac task dang chay da hoan thanh xong hay chua
	 * 
	 * @param isUrgent
	 * @return
	 */
	public boolean isDone(boolean isUrgent) {
		Collection<Future<?>> futures = mInstance.getAllDoingTask(isUrgent);
		boolean flag = true;
		for (Future<?> future : futures) {
			flag = flag & future.isDone();
		}
		if (flag) {
			if (isUrgent) {
				futuresUrgent.clear();
			} else {
				futuresNormal.clear();
			}
		}
		return flag;
	}
}
