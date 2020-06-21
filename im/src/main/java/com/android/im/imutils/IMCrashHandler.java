
/**
 * 系统项目名称
 * CrashHandler.java
 * 
 */

package com.android.im.imutils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * lcy
 *
 * CrashHandler
 * 
 * @version 1.0.0
 * 
 */

public class IMCrashHandler implements UncaughtExceptionHandler {

	private static final String TAG = "CrashHandler";

	// 系统默认的UncaughtException处理类
	private UncaughtExceptionHandler defaultHandler;

	private static IMCrashHandler instance;

	// private DateFormat formatter = new
	// SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	private static final ExecutorService THREAD_POOL = Executors.newSingleThreadExecutor();

	private Future<?> future;

	// private List<CrashListener> mListeners;

	protected Context ctx;

	private IMCrashHandler() {
		// mListeners = new ArrayList<CrashListener>();
	}

	public synchronized static IMCrashHandler getInstance() {
		if (instance == null) {
			instance = new IMCrashHandler();
		}
		return instance;
	}

	public void init(Context ctx) {
		this.ctx = ctx;
		defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	// public void addListener(CrashListener listener){
	// if (listener != null) {
	// mListeners.add(listener);
	// }
	// }

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// Log.e(TAG, "uncaughtException, thread: " + thread + " name: " +
		// thread.getName() + " id: " + thread.getId() + " exception: " + ex);

		Log.i(TAG, Log.getStackTraceString(ex));

		// final String msg = Log.getStackTraceString(ex);
		//
		// writeLogMsg(msg);

		// future = THREAD_POOL.submit(new Runnable() {
		// public void run() {
		// if (mListeners.size() > 0) {
		// for (int i = 0; i < mListeners.size(); i++) {
		// mListeners.get(i).afterSaveCrash(msg, new File(ctx.getFilesDir(),
		// "crash.log"));
		// }
		// }
		// };
		// });
		// if (!future.isDone()) {
		// try {
		// future.get();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }

//		defaultHandler.uncaughtException(thread, ex);
	}

	protected void writeLogMsg(String msg) {
		// File file = ctx.getFilesDir();
		// if (!file .exists() && !file .isDirectory())
		// {
		// file .mkdirs();
		// }
		// file = new File(ctx.getFilesDir(), "crash.log");

		File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "logcatwyx.txt");

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		SimpleDateFormat timeFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault());
		String time = timeFormat.format(Calendar.getInstance().getTime());
		synchronized (file) {
			FileWriter fileWriter = null;
			BufferedWriter buffWriter = null;
			PrintWriter printWriter = null;
			try {
				fileWriter = new FileWriter(file, true);
				buffWriter = new BufferedWriter(fileWriter);
				printWriter = new PrintWriter(fileWriter);
				buffWriter.append(time).append(" ").append("error").append('/').append("CrashHandler").append(" ")
						.append(msg).append('\n');
				buffWriter.flush();
				// tr.printStackTrace(printWriter);
				printWriter.flush();
				fileWriter.flush();
			} catch (IOException e) {
			} finally {
				try {
					printWriter.close();
					buffWriter.close();
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
