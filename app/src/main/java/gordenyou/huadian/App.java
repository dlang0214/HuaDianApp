package gordenyou.huadian;
import android.app.Application;
import android.content.Context;
import android.hardware.uhf.magic.DevBeep;
import android.hardware.uhf.magic.reader;

public class App extends Application {
	static String C5U = "/dev/ttyMT1";
	static String C7DU = "/dev/ttyMT2";
	static String CM550 = "/dev/ttyMT2";
	static String CM398M = "/dev/ttyMSM0";
	static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		InitUHF(C7DU);
		context=getApplicationContext();
	}

	/**
	 * Module initialization
	 * Scanning sound initialization
	 * @param type
	 */
	public void InitUHF(String type) {
		reader.init(type);
		reader.Open(type);
		reader.SetTransmissionPower(1950);
		DevBeep.init(App.this);
	}
	public static Context getAppContext() {
		return App.context;
	}

}
