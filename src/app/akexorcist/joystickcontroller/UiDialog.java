package app.akexorcist.joystickcontroller;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;


public class UiDialog 
{
	
	private static final String TAG = "Dialog"; 
	private String DEVICE_ADDRESS;
	private String DEVICE_ADDRESS2;
	private int Threshold;
	
	private SeekBar ThresholdSeek;
	private TextView ThresholdText;
	private TextView FinishText;
	private TextView StartText;
	private Spinner  FinishSpinner;
	private Spinner  StartSpinner;
	private BluetoothDiscovery BTD;
	
	Activity MainActivity;
	
	public void UiDialog_main(Activity activity, int arg, String DEVICE_ADDRESS_formMain)
	{
		Log.i(TAG, "UiDialog_main arg:"+arg);
		MainActivity = activity;
		switch(arg)
		{
		    case R.id.setting:
		    	DEVICE_ADDRESS = DEVICE_ADDRESS_formMain;
		    	Setting_Dialog();
		    	break;
		    case R.id.about:
		    	Log.i(TAG, "UiDialog_main about");
		    	About_Dialog();
		    	break;
		}
	}
	
	private void About_Dialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity);
		builder.setTitle("About").setMessage("Writer: Arc Sung").show();		
	}
	
	private void Setting_Dialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity);
		builder.setTitle("Setting");
		
		// Setting_Dialog UI set up
		LinearLayout linear=new LinearLayout(MainActivity);
		linear.setOrientation(1);
								
		
	    ArrayList<String> spinnerArray = new ArrayList<String>();
	    spinnerArray.add(DEVICE_ADDRESS);
		BTD = new BluetoothDiscovery(MainActivity, spinnerArray);
	    //spinnerArray.add("98:D3:31:B1:77:84");
	    //spinnerArray.add("00:14:01:25:11:21");
	    spinnerArray = BTD.getBTAddress();
	    
	    FinishSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
	    {
	    	public void onItemSelected(AdapterView adapterView, View view, int position, long id)
	    	{
	    		DEVICE_ADDRESS = adapterView.getSelectedItem().toString();
	    	}
	    	
	    	public void onNothingSelected(AdapterView arg0){}; 
	    });
	    
	    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MainActivity, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
	    FinishSpinner.setAdapter(spinnerArrayAdapter);

	    linear.addView(FinishText);
	    linear.addView(FinishSpinner);


	    builder.setView(linear); 
				
		
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
		{
	        public void onClick(DialogInterface dialog, int id) 
	        {
	        	int flag = DEVICE_ADDRESS.compareTo(DEVICE_ADDRESS2);
	        	if (flag == 0)
	        	{
	        		DEVICE_ADDRESS  = "NULL";
	        	}		
	        }
	    });
		
	    builder.setNegativeButton("Clean", new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int id) 
	        {
	        	DEVICE_ADDRESS  = "NULL";
	        }
	    });
	    
	    AlertDialog setting_dialog = builder.create();
	    setting_dialog.show();
	}	
}