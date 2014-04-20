package app.akexorcist.joystickcontroller;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import at.abraxas.amarino.Amarino;
import at.abraxas.amarino.AmarinoIntent;

public class Main extends Activity {
	RelativeLayout layout_joystick;
	ImageView image_joystick, image_border;
	TextView textView1, textView2, textView3, textView4, textView5;
	private String DEVICE_ADDRESS = "98:D3:31:B1:77:84";
	
	JoyStickClass js;
	
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
					textView1.setText("X : " + String.valueOf(js.getX()));
					textView2.setText("Y : " + String.valueOf(js.getY()));
					textView3.setText("Angle : " + String.valueOf(js.getAngle()));
					textView4.setText("Distance : " + String.valueOf(js.getDistance()));
					
					int direction = js.get8Direction();
					SendArgtoArduino(direction,(int)js.getDistance(), (int)js.getAngle());
					if(direction == JoyStickClass.STICK_UP) 
					{
						textView5.setText("Direction : Up");
					} else if(direction == JoyStickClass.STICK_UPRIGHT) {
						textView5.setText("Direction : Up Right");
					} else if(direction == JoyStickClass.STICK_RIGHT) {
						textView5.setText("Direction : Right");
					} else if(direction == JoyStickClass.STICK_DOWNRIGHT) {
						textView5.setText("Direction : Down Right");
					} else if(direction == JoyStickClass.STICK_DOWN) {
						textView5.setText("Direction : Down");
					} else if(direction == JoyStickClass.STICK_DOWNLEFT) {
						textView5.setText("Direction : Down Left");
					} else if(direction == JoyStickClass.STICK_LEFT) {
						textView5.setText("Direction : Left");
					} else if(direction == JoyStickClass.STICK_UPLEFT) {
						textView5.setText("Direction : Up Left");
					} else if(direction == JoyStickClass.STICK_NONE) {
						textView5.setText("Direction : Center");
					}
				} else if(arg1.getAction() == MotionEvent.ACTION_UP) {
					textView1.setText("X :");
					textView2.setText("Y :");
					textView3.setText("Angle :");
					textView4.setText("Distance :");
					textView5.setText("Direction :");
					SendArgtoArduino(0, 0, 0);
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
    
    private void SendArgtoArduino(int Direction, int Distance, int  Angle)
    {
    	if (Distance > 255)
    		Distance = 255;
    	int arg[] = {Direction, Distance, Angle};
    	Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'A', Distance);
    	Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'o', Direction);
    }
}
