package app.akexorcist.joystickcontroller;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import at.abraxas.amarino.Amarino;
import at.abraxas.amarino.AmarinoIntent;

public class Main extends Activity {
	RelativeLayout layout_joystick, layout_joystick_left;
	ImageView image_joystick, image_border;
	TextView textView1, textView2, textView3, textView4, textView5;
	private String DEVICE_ADDRESS = "98:D3:31:B1:77:84";
	private UiDialog UiDialogSetting;
	
	JoyStickClass js, js_left;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView3 = (TextView)findViewById(R.id.textView3);
        textView4 = (TextView)findViewById(R.id.textView4);
        textView5 = (TextView)findViewById(R.id.textView5);
        
	    layout_joystick = (RelativeLayout)findViewById(R.id.layout_joystick);

        js = new JoyStickClass(getApplicationContext()
        		, layout_joystick, R.drawable.image_button);
	    js.setStickSize(150, 150);
	    js.setLayoutSize(500, 500);
	    js.setLayoutAlpha(150);
	    js.setStickAlpha(100);
	    js.setOffset(90);
	    js.setMinimumDistance(50);
	    
	    layout_joystick.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				js.drawStick(arg1);
				if(arg1.getAction() == MotionEvent.ACTION_DOWN
						|| arg1.getAction() == MotionEvent.ACTION_MOVE) {
					textView1.setText("X right: " + String.valueOf(js.getX()));
					textView2.setText("Y right: " + String.valueOf(js.getY()));
					textView3.setText("Angle right: " + String.valueOf(js.getAngle()));
					textView4.setText("Distance right: " + String.valueOf(js.getDistance()));
					
					int direction = js.get8Direction();
					SendArgtoArduino_right(js.getY());
					if(direction == JoyStickClass.STICK_UP) 
					{
						textView5.setText("Direction right: Up");
					} else if(direction == JoyStickClass.STICK_UPRIGHT) {
						textView5.setText("Direction right: Up Right");
					} else if(direction == JoyStickClass.STICK_RIGHT) {
						textView5.setText("Direction right: Right");
					} else if(direction == JoyStickClass.STICK_DOWNRIGHT) {
						textView5.setText("Direction right: Down Right");
					} else if(direction == JoyStickClass.STICK_DOWN) {
						textView5.setText("Direction right: Down");
					} else if(direction == JoyStickClass.STICK_DOWNLEFT) {
						textView5.setText("Direction right: Down Left");
					} else if(direction == JoyStickClass.STICK_LEFT) {
						textView5.setText("Direction right: Left");
					} else if(direction == JoyStickClass.STICK_UPLEFT) {
						textView5.setText("Direction right: Up Left");
					} else if(direction == JoyStickClass.STICK_NONE) {
						textView5.setText("Direction right: Center");
					}
				} else if(arg1.getAction() == MotionEvent.ACTION_UP) {
					textView1.setText("X :");
					textView2.setText("Y :");
					textView3.setText("Angle :");
					textView4.setText("Distance :");
					textView5.setText("Direction :");
					SendArgtoArduino_right(0);
				}
				return true;
			}
        });
	    
	    layout_joystick_left = (RelativeLayout)findViewById(R.id.layout_joystick_left);

        js_left = new JoyStickClass(getApplicationContext()
        		, layout_joystick_left, R.drawable.image_button);
        js_left.setStickSize(150, 150);
        js_left.setLayoutSize(500, 500);
        js_left.setLayoutAlpha(150);
        js_left.setStickAlpha(100);
        js_left.setOffset(90);
        js_left.setMinimumDistance(50);
	    
	    layout_joystick_left.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				js_left.drawStick(arg1);
				if(arg1.getAction() == MotionEvent.ACTION_DOWN
						|| arg1.getAction() == MotionEvent.ACTION_MOVE) {
					textView1.setText("X left: " + String.valueOf(js_left.getX()));
					textView2.setText("Y left: " + String.valueOf(js_left.getY()));
					textView3.setText("Angle left: " + String.valueOf(js_left.getAngle()));
					textView4.setText("Distance left: " + String.valueOf(js_left.getDistance()));
					
					int direction = js_left.get8Direction();
					SendArgtoArduino_left(js_left.getY());
					if(direction == JoyStickClass.STICK_UP) 
					{
						textView5.setText("Direction left: Up");
					} else if(direction == JoyStickClass.STICK_UPRIGHT) {
						textView5.setText("Direction left: Up Right");
					} else if(direction == JoyStickClass.STICK_RIGHT) {
						textView5.setText("Direction left: Right");
					} else if(direction == JoyStickClass.STICK_DOWNRIGHT) {
						textView5.setText("Direction left: Down Right");
					} else if(direction == JoyStickClass.STICK_DOWN) {
						textView5.setText("Direction left: Down");
					} else if(direction == JoyStickClass.STICK_DOWNLEFT) {
						textView5.setText("Direction left: Down Left");
					} else if(direction == JoyStickClass.STICK_LEFT) {
						textView5.setText("Direction left: Left");
					} else if(direction == JoyStickClass.STICK_UPLEFT) {
						textView5.setText("Direction left: Up Left");
					} else if(direction == JoyStickClass.STICK_NONE) {
						textView5.setText("Direction left: Center");
					}
				} else if(arg1.getAction() == MotionEvent.ACTION_UP) {
					textView1.setText("X :");
					textView2.setText("Y :");
					textView3.setText("Angle :");
					textView4.setText("Distance :");
					textView5.setText("Direction :");
					SendArgtoArduino_left(0);
				}
				return true;
			}
        });
    }
    
    @Override
    protected void onStart(){
    	super.onStart();
    	
    	//SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	//DEVICE_ADDRESS = prefs.getString("DEVICE_ADDRESS", "NULL");
    	Amarino.connect(this, DEVICE_ADDRESS);

    }
    
    @Override
    protected void onStop() {
    	super.onStop();    	
    	// if you connect in onStart() you must not forget to disconnect when your app is closed
    	Amarino.disconnect(this, DEVICE_ADDRESS);

    }


    @Override
    protected void onResume(){
    	super.onResume();
    	Amarino.connect(this, DEVICE_ADDRESS);
    	
    }
    
    private void SendArgtoArduino_right(int y)
    {
    	if (y > 255)
    		y = 255;
    	else if (y < -255)
    		y = -255;
    	Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'A', y);
    }
    
    private void SendArgtoArduino_left(int y)
    {
    	if (y > 255)
    		y = 255;
    	else if (y < -255)
    		y = -255;
    	Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'B', y);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	MenuInflater inflater=getMenuInflater();
    	inflater.inflate(R.menu.more_tab_menu, menu);
    	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	UiDialogSetting.UiDialog_main(this, item.getItemId(), DEVICE_ADDRESS);
    	return true;
    }
}
